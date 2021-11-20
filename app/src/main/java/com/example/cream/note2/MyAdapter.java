package com.example.cream.note2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
    private Context mContext;
    private Cursor mCursor;
    private LinearLayout mLayout;

    public MyAdapter(Context mContext, Cursor mCursor) {
        this.mContext = mContext;
        this.mCursor = mCursor;
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return mCursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mLayout = (LinearLayout) inflater.inflate(R.layout.item, null);
        TextView content = (TextView) mLayout.findViewById(R.id.list_content);
        TextView time = (TextView) mLayout.findViewById(R.id.list_time);
        mCursor.moveToPosition(position);
        String dbcontext = mCursor.getString(mCursor.getColumnIndex("title"));
        String author = mCursor.getString(mCursor.getColumnIndex("author"));
        String dbtime = mCursor.getString(mCursor.getColumnIndex("time"));
        content.setText(dbcontext);
        time.setText(author+" "+dbtime);
        return mLayout;
    }
}