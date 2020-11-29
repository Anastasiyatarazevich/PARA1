package com.example.para1;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button buttonToEncodeTxt;
    Button buttonToDecodeTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonToEncodeTxt = findViewById(R.id.buttonDecode);
        buttonToDecodeTxt = findViewById(R.id.buttonEncode);

    }

    public void onClickBtn1(View view) {
        Intent intent = new Intent(this, EncodeText.class);
        startActivity(intent);
    }

    public void onClickBtn2(View view) {
        Intent intent = new Intent(this, DecodeText.class);
        startActivity(intent);
    }
}