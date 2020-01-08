package com.mobile.fkpn.content.menu

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.mobile.fkpn.R
import com.mobile.fkpn.model.EndCode
import com.mobile.fkpn.model.Token
import com.mobile.fkpn.model.Url
import java.net.URL

class BinaryActivity : AppCompatActivity() {

    private lateinit var token: Token
    private lateinit var web: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_binary)

        web = findViewById(R.id.webView)
        token = Token(this)

        web.removeAllViews()
        web.webViewClient = WebViewClient()
        web.webChromeClient = WebChromeClient()
        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true
        web.settings.javaScriptCanOpenWindowsAutomatically = true
        val url = URL(Url.getBinary().toString())

        val endCode = EndCode().endCode(token.username)
        web.loadUrl("$url/home/$endCode")
    }
}
