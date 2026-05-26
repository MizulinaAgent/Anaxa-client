package com.example.anaxa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.anaxa.ui.navigation.AnaxaNavGraph
import com.example.anaxa.ui.theme.AnaxaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnaxaTheme {
                AnaxaNavGraph()
            }
        }
    }
}
