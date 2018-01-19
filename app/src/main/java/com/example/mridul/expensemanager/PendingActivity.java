package com.example.mridul.expensemanager;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class PendingActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton add;
    TextView noEntry;
    RecyclerView RV;
    DBHelper dbHelper;
    SQLiteDatabase database;
    Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add = findViewById(R.id.addNewPending);
        noEntry = findViewById(R.id.noRecordsP);
        RV = findViewById(R.id.pendingList);
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        adapter = new Adapter(this , RV , noEntry ,Adapter.CALL_FROM_PENDING);
        adapter.cursor = database.query(DBHelper.PENDING_TABLE,null,null,null,null,null,null);
        RV.setAdapter(adapter);
        RV.setLayoutManager(new LinearLayoutManager(this));
        add.setOnClickListener(this);
        if(adapter.cursor.getCount() != 0)
            noEntry.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View view) {
        MyDialog dialog = new MyDialog(this , adapter , MyDialog.NEW_PENDING );
        dialog.makeDialogs(null);
    }
}
