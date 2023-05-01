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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText etname,etemail,etphno,etadd,etpass;
    Button register,login;

    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etname = findViewById(R.id.etname);
        etemail = findViewById(R.id.etemail);
        etphno = findViewById(R.id.etphn);
        etadd = findViewById(R.id.etadd);
        etpass = findViewById(R.id.etpass);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,MainActivity.class));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sname = etname.getText().toString();
                String semail = etemail.getText().toString();
                String sphn = etphno.getText().toString();
                String sadd = etadd.getText().toString();
                String spass = etpass.getText().toString();

                if(TextUtils.isEmpty(sname)){
                    etname.setError("Enter name");
                    return;
                }
                if(TextUtils.isEmpty(semail)){
                    etemail.setError("Enter email");
                    return;
                }
                if(TextUtils.isEmpty(sphn)){
                    etpass.setError("Enter password");
                    return;
                }
                if(TextUtils.isEmpty(sadd)){
                    etadd.setError("Enter address");
                    return;
                }

                fauth.createUserWithEmailAndPassword(semail, spass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            FirebaseUser fuser = fauth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Register.this, "verfication send", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register.this, "Failed to send verificatoiin"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            Toast.makeText(Register.this, "Account created", Toast.LENGTH_SHORT).show();

                            userId  = fauth.getCurrentUser().getUid();
                            DocumentReference reference = fstore.collection("users").document(userId);

                            Map<String , Object> user = new HashMap<>();
                            user.put("name",sname);
                            user.put("email",semail);
                            user.put("phnno",sphn);
                            user.put("address",sadd);
                            reference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(Register.this, "Stored", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Register.this, MainActivity.class));
                                    }else{
                                        Toast.makeText(Register.this, "Store failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Register.this, "Not created", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

    }
}