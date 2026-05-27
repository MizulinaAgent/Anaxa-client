package com.example.anaxa.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.anaxa.ui.components.AnaxaButton
import com.example.anaxa.ui.components.AnaxaTextField
import com.example.anaxa.ui.theme.Background
import com.example.anaxa.ui.theme.EmeraldGradient
import com.example.anaxa.ui.theme.ErrorRed
import com.example.anaxa.ui.theme.NeonEmerald
import com.example.anaxa.ui.theme.NeonMint
import com.example.anaxa.ui.theme.TextMuted

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) onAuthSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.height(48.dp))

        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(EmeraldGradient),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "A",
                fontSize = 56.sp,
                fontWeight = FontWeight.Black,
                color = Background
            )
        }

        Text(
            text = "ANAXA",
            style = MaterialTheme.typography.headlineLarge,
            color = NeonMint,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Биржа игровых ценностей",
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted
        )

        TabRow(
            selectedTabIndex = if (state.isRegister) 1 else 0,
            modifier = Modifier.padding(top = 32.dp),
            containerColor = Background,
            contentColor = NeonEmerald
        ) {
            Tab(
                selected = !state.isRegister,
                onClick = { viewModel.setRegisterMode(false) },
                text = { Text("Вход") }
            )
            Tab(
                selected = state.isRegister,
                onClick = { viewModel.setRegisterMode(true) },
                text = { Text("Регистрация") }
            )
        }

        Column(
            modifier = Modifier.padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (state.isRegister) {
                AnaxaTextField(
                    value = state.username,
                    onValueChange = viewModel::onUsernameChange,
                    label = "Имя пользователя",
                    leadingIcon = Icons.Filled.Person
                )
            }
            AnaxaTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                label = "Email",
                leadingIcon = Icons.Filled.Email,
                keyboardType = KeyboardType.Email
            )
            AnaxaTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                label = "Пароль",
                leadingIcon = Icons.Filled.Lock,
                isPassword = true
            )

            if (state.error != null) {
                Text(
                    text = state.error,
                    color = ErrorRed,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            AnaxaButton(
                text = if (state.isRegister) "Зарегистрироваться" else "Войти",
                onClick = viewModel::submit,
                enabled = state.canSubmit,
                loading = state.isLoading,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
