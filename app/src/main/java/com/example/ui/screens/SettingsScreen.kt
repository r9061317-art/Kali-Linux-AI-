package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.theme.*
import com.example.viewmodel.KaliViewModel

@Composable
fun SettingsScreen(viewModel: KaliViewModel) {
    val vpnState by viewModel.vpnState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(KaliDarkBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "STEALTH & ANONYMIZER SETTINGS",
                color = KaliGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "Configure advanced network obfuscation, kill switch, and spoofing parameters.",
                color = KaliTextSecondary,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        item {
            CyberCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "NETWORK KILL SWITCH",
                            color = KaliTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Instantly block all non-VPN traffic if tunnel drops.",
                            color = KaliTextSecondary,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Switch(
                        checked = vpnState.killSwitchEnabled,
                        onCheckedChange = { viewModel.toggleKillSwitch() },
                        colors = SwitchDefaults.colors(checkedThumbColor = KaliGreen)
                    )
                }
            }
        }

        item {
            CyberCard {
                Text(
                    text = "HARDWARE SPOOFING IDENTIFIERS",
                    color = KaliTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Current MAC: ${vpnState.macSpoofed}",
                    color = KaliCyan,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "Current IMEI: ${vpnState.imeiSpoofed}",
                    color = KaliCyan,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(12.dp))
                CyberButton(
                    text = "Randomize Identifiers",
                    onClick = { viewModel.randomizeMacAndImei() },
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = KaliCyan,
                    contentColor = KaliDarkBg
                )
            }
        }

        item {
            CyberCard {
                Text(
                    text = "FIREWALL BYPASS & OBFUSCATION",
                    color = KaliTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Protocol: ShadowTLS-V2 Custom Encrypted Tunneling\nProxy Chain: 3-Hop Tor + SOCKS5 Relay\nTime Spoofing: Active (UTC Offset Masked)",
                    color = KaliTextSecondary,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
