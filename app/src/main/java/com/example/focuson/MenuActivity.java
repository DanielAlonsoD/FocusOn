package com.example.focuson;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MenuActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        OnBackPressedCallback haciaAtras = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {}
        };
        getOnBackPressedDispatcher().addCallback(this, haciaAtras);

        BottomNavigationView barraInferior = findViewById(R.id.barraInferiorOpciones);
        barraInferior.setOnItemSelectedListener(this::onNavigationItemSelected);

        navController = Navigation.findNavController(this, R.id.contenedorFragmentos);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean realizado = false;
        if (item.getItemId() == R.id.itemTareas) {
            realizado = true;
            navController.navigate(R.id.tareasFragment);
        } else if (item.getItemId() == R.id.itemHorarios) {
            realizado = true;
            navController.navigate(R.id.horariosFragment);
        } else if (item.getItemId() == R.id.itemCronometros) {
            realizado = true;
            navController.navigate(R.id.cronometrosFragment);
        }
        return realizado;
    }
}