package com.shaneoosthuizen.assessment.clonedstackoverflow.core.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Question

@Composable
fun OfflineScreen(
    onQuestionSelected: (Int) -> Unit,
    onBack: () -> Unit,
    viewModel: OfflineCacheViewModel = hiltViewModel()
) {
    val questions by viewModel.cachedQuestions.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Cached Questions", style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 12.dp))

        if (questions.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center) {
                Text(
                    "No cached questions yet.\nOpen questions while online to cache them.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
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

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Text("Back")
        }
    }
}

@Composable
private fun CachedQuestionCard(question: Question, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(question.title, style = MaterialTheme.typography.titleMedium)
            Text("by ${question.author}", style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 2.dp))
            Row(modifier = Modifier.padding(top = 4.dp)) {
                Text("Score: ${question.score}", style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(end = 12.dp))
                Text("Answers: ${question.answerCount}", style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(end = 12.dp))
                Text(if (question.isAnswered) "✓ Answered" else "Unanswered",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (question.isAnswered) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error)
            }
            Text(question.tags.joinToString(" · "),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(top = 2.dp))
        }
    }
    HorizontalDivider()
}