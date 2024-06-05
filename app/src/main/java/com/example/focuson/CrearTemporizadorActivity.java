package com.example.focuson;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import tablas.Rutina;
import tablas.Temporizador;

public class CrearTemporizadorActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference baseDeDatos;
    private EditText editInsertarTituloTemporizador;
    private TextView textViewTiempoTrabajo, textViewTiempoDescanso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_temporizador);

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        baseDeDatos = FirebaseDatabase.getInstance().getReference(usuario.getUid()).child("Temporizadores");

        MaterialToolbar encabezado = findViewById(R.id.encabezadoCrearTemporizador);
        editInsertarTituloTemporizador =  findViewById(R.id.editInsertarTituloTemporizador);
        MaterialButton botonSeleccionarTiempoTrabajo = findViewById(R.id.botonSeleccionarTiempoTrabajo);
        textViewTiempoTrabajo = findViewById(R.id.textViewTiempoTrabajo);
        MaterialButton botonSeleccionarTiempoDescanso = findViewById(R.id.botonSeleccionarTiempoDescanso);
        textViewTiempoDescanso = findViewById(R.id.textViewTiempoDescanso);
        FloatingActionButton botonRealizar = findViewById(R.id.botonRealizarTemporizador);

        encabezado.setNavigationOnClickListener(this);
        botonSeleccionarTiempoTrabajo.setOnClickListener(this);
        botonSeleccionarTiempoDescanso.setOnClickListener(this);
        botonRealizar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.botonSeleccionarTiempoTrabajo || v.getId() == R.id.botonSeleccionarTiempoDescanso) {
            TextView textView = textViewTiempoDescanso;
            if (v.getId() == R.id.botonSeleccionarTiempoTrabajo) {
                textView = textViewTiempoTrabajo;
            }
            TextView finalTextView = textView;

            TimePickerDialog selectorHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    LocalTime horaYMinuto = LocalTime.of(hourOfDay, minute);
                    DateTimeFormatter formateadorDeHora = DateTimeFormatter.ofPattern("HH:mm:ss");
                    finalTextView.setText(horaYMinuto.format(formateadorDeHora));
                    finalTextView.setVisibility(View.VISIBLE);
                }
            }, 0, 0, false);
            selectorHora.show();
        } else if (v.getId() == R.id.botonRealizarTemporizador) {
            String tituloTemporizador = editInsertarTituloTemporizador.getText().toString();
            String tiempoTrabajo = textViewTiempoTrabajo.getText().toString();
            String tiempoDescanso = textViewTiempoDescanso.getText().toString();

            if (tituloTemporizador.isEmpty() || tiempoTrabajo.isEmpty() || tiempoDescanso.isEmpty()) {
                RelativeLayout layoutCrearTemporizador = findViewById(R.id.layoutCrearTemporizador);
                Snackbar.make(layoutCrearTemporizador, R.string.textoErrorDatosVacios, Snackbar.LENGTH_SHORT).show();
            } else {
                Temporizador temporizador = new Temporizador(tituloTemporizador, tiempoTrabajo, tiempoDescanso);
                baseDeDatos.push().setValue(temporizador);
                getOnBackPressedDispatcher().onBackPressed();
            }
        } else {
            getOnBackPressedDispatcher().onBackPressed();
        }
    }
}