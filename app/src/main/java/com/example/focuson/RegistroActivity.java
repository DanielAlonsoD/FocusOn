package com.example.focuson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import tablas.DatosUsuarioFirebase;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth autenticacion;
    private FirebaseDatabase baseDeDatos;
    private EditText textoNombre, textoCorreo, textoContrasena, textoConfirmarContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        autenticacion = FirebaseAuth.getInstance();
        baseDeDatos = FirebaseDatabase.getInstance();

        MaterialToolbar encabezado = findViewById(R.id.encabezadoRegistro);
        textoNombre = findViewById(R.id.editTextNombreRegistro);
        textoCorreo = findViewById(R.id.editTextCorreoRegistro);
        textoContrasena = findViewById(R.id.editTextContrasenaRegistro);
        textoConfirmarContrasena = findViewById(R.id.editTextConfirmarContrasenaRegistro);
        MaterialButton botonRegistrarse = findViewById(R.id.botonRegistrarse);

        encabezado.setNavigationOnClickListener(this);
        botonRegistrarse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.botonRegistrarse) {
            String nombre = textoNombre.getText().toString().trim();
            String correo = textoCorreo.getText().toString().trim();
            String contrasena = textoContrasena.getText().toString();
            String confirmarContrasena = textoConfirmarContrasena.getText().toString();

            if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {

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
                                }
                            }
                        });
            }
        } else {
            getOnBackPressedDispatcher().onBackPressed();
        }
    }
}