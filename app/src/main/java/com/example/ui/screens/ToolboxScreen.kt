package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
fun ToolboxScreen(viewModel: KaliViewModel, onRunCommand: (String, String) -> Unit) {
    val tools by viewModel.tools.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val categories = listOf(
        "All",
        "Information Gathering",
        "Vulnerability Analysis",
        "Wireless Attacks",
        "Exploitation",
        "Password Attacks",
        "Sniffing & Spoofing",
        "Web Applications"
    )

    val filteredTools = tools.filter { tool ->
        val matchesCategory = selectedCategory == "All" || tool.category == selectedCategory
        val matchesSearch = tool.name.contains(searchQuery, ignoreCase = true) ||
                tool.description.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KaliDarkBg)
            .padding(16.dp)
    ) {
        Text(
            text = "KALI TOOLBOX (600+ MODULES)",
            color = KaliGreen,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search tools (e.g. nmap, sqlmap, hydra)...", fontFamily = FontFamily.Monospace) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = KaliGreen) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = KaliGreen,
                unfocusedBorderColor = KaliSurfaceVariant,
                focusedTextColor = KaliTextPrimary,
                unfocusedTextColor = KaliTextPrimary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { viewModel.setCategory(category) },
                    label = { Text(category, fontFamily = FontFamily.Monospace, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = KaliGreen,
                        selectedLabelColor = KaliDarkBg,
                        containerColor = KaliSurface,
                        labelColor = KaliTextPrimary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tools List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredTools) { tool ->
                CyberCard(
                    borderColor = when (tool.riskLevel) {
                        "CRITICAL" -> KaliRed.copy(alpha = 0.6f)
                        "HIGH" -> KaliYellow.copy(alpha = 0.6f)
                        else -> KaliGreen.copy(alpha = 0.3f)
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tool.name,
                            color = KaliTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        StatusBadge(
                            text = tool.riskLevel,
                            color = when (tool.riskLevel) {
                                "CRITICAL" -> KaliRed
                                "HIGH" -> KaliYellow
                                else -> KaliGreen
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = tool.description,
                        color = KaliTextSecondary,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Cmd: ${tool.defaultCommand}",
                        color = KaliCyan,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    CyberButton(
                        text = "Execute Tool",
                        onClick = { onRunCommand(tool.name, tool.defaultCommand) },
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = KaliGreen,
                        contentColor = KaliDarkBg
                    )
                }
            }
        }
    }
}
