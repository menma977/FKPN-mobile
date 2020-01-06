package com.mobile.fkpn.content.menu

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import com.mobile.fkpn.R
import java.net.URISyntaxException
import java.net.URL
import java.util.*

class BinaryActivity : AppCompatActivity() {

    private lateinit var web: WebView
    lateinit var mWebBackForwardList: WebBackForwardList
    private var url = URL("https://www.padippob.com/home")

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_binary)

        web = findViewById(R.id.webView)

        web.removeAllViews()
        web.webViewClient = WebViewClient()
        web.webChromeClient = WebChromeClient()
        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true
        web.settings.javaScriptCanOpenWindowsAutomatically = true

        web.loadUrl(url.toString())
    }
}
