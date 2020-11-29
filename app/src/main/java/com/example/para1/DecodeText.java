package com.example.para1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;


import java.io.IOException;

public class DecodeText extends AppCompatActivity implements DecodingOfTextCallback {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Decode Class";
    private TextView textView;
    private ImageView imageView;
    private TextView message;
    private EditText secret_key;
    private Uri filepath;
    private Bitmap original_image;

    String select1, resolution1, nomes, decoded, nosecretkey, selectfirst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode_text1);
         select1  = getResources().getString(R.string.select);
        resolution1  = getResources().getString(R.string.resolution);
        nomes  = getResources().getString(R.string.nomessage);
        decoded  = getResources().getString(R.string.decoded);
        nosecretkey  = getResources().getString(R.string.nosecretkey);
        selectfirst  = getResources().getString(R.string.selectimagefirst);
        textView = findViewById(R.id.message1);
        imageView = findViewById(R.id.imageButton1);
        message = findViewById(R.id.message1);
        secret_key = findViewById(R.id.secret_key1);
        Button decode_button = findViewById(R.id.decode_button1);
        decode_button.setText(getResources().getString(R.string.decode));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageChooser();
            }
        });
        decode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filepath != null) {
                    ImageSteganography imageSteganography = new ImageSteganography(secret_key.getText().toString(),
                            original_image);
                    DecodingOfText decodingOfText = new DecodingOfText(DecodeText.this, DecodeText.this);
                    decodingOfText.execute(imageSteganography);
                }
            }
        });
    }

    private void ImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, select1), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();
            try {
                original_image = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                if(original_image.getHeight()>=200 && original_image.getWidth()>=200 && original_image.getHeight()<=5000 && original_image.getWidth()<=5000){
                    imageView.setImageBitmap(original_image);}
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            resolution1, Toast.LENGTH_SHORT);
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

        if (result != null) {
            if (!result.isDecoded())
                textView.setText(nomes);
            else {
                if (!result.isSecretKeyWrong()) {
                    textView.setText(decoded);
                    message.setText("" + result.getMessage());
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("", message.getText().toString());
                    clipboard.setPrimaryClip(clip);
                } else {
                    textView.setText(nosecretkey);
                }
            }
        } else {
            textView.setText(selectfirst);
        }
    }
}