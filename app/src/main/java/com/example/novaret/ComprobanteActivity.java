package com.example.novaret;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.example.novaret.Utils.UtilsDialogError;
import com.example.novaret.Utils.Variables;
import com.example.novaret.Volumetrico.GetTicket;
import com.example.novaret.Volumetrico.VolumetricoListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComprobanteActivity extends AppCompatActivity {

    /*Variables para imprimir*/
    public static Printer mPrinter = null;
    @BindView(R.id.containerMain)
    ConstraintLayout containerMain;
    private Context mContext = null;
    private static final int REQUEST_PERMISSION = 100;


    /*@BindView(R.id.actvPosicion)
    AppCompatAutoCompleteTextView actvPosicion;*/
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
    int tipoVenta = 0;
    int tipoTransaccion = 0;
    @BindView(R.id.spnPosicion)
    AppCompatSpinner spnPosicion;
    JSONObject comprobante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprobante);
        ButterKnife.bind(this);
        mContext = this;
        /*requestRuntimePermission();*/
        GetExtras();
        FillInitial();
        FillImage();
        FillAdapter();
        spnPosicion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new GetTicket( ComprobanteActivity.this, output -> {
                    try {
                        comprobante = output;
                        FillData(output);
                    } catch (JSONException e) {
                        UtilsDialogError.showAlert(ComprobanteActivity.this,
                                getApplicationContext().getString(R.string.dialog_title_error),String.valueOf(e));
                        e.printStackTrace();
                    }
                }).execute(spnPosicion.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*
        Funcion para obtener datos de la actividad, 1-contado|2-credito|3-TPV|4-anticipo|5-aceites
        tipo de transaccion 1-comprobante|2-cfdi
        */
        Intent intent = getIntent();
        if (intent.hasExtra("TipoVenta")) {
            tipoVenta = intent.getIntExtra("TipoVenta", 0);
        }
        if (intent.hasExtra("TipoTransaccion")) {
            tipoTransaccion = intent.getIntExtra("TipoTransaccion", 0);
        }
    }

    private void FillInitial() {
        if (tipoVenta == 1 || tipoTransaccion == 1) {
            etCliente.setText(R.string.publicogeneral);
        }
    }

    /*Funcion para el llenado de datos a partir del cambio de la posicion*/
    private void FillData(JSONObject jsonObject) throws JSONException {

        etFolio.setText(jsonObject.getString("nrotrn"));
        etProducto.setText(jsonObject.getString("producto"));
        etVolumen.setText(String.format(Locale.ENGLISH,"%,.4f",jsonObject.getDouble("volumen")));
        etPrecio.setText(String.format(Locale.ENGLISH,"%,.2f",jsonObject.getDouble("precio")));
        etTotal.setText(String.format(Locale.ENGLISH,"%,.2f",jsonObject.getDouble("total")));
    }

    private void GetExtras() {
        Intent intent = getIntent();
        if (intent.hasExtra("TipoVenta")) {
            tipoVenta = intent.getIntExtra("TipoVenta", 0);
        }
        if (intent.hasExtra("TipoTransaccion")) {
            tipoTransaccion = intent.getIntExtra("TipoTransaccion", 0);
        }
    }

    private void FillImage() {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();
        Glide.with(getApplicationContext())
                .load("http://189.206.183.110:1390/cecg_app/images/logonova.png")
                .apply(options)
                .into(imgCardComprobante);
    }

    private void FillAdapter() {
        String[] bombas = new String[]{"1", "2", "3", "4"};
        ArrayAdapter<String> posicion = new ArrayAdapter<>(this, R.layout.spinner_posicion,
                R.id.posicion, bombas);
        spnPosicion.setAdapter(posicion);
    }

    @OnClick(R.id.btnPrint)
    public void onViewClicked() {
        ChooserPaymentMethod();

        /*updateButtonState(false);
        if (!runPrintReceiptSequence()) {
            updateButtonState(true);
        }*/
    }

    public void ChooserPaymentMethod(){
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(ComprobanteActivity.this,
                android.R.layout.select_dialog_item);
        adapter.add("Efectivo");
        adapter.add("Tarjeta debito");
        adapter.add("Tarjeta credito");
        new MaterialAlertDialogBuilder(ComprobanteActivity.this)
                .setTitle(R.string.chooserPayment)
                .setAdapter(adapter, (dialogInterface, i) ->
                        {
                            try {
                                comprobante.put(Variables.PAYMENT_METHOD, PaymentMethodSelected(adapter.getItem(i)));
                                Log.w("Json", String.valueOf(comprobante));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        })
                .show();
    }
    private String PaymentMethodSelected(String metodo){
        String res = "1|Efectivo";
        switch (metodo){
            case "Efectivo":
                res = "1|Efectivo";
                break;
            case "Tarjeta debito":
                res = "2|Tarjeta debito";
                break;
            case "Tarjeta credito":
                res = "3|Tarjeta credito";
                break;
        }
        return res;
    }
    private void requestRuntimePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        int permissionStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> requestPermissions = new ArrayList<>();

        if (permissionStorage == PackageManager.PERMISSION_DENIED) {
            requestPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionLocation == PackageManager.PERMISSION_DENIED) {
            requestPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!requestPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, requestPermissions.toArray(new String[requestPermissions.size()]), REQUEST_PERMISSION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode != REQUEST_PERMISSION || grantResults.length == 0) {
            return;
        }

        List<String> requestPermissions = new ArrayList<>();

        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                requestPermissions.add(permissions[i]);
            }
            if (permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION)
                    && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                requestPermissions.add(permissions[i]);
            }
        }

        if (!requestPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, requestPermissions.toArray(new String[requestPermissions.size()]), REQUEST_PERMISSION);
        }
    }



}
