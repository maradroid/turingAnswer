package com.maradroid.turinganswer.Activity.CalculateSimulation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maradroid.turinganswer.Activity.Base.SimulationBaseActivity;
import com.maradroid.turinganswer.DataModel.VariableSnapshot;
import com.maradroid.turinganswer.ListenerManager.ListenerManager;
import com.maradroid.turinganswer.ListenerManager.Listeners.SimulationListener;
import com.maradroid.turinganswer.R;

/**
 * Created by mara on 4/20/16.
 */
public class SimulationActivity extends SimulationBaseActivity {

    private TextView tvTape;
    private TextView tvTempRules;
    private TextView tvTempTape;

    private Button button;

    private VariableSnapshot snapshot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);

        initToolbar();
        initViews();
        getVariableSnapshot();
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
    }

    private void initViews() {

        tvTape = (TextView) findViewById(R.id.tv_tape);
        tvTempTape = (TextView) findViewById(R.id.tv_temp_tape);
        tvTempRules = (TextView)  findViewById(R.id.tv_temp_rule);
        button = (Button) findViewById(R.id.btn_simulation);

        setTextViews(tvTape, tvTempRules, tvTempTape);
    }

    private void getVariableSnapshot() {

        SimulationListener simulationListener = ListenerManager.getSimulationListener();

        if (simulationListener != null) {
            snapshot = simulationListener.getVariableSnapshot();

            if (snapshot != null) {
                setButton(button);
                setTape();
                setVariables(snapshot);
            }
        }
    }

    private void setTape() {

        if (snapshot.getTapeArray() != null) {
            tvTape.setText(snapshot.getTapeArray().toString());
        }

    }

    public void startSimulation(View view) {

        if (isRunning()) {
            stopSimulation();
            button.setText(getString(R.string.start_simulation));

        } else if (button.getText().equals(getString(R.string.reset)) && snapshot != null) {
            setVariables(snapshot);
            button.setText(getString(R.string.start_simulation));

        } else if (snapshot != null && !isRunning()) {
            button.setText(getString(R.string.stop_simulation));
            startSimulation();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isRunning()) {
            stopSimulation();
            button.setText(getString(R.string.start_simulation));

            if (snapshot != null) {
                setVariables(snapshot);
            }
        }
    }
}
