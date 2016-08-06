package com.maradroid.turinganswer.Activity.Base;



import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.maradroid.turinganswer.Activity.Main.MainActivity;
import com.maradroid.turinganswer.DataModel.Rules;
import com.maradroid.turinganswer.DataModel.VariableSnapshot;
import com.maradroid.turinganswer.Dialog.ResultDialog;
import com.maradroid.turinganswer.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by mara on 4/9/16.
 */
public class CalculateBaseActivity extends AppCompatActivity {

    private static final int TAG_ACCEPTED = 0;
    private static final int TAG_NOT_ACCEPTED = 1;
    private static final int TAG_NOT_EXECUTABLE = 2;
    private static final int TIME_OFFSET = 2;

    private ArrayList<String> tapeArray;
    private ArrayList<Rules> rulesArray;
    private ArrayList<Rules> appRulesArray;
    private ArrayList<Rules> stepRulesArray;
    private ArrayList<String> allUsedStates;
    private ArrayList<VariableSnapshot> snapshotArray;

    private Rules appRule;

    private String state = "0";
    private String acState;
    private String emptySpace;
    private String unconditionalJump;

    private Button btnCheck;
    private Button btnSimulation;
    private Button btnAutomate;

    private int head = 0;
    private int unusedRules = 0;

    private long startTime = 0;
    private long stopTime = 0;
    private long exeTime = 0; // total execution time
    private long singleTime = 0; // single step time
    private long criticalConstant = 0;

    private class CalculateThread extends AsyncTask<String, String, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Log.e("maradroid", "doInBackground...");

            criticalConstant = rulesArray.size() * TIME_OFFSET;

