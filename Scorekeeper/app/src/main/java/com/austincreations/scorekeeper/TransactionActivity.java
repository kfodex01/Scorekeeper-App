package com.austincreations.scorekeeper;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.austincreations.scorekeeper.R.id.cash;

public class TransactionActivity extends AppCompatActivity {

    private boolean willAdd;
    private Button bnCommit;
    private EditText etAmount;
    private long amount = 0;
    private int trans_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        // To find out if this transaction is supposed to be added or minused from total
        Intent i = getIntent();
        willAdd = i.getExtras().getBoolean("willAdd");

        if(willAdd)
        {
            setTitle(DBOpenHelper.TRANS_INCOME);
        }
        else
        {
            setTitle(DBOpenHelper.TRANS_PAYOUT);
        }
        Cursor cursor = getContentResolver().query(TransactionProvider.CONTENT_URI, DBOpenHelper.TRANS_ALL_COLUMNS,
                null, null, null);
        trans_id  = 1;
        if (cursor != null) {
            if (cursor.moveToLast()) {
                trans_id = (cursor.getInt(cursor.getColumnIndex(DBOpenHelper.TRANS_ID))) + 1;
            }
        }

        etAmount = (EditText) findViewById(R.id.transaction_amount);
        bnCommit = (Button) findViewById(R.id.commit_transaction);
        bnCommit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(etAmount.getText().length() != 0){
                    try {
                        amount = Long.parseLong(etAmount.getText().toString());
                    }
                    catch (NumberFormatException e)
                    {
                        Toast.makeText(TransactionActivity.this, "This number is far too large. Please try again.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    addTransaction();
                    finish();
                }
                else
                {
                    Toast.makeText(TransactionActivity.this, "Please enter a number. Hit back to cancel.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void addTransaction() {

        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TRANS_ID, trans_id);
        if(willAdd) {
            values.put(DBOpenHelper.TRANS_TYPE, DBOpenHelper.TRANS_INCOME);
        } else
        {
            values.put(DBOpenHelper.TRANS_TYPE, DBOpenHelper.TRANS_PAYOUT);
        }
        values.put(DBOpenHelper.TRANS_AMOUNT, amount);
        getContentResolver().insert(TransactionProvider.CONTENT_URI, values);
        setResult(RESULT_OK);

    }
}
