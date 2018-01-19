package com.example.mridul.expensemanager;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class DBHelper extends SQLiteOpenHelper {



    private Context context;
    public static final int VERSION = 1;
    public static final String DATE = "Date";
    public static final String TIME = "Time";
    public static final String DATABASE_NAME = "ExpenseDataBase";
    public static final String EXPENSE_TABLE = "ExpenseTable";
    public static final String PENDING_TABLE = "PendingTable";
    public static final String EXPENSE_CAUSE = "ExpenseCause";
    public static final String PENDING_CAUSE = "PendingCause";
    public static final String EXPENSE_AMOUNT = "ExpenseAmount";
    public static final String PENDING_NAME = "PendingName";
    public static final String PENDING_AMOUNT = "PendingAmount";
    public static final String PENDING_STATUS = "Status";
    public static final int GIVE = 0;
    public static final int RECEIVE = 1;



    public DBHelper(Context context) {
        super(context,DATABASE_NAME,null,VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_EXPENSE_TABLE = "CREATE TABLE " + EXPENSE_TABLE + " ( "  + EXPENSE_CAUSE + " VARCHAR(300) , " + EXPENSE_AMOUNT + " INTEGER NOT NULL , " + DATE + " VARCHAR(20) NOT NULL , " + TIME + " VARCHAR(20) NOT NULL);";
        String CREATE_PENDING_TABLE = "CREATE TABLE " + PENDING_TABLE + " ( "  + PENDING_NAME +  " VARCHAR(20) NOT NULL , " + PENDING_AMOUNT  + " INTEGER NOT NULL, " + PENDING_CAUSE + " , " + PENDING_STATUS + " INTEGER  NOT NULL , " + DATE + " VARCHAR(20) NOT NULL , " + TIME + " VARCHAR(20) NOT NULL);";
        try {
            sqLiteDatabase.execSQL(CREATE_EXPENSE_TABLE);
            sqLiteDatabase.execSQL(CREATE_PENDING_TABLE);
            Toast.makeText(context,"Tables Created",Toast.LENGTH_SHORT).show();
        }
        catch (SQLException e) {
            Toast.makeText(context,"Tables Not Created " + e.getMessage(),Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
