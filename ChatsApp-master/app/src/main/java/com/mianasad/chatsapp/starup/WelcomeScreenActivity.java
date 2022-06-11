package com.mianasad.chatsapp.starup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mianasad.chatsapp.Activity.VerificationActivity;
import com.mianasad.chatsapp.R;

public class WelcomeScreenActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        Button btnAgree = findViewById(R.id.btn_agree);

        btnAgree.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(WelcomeScreenActivity.this, VerificationActivity.class));
                finish();
            }
        });
    }
}