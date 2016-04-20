package com.maradroid.turinganswer.Activity.CalculateSimulation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

        setTextViews(tvTape, tvTempRules, tvTempTape);
    }

    private void getVariableSnapshot() {

        SimulationListener simulationListener = ListenerManager.getSimulationListener();

        if (simulationListener != null) {
            snapshot = simulationListener.getVariableSnapshot();

            if (snapshot != null) {
                setTape();
            }
        }
    }

    private void setTape() {
        tvTape.setText(snapshot.getTapeArray().toString());
    }

    public void startSimulation(View view) {

        if (snapshot != null) {

            startSimulation(snapshot);
        }
    }
}
