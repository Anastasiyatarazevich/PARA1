package com.example.para1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class EncodeText extends AppCompatActivity implements EncodingOfTextCallback {
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Encode Class";

    private TextView whether_encoded;
    private ImageView imageView;
    private EditText message;
    private EditText secret_key;

    private EncodingOfText encodingOfText;
    private ImageSteganography imageSteganography;
    private ProgressDialog save;
    private Uri filepath;

    private Bitmap original_image;
    private Bitmap encoded_image;
    public Button save_image_button;

    String wait, saveTxt, saved, select, resolution, encoded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode_text);

        wait = getResources().getString(R.string.wait);
        saveTxt = getResources().getString(R.string.save);
        saved = getResources().getString(R.string.saved);
        select = getResources().getString(R.string.select);
        resolution = getResources().getString(R.string.resolution);
        encoded = getResources().getString(R.string.encoded);

        imageView = findViewById(R.id.imageButton);
        message = findViewById(R.id.message1);
        secret_key = findViewById(R.id.secret_key);

        Button encode_button = findViewById(R.id.decode_button);
        save_image_button = findViewById(R.id.button2);
        save_image_button.setVisibility(View.INVISIBLE);

        checkAndRequestPermissions();
        encode_button.setText(getResources().getString(R.string.textBtn1));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageChooser();
            }
        });

        encode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String key = secret_key.getText().toString().trim();

                if (secret_key != null) {
                if (filepath != null) {
                    if (message.getText() != null) {


                        imageSteganography = new ImageSteganography(message.getText().toString(),
                                secret_key.getText().toString(),
                                original_image);

                        encodingOfText = new EncodingOfText(EncodeText.this, EncodeText.this);

                        encodingOfText.execute(imageSteganography);
                    }
                }
            }
            }
        });


        save_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Bitmap imgToSave = encoded_image;
                Thread PerformEncoding = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        saveToInternalStorage(imgToSave);

                    }
                });
                save = new ProgressDialog(EncodeText.this);
                save.setMessage(wait);
                save.setTitle(saveTxt);
                save.setIndeterminate(false);
                save.setCancelable(false);
                save.show();
                PerformEncoding.start();

            }
        });

    }

    private void ImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, select), SELECT_PICTURE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filepath = data.getData();
            try {
                original_image = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                if(original_image.getHeight()>=200 && original_image.getWidth()>=200 && original_image.getHeight()<=5000 && original_image.getWidth()<=5000){
                imageView.setImageBitmap(original_image);
                    save_image_button.setClickable(true);}
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            resolution, Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (IOException e) {
                Log.d(TAG, "Error : " + e);
            }
        }

    }



    @Override
    public void onStartTextEncoding() {

    }

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {



        if (result != null && result.isEncoded()) {
            encoded_image = result.getEncoded_image();
            Toast toast = Toast.makeText(getApplicationContext(),
                    encoded, Toast.LENGTH_SHORT);
            toast.show();
            imageView.setImageBitmap(encoded_image);
            save_image_button.setVisibility(View.VISIBLE);
        }
    }

    private void saveToInternalStorage(Bitmap bitmapImage) {

        OutputStream fOut;


            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "Encoded" + ".PNG");
            try {
                fOut = new FileOutputStream(file);
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.close();

                whether_encoded.post(new Runnable() {
                    @Override
                    public void run() {
                        save.dismiss();
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();}

        }


    private void checkAndRequestPermissions() {
        int permissionWriteStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int ReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (ReadPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), 1);
        }
    }


}