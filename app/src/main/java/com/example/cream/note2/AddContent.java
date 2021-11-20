package com.example.cream.note2;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddContent extends AppCompatActivity {
    private EditText mEt;
    private EditText mTitle;
    private NoteDb mDb;
    private SQLiteDatabase mSqldb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);
        mEt = (EditText) this.findViewById(R.id.text);
        mTitle = (EditText) this.findViewById(R.id.title);
        mDb = new NoteDb(this);
        mSqldb = mDb.getWritableDatabase();
    }

    public void save(View v) {
        ContentValues cv = new ContentValues();
        cv.put(NoteDb.CONTENT, mEt.getText().toString());
        cv.put(NoteDb.TIME, getTime());
        cv.put(NoteDb.TITLE, mTitle.getText().toString());
        SharedPreferences prefs = getSharedPreferences("data", MODE_PRIVATE);
        String names = prefs.getString("name","无名");
        cv.put(NoteDb.AUTHOR, names);
        mSqldb.insert(NoteDb.TABLE_NAME, null, cv);
        finish();
    }

    public static String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        String str = sdf.format(date);
        return str;
    }
    public void pic(View v){
        Intent intent;
        //添加图片的主要代码
        intent = new Intent();
        //设定类型为image
        intent.setType("image/*");
        //设置action
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //选中相片后返回本Activity
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            //取得数据
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            Bitmap bitmap = null;
            Bundle extras = null;
            //如果是选择照片
            if(requestCode == 1){

                try {
                    //将对象存入Bitmap中
                    bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                ContentValues cv = new ContentValues();
                cv.put("content", baos.toByteArray());
                mSqldb.insert("image", null, cv);
            }

            int imgWidth = bitmap.getWidth();
            int imgHeight = bitmap.getHeight();
            double partion = imgWidth*1.0/imgHeight;
            double sqrtLength = Math.sqrt(partion*partion + 1);
            //新的缩略图大小
            double newImgW = 480*(partion / sqrtLength);
            double newImgH = 480*(1 / sqrtLength);
            float scaleW = (float) (newImgW/imgWidth);
            float scaleH = (float) (newImgH/imgHeight);

            Matrix mx = new Matrix();
            //对原图片进行缩放
            mx.postScale(scaleW, scaleH);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight, mx, true);
            final ImageSpan imageSpan = new ImageSpan(this,bitmap);


            Cursor cursor = mSqldb.query("image",null, null, null, null, null, null);
            String picname = cursor.getCount()+"";
            SpannableString spannableString = new SpannableString("[pic="+picname+"]");
            spannableString.setSpan(imageSpan, 0, spannableString.length(), SpannableString.SPAN_MARK_MARK);
            //光标移到下一行
            mEt.append("\n");
            Editable editable = mEt.getEditableText();
            int selectionIndex = mEt.getSelectionStart();
            spannableString.getSpans(0, spannableString.length(), ImageSpan.class);

            //将图片添加进EditText中
            editable.insert(selectionIndex, spannableString);
            //添加图片后自动空出两行
            mEt.append("\n\n");
        }
    }
}