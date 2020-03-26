package com.pep.core.libbase;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;


/**
 * @author sunbaixin
 */
public class EasyBaseWebViewActivity extends EasyBaseActivity {
    private WebView webView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_pep_base_webview;

    }

    @Override
    public void initView() {
        webView = (WebView) findViewById(R.id.webview);

    }

    @Override
    public void initData() {
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.baidu.com/");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void invoke(Context context) {
        Intent intent = new Intent(context, EasyBaseWebViewActivity.class);
        context.startActivity(intent);
    }
}
