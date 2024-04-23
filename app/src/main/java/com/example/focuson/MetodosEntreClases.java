package com.example.focuson;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class MetodosEntreClases {

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

    public void cambioColoresTextInput(Context context, TextInputLayout textInputLayout, int color, int texto, boolean error) {
        textInputLayout.setBoxStrokeColor(context.getColor(color));
        textInputLayout.setHintTextColor(context.getColorStateList(color));
        textInputLayout.setError(context.getText(texto));
        textInputLayout.setErrorEnabled(error);
    }

}
