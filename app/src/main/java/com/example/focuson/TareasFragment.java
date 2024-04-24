package com.example.focuson;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import adaptadores.AdaptadorTareas;
import tablas.Tarea;

public class TareasFragment extends Fragment implements View.OnClickListener {
    private AdaptadorTareas adaptador;
    private ArrayList<Tarea> tareas = new ArrayList<>();
    private DatabaseReference baseDeDatos;

    public TareasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View elemento = inflater.inflate(R.layout.fragment_tareas, container, false);

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        baseDeDatos = FirebaseDatabase.getInstance().getReference(usuario.getUid()).child("Tareas");

        FloatingActionButton botonCrear = elemento.findViewById(R.id.botonCrearTarea);
        RecyclerView lista = elemento.findViewById(R.id.listaTareas);

        baseDeDatos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    tareas.add(new Tarea(ds.getKey(), ds.child("nombre").getValue(String.class), ds.child("realizado").getValue(Boolean.class)));
                }

                adaptador = new AdaptadorTareas(tareas, getActivity().getBaseContext());
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(TareasFragment.this.getContext(), 1);
                lista.setAdapter(adaptador);
                lista.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        botonCrear.setOnClickListener(this);

        return elemento;
    }

    @Override
    public void onClick(View v) {
        View alertaCrearTarea = LayoutInflater.from(this.getContext()).inflate(R.layout.alerta_crear_editar_tarea_subtarea, null);
        EditText editTextTextoAlerta =  alertaCrearTarea.findViewById(R.id.editTextTextoAlerta);
        new MaterialAlertDialogBuilder(this.getContext(), R.style.Alertas).setTitle(R.string.textoCreaUnaTarea).setView(alertaCrearTarea)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Tarea tarea = new Tarea(editTextTextoAlerta.getText().toString(), false);
                        baseDeDatos.push().setValue(tarea);
                        adaptador.notifyDataSetChanged();
                    }
                })
                .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                }).show();
    }
}