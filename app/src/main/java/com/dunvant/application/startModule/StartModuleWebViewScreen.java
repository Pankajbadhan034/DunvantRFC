package com.dunvant.application.startModule;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.dunvant.application.R;

import androidx.appcompat.app.AppCompatActivity;

public class StartModuleWebViewScreen extends AppCompatActivity {
    ImageView backButton;
     WebView webView;
    String linkStr, titleStr;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_module_web_view_activity);
        backButton = findViewById(R.id.backButton);
        webView = (WebView) findViewById(R.id.webview);

        linkStr = getIntent().getStringExtra("link");

//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);

//        WebViewClientImpl webViewClient = new WebViewClientImpl(this);
//        webView.setWebViewClient(webViewClient);

        webView.loadUrl(linkStr);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
//            this.webView.goBack();
//            return true;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }


    public class WebViewClientImpl extends WebViewClient {

        private Activity activity = null;

        public WebViewClientImpl(Activity activity) {
            this.activity = activity;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            if(url.indexOf(linkStr) > -1 ) return false;

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
            return true;
        }

    }
}