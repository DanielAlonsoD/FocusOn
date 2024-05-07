package com.example.focuson;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import tablas.Subtarea;
import tablas.Tarea;

public class TareasFragment extends Fragment implements View.OnClickListener {
    private AdaptadorTareas adaptador;
    private ArrayList<Tarea> tareas = new ArrayList<>();
    private DatabaseReference baseDeDatos;

    public TareasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                    String tareaId = ds.getKey();
                    String nombre = ds.child("nombre").getValue(String.class);
                    boolean realizado = ds.child("realizado").getValue(Boolean.class);
                    Tarea tarea = new Tarea(tareaId, nombre, realizado, new ArrayList<>());
                    DatabaseReference baseSubtareas = baseDeDatos.child(tareaId).child("Subtareas");
                    baseSubtareas.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<Subtarea> subtareas = new ArrayList<>();
                            for (DataSnapshot ds2 : snapshot.getChildren()) {
                                subtareas.add(new Subtarea(ds2.getKey(), ds2.child("nombre").getValue(String.class), ds2.child("realizado").getValue(Boolean.class)));
                            }
                            tarea.setSubtareas(subtareas);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                    tareas.add(tarea);
                }

                adaptador = new AdaptadorTareas(tareas, TareasFragment.this.getContext());
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
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
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String textoAlerta = editTextTextoAlerta.getText().toString().trim();
                        if (!textoAlerta.isEmpty()) {
                            Tarea tarea = new Tarea(editTextTextoAlerta.getText().toString(), false, new ArrayList<Subtarea>(){});
                            baseDeDatos.push().setValue(tarea);
                            getParentFragmentManager().beginTransaction().detach(TareasFragment.this).attach(TareasFragment.this).commit();
                        }
                    }
                })
                .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                }).show();
    }
}