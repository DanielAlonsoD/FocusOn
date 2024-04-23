package adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.focuson.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tablas.Tarea;

public class AdaptadorTareas extends RecyclerView.Adapter<AdaptadorTareas.ViewHolder> implements PopupMenu.OnMenuItemClickListener {
    private ArrayList<Tarea> tareas;
    private Context contexto;
    private int layout;

    public AdaptadorTareas (ArrayList<Tarea> tareas, Context contexto) {
        this.tareas = tareas;
        this.contexto = contexto;
        layout = R.layout.elemento_tarea;
    }

    @NonNull
    @Override
    public AdaptadorTareas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elemento = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        return new ViewHolder(elemento);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorTareas.ViewHolder holder, int position) {
        Tarea tarea = tareas.get(position);
        holder.representacionElementos(tarea);

        holder.checkRealizada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.isRealizado(isChecked, holder.textoTarea, tarea.getId());
            }
        });

        holder.botonMasOpciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(contexto, holder.botonMasOpciones);
                menu.getMenuInflater().inflate(R.menu.opciones_tarea, menu.getMenu());
                menu.setOnMenuItemClickListener(AdaptadorTareas.this::onMenuItemClick);
                menu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tareas.size();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        View alertaInsertarTexto = LayoutInflater.from(contexto).inflate(R.layout.alerta_crear_editar_tarea_subtarea, null);
        EditText editTextTextoAlerta =  alertaInsertarTexto.findViewById(R.id.editTextTextoAlerta);
        MaterialAlertDialogBuilder alerta = new MaterialAlertDialogBuilder(contexto.getApplicationContext(), R.style.Alertas);
        if (item.getItemId()==R.id.itemCrearSubtarea) {
            alerta.setTitle(R.string.textoCreaUnaSubtarea).setPositiveButton(R.string.textoOk, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setNeutralButton(R.string.textoCancelar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}});
            return true;
        } else if (item.getItemId()==R.id.itemCrearSubtarea) {
            return true;
        } else {
            return true;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textoTarea;
        public CheckBox checkRealizada;
        public ImageButton botonMasOpciones;
        public DatabaseReference baseDeDatos;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textoTarea = itemView.findViewById(R.id.textoTarea);
            checkRealizada = itemView.findViewById(R.id.checkRealizado);
            botonMasOpciones = itemView.findViewById(R.id.botonMasOpciones);
            FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
            baseDeDatos = FirebaseDatabase.getInstance().getReference(usuario.getUid()).child("Tareas");
        }

        public void representacionElementos(Tarea tarea) {
            textoTarea.setText(tarea.getNombre());
            checkRealizada.setChecked(tarea.isRealizado());
            isRealizado(tarea.isRealizado(), textoTarea, tarea.getId());
        }

        public void isRealizado(boolean isChecked, TextView textoTarea, String id) {
            if (isChecked) {
                textoTarea.setPaintFlags(textoTarea.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                baseDeDatos.child(id).child("realizado").setValue(true);
            } else {
                textoTarea.setPaintFlags(textoTarea.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                baseDeDatos.child(id).child("realizado").setValue(false);
            }
        }
    }
}
