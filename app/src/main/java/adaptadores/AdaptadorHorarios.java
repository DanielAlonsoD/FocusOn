package adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.focuson.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tablas.Horario;

public class AdaptadorHorarios extends RecyclerView.Adapter<AdaptadorHorarios.ViewHolder> {
    private ArrayList<Horario> horarios;
    private Context contexto;
    private int layout;

    public AdaptadorHorarios(ArrayList<Horario> horarios, Context contexto) {
        this.horarios = horarios;
        this.contexto = contexto;
        layout = R.layout.elemento_horario;
    }

    @NonNull
    @Override
    public AdaptadorHorarios.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elemento = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        return new ViewHolder(elemento);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorHorarios.ViewHolder holder, int position) {
        Horario horario = horarios.get(position);
        holder.representacionElementos(horario);

        holder.botonEliminarHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase.getInstance().getReference(usuario.getUid()).child("Horarios").child(horario.getId()).removeValue();
                horarios.remove(horario);
            }
        });
    }

    @Override
    public int getItemCount() {
        return horarios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTituloHorario, textViewDiasSemana, textViewHoras;
        public MaterialButton botonEliminarHorario;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTituloHorario = itemView.findViewById(R.id.textViewTituloHorario);
            textViewDiasSemana = itemView.findViewById(R.id.textViewDiasSemana);
            textViewHoras = itemView.findViewById(R.id.textViewHoras);
            botonEliminarHorario = itemView.findViewById(R.id.botonEliminarHorario);
        }

        @SuppressLint("SetTextI18n")
        public void representacionElementos(Horario horario) {
            textViewTituloHorario.setText(horario.getTitulo());
            textViewDiasSemana.setText(horario.getDiasSemana());
            textViewHoras.setText(horario.getHoraInicio()+" - "+horario.getHoraFinal());
        }
    }
}
