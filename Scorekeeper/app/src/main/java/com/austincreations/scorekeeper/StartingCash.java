package com.austincreations.scorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartingCash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_cash);

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
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("cash", cash);
                startActivity(i);
            }
        });

    }
}
