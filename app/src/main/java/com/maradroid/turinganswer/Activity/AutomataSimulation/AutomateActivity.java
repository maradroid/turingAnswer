package com.maradroid.turinganswer.Activity.AutomataSimulation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.maradroid.turinganswer.Activity.Base.AutomateBaseActivity;
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

    private WebView myWebView;

    private ArrayList<Rules> rulesArray;
    private ArrayList<Rules> appRulesArray;
    private ArrayList<String> nodesArray;

    private String acState;
    private String nodesString;
    private String linksString;

    private boolean dataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automate);

        getExtras();
        getData();
        getNodes();
        getLinks();
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

    private void initWebView() {

        myWebView = (WebView) findViewById(R.id.webview);
        //myWebView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

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

                }
            }
        });

        if (appRulesArray != null && acState != null) {
            setSimulationVariables(appRulesArray, acState, myWebView);
        }

        //myWebView.loadUrl("javascript:sessionStorage.setItem(\"nodes\", JSON.stringify([{atom:'0',size:12,id:0},{atom:'1',size:12,id:1},{atom:'2',size:12,id:2},{atom:'3',size:12,id:3},{atom:'4',size:12,id:4}]));");
        myWebView.loadUrl("file:///android_asset/test_page.html");
        //myWebView.loadUrl("javascript:changeCircleColor('circle_3');");
        //myWebView.loadUrl("javascript:setJSON([{atom:'0',size:12,id:0},{atom:'1',size:12,id:1},{atom:'2',size:12,id:2},{atom:'3',size:12,id:3},{atom:'4',size:12,id:4}]);");

    }

    public void simulationButton(View v) {
        Log.e("maradroid", "Simulacija");
        //myWebView.loadUrl("javascript:document.getElementById('circle_1').style.fill = '#ccc';");
        //myWebView.loadUrl("javascript:changeCircleColor('circle_3');");
        //myWebView.loadUrl("javascript:setJSON([{atom:'0',size:12,id:0},{atom:'1',size:12,id:1},{atom:'2',size:12,id:2},{atom:'3',size:12,id:3},{atom:'4',size:12,id:4}]);");
        //myWebView.loadUrl("javascript:changeCircleColor('circle_3');");
        //myWebView.loadUrl("javascript:changeCircleColor('circle_2');");
        startSimulation();
    }

}
