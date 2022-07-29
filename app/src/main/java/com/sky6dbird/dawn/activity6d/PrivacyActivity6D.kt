package com.sky6dbird.dawn.activity6d

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.sky6dbird.dawn.R
import com.sky6dbird.dawn.base6d.Base6DActivity
import com.sky6dbird.dawn.common6d.Constants6d
import com.sky6dbird.dawn.databinding.Activity6dPrivacyBinding
import java.lang.Exception

/**
 * Date：2022/7/29
 * Describe:
 */
class PrivacyActivity6D : Base6DActivity<Activity6dPrivacyBinding>(R.layout.activity6d_privacy) {
    override fun init() {
        dataBinding.run {
            ivBack.setOnClickListener { onBackPressed() }
        }
        initData()
    }

    private fun initData() {
        val url = Constants6d.PRIVACY_6D
        dataBinding.run {
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    try {
                        if (!url.startsWith("http://") && !url.startsWith("https://")) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                            return true
                        }
                    } catch (e: Exception) {
                        return false
                    }
                    webView.loadUrl(url)
                    return super.shouldOverrideUrlLoading(webView, url)
                }
            }
            webView.loadUrl(url)
        }
    }


    override fun onDestroy() {
        dataBinding.run {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            webView.clearHistory()
            (webView.parent as ViewGroup).removeView(webView)
            webView.destroy()
        }
        super.onDestroy()
    }
}