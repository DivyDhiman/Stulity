package com.StudentFaclity.Stulity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.victor.loading.newton.NewtonCradleLoading;

import Controllers.Controller;

public class CentralizedWebView extends AppCompatActivity {
    private Context context;
    private Controller controller;
    private WebView webView;
    private String urlLoad;
    private NewtonCradleLoading newtonCradleLoading;
    private Handler handler;
    private ImageView menu_btn,appIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.centralized_wb_view);

        context = CentralizedWebView.this;
        controller = new Controller();

        urlLoad = getIntent().getExtras().getString("url");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        set_action_bar();
        initialise();
    }

    private void initialise() {
        webView = (WebView)findViewById(R.id.web_view);
        newtonCradleLoading = (NewtonCradleLoading) findViewById(R.id.newton_cradle_loading);
        newtonCradleLoading.setLoadingColor(R.color.button_bg);
        controller.LoadingStart(newtonCradleLoading);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("TAG", "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        controller.LoadingStop(newtonCradleLoading);
                        newtonCradleLoading.setVisibility(View.GONE);
                    }
                }, 400);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        controller.LoadingStop(newtonCradleLoading);
                        newtonCradleLoading.setVisibility(View.GONE);
                    }
                }, 400);
            }
        });
        webView.loadUrl(urlLoad);
    }

    private void set_action_bar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        menu_btn = (ImageView) toolbar.findViewById(R.id.menu_btn);
        appIcon = (ImageView) toolbar.findViewById(R.id.app_icon);
        ImageView search_dashboard = (ImageView) toolbar.findViewById(R.id.search_dashboard);

        search_dashboard.setVisibility(View.GONE);
        menu_btn.setBackgroundResource(R.drawable.close_floating_btn);

        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        controller.Animation_backward(context);
    }
}