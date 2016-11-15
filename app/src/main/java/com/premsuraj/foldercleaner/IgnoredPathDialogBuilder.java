package com.premsuraj.foldercleaner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Premsuraj
 */

public class IgnoredPathDialogBuilder implements TextWatcher {

    public EditText input;

    public AlertDialog build(final Activity activity, final Callback<String> onSuccess, final Callback<Void> onCancel) {
        final View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_input_type, null);
        input = ((EditText) dialogView.findViewById(R.id.edt_type_to_ignore));
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(R.mipmap.ic_launcher)
                .setTitle("Ignore")
                .setView(dialogView)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (onSuccess != null) onSuccess.execute(input.getText().toString());
                    }
                })
                .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (onCancel != null) onCancel.execute(null);
                    }
                });

        input.addTextChangedListener(this);
        return builder.create();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String value = editable.toString();
        if (value.contains("*")) {
            input.setText(value.replace("*", ""));
        }
    }

    public interface Callback<T> {
        void execute(T data);
    }
}
