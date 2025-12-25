package com.example.agent.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.agent.ui.auth.LoginScreen
import com.example.agent.ui.auth.LoginViewModel
import com.example.agent.ui.convo.ConversationScreen
import com.example.agent.ui.thread.ThreadsScreen


@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    val loginViewModel: LoginViewModel = hiltViewModel()
    val token by loginViewModel.authToken.collectAsState(initial = null)

    val startDestination = if (token != null) {
        Routes.THREADS
    } else {
        Routes.LOGIN
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {


        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = hiltViewModel(),
                onLoginSuccess = {
                    navController.navigate(Routes.THREADS) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }


        composable(Routes.THREADS) {
            ThreadsScreen(
                viewModel = hiltViewModel(),
                onThreadClick = { threadId ->
                    navController.navigate(
                        Routes.conversation(threadId)
                    )
                },

            )
        }


        composable(
            route = "${Routes.CONVERSATION}/{threadId}",
            arguments = listOf(
                navArgument("threadId") {
                    type = NavType.IntType
                }
            )
        ) {
            ConversationScreen(
                viewModel = hiltViewModel()
            )
        }
    }
}
