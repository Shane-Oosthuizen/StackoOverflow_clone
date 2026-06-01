package com.shaneoosthuizen.assessment.clonedstackoverflow.components.offlinecache.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Question
import com.shaneoosthuizen.assessment.clonedstackoverflow.ui.theme.AppTheme


@Composable
fun OfflineScreen(
    onQuestionSelected: (Int) -> Unit,
    onBack: () -> Unit,
    viewModel: OfflineCacheViewModel = hiltViewModel()
) {
    val questions by viewModel.cachedQuestions.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(
            "Cached Questions",
            style = AppTheme.typography.titleLarge,
            color = AppTheme.colors.overflowOrange,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Text(
            "These are questions you've viewed while online. Tap to view details offline.",
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.questionTitle,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (questions.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No cached questions yet.\nOpen questions while online to cache them.",
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.text
                )
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(questions) { question ->
                    CachedQuestionCard(
                        question = question,
                        onClick = { onQuestionSelected(question.questionId) }
                    )
                }
            }
        }

        Button(onClick = onBack, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppTheme.colors.button,
                contentColor = AppTheme.colors.text,
            )
            )
        {
            Text(
                text = "Back",
                color = AppTheme.colors.text,
            )
        }
    }
}

@Composable
private fun CachedQuestionCard(question: Question, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.cardBackground
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                question.title,
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colors.questionTitle
            )
            Text(
                "by ${question.author}", style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.username,
                modifier = Modifier.padding(top = 2.dp)
            )
            Row(
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    "Score: ${question.score}",
                    style = AppTheme.typography.labelSmall,
                    color = AppTheme.colors.text,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Text(
                    "Answers: ${question.answerCount}",
                    style = AppTheme.typography.labelSmall,
                    color = AppTheme.colors.text,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Text(
                    if (question.isAnswered) "✓ Answered" else "Unanswered",
                    style = AppTheme.typography.labelSmall,
                    color = if (question.isAnswered) AppTheme.colors.answered
                    else AppTheme.colors.error
                )
            }
            LazyRow {
                items(question.tags) { tag ->
                    Text(
                        text = " [ $tag ] ",
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.tag,
                        style = AppTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}