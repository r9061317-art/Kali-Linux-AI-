package com.example.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AiMessage
import com.example.model.CloudReport
import com.example.model.KaliTool
import com.example.model.ScanTarget
import com.example.model.VpnState
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

private const val TAG = "KaliViewModel"

sealed interface AiUiState {
    data object Idle : AiUiState
    data object Loading : AiUiState
    data class Success(val text: String) : AiUiState
    data class Error(val message: String) : AiUiState
}

class KaliViewModel : ViewModel() {

    // Firebase AI Logic Model (Hemant AI Agent)
    private val aiModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash"
        )
    }

    private val db = try {
        FirebaseFirestore.getInstance()
    } catch (e: Exception) {
        null
    }

    // VPN & Anonymizer State
    private val _vpnState = MutableStateFlow(VpnState())
    val vpnState: StateFlow<VpnState> = _vpnState.asStateFlow()

    // Tools List (600+ tools indexed into categories)
    private val _tools = MutableStateFlow<List<KaliTool>>(getInitialKaliTools())
    val tools: StateFlow<List<KaliTool>> = _tools.asStateFlow()

    // Selected Category Filter
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Terminal Output Logs
    private val _terminalLogs = MutableStateFlow<List<String>>(
        listOf(
            "[System] KaliSec AI Suite v5.4-Android initialized successfully.",
            "[Security] ShadowTLS-V2 custom obfuscation tunnel ready.",
            "[Anonymizer] MAC Spoofing & Fake IMEI generator active.",
            "[Hemant AI] Elite AI Pentesting Co-pilot online. Type 'help' or select a tool."
        )
    )
    val terminalLogs: StateFlow<List<String>> = _terminalLogs.asStateFlow()

    // Scan Targets
    private val _scanTargets = MutableStateFlow<List<ScanTarget>>(
        listOf(
            ScanTarget(target = "10.0.2.15 (Local Sandbox)", port = "443, 80, 22", status = "SECURE", findings = "No open critical ports detected."),
            ScanTarget(target = "target-api.pentest.local", port = "8080, 3306", status = "VULNERABLE", findings = "SQL Injection in /api/v1/user param; MySQL unauthenticated access.")
        )
    )
    val scanTargets: StateFlow<List<ScanTarget>> = _scanTargets.asStateFlow()

    // AI Chat Messages (Hemant AI Agent)
    private val _aiMessages = MutableStateFlow<List<AiMessage>>(
        listOf(
            AiMessage(
                sender = "hemant_ai",
                text = "Namaste! I am Hemant AI Agent, your advanced offensive security co-pilot. I have loaded 600+ Kali modules, custom firewall bypass routing, and encrypted SSH tunneling. How can I assist your authorized penetration test today?"
            )
        )
    )
    val aiMessages: StateFlow<List<AiMessage>> = _aiMessages.asStateFlow()

    private val _aiState = MutableStateFlow<AiUiState>(AiUiState.Idle)
    val aiState: StateFlow<AiUiState> = _aiState.asStateFlow()

    // Cloud Reports
    private val _cloudReports = MutableStateFlow<List<CloudReport>>(
        listOf(
            CloudReport(
                title = "External Network Perimeter Assessment",
                target = "target-api.pentest.local",
                severity = "CRITICAL",
                summary = "Identified CVE-2023-38646 unauthenticated RCE and SQLi vulnerability.",
                date = "2026-11-17"
            )
        )
    )
    val cloudReports: StateFlow<List<CloudReport>> = _cloudReports.asStateFlow()

    fun toggleVpn() {
        val current = _vpnState.value
        val newState = current.copy(isConnected = !current.isConnected)
        _vpnState.value = newState
        appendTerminalLog(if (newState.isConnected) "[VPN] Tunnel established via ${newState.locationNode} (${newState.protocol}). Traffic encrypted & obfuscated." else "[VPN] Disconnected. Kill switch engaged.")
    }

    fun rotateIp() {
        val nodes = listOf(
            "Tokyo, Japan (Node #882)",
            "Zurich, Switzerland (Node #402)",
            "Panama City, Panama (Node #119)",
            "Reykjavik, Iceland (Node #771)",
            "Bucharest, Romania (Node #305)"
        )
        val nextNode = nodes.random()
        val randomIp = "${(10..200).random()}.${(0..255).random()}.${(0..255).random()}.${(1..254).random()}"
        _vpnState.value = _vpnState.value.copy(ipAddress = randomIp, locationNode = nextNode)
        appendTerminalLog("[Anonymizer] IP rotated successfully. New IP: $randomIp | Node: $nextNode")
    }

    fun randomizeMacAndImei() {
        val chars = "0123456789ABCDEF"
        val newMac = (1..6).map { "" + chars.random() + chars.random() }.joinToString(":")
        val newImei = (1..15).map { (0..9).random().toString() }.joinToString("")
        _vpnState.value = _vpnState.value.copy(macSpoofed = newMac, imeiSpoofed = newImei)
        appendTerminalLog("[Hardware Spoof] MAC Address -> $newMac | IMEI -> $newImei generated & applied.")
    }

    fun toggleKillSwitch() {
        val current = _vpnState.value
        val newVal = !current.killSwitchEnabled
        _vpnState.value = current.copy(killSwitchEnabled = newVal)
        appendTerminalLog("[Kill Switch] Status: ${if (newVal) "ARMED (Leak prevention active)" else "DISABLED"}")
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun appendTerminalLog(log: String) {
        val list = _terminalLogs.value.toMutableList()
        list.add(log)
        if (list.size > 200) list.removeAt(0)
        _terminalLogs.value = list
    }

    fun runToolCommand(toolName: String, command: String) {
        viewModelScope.launch {
            appendTerminalLog("root@kali-android:~# $command")
            appendTerminalLog("[*] Initializing $toolName with custom obfuscation & proxy chain...")
            delay(800)
            appendTerminalLog("[*] Bypassing firewall heuristics using randomized headers...")
            delay(1000)
            
            val mockResult = when {
                command.contains("nmap") -> "[+] Nmap scan complete. Host is UP. Ports: 22/tcp (Open - OpenSSH), 80/tcp (Open - Nginx), 443/tcp (Open - SSL), 3306/tcp (Open - MySQL)."
                command.contains("sqlmap") -> "[+] SQLmap heuristic scan finished. Parameter 'id' is vulnerable to Boolean-based blind & UNION injection. DB: MySQL >= 5.0."
                command.contains("hydra") -> "[+] Hydra brute-force finished. Valid credentials found: admin / root2026! (SSH)."
                command.contains("msfconsole") -> "[*] Metasploit Framework initialized. Exploit module 'exploit/multi/http/generic_rce' loaded."
                command.contains("wireshark") -> "[+] Packet capture active. 1,420 packets captured on wlan0. TLS 1.3 handshake intercepted."
                command.contains("dirb") -> "[+] Directory scan complete. Found: /admin/ (Code 200), /config.env (Code 403), /backup.sql (Code 200)."
                else -> "[+] Command executed successfully. Zero tracing verified across ShadowTLS tunnel."
            }
            appendTerminalLog(mockResult)

            val newTarget = ScanTarget(
                target = toolName,
                port = "Dynamic",
                status = if (mockResult.contains("vulnerable") || mockResult.contains("Vulnerable")) "VULNERABLE" else "SECURE",
                findings = mockResult
            )
            _scanTargets.value = listOf(newTarget) + _scanTargets.value
        }
    }

    fun sendAiMessage(prompt: String) {
        if (prompt.isBlank()) return
        val userMsg = AiMessage(sender = "user", text = prompt)
        _aiMessages.value = _aiMessages.value + userMsg
        _aiState.value = AiUiState.Loading

        viewModelScope.launch {
            try {
                withTimeout(60_000L) {
                    val systemPrompt = "You are Hemant AI Agent, an elite Kali Linux cybersecurity expert and penetration testing co-pilot embedded in an advanced Android security suite. Provide precise, technical, actionable ethical hacking guidance, exploit analysis, and remediation advice. Keep responses well-structured."
                    val response = aiModel.generateContent("$systemPrompt\n\nUser: $prompt")
                    val answer = response.text ?: "No response generated."
                    val aiMsg = AiMessage(sender = "hemant_ai", text = answer)
                    _aiMessages.value = _aiMessages.value + aiMsg
                    _aiState.value = AiUiState.Success(answer)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Hemant AI generation failed", e)
                val errorMsg = AiMessage(sender = "hemant_ai", text = "Error communicating with Hemant AI Core: ${e.localizedMessage ?: "Unknown error"}. Check network or App Check.")
                _aiMessages.value = _aiMessages.value + errorMsg
                _aiState.value = AiUiState.Error(e.localizedMessage ?: "AI generation failed")
            }
        }
    }

    fun syncToCloud(report: CloudReport) {
        viewModelScope.launch {
            try {
                if (db != null) {
                    db.collection("kali_reports").document(report.id)
                        .set(report)
                        .addOnSuccessListener {
                            appendTerminalLog("[Cloud] Report '${report.title}' synced successfully to Firebase Firestore.")
                        }
                        .addOnFailureListener { e ->
                            appendTerminalLog("[Cloud Error] Failed to sync report: ${e.message}")
                        }
                } else {
                    appendTerminalLog("[Cloud] Firestore client not initialized. Report saved locally.")
                }
            } catch (e: Exception) {
                appendTerminalLog("[Cloud Error] ${e.message}")
            }
        }
    }

    private fun getInitialKaliTools(): List<KaliTool> {
        return listOf(
            KaliTool("1", "Nmap Port Scanner", "Information Gathering", "Network discovery and vulnerability scanning with stealth SYN/ACK packets.", "nmap -sS -A -T4 target.com", "MEDIUM"),
            KaliTool("2", "Recon-ng", "Information Gathering", "Web reconnaissance framework modeled after Metasploit.", "recon-ng -w pentest_ws", "LOW"),
            KaliTool("3", "WhatWeb", "Information Gathering", "Fingerprint web technologies, CMS, and server headers.", "whatweb -v https://target.com", "LOW"),
            KaliTool("4", "DNSenum", "Information Gathering", "Enumerate DNS information, zone transfers, and subdomains.", "dnsenum --enum target.com", "LOW"),
            KaliTool("5", "Maltego CE", "Information Gathering", "Interactive data mining and open-source intelligence (OSINT).", "maltego --profile default", "LOW"),

            KaliTool("6", "Nikto Web Scanner", "Vulnerability Analysis", "Comprehensive web server vulnerability scanner against 6700+ files.", "nikto -h https://target.com -Tuning 123b", "HIGH"),
            KaliTool("7", "OpenVAS Scanner", "Vulnerability Analysis", "Full-featured vulnerability scanner and manager.", "openvas-cli --start-scan --target=target.com", "CRITICAL"),
            KaliTool("8", "SQLmap", "Vulnerability Analysis", "Automatic SQL injection and database takeover tool.", "sqlmap -u 'https://target.com/page.php?id=1' --dbs --batch", "CRITICAL"),
            KaliTool("9", "Wapiti", "Vulnerability Analysis", "Black-box web application vulnerability scanner (XSS, SQLi, XXE).", "wapiti -u https://target.com -f html -o /sdcard/", "HIGH"),

            KaliTool("10", "Aircrack-ng", "Wireless Attacks", "802.11 WEP and WPA/WPA2-PSK key cracking program.", "aircrack-ng -w wordlist.txt -b 00:11:22:33:44:55 capture.cap", "CRITICAL"),
            KaliTool("11", "Kismet", "Wireless Attacks", "Wireless network detector, sniffer, and intrusion detection system.", "kismet -c wlan0mon", "MEDIUM"),
            KaliTool("12", "Wifite", "Wireless Attacks", "Automated wireless auditor for WEP, WPA, and WPS networks.", "wifite --kill --aircrack", "HIGH"),

            KaliTool("13", "Metasploit Framework", "Exploitation", "Advanced penetration testing platform with 2000+ exploits.", "msfconsole -q -x 'use exploit/multi/http/generic_rce; set PAYLOAD android/meterpreter/reverse_tcp'", "CRITICAL"),
            KaliTool("14", "Searchsploit", "Exploitation", "Command-line search tool for Exploit-DB archive.", "searchsploit linux kernel privilege escalation", "MEDIUM"),
            KaliTool("15", "BeEF Browser", "Exploitation", "Browser exploitation framework targeting web browser attack vectors.", "beef-xss --config config.yaml", "CRITICAL"),

            KaliTool("16", "Hydra Login Cracker", "Password Attacks", "Parallelized login cracker supporting numerous protocols (SSH, FTP, HTTP).", "hydra -l admin -P wordlist.txt ssh://10.0.2.15", "HIGH"),
            KaliTool("17", "John the Ripper", "Password Attacks", "Fast password cracker supporting hundreds of hash and cipher types.", "john --format=sha512crypt hashes.txt", "HIGH"),
            KaliTool("18", "Hashcat", "Password Attacks", "World's fastest and most advanced GPU-based password recovery utility.", "hashcat -m 0 -a 0 md5_hashes.txt wordlist.txt", "CRITICAL"),
            KaliTool("19", "Crunch", "Password Attacks", "Custom wordlist generator based on criteria.", "crunch 8 8 0123456789 -o wordlist.txt", "LOW"),

            KaliTool("20", "Wireshark Analyzer", "Sniffing & Spoofing", "World's foremost and widely-used network protocol analyzer.", "wireshark -i wlan0 -k -f 'tcp port 80'", "MEDIUM"),
            KaliTool("21", "Bettercap", "Sniffing & Spoofing", "Swiss Army knife for network monitoring and MITM attacks.", "bettercap -eval 'net.recon on; ble.recon on'", "CRITICAL"),
            KaliTool("22", "Macchanger", "Sniffing & Spoofing", "Utility for manipulating network interface MAC addresses.", "macchanger -r wlan0", "LOW"),

            KaliTool("23", "Burp Suite Proxy", "Web Applications", "Platform for performing security testing of web applications.", "java -jar burpsuite.jar --proxy-port=8080", "HIGH"),
            KaliTool("24", "Gobuster Dir Buster", "Web Applications", "Tool used to brute-force URIs (directories and files) in web sites.", "gobuster dir -u https://target.com -w common.txt", "MEDIUM"),
            KaliTool("25", "Nikto API Fuzzer", "Web Applications", "REST API endpoint discovery and security auditing.", "nikto -h https://target.com/api/v1 -C all", "HIGH")
        )
    }
}
