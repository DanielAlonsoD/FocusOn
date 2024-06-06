package adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.focuson.IniciarTemporizadorActivity;
import com.example.focuson.R;
import com.example.focuson.RegistroActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tablas.Rutina;
import tablas.Temporizador;

public class AdaptadorTemporizadores extends RecyclerView.Adapter<AdaptadorTemporizadores.ViewHolder> {
    private ArrayList<Temporizador> temporizadores;
    private Context contexto;
    private int layout;
    private DatabaseReference baseDeDatos;

    public AdaptadorTemporizadores(ArrayList<Temporizador> temporizadores, Context contexto) {
        this.temporizadores = temporizadores;
        this.contexto = contexto;
        layout = R.layout.elemento_temporizador;
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        baseDeDatos = FirebaseDatabase.getInstance().getReference(usuario.getUid()).child("Temporizadores");
    }

    @NonNull
    @Override
    public AdaptadorTemporizadores.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elemento = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        return new ViewHolder(elemento);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorTemporizadores.ViewHolder holder, int position) {
        Temporizador temporizador = temporizadores.get(position);
        holder.representacionElementos(temporizador);

        holder.botonIniciarTemporizador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent actividadIniciarTemporizador = new Intent(v.getContext(), IniciarTemporizadorActivity.class);
                actividadIniciarTemporizador.putExtra("Temporizador", temporizador);
                v.getContext().startActivity(actividadIniciarTemporizador);
            }
        });

        holder.botonEliminarTemporizador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder alerta = new MaterialAlertDialogBuilder(v.getContext(), R.style.Alertas);
                alerta.setTitle(R.string.textoBorrarTemporizador).setMessage(R.string.descripcionBorrarTemporizador)
                        .setPositiveButton(R.string.textoSi, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                baseDeDatos.child(temporizador.getId()).removeValue();
                                temporizadores.remove(temporizador);
                                notifyDataSetChanged();
                            }
                        }).setNeutralButton(R.string.textoCancelar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}});
                alerta.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return temporizadores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView  textViewTituloRutina,textViewMinutosSegundosTrabajo, textViewMinutosSegundosDescanso;
        public MaterialButton botonIniciarTemporizador, botonEliminarTemporizador;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTituloRutina = itemView.findViewById(R.id.textViewTituloRutina);
            textViewMinutosSegundosTrabajo = itemView.findViewById(R.id.textViewMinutosSegundosTrabajo);
            textViewMinutosSegundosDescanso = itemView.findViewById(R.id.textViewMinutosSegundosDescanso);
            botonIniciarTemporizador = itemView.findViewById(R.id.botonIniciarTemporizador);
            botonEliminarTemporizador = itemView.findViewById(R.id.botonEliminarTemporizador);
        }

        @SuppressLint("SetTextI18n")
        public void representacionElementos(Temporizador temporizador) {
            textViewTituloRutina.setText(temporizador.getTitulo());
            textViewMinutosSegundosTrabajo.setText(temporizador.getTrabajo());
            textViewMinutosSegundosDescanso.setText(temporizador.getDescanso());
        }
    }
}
