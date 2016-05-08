package com.maradroid.turinganswer.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.maradroid.turinganswer.ListenerManager.ListenerManager;
import com.maradroid.turinganswer.ListenerManager.Listeners.InputSettingsListener;
import com.maradroid.turinganswer.R;

/**
 * Created by mara on 5/8/16.
 */
public class InputSettingsDialog extends DialogFragment {

    public static final String TYPE_TAPE = "tape";
    public static final String TYPE_AC_STATE = "acState";
    public static final String TYPE_EMPTY_SPACE = "emptySpace";
    public static final String TYPE_UNCONDITIONAL_JUMP = "unconditionalJump";

    private String type;
    private String value;

    private EditText etInput;
    private Button btnSubmitButton;
    private Button btnCancelButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        type = getExtras();

        AlertDialog.Builder builder = getDialogBuilder();

        return builder.create();
    }

    private String getExtras() {

        Bundle params = getArguments();
        String type = null;

        if (params != null) {
            type = params.getString("type");

            String tempValue = params.getString("value");

            if (tempValue != null && !tempValue.isEmpty()) {

                if (type != null && type.equals(TYPE_AC_STATE)){
                    value = tempValue.substring(1);

                } else {
                    value = tempValue;
                }


            }
        }

        return type;
    }

    private AlertDialog.Builder getDialogBuilder() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = getDialogView(inflater);

        if (dialogView != null) {
            builder.setView(dialogView);
        }

        return builder;
    }

    private View getDialogView(LayoutInflater inflater) {

        View dialogView = null;

        if (type != null) {

            switch (type) {

                case TYPE_TAPE:
                    dialogView = inflater.inflate(R.layout.dialog_tape, null);
                    break;

                case TYPE_AC_STATE:
                    dialogView = inflater.inflate(R.layout.dialog_ac_state, null);
                    break;

                case TYPE_EMPTY_SPACE:
                    dialogView = inflater.inflate(R.layout.dialog_empty_space, null);
                    break;

                case TYPE_UNCONDITIONAL_JUMP:
                    dialogView = inflater.inflate(R.layout.dialog_unconditional_jump, null);
                    break;
            }
        }

        return dialogView;
    }

    private void initViews() {

        Dialog dialog = getDialog();

        etInput = (EditText) dialog.findViewById(R.id.et_input);

        if (value != null) {
            etInput.setText(value);
        }

        btnSubmitButton = (Button) dialog.findViewById(R.id.btn_submit);
        btnSubmitButton.setOnClickListener(submitButton);

        btnCancelButton = (Button) dialog.findViewById(R.id.btn_cancel);
        btnCancelButton.setOnClickListener(cancelButton);
    }

    private View.OnClickListener submitButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            InputSettingsListener listener = ListenerManager.getInputSettingsListener();

            String value = etInput.getText().toString();

            if (value.isEmpty()) {
                Toast.makeText(getActivity(), getResources().getString(R.string.nema_unosa), Toast.LENGTH_SHORT).show();

            } else if (value.contains(" ")) {
                Toast.makeText(getActivity(), getResources().getString(R.string.neispravan_unos), Toast.LENGTH_SHORT).show();

            }else if (listener != null) {
                listener.onInputSet(type, etInput.getText().toString());
                getDialog().dismiss();
            }

        }
    };

    private View.OnClickListener cancelButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getDialog().dismiss();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        initViews();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ListenerManager.removeInputSettingsListener();
    }
}
