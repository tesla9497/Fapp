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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Update extends AppCompatActivity {

    EditText ename,edit_email,edit_phn,edit_add;
    Button save,delete;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        ename = findViewById(R.id.ename);
        edit_email = findViewById(R.id.edit_email);
        edit_phn = findViewById(R.id.edit_phn);
        edit_add = findViewById(R.id.edit_add);
        save = findViewById(R.id.save);
        delete = findViewById(R.id.delete);
        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();



        Intent data = getIntent();
        String name = data.getStringExtra("name");
        String email = data.getStringExtra("email");
        String phnno = data.getStringExtra("phnno");
        String address = data.getStringExtra("address");
        ename.setText(name);
        edit_email.setText(email);
        edit_phn.setText(phnno);
        edit_add.setText(address);



        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               FirebaseUser fuser = fauth.getCurrentUser();
               userId = fauth.getCurrentUser().getUid();
               DocumentReference reference = fstore.collection("users").document(userId);

                fuser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()) {
                            reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Update.this, "data deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Toast.makeText(Update.this, "ac deletd", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Update.this, MainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(Update.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String sname = ename.getText().toString();
                String semail = edit_email.getText().toString();
                String sphn = edit_phn.getText().toString();
                String sadd = edit_add.getText().toString();


                if(TextUtils.isEmpty(sname)){
                    ename.setError("Enter name");
                    return;
                }
                if(TextUtils.isEmpty(semail)){
                    edit_email.setError("Enter email");
                    return;
                }
                if(TextUtils.isEmpty(sphn)){
                    edit_phn.setError("Enter phn no");
                    return;
                }
                if(TextUtils.isEmpty(sadd)){
                    edit_add.setError("Enter address");
                    return;
                }

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
                            Toast.makeText(Update.this, "Saved", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Update.this, MainActivity.class));
                        }else{
                            Toast.makeText(Update.this, "saved failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}