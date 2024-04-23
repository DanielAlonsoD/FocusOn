package com.example.focuson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth autenticacion;
    private MetodosEntreClases metodosEntreClases = new MetodosEntreClases();
    private TextInputLayout textInputCorreo, textInputContrasena;
    private EditText textoCorreo, textoContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        autenticacion = FirebaseAuth.getInstance();

        textInputCorreo = findViewById(R.id.textInputCorreo);
        textoCorreo = findViewById(R.id.textoInsertarCorreo);
        textInputContrasena = findViewById(R.id.textInputContrasena);
        textoContrasena = findViewById(R.id.textoInsertarContrasena);
        MaterialButton botonInicio = findViewById(R.id.botonIniciarSesion);
        MaterialButton botonRegistrarse = findViewById(R.id.botonRegistrarse);

        metodosEntreClases.cambiaColorEditarTextos(this.getBaseContext(), textoCorreo, textInputCorreo);
        metodosEntreClases.cambiaColorEditarTextos(this.getBaseContext(), textoContrasena, textInputContrasena);

        botonInicio.setOnClickListener(this);
        botonRegistrarse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.botonIniciarSesion) {
            LinearLayout layoutMainActivity = findViewById(R.id.layoutMainActivity);
            String correo = textoCorreo.getText().toString();
            String contrasena = textoContrasena.getText().toString();

            if (correo.isEmpty() || contrasena.isEmpty()) {
                Snackbar.make(layoutMainActivity, R.string.textoErrorDatosVacios, Snackbar.LENGTH_SHORT).show();
                cambiarColorTextInputVacios(correo, contrasena, R.string.textoDatoVacío);
            } else {
                autenticacion.signInWithEmailAndPassword(correo, contrasena)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent actividadMenu = new Intent(MainActivity.this, MenuActivity.class);
                                    startActivity(actividadMenu);
                                } else {
                                    Snackbar.make(layoutMainActivity, R.string.textoErrorCorreoContrasenaIncorrectos, Snackbar.LENGTH_SHORT).show();
                                    cambiarColorTextInputVacios("", "", R.string.textoDatoIncorrecto);
                                }
                            }
                        });
            }
        } else if (v.getId() == R.id.botonRegistrarse) {
            Intent actividadRegistro = new Intent(this, RegistroActivity.class);
            startActivity(actividadRegistro);
        }
    }

    public void cambiarColorTextInputVacios(String correo, String contrasena, int texto) {
        if (correo.isEmpty() && contrasena.isEmpty()) {
            metodosEntreClases.cambioColoresTextInput(getBaseContext(), textInputCorreo, R.color.red, texto, true);
            metodosEntreClases.cambioColoresTextInput(getBaseContext(), textInputContrasena, R.color.red, texto, true);
        } else if (correo.isEmpty()) {
            metodosEntreClases.cambioColoresTextInput(getBaseContext(), textInputCorreo, R.color.red, texto, true);
        } else {
            metodosEntreClases.cambioColoresTextInput(getBaseContext(), textInputContrasena, R.color.red, texto, true);
        }
    }


}