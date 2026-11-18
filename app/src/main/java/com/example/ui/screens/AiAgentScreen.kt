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
import com.example.ui.components.CyberCard
import com.example.ui.theme.*
import com.example.viewmodel.AiUiState
import com.example.viewmodel.KaliViewModel

@Composable
fun AiAgentScreen(viewModel: KaliViewModel) {
    val messages by viewModel.aiMessages.collectAsState()
    val aiState by viewModel.aiState.collectAsState()
    var inputPrompt by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
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
            Column {
                Text(
                    text = "HEMANT AI AGENT (CO-PILOT)",
                    color = KaliCyan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "Powered by Gemini 3.8 Flash & Firebase AI Logic",
                    color = KaliTextSecondary,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Chat Messages List
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { msg ->
                    val isUser = msg.sender == "user"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                    ) {
                        CyberCard(
                            modifier = Modifier.widthIn(max = 320.dp),
                            borderColor = if (isUser) KaliCyan.copy(alpha = 0.5f) else KaliGreen.copy(alpha = 0.5f)
                        ) {
                            Text(
                                text = if (isUser) "You" else "Hemant AI",
                                color = if (isUser) KaliCyan else KaliGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = msg.text,
                                color = KaliTextPrimary,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
                if (aiState is AiUiState.Loading) {
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                            CyberCard(borderColor = KaliGreen.copy(alpha = 0.3f)) {
                                Text(
                                    text = "Hemant AI is analyzing vulnerabilities & generating exploit strategy...",
                                    color = KaliGreen,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Input Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputPrompt,
                onValueChange = { inputPrompt = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ask Hemant AI for exploit payload or analysis...", fontFamily = FontFamily.Monospace) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = KaliCyan,
                    unfocusedBorderColor = KaliSurfaceVariant,
                    focusedTextColor = KaliTextPrimary,
                    unfocusedTextColor = KaliTextPrimary
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace)
            )
            CyberButton(
                text = "Send",
                onClick = {
                    if (inputPrompt.isNotBlank()) {
                        val prompt = inputPrompt
                        inputPrompt = ""
                        viewModel.sendAiMessage(prompt)
                    }
                },
                modifier = Modifier.width(90.dp),
                containerColor = KaliCyan,
                contentColor = KaliDarkBg
            )
        }
    }
}
