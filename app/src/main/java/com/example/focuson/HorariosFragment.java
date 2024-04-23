package com.example.focuson;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import adaptadores.AdaptadorHorarios;
import adaptadores.AdaptadorTareas;
import tablas.Horario;
import tablas.Tarea;

public class HorariosFragment extends Fragment implements View.OnClickListener {
    private AdaptadorHorarios adaptador;
    private ArrayList<Horario> horarios = new ArrayList<>();
    private DatabaseReference baseDeDatos;

    public HorariosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getParentFragmentManager().beginTransaction().detach(HorariosFragment.this).attach(HorariosFragment.this).commit();

        View elemento = inflater.inflate(R.layout.fragment_horarios, container, false);

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        baseDeDatos = FirebaseDatabase.getInstance().getReference(usuario.getUid()).child("Horarios");

        RecyclerView listaHorarios = elemento.findViewById(R.id.listaHorarios);
        FloatingActionButton botonCrear = elemento.findViewById(R.id.botonCrearHorario);

        baseDeDatos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String id = ds.getKey();
                    String titulo = ds.child("titulo").getValue(String.class);
                    String diasSemana = ds.child("diasSemana").getValue(String.class);
                    String horaInicio = ds.child("horaInicio").getValue(String.class);
                    String horaFinal = ds.child("horaFinal").getValue(String.class);
                    horarios.add(new Horario(id, titulo, diasSemana, horaInicio, horaFinal));
                }

                adaptador = new AdaptadorHorarios(horarios, getActivity().getBaseContext());
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(HorariosFragment.this.getContext(), 1);
                listaHorarios.setAdapter(adaptador);
                listaHorarios.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        botonCrear.setOnClickListener(this);

        return elemento;
    }

    @Override
    public void onClick(View v) {
        Intent actividadCrearHorario = new Intent(this.getContext(), CrearHorario.class);
        startActivity(actividadCrearHorario);
    }
}