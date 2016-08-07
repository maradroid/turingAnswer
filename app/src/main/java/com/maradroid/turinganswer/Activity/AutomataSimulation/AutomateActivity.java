package com.maradroid.turinganswer.Activity.AutomataSimulation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.maradroid.turinganswer.Activity.Base.AutomateBaseActivity;
import com.maradroid.turinganswer.CustomWebView.MyWebView;
import com.maradroid.turinganswer.DataModel.Rules;
import com.maradroid.turinganswer.DataModel.VariableSnapshot;
import com.maradroid.turinganswer.ListenerManager.ListenerManager;
import com.maradroid.turinganswer.ListenerManager.Listeners.SimulationListener;
import com.maradroid.turinganswer.R;

import java.util.ArrayList;

/**
 * Created by mara on 1/10/16.
 */
public class AutomateActivity extends AutomateBaseActivity {

    private MyWebView myWebView;

    private ArrayList<Rules> rulesArray;
    private ArrayList<Rules> appRulesArray;
    private ArrayList<String> nodesArray;
    private ArrayList<String> allUsedStates;

    private String acState;
    private String nodesString;
    private String linksString;

    private Button button;

    private boolean dataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automate);

        getExtras();
        getData();
        getNodes();
        getLinks();
        initButton();
        initWebView();
    }

    private void getExtras() {

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            acState = extras.getString("acState");
        }
    }

    private void getData() {

        SimulationListener listener = ListenerManager.getSimulationListener();
        VariableSnapshot snapshot = null;

        if (listener != null) {
            snapshot = listener.getVariableSnapshot();
        }

        if (snapshot != null) {

            rulesArray = snapshot.getRulesArray();
            appRulesArray = snapshot.getAppRulesArray();
            allUsedStates = snapshot.getAllUsedStates();
        }
    }

    private void getNodes() {

        if (rulesArray != null) {

            nodesArray = new ArrayList<>();

            for (Rules rule : rulesArray) {

                if (!nodesArray.contains(rule.getTrenutnoStanje())) {
                    nodesArray.add(rule.getTrenutnoStanje());
                }

                if (!nodesArray.contains(rule.getBuduceStanje())) {
                    nodesArray.add(rule.getBuduceStanje());
                }
            }

            generateNodesString();
        }
    }

    private void generateNodesString() {

        StringBuilder nodesBuilder = new StringBuilder();

        nodesBuilder.append("[");

        for (int i = 0; i < nodesArray.size(); i++) {

            nodesBuilder.append("{atom:'")
                    .append(nodesArray.get(i))
                    .append("',size:12,id:")
                    .append(i);

            if (nodesArray.get(i).equals(acState)) {
                nodesBuilder.append(",acc:1}");

            } else {
                nodesBuilder.append(",acc:0}");
            }

            if (i != nodesArray.size() - 1) {
                nodesBuilder.append(",");
            }

        }

        nodesBuilder.append("]");

        nodesString = nodesBuilder.toString();
        Log.e("maradroid", "nodesString " + nodesString);
    }

    private void getLinks() {

        StringBuilder linkBuilder = new StringBuilder();

        linkBuilder.append("[");

        for (int i = 0; i < rulesArray.size(); i++) {

            Rules rule = rulesArray.get(i);

            linkBuilder.append("{source:")
                    .append(getNodePosition(rule.getTrenutnoStanje()))
                    .append(",target:")
                    .append(getNodePosition(rule.getBuduceStanje()))
                    .append(",okreni:'ne',bond:'")
                    .append(rule.getProcitanaVrijednost())
                    .append("->")
                    .append(rule.getVrijednostPisanja())
                    .append(",")
                    .append(rule.getPomak().toUpperCase())
                    .append("'}");

            if (i != rulesArray.size() - 1) {
                linkBuilder.append(",");
            }
        }

        linkBuilder.append("]");

        linksString = linkBuilder.toString();

        Log.e("maradroid", "linksString " + linksString);

    }

    private int getNodePosition(String node) {

        for (int i = 0; i < nodesArray.size(); i++) {

            if (node.equals(nodesArray.get(i))) {
                return i;
            }
        }

        return -1;
    }

    private void initButton() {
        button = (Button) findViewById(R.id.btn_simulation);
    }

    private void initWebView() {

        myWebView = (MyWebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(false);

        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                //super.onPageFinished(view, url);

                if (!dataSet) {

                    StringBuilder builder = new StringBuilder();
                    builder.append("javascript:setJSON(")
                            .append(nodesString)
                            .append(",")
                            .append(linksString)
                            .append(");");

                    String javascript = builder.toString();

                    view.loadUrl(javascript);
                    dataSet = true;

                } else {
                    button.setEnabled(true);
                    myWebView.setScroll(true); // iz nekog razloga prije se pozove onaj interface nego invalidate pa zbog toga ovo radi

                }
            }
        });

        if (appRulesArray != null && acState != null) {
            setSimulationVariables(appRulesArray, allUsedStates, acState, myWebView, button);
        }

        myWebView.loadUrl("file:///android_asset/test_page.html");

    }

    public void simulationButton(View v) {
        Log.e("maradroid", "Simulacija");

        if (button.getText().equals(getString(R.string.reset))) {
            button.setEnabled(false);
            myWebView.loadUrl("file:///android_asset/test_page.html");
            button.setText(getString(R.string.start_simulation));

        }else if (isRunning()) {
            stopSimulation();
            button.setText(getString(R.string.start_simulation));

        } else {
            startSimulation();
            button.setText(getString(R.string.stop_simulation));
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isRunning()) {
            stopSimulation();
            button.setText(getString(R.string.start_simulation));
        }
    }
}
