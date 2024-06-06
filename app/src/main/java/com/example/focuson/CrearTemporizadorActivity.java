package com.example.focuson;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import java.lang.reflect.Array;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import tablas.Rutina;
import tablas.Temporizador;

public class CrearTemporizadorActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference baseDeDatos;
    private EditText editInsertarTituloTemporizador, editTextInsertarTiempoTrabajoHoras, editTextInsertarTiempoTrabajoMinutos, editTextInsertarTiempoTrabajoSegundos, editTextInsertarTiempoDescansoHoras, editTextInsertarTiempoDescansoMinutos, editTextInsertarTiempoDescansoSegundos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_temporizador);

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        baseDeDatos = FirebaseDatabase.getInstance().getReference(usuario.getUid()).child("Temporizadores");

        MaterialToolbar encabezado = findViewById(R.id.encabezadoCrearTemporizador);
        editInsertarTituloTemporizador =  findViewById(R.id.editInsertarTituloTemporizador);
        editTextInsertarTiempoTrabajoHoras = findViewById(R.id.editTextInsertarTiempoTrabajoHoras);
        editTextInsertarTiempoTrabajoMinutos = findViewById(R.id.editTextInsertarTiempoTrabajoMinutos);
        editTextInsertarTiempoTrabajoSegundos = findViewById(R.id.editTextInsertarTiempoTrabajoSegundos);
        editTextInsertarTiempoDescansoHoras = findViewById(R.id.editTextInsertarTiempoDescansoHoras);
        editTextInsertarTiempoDescansoMinutos = findViewById(R.id.editTextInsertarTiempoDescansoMinutos);
        editTextInsertarTiempoDescansoSegundos = findViewById(R.id.editTextInsertarTiempoDescansoSegundos);
        FloatingActionButton botonRealizar = findViewById(R.id.botonRealizarTemporizador);

        editTextTiempo(editTextInsertarTiempoTrabajoHoras, editTextInsertarTiempoTrabajoMinutos, null, false, true, false);
        editTextTiempo(editTextInsertarTiempoTrabajoMinutos, editTextInsertarTiempoTrabajoSegundos, editTextInsertarTiempoTrabajoHoras, true, true, true);
        editTextTiempo(editTextInsertarTiempoTrabajoSegundos, null, editTextInsertarTiempoTrabajoMinutos, true, false, true);
        editTextTiempo(editTextInsertarTiempoDescansoHoras, editTextInsertarTiempoDescansoMinutos, null, false, true, false);
        editTextTiempo(editTextInsertarTiempoDescansoMinutos, editTextInsertarTiempoDescansoSegundos, editTextInsertarTiempoDescansoHoras, true, true, true);
        editTextTiempo(editTextInsertarTiempoDescansoSegundos, null, editTextInsertarTiempoDescansoMinutos, true, false, true);

        encabezado.setNavigationOnClickListener(this);
        botonRealizar.setOnClickListener(this);
    }

    public void editTextTiempo(EditText editTextActual, EditText editTextSiguiente, EditText editTextAnterior, boolean hastaSesenta, boolean haySiguiente, boolean hayAnterior){
        editTextActual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 2) {
                    int tiempo = Integer.parseInt(s.toString());
                    if (hastaSesenta && tiempo >= 60) {
                        editTextActual.setText(s.toString().split("")[0]);
                    } else if (haySiguiente) {
                        editTextSiguiente.requestFocus();
                    }
                } else if (s.length() == 0 && hayAnterior){
                    editTextAnterior.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.botonRealizarTemporizador) {
            String tituloTemporizador = editInsertarTituloTemporizador.getText().toString();
            String trabajoHoras = editTextInsertarTiempoTrabajoHoras.getText().toString();
            String trabajoMinutos = editTextInsertarTiempoTrabajoMinutos.getText().toString();
            String trabajoSegundos = editTextInsertarTiempoTrabajoSegundos.getText().toString();
            String descansoHoras = editTextInsertarTiempoDescansoHoras.getText().toString();
            String descansoMinutos = editTextInsertarTiempoDescansoMinutos.getText().toString();
            String descansoSegundos = editTextInsertarTiempoDescansoSegundos.getText().toString();

            if (tituloTemporizador.isEmpty() || trabajoHoras.isEmpty() || trabajoMinutos.isEmpty() || trabajoSegundos.isEmpty() || descansoHoras.isEmpty() || descansoMinutos.isEmpty() || descansoSegundos.isEmpty()) {
                RelativeLayout layoutCrearTemporizador = findViewById(R.id.layoutCrearTemporizador);
                Snackbar.make(layoutCrearTemporizador, R.string.textoErrorDatosVacios, Snackbar.LENGTH_SHORT).show();
            } else {
                String tiempoTrabajo = creacionTiempo(new String[]{trabajoHoras, trabajoMinutos, trabajoSegundos});
                String tiempoDescanso = creacionTiempo(new String[]{descansoHoras, descansoMinutos, descansoSegundos});
                Temporizador temporizador = new Temporizador(tituloTemporizador, tiempoTrabajo, tiempoDescanso);
                baseDeDatos.push().setValue(temporizador);
                getOnBackPressedDispatcher().onBackPressed();
            }
        } else {
            getOnBackPressedDispatcher().onBackPressed();
        }
    }

    public String creacionTiempo(String[] tiempo) {
        String resultado = "";
        for(int i=0;i<tiempo.length; i++){
            if (tiempo[i].length()==1){
                tiempo[i]="0"+tiempo[i];
            }
            resultado+=tiempo[i];
            if (i<tiempo.length-1){
                resultado+=":";
            }
        }
        return resultado;
    }
}