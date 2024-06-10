package com.example.focuson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Timer;
import java.util.TimerTask;

import tablas.Temporizador;

/**
 * @author Daniel Alonso
 */
public class IniciarTemporizadorActivity extends AppCompatActivity implements View.OnClickListener {
    private Temporizador temporizador;
    private TextView textoTipoTiempo, textoTiempo;
    private MaterialButton botonIniciar;
    private Timer timer;
    private TimerTask timerTask;
    private double tiempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_temporizador);

        temporizador = getIntent().getParcelableExtra("Temporizador");

        MaterialToolbar encabezado = findViewById(R.id.encabezadoIniciarTemporizador);
        textoTipoTiempo = findViewById(R.id.textoTipoTiempo);
        textoTiempo = findViewById(R.id.textoTiempo);
        botonIniciar = findViewById(R.id.botonIniciar);
        MaterialButton botonPausar = findViewById(R.id.botonPausar);
        MaterialButton botonReiniciar = findViewById(R.id.botonReiniciar);

        encabezado.setTitle(temporizador.getTitulo());
        textoTiempo.setText(temporizador.getTrabajo());

        timer = new Timer();
        tiempo = getTiempo(temporizador.getTrabajo());

        encabezado.setNavigationOnClickListener(this);
        botonIniciar.setOnClickListener(this);
        botonPausar.setOnClickListener(this);
        botonReiniciar.setOnClickListener(this);
    }

    public double getTiempo(String tiempo) {
        String[] tiempos = tiempo.split(":");
        double horas = Double.parseDouble(tiempos[0])*3600;
        double minutos = Double.parseDouble(tiempos[1])*60;
        double segundos = Double.parseDouble(tiempos[2]);
        return horas+minutos+segundos;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.botonIniciar) {
            cambioEstiloBoton(botonIniciar, false);

        } else if (v.getId() == R.id.botonPausar) {
            cambioEstiloBoton(botonIniciar, true);
        } else if (v.getId() == R.id.botonReiniciar) {
            MaterialAlertDialogBuilder alerta = new MaterialAlertDialogBuilder(v.getContext(), R.style.Alertas);
            alerta.setTitle(R.string.textoReiniciarTemporizador).setMessage(R.string.descripcionReiniciarTemporizador)
                    .setPositiveButton(R.string.textoSi, (dialog, which) -> {
                        textoTipoTiempo.setText(R.string.textoTrabajo);
                        textoTiempo.setText(temporizador.getTrabajo());
                        cambioEstiloBoton(botonIniciar, true);
                        try {
                            timerTask.cancel();
                        } catch (NullPointerException ignored) {}
                        tiempo = getTiempo(temporizador.getTrabajo());
                    }).setNeutralButton(R.string.textoCancelar, (dialog, which) -> {});
            alerta.show();
        } else {
            try {
                timerTask.cancel();
            } catch (NullPointerException ignored) {}
            getOnBackPressedDispatcher().onBackPressed();
        }
    }

    public void cambioEstiloBoton (MaterialButton boton, boolean habilitado) {
        if (habilitado) {
            boton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.scampi));
            boton.setEnabled(true);
            try {
                timerTask.cancel();
            } catch (NullPointerException ignored) {}
        } else {
            boton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.blue_bell));
            boton.setEnabled(false);
            comenzarTiempo();
        }
    }

    private void comenzarTiempo()
    {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(() -> {
                    if (tiempo!=0) {
                        tiempo--;
                        textoTiempo.setText(getTiempoRestante());
                    } else if (textoTipoTiempo.getText().equals("Trabajo")) {
                        tiempo = getTiempo(temporizador.getDescanso());
                        textoTipoTiempo.setText(R.string.textoDescanso);
                        textoTiempo.setText(temporizador.getDescanso());
                        MediaPlayer.create(IniciarTemporizadorActivity.this, R.raw.start_sound_beep).start();
                    } else {
                        tiempo = getTiempo(temporizador.getTrabajo());
                        textoTipoTiempo.setText(R.string.textoTrabajo);
                        textoTiempo.setText(temporizador.getTrabajo());
                        MediaPlayer.create(IniciarTemporizadorActivity.this, R.raw.start_sound_beep).start();
                    }
                });
            }

        };
        timer.scheduleAtFixedRate(timerTask, 0 ,1000);
    }


    @SuppressLint("DefaultLocale")
    private String getTiempoRestante()
    {
        int redondear = (int) Math.round(tiempo);

        int segundos = ((redondear % 86400) % 3600) % 60;
        int minutos = ((redondear % 86400) % 3600) / 60;
        int horas = ((redondear % 86400) / 3600);

        return String.format("%02d",horas) + ":" + String.format("%02d",minutos) + ":" + String.format("%02d",segundos);
    }
}