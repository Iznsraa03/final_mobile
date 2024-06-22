package com.example.final_wais;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class dataMhs extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Adapter adapter;
    private List<Mahasiswa> mahasiswaList;
    private FirebaseFirestore db;

    private static final String TAG = "dataMhs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_mhs);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mahasiswaList = new ArrayList<>();
        adapter = new Adapter(mahasiswaList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadDataFromFirestore();

        FloatingActionButton fab = findViewById(R.id.tambah);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(dataMhs.this, TambahData.class);
                 startActivity(intent);
            }
        });
    }

    private void loadDataFromFirestore() {
        db.collection("mhs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mahasiswaList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Mahasiswa mahasiswa = document.toObject(Mahasiswa.class);
                                mahasiswaList.add(mahasiswa);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
