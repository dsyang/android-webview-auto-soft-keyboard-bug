/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.samples.webviewdemo

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.res.Configuration
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebSettingsCompat.DARK_STRATEGY_PREFER_WEB_THEME_OVER_USER_AGENT_DARKENING
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import androidx.webkit.WebViewFeature
import com.android.samples.webviewdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    // Creating the custom WebView Client Class
    private class MyWebViewClient(private val assetLoader: WebViewAssetLoader) :
        WebViewClientCompat() {
        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(request.url)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            val webView = view ?: return

            webView.requestFocus()
            webView.evaluateJavascript("""
                console.log("onPageFinished")
                setTimeout(() => {
                  console.log("focusing to show keyboard")
                  focusToShowSoftKeyboard()
                }, 1000)
                
            """.trimIndent(), null)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Configure asset loader with custom domain
        // *NOTE* :
        // The assets path handler is set with the sub path /views-widgets-samples/ here because we
        // are tyring to ensure that the address loaded with
        // loadUrl("https://raw.githubusercontent.com/views-widgets-samples/assets/index.html") does
        // not conflict with a real web address. In this case, if the path were only /assests/ we
        // would need to load "https://raw.githubusercontent.com/assets/index.html" in order to
        // access our local index.html file.
        // However we cannot guarantee "https://raw.githubusercontent.com/assets/index.html" is not
        // a valid web address. Therefore we must let the AssetLoader know to expect the
        // /views-widgets-samples/ sub path as well as the /assets/.
        val assetLoader = WebViewAssetLoader.Builder()
            .setDomain("raw.githubusercontent.com")
            .addPathHandler(
                "/views-widgets-samples/assets/",
                WebViewAssetLoader.AssetsPathHandler(this)
            )
            .addPathHandler(
                "/views-widgets-samples/res/",
                WebViewAssetLoader.ResourcesPathHandler(this)
            )
            .build()

        // Set clients
        binding.webview.webViewClient = MyWebViewClient(assetLoader)

        // Set Title
        title = getString(R.string.app_name)

        // Setup debugging; See https://developers.google.com/web/tools/chrome-devtools/remote-debugging/webviews for reference
        if (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        // Enable Javascript
        binding.webview.settings.javaScriptEnabled = true

        // Load the content
        binding.webview.loadUrl("https://raw.githubusercontent.com/views-widgets-samples/assets/index.html")
        var reloadCounter = 0
        binding.reloadButton.setOnClickListener {
            binding.webview.loadUrl("https://raw.githubusercontent.com/views-widgets-samples/assets/index.html")
            binding.webview.requestFocus()
            reloadCounter++
            binding.reloadButton.text = "Reload counter: $reloadCounter"
        }
    }
}
