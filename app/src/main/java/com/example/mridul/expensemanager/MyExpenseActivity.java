package com.example.mridul.expensemanager;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MyExpenseActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton add;
    TextView noEntry;
    DBHelper dbHelper;
    SQLiteDatabase database;
    RecyclerView RV;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_expense);

        add = findViewById(R.id.addNewExpense);
        noEntry = findViewById(R.id.noRecordsE);
        RV = findViewById(R.id.expenseList);
        adapter = new Adapter(this, RV , noEntry , Adapter.CALL_FROM_EXPENSE);
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        adapter.cursor = database.query(DBHelper.EXPENSE_TABLE,null,null,null,null,null,null);
        RV.setAdapter(adapter);
        RV.setLayoutManager(new LinearLayoutManager(this));
        add.setOnClickListener(this);
        if(adapter.cursor.getCount() != 0)
            noEntry.setVisibility(View.INVISIBLE);
    }


    //Alert Dialog
    @Override
    public void onClick(View view) {
        MyDialog dialog = new MyDialog(this , adapter , MyDialog.NEW_EXPENSE);
        dialog.makeDialogs( null);
    }

}
