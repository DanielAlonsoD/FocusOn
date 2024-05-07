package com.example.focuson;

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
import adaptadores.AdaptadorTemporizadores;
import tablas.Temporizador;

public class TemporizadoresFragment extends Fragment implements View.OnClickListener {

    private AdaptadorTemporizadores adaptador;
    private ArrayList<Temporizador> temporizadores = new ArrayList<>();
    private DatabaseReference baseDeDatos;

    public TemporizadoresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getParentFragmentManager().beginTransaction().detach(TemporizadoresFragment.this).attach(TemporizadoresFragment.this).commit();

        View elemento = inflater.inflate(R.layout.fragment_temporizadores, container, false);

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        baseDeDatos = FirebaseDatabase.getInstance().getReference(usuario.getUid()).child("Temporizadores");

        RecyclerView listaTemporizadores = elemento.findViewById(R.id.listaTemporizadores);
        FloatingActionButton botonCrear = elemento.findViewById(R.id.botonCrearTemporizador);

        baseDeDatos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String id = ds.getKey();
                    String titulo = ds.child("titulo").getValue(String.class);
                    String trabajo = ds.child("diasSemana").getValue(String.class);
                    String descanso = ds.child("horaInicio").getValue(String.class);
                    temporizadores.add(new Temporizador(id, titulo, trabajo, descanso));
                }

                adaptador = new AdaptadorTemporizadores(temporizadores, getActivity().getBaseContext());
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(TemporizadoresFragment.this.getContext(), 1);
                listaTemporizadores.setAdapter(adaptador);
                listaTemporizadores.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        botonCrear.setOnClickListener(this);

        return elemento;
    }

    @Override
    public void onClick(View v) {

    }
}