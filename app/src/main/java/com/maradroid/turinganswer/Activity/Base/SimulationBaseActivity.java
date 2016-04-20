package com.maradroid.turinganswer.Activity.Base;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
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

    private String emptySpace;

    private StringBuilder tempTape;
    private StringBuilder tempRules;

    private int head = 0;

    private class CalculateThread extends AsyncTask<String, String[], String> {

        @Override
        protected String doInBackground(String... params) {

            for (Rules rule : rulesArray) {

                String[] tempValues = getTempValues(rule);

                publishProgress(tempValues);

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

            tvTape.setTextColor(getResources().getColor(R.color.main_color));

        }
    }

    private String[] getTempValues(Rules rule) {

        String tempTape = tapeArray.toString();

        StringBuilder tempRule = new StringBuilder();

        tempRule.append("(");
        tempRule.append(rule.getTrenutnoStanje());
        tempRule.append(",");
        tempRule.append(rule.getProcitanaVrijednost());
        tempRule.append(") = (");
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

    public void startSimulation(VariableSnapshot snapshot) {

        setVariables(snapshot);

        if (tapeArray != null && rulesArray != null && emptySpace != null) {
            CalculateThread calculateThread = new CalculateThread();
            calculateThread.execute();
        }
    }

    private void setVariables(VariableSnapshot snapshot) {

        resetVariables();

        this.tapeArray = snapshot.getTapeArray();
        this.rulesArray = snapshot.getAppRulesArray();
        this.emptySpace = snapshot.getEmptySpace();
        this.tempRules = new StringBuilder();
        this.tempTape = new StringBuilder();
    }

    private void resetVariables() {

        this.tapeArray = null;
        this.rulesArray = null;
        this.emptySpace = null;
        this.tempRules = null;
        this.tempTape = null;

    }

    protected void setTextViews(TextView tvTape,TextView tvTempRules, TextView tvTempTape) {

        this.tvTape = tvTape;
        this.tvTempRules = tvTempRules;
        this.tvTempTape = tvTempTape;
    }
}
