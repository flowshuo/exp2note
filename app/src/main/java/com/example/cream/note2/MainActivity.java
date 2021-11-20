package com.example.cream.note2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private ListView mList;
    private Intent mIntent;
    private MyAdapter mAdapter;
    private NoteDb mNotedb;
    private Cursor cursor;
    private SQLiteDatabase dbreader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = (ListView) this.findViewById(R.id.list);
        mNotedb = new NoteDb(this);
        dbreader = mNotedb.getReadableDatabase();

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cursor.moveToPosition(i);
                Intent intent = new Intent(MainActivity.this, ShowContent.class);
                intent.putExtra(NoteDb.ID, cursor.getInt(cursor.getColumnIndex(NoteDb.ID)));
                intent.putExtra(NoteDb.TITLE, cursor.getString(cursor.getColumnIndex(NoteDb.TITLE)));
                intent.putExtra(NoteDb.TIME, cursor.getString(cursor.getColumnIndex(NoteDb.TIME)));
                intent.putExtra(NoteDb.AUTHOR, cursor.getString(cursor.getColumnIndex(NoteDb.AUTHOR)));
                intent.putExtra(NoteDb.CONTENT, cursor.getString(cursor.getColumnIndex(NoteDb.CONTENT)));
                startActivity(intent);
            }
        });
    }

    public void add(View v) {
        mIntent = new Intent(MainActivity.this, AddContent.class);
        startActivity(mIntent);
    }
    public void author(View v) {
        mIntent = new Intent(MainActivity.this, EditAuthor.class);
        startActivity(mIntent);
    }

    public void selectDb() {
        cursor = dbreader.query
                (NoteDb.TABLE_NAME, null, null, null, null, null, null);
        mAdapter = new MyAdapter(this, cursor);
        mList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDb();
    }
}