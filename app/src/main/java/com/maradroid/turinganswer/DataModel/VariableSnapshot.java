package com.maradroid.turinganswer.DataModel;

import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mara on 4/10/16.
 */
public class VariableSnapshot {

    private ArrayList<String> tapeArray;
    private ArrayList<Rules> rulesArray;
    private ArrayList<Rules> appRulesArray;
    private ArrayList<Rules> stepRulesArray;

    private String state = "0";
    private String acState;
    private String emptySpace;
    private String unconditionalJump;

    private Button btnCheck;
    private Button btnSimulation;
    private Button btnAutomate;

    private int head = 0;
    private int unusedRules = 0;

    public VariableSnapshot() {
    }

    /** prije racunanja **/
    public VariableSnapshot(ArrayList<String> tapeArray, ArrayList<Rules> rulesArray, String unconditionalJump, String emptySpace, String acState, Button btnCheck, Button btnAutomate, Button btnSimulation) {
        this.tapeArray = tapeArray;
        this.rulesArray = rulesArray;
        this.unconditionalJump = unconditionalJump;
        this.emptySpace = emptySpace;
        this.acState = acState;
        this.btnCheck = btnCheck;
        this.btnAutomate = btnAutomate;
        this.btnSimulation = btnSimulation;
    }

    /** tijekom racunanja **/
    public VariableSnapshot(ArrayList<String> tapeArray, ArrayList<Rules> appRulesArray, ArrayList<Rules> stepRulesArray, String state, int head) {
        this.tapeArray = new ArrayList<>(tapeArray);
        this.appRulesArray = new ArrayList<>(appRulesArray);
        this.stepRulesArray = new ArrayList<>(stepRulesArray);
        this.state = state;
        this.head = head;
    }

    /** za simulaciju **/
    public VariableSnapshot(ArrayList<String> tapeArray, ArrayList<Rules> appRulesArray, String emptySpace) {
        this.tapeArray = new ArrayList<>(tapeArray);
        this.appRulesArray = new ArrayList<>(appRulesArray);
        this.emptySpace = emptySpace;
    }

    /** za automat **/
    public VariableSnapshot(ArrayList<Rules> rulesArray, ArrayList<Rules> appRulesArray) {
        this.rulesArray = rulesArray;
        this.appRulesArray = appRulesArray;
    }

    public ArrayList<String> getTapeArray() {
        return tapeArray;
    }

    public void setTapeArray(ArrayList<String> tapeArray) {
        this.tapeArray = tapeArray;
    }

    public ArrayList<Rules> getRulesArray() {
        return rulesArray;
    }

    public void setRulesArray(ArrayList<Rules> rulesArray) {
        this.rulesArray = rulesArray;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAcState() {
        return acState;
    }

    public void setAcState(String acState) {
        this.acState = acState;
    }

    public String getEmptySpace() {
        return emptySpace;
    }

    public void setEmptySpace(String emptySpace) {
        this.emptySpace = emptySpace;
    }

    public String getUnconditionalJump() {
        return unconditionalJump;
    }

    public void setUnconditionalJump(String unconditionalJump) {
        this.unconditionalJump = unconditionalJump;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public int getUnusedRules() {
        return unusedRules;
    }

    public void setUnusedRules(int unusedRules) {
        this.unusedRules = unusedRules;
    }

    public ArrayList<Rules> getAppRulesArray() {
        return appRulesArray;
    }

    public void setAppRulesArray(ArrayList<Rules> appRulesArray) {
        this.appRulesArray = appRulesArray;
    }

    public ArrayList<Rules> getStepRulesArray() {
        return stepRulesArray;
    }

    public void setStepRulesArray(ArrayList<Rules> stepRulesArray) {
        this.stepRulesArray = stepRulesArray;
    }

    public Button getBtnSimulation() {
        return btnSimulation;
    }

    public Button getBtnAutomate() {
        return btnAutomate;
    }

    public Button getBtnCheck() {
        return btnCheck;
    }
}
