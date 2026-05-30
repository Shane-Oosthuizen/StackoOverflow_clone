package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.ui

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
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Comment
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.ui.HtmlView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AnswerScreen(
    answerId: Int,
    onBack: () -> Unit,
    viewModel: AnswerViewModel = hiltViewModel()
) {
    val answer by viewModel.answer.collectAsState()
    val comments by viewModel.comments.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(answerId) {
        viewModel.loadAnswerWithComments(answerId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when {
            isLoading -> Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            error != null -> Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
            }
            answer != null -> AnswerContent(
                answer = answer!!,
                comments = comments,
                modifier = Modifier.weight(1f)
            )
            else -> Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("No data")
            }
        }

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Text("Back")
        }
    }
}

@Composable
fun AnswerContent(
    answer: Answer,
    comments: List<Comment>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = answer.author, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "Posted: ${formatAnswerDate(answer.creationDate)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                if (answer.isAccepted) {
                    Text(
                        text = "✓ Accepted",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Text(
                text = "Score: ${answer.score}",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 4.dp)
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
            HtmlView(html = answer.body, modifier = Modifier.fillMaxWidth())
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            Text(
                text = "Comments (${comments.size})",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (comments.isEmpty()) {
            item {
                Text(
                    "No comments",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        } else {
            items(comments) { comment ->
                CommentCard(comment = comment)
            }
        }
    }
}

@Composable
fun CommentCard(comment: Comment) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row {
                Text(
                    text = comment.author,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                if (comment.score > 0) {
                    Text(
                        text = "▲ ${comment.score}",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Text(
                text = comment.body,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = formatAnswerDate(comment.creationDate),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

private fun formatAnswerDate(epochSeconds: Long): String =
    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(epochSeconds * 1000))
