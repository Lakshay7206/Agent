package com.example.agent.ui.convo


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConversationScreen(
    viewModel: ConversationViewModel
) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(state.messages) { msg ->
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Row(modifier = Modifier.padding(8.dp)) {
            TextField(
                value = state.input,
                onValueChange = viewModel::onInputChange,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = viewModel::sendMessage) {
                Icon(Icons.Default.Send, contentDescription = null)
            }
        }
    }
}
