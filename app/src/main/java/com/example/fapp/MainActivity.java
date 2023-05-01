package com.example.fapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText et1,et2;
    Button login,reg;
    FirebaseAuth fauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        login = findViewById(R.id.login);
        reg = findViewById(R.id.reg);
        fauth = FirebaseAuth.getInstance();

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Register.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String semail = et1.getText().toString();
                String slpass = et2.getText().toString();


                if(TextUtils.isEmpty(semail)){
                    et1.setError("Enter email");
                    return;
                }
                if(TextUtils.isEmpty(slpass)){
                    et2.setError("Enter password");
                    return;
                }



                fauth.signInWithEmailAndPassword(semail, slpass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(MainActivity.this,"Login successfull", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this,Home.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Login failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}