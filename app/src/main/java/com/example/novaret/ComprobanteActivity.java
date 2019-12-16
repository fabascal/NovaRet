package com.example.novaret;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComprobanteActivity extends AppCompatActivity {

    @BindView(R.id.actvPosicion)
    AppCompatAutoCompleteTextView actvPosicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprobante);
        ButterKnife.bind(this);
        FillAdapter();
        actvPosicion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Toast.makeText(getApplication(),editable,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void FillAdapter() {
        String[] bombas = new String[]{"1","2","3","4"};
        ArrayAdapter <String> posicion = new ArrayAdapter<>(this,R.layout.item_posicion_dropdown,
                bombas);
        actvPosicion.setAdapter(posicion);
    }
}
