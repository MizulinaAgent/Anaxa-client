package com.example.anaxa.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.anaxa.domain.model.Message
import com.example.anaxa.ui.components.ErrorView
import com.example.anaxa.ui.components.LoadingIndicator
import com.example.anaxa.ui.theme.Background
import com.example.anaxa.ui.theme.NeonEmerald
import com.example.anaxa.ui.theme.Surface
import com.example.anaxa.ui.theme.SurfaceVariant
import com.example.anaxa.ui.theme.TextMuted
import com.example.anaxa.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onBack: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.lastIndex)
        }
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("Чат", color = TextSecondary, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = NeonEmerald)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Background)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.draft,
                    onValueChange = viewModel::onDraftChange,
                    placeholder = { Text("Сообщение", color = TextMuted) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonEmerald,
                        unfocusedBorderColor = SurfaceVariant,
                        focusedContainerColor = Surface,
                        unfocusedContainerColor = Surface
                    )
                )
                IconButton(
                    onClick = viewModel::send,
                    enabled = state.draft.isNotBlank() && !state.isSending
                ) {
                    if (state.isSending) {
                        CircularProgressIndicator(
                            color = NeonEmerald,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Отправить",
                            tint = NeonEmerald
                        )
                    }
                }
            }
        }
    ) { padding ->
        when {
            state.isLoading -> LoadingIndicator(modifier = Modifier.padding(padding))
            state.error != null && state.messages.isEmpty() ->
                ErrorView(state.error, modifier = Modifier.padding(padding), onRetry = viewModel::load)
            else -> LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.messages) { message ->
                    MessageBubble(message = message, isOwn = message.sender.id == state.currentUserId)
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: Message, isOwn: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isOwn) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            if (!isOwn) {
                Text(
                    text = message.sender.username,
                    style = MaterialTheme.typography.labelMedium,
                    color = NeonEmerald,
                    modifier = Modifier.padding(bottom = 2.dp, start = 4.dp)
                )
            }
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isOwn) 16.dp else 4.dp,
                            bottomEnd = if (isOwn) 4.dp else 16.dp
                        )
                    )
                    .background(if (isOwn) NeonEmerald else Surface)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isOwn) Background else TextSecondary
                )
            }
        }
    }
}
