package com.maradroid.turinganswer.Activity.Main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.maradroid.turinganswer.Activity.AutomataSimulation.AutomateActivity;
import com.maradroid.turinganswer.Activity.Base.CalculateBaseActivity;
import com.maradroid.turinganswer.Activity.CalculateSimulation.SimulationActivity;
import com.maradroid.turinganswer.Dialog.InputSettingsDialog;
import com.maradroid.turinganswer.Dialog.RuleDialog;
import com.maradroid.turinganswer.Adapter.RecyclerAdapter;
import com.maradroid.turinganswer.DataModel.Rules;
import com.maradroid.turinganswer.DataModel.VariableSnapshot;
import com.maradroid.turinganswer.ListenerManager.ListenerManager;
import com.maradroid.turinganswer.ListenerManager.Listeners.InputSettingsListener;
import com.maradroid.turinganswer.ListenerManager.Listeners.SimulationListener;
import com.maradroid.turinganswer.R;

import java.util.ArrayList;

/**
 * Created by mara on 12/15/15.
 */
public class MainActivity extends CalculateBaseActivity implements RecyclerAdapter.ClickListener, RuleDialog.DialogDataInterface, SimulationListener, InputSettingsListener{

    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;

    private DialogFragment dialog;

    private TextView tvTape;
    private TextView tvAcState;
    private TextView tvEmptySpace;
    private TextView tvUnconditionalJump;

    private Button btnCheck;
    private Button btnAutomat;
    private Button btnSimulation;

    private ArrayList<Rules> rulesArray;

