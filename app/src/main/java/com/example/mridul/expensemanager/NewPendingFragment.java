package com.example.mridul.expensemanager;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;


public class NewPendingFragment extends DialogFragment {

    EditText person,amount,cause;
    RadioButton give,receive;
    View view;
    DBHelper dbHelper;
    SQLiteDatabase database ;
    LayoutInflater inflater;
    ContentValues newEntry;

    public NewPendingFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Defining Variables
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_new_pending,null);
        person = view.findViewById(R.id.person);
        cause = view.findViewById(R.id.expenseCause);
        amount = view.findViewById(R.id.amount);
        give = view.findViewById(R.id.give);
        receive = view.findViewById(R.id.receive);
        newEntry = new ContentValues();
        dbHelper =  new DBHelper(getActivity().getApplicationContext());
        database = dbHelper.getWritableDatabase();

        builder.setView(view);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity().getApplicationContext(),"Negative",Toast.LENGTH_SHORT).show();
            }
        });
        final AlertDialog dialog =   builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimations;
        dialog.show();
        Button add = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!person.getText().toString().equals("") && !amount.getText().toString().equals("")  && (give.isChecked() || receive.isChecked()))
                {
                        newEntry.put(DBHelper.PENDING_AMOUNT,Integer.parseInt(amount.getText().toString()));
                        newEntry.put(DBHelper.PENDING_CAUSE,cause.getText().toString());
                        newEntry.put(DBHelper.PENDING_NAME,person.getText().toString());
                        newEntry.put(DBHelper.PENDING_STATUS,(give.isChecked() ? DBHelper.GIVE : DBHelper.RECEIVE));
                        newEntry.put(DBHelper.DATE, DateFormat.getDateInstance().format(new Date()));
                        newEntry.put(DBHelper.TIME, DateFormat.getTimeInstance().format(new Date()));
                        database.insert(DBHelper.PENDING_TABLE, DBHelper.PENDING_CAUSE,newEntry);
                        Toast.makeText(getActivity().getApplicationContext(),"Positive " + DatabaseUtils.queryNumEntries(database,DBHelper.PENDING_TABLE),Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                }
                else
                    Toast.makeText(getActivity().getApplicationContext(),"Invalid Input",Toast.LENGTH_SHORT).show();

            }
        });
        return dialog;
    }

}