            while (isCancelled() == false) {
                Log.e("maradroid", "while...");

                long criticalTime = criticalConstant * singleTime;

                startTime = System.currentTimeMillis();

                if(state.equals(acState)) {

                    return TAG_ACCEPTED;

                } else if (exeTime > criticalTime){

                    return TAG_NOT_EXECUTABLE;

                } else {

                    appRulesArray.clear();

                    for (Rules rule : rulesArray) {

                        if(state.equals(rule.getTrenutnoStanje()) && (tapeArray.get(head).equals(rule.getProcitanaVrijednost()) || rule.getProcitanaVrijednost().equals(unconditionalJump))){
                            appRulesArray.add(rule);
                        }
                    }

                    if (appRulesArray.size() >= 2) {Log.e("maradroid", "appRulesArray.size() >= 2");

                        appRule = appRulesArray.get(0);
                        appRulesArray.remove(0);

                        VariableSnapshot snapshot = new VariableSnapshot(tapeArray, appRulesArray, stepRulesArray, state, head);
                        snapshotArray.add(snapshot);
                        applyRule();

                    } else if (appRulesArray.size() == 0) {Log.e("maradroid", "appRulesArray.size() == 0");

                        if (snapshotArray.size() != 0) {
                            restoreSnapshot();

                            appRule = appRulesArray.get(0);
                            appRulesArray.remove(0);

                            applyRule();

                        } else {
                            // return niz nije prihvacen ili nesto
                            return TAG_NOT_ACCEPTED;
                        }

                    } else {Log.e("maradroid", "else");

                        appRule = appRulesArray.get(0);
                        appRulesArray.remove(0);

                        applyRule();
                    }
                }

                stopTime = System.currentTimeMillis();

                long tempTime = stopTime - startTime; // step duration

                if (singleTime < tempTime) { // if new step time is smaller then previous one, set new step time
                    singleTime = tempTime;
                }

                exeTime += tempTime;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            Log.e("maradroid", "onPostExecute...");

            btnCheck.setEnabled(true);

            String title = getResources().getString(R.string.result);

            switch (s) {
                case TAG_ACCEPTED:
                    showMessageDialog(title, getResources().getString(R.string.accepted));
                    btnCheck.setEnabled(true);
                    btnSimulation.setEnabled(true);
                    btnAutomate.setEnabled(true);
                    break;

                case TAG_NOT_ACCEPTED:
                    showMessageDialog(title, getResources().getString(R.string.not_accepted));
                    btnCheck.setEnabled(true);
                    btnSimulation.setEnabled(true);
                    btnAutomate.setEnabled(true);
                    break;

                case TAG_NOT_EXECUTABLE:
                    showMessageDialog(title, getResources().getString(R.string.not_executable));
                    btnCheck.setEnabled(true);
                    btnSimulation.setEnabled(false);
                    btnAutomate.setEnabled(false);
                    break;
            }
        }
    }

    public void showMessageDialog(String title, String message) {

        Bundle arg = new Bundle();
        arg.putString("title", title);
        arg.putString("result", message);

        DialogFragment dialog = new ResultDialog();
        dialog.setArguments(arg);
        dialog.show(getSupportFragmentManager(), "");

    }

    private void restoreSnapshot() {
        Log.e("maradroid", "restoreSnapshot");
        int lastSnapshot = snapshotArray.size() - 1;
        VariableSnapshot snapshot = snapshotArray.get(lastSnapshot);

        tapeArray.clear();
        tapeArray.addAll(snapshot.getTapeArray());

        Log.e("maradroid", "before " + appRulesArray.size());
        appRulesArray.clear();
        appRulesArray.addAll(snapshot.getAppRulesArray());
        Log.e("maradroid", "after " + appRulesArray.size());

        stepRulesArray.clear();
        stepRulesArray.addAll(snapshot.getStepRulesArray());

        snapshot.getAppRulesArray().remove(0);

        state = snapshot.getState();

        head = snapshot.getHead();

        if (appRulesArray.size() == 1) {
            snapshotArray.remove(lastSnapshot);
        }

    }

    private void applyRule() {
        Log.e("maradroid", "applyRule");
        tapeArray.set(head, appRule.getVrijednostPisanja());

        state = appRule.getBuduceStanje();

        stepRulesArray.add(appRule);
        allUsedStates.add(appRule.getBuduceStanje());

        if(appRule.getPomak().equals("L") || appRule.getPomak().equals("l")){

            head--;

            if(head < 0){
                tapeArray.add(0, emptySpace);
                head = 0;
            }

        }else if(appRule.getPomak().equals("R") || appRule.getPomak().equals("D") || appRule.getPomak().equals("r") || appRule.getPomak().equals("d")){

            head++;

            if(head > (tapeArray.size() - 1)){
                tapeArray.add(emptySpace);
            }

        }else{

            //return ("Neispravno definiran pomak!");
            Log.e("maradroid", "neispravno definiran pomak");
        }
    }

    public ArrayList<Rules> getStepRulesArray() {

        return stepRulesArray;
    }

    public ArrayList<String> getAllUsedStates() {

        return allUsedStates;
    }

    private ArrayList<Rules> sortRulesArray(ArrayList<Rules> rules) {

        ArrayList<Rules> sortedArray = new ArrayList<>(rules);

        Collections.sort(sortedArray, new Comparator<Rules>() {

            public int compare(Rules rule1, Rules rule2) {
                return rule1.getTrenutnoStanje().compareTo(rule2.getTrenutnoStanje());
            }
        });

        return sortedArray;
    }

    protected void startCalculations(VariableSnapshot snapshot) {

        setVariables(snapshot);
        Log.e("maradroid", "starting new thread...");
        CalculateThread calculateThread = new CalculateThread();
        calculateThread.execute();


    }

    private void setVariables(VariableSnapshot snapshot) {
        Log.e("maradroid", "setVariables...");
        resetVariables();

        tapeArray = snapshot.getTapeArray();
        rulesArray = sortRulesArray(snapshot.getRulesArray());

        acState = snapshot.getAcState();
        emptySpace = snapshot.getEmptySpace();
        unconditionalJump = snapshot.getUnconditionalJump();

        btnCheck = snapshot.getBtnCheck();
        btnAutomate = snapshot.getBtnAutomate();
        btnSimulation = snapshot.getBtnSimulation();
    }

    private void resetVariables() {
        Log.e("maradroid", "resetVariables...");
        tapeArray = null;
        rulesArray = null;
        appRulesArray = new ArrayList<>();
        snapshotArray = new ArrayList<>();
        stepRulesArray = new ArrayList<>();
        allUsedStates = new ArrayList<>();
        allUsedStates.add("0");

        appRule = null;

        state = "0";
        acState = null;
        emptySpace = null;
        unconditionalJump = null;

        btnCheck = null;
        btnAutomate = null;
        btnSimulation = null;

        head = 0;
        unusedRules = 0;

        startTime = 0;
        stopTime = 0;
        exeTime = 0;
    }
}