    private String typeOfSimulation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();
        initViews();
        initDialog();

    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
    }

    private void initViews() {

        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        tvTape = (TextView) findViewById(R.id.tv_tape);
        tvAcState = (TextView) findViewById(R.id.tv_ac_state);
        tvEmptySpace = (TextView) findViewById(R.id.tv_empty_space);
        tvUnconditionalJump = (TextView) findViewById(R.id.tv_unconditional_jump);

        btnCheck = (Button) findViewById(R.id.btn_check);
        btnAutomat = (Button) findViewById(R.id.btn_automat);
        btnSimulation = (Button) findViewById(R.id.btn_simulation);

        initRecycler();
    }

    private void initRecycler() {

        rulesArray = new ArrayList<>();
        getDummyData();

        mRecycler = (RecyclerView) findViewById(R.id.recycler_view);
        mRecycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(rulesArray);
        mAdapter.setClickListener(this);
        mRecycler.setAdapter(mAdapter);

    }

    private void getDummyData() {
        rulesArray.add(new Rules("0","1","1","2","d"));
        rulesArray.add(new Rules("0","y","2","2","d"));
        rulesArray.add(new Rules("2","1","3","2","d"));
        rulesArray.add(new Rules("2","1","4","2","d"));
        rulesArray.add(new Rules("2","1","5","2","d"));
        rulesArray.add(new Rules("4","1","6","2","d"));
        rulesArray.add(new Rules("4","1","7","2","d"));
        rulesArray.add(new Rules("4","1","8","2","d"));
    }

    private void initDialog() {

        ListenerManager.setDataInterface(this);

    }

    private void removeRule(int position) {
        rulesArray.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    private void editRule(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);

        openDialog(bundle);
    }

    private void openDialog(Bundle arg) {

        dialog = new RuleDialog();
        dialog.setArguments(arg);
        dialog.show(getSupportFragmentManager(), "");

    }

    @Override
    public void onClick(View v, int position) {

        int id = v.getId();

        if (id == R.id.ll_remove_layout) {
            removeRule(position);

        } else if (id == R.id.ll_edit_layout) {
            editRule(position);
        }
    }

    public void inputSettingsButton(View view) {

        int id = view.getId();

        Bundle bundle = new Bundle();

        switch (id) {
            case R.id.ll_tape:
                bundle.putString("type", InputSettingsDialog.TYPE_TAPE);
                bundle.putString("value", tvTape.getText().toString());

                break;

            case R.id.ll_ac_state:
                bundle.putString("type", InputSettingsDialog.TYPE_AC_STATE);
                bundle.putString("value", tvAcState.getText().toString());

                break;

            case R.id.ll_empty_space:
                bundle.putString("type", InputSettingsDialog.TYPE_EMPTY_SPACE);
                bundle.putString("value", tvEmptySpace.getText().toString());

                break;

            case R.id.ll_unconditional_jump:
                bundle.putString("type", InputSettingsDialog.TYPE_UNCONDITIONAL_JUMP);
                bundle.putString("value", tvUnconditionalJump.getText().toString());

                break;
        }

        ListenerManager.setInputSettingsListener(this);

        dialog = new InputSettingsDialog();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onInputSet(String type, String value) {

        if (type != null) {

            switch (type) {

                case InputSettingsDialog.TYPE_TAPE:
                    tvTape.setText(value);
                    break;

                case InputSettingsDialog.TYPE_AC_STATE:
                    tvAcState.setText("q" + value);
                    break;

                case InputSettingsDialog.TYPE_EMPTY_SPACE:
                    tvEmptySpace.setText(value);
                    break;

                case InputSettingsDialog.TYPE_UNCONDITIONAL_JUMP:
                    tvUnconditionalJump.setText(value);
                    break;
            }
        }

        btnSimulation.setEnabled(false);
        btnAutomat.setEnabled(false);
    }

    public void addNewFunctionButton(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", -1);

        openDialog(bundle);
    }

    public void refreshButton(View view) {

        btnSimulation.setEnabled(false);
        btnAutomat.setEnabled(false);

        rulesArray.clear();
        mAdapter.notifyDataSetChanged();

    }

    public void checkButton(View view) {

        String tape = tvTape.getText().toString();
        String acState = tvAcState.getText().toString();
        String unconditionalJump = tvUnconditionalJump.getText().toString();
        String emptySpace = tvEmptySpace.getText().toString();

        if (!tape.isEmpty() && !acState.isEmpty() && !unconditionalJump.isEmpty() && !emptySpace.isEmpty()) {

            if (rulesArray.size() > 0) {

                boolean startStatePresent = false;

                for (Rules rule : rulesArray) {

                    if (rule.getTrenutnoStanje().equals("0")) {
                        startStatePresent = true;
                    }
                }

                if (startStatePresent) {

                    btnCheck.setEnabled(false);
                    btnAutomat.setEnabled(false);
                    btnSimulation.setEnabled(false);

                    ArrayList<String> tapeArray = new ArrayList<>();

                    for (char c : tape.toCharArray()) {
                        tapeArray.add(String.valueOf(c));
                    }

                    String textTvAcState = acState.substring(1);

                    VariableSnapshot snapshot = new VariableSnapshot(tapeArray,
                            rulesArray,
                            unconditionalJump,
                            emptySpace,
                            textTvAcState,
                            btnCheck,
                            btnAutomat,
                            btnSimulation);

                    Log.e("maradroid", "startCalculations...");
                    startCalculations(snapshot);

                } else {
                    Toast.makeText(this,getResources().getString(R.string.no_start_state),Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this,getResources().getString(R.string.no_rules),Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this,getResources().getString(R.string.fill_all_fields),Toast.LENGTH_SHORT).show();
        }
    }

    public void automateButton(View view) {

        ListenerManager.setSimulationListener(MainActivity.this);
        typeOfSimulation = "automate";

        String textTvAcState = tvAcState.getText().toString();
        String acState = textTvAcState.substring(1);

        Intent intent = new Intent(MainActivity.this, AutomateActivity.class);
        intent.putExtra("acState", acState);
        startActivity(intent);
    }

    public void simulationButton(View view) { // napraviti provjeru unosa!!

        ListenerManager.setSimulationListener(MainActivity.this);
        typeOfSimulation = "stepByStep";

        Intent intent = new Intent(MainActivity.this, SimulationActivity.class);
        startActivity(intent);
    }

    @Override
    public void addNewRule(boolean isEdited, Rules rule, int position) {///////////////// postaviti provjere ispravnosti

        if (rule != null) {

            if (isEdited && position != -1) {
                rulesArray.set(position, rule);
                mAdapter.notifyItemChanged(position);

            } else {
                rulesArray.add(rule);
                mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
            }
        }

        dialog.dismiss();

        btnSimulation.setEnabled(false);
        btnAutomat.setEnabled(false);
    }

    @Override
    public Rules getRule(int position) {
        return rulesArray.get(position);
    }


    @Override
    public VariableSnapshot getVariableSnapshot() {

        if (typeOfSimulation.equals("stepByStep")) {

            ArrayList<String> tapeArray = new ArrayList<>();

            for (char c : tvTape.getText().toString().toCharArray()) {
                tapeArray.add(String.valueOf(c));
            }

            VariableSnapshot snapshot = new VariableSnapshot(tapeArray, getStepRulesArray(), tvEmptySpace.getText().toString());

            if (snapshot != null) {
                return snapshot;
            }

        } else if (typeOfSimulation.equals("automate")) {

            VariableSnapshot snapshot = new VariableSnapshot(rulesArray, getStepRulesArray());

            if (snapshot != null) {
                return snapshot;
            }

        }

        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListenerManager.removeSimulationListener(); // provjeriti da li je potrebno
        typeOfSimulation = null;
    }
}
