package adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.focuson.R;
import com.example.focuson.TareasFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tablas.Subtarea;
import tablas.Tarea;

public class AdaptadorTareas extends RecyclerView.Adapter<AdaptadorTareas.ViewHolder> {
    private ArrayList<Tarea> tareas;
    private Context contexto;
    private int layout;
    private DatabaseReference baseDeDatos;

    public AdaptadorTareas (ArrayList<Tarea> tareas, Context contexto) {
        this.tareas = tareas;
        this.contexto = contexto;
        layout = R.layout.elemento_tarea;
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        baseDeDatos = FirebaseDatabase.getInstance().getReference(usuario.getUid()).child("Tareas");
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
        holder.representacionElementos(tarea, contexto, baseDeDatos);

        holder.imagenSubtareasVisibles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mostrarSubtareas();
                notifyDataSetChanged();
            }
        });

        holder.imagenSubtareasNoVisibles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mostrarSubtareas();
                notifyDataSetChanged();
            }
        });

        holder.checkRealizada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.isRealizado(isChecked, holder.textoTarea, tarea.getId(), baseDeDatos);
            }
        });

        holder.botonMasOpciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(contexto, holder.botonMasOpciones);
                menu.getMenuInflater().inflate(R.menu.opciones_tarea, menu.getMenu());
                menuDeOpciones(menu, v, tarea, holder);
                menu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tareas.size();
    }

    public void menuDeOpciones(PopupMenu menu, View v, Tarea tarea, AdaptadorTareas.ViewHolder holder) {
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.itemCrearSubtarea) {
                    crearSubtarea(v, tarea, holder);
                } else if (item.getItemId() == R.id.itemEditarTarea) {
                    editarTarea(v, tarea);
                } else {
                    borrarTarea(v, tarea);
                }
                return true;
            }
        });
    }

    public void crearSubtarea(View v, Tarea tarea, AdaptadorTareas.ViewHolder holder) {
        View alertaInsertarTexto = LayoutInflater.from(v.getContext()).inflate(R.layout.alerta_crear_editar_tarea_subtarea, null);
        TextInputLayout textInputTextoAlerta = alertaInsertarTexto.findViewById(R.id.textInputTextoAlerta);
        textInputTextoAlerta.setHint(R.string.textoInsertarTituloSubtarea);
        EditText editTextTextoAlerta =  alertaInsertarTexto.findViewById(R.id.editTextTextoAlerta);
        MaterialAlertDialogBuilder alerta = new MaterialAlertDialogBuilder(v.getContext(), R.style.Alertas).setView(alertaInsertarTexto);
        alerta.setTitle(R.string.textoCreaUnaSubtarea).setPositiveButton(R.string.textoOk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String textoAlerta = editTextTextoAlerta.getText().toString().trim();
                if (!textoAlerta.isEmpty()) {
                    Subtarea subtarea = new Subtarea(editTextTextoAlerta.getText().toString(), false);
                    String subtareaId = baseDeDatos.push().getKey();
                    baseDeDatos.child(tarea.getId()).child("Subtareas").child(subtareaId).setValue(subtarea);
                    subtarea.setId(subtareaId);
                    ArrayList<Subtarea> subtareas = new ArrayList<>();
                    if (tarea.getSubtareas() != null) {
                        subtareas = tarea.getSubtareas();
                    }
                    subtareas.add(subtarea);
                    tarea.setSubtareas(subtareas);
                    if (holder.imagenSubtareasNoVisibles.getVisibility() == View.VISIBLE) {
                        holder.mostrarSubtareas();
                    }
                    notifyDataSetChanged();
                }
            }
        }).setNeutralButton(R.string.textoCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}});
        alerta.show();
    }

    public void editarTarea(View v, Tarea tarea) {
        View alertaInsertarTexto = LayoutInflater.from(v.getContext()).inflate(R.layout.alerta_crear_editar_tarea_subtarea, null);
        EditText editTextTextoAlerta =  alertaInsertarTexto.findViewById(R.id.editTextTextoAlerta);
        editTextTextoAlerta.setText(tarea.getNombre());
        MaterialAlertDialogBuilder alerta = new MaterialAlertDialogBuilder(v.getContext(), R.style.Alertas).setView(alertaInsertarTexto);
        alerta.setTitle(R.string.textoEditarTarea).setPositiveButton(R.string.textoOk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String textoAlerta = editTextTextoAlerta.getText().toString().trim();
                if (!textoAlerta.isEmpty()) {
                    baseDeDatos.child(tarea.getId()).child("nombre").setValue(textoAlerta);
                    tarea.setNombre(textoAlerta);
                    notifyDataSetChanged();
                }
            }
        }).setNeutralButton(R.string.textoCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}});
        alerta.show();
    }

    public void borrarTarea(View v, Tarea tarea) {
        MaterialAlertDialogBuilder alerta = new MaterialAlertDialogBuilder(v.getContext(), R.style.Alertas);
        alerta.setTitle(R.string.textoBorrarTarea).setMessage(R.string.descripcionBorrarTarea)
                .setPositiveButton(R.string.textoSi, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        baseDeDatos.child(tarea.getId()).removeValue();
                        tareas.remove(tarea);
                        notifyDataSetChanged();
                    }
                }).setNeutralButton(R.string.textoCancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}});
        alerta.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textoTarea;
        public ImageButton imagenSubtareasVisibles, imagenSubtareasNoVisibles;
        public CheckBox checkRealizada;
        public ImageButton botonMasOpciones;
        public RecyclerView listaSubtareas;
        public AdaptadorSubtareas adaptador;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textoTarea = itemView.findViewById(R.id.textoTarea);
            imagenSubtareasVisibles = itemView.findViewById(R.id.imagenSubtareasVisibles);
            imagenSubtareasNoVisibles = itemView.findViewById(R.id.imagenSubtareasNoVisibles);
            checkRealizada = itemView.findViewById(R.id.checkRealizado);
            botonMasOpciones = itemView.findViewById(R.id.botonMasOpciones);
            listaSubtareas = itemView.findViewById(R.id.listaSubtareas);
        }

        public void representacionElementos(Tarea tarea, Context context, DatabaseReference baseDeDatos) {
            textoTarea.setText(tarea.getNombre());
            checkRealizada.setChecked(tarea.isRealizado());
            isRealizado(tarea.isRealizado(), textoTarea, tarea.getId(), baseDeDatos);
            if (tarea.getSubtareas().size()>=1 && imagenSubtareasVisibles.getVisibility() == View.VISIBLE) {
                adaptador = new AdaptadorSubtareas(tarea.getSubtareas(), context, tarea.getId());
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 1);
                listaSubtareas.setAdapter(adaptador);
                listaSubtareas.setLayoutManager(layoutManager);
            } else {
                listaSubtareas.setAdapter(null);
            }
        }

        public void isRealizado(boolean isChecked, TextView textoTarea, String id, DatabaseReference baseDeDatos) {
            if (isChecked) {
                textoTarea.setPaintFlags(textoTarea.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                baseDeDatos.child(id).child("realizado").setValue(true);
            } else {
                textoTarea.setPaintFlags(textoTarea.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                baseDeDatos.child(id).child("realizado").setValue(false);
            }
        }

        public void mostrarSubtareas() {
            if (imagenSubtareasVisibles.getVisibility() == View.INVISIBLE) {
                imagenSubtareasNoVisibles.setVisibility(View.INVISIBLE);
                imagenSubtareasVisibles.setVisibility(View.VISIBLE);
            } else {
                imagenSubtareasVisibles.setVisibility(View.INVISIBLE);
                imagenSubtareasNoVisibles.setVisibility(View.VISIBLE);
            }
        }
    }
}
