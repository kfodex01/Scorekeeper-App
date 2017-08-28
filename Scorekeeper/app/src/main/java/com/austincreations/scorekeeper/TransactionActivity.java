package com.austincreations.scorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TransactionActivity extends AppCompatActivity {

    private boolean willAdd;
    private Button bnCommit;
    private EditText etAmount;
    private long amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        // To find out if this transaction is supposed to be added or minused from total
        Intent i = getIntent();
        willAdd = i.getExtras().getBoolean("willAdd");

        if(willAdd)
        {
            setTitle("Income");
        }
        else
        {
            setTitle("Payout");
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
                    finish();
                }
                else
                {
                    Toast.makeText(TransactionActivity.this, "Please enter a number. Hit back to cancel.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
