package com.maradroid.turinganswer.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.maradroid.turinganswer.DataModel.Rules;
import com.maradroid.turinganswer.ListenerManager.ListenerManager;
import com.maradroid.turinganswer.R;

/**
 * Created by mara on 4/6/16.
 */
public class DialogAdapter extends DialogFragment {

    private Button btnDialogOk;
    private Button btnDialogCancel;

    private EditText etTrenutnoStanje;
    private EditText etProcitanaVrijednost;
    private EditText etBuduceStanje;
    private EditText etVrijednostPisanja;
    private EditText etPomak;

    private int position = -1;
    private boolean isFirstTime = true;
    private DialogDataInterface dialogDataInterface;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.item_dialog, null);

        builder.setView(dialogView);

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();


        if (isFirstTime) {

            dialogDataInterface = ListenerManager.getDataInterface();

            initViews();

            Rules rule = getExtras();

            if (rule != null) {
                setViews(rule);
                setOkClick(true);

            } else {
                setOkClick(false);
            }

            btnDialogCancel.setOnClickListener(cancelClick);

            isFirstTime = false;
        }
    }

    private void initViews() {

        Dialog dialog = getDialog();

        btnDialogOk = (Button) dialog.findViewById(R.id.btn_submit);
        btnDialogCancel = (Button) dialog.findViewById(R.id.btn_cancel);

        etTrenutnoStanje = (EditText) dialog.findViewById(R.id.et_trenutno_stanje);
        etProcitanaVrijednost = (EditText) dialog.findViewById(R.id.et_procitana_vrijednost);
        etBuduceStanje = (EditText) dialog.findViewById(R.id.et_buduce_stanje);
        etVrijednostPisanja = (EditText) dialog.findViewById(R.id.et_vrijednost_pisanja);
        etPomak = (EditText) dialog.findViewById(R.id.et_pomak);
    }

    private Rules getExtras() {
        
        Bundle params = getArguments();

        if (params != null) {
            position = params.getInt("position");
        }

        if (position != -1) {
            return dialogDataInterface.getRule(position);
        }
        return null;
    }

    private void setViews(Rules rule) {

        etTrenutnoStanje.setText(rule.getTrenutnoStanje());
        etProcitanaVrijednost.setText(rule.getProcitanaVrijednost());
        etBuduceStanje.setText(rule.getBuduceStanje());
        etVrijednostPisanja.setText(rule.getVrijednostPisanja());
        etPomak.setText(rule.getPomak());
    }

    private void setOkClick(final boolean isEdited) {

        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Rules rule = new Rules(etTrenutnoStanje.getText().toString(),
                        etProcitanaVrijednost.getText().toString(),
                        etBuduceStanje.getText().toString(),
                        etVrijednostPisanja.getText().toString(),
                        etPomak.getText().toString());

                if (dialogDataInterface != null) {
                    dialogDataInterface.addNewRule(isEdited, rule, position);

                }

                position = -1;
            }
        });
    }

    View.OnClickListener cancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            position = -1;
            getDialog().cancel();
        }
    };

    public interface DialogDataInterface{
        public void addNewRule(boolean isEdited, Rules rule, int position);
        public Rules getRule(int position);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        position = -1;
    }
}
