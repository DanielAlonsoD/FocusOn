package com.example.focuson;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

/**
 * @author Daniel Alonso
 */
public class MenuActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private NavController navController;
    private MaterialToolbar encabezado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        OnBackPressedCallback haciaAtras = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {}
        };
        getOnBackPressedDispatcher().addCallback(this, haciaAtras);

        encabezado = findViewById(R.id.encabezadoMenu);
        BottomNavigationView barraInferior = findViewById(R.id.barraInferiorOpciones);
        barraInferior.setOnItemSelectedListener(this);

        navController = Navigation.findNavController(this, R.id.contenedorFragmentos);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean realizado = false;
        if (item.getItemId() == R.id.itemTareas) {
            realizado = true;
            navController.navigate(R.id.tareasFragment);
            encabezado.setTitle(R.string.textoTareas);
        } else if (item.getItemId() == R.id.itemHorarios) {
            realizado = true;
            navController.navigate(R.id.horariosFragment);
            encabezado.setTitle(R.string.textoRutinas);
        } else if (item.getItemId() == R.id.itemCronometros) {
            realizado = true;
            navController.navigate(R.id.cronometrosFragment);
            encabezado.setTitle(R.string.textoTemporizadores);
        }
        return realizado;
    }
}