package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Answer
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Question
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.ui.HtmlView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun QuestionScreen(
    questionId: Int,
    onAnswerSelected: (Int) -> Unit,
    onBack: () -> Unit,
    viewModel: QuestionViewModel = hiltViewModel()
) {
    val question by viewModel.question.collectAsState()
    val answers by viewModel.answers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(questionId) {
        viewModel.loadQuestion(questionId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            isLoading -> Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            error != null -> Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
            }
            question != null -> QuestionContent(
                question = question!!,
                answers = answers,
                onAnswerSelected = onAnswerSelected,
                modifier = Modifier.weight(1f)
            )
            else -> Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("No data")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Back")
            }
        }
    }
}

@Composable
fun QuestionContent(
    question: Question,
    answers: List<Answer>,
    onAnswerSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column() {
        Text(text = question.title, style = MaterialTheme.typography.titleLarge)
        Text(text = "by ${question.author}", style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(top = 2.dp))
        Row(modifier = Modifier.padding(vertical = 6.dp)) {
            Text("Score: ${question.score}", style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(end = 12.dp))
            Text("Views: ${question.viewCount}", style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(end = 12.dp))
            Text(if (question.isAnswered) "✓ Answered" else "Unanswered",
                style = MaterialTheme.typography.labelSmall,
                color = if (question.isAnswered) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.error)
        }
        Text(question.tags.joinToString(" · "), style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.tertiary)
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
        Text("Answers (${answers.size})", style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp))
    }
    LazyColumn(modifier = modifier) {

        items(answers) { answer ->
            AnswerCard(answer = answer, onClick = { onAnswerSelected(answer.answerId) })
        }
    }
}

@Composable
fun AnswerCard(answer: Answer, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onClick() }) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row {
                Text(text = answer.author, style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f))
                Text("comments: ${answer.commentCount}", style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(end = 8.dp))
                if (answer.isAccepted) {
                    Text("✓ Accepted", style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary)
                }
            }
            Text("Score: ${answer.score}", style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 4.dp))
            Text("Posted: ${formatDate(answer.creationDate)}", style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 2.dp))
            if (answer.body.isNotBlank()) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                HtmlView(
                    html = answer.body,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private fun formatDate(epochSeconds: Long): String =
    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(epochSeconds * 1000))


