package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.viewmodel.KaliViewModel

@Composable
fun DashboardScreen(viewModel: KaliViewModel, onNavigateToTerminal: () -> Unit) {
    val vpnState by viewModel.vpnState.collectAsState()
    val scanTargets by viewModel.scanTargets.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(KaliDarkBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Header Banner
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "KALISEX AI SUITE v5.4",
                        color = KaliGreen,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Secure Pentesting & Reconnaissance Core",
                        color = KaliTextSecondary,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
                StatusBadge(
                    text = if (vpnState.isConnected) "SECURE TUNNEL" else "VULNERABLE",
                    color = if (vpnState.isConnected) KaliGreen else KaliRed
                )
            }
        }

        item {
            // VPN & Stealth Anonymizer Card
            CyberCard(borderColor = if (vpnState.isConnected) KaliGreen else KaliYellow) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "SHADOW-TLS OBFUSCATION VPN",
                            color = KaliTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Node: ${vpnState.locationNode}",
                            color = KaliCyan,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Spoofed IP: ${vpnState.ipAddress}",
                            color = KaliTextSecondary,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Switch(
                        checked = vpnState.isConnected,
                        onCheckedChange = { viewModel.toggleVpn() },
                        colors = SwitchDefaults.colors(checkedThumbColor = KaliGreen)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CyberButton(
                        text = "Rotate IP",
                        onClick = { viewModel.rotateIp() },
                        modifier = Modifier.weight(1f),
                        containerColor = KaliCyan,
                        contentColor = KaliDarkBg
                    )
                    CyberButton(
                        text = "Spoof MAC/IMEI",
                        onClick = { viewModel.randomizeMacAndImei() },
                        modifier = Modifier.weight(1f),
                        containerColor = KaliSurfaceVariant,
                        contentColor = KaliGreen
                    )
                }
            }
        }

        item {
            // Quick Status Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CyberCard(modifier = Modifier.weight(1f)) {
                    Text("KILL SWITCH", color = KaliTextSecondary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (vpnState.killSwitchEnabled) "ARMED" else "OFF",
                        color = if (vpnState.killSwitchEnabled) KaliGreen else KaliRed,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
                CyberCard(modifier = Modifier.weight(1f)) {
                    Text("FIREWALL BYPASS", color = KaliTextSecondary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ACTIVE",
                        color = KaliGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RECENT TARGET SCAN LOGS",
                    color = KaliTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace
                )
                TextButton(onClick = onNavigateToTerminal) {
                    Text("Open Terminal >", color = KaliGreen, fontFamily = FontFamily.Monospace)
                }
            }
        }

        items(scanTargets.take(5)) { target ->
            CyberCard(
                borderColor = if (target.status == "VULNERABLE") KaliRed.copy(alpha = 0.5f) else KaliGreen.copy(alpha = 0.3f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = target.target,
                        color = KaliTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    StatusBadge(
                        text = target.status,
                        color = if (target.status == "VULNERABLE") KaliRed else KaliGreen
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = target.findings,
                    color = KaliTextSecondary,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
