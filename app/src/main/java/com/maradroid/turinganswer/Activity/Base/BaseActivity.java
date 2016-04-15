package com.maradroid.turinganswer.Activity.Base;



import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.maradroid.turinganswer.DataModel.Rules;
import com.maradroid.turinganswer.DataModel.VariableSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by mara on 4/9/16.
 */
public class BaseActivity extends AppCompatActivity {

    private ArrayList<String> tapeArray;
    private ArrayList<Rules> rulesArray;
    private ArrayList<Rules> appRulesArray;
    private ArrayList<VariableSnapshot> snapshotArray;

    private Rules appRule;

    private String state = "0";
    private String acState;
    private String emptySpace;
    private String unconditionalJump;

    private int head = 0;
    private int unusedRules = 0;

    private long startTime = 0;
    private long stopTime = 0;
    private long exeTime = 0;

    private class CalculateThread extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            Log.e("maradroid", "doInBackground...");

            while (isCancelled() == false) {
                Log.e("maradroid", "while...");

                if(state.equals(acState)) {

                    return ("Niz je prihvaćen!");

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

                        VariableSnapshot snapshot = new VariableSnapshot(tapeArray, appRulesArray, state, head);
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
                            return "niz nije prihvacen";
                        }

                    } else {Log.e("maradroid", "else");

                        appRule = appRulesArray.get(0);
                        appRulesArray.remove(0);

                        applyRule();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("maradroid", "onPostExecute...");
            Log.e("maradroid", s);
        }

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
    }

    private void resetVariables() {
        Log.e("maradroid", "resetVariables...");
        tapeArray = null;
        rulesArray = null;
        appRulesArray = new ArrayList<>();
        snapshotArray = new ArrayList<>();

        appRule = null;

        state = "0";
        acState = null;
        emptySpace = null;
        unconditionalJump = null;

        head = 0;
        unusedRules = 0;

        startTime = 0;
        stopTime = 0;
        exeTime = 0;
    }
}
