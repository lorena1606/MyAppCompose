package com.example.myappcompose.ui.screen.drafts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myappcompose.domain.model.TaskDraft
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DraftsScreen(
    viewModel: DraftsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Borradores Locales") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.drafts.isEmpty()) {
                Text(
                    text = "No tienes borradores guardados.",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(uiState.drafts, key = { it.id }) { draft ->
                        DraftItemRow(
                            draft = draft,
                            onPublish = { viewModel.publishDraft(draft.id) },
                            onDelete = { viewModel.deleteDraft(draft.id) }
                        )
                    }
                }
            }

            if (uiState.isPublishing) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter))
            }

            uiState.error?.let { error ->
                Snackbar(modifier = Modifier.padding(16.dp).align(Alignment.BottomCenter)) {
                    Text(text = error)
                }
            }
        }
    }
}

@Composable
fun DraftItemRow(
    draft: TaskDraft,
    onPublish: () -> Unit,
    onDelete: () -> Unit
) {
    val date = remember(draft.savedAt) {
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(draft.savedAt))
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = draft.title, style = MaterialTheme.typography.titleMedium)
                Text(text = "Guardado: $date", style = MaterialTheme.typography.bodySmall)
            }
            Row {
                IconButton(onClick = onPublish) {
                    Icon(Icons.Default.CloudUpload, contentDescription = "Publicar", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
