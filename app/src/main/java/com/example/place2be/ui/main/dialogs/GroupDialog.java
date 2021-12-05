package com.example.place2be.ui.main.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.place2be.R;

public class GroupDialog extends Dialog {

    private GroupDialogListener listener;
    private final Button confirmButton;
    private final Button cancelButton;

    public GroupDialog(Context context) {
        super(context);
        this.setContentView(R.layout.group_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.transparent)));
        setCancelable(true);
        show();

        confirmButton = (Button) findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressBar spinner = (ProgressBar) findViewById(R.id.loading_spinner);
                spinner.clearAnimation();
                spinner.setVisibility(View.VISIBLE);

                View progressLayout = (View) findViewById(R.id.progress_layout);
                progressLayout.setVisibility(View.GONE);

                listener.onDialogResult(true);
            }
        });

        cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDialogResult(false);
            }
        });
    }

    public void setListener(GroupDialogListener listener) {
        this.listener = listener;
    }

    public interface GroupDialogListener {
        public void onDialogResult(boolean result);
    }
}
