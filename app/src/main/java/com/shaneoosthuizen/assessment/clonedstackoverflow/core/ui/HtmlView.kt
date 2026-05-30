package com.shaneoosthuizen.assessment.clonedstackoverflow.core.ui

import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
@Composable
fun HtmlView(
    html: String,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = false
                isVerticalScrollBarEnabled = false
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL(null, wrapHtml(html), "text/html", "UTF-8", null)
        },
        modifier = modifier
    )
}

private fun wrapHtml(body: String): String = """
    <html>
    <head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        body {
            font-family: -apple-system, sans-serif;
            font-size: 14px;
            line-height: 1.6;
            color: #1a1a1a;
            margin: 0; padding: 0;
            background: transparent;
        }
        pre {
            background: #f6f6f6;
            border: 1px solid #e0e0e0;
            border-radius: 4px;
            padding: 12px;
            overflow-x: auto;
            white-space: pre-wrap;
            word-wrap: break-word;
        }
        code {
            font-family: 'Courier New', monospace;
            font-size: 13px;
            background: #f6f6f6;
            padding: 1px 4px;
            border-radius: 3px;
        }
        pre code {
            background: none;
            padding: 0;
        }
        blockquote {
            border-left: 4px solid #e0e0e0;
            margin: 8px 0;
            padding-left: 12px;
            color: #555;
        }
        a { color: #0077cc; }
        p { margin: 8px 0; }
        ul, ol { padding-left: 20px; margin: 8px 0; }
    </style>
    </head>
    <body>$body</body>
    </html>
""".trimIndent()

