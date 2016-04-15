package com.maradroid.turinganswer.Activity.Main;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.maradroid.turinganswer.Activity.Base.BaseActivity;
import com.maradroid.turinganswer.Adapter.DialogAdapter;
import com.maradroid.turinganswer.Adapter.RecyclerAdapter;
import com.maradroid.turinganswer.DataModel.Rules;
import com.maradroid.turinganswer.DataModel.VariableSnapshot;
import com.maradroid.turinganswer.ListenerManager.ListenerManager;
import com.maradroid.turinganswer.R;

import java.util.ArrayList;

/**
 * Created by mara on 12/15/15.
 */
public class MainActivity extends BaseActivity implements RecyclerAdapter.ClickListener, DialogAdapter.DialogDataInterface{

    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;

    private DialogFragment dialog;

    private EditText etTape;
    private EditText etAcState;
    private EditText etEmptySpace;
    private EditText etUnconditionalJump;

    private ArrayList<Rules> rulesArray;

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

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        etTape = (EditText) findViewById(R.id.et_tape);
        etAcState = (EditText) findViewById(R.id.et_ac_state);
        etEmptySpace = (EditText) findViewById(R.id.et_empty_space);
        etUnconditionalJump = (EditText) findViewById(R.id.et_unconditional_jump);

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

        dialog = new DialogAdapter();
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

    public void addNewFunctionButton(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", -1);

        openDialog(bundle);
    }

    public void checkButton(View view) { // preventirati dvostruki klik

        ArrayList<String> tapeArray = new ArrayList<>();

        for (char c : etTape.getText().toString().toCharArray()) {
            tapeArray.add(String.valueOf(c));
        }

        VariableSnapshot snapshot = new VariableSnapshot(tapeArray,
                rulesArray,
                etUnconditionalJump.getText().toString(),
                etEmptySpace.getText().toString(),
                etAcState.getText().toString());

        Log.e("maradroid", "startCalculations...");
        startCalculations(snapshot);
    }

    public void automateButton(View view) {
    }

    public void simulationButton(View view) { // napraviti provjeru unosa

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
    }

    @Override
    public Rules getRule(int position) {
        return rulesArray.get(position);
    }


}
