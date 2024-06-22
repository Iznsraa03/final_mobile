package com.example.final_wais;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TambahData extends AppCompatActivity {

    private EditText editTextNama, editTextNim, editTextJurusan;
    private Button buttonSave, buttonClear, buttonCancel;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_data);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        editTextNama = findViewById(R.id.editTextNama);
        editTextNim = findViewById(R.id.editTextNim);
        editTextJurusan = findViewById(R.id.editTextJurusan);

        buttonSave = findViewById(R.id.buttonSave);
        buttonClear = findViewById(R.id.buttonClear);
        buttonCancel = findViewById(R.id.buttonCancel);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveData() {
        String nama = editTextNama.getText().toString().trim();
        String nim = editTextNim.getText().toString().trim();
        String jurusan = editTextJurusan.getText().toString().trim();

        if(nama.isEmpty() || nim.isEmpty() || jurusan.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new user with a first, middle, and last name
        Map<String, Object> mhs = new HashMap<>();
        mhs.put("nama", nama);
        mhs.put("nim", nim);
        mhs.put("jurusan", jurusan);

        // Add a new document with a generated ID
        db.collection("mhs")
                .add(mhs)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(TambahData.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TambahData.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void clearData() {
        editTextNama.setText("");
        editTextNim.setText("");
        editTextJurusan.setText("");
    }
}
