package com.hophey.jetgo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.hophey.jetgo.core.datastorage.ThemeStorage
import com.hophey.jetgo.navigation.AppNavGraph
import com.hophey.jetgo.navigation.MainScreen
import com.hophey.jetgo.theme.JetGoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val themeStorage = ThemeStorage(this)
            val isDark by themeStorage.isDarkTheme.collectAsStateWithLifecycle(false)
            JetGoTheme(darkTheme = isDark, dynamicColor = false) {
                MainScreen(navController)
            }
        }
    }
}