package com.example.anaxa.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.anaxa.ui.theme.Background
import com.example.anaxa.ui.theme.EmeraldGradient
import com.example.anaxa.ui.theme.NeonEmerald
import com.example.anaxa.ui.theme.NeonMint

@Composable
fun SplashScreen(
    onAuthenticated: () -> Unit,
    onUnauthenticated: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    LaunchedEffect(viewModel.authState) {
        when (viewModel.authState) {
            AuthState.AUTHENTICATED -> onAuthenticated()
            AuthState.UNAUTHENTICATED -> onUnauthenticated()
            AuthState.CHECKING -> Unit
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(96.dp).clip(RoundedCornerShape(28.dp)).background(EmeraldGradient),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "A", fontSize = 56.sp, fontWeight = FontWeight.Black, color = Background)
        }
        Text(
            text = "ANAXA",
            style = MaterialTheme.typography.headlineLarge,
            color = NeonMint,
            modifier = Modifier.padding(top = 16.dp)
        )
        CircularProgressIndicator(
            color = NeonEmerald,
            modifier = Modifier.padding(top = 32.dp).size(28.dp),
            strokeWidth = 2.dp
        )
    }
}
