package com.austincreations.scorekeeper;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView tvCash;
    private static final int EDITOR_REQUEST_CODE = 5;
    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cursorAdapter = new TransactionCursorAdapter(this, null, 0);
        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);

        getLoaderManager().initLoader(0, null, this);

        //totals up the cash
        tvCash = (TextView) findViewById(R.id.cash);
        sumTransactions();

        // sets event handlers for buttons
        Button bnIncome = (Button) findViewById(R.id.income);
        Button bnPayout = (Button) findViewById(R.id.payout);
        bnIncome.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(), TransactionActivity.class);
                i.putExtra("willAdd", true);
                startActivityForResult(i, EDITOR_REQUEST_CODE);
            }
        });
        bnPayout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(), TransactionActivity.class);
                i.putExtra("willAdd", false);
                startActivityForResult(i, EDITOR_REQUEST_CODE);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, TransactionActivity.class);
                Uri uri = Uri.parse(TransactionProvider.CONTENT_URI + "/" + id);
                i.putExtra(TransactionProvider.CONTENT_ITEM_TYPE, uri);
                i.putExtra(DBOpenHelper.TRANS_ID, (int)id);
                startActivityForResult(i, EDITOR_REQUEST_CODE);
            }
        });
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

        long cash = 0;
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
            cursor.close();
        }

        // fills in the cash total
        String text = "$" + cash;
        tvCash.setText(text);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new android.content.CursorLoader(this, TransactionProvider.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
        sumTransactions();
    }

    private Loader<Cursor> restartLoader() {
        sumTransactions();
        return getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        sumTransactions();
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK){
            restartLoader();
        }
    }

}
