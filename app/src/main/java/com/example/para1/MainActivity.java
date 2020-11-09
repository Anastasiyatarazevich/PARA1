package com.example.para1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText inputEditText;
    EditText keytEditText;
    EditText outputEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         inputEditText = findViewById(R.id.input);
         keytEditText = findViewById(R.id.key);
        outputEditText = findViewById(R.id.result);

    }

    private String encrypt(String s, int key){

        int m = 'Z' - 'A' + 1;
        int offset = 'A';
        StringBuilder output = new StringBuilder();

        for(int i=0; i < s.length(); i++){
            char y = (char) (((s.charAt(i) - offset + key) % m) + offset);
            output.append(y);
        }

        return output.toString();
    }

    public void onCaesarButtonClick(View view){

        String sInput = inputEditText.getText().toString().toUpperCase();
        String sKey = keytEditText.getText().toString();
        int key_int = Integer.parseInt(sKey);
        outputEditText.setText(encrypt(sInput, key_int));

    }
}