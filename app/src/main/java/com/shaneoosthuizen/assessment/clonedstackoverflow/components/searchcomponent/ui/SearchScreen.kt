package com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlignHorizontalRight
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.OrderEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.SortEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.Question
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.SearchTypeEnum
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onQuestionSelected: (Int) -> Unit = {},
    viewModel: SearchViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Stack Overflow Questions",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        if (isLoading) {
            SearchControls(viewModel)
            QuestionControls(viewModel)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            SearchControls(viewModel)
            QuestionControls(viewModel)
            ShowQuestions(onQuestionSelected, viewModel)
        }
    }
}

@Composable
fun SearchControls(viewModel: SearchViewModel) {
    val currentSort by viewModel.searchSortOption.collectAsState()
    val currentOrder by viewModel.searchOrderOption.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val currentSearchType by viewModel.searchType.collectAsState()
    var searchFiltersExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { searchFiltersExpanded = !searchFiltersExpanded }) {
                Icon(
                    imageVector = if (searchFiltersExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (searchFiltersExpanded) "Hide filters" else "Show filters"
                )
            }
            TextField(
                value = searchText,
                onValueChange = { viewModel.updateSearchText(it) },
                label = { Text("Search questions...") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { viewModel.performSearch() },
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Text("Search")
            }
        }

        AnimatedVisibility(
            visible = searchFiltersExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp) // Spacing between the Cards
            ) {
                Text(
                    text = "Search Options",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                FilterCard(title = "Sort by") {
                    FilterGrid(
                        items = SortEnum.entries,
                        isSelected = { currentSort == it },
                        onSelected = { viewModel.updateSearchSortOption(it) },
                        displayName = { it.displayName }
                    )
                }

                FilterCard(title = "Order") {
                    FilterGrid(
                        items = OrderEnum.entries,
                        isSelected = { currentOrder == it },
                        onSelected = { viewModel.updateSearchOrderOption(it) },
                        displayName = { it.displayName }
                    )
                }

                FilterCard(title = "Search Type") {
                    FilterGrid(
                        items = SearchTypeEnum.entries,
                        isSelected = { currentSearchType == it },
                        onSelected = { viewModel.updateSearchType(it) },
                        displayName = { it.displayName }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.heightIn(8.dp))
        HorizontalDivider()
    }
}

@Composable
fun QuestionControls(viewModel: SearchViewModel){
    var questionFiltersExpanded by remember { mutableStateOf(false) }
    val currentQuestionSort by viewModel.sortQuestionOption.collectAsState()
    val currentQuestionOrder by viewModel.orderQuestionOption.collectAsState()


    Row(modifier = Modifier
        .fillMaxWidth()){
        Text(
            text = "Questions",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { viewModel.getQuestions() },
            modifier = Modifier.padding(start = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CloudSync,
                contentDescription = "Refresh questions"
            )
        }
        IconButton(
            onClick = { questionFiltersExpanded = !questionFiltersExpanded },
            modifier = Modifier.padding(start = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AlignHorizontalRight,
                contentDescription = "Apply filters to questions"
            )
        }
    }
    Row(modifier = Modifier
        .fillMaxWidth()){
        AnimatedVisibility(
            visible = questionFiltersExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                FilterCard(
                    title = "Sort questions by:"
                ) {
                    FilterGrid(
                        items = SortEnum.entries,
                        isSelected = { currentQuestionSort == it },
                        onSelected = { viewModel.updateQuestionSortOption(it) },
                        displayName = { it.displayName }
                    )
                }

                FilterCard(
                    title = "Questions Order"
                ) {
                    FilterGrid(
                        items = OrderEnum.entries,
                        isSelected = { currentQuestionOrder == it },
                        onSelected = { viewModel.updateQuestionOrderOption(it) },
                        displayName = { it.displayName }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f) // Subtle background tint
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
fun <T> FilterGrid(
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
                    onClick = { onSelected(item) }
                )
                Text(
                    text = displayName(item),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}


@Composable
fun ShowQuestions(
    onPostSelected: (Int) -> Unit = {},
    viewModel: SearchViewModel
) {
    val questions by viewModel.questions.collectAsState()
    if (questions.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "No questions found",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            val reasons = """
            Possible reasons:
            • Try adjusting your search criteria.
            • API daily quota might have been reached.
        """.trimIndent()

            Text(
                text = reasons,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }
    LazyColumn {
        items(questions.size) { index ->
            QuestionCard(
                question = questions[index],
                onClick = { onPostSelected(questions[index].questionId) }
            )
        }
    }
}

@Composable
fun QuestionCard(
    question: Question,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = question.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "by ${question.author}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 2.dp)
            )
            Row(modifier = Modifier.padding(top = 6.dp)) {
                Text(
                    text = "Score: ${question.score}",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Text(
                    text = "Answers: ${question.answerCount}",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Text(
                    text = if (question.isAnswered) "✓ Answered" else "Unanswered",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (question.isAnswered) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.error
                )
            }
            Text(
                text = question.tags.joinToString(" · "),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "Posted: ${formatDate(question.creationDate)}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
    HorizontalDivider()
}

private fun formatDate(epochSeconds: Long): String {
    val date = Date(epochSeconds * 1000)
    return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
}