package com.example.yu.databasetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private String newId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void CreateDatabase(View view) {
        dbHelper.getWritableDatabase();
    }

    public void AddData(View view) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        //��ʼ��װ��һ������
        values.put("name", "The Da Vinci Code");
        values.put("author", "Dan Brown");
        values.put("pages", 454);
        values.put("price", 16.96);
        db.insert("Book", null, values);//�����һ������
        values.clear();
        //��ʼ��װ�ڶ�������
        values.put("name", "The Lost Symbol");
        values.put("author", "Dan Brown");
        values.put("pages", 510);
        values.put("price", 19.95);
        db.insert("Book", null, values);//����ڶ�������

    }

    public void UpdateData(View view) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("price", 10.99);
        db.update("Book", values, "name = ?", new String[] { "The Da Vinci Code"});
    }

    public void DeleteData(View view) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Book", "pages > ?", new String[] { "500" });
    }

    public void QueryData(View view) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //��ѯBook���е���������
        Cursor cursor = db.query("Book", null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            do {
                //����Cursor����ȡ�����ݲ���ӡ
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));

                Log.d("MainActivity", "book name is "+ name);
                Log.d("MainActivity", "book author is "+ author);
                Log.d("MainActivity", "book pages is "+ pages);
                Log.d("MainActivity", "book price is "+ price);

            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void ReplaceData(View view) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();  //��������
        try {
            db.delete("Book", null, null);
            if (true){
                //�ֶ��׳�һ���쳣��������ʧ��
//                throw new NullPointerException();
            }
            ContentValues values = new ContentValues();
            values.put("name", "Game of Thrones");
            values.put("author", "George Martin");
            values.put("pages", 720);
            values.put("price", 20.85);
            db.insert("Book", null, values);
            db.setTransactionSuccessful();  //�����Ѿ�ִ�гɹ�
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();    //�������
        }
    }

    public void AddToBook(View view) {
        //�������
        Uri uri = Uri.parse("content://com.example.yu.databasetest.provider/book");
        ContentValues values = new ContentValues();
        values.put("name", "A Clash of Kings");
        values.put("author", "George Martin");
        values.put("pages", 1040);
        values.put("price", 22.85);
        Uri newUri = getContentResolver().insert(uri,values);
        newId = newUri.getPathSegments().get(1);
    }

    public void QueryBook(View view) {
        //��ѯ����
        Uri uri = Uri.parse("content://com.example.yu.databasetest.provider/book");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null){
            while (cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));

                Log.d("MainActivity", "book name is " + name);
                Log.d("MainActivity", "book author is " + author);
                Log.d("MainActivity", "book pages is " + pages);
                Log.d("MainActivity", "book price is " + price);

            }
            cursor.close();
        }
    }

    public void UpdateBook(View view) {
        //��������
        Uri uri = Uri.parse("content://com.example.yu.databasetest.provider/book/" + newId);
        ContentValues values = new ContentValues();
        values.put("name", "A Storm of Swords");
        values.put("pages", 1216);
        values.put("price", 24.05);
        getContentResolver().update(uri, values, null, null);
    }

    public void DeleteBook(View view) {
        //ɾ������
        Uri uri = Uri.parse("content://com.example.yu.databasetest.provider/book/" + newId);
        getContentResolver().delete(uri, null, null);
    }
}
