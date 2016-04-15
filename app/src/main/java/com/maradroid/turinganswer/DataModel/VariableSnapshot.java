package com.maradroid.turinganswer.DataModel;

import java.util.ArrayList;

/**
 * Created by mara on 4/10/16.
 */
public class VariableSnapshot {

    private ArrayList<String> tapeArray;
    private ArrayList<Rules> rulesArray;
    private ArrayList<Rules> appRulesArray;

    private String state = "0";
    private String acState;
    private String emptySpace;
    private String unconditionalJump;

    private int head = 0;
    private int unusedRules = 0;

    public VariableSnapshot() {
    }

    public VariableSnapshot(ArrayList<String> tapeArray, ArrayList<Rules> rulesArray, String unconditionalJump, String emptySpace, String acState) {
        this.tapeArray = tapeArray;
        this.rulesArray = rulesArray;
        this.unconditionalJump = unconditionalJump;
        this.emptySpace = emptySpace;
        this.acState = acState;
    }

    public VariableSnapshot(ArrayList<String> tapeArray, ArrayList<Rules> appRulesArray, String state, int head) {
        this.tapeArray = new ArrayList<>(tapeArray);
        this.appRulesArray = new ArrayList<>(appRulesArray);
        this.state = state;
        this.head = head;
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
}
