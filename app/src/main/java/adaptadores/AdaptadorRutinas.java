package adaptadores;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.focuson.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tablas.Rutina;

public class AdaptadorRutinas extends RecyclerView.Adapter<AdaptadorRutinas.ViewHolder> {
    private final ArrayList<Rutina> rutinas;
    private final int layout;
    private final DatabaseReference baseDeDatos;

    public AdaptadorRutinas(ArrayList<Rutina> rutinas) {
        this.rutinas = rutinas;
        layout = R.layout.elemento_rutina;
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        assert usuario != null;
        baseDeDatos = FirebaseDatabase.getInstance().getReference(usuario.getUid()).child("Rutinas");
    }

    @NonNull
    @Override
    public AdaptadorRutinas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elemento = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        return new ViewHolder(elemento);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorRutinas.ViewHolder holder, int position) {
        Rutina rutina = rutinas.get(position);
        holder.representacionElementos(rutina);

        holder.botonEliminarHorario.setOnClickListener(v -> {
            MaterialAlertDialogBuilder alerta = new MaterialAlertDialogBuilder(v.getContext(), R.style.Alertas);
            alerta.setTitle(R.string.textoBorrarRutina).setMessage(R.string.descripcionBorrarRutina)
                    .setPositiveButton(R.string.textoSi, (dialog, which) -> {
                        baseDeDatos.child(rutina.getId()).removeValue();
                        rutinas.remove(rutina);
                        notifyDataSetChanged();
                    }).setNeutralButton(R.string.textoCancelar, (dialog, which) -> {});
            alerta.show();
        });
    }

    @Override
    public int getItemCount() {
        return rutinas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textViewTituloHorario, textViewDiasSemana, textViewHoras;
        public final MaterialButton botonEliminarHorario;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTituloHorario = itemView.findViewById(R.id.textViewTituloRutina);
            textViewDiasSemana = itemView.findViewById(R.id.textViewDiasSemana);
            textViewHoras = itemView.findViewById(R.id.textViewHoras);
            botonEliminarHorario = itemView.findViewById(R.id.botonEliminarRutinas);
        }

        @SuppressLint("SetTextI18n")
        public void representacionElementos(Rutina rutina) {
            textViewTituloHorario.setText(rutina.getTitulo());
            textViewDiasSemana.setText(rutina.getDiasSemana());
            textViewHoras.setText(rutina.getHoraInicio()+" - "+ rutina.getHoraFinal());
        }
    }
}
