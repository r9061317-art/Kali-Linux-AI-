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
import com.example.model.CloudReport
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.viewmodel.KaliViewModel

@Composable
fun ReportsScreen(viewModel: KaliViewModel) {
    val reports by viewModel.cloudReports.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KaliDarkBg)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "CLOUD REPORTS & SYNC",
                    color = KaliGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "Sync assessment results securely to Firebase Firestore",
                    color = KaliTextSecondary,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            CyberButton(
                text = "New Report",
                onClick = {
                    val newReport = CloudReport(
                        title = "Automated Pentest Audit #${(100..999).random()}",
                        target = "target-api.pentest.local",
                        severity = "HIGH",
                        summary = "Discovered SQL injection and unauthenticated endpoint.",
                        date = "2026-11-17"
                    )
                    viewModel.syncToCloud(newReport)
                },
                modifier = Modifier.width(130.dp).height(36.dp),
                containerColor = KaliGreen,
                contentColor = KaliDarkBg
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reports) { report ->
                CyberCard(
                    borderColor = when (report.severity) {
                        "CRITICAL" -> KaliRed.copy(alpha = 0.6f)
                        "HIGH" -> KaliYellow.copy(alpha = 0.6f)
                        else -> KaliGreen.copy(alpha = 0.4f)
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = report.title,
                            color = KaliTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        StatusBadge(
                            text = report.severity,
                            color = when (report.severity) {
                                "CRITICAL" -> KaliRed
                                "HIGH" -> KaliYellow
                                else -> KaliGreen
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Target: ${report.target} | Date: ${report.date}",
                        color = KaliCyan,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = report.summary,
                        color = KaliTextSecondary,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    CyberButton(
                        text = "Sync to Firebase Console",
                        onClick = { viewModel.syncToCloud(report) },
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = KaliSurfaceVariant,
                        contentColor = KaliGreen
                    )
                }
            }
        }
    }
}
