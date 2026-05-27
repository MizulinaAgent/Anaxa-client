package com.example.anaxa.ui.screens.createlot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.anaxa.ui.components.AnaxaButton
import com.example.anaxa.ui.components.AnaxaTextField
import com.example.anaxa.ui.theme.Background
import com.example.anaxa.ui.theme.ErrorRed
import com.example.anaxa.ui.theme.NeonEmerald
import com.example.anaxa.ui.theme.Surface
import com.example.anaxa.ui.theme.SurfaceVariant
import com.example.anaxa.ui.theme.TextMuted
import com.example.anaxa.ui.theme.TextSecondary
import com.example.anaxa.ui.util.categoryLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateLotScreen(
    onBack: () -> Unit,
    onCreated: () -> Unit,
    viewModel: CreateLotViewModel = hiltViewModel()
) {
    val state = viewModel.state
    var gameMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(state.created) {
        if (state.created) onCreated()
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("Создать лот", color = TextSecondary, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = NeonEmerald)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = gameMenuExpanded,
                onExpandedChange = { gameMenuExpanded = it }
            ) {
                OutlinedTextField(
                    value = state.selectedGameName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Игра") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(gameMenuExpanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.small,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonEmerald,
                        unfocusedBorderColor = SurfaceVariant,
                        focusedContainerColor = Surface,
                        unfocusedContainerColor = Surface,
                        focusedLabelColor = NeonEmerald,
                        unfocusedLabelColor = TextMuted
                    )
                )
                ExposedDropdownMenu(
                    expanded = gameMenuExpanded,
                    onDismissRequest = { gameMenuExpanded = false }
                ) {
                    state.games.forEach { game ->
                        DropdownMenuItem(
                            text = { Text(game.name) },
                            onClick = {
                                viewModel.selectGame(game.id)
                                gameMenuExpanded = false
                            }
                        )
                    }
                }
            }

            if (state.categories.isNotEmpty()) {
                Text("Категория", color = TextSecondary, style = MaterialTheme.typography.titleMedium)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.categories.size) { index ->
                        val category = state.categories[index]
                        FilterChip(
                            selected = category.id == state.selectedCategoryId,
                            onClick = { viewModel.selectCategory(category.id) },
                            label = { Text(categoryLabel(category.type)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NeonEmerald,
                                selectedLabelColor = Background,
                                containerColor = SurfaceVariant,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }
            }

            AnaxaTextField(
                value = state.title,
                onValueChange = viewModel::onTitleChange,
                label = "Название"
            )
            AnaxaTextField(
                value = state.description,
                onValueChange = viewModel::onDescriptionChange,
                label = "Описание",
                singleLine = false
            )
            AnaxaTextField(
                value = state.price,
                onValueChange = viewModel::onPriceChange,
                label = "Цена, ₽",
                keyboardType = KeyboardType.Number
            )

            if (state.error != null) {
                Text(state.error, color = ErrorRed, style = MaterialTheme.typography.bodyMedium)
            }

            AnaxaButton(
                text = "Создать лот",
                onClick = viewModel::submit,
                enabled = state.canSubmit,
                loading = state.isSubmitting,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
