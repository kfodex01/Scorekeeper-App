package com.austincreations.scorekeeper;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.austincreations.scorekeeper.R.string.commit;

public class StartingCash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_cash);

        Cursor cursor = getContentResolver().query(TransactionProvider.CONTENT_URI, DBOpenHelper.TRANS_ALL_COLUMNS,
                null, null, null);
        if(cursor != null && cursor.getCount() > 0){
            goToMainScreen();
        }

        Button commit = (Button) findViewById(R.id.commit_start);
        commit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                EditText etStartingCash = (EditText) findViewById(R.id.starting_cash);

                // if the line is blank, makes starting cash zero.
                long cash = 0;
                if(etStartingCash.getText().length() != 0){
                    try {
                        cash = Long.parseLong(etStartingCash.getText().toString());
                    }
                    catch (NumberFormatException e)
                    {
                        Toast.makeText(StartingCash.this, "This number is far too large. Please try again.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                addTransaction(cash);
                goToMainScreen();

            }
        });

    }

    private void addTransaction(long cash) {

        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TRANS_ID, 1);
        values.put(DBOpenHelper.TRANS_TYPE, DBOpenHelper.TRANS_INCOME);
        values.put(DBOpenHelper.TRANS_AMOUNT, cash);
        getContentResolver().insert(TransactionProvider.CONTENT_URI, values);
        setResult(RESULT_OK);

    }

    private void goToMainScreen() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}
