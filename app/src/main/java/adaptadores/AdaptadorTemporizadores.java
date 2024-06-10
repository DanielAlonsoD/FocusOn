package adaptadores;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.focuson.IniciarTemporizadorActivity;
import com.example.focuson.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tablas.Temporizador;

/**
 * @author Daniel Alonso
 */
public class AdaptadorTemporizadores extends RecyclerView.Adapter<AdaptadorTemporizadores.ViewHolder> {
    private final ArrayList<Temporizador> temporizadores;
    private final int layout;
    private final DatabaseReference baseDeDatos;

    public AdaptadorTemporizadores(ArrayList<Temporizador> temporizadores) {
        this.temporizadores = temporizadores;
        layout = R.layout.elemento_temporizador;
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        assert usuario != null;
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

        holder.botonIniciarTemporizador.setOnClickListener(v -> {
            Intent actividadIniciarTemporizador = new Intent(v.getContext(), IniciarTemporizadorActivity.class);
            actividadIniciarTemporizador.putExtra("Temporizador", temporizador);
            v.getContext().startActivity(actividadIniciarTemporizador);
        });

        holder.botonEliminarTemporizador.setOnClickListener(v -> {
            MaterialAlertDialogBuilder alerta = new MaterialAlertDialogBuilder(v.getContext(), R.style.Alertas);
            alerta.setTitle(R.string.textoBorrarTemporizador).setMessage(R.string.descripcionBorrarTemporizador)
                    .setPositiveButton(R.string.textoSi, (dialog, which) -> {
                        baseDeDatos.child(temporizador.getId()).removeValue();
                        temporizadores.remove(temporizador);
                        notifyDataSetChanged();
                    }).setNeutralButton(R.string.textoCancelar, (dialog, which) -> {});
            alerta.show();
        });
    }

    @Override
    public int getItemCount() {
        return temporizadores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView  textViewTituloRutina,textViewMinutosSegundosTrabajo, textViewMinutosSegundosDescanso;
        public final MaterialButton botonIniciarTemporizador, botonEliminarTemporizador;

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
