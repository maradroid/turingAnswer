package com.maradroid.turinganswer.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maradroid.turinganswer.R;

/**
 * Created by mara on 5/11/16.
 */
public class ResultDialog extends DialogFragment {

    private TextView tvTitle;
    private TextView tvResult;

    private Button btnOK;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_result, null);

        builder.setView(dialogView);

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();

        initViews();
        getExtras();

    }

    private void initViews() {

        Dialog dialog = getDialog();

        tvTitle = (TextView) dialog.findViewById(R.id.tv_title);
        tvResult = (TextView) dialog.findViewById(R.id.tv_result);

        btnOK = (Button) dialog.findViewById(R.id.btn_ok);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void getExtras() {

        Bundle arg = getArguments();

        if (arg != null) {
            tvResult.setText(arg.getString("result"));
            tvTitle.setText(arg.getString("title"));
        }
    }
}
