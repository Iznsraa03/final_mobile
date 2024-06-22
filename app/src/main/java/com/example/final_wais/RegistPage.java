package com.example.final_wais;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class RegistPage extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextNama;
    private EditText editTextPassword;
    private Button buttonSignUp;
    private ImageButton backButton;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_page);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextNama = findViewById(R.id.editTextNama);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        firestore = FirebaseFirestore.getInstance();

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInputs();
            }
        });
    }

    private void validateInputs() {
        String email = editTextEmail.getText().toString().trim();
        String nama = editTextNama.getText().toString().trim();
        String pass = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email harus diisi");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Email tidak valid");
            return;
        }
        if (TextUtils.isEmpty(nama)) {
            editTextNama.setError("Nama harus diisi");
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            editTextPassword.setError("Password harus diisi");
            return;
        }
        if (pass.length() < 6) {
            editTextPassword.setError("Password harus lebih dari 6 karakter");
            return;
        }

        // Save user data to Firebase Firestore
        User user = new User(email, nama, pass);
        firestore.collection("users").add(user)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Sign Up berhasil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistPage.this, LoginPage.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}

class User {
    private String email;
    private String nama;
    private String pass;

    public User(String email, String nama, String pass) {
        this.email = email;
        this.nama = nama;
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public String getNama() {
        return nama;
    }

    public String getPass() {
        return pass;
    }
}