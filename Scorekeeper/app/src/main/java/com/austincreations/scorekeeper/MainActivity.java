package com.austincreations.scorekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvCash;
    private long cash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // fills in the cash total
        tvCash = (TextView) findViewById(R.id.cash);
        Intent i = getIntent();
        cash = i.getLongExtra("cash", 0);
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
}
