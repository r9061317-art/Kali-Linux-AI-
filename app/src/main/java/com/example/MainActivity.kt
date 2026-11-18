package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.configureAppCheck
import com.example.ui.screens.*
import com.example.ui.theme.KaliDarkBg
import com.example.ui.theme.KaliGreen
import com.example.ui.theme.KaliSurface
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.KaliViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Home)
    object Toolbox : Screen("toolbox", "Toolbox", Icons.Default.Build)
    object Terminal : Screen("terminal", "Terminal", Icons.Default.Terminal)
    object AiAgent : Screen("ai_agent", "Hemant AI", Icons.Default.SmartToy)
    object Reports : Screen("reports", "Reports", Icons.Default.Assessment)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // CRITICAL: Configure App Check before UI or Firebase operations
        configureAppCheck(this, intent)

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: KaliViewModel = viewModel()
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }

                val screens = listOf(
                    Screen.Dashboard,
                    Screen.Toolbox,
                    Screen.Terminal,
                    Screen.AiAgent,
                    Screen.Reports,
                    Screen.Settings
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = KaliDarkBg,
                    bottomBar = {
                        NavigationBar(
                            containerColor = KaliSurface,
                            tonalElevation = 8.dp
                        ) {
                            screens.forEach { screen ->
                                NavigationBarItem(
                                    icon = { Icon(screen.icon, contentDescription = screen.title) },
                                    label = { Text(screen.title, fontSize = 10.sp) },
                                    selected = currentScreen == screen,
                                    onClick = { currentScreen = screen },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = KaliDarkBg,
                                        selectedTextColor = KaliGreen,
                                        indicatorColor = KaliGreen,
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentScreen) {
                            is Screen.Dashboard -> DashboardScreen(viewModel) {
                                currentScreen = Screen.Terminal
                            }
                            is Screen.Toolbox -> ToolboxScreen(viewModel) { toolName, cmd ->
                                viewModel.runToolCommand(toolName, cmd)
                                currentScreen = Screen.Terminal
                            }
                            is Screen.Terminal -> TerminalScreen(viewModel)
                            is Screen.AiAgent -> AiAgentScreen(viewModel)
                            is Screen.Reports -> ReportsScreen(viewModel)
                            is Screen.Settings -> SettingsScreen(viewModel)
                        }
                    }
                }
            }
        }
    }
}
