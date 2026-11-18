package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CyberButton
import com.example.ui.theme.*
import com.example.viewmodel.KaliViewModel

@Composable
fun TerminalScreen(viewModel: KaliViewModel) {
    val logs by viewModel.terminalLogs.collectAsState()
    var inputCommand by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            listState.animateScrollToItem(logs.size - 1)
        }
    }

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
            Text(
                text = "INTERACTIVE KALI TERMINAL",
                color = KaliGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace
            )
            CyberButton(
                text = "Clear",
                onClick = { viewModel.appendTerminalLog("[System] Terminal cleared.") },
                modifier = Modifier.width(90.dp).height(36.dp),
                containerColor = KaliSurfaceVariant,
                contentColor = KaliGreen
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Terminal Output Box
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(KaliSurface, shape = MaterialTheme.shapes.medium)
                .padding(12.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(logs) { log ->
                    Text(
                        text = log,
                        color = when {
                            log.startsWith("root@") -> KaliGreen
                            log.startsWith("[*]") -> KaliCyan
                            log.startsWith("[+]") -> KaliGreen
                            log.startsWith("[Cloud Error]") || log.contains("Error") -> KaliRed
                            else -> KaliTextPrimary
                        },
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Command Input Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputCommand,
                onValueChange = { inputCommand = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("root@kali:~#", fontFamily = FontFamily.Monospace) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = KaliGreen,
                    unfocusedBorderColor = KaliSurfaceVariant,
                    focusedTextColor = KaliGreen,
                    unfocusedTextColor = KaliGreen
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace)
            )
            CyberButton(
                text = "Run",
                onClick = {
                    if (inputCommand.isNotBlank()) {
                        val cmd = inputCommand
                        inputCommand = ""
                        viewModel.runToolCommand("Custom Shell", cmd)
                    }
                },
                modifier = Modifier.width(90.dp),
                containerColor = KaliGreen,
                contentColor = KaliDarkBg
            )
        }
    }
}
