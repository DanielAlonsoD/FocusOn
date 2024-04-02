package com.example.focuson;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tablas.Tarea;

public class TareasFragment extends Fragment {
    private AdaptadorTareas adaptador;
    private ArrayList<Tarea> tareas = new ArrayList<>();

    public TareasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View elemento = inflater.inflate(R.layout.fragment_tareas, container, false);

        tareas.add(new Tarea("1", "Acabar el Trabajo", false));
        tareas.add(new Tarea("2", "Seguir con el Trabajo", true));

        RecyclerView lista = elemento.findViewById(R.id.listaTareas);
        adaptador = new AdaptadorTareas(tareas, this.getContext());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this.getContext(), 1);
        lista.setAdapter(adaptador);
        lista.setLayoutManager(layoutManager);

        return elemento;
    }
}