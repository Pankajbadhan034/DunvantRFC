package com.dunvant.application.parent.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dunvant.application.R;
import com.dunvant.application.utils.Utilities;

public class ParentHelpDocFragment extends Fragment{

    WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fragment_help_doc, container, false);

        webView = (WebView) view.findViewById(R.id.webView);

//        String documentUrl = Utilities.BASE_DOC_URL + "IFAAppUserGuide.docx";
        String documentUrl = Utilities.BASE_DOC_URL + "IFA-User-Guide.html";

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
//        webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + documentUrl);
        webView.loadUrl(documentUrl);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        return view;
    }
}