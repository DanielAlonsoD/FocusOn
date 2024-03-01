package com.example.focuson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth autenticacion;
    private EditText textoCorreo, textoContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        autenticacion = FirebaseAuth.getInstance();

        textoCorreo = findViewById(R.id.textoInsertarCorreo);
        textoContrasena = findViewById(R.id.textoInsertarContrasena);
        Button botonInicio = findViewById(R.id.botonIniciarSesion);
        Button botonRegistrarse = findViewById(R.id.botonRegistrarse);
        Button botonGoogle = findViewById(R.id.botonGoogle);

        botonInicio.setOnClickListener(this);
        botonRegistrarse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.botonIniciarSesion) {
            String correo = textoCorreo.getText().toString();
            String contrasena = textoContrasena.getText().toString();

            if (correo.isEmpty() || contrasena.isEmpty()) {

            } else {
                autenticacion.signInWithEmailAndPassword(correo, contrasena)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent actividadMenu = new Intent(MainActivity.this, MenuActivity.class);
                                    startActivity(actividadMenu);
                                } else {

                                }
                            }
                        });
            }
        } else if (v.getId() == R.id.botonRegistrarse) {
            Intent actividadRegistro = new Intent(this, RegistroActivity.class);
            startActivity(actividadRegistro);
        }
    }
}