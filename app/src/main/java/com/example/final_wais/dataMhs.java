package com.example.final_wais;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
        adapter = new Adapter(mahasiswaList, new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(Mahasiswa mahasiswa) {
                showOptionsDialog(mahasiswa);
            }
        });
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // Start listening to real-time updates
        listenToRealtimeUpdates();

        FloatingActionButton fab = findViewById(R.id.tambah);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dataMhs.this, TambahData.class);
                startActivity(intent);
            }
        });
    }

    private void listenToRealtimeUpdates() {
        db.collection("mhs")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (snapshots != null) {
                            mahasiswaList.clear();
                            for (QueryDocumentSnapshot document : snapshots) {
                                Mahasiswa mahasiswa = document.toObject(Mahasiswa.class);
                                mahasiswa.setId(document.getId()); // Set the ID here
                                mahasiswaList.add(mahasiswa);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void showOptionsDialog(final Mahasiswa mahasiswa) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option")
                .setItems(new String[]{"Update", "Delete"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                updateMahasiswa(mahasiswa);
                                break;
                            case 1:
                                deleteMahasiswa(mahasiswa);
                                break;
                        }
                    }
                });
        builder.show();
    }

    private void updateMahasiswa(Mahasiswa mahasiswa) {
        Intent intent = new Intent(dataMhs.this, UpdateData.class);
        intent.putExtra("mahasiswa", mahasiswa);
        startActivity(intent);
    }

    private void deleteMahasiswa(Mahasiswa mahasiswa) {
        db.collection("mhs").document(mahasiswa.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }
}
