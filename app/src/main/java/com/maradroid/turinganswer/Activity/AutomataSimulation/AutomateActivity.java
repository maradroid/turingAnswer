package com.maradroid.turinganswer.Activity.AutomataSimulation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.maradroid.turinganswer.R;

/**
 * Created by mara on 1/10/16.
 */
public class AutomateActivity extends AppCompatActivity {

    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automate);

        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

        myWebView.loadUrl("file:///android_asset/test_page.html");
    }

    public void simulationButton(View v) {
        Log.e("maradroid", "Simulacija");
        //myWebView.loadUrl("javascript:document.getElementById('circle_1').style.fill = '#ccc';");
        myWebView.loadUrl("javascript:changeCircleColor('circle_3');");
    }
}
