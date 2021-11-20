package com.example.cream.note2;



import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditAuthor extends AppCompatActivity {
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);
        name = (EditText) this.findViewById(R.id.name);
        SharedPreferences prefs = getSharedPreferences("data", MODE_PRIVATE);
        String names = prefs.getString("name","无名");
        name.setText(names);
    }

    public void save(View v) {
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("name", name.getText().toString());
        editor.commit();
        finish();
    }
}