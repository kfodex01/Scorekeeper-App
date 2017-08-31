package com.austincreations.scorekeeper;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.austincreations.scorekeeper.R.id.cash;

public class TransactionActivity extends AppCompatActivity {

    private boolean willAdd;
    private Button bnCommit;
    private EditText etAmount;
    private long amount = 0;
    private int trans_id;
    private Uri uri;
    private String action;
    private String transFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        etAmount = (EditText) findViewById(R.id.transaction_amount);
        Intent i = getIntent();
        uri = i.getParcelableExtra(TransactionProvider.CONTENT_ITEM_TYPE);
        trans_id = i.getIntExtra(DBOpenHelper.TRANS_ID, -1);
        if(trans_id == -1) {
            action = Intent.ACTION_INSERT;

            // To find out if this transaction is supposed to be added or minused from total
            willAdd = i.getExtras().getBoolean("willAdd");
            if (willAdd) {
                setTitle(DBOpenHelper.TRANS_INCOME);
            } else {
                setTitle(DBOpenHelper.TRANS_PAYOUT);
            }

            // sets the transaction id, when adding
            Cursor cursor = getContentResolver().query(TransactionProvider.CONTENT_URI, DBOpenHelper.TRANS_ALL_COLUMNS,
                    null, null, null);
            trans_id = 1;
            if (cursor != null) {
                if (cursor.moveToLast()) {
                    trans_id = (cursor.getInt(cursor.getColumnIndex(DBOpenHelper.TRANS_ID))) + 1;
                }
                cursor.close();
            }
        } else
        {
            action = Intent.ACTION_EDIT;
            setTitle("Edit Transaction");
            transFilter = DBOpenHelper.TRANS_ID + "=" + uri.getLastPathSegment();
            Cursor cursor = getContentResolver().query(uri, DBOpenHelper.TRANS_ALL_COLUMNS, transFilter, null, null);
            cursor.moveToFirst();
            long oldAmount = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.TRANS_AMOUNT));
            etAmount.setText("" + oldAmount);
            String type = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TRANS_TYPE));
            TextView tvInstructions = (TextView) findViewById(R.id.tv_transaction_message);
            if (type.equals(DBOpenHelper.TRANS_INCOME)){
                willAdd = true;
                tvInstructions.setText("Enter new income amount (leave blank to delete):");
            } else {
                willAdd = false;
                tvInstructions.setText("Enter new pay out amount (leave blank to delete):");
            }
        }

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
                    switch (action){
                        case Intent.ACTION_INSERT:
                            addTransaction();
                            break;
                        case Intent.ACTION_EDIT:
                            editTransaction();
                            break;
                    }
                    finish();
                }
                else
                {
                    switch (action) {
                        case Intent.ACTION_INSERT:
                            setResult(RESULT_CANCELED);
                            break;
                        case Intent.ACTION_EDIT:
                            deleteTransaction();
                            break;
                    }
                    finish();
                }
            }
        });

    }

    private void deleteTransaction() {
        getContentResolver().delete(TransactionProvider.CONTENT_URI, transFilter, null);
        Toast.makeText(this, "Transaction Deleted", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void editTransaction() {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TRANS_ID, trans_id);
        if(willAdd) {
            values.put(DBOpenHelper.TRANS_TYPE, DBOpenHelper.TRANS_INCOME);
        } else
        {
            values.put(DBOpenHelper.TRANS_TYPE, DBOpenHelper.TRANS_PAYOUT);
        }
        values.put(DBOpenHelper.TRANS_AMOUNT, amount);
        getContentResolver().update(TransactionProvider.CONTENT_URI, values, transFilter, null);
        setResult(RESULT_OK);
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
