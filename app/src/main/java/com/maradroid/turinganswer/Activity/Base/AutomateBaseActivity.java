package com.maradroid.turinganswer.Activity.Base;

import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Button;

import com.maradroid.turinganswer.DataModel.Rules;
import com.maradroid.turinganswer.R;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;


/**
 * Created by mara on 4/23/16.
 */
public class AutomateBaseActivity extends AppCompatActivity {

    private static final String BLUE_STATE = "#E3F2FD";
    private static final String RED_STATE = "#F48FB1";
    private static final String YELLOW_STATE = "#FFF59D";
    private static final String GREEN_STATE = "#C5E1A5";
    private static final String PINK_STATE = "#FCE4EC";

    private SimulationThread simulation;

    private ArrayList<String> appRulesArray;
    private ArrayList<String> allUsedStates;
    private String acState;
    private SimplePair lastID;

    private WebView webView;

    private Button button;

    private boolean isRunning = false;

    private class SimulationThread extends AsyncTask<Void, String[], Integer> {

        @Override
        protected Integer doInBackground(Void... params) {

            lastID = new SimplePair("0", false);

            for (int i = 0; i < allUsedStates.size(); i++) {

                if (isCancelled())
                    return RESULT_CANCELED;

                if (i == 0) {
                    String[] updateArray = {"0", YELLOW_STATE};
                    publishProgress(updateArray);

                } else {

                    if (appRulesArray.contains(allUsedStates.get(i))) {
                        String[] updateArray = {allUsedStates.get(i), YELLOW_STATE};
                        publishProgress(updateArray);

                    } else {
                        String[] updateArray = {allUsedStates.get(i), PINK_STATE};
                        publishProgress(updateArray);
                    }
                }

                try {
                    synchronized (this) {
                        wait(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            return RESULT_OK;
        }

        @Override
        protected void onProgressUpdate(String[]... values) {
            super.onProgressUpdate(values);

            String[] update = values[0];

            String id = update[0];
            String color = update[1];

            changeColor(id, color, false);

            if (color.equals(PINK_STATE)) {
                lastID.setPair(id, true);

            } else {
                lastID.setPair(id, false);
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            if (result == RESULT_OK) {Log.e("maradroid", "RESULT_OK");

                if (appRulesArray.get(appRulesArray.size() - 1).equals(acState)) {

                    changeColor(acState, GREEN_STATE, true);

                } else {
                    changeColor(appRulesArray.get(appRulesArray.size() - 1), RED_STATE, true);
                }

                button.setText(getString(R.string.reset));

            }

            isRunning = false;
        }

        @Override
        protected void onCancelled(Integer integer) {
            super.onCancelled(integer);

            Log.e("maradroid", "RESULT_CANCELED");
            button.setEnabled(false);
            isRunning = false;
            webView.loadUrl("file:///android_asset/test_page.html");
        }
    }

    private void changeColor(String id, String color, boolean finalCircle) {

        String javascript = null;

        if (finalCircle) {
            javascript = "javascript:changeFinalCircleColor('circle_"+ id +"','"+ color +"');";

        } else {

            if (lastID.repaint) {
                javascript = "javascript:changeTwoCirclesColor('circle_"+ lastID.getId() +"','"+ BLUE_STATE +"','circle_" + id + "','" + color + "');";

            } else {
                javascript = "javascript:changeCircleColor('circle_"+ id +"','"+ color +"');";
            }

        }

        if (javascript != null) {
            webView.loadUrl(javascript);
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void stopSimulation() {

        AsyncTask.Status status = simulation.getStatus();

        if (status == AsyncTask.Status.RUNNING || status == AsyncTask.Status.PENDING) {
            simulation.cancel(true);
        }
    }

    public void setSimulationVariables(ArrayList<Rules> appRulesArray, ArrayList<String> allUsedStates, String acState, WebView webView, Button button) {
        //this.appRulesArray = appRulesArray;
        this.allUsedStates = allUsedStates;
        this.acState = acState;
        this.webView = webView;
        this.button = button;

        this.appRulesArray = new ArrayList<>();
        this.appRulesArray.add("0");

        for (Rules rule : appRulesArray) {
            this.appRulesArray.add(rule.getBuduceStanje());
        }
    }

    public void startSimulation() {

        isRunning = true;
        simulation = new SimulationThread();
        simulation.execute();
    }

    private class SimplePair {

        private String id;
        private boolean repaint;

        public SimplePair(String id, boolean repaint) {
            this.id = id;
            this.repaint = repaint;
        }

        public void setPair( String id, boolean repaint) {
            this.repaint = repaint;
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public boolean repaint() {
            return repaint;
        }
    }


}
