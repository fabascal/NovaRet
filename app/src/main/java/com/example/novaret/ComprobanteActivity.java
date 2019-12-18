package com.example.novaret;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.novaret.Utils.UtilsNetwork;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComprobanteActivity extends AppCompatActivity {

    @BindView(R.id.actvPosicion)
    AppCompatAutoCompleteTextView actvPosicion;
    @BindView(R.id.etCliente)
    TextInputEditText etCliente;
    @BindView(R.id.etFolio)
    TextInputEditText etFolio;
    @BindView(R.id.etProducto)
    TextInputEditText etProducto;
    @BindView(R.id.etVolumen)
    TextInputEditText etVolumen;
    @BindView(R.id.etPrecio)
    TextInputEditText etPrecio;
    @BindView(R.id.etTotal)
    TextInputEditText etTotal;
    @BindView(R.id.btnPrint)
    MaterialButton btnPrint;
    @BindView(R.id.imgCardComprobante)
    AppCompatImageView imgCardComprobante;
    int tipoVenta=0;
    int tipoTransaccion=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprobante);
        ButterKnife.bind(this);
        GetExtras();
        FillInitial();
        FillData();
        FillImage();
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
                Toast.makeText(getApplication(), editable, Toast.LENGTH_SHORT).show();
            }
        });
        /*
        Funcion para obtener datos de la actividad, 1-contado|2-credito|3-TPV|4-anticipo|5-aceites
        tipo de transaccion 1-comprobante|2-cfdi
        */
        Intent intent = getIntent();
        if (intent.hasExtra("TipoVenta")){
            tipoVenta = intent.getIntExtra("TipoVenta",0);
        }
        if ( intent.hasExtra("TipoTransaccion")){
            tipoTransaccion = intent.getIntExtra("TipoTransaccion",0);
        }
    }

    private void FillInitial() {
        if(tipoVenta==1 || tipoTransaccion==1){
            etCliente.setText(R.string.publicogeneral);
        }
    }

    /*Funcion para el llenado de datos a partir del cambio de la posicion*/
    private void FillData() {
    }

    private void GetExtras() {
        Intent intent = getIntent();
        if (intent.hasExtra("TipoVenta")){
            tipoVenta = intent.getIntExtra("TipoVenta",0);
        }
        if ( intent.hasExtra("TipoTransaccion")){
            tipoTransaccion = intent.getIntExtra("TipoTransaccion",0);
        }
    }

    private void FillImage() {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();
        Glide.with(getApplicationContext())
                    .load("http://189.206.183.110:1390/nova/images/logonova.png")
                .apply(options)
                .into(imgCardComprobante);
    }

    private void FillAdapter() {
        String[] bombas = new String[]{"1", "2", "3", "4"};
        ArrayAdapter<String> posicion = new ArrayAdapter<>(this, R.layout.item_posicion_dropdown,
                bombas);
        actvPosicion.setAdapter(posicion);
    }

    @OnClick(R.id.btnPrint)
    public void onViewClicked() {
    }
}
