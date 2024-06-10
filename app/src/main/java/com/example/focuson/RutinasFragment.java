package com.example.focuson;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import adaptadores.AdaptadorRutinas;
import tablas.Rutina;

/**
 * @author Daniel Alonso
 */
public class RutinasFragment extends Fragment implements View.OnClickListener {
    private AdaptadorRutinas adaptador;
    private final ArrayList<Rutina> rutinas = new ArrayList<>();

    public RutinasFragment() {
        // Required empty public constructor
    }

    @SuppressLint("DetachAndAttachSameFragment")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getParentFragmentManager().beginTransaction().detach(RutinasFragment.this).attach(RutinasFragment.this).commit();

        View elemento = inflater.inflate(R.layout.fragment_rutinas, container, false);

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        assert usuario != null;
        DatabaseReference baseDeDatos = FirebaseDatabase.getInstance().getReference(usuario.getUid()).child("Rutinas");

        RecyclerView listaRutinas = elemento.findViewById(R.id.listaRutinas);
        FloatingActionButton botonCrear = elemento.findViewById(R.id.botonCrearRutina);

        baseDeDatos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String id = ds.getKey();
                    String titulo = ds.child("titulo").getValue(String.class);
                    String diasSemana = ds.child("diasSemana").getValue(String.class);
                    String horaInicio = ds.child("horaInicio").getValue(String.class);
                    String horaFinal = ds.child("horaFinal").getValue(String.class);
                    rutinas.add(new Rutina(id, titulo, diasSemana, horaInicio, horaFinal));
                }

                adaptador = new AdaptadorRutinas(rutinas);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(RutinasFragment.this.getContext(), 1);
                listaRutinas.setAdapter(adaptador);
                listaRutinas.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        botonCrear.setOnClickListener(this);

        return elemento;
    }

    @Override
    public void onClick(View v) {
        Intent actividadCrearRutina = new Intent(this.getContext(), CrearRutinaActivity.class);
        startActivity(actividadCrearRutina);
    }
}