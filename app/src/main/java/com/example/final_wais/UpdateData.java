package com.example.final_wais;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateData extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextNim;
    private EditText editTextJur;
    private FirebaseFirestore db;
    private Mahasiswa mahasiswa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_data);

        editTextName = findViewById(R.id.updateName);
        editTextNim = findViewById(R.id.updateNim);
        editTextJur = findViewById(R.id.updateJur);
        Button buttonUpdate = findViewById(R.id.buttonUpdate);

        db = FirebaseFirestore.getInstance();

        mahasiswa = (Mahasiswa) getIntent().getSerializableExtra("mahasiswa");

        if (mahasiswa != null) {
            editTextName.setText(mahasiswa.getNama());
            editTextNim.setText(mahasiswa.getNim());
            editTextJur.setText(mahasiswa.getJurusan());
        }

        buttonUpdate.setOnClickListener(view -> {
            String newName = editTextName.getText().toString();
            String newNim = editTextNim.getText().toString();
            String newJur = editTextJur.getText().toString();
            if (!newName.isEmpty()) {
                mahasiswa.setNama(newName);
                mahasiswa.setNim(newNim);
                mahasiswa.setJurusan(newJur);
                db.collection("mhs").document(mahasiswa.getId())
                        .set(mahasiswa)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                            finish();
                        })
                        .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
            }
        });
    }
}
