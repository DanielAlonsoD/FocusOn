package com.example.focuson;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tablas.Tarea;

public class AdaptadorTareas extends RecyclerView.Adapter<AdaptadorTareas.ViewHolder> {
    private ArrayList<Tarea> tareas;
    private Context contexto;
    private int layout;

    public AdaptadorTareas (ArrayList<Tarea> tareas, Context contexto) {
        this.tareas = tareas;
        this.contexto = contexto;
        layout = R.layout.elemento_tarea;
    }

    @NonNull
    @Override
    public AdaptadorTareas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elemento = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        return new ViewHolder(elemento);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorTareas.ViewHolder holder, int position) {
        Tarea tarea = tareas.get(position);
        holder.representacionElementos(tarea);

        holder.checkRealizada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.isRealizado(isChecked, holder.textoTarea, tarea.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tareas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textoTarea;
        public CheckBox checkRealizada;
        public DatabaseReference baseDeDatos;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textoTarea = itemView.findViewById(R.id.textoTarea);
            checkRealizada = itemView.findViewById(R.id.checkRealizado);
            FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
            baseDeDatos = FirebaseDatabase.getInstance().getReference(usuario.getUid()).child("Tareas");
        }

        public void representacionElementos(Tarea tarea) {
            textoTarea.setText(tarea.getNombre());
            checkRealizada.setChecked(tarea.isRealizado());
            isRealizado(tarea.isRealizado(), textoTarea, tarea.getId());
        }

        public void isRealizado(boolean isChecked, TextView textoTarea, String id) {
            if (isChecked) {
                textoTarea.setPaintFlags(textoTarea.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                baseDeDatos.child(id).child("realizado").setValue(true);
            } else {
                textoTarea.setPaintFlags(textoTarea.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                baseDeDatos.child(id).child("realizado").setValue(false);
            }
        }
    }
}
