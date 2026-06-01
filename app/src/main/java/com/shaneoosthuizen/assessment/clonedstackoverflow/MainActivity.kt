package com.shaneoosthuizen.assessment.clonedstackoverflow

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.offlinecache.ui.OfflineScreen
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.ui.AnswerScreen
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.ui.QuestionScreen
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.ui.SearchScreen
import com.shaneoosthuizen.assessment.clonedstackoverflow.ui.theme.AppTheme
import com.shaneoosthuizen.assessment.clonedstackoverflow.ui.theme.ClonedStackOverflowTheme
import dagger.hilt.android.AndroidEntryPoint

@Composable
fun ClonedStackOverflowApp(modifier: Modifier = Modifier, isConnected: Boolean) {
    val navController = rememberNavController()
    var showOfflineDialog by remember { mutableStateOf(true) }

    NavHost(
        navController = navController,
        startDestination = Routes.SEARCH_SCREEN,
        modifier = modifier
    ) {
        composable(Routes.SEARCH_SCREEN) {
            SearchScreen(
                onQuestionSelected = { questionId: Int ->
                    navController.navigate(Routes.questionSelectedScreen(questionId))
                }
            )
        }

        composable(
            route = Routes.QUESTION_SCREEN,
            arguments = listOf(navArgument("questionId") { type = NavType.IntType })
        ) { backStackEntry ->
            val questionId = backStackEntry.arguments?.getInt("questionId") ?: 0
            QuestionScreen(
                questionId = questionId,
                onAnswerSelected = { answerId ->
                    navController.navigate(Routes.answerSelectedScreen(answerId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.ANSWER_SCREEN,
            arguments = listOf(navArgument("answerId") { type = NavType.IntType })
        ) { backStackEntry ->
            val answerId = backStackEntry.arguments?.getInt("answerId") ?: 0
            AnswerScreen(
                answerId = answerId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.OFFLINE_SCREEN) {
            OfflineScreen(
                onQuestionSelected = {
                    navController.navigate(Routes.questionSelectedScreen(it))
                },
                onBack = {
                    navController.popBackStack()
                    if(!isConnected) showOfflineDialog = true
                }
            )
        }
    }


    LaunchedEffect(isConnected) {
        if (!isConnected) showOfflineDialog = true
    }

    if (!isConnected && showOfflineDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("No Internet Connection") },
            text = { Text("You're offline. using Offline mode you can browse questions you have already opened in the past.") },
            confirmButton = {
                Button(onClick = {
                    showOfflineDialog = false
                    navController.navigate(Routes.OFFLINE_SCREEN)
                }) {
                    Text("Open Offline Mode")
                }
            }
        )
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val StackOverflowApplicationViewModel: StackOverflowApplicationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        enableEdgeToEdge()
        setContent {
            val isConnected by StackOverflowApplicationViewModel.isConnected.collectAsState()
            ClonedStackOverflowTheme {
                Scaffold(modifier = Modifier
                    .fillMaxSize()) { innerPadding ->
                    ClonedStackOverflowApp(
                        modifier = Modifier
                            .background(AppTheme.colors.background)
                            .padding(innerPadding),
                        isConnected = isConnected
                    )
                }
            }
        }
    }
}