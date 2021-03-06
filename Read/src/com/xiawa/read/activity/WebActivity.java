package com.xiawa.read.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.xiawa.read.R;


public class WebActivity extends BaseActivity
{

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        setHeaderTitle("漂读");
        init();
    }

    private ProgressDialog dialog;

    private void init()
    {
        webView = (WebView) findViewById(R.id.wv);
        Intent intent = getIntent();
        if (intent == null)
            return;
        String url = intent.getStringExtra("url");
        Toast.makeText(this, url + "   url", Toast.LENGTH_SHORT).show();

        webView = (WebView) findViewById(R.id.wv);
        webView.loadUrl(url);
        // 覆盖WebView默认通过第三方或者是系统浏览器打开网页的行为，使得网页可以在WebVIew中打开
        webView.setWebViewClient(new WebViewClient()
        {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                if (url.startsWith("tel:"))
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL,
                            Uri.parse(url));
                    startActivity(intent);
                } else if (url.startsWith("http:") || url.startsWith("https:"))
                {
                    view.loadUrl(url);
                }

                //返回值是true的时候控制网页在WebView中去打开，如果为false调用系统浏览器或第三方浏览器去打开
                return true;
            }
            //WebViewClient帮助WebView去处理一些页面控制和请求通知

        });
        //启用支持JavaScript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //WebView加载页面优先使用缓存加载
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        webView.setWebChromeClient(new WebChromeClient()
        {

            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {

                //newProgress 1-100之间的整数
                if (newProgress == 100)
                {
                    //网页加载完毕，关闭ProgressDialog
                    closeDialog();
                } else
                {
                    //网页正在加载,打开ProgressDialog
                    openDialog(newProgress);
                }
            }

            private void closeDialog()
            {

                if (dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                    dialog = null;
                }
            }

            private void openDialog(int newProgress)
            {

                if (dialog == null)
                {
                    dialog = new ProgressDialog(WebActivity.this);
                    dialog.setTitle("加载中……");
                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.setProgress(newProgress);
                    dialog.show();

                } else
                {
                    dialog.setProgress(newProgress);
                }


            }
        });
    }

    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            //Toast.makeText(this, webView.getUrl(), Toast.LENGTH_SHORT).show();
            if (webView.canGoBack())
            {
                webView.goBack();//返回上一页面
                return true;
            } else
            {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

