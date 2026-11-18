package com.example.model

data class KaliTool(
    val id: String,
    val name: String,
    val category: String, // "Information Gathering", "Vulnerability Analysis", "Wireless Attacks", "Exploitation", "Password Attacks", "Sniffing & Spoofing", "Web Apps", "Post Exploitation"
    val description: String,
    val defaultCommand: String,
    val riskLevel: String, // "CRITICAL", "HIGH", "MEDIUM", "LOW"
    val isRunning: Boolean = false
)

data class ScanTarget(
    val id: String = java.util.UUID.randomUUID().toString(),
    val target: String,
    val port: String,
    val status: String, // "VULNERABLE", "SECURE", "WARNING"
    val findings: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class VpnState(
    val isConnected: Boolean = false,
    val ipAddress: String = "192.168.1.105 (Spoofed)",
    val locationNode: String = "Zurich, Switzerland (Node #402)",
    val protocol: String = "ShadowTLS-V2 Custom Obfuscation",
    val killSwitchEnabled: Boolean = true,
    val macSpoofed: String = "02:4B:99:A1:7C:F2",
    val imeiSpoofed: String = "864290041839210",
    val timeSpoofed: Boolean = true,
    val firewallBypassActive: Boolean = true
)

data class AiMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: String, // "user" or "hemant_ai"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class CloudReport(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val target: String,
    val severity: String,
    val summary: String,
    val date: String,
    val synced: Boolean = true
)
