package com.example.focuson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import tablas.Temporizador;

public class IniciarTemporizadorActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_temporizador);

        Temporizador temporizador = getIntent().getParcelableExtra("Temporizador");

        MaterialToolbar encabezado = findViewById(R.id.encabezadoIniciarTemporizador);
        TextView textoTipoTiempo = findViewById(R.id.textoTipoTiempo);
        TextView textoTiempo = findViewById(R.id.textoTiempo);
        MaterialButton botonIniciar = findViewById(R.id.botonIniciar);
        MaterialButton botonPausar = findViewById(R.id.botonPausar);
        MaterialButton botonReiniciar = findViewById(R.id.botonReiniciar);

        encabezado.setTitle(temporizador.getTitulo());
        textoTiempo.setText(temporizador.getTrabajo());

        encabezado.setNavigationOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        getOnBackPressedDispatcher().onBackPressed();
    }
}