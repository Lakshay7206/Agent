package com.example.agent.ui.thread

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ThreadsScreen(
    viewModel: ThreadsViewModel,
    onThreadClick: (Int) -> Unit
) {
    val threads by viewModel.threads.collectAsState()

    val pullState = rememberPullRefreshState(
        refreshing = false,
        onRefresh = { viewModel.refresh() }
    )

    Scaffold(
        topBar = {
            ThreadsTopBar(
                onRefresh = { viewModel.refresh() }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .pullRefresh(pullState)
        ) {

            LazyColumn {
                items(threads) { thread ->
                    ThreadItem(
                        thread = thread,
                        onClick = { onThreadClick(thread.threadId) }
                    )
                    Divider()
                }
            }

            PullRefreshIndicator(
                refreshing = false,
                state = pullState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}


//@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
//@Composable
//fun ThreadsScreen(
//    viewModel: ThreadsViewModel,
//    onThreadClick: (Int) -> Unit
//) {
//    val threads by viewModel.threads.collectAsState()
//
//    val pullState = rememberPullRefreshState(
//        refreshing = false,
//        onRefresh = { viewModel.refresh() }
//    )
//
//    Box(Modifier.pullRefresh(pullState)) {
//        LazyColumn {
//            items(threads) { thread ->
//                ListItem(
//                    headlineContent = { Text(thread.lastMessage) },
//                    supportingContent = {
//                        Text("${thread.sender} • ${thread.timestamp}")
//                    },
//                    modifier = Modifier.clickable {
//                        onThreadClick(thread.threadId)
//                    }
//                )
//                Divider()
//            }
//        }
//
//        PullRefreshIndicator(
//            refreshing = false,
//            state = pullState,
//            modifier = Modifier.align(Alignment.TopCenter)
//        )
//    }
//}

