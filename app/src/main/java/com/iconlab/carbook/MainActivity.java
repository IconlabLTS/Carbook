package com.iconlab.carbook;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> arrayname;
    ArrayList<Integer> idarray;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listview);
        getData();
        arrayname = new ArrayList<>();
        idarray = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayname);
        listView.setAdapter(arrayAdapter);



    }

    public void getData() {
        try {
            SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("Carbook", MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM carbook", null);
            int nameix = cursor.getColumnIndex("carnamesave");
            int idix = cursor.getColumnIndex("id");

            while (cursor.moveToNext()) {
                arrayname.add(cursor.getString(nameix));
                idarray.add(cursor.getInt(idix));


            } cursor.close();
            arrayAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflater
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.addcar, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.ekle) {
            Intent intent = new Intent(MainActivity.this, sidemainactivity.class);

            startActivity(intent);


        }

        return super.onOptionsItemSelected(item);
    }


}