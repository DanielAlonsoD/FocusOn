package com.example.focuson;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

/**
 * @author Daniel Alonso
 */
public class CrearRutinaActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference baseDeDatos;
    private EditText editInsertarTituloHorario;
    private TextView textViewHoraIncial, textViewHoraFinal;
    private final String[] diasSemana = new String[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_rutina);

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        assert usuario != null;
        baseDeDatos = FirebaseDatabase.getInstance().getReference(usuario.getUid()).child("Rutinas");

        MaterialToolbar encabezado = findViewById(R.id.encabezadoCrearRutina);
        editInsertarTituloHorario =  findViewById(R.id.editInsertarTituloRutina);
        creadorCheckboxHorario();
        MaterialButton botonSeleccionarHoraInicial = findViewById(R.id.botonSeleccionarHoraInicial);
        textViewHoraIncial = findViewById(R.id.textViewHoraIncial);
        MaterialButton botonSeleccionarHoraFinal = findViewById(R.id.botonSeleccionarHoraFinal);
        textViewHoraFinal = findViewById(R.id.textViewHoraFinal);
        FloatingActionButton botonRealizar = findViewById(R.id.botonRealizarRutina);

        encabezado.setNavigationOnClickListener(this);
        botonSeleccionarHoraInicial.setOnClickListener(this);
        botonSeleccionarHoraFinal.setOnClickListener(this);
        botonRealizar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        /*Si esta condición se cumple se abre una ventana para seleccionar una hora y que al aceptar
        aparezca esta en un texto.*/
        if (v.getId() == R.id.botonSeleccionarHoraInicial || v.getId() == R.id.botonSeleccionarHoraFinal) {
            TextView textView = textViewHoraFinal;
            if (v.getId() == R.id.botonSeleccionarHoraInicial) {
                textView = textViewHoraIncial;
            }
            TextView finalTextView = textView;

            TimePickerDialog selectorHora = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                LocalTime horaYMinuto = LocalTime.of(hourOfDay, minute);
                DateTimeFormatter formateadorDeHora = DateTimeFormatter.ofPattern("HH:mm");
                finalTextView.setText(horaYMinuto.format(formateadorDeHora));
                finalTextView.setVisibility(View.VISIBLE);
            }, 0, 0, true);
            selectorHora.show();

        /*Si se cumple esta condición se revisa si los datos están rellenados y si lo están se guarda
        la Rutina en la base de datos.*/
        } else if (v.getId() == R.id.botonRealizarRutina) {
            String tituloHorario = editInsertarTituloHorario.getText().toString();
            String diasTotales = diasSemanaHorario();
            String horaIncial = textViewHoraIncial.getText().toString();
            String horaFinal = textViewHoraFinal.getText().toString();

            if (tituloHorario.isEmpty() || diasTotales.length() == 0 || horaIncial.isEmpty() || horaFinal.isEmpty()) {
                RelativeLayout layoutCrearHorario = findViewById(R.id.layoutCrearRutina);
                Snackbar.make(layoutCrearHorario, R.string.textoErrorDatosVacios, Snackbar.LENGTH_SHORT).show();
            } else {
                Rutina rutina = new Rutina(tituloHorario, diasTotales, horaIncial, horaFinal);
                baseDeDatos.push().setValue(rutina);
                getOnBackPressedDispatcher().onBackPressed();
            }
        //Si no se cumple ninguna condición se vuelve a la pantalla anterior.
        } else {
            getOnBackPressedDispatcher().onBackPressed();
        }
    }

    /**
     * En este método se obtienen todos los checkboxes para realizar el método setCheckBoxTexto con estos.
     */
    public void creadorCheckboxHorario () {
        CheckBox checkboxLunes = findViewById(R.id.checkboxLunes);
        CheckBox checkboxMartes = findViewById(R.id.checkboxMartes);
        CheckBox checkboxMiercoles = findViewById(R.id.checkboxMiercoles);
        CheckBox checkboxJueves = findViewById(R.id.checkboxJueves);
        CheckBox checkboxViernes = findViewById(R.id.checkboxViernes);
        CheckBox checkboxSabado = findViewById(R.id.checkboxSabado);
        CheckBox checkboxDomingo = findViewById(R.id.checkboxDomingo);
        CheckBox[] checkBoxes = new CheckBox[]{checkboxLunes, checkboxMartes, checkboxMiercoles, checkboxJueves, checkboxViernes, checkboxSabado, checkboxDomingo};
        setCheckboxTexto(checkBoxes);
    }

    /**
     * En este método se aplica la función de que si el checkbox es pulsado se añada el valor del checkbox al
     * array de diasSemana o se cambie el dato por un null.
     *
     * @param checkBoxes: Array de CheckBox con los que realizaremos el método.
     */
    public void setCheckboxTexto (CheckBox[] checkBoxes) {
        for (int i = 0; i<checkBoxes.length; i++) {
            int numero = i;
            checkBoxes[i].setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    diasSemana[numero] = checkBoxes[numero].getText().toString();
                } else {
                    diasSemana[numero] = null;
                }
            });
        }
    }

    /**
     * En este método se obtiene un String con todos los dias de la semana seleccionados por el usuario.
     *
     * @return Un String con los días de la semana seleccionados.
     */
    public String diasSemanaHorario() {
        String diasTotales = "";
        for (String s : diasSemana) {
            if (s != null) {
                //noinspection StringConcatenationInLoop
                diasTotales += s + ", ";
            }
        }
        if (!diasTotales.isEmpty()) {
            diasTotales = diasTotales.substring(0, diasTotales.lastIndexOf(','));
        }
        return diasTotales;
    }
}