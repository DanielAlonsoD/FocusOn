package com.example.focuson;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

/**
 * @author Daniel Alonso
 */
public class MetodosEntreClases {

    /**
     * En este método un EditText al insertar texto mediante el input del usuario se modifica el mismo para que desaparezcan
     * los textos de error.
     *
     * @param context: El contexto de la actividad en el que se realiza el cambio.
     * @param editText: El elemento en el que se verificará si el usuario ha realizado algún cambio.
     * @param textInputLayout: El elemento de texto en el que se realizarán los cambios.
     */
    public void cambiaColorEditarTextos(Context context, EditText editText, TextInputLayout textInputLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cambioColoresTextInput(context, textInputLayout, R.color.scampi, R.string.hello_blank_fragment, false);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /**
     * En este método se utiliza el TextInputLayout insertado para cambiarle los colores correspondientes por el insertado.
     *
     * @param context: El contexto de la actividad en el que se realiza el cambio.
     * @param textInputLayout: El elemento de texto en el que se realizarán los cambios.
     * @param color: El color que se utilizará para los cambios.
     * @param texto: El texto de error que aparecerá debajo del texto.
     * @param error: Es un booleano que indica si el texto de error es visible o no.
     */
    public void cambioColoresTextInput(Context context, TextInputLayout textInputLayout, int color, int texto, boolean error) {
        textInputLayout.setBoxStrokeColor(context.getColor(color));
        textInputLayout.setHintTextColor(context.getColorStateList(color));
        textInputLayout.setError(context.getText(texto));
        textInputLayout.setErrorEnabled(error);
    }

}
