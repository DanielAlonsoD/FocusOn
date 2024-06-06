package adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.util.Log;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tablas.Subtarea;
import tablas.Tarea;

public class AdaptadorSubtareas extends RecyclerView.Adapter<AdaptadorSubtareas.ViewHolder> {
    private ArrayList<Subtarea> subtareas;
    private Context contexto;
    private int layout;
    private DatabaseReference baseDeDatos;

    public AdaptadorSubtareas(ArrayList<Subtarea> subtareas, Context contexto, String idTarea) {
        this.subtareas = subtareas;
        this.contexto = contexto;
        layout = R.layout.elemento_subtarea;
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        baseDeDatos = FirebaseDatabase.getInstance().getReference(usuario.getUid()).child("Tareas").child(idTarea).child("Subtareas");
    }

    @NonNull
    @Override
    public AdaptadorSubtareas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elemento = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        return new ViewHolder(elemento);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorSubtareas.ViewHolder holder, int position) {
        Subtarea subtarea = subtareas.get(position);
        holder.representacionElementos(subtarea, baseDeDatos);

        holder.checkRealizada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.isRealizado(isChecked, holder.textoTarea, subtarea, baseDeDatos);
            }
        });

        holder.botonMasOpciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(contexto, holder.botonMasOpciones);
                menu.getMenuInflater().inflate(R.menu.opciones_subtarea, menu.getMenu());
                menuDeOpciones(menu, v, subtarea);
                menu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return subtareas.size();
    }

    public void menuDeOpciones(PopupMenu menu, View v, Subtarea subtarea) {
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.itemEditarSubtarea) {
                    editarTarea(v, subtarea);
                } else {
                    borrarTarea(v, subtarea);
                }
                return true;
            }
        });
    }

    public void editarTarea(View v, Subtarea subtarea) {
        View alertaInsertarTexto = LayoutInflater.from(v.getContext()).inflate(R.layout.alerta_crear_editar_tarea_subtarea, null);
        EditText editTextTextoAlerta =  alertaInsertarTexto.findViewById(R.id.editTextTextoAlerta);
        editTextTextoAlerta.setText(subtarea.getNombre());
        MaterialAlertDialogBuilder alerta = new MaterialAlertDialogBuilder(v.getContext(), R.style.Alertas).setView(alertaInsertarTexto);
        alerta.setTitle(R.string.textoEditarTarea).setPositiveButton(R.string.textoOk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String textoAlerta = editTextTextoAlerta.getText().toString().trim();
                if (!textoAlerta.isEmpty()) {
                    baseDeDatos.child(subtarea.getId()).child("nombre").setValue(textoAlerta);
                    subtarea.setNombre(textoAlerta);
                    notifyDataSetChanged();
                }
            }
        }).setNeutralButton(R.string.textoCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}});
        alerta.show();
    }

    public void borrarTarea(View v, Subtarea subtarea) {
        MaterialAlertDialogBuilder alerta = new MaterialAlertDialogBuilder(v.getContext(), R.style.Alertas);
        alerta.setTitle(R.string.textoBorrarSubtarea).setMessage(R.string.descripcionBorrarSubtarea)
                .setPositiveButton(R.string.textoSi, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        baseDeDatos.child(subtarea.getId()).removeValue();
                        subtareas.remove(subtarea);
                        notifyDataSetChanged();
                    }
                }).setNeutralButton(R.string.textoCancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}});
        alerta.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textoTarea;
        public CheckBox checkRealizada;
        public ImageButton botonMasOpciones;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textoTarea = itemView.findViewById(R.id.textoSubtarea);
            checkRealizada = itemView.findViewById(R.id.checkRealizadoSubtarea);
            botonMasOpciones = itemView.findViewById(R.id.botonMasOpcionesSubtarea);
        }

        public void representacionElementos(Subtarea subtarea, DatabaseReference baseDeDatos) {
            textoTarea.setText(subtarea.getNombre());
            checkRealizada.setChecked(subtarea.isRealizado());
            isRealizado(subtarea.isRealizado(), textoTarea, subtarea, baseDeDatos);
        }

        public void isRealizado(boolean isChecked, TextView textoTarea, Subtarea subtarea, DatabaseReference baseDeDatos) {
            if (isChecked) {
                textoTarea.setPaintFlags(textoTarea.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                baseDeDatos.child(subtarea.getId()).child("realizado").setValue(true);
                subtarea.setRealizado(true);
            } else {
                textoTarea.setPaintFlags(textoTarea.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                baseDeDatos.child(subtarea.getId()).child("realizado").setValue(false);
                subtarea.setRealizado(false);
            }
        }
    }
}
