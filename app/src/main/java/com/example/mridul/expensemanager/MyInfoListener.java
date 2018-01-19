package com.example.mridul.expensemanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Mridul on 15-01-2018.
 */

public class MyInfoListener implements View.OnClickListener {

    int callFrom;
    Context context;
    Adapter.ExpenseViewHolder holder;
    LayoutInflater inflater;
    View view;

    public MyInfoListener(Context context , int callFrom , Adapter.ExpenseViewHolder holder) {
        this.context = context;
        this.callFrom = callFrom;
        this.holder = holder;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view = inflater.inflate(R.layout.expense_info , null , false));
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimations;

        TextView textView = view.findViewById(R.id.titleHead);
        textView.setText(holder.dateS);
        dialog.show();
    }
}
