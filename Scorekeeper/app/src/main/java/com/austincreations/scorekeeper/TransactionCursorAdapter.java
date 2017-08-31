package com.austincreations.scorekeeper;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class TransactionCursorAdapter extends CursorAdapter {

    TransactionCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.list_item_layout, parent, false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String type = cursor.getString(
                cursor.getColumnIndex(DBOpenHelper.TRANS_TYPE));
        String amount = "$" + cursor.getLong(
                cursor.getColumnIndex(DBOpenHelper.TRANS_AMOUNT));
        TextView tvType = (TextView) view.findViewById(R.id.tvType);
        TextView tvAmount = (TextView) view.findViewById(R.id.tvAmount);
        tvType.setText(type);
        tvAmount.setText(amount);
    }
}
