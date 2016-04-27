package com.maradroid.turinganswer.Activity.Base;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.maradroid.turinganswer.DataModel.Rules;

import java.util.ArrayList;

/**
 * Created by mara on 4/23/16.
 */
public class AutomateBaseActivity extends AppCompatActivity {

    private ArrayList<Rules> appRulesArray;
    private String acState;
    private WebView webView;

    private class SimulationThread extends AsyncTask<Void, String[], String> {

        @Override
        protected String doInBackground(Void... params) {

            for (int i = 0; i < appRulesArray.size(); i++) {

                String[] updateArray = {appRulesArray.get(i).getTrenutnoStanje(), "#FFF59D"};
                publishProgress(updateArray);


                try {
                    synchronized (this) {
                        wait(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String[]... values) {
            super.onProgressUpdate(values);

            String[] update = values[0];

            String id = update[0];
            String color = update[1];

            changeColor(id, color, false);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (appRulesArray.get(appRulesArray.size() - 1).getBuduceStanje().equals(acState)) { // zeleno

                changeColor(appRulesArray.get(appRulesArray.size() - 1).getBuduceStanje(), "#C5E1A5", true);

            } else { // crveno
                changeColor(appRulesArray.get(appRulesArray.size() - 1).getBuduceStanje(), "#F48FB1", true);
            }
        }
    }

    private void changeColor(String id, String color, boolean finalCircle) {

        String javascript = null;

        if (finalCircle) {
            javascript = "javascript:changeFinalCircleColor('circle_"+ id +"','"+ color +"');";

        } else {
            javascript = "javascript:changeCircleColor('circle_"+ id +"','"+ color +"');";
        }

        if (javascript != null) {
            webView.loadUrl(javascript);
        }
    }

    public void setSimulationVariables(ArrayList<Rules> appRulesArray, String acState, WebView webView) {
        this.appRulesArray = appRulesArray;
        this.acState = acState;
        this.webView = webView;
    }

    public void startSimulation() {

        SimulationThread simulation = new SimulationThread();
        simulation.execute();
    }


}
