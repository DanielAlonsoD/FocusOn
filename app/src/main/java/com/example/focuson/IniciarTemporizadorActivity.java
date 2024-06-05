package com.example.focuson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;

public class IniciarTemporizadorActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_temporizador);

        MaterialToolbar encabezado = findViewById(R.id.encabezadoIniciarTemporizador);

        encabezado.setNavigationOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        getOnBackPressedDispatcher().onBackPressed();
    }
}