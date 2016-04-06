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
import android.widget.Button;

import com.maradroid.turinganswer.Adapter.DialogAdapter;
import com.maradroid.turinganswer.Adapter.RecyclerAdapter;
import com.maradroid.turinganswer.DataModel.Rules;
import com.maradroid.turinganswer.ListenerManager.ListenerManager;
import com.maradroid.turinganswer.R;

import java.util.ArrayList;

/**
 * Created by mara on 12/15/15.
 */
public class MainActivity extends AppCompatActivity implements RecyclerAdapter.ClickListener, DialogAdapter.DialogDataInterface{

    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;

    DialogFragment dialog;

    private ArrayList<Rules> rulesDataArray;

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

        initRecycler();
    }

    private void initRecycler() {

        rulesDataArray = new ArrayList<>();

        mRecycler = (RecyclerView) findViewById(R.id.recycler_view);
        mRecycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(rulesDataArray);
        mAdapter.setClickListener(this);
        mRecycler.setAdapter(mAdapter);

    }

    private void initDialog() {

        ListenerManager.setDataInterface(this);

    }

    private void removeRule(int position) {
        rulesDataArray.remove(position);
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

    public void checkButton(View view) {

    }

    public void automateButton(View view) {
    }

    public void simulationButton(View view) {
    }

    @Override
    public void addNewRule(boolean isEdited, Rules rule, int position) {///////////////// postaviti provjere ispravnosti

        if (rule != null) {

            if (isEdited && position != -1) {
                rulesDataArray.set(position, rule);
                mAdapter.notifyItemChanged(position);

            } else {
                rulesDataArray.add(rule);
                mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
            }
        }

        dialog.dismiss();
    }

    @Override
    public Rules getRule(int position) {
        return rulesDataArray.get(position);
    }


}
