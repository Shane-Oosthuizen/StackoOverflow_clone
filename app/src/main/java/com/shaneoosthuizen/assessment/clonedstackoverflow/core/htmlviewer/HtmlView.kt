package com.shaneoosthuizen.assessment.clonedstackoverflow.core.htmlviewer

import android.content.Context
import com.shaneoosthuizen.assessment.clonedstackoverflow.ui.theme.AppColors
import android.graphics.Typeface
import android.widget.TextView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.shaneoosthuizen.assessment.clonedstackoverflow.ui.theme.AppTheme
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.html.HtmlPlugin

@Composable
fun HtmlView(
    html: String,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    val colors = if (isDarkTheme) AppTheme.colors else AppTheme.colors  // already resolves correct theme
    val codeTextColor = AppTheme.colors.text.toArgb()
    val textColor = AppTheme.colors.text.toArgb()
    val codeBackgroundColor = AppTheme.colors.codeBlock.toArgb()

    AndroidView(
        factory = { context ->
            TextView(context).apply {
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
                setTextColor(textColor)
                tag = ""
                buildMarkwon(context, isDarkTheme, codeTextColor, codeBackgroundColor).setMarkdown(this, html)
            }
        },
        update = { textView ->
            if (textView.tag != html) {
                textView.tag = html
                buildMarkwon(textView.context, isDarkTheme, codeTextColor, codeBackgroundColor).setMarkdown(textView, html)
            }
        },
        modifier = modifier
    )
}

private fun buildMarkwon(
    context: Context,
    isDarkTheme: Boolean,
    codeTextColor: Int,
    codeBackgroundColor: Int
): Markwon =
    Markwon.builder(context)
        .usePlugin(object : AbstractMarkwonPlugin() {
            override fun configureTheme(builder: MarkwonTheme.Builder) {
                builder
                    .codeBackgroundColor(codeBackgroundColor)
                    .codeTextColor(codeTextColor)
                    .codeTypeface(Typeface.MONOSPACE)
                    .blockQuoteColor(if (isDarkTheme) 0xFF888888.toInt() else 0xFF555555.toInt())
                    .linkColor(if (isDarkTheme) 0xFF4da6ff.toInt() else 0xFF0077cc.toInt())
            }
        })
        .usePlugin(HtmlPlugin.create())
        .build()