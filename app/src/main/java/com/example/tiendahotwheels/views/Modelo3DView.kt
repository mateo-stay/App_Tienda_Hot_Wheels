package com.example.tiendahotwheels.views

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun Modelo3DView(
    modelUrl: String,               // 👈 URL https del .glb
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {

                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true

                webChromeClient = WebChromeClient()
                webViewClient = WebViewClient()

                // Le pasamos la URL del modelo por query param
                val url = "file:///android_asset/modelo3d.html?model=$modelUrl"
                loadUrl(url)
            }
        }
    )
}
