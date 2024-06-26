package com.example.final_wais;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MahasiswaViewHolder> {

    private List<Mahasiswa> mahasiswaList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Mahasiswa mahasiswa);
    }

    public Adapter(List<Mahasiswa> mahasiswaList, OnItemClickListener onItemClickListener) {
        this.mahasiswaList = mahasiswaList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MahasiswaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false);
        return new MahasiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MahasiswaViewHolder holder, int position) {
        Mahasiswa mahasiswa = mahasiswaList.get(position);
        holder.namaTextView.setText(mahasiswa.getNama());
        holder.nimTextView.setText(mahasiswa.getNim());
        holder.jurusanTextView.setText(mahasiswa.getJurusan());
        holder.bind(mahasiswa, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mahasiswaList.size();
    }

    public static class MahasiswaViewHolder extends RecyclerView.ViewHolder {
        public TextView namaTextView;
        public TextView nimTextView;
        public TextView jurusanTextView;

        public MahasiswaViewHolder(@NonNull View itemView) {
            super(itemView);
            namaTextView = itemView.findViewById(R.id.namaTextView);
            nimTextView = itemView.findViewById(R.id.nimTextView);
            jurusanTextView = itemView.findViewById(R.id.jurusanTextView);
        }

        public void bind(final Mahasiswa mahasiswa, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(mahasiswa);
                }
            });
        }
    }
}
