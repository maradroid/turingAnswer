package com.maradroid.turinganswer.Activity.Base;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.maradroid.turinganswer.DataModel.Rules;
import com.maradroid.turinganswer.DataModel.VariableSnapshot;
import com.maradroid.turinganswer.R;

import java.util.ArrayList;

/**
 * Created by mara on 4/20/16.
 */
public class SimulationBaseActivity extends AppCompatActivity {

    private ArrayList<Rules> rulesArray;
    private ArrayList<String> tapeArray;

    private TextView tvTape;
    private TextView tvTempRules;
    private TextView tvTempTape;

    private Button button;

    private String emptySpace;

    private StringBuilder tempTape;
    private StringBuilder tempRules;

    private CalculateThread calculateThread;

    private VariableSnapshot snapshot;

    private int head = 0;

    private boolean isRunning = false;

    private class CalculateThread extends AsyncTask<String, String[], String> {

        @Override
        protected String doInBackground(String... params) {
            Log.e("maradroid", "simulation... " + rulesArray.size());

            for (Rules rule : rulesArray) {

                if (isCancelled())
                    return null;

                String[] tempValues2 = getTempValues(rule);

                publishProgress(tempValues2);

                applyChanges(rule);

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

            String[] tempValue = values[0];

            tvTape.setText(tempValue[0]);

            tempTape.append(tempValue[0]);
            tempTape.append("\n");

            tempRules.append(tempValue[1]);
            tempRules.append("\n");

            tvTempTape.setText(tempTape.toString());
            tvTempRules.setText(tempRules.toString());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            tvTape.setText(tapeArray.toString());
            tvTape.setTextColor(getResources().getColor(R.color.main_color));
            isRunning = false;
            button.setText(getString(R.string.reset));

        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            isRunning = false;
            setVariables();
        }
    }

    private String[] getTempValues(Rules rule) {

        String tempTape = tapeArray.toString();

        StringBuilder tempRule = new StringBuilder();

        tempRule.append("(q");
        tempRule.append(rule.getTrenutnoStanje());
        tempRule.append(",");
        tempRule.append(rule.getProcitanaVrijednost());
        tempRule.append(") = (q");
        tempRule.append(rule.getBuduceStanje());
        tempRule.append(",");
        tempRule.append(rule.getVrijednostPisanja());
        tempRule.append(",");
        tempRule.append(rule.getPomak());
        tempRule.append(")");

        String[] tempValue = {tempTape, tempRule.toString()};

        return tempValue;
    }

    private void applyChanges(Rules rule) {

        tapeArray.set(head, rule.getVrijednostPisanja());

        if(rule.getPomak().equals("L") || rule.getPomak().equals("l")){

            head--;

            if(head < 0){
                tapeArray.add(0, emptySpace);
                head = 0;
            }

        }else if(rule.getPomak().equals("R") || rule.getPomak().equals("D") || rule.getPomak().equals("r") || rule.getPomak().equals("d")){

            head++;

            if(head > (tapeArray.size() - 1)){
                tapeArray.add(emptySpace);
            }
        }
    }

    public void startSimulation() {

        //setVariables(snapshot);
        isRunning = true;

        if (tapeArray != null && rulesArray != null && emptySpace != null) {
            calculateThread = new CalculateThread();
            calculateThread.execute();
        }
    }

    public void stopSimulation() {

        AsyncTask.Status status = calculateThread.getStatus();

        if (status == AsyncTask.Status.RUNNING || status == AsyncTask.Status.PENDING) {
            calculateThread.cancel(true);
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public void setVariables(VariableSnapshot snapshot) {

        this.snapshot = snapshot;

        setVariables();
    }

    private void setVariables() {

        resetVariables();

        this.tapeArray.addAll(snapshot.getTapeArray());
        this.rulesArray = snapshot.getAppRulesArray();
        this.emptySpace = snapshot.getEmptySpace();
        this.tempRules = new StringBuilder();
        this.tempTape = new StringBuilder();
        this.tvTape.setText(snapshot.getTapeArray().toString());
    }

    private void resetVariables() {

        this.tapeArray = new ArrayList<>();
        this.rulesArray = null;
        this.emptySpace = null;
        this.tempRules = null;
        this.tempTape = null;
        this.tvTempRules.setText("");
        this.tvTempTape.setText("");
        this.head = 0;

        tvTape.setTextColor(getResources().getColor(R.color.text_gray_color));

    }

    protected void setTextViews(TextView tvTape,TextView tvTempRules, TextView tvTempTape) {

        this.tvTape = tvTape;
        this.tvTempRules = tvTempRules;
        this.tvTempTape = tvTempTape;
    }
}
