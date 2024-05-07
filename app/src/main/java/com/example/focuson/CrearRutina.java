package com.example.focuson;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import java.util.Calendar;

import tablas.Rutina;

public class CrearRutina extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference baseDeDatos;
    private EditText editInsertarTituloHorario;
    private TextView textViewHoraIncial, textViewHoraFinal;
    private String[] diasSemana = new String[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_rutina);

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        baseDeDatos = FirebaseDatabase.getInstance().getReference(usuario.getUid()).child("Rutinas");

        MaterialToolbar encabezado = findViewById(R.id.encabezadoCrearRutina);
        editInsertarTituloHorario =  findViewById(R.id.editInsertarTituloHorario);
        CheckBox[] checkboxesDias = creadorCheckboxHorario();
        MaterialButton botonSeleccionarHoraInicial = findViewById(R.id.botonSeleccionarHoraInicial);
        textViewHoraIncial = findViewById(R.id.textViewHoraIncial);
        MaterialButton botonSeleccionarHoraFinal = findViewById(R.id.botonSeleccionarHoraFinal);
        textViewHoraFinal = findViewById(R.id.textViewHoraFinal);
        FloatingActionButton botonRealizar = findViewById(R.id.botonRealizarHorario);

        encabezado.setNavigationOnClickListener(this);
        botonSeleccionarHoraInicial.setOnClickListener(this);
        botonSeleccionarHoraFinal.setOnClickListener(this);
        botonRealizar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.botonSeleccionarHoraInicial || v.getId() == R.id.botonSeleccionarHoraFinal) {
            TextView textView = textViewHoraFinal;
            if (v.getId() == R.id.botonSeleccionarHoraInicial) {
                textView = textViewHoraIncial;
            }
            TextView finalTextView = textView;

            TimePickerDialog selectorHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    LocalTime horaYMinuto = LocalTime.of(hourOfDay, minute);
                    DateTimeFormatter formateadorDeHora = DateTimeFormatter.ofPattern("HH:mm");
                    finalTextView.setText(horaYMinuto.format(formateadorDeHora));
                    finalTextView.setVisibility(View.VISIBLE);
                }
            }, 0, 0, true);
            selectorHora.show();
        } else if (v.getId() == R.id.botonRealizarHorario) {
            String tituloHorario = editInsertarTituloHorario.getText().toString();
            String diasTotales = diasSemanaHorario();
            String horaIncial = textViewHoraIncial.getText().toString();
            String horaFinal = textViewHoraFinal.getText().toString();

            if (tituloHorario.isEmpty() || diasTotales.isEmpty() || horaIncial.isEmpty() || horaFinal.isEmpty()) {
                RelativeLayout layoutCrearHorario = findViewById(R.id.layoutCrearHorario);
                Snackbar.make(layoutCrearHorario, R.string.textoErrorDatosVacios, Snackbar.LENGTH_SHORT).show();
            } else {
                Rutina rutina = new Rutina(tituloHorario, diasTotales, horaIncial, horaFinal);
                baseDeDatos.push().setValue(rutina);
                getOnBackPressedDispatcher().onBackPressed();
            }
        } else {
            getOnBackPressedDispatcher().onBackPressed();
        }
    }

    public CheckBox[] creadorCheckboxHorario () {
        CheckBox checkboxLunes = findViewById(R.id.checkboxLunes);
        CheckBox checkboxMartes = findViewById(R.id.checkboxMartes);
        CheckBox checkboxMiercoles = findViewById(R.id.checkboxMiercoles);
        CheckBox checkboxJueves = findViewById(R.id.checkboxJueves);
        CheckBox checkboxViernes = findViewById(R.id.checkboxViernes);
        CheckBox checkboxSabado = findViewById(R.id.checkboxSabado);
        CheckBox checkboxDomingo = findViewById(R.id.checkboxDomingo);
        CheckBox[] checkBoxes = new CheckBox[]{checkboxLunes, checkboxMartes, checkboxMiercoles, checkboxJueves, checkboxViernes, checkboxSabado, checkboxDomingo};
        setCheckboxTexto(checkBoxes);
        return checkBoxes;
    };

    public void setCheckboxTexto (CheckBox[] checkBoxes) {
        for (int i = 0; i<checkBoxes.length; i++) {
            int numero = i;
            checkBoxes[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        diasSemana[numero] = checkBoxes[numero].getText().toString();
                    } else {
                        diasSemana[numero] = "";
                    }
                }
            });
        }
    }

    public String diasSemanaHorario() {
        String diasTotales = "";
        for (int i = 0; i<diasSemana.length; i++) {
            if (diasSemana[i]!=null) {
                diasTotales += diasSemana[i]+", ";
            }
        }
        diasTotales = diasTotales.substring(0, diasTotales.lastIndexOf(','));
        return diasTotales;
    }
}