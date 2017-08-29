package com.austincreations.scorekeeper;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvCash;
    private long cash = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //totals up the cash
        sumTransactions();

        // fills in the cash total
        tvCash = (TextView) findViewById(R.id.cash);
        tvCash.setText("$" + cash);

        // sets event handlers for buttons
        Button bnIncome = (Button) findViewById(R.id.income);
        Button bnPayout = (Button) findViewById(R.id.payout);
        bnIncome.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(), TransactionActivity.class);
                i.putExtra("willAdd", true);
                startActivity(i);
            }
        });
        bnPayout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(), TransactionActivity.class);
                i.putExtra("willAdd", false);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_new_game) {
            getContentResolver().delete(TransactionProvider.CONTENT_URI, null, null);
            finish();
        }

        return super.onOptionsItemSelected(item);

    }

            private void sumTransactions(){

        Cursor cursor = getContentResolver().query(TransactionProvider.CONTENT_URI, DBOpenHelper.TRANS_ALL_COLUMNS,
                null, null, null);
        if(cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            String transType = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TRANS_TYPE));
            long amount = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.TRANS_AMOUNT));
            if (transType.equals(DBOpenHelper.TRANS_INCOME))
            {
                cash += amount;
            } else {
                cash -= amount;
            }
            while(cursor.moveToNext())
            {
                transType = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TRANS_TYPE));
                amount = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.TRANS_AMOUNT));
                if (transType.equals(DBOpenHelper.TRANS_INCOME))
                {
                    cash += amount;
                } else {
                    cash -= amount;
                }
            }
        }

    }
}
