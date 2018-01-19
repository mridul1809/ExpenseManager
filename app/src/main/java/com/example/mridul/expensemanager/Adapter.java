package com.example.mridul.expensemanager;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Adapter extends RecyclerView.Adapter<Adapter.ExpenseViewHolder>{


    private Context context;
    LayoutInflater inflater;
    Cursor cursor;
    RecyclerView RV;
    TextView noEntry;
    int callFrom;
    public static int CALL_FROM_EXPENSE = 0;
    public static int CALL_FROM_PENDING = 1;


    public Adapter(Context context , RecyclerView RV , TextView noEntry, int callFrom) {
        this.context = context;
        this.RV = RV;
        inflater = LayoutInflater.from(context);
        this.noEntry = noEntry;
        this.callFrom = callFrom;
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = inflater.inflate((callFrom == CALL_FROM_EXPENSE? R.layout.expense_row : R.layout.pending_row),parent,false);
        final ExpenseViewHolder holder =  new ExpenseViewHolder(row , callFrom);
        //row.setOnClickListener(new MyInfoListener(context , callFrom , holder));
        return  holder;
    }

    @Override
    public void onBindViewHolder(final ExpenseViewHolder holder, final int position) {

        cursor.moveToPosition(position);
        if(callFrom == CALL_FROM_EXPENSE) {
            holder.cause.setText(cursor.getString(cursor.getColumnIndex(DBHelper.EXPENSE_CAUSE)));
            holder.amount.setText(cursor.getString(cursor.getColumnIndex(DBHelper.EXPENSE_AMOUNT)));
            holder.date.setText(cursor.getString(cursor.getColumnIndex(DBHelper.DATE)));
            holder.dateS = holder.date.getText().toString();
            holder.time.setText(cursor.getString(cursor.getColumnIndex(DBHelper.TIME)));
            holder.timeS = holder.time.getText().toString();
        }

        if(callFrom == CALL_FROM_PENDING) {

            holder.cause.setText(cursor.getString(cursor.getColumnIndex(DBHelper.PENDING_CAUSE)));
            holder.amount.setText(cursor.getString(cursor.getColumnIndex(DBHelper.PENDING_AMOUNT)));
            holder.date.setText(cursor.getString(cursor.getColumnIndex(DBHelper.DATE)));
            holder.dateS = holder.date.getText().toString();
            holder.time.setText(cursor.getString(cursor.getColumnIndex(DBHelper.TIME)));
            holder.timeS = holder.time.getText().toString();
            holder.person.setText(cursor.getString(cursor.getColumnIndex(DBHelper.PENDING_NAME)));
            if(cursor.getInt(cursor.getColumnIndex(DBHelper.PENDING_STATUS)) == DBHelper.GIVE) {
                holder.rootLayout.setBackgroundColor(context.getResources().getColor(R.color.giveColor));
                holder.status = DBHelper.GIVE;
            }
            else {
                holder.rootLayout.setBackgroundColor(context.getResources().getColor(R.color.takeColor));
                holder.status = DBHelper.RECEIVE;
            }

        }

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }


    public class ExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        TextView cause,amount,date,time,person;
        String dateS,timeS;
        int callFrom;
        int status;
        LinearLayout rootLayout;
        public ExpenseViewHolder(View itemView,  int callFrom) {
            super(itemView);
            this.callFrom = callFrom;
            if(callFrom == CALL_FROM_PENDING) {
                person = itemView.findViewById(R.id.personText);
                rootLayout = itemView.findViewById(R.id.parentLL);
            }
            cause = itemView.findViewById(R.id.causeText);
            amount = itemView.findViewById(R.id.amountText);
            date = itemView.findViewById(R.id.dateText);
            time = itemView.findViewById(R.id.timeText);
            dateS = date.getText().toString();
            timeS = time.getText().toString();
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            contextMenu.setHeaderTitle("Options");
            MenuItem edit = contextMenu.add(Menu.NONE , 1 , 0 , "Edit");
            MenuItem delete = contextMenu.add(Menu.NONE , 2 , 1 , "Delete");
            contextMenu.add(Menu.NONE , 3 , 2 , "Cancel");
            edit.setOnMenuItemClickListener(callFrom == CALL_FROM_EXPENSE? clickedExpense : clickedPending);
            delete.setOnMenuItemClickListener(clickedExpense);
        }

        private final MenuItem.OnMenuItemClickListener clickedExpense = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if(menuItem.getItemId() == 1) {
                    MyDialog dialog = new MyDialog(context , getAdapter() , MyDialog.EDIT_EXPENSE);
                    ContentValues oldEntry = new ContentValues();
                    oldEntry.put(DBHelper.DATE , dateS);
                    oldEntry.put(DBHelper.TIME , timeS);
                    oldEntry.put(DBHelper.EXPENSE_CAUSE , cause.getText().toString());
                    oldEntry.put(DBHelper.EXPENSE_AMOUNT , amount.getText().toString());
                    dialog.makeDialogs( oldEntry);
                }
                if(menuItem.getItemId() == 2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Delete Selected Item ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            delete(dateS,timeS);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create().show();
                }
                return true;
            }
        };


        private final MenuItem.OnMenuItemClickListener clickedPending = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId() == 1) {
                    MyDialog dialog = new MyDialog(context , getAdapter() , MyDialog.EDIT_PENDING);
                    ContentValues oldEntry = new ContentValues();
                    oldEntry.put(DBHelper.DATE , dateS);
                    oldEntry.put(DBHelper.TIME , timeS);
                    oldEntry.put(DBHelper.PENDING_CAUSE , cause.getText().toString());
                    oldEntry.put(DBHelper.PENDING_AMOUNT , amount.getText().toString());
                    oldEntry.put(DBHelper.PENDING_NAME , person.getText().toString());
                    oldEntry.put(DBHelper.PENDING_STATUS , status);
                    dialog.makeDialogs( oldEntry);
                }
                return true;
            }
        };
    }



    Adapter getAdapter() {
        return  this;
    }

    public void insert(SQLiteDatabase database) {
        cursor = database.query((callFrom == CALL_FROM_EXPENSE ?DBHelper.EXPENSE_TABLE : DBHelper.PENDING_TABLE ),null,null,null,null,null,null);
        if(cursor.getCount() != 0)
            noEntry.setVisibility(View.INVISIBLE);
        this.notifyDataSetChanged();
    }

    public void delete(String dateS , String timeS) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(callFrom == CALL_FROM_EXPENSE? DBHelper.EXPENSE_TABLE : DBHelper.PENDING_TABLE, DBHelper.DATE + " =? AND " + DBHelper.TIME + " =? " , new String[] {dateS , timeS} );
        cursor = database.query((callFrom == CALL_FROM_EXPENSE ?DBHelper.EXPENSE_TABLE : DBHelper.PENDING_TABLE ),null,null,null,null,null,null);
        this.notifyDataSetChanged();
        if(cursor.getCount() == 0)
            noEntry.setVisibility(View.VISIBLE);
    }


}
