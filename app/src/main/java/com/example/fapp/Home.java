package com.example.fapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Home extends AppCompatActivity {

    TextView tv1,tv2,tv3,tv4;
    Button b1,b2;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();


        userId = fauth.getCurrentUser().getUid();
        DocumentReference reference = fstore.collection("users").document(userId);
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tv1.setText(value.getString("name"));
                tv2.setText(value.getString("email"));
                tv3.setText(value.getString("phnno"));
                tv4.setText(value.getString("address"));
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this,Update.class);
                i.putExtra("name",tv1.getText().toString());
                i.putExtra("email",tv2.getText().toString());
                i.putExtra("phnno",tv3.getText().toString());
                i.putExtra("address",tv4.getText().toString());
                startActivity(i);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fauth.signOut();
                startActivity(new Intent(Home.this,MainActivity.class));
                finish();
            }
        });
    }
}