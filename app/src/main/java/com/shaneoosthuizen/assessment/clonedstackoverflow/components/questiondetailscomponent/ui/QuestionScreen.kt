package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AlignHorizontalLeft
import androidx.compose.material.icons.automirrored.filled.AlignHorizontalRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.sharp.KeyboardDoubleArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Answer
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.OrderEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Question
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.SortEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.htmlviewer.HtmlView
import com.shaneoosthuizen.assessment.clonedstackoverflow.ui.theme.AppTheme
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
    val isFromCache by viewModel.isFromCache.collectAsState()

    LaunchedEffect(questionId) {
        viewModel.loadQuestion(questionId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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
                text = "Question Details",
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

            question != null -> QuestionContent(
                question = question!!,
                answers = answers,
                onAnswerSelected = onAnswerSelected,
                modifier = Modifier.weight(1f),
                viewModel = viewModel,
                isFromCache = isFromCache,
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.colors.button,
                    contentColor = AppTheme.colors.text,
                )
            ) {
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
    modifier: Modifier = Modifier,
    viewModel: QuestionViewModel,
    isFromCache: Boolean = false,
) {
    val currentSort by viewModel.sortOption.collectAsState()
    val currentOrder by viewModel.orderOption.collectAsState()
    var filtersExpanded by remember { mutableStateOf(false) }
    Column {
        Text(
            text = question.title,
            style = AppTheme.typography.titleLarge,
            color = AppTheme.colors.questionTitle
        )
        Text(
            text = "by ${question.author}",
            style = AppTheme.typography.bodySmall,
            color = AppTheme.colors.username,
            modifier = Modifier.padding(top = 2.dp)
        )
        Row(
            modifier = Modifier.padding(vertical = 6.dp)
        ) {
            Text(
                text = "Score: ${question.score}",
                style = AppTheme.typography.labelSmall,
                color = AppTheme.colors.text,
                modifier = Modifier.padding(end = 12.dp)
            )
            Text(
                text = "Views: ${question.viewCount}",
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

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 10.dp),
            color = AppTheme.colors.overflowOrange
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Answers (${answers.size})",
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colors.overflowOrange,
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { filtersExpanded = !filtersExpanded }
            ) {
                Icon(
                    imageVector = if (filtersExpanded) Icons.AutoMirrored.Filled.AlignHorizontalLeft else Icons.AutoMirrored.Filled.AlignHorizontalRight,
                    contentDescription = if (filtersExpanded) "Hide sort options" else "Show sort options",
                    tint = AppTheme.colors.button
                )
            }
        }
        AnimatedVisibility(
            visible = filtersExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                AnswerFilterCard(title = "Sort by") {
                    AnswerFilterGrid(
                        items = SortEnum.entries,
                        isSelected = { currentSort == it },
                        onSelected = { viewModel.updateSortOption(it) },
                        displayName = { it.displayName }
                    )
                }
                AnswerFilterCard(title = "Order") {
                    AnswerFilterGrid(
                        items = OrderEnum.entries,
                        isSelected = { currentOrder == it },
                        onSelected = { viewModel.updateOrderOption(it) },
                        displayName = { it.displayName }
                    )
                }
            }
        }
        if (isFromCache) {
            Text(
                text = "⚠ Offline — answers are read-only",
                style = AppTheme.typography.labelSmall,
                color = AppTheme.colors.error,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
    LazyColumn(modifier = modifier) {
        items(answers) { answer ->
            AnswerCard(
                answer = answer,
                isClickable = !isFromCache,
                onClick = { onAnswerSelected(answer.answerId) }
            )
        }
    }
}

@Composable
fun AnswerFilterCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.codeBlock.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = AppTheme.typography.labelLarge,
                color = AppTheme.colors.overflowOrange,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
fun <T> AnswerFilterGrid(
    items: List<T>,
    isSelected: (T) -> Boolean,
    onSelected: (T) -> Unit,
    displayName: (T) -> String
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.wrapContentHeight()
    ) {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelected(item) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected(item),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = AppTheme.colors.overflowOrange,
                    ),
                    onClick = { onSelected(item) }
                )
                Text(
                    text = displayName(item),
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.text,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun AnswerCard(
    answer: Answer,
    isClickable: Boolean = true,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .then(
                if (isClickable) Modifier.clickable { onClick() }
                else Modifier
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (answer.isAccepted) AppTheme.colors.answered
            else if (!isClickable) AppTheme.colors.cardBackground.copy(alpha = 0.5f)
            else AppTheme.colors.cardBackground
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row {
                Text(
                    text = answer.author,
                    color = AppTheme.colors.username,
                    style = AppTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    "comments: ${answer.commentCount}",
                    style = AppTheme.typography.labelSmall,
                    color = AppTheme.colors.button,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Text(
                "Score: ${answer.score}",
                style = AppTheme.typography.labelSmall,
                color = AppTheme.colors.text,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                "Posted: ${formatDate(answer.creationDate)}",
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.text,
                modifier = Modifier.padding(top = 2.dp)
            )
            if (answer.body.isNotBlank()) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = AppTheme.colors.overflowOrange
                )
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
