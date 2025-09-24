package com.example.andr_dev

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.fragment.app.Fragment

class RulesFragment : Fragment(R.layout.fragment_rules) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val webView = view.findViewById<WebView>(R.id.webViewRules)
        val htmlText = getString(R.string.rules_html)
        webView.loadDataWithBaseURL(null, htmlText, "text/html", "utf-8", null)
    }
}
