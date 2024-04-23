package com.example.focuson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import tablas.DatosUsuarioFirebase;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth autenticacion;
    private FirebaseDatabase baseDeDatos;
    private  MetodosEntreClases metodosEntreClases = new MetodosEntreClases();
    private TextInputLayout textInputNombre, textInputCorreo, textInputContrasena, textInputConfirmarContrasena;
    private EditText textoNombre, textoCorreo, textoContrasena, textoConfirmarContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        autenticacion = FirebaseAuth.getInstance();
        baseDeDatos = FirebaseDatabase.getInstance();

        MaterialToolbar encabezado = findViewById(R.id.encabezadoRegistro);
        textInputNombre = findViewById(R.id.textInputNombreRegistro);
        textoNombre = findViewById(R.id.editTextNombreRegistro);
        textInputCorreo = findViewById(R.id.textInputCorreoRegistro);
        textoCorreo = findViewById(R.id.editTextCorreoRegistro);
        textInputContrasena = findViewById(R.id.textInputContrasenaRegistro);
        textoContrasena = findViewById(R.id.editTextContrasenaRegistro);
        textInputConfirmarContrasena = findViewById(R.id.textInputConfirmarContrasenaRegistro);
        textoConfirmarContrasena = findViewById(R.id.editTextConfirmarContrasenaRegistro);
        MaterialButton botonRegistrarse = findViewById(R.id.botonRegistrarse);

        metodosEntreClases.cambiaColorEditarTextos(this.getBaseContext(), textoNombre, textInputNombre);
        metodosEntreClases.cambiaColorEditarTextos(this.getBaseContext(), textoCorreo, textInputCorreo);
        metodosEntreClases.cambiaColorEditarTextos(this.getBaseContext(), textoContrasena, textInputContrasena);
        metodosEntreClases.cambiaColorEditarTextos(this.getBaseContext(), textoConfirmarContrasena, textInputConfirmarContrasena);

        encabezado.setNavigationOnClickListener(this);
        botonRegistrarse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.botonRegistrarse) {
            RelativeLayout layoutRegistroActivity = findViewById(R.id.layoutRegistroActivity);
            String nombre = textoNombre.getText().toString().trim();
            String correo = textoCorreo.getText().toString().trim();
            String contrasena = textoContrasena.getText().toString();
            String confirmarContrasena = textoConfirmarContrasena.getText().toString();

            if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
                Snackbar.make(layoutRegistroActivity, R.string.textoErrorDatosVacios, Snackbar.LENGTH_SHORT).show();
                cambiarColorTextInputVacios(nombre, correo, contrasena, confirmarContrasena, R.string.textoDatoVacío);
            } else if (contrasena.length() < 6) {
                Snackbar.make(layoutRegistroActivity, R.string.textoErrorContrasenaCorta, Snackbar.LENGTH_SHORT).show();
                cambiarColorTextInputVacios(nombre, correo, "", confirmarContrasena, R.string.textoMinimo6Caracteres);
            } else if (!contrasena.equals(confirmarContrasena)) {
                Snackbar.make(layoutRegistroActivity, R.string.textoErrorConfirmarContrasenaErronea, Snackbar.LENGTH_SHORT).show();
                cambiarColorTextInputVacios(nombre, correo, "", "", R.string.textoDatoDiferente);
            } else {
                autenticacion.createUserWithEmailAndPassword(correo, contrasena)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser usuario = autenticacion.getCurrentUser();
                                    DatosUsuarioFirebase datosUsuarioFirebase = new DatosUsuarioFirebase(nombre, null);
                                    baseDeDatos.getReference(usuario.getUid()).child("DatosUsuario").setValue(datosUsuarioFirebase);

                                    Intent actividadMenu = new Intent(RegistroActivity.this, MenuActivity.class);
                                    startActivity(actividadMenu);
                                } else {
                                    Snackbar.make(layoutRegistroActivity, R.string.textoErrorRegistro, Snackbar.LENGTH_SHORT).show();
                                    cambiarColorTextInputVacios(nombre, "", "", "", R.string.textoDatoIncorrecto);
                                }
                            }
                        });
            }
        } else {
            getOnBackPressedDispatcher().onBackPressed();
        }
    }

    public void cambiarColorTextInputVacios(String nombre, String correo, String contrasena, String confirmarContrasena, int texto) {
        if (nombre.isEmpty()) {
            metodosEntreClases.cambioColoresTextInput(getBaseContext(), textInputNombre, R.color.red, texto,true);
        }
        if (correo.isEmpty()) {
            metodosEntreClases.cambioColoresTextInput(getBaseContext(), textInputCorreo, R.color.red, texto, true);
        }
        if (contrasena.isEmpty()) {
            metodosEntreClases.cambioColoresTextInput(getBaseContext(), textInputContrasena, R.color.red, texto, true);
        }
        if (confirmarContrasena.isEmpty()) {
            metodosEntreClases.cambioColoresTextInput(getBaseContext(), textInputConfirmarContrasena, R.color.red, texto, true);
        }
    }
}