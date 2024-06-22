package com.example.final_wais;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginPage extends AppCompatActivity {

    private EditText editTextNama;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonSignIn;
    private FirebaseFirestore db;

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        editTextNama = findViewById(R.id.editTextNama);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignIn = findViewById(R.id.buttonSignIn);

        db = FirebaseFirestore.getInstance();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInputs();
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginPage.this, "Sign In clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginPage.this, RegistPage.class);
                startActivity(intent);
            }
        });
    }

    private void validateInputs() {
        String nama = editTextNama.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(nama)) {
            editTextNama.setError("Nama harus diisi");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password harus diisi");
            return;
        }

        authenticateUser(nama, password);

    }

    private void authenticateUser(String nama, String password) {
        db.collection("users")
                .whereEqualTo("nama", nama)
                .whereEqualTo("pass", password)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean isAuthenticated = false;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    isAuthenticated = true;
                                    break;
                                }
                            }
                            if (isAuthenticated) {
                                Toast.makeText(LoginPage.this, "Login berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginPage.this, dataMhs.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginPage.this, "Login gagal: Nama atau password salah", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginPage.this, "Login gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
