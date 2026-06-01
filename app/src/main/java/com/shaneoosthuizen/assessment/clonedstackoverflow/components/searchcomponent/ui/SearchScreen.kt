package com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AlignHorizontalLeft
import androidx.compose.material.icons.automirrored.filled.AlignHorizontalRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material.icons.sharp.SearchOff
import androidx.compose.material.icons.sharp.SignalCellularAlt
import androidx.compose.material.icons.sharp.SignalCellularOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.OrderEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.Question
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.SearchTypeEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.SortEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.ui.theme.AppTheme
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
    val isConnected by viewModel.isConnected.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(AppTheme.icon.logo),
                contentDescription = "Stack Overflow Logo",
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = if (isConnected) Icons.Sharp.SignalCellularAlt else Icons.Sharp.SignalCellularOff,
                contentDescription = "Network status",
                tint = if (isConnected) AppTheme.colors.answered else AppTheme.colors.error,
                modifier = Modifier.size(32.dp)
            )
            IconButton(
                onClick = { viewModel.getQuestions() },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = AppTheme.colors.overflowOrange,
                )
            ) {
                Icon(
                    imageVector = Icons.Default.CloudSync,
                    contentDescription = "Refresh questions"
                )
            }
        }
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                SearchControls(viewModel)
                QuestionControls(viewModel)
                ShowQuestions(onQuestionSelected, viewModel)
            }
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
            IconButton(
                onClick = { searchFiltersExpanded = !searchFiltersExpanded }
            ) {
                Icon(
                    imageVector = if (searchFiltersExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (searchFiltersExpanded) "Hide filters" else "Show filters",
                    tint = AppTheme.colors.button
                )
            }
            TextField(
                value = searchText,
                onValueChange = { viewModel.updateSearchText(it) },
                label = {
                    Text(
                        text = "Search questions",
                        color = AppTheme.colors.overflowOrange
                    )
                },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                viewModel.updateSearchText("")
                                viewModel.getQuestions()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search",
                                tint = AppTheme.colors.overflowOrange
                            )
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(8.dp),
            )
            IconButton(
                onClick = { viewModel.performSearch() },
                modifier = Modifier.padding(start = 4.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = AppTheme.colors.button,
                    contentColor = AppTheme.colors.text,
                )
            ) {
                Icon(
                    imageVector = if (searchText.isEmpty()) Icons.Sharp.SearchOff else Icons.Sharp.Search,
                    contentDescription = "Refresh search results",
                )
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
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Search Options",
                        style = AppTheme.typography.titleMedium,
                        color = AppTheme.colors.overflowOrange,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }

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
        HorizontalDivider(color = AppTheme.colors.overflowOrange)
    }
}

@Composable
fun QuestionControls(viewModel: SearchViewModel) {
    var questionFiltersExpanded by remember { mutableStateOf(false) }
    val currentQuestionSort by viewModel.sortQuestionOption.collectAsState()
    val currentQuestionOrder by viewModel.orderQuestionOption.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Questions",
            style = AppTheme.typography.titleMedium,
            color = AppTheme.colors.overflowOrange,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { questionFiltersExpanded = !questionFiltersExpanded },
            modifier = Modifier.padding(start = 4.dp),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = AppTheme.colors.overflowOrange,
            )
        ) {
            Icon(
                imageVector = if (questionFiltersExpanded) Icons.AutoMirrored.Filled.AlignHorizontalLeft else Icons.AutoMirrored.Filled.AlignHorizontalRight,
                contentDescription = "Apply filters to questions"
            )
        }
        QuestionAmount(viewModel)
    }
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
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
                FilterCard(title = "Sort questions by:") {
                    FilterGrid(
                        items = SortEnum.entries,
                        isSelected = { currentQuestionSort == it },
                        onSelected = { viewModel.updateQuestionSortOption(it) },
                        displayName = { it.displayName }
                    )
                }

                FilterCard(title = "Questions Order") {
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
fun QuestionAmount(viewModel: SearchViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val currentAmount by viewModel.questionAmount.collectAsState()

    Box {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = AppTheme.colors.button,
                contentColor = AppTheme.colors.text
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Items: $currentAmount")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = AppTheme.colors.codeBlock
        ) {
            listOf(10, 20, 30, 50, 60, 70, 80, 90, 100).forEach { amount ->
                val isSelected = amount == currentAmount

                DropdownMenuItem(
                    text = {
                        Text(
                            text = "$amount",
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        viewModel.updateQuestionList(amount)
                        expanded = false
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = if (isSelected) AppTheme.colors.overflowOrange else AppTheme.colors.text
                    )
                )
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
fun ShowQuestions(
    onPostSelected: (Int) -> Unit = {},
    viewModel: SearchViewModel
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val questions by viewModel.questions.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()
    if (!isLoading && isConnected && questions.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "No questions found",
                style = AppTheme.typography.titleLarge,
                color = AppTheme.colors.text
            )

            val reasons = """
            Possible reasons:
            • Try adjusting your search criteria.
            • API daily quota might have been reached.
        """.trimIndent()

            Text(
                text = reasons,
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.text
            )
        }
        return
    }
    LazyColumn {
        items(questions.size) { index ->
            QuestionCard(
                question = questions[index],
                onClick = { onPostSelected(questions[index].questionId) },
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun QuestionCard(
    question: Question,
    onClick: () -> Unit = {},
    viewModel: SearchViewModel
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.cardBackground
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = question.title,
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colors.title
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = question.profileImage,
                        contentDescription = "Author profile picture",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "by ${question.author}",
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colors.username,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            Row(
                modifier = Modifier.padding(top = 6.dp)
            ) {
                Text(
                    text = "Score: ${question.score}",
                    style = AppTheme.typography.labelSmall,
                    color = AppTheme.colors.text,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Text(
                    text = "Answers: ${question.answerCount}",
                    style = AppTheme.typography.labelSmall,
                    color = AppTheme.colors.text,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Text(
                    text = if (question.isAnswered) "✓ Answered" else "Unanswered",
                    style = AppTheme.typography.labelSmall,
                    color = if (question.isAnswered) AppTheme.colors.answered
                    else AppTheme.colors.error
                )
            }
            LazyRow {
                items(question.tags) { tag ->
                    Button(
                        onClick = { viewModel.searchTag(tag) },
                        modifier = Modifier.padding(end = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppTheme.colors.tag,
                            contentColor = AppTheme.colors.text
                        ),
                    ) {
                        Text(
                            text = "[ $tag ]",
                            fontWeight = FontWeight.Bold,
                            style = AppTheme.typography.bodySmall
                        )
                    }
                }
            }
            Text(
                text = "Posted: ${formatDate(question.creationDate)}",
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.text,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

private fun formatDate(epochSeconds: Long): String {
    val date = Date(epochSeconds * 1000)
    return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
}