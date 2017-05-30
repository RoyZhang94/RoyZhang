package com.bignerdranch.andriod.tot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by Roy on 2017/4/15.
 */

public class NewsFragment extends Fragment {
    private static final String ARG_NEWS_URL = "news_url";
    private String Url;
    private WebView mWebView;
    private ProgressBar mProgressBar;


    public static NewsFragment newInstance(String url){
        Bundle args = new Bundle();
        args.putSerializable(ARG_NEWS_URL,url);

        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        String url = (String)getArguments()
                .getSerializable(ARG_NEWS_URL);
        Url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.fragment_news,container,false);




        mWebView = (WebView)view.findViewById(R.id.news_webview);
        mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView,String url){
                webView.loadUrl(url);
                return true;
            }
        });
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.loadUrl(Url);

        return view;
    }

}
