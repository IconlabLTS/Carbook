package com.iconlab.carbook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class sidemainactivity extends AppCompatActivity {
    Bitmap selectedImage;
    EditText carname, carname2, price;
    Button save;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidemainactivity);
        carname = findViewById(R.id.carname);
        carname2 = findViewById(R.id.carname2);
        price = findViewById(R.id.price);
        save = findViewById(R.id.save);
        imageView = findViewById(R.id.imageView);


    }

    public void selectimage(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


        } else {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery, 2);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent gallery2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery2, 2);


            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Uri imagedata = data.getData();
            if (Build.VERSION.SDK_INT >= 28) {
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imagedata);
                try {
                    selectedImage = ImageDecoder.decodeBitmap(source);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(selectedImage);
            } else {


                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagedata);
                    imageView.setImageBitmap(selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }


        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    public void save(View view) {

        String carnamesave = carname.getText().toString();
        String carnamesave2 = carname2.getText().toString();
        String priceinput = price.getText().toString();
        Bitmap ayarresimi = kucukresim(selectedImage, 300);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ayarresimi.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
        byte[] bytearray = outputStream.toByteArray();


    try {
            SQLiteDatabase database = this.openOrCreateDatabase("Carbook",MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS carbook(id INTEGER PRIMARY KEY ,carnamesave VARCHAR,carnamesave2 VARCHAR,priceinput VARCHAR,ayarresimi BLOB)");
            String sqlstring = "INSERT INTO carbook(carnamesave,carnamesave2,priceinput,ayarresimi)VALUES (?,?,?,?) ";
            SQLiteStatement sqLiteStatement = database.compileStatement(sqlstring);
            sqLiteStatement.bindString(1,carnamesave);
            sqLiteStatement.bindString(2,carnamesave2);
            sqLiteStatement.bindString(3,priceinput);
            sqLiteStatement.bindBlob(4,bytearray);
            sqLiteStatement.execute();
        }catch (Exception e){
        }
        finish();
   }


    public Bitmap kucukresim(Bitmap imageView, int maximumsize) {

        int width = imageView.getWidth();
        int height = imageView.getHeight();
        float bitmapratio = (float) width / (float) height;

        if (bitmapratio > 1) {
            width = maximumsize;
            height = (int) (height / bitmapratio);
        } else {
            height = maximumsize;
            width = (int) (width * bitmapratio);


        }
        return (Bitmap.createScaledBitmap(imageView, width, height, true));
    }
}



