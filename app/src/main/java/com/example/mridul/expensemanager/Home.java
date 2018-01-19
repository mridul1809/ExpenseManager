package com.example.mridul.expensemanager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Home extends AppCompatActivity implements View.OnClickListener , View.OnLongClickListener{

    //Declarations
    Button myExpenses,pending;

    //Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        myExpenses = (Button) findViewById(R.id.myExpense);
        pending = (Button) findViewById(R.id.pending);
        myExpenses.setOnClickListener(this);
        pending.setOnClickListener(this);
        myExpenses.setOnLongClickListener(this);
        pending.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.myExpense :
                startActivity(new Intent(this,MyExpenseActivity.class));
                break;

            case R.id.pending :
                startActivity(new Intent(this,PendingActivity.class));
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId())
        {
            case R.id.myExpense :
                Toast.makeText(this,"My Previous Expenses",Toast.LENGTH_SHORT).show();
                break;
            case R.id.pending :
                Toast.makeText(this,"My Pending Transactions",Toast.LENGTH_SHORT).show();
                break;
            default:
                return false;
        }
        return true;
    }
}
