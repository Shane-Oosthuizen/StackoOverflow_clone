package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.sharp.SignalCellularAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import com.shaneoosthuizen.assessment.clonedstackoverflow.ui.theme.AppTheme
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
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.htmlviewer.HtmlView
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = AppTheme.colors.button,
                    contentColor = AppTheme.colors.text,
                )
            ) {
                Icon(
                    imageVector = Icons.Sharp.KeyboardDoubleArrowLeft,
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Answer Details",
                style = AppTheme.typography.headlineSmall,
                color = AppTheme.colors.overflowOrange,
                modifier = Modifier.weight(1f)
            )
        }
        when {
            isLoading -> Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            error != null -> Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: $error", color = AppTheme.colors.error)
            }
            answer != null -> AnswerContent(
                answer = answer!!,
                comments = comments,
                modifier = Modifier.weight(1f)
            )
            else -> Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("No data")
            }
        }

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppTheme.colors.button,
                contentColor = AppTheme.colors.text,
            )
        ) {
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = "Author: ${answer.author}",
                        style = AppTheme.typography.titleMedium,
                        color = AppTheme.colors.username
                    )
                    Text(
                        text = "Posted: ${formatAnswerDate(answer.creationDate)}",
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colors.button
                    )
                }
                if (answer.isAccepted) {
                    Text(
                        text = "✓ Accepted",
                        style = AppTheme.typography.labelSmall,
                        color = AppTheme.colors.answered
                    )
                }
            }
            Text(
                text = "Score: ${answer.score}",
                style = AppTheme.typography.labelSmall,
                color = AppTheme.colors.text,
                modifier = Modifier.padding(top = 4.dp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                color = AppTheme.colors.overflowOrange
            )
            HtmlView(
                html = answer.body,
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 12.dp),
                color = AppTheme.colors.overflowOrange
            )
            Text(
                text = "Comments (${comments.size})",
                style = AppTheme.typography.titleSmall,
                color = AppTheme.colors.overflowOrange,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (comments.isEmpty()) {
            item {
                Text(
                    "No comments",
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colors.text
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.cardBackground,
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row {
                Text(
                    text = comment.author,
                    style = AppTheme.typography.labelMedium,
                    color = AppTheme.colors.username,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Score: ${comment.score}",
                    style = AppTheme.typography.labelSmall,
                    color = AppTheme.colors.text,
                )
            }
            Text(
                text = comment.body,
                style = AppTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = formatAnswerDate(comment.creationDate),
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.button,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

private fun formatAnswerDate(epochSeconds: Long): String =
    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(epochSeconds * 1000))
