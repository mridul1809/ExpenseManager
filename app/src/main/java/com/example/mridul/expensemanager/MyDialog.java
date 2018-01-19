package com.example.mridul.expensemanager;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Mridul on 18-01-2018.
 */

public class MyDialog {

    Context context;
    Adapter adapter;
    View dialogView;
    LayoutInflater inflater;
    Button customPositive;
    DBHelper dbHelper;
    SQLiteDatabase database;
    public static final int NEW_EXPENSE = 0;
    public static final int EDIT_EXPENSE = 1;
    public static final int NEW_PENDING = 2;
    public static final int EDIT_PENDING = 3;
    EditText cause , amount , person;
    int callFrom ;
    RadioButton give , recieve;

    public MyDialog(Context context , Adapter adapter , int callFrom) {
        this.context = context;
        this.adapter = adapter;
        inflater = LayoutInflater.from(context);
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        this.callFrom = callFrom;
    }

    public void makeDialogs( final ContentValues oldEntry) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(callFrom == NEW_EXPENSE || callFrom == EDIT_EXPENSE)
            dialogView  = inflater.inflate(R.layout.fragment_new_expense , null , false);
        else
            dialogView = inflater.inflate(R.layout.fragment_new_pending , null , false);
        builder.setView(dialogView);
        if(callFrom == EDIT_PENDING) {
            cause = dialogView.findViewById(R.id.pendingCause);
            amount =  dialogView.findViewById(R.id.amount);
            person = dialogView.findViewById(R.id.person);
            give = dialogView.findViewById(R.id.give);
            recieve = dialogView.findViewById(R.id.receive);
            cause.setText(oldEntry.getAsString(DBHelper.PENDING_CAUSE));
            amount.setText(oldEntry.getAsString(DBHelper.PENDING_AMOUNT));
            person.setText(oldEntry.getAsString(DBHelper.PENDING_NAME));
            give.setChecked(oldEntry.getAsInteger(DBHelper.PENDING_STATUS) == DBHelper.GIVE);
            recieve.setChecked(oldEntry.getAsInteger(DBHelper.PENDING_STATUS) == DBHelper.RECEIVE);
        }
        else if(callFrom == EDIT_EXPENSE) {
            cause = dialogView.findViewById(R.id.expenseCause);
            amount = dialogView.findViewById(R.id.amount);

            cause.setText(oldEntry.getAsString(DBHelper.EXPENSE_CAUSE));
            amount.setText(oldEntry.getAsString(DBHelper.EXPENSE_AMOUNT));
        }
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        final AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimations;
        dialog.show();
        customPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        customPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( validate( oldEntry) )
                    dialog.dismiss();
            }
        });
    }

    private boolean validate( ContentValues oldEntry) {

        if(callFrom == NEW_EXPENSE) {
            amount = dialogView.findViewById(R.id.amount);
            cause = dialogView.findViewById(R.id.expenseCause);
        }
        if(callFrom == NEW_PENDING) {
            amount = dialogView.findViewById(R.id.amount);
            cause = dialogView.findViewById(R.id.pendingCause);
            person = dialogView.findViewById(R.id.person);
            give = dialogView.findViewById(R.id.give);
            recieve = dialogView.findViewById(R.id.receive);
        }
        if(callFrom == NEW_EXPENSE || callFrom == EDIT_EXPENSE) {
            if(!amount.getText().toString().equals("")) {
                ContentValues newEntry = new ContentValues();
                newEntry.put(DBHelper.EXPENSE_CAUSE,cause.getText().toString());
                newEntry.put(DBHelper.EXPENSE_AMOUNT,Integer.parseInt(amount.getText().toString()));
                if(callFrom == NEW_EXPENSE) {
                    newEntry.put(DBHelper.DATE, DateFormat.getDateInstance().format(new Date()));
                    newEntry.put(DBHelper.TIME, DateFormat.getTimeInstance().format(new Date()));
                    insert(newEntry );
                }
                else
                    update(newEntry , oldEntry);
                return true;
            }
            else {
                Toast.makeText(context , "Invalid Input" , Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else {
            if(!amount.getText().toString().equals("") && !person.getText().toString().equals("") && (give.isChecked() || recieve.isChecked())) {
                ContentValues newEntry = new ContentValues();
                newEntry.put(DBHelper.PENDING_CAUSE,cause.getText().toString());
                newEntry.put(DBHelper.PENDING_AMOUNT,Integer.parseInt(amount.getText().toString()));
                newEntry.put(DBHelper.PENDING_STATUS, give.isChecked()? DBHelper.GIVE : DBHelper.RECEIVE);
                newEntry.put(DBHelper.PENDING_NAME , person.getText().toString());
                if(callFrom == NEW_PENDING) {
                    newEntry.put(DBHelper.DATE, DateFormat.getDateInstance().format(new Date()));
                    newEntry.put(DBHelper.TIME, DateFormat.getTimeInstance().format(new Date()));
                    insert(newEntry );
                }
                else
                    update(newEntry , oldEntry);
                return true;
            }
            else {
                Toast.makeText(context , "Invalid Input" , Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    public void insert(ContentValues newEntry) {
        if(callFrom == NEW_EXPENSE )
            database.insert(DBHelper.EXPENSE_TABLE , DBHelper.EXPENSE_CAUSE , newEntry);
        else
            database.insert(DBHelper.PENDING_TABLE , DBHelper.PENDING_CAUSE , newEntry);
        adapter.insert(database);

    }

    public void update(ContentValues newEntry , ContentValues oldEntry) {
        database.update(callFrom == EDIT_EXPENSE ? DBHelper.EXPENSE_TABLE : DBHelper.PENDING_TABLE , newEntry  , DBHelper.DATE + "=? AND " + DBHelper.TIME + "=?",new String[] {oldEntry.getAsString(DBHelper.DATE) , oldEntry.getAsString(DBHelper.TIME)});
        adapter.insert(database);

    }
}

