package com.example.agent.ui.convo


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(
    viewModel: ConversationViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ConversationTopBar(
                title = "Conversation",
                onBack = onBack
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
        ) {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                contentPadding = PaddingValues(
                    top = 12.dp,
                    bottom = 12.dp
                )
            ) {
                items(state.messages) { msg ->
                    MessageBubble(
                        text = msg.body,
                        isMe = msg.agent_id != null
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }

            Divider()

            MessageInputBar(
                text = state.input,
                onTextChange = viewModel::onInputChange,
                onSend = viewModel::sendMessage
            )
        }
    }
}
