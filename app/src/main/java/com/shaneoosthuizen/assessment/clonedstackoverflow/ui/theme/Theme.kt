package com.shaneoosthuizen.assessment.clonedstackoverflow.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.foundation.isSystemInDarkTheme
import com.shaneoosthuizen.assessment.clonedstackoverflow.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppIcon(
    @DrawableRes val logo : Int
)
data class AppColors(
    val background: Color,
    val cardBackground: Color,
    val codeBlock: Color,
    val overflowOrange: Color,
    val text: Color,
    val questionTitle: Color,
    val button: Color,
    val answered: Color,
    val tag: Color,
    val title: Color,
    val username: Color,
    val error: Color
)

private val lightIcon = AppIcon(
    logo = R.drawable.light_stack_overflow_logo
)

private val darkIcon = AppIcon(
    logo = R.drawable.dark_stack_overflow_logo
)

private val LightColorScheme = AppColors(
    background = LightBackground,
    cardBackground = LightCard,
    codeBlock = LightCodeBlock,
    text = LightText,
    overflowOrange = StackOverflowOrange,
    questionTitle = LightQuestionTitle,
    button = StackOverflowButton,
    answered = StackOverflowAnswered,
    tag = StackOverflowTag,
    title = StackOverflowTitle,
    username = StackOverflowUsername,
    error = StackOverflowError
)

private val DarkColorScheme = AppColors(
    background = DarkBackground,
    cardBackground = DarkCard,
    codeBlock = DarkCodeBlock,
    text = DarkText,
    overflowOrange = StackOverflowOrange,
    questionTitle = DarkQuestionTitle,
    button = StackOverflowButton,
    answered = StackOverflowAnswered,
    tag = StackOverflowTag,
    title = StackOverflowTitle,
    username = StackOverflowUsername,
    error = StackOverflowError
)

val LocalAppColors = staticCompositionLocalOf { LightColorScheme }
val LocalAppTypography = staticCompositionLocalOf { AppTypography() }

val LocalAppIcon = staticCompositionLocalOf { darkIcon }
object AppTheme {
    val colors: AppColors
        @Composable get() = LocalAppColors.current
    val typography: AppTypography
        @Composable get() = LocalAppTypography.current

    val icon: AppIcon
        @Composable get() = LocalAppIcon.current
}

@Composable
fun ClonedStackOverflowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme
    val icon = if (darkTheme) lightIcon else darkIcon

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppIcon provides icon,
        LocalAppTypography provides AppTypography()
    ) {
        content()
    }
}