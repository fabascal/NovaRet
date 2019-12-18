package com.example.novaret;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.novaret.Utils.UtilsVersion;
import com.google.android.material.chip.Chip;
import com.google.android.material.textview.MaterialTextView;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.chGateway)
    Chip chGateway;
    @BindView(R.id.ivVersion)
    ImageView ivVersion;
    @BindView(R.id.ivOdbcConnect)
    ImageView ivOdbcConnect;
    @BindView(R.id.ivOdbcVolumetrico)
    ImageView ivOdbcVolumetrico;
    @BindView(R.id.ivFacturacion)
    ImageView ivFacturacion;
    @BindView(R.id.tvVersion)
    MaterialTextView tvVersion;
    @BindView(R.id.tvVersionNueva)
    MaterialTextView tvVersionNueva;
    @BindView(R.id.tvVersionFecha)
    MaterialTextView tvVersionFecha;
    @BindView(R.id.tvIpConnecta)
    MaterialTextView tvIpConnecta;
    @BindView(R.id.tvUsuarioConnecta)
    MaterialTextView tvUsuarioConnecta;
    @BindView(R.id.tvPasswordConnecta)
    MaterialTextView tvPasswordConnecta;
    @BindView(R.id.tvPortConnecta)
    MaterialTextView tvPortConnecta;
    @BindView(R.id.tvIpVolumetrico)
    MaterialTextView tvIpVolumetrico;
    @BindView(R.id.tvBaseVolumetrico)
    MaterialTextView tvBaseVolumetrico;
    @BindView(R.id.tvUsuarioVolumetrico)
    MaterialTextView tvUsuarioVolumetrico;
    @BindView(R.id.tvPasswordVolumetrico)
    MaterialTextView tvPasswordVolumetrico;
    @BindView(R.id.tvPortVolumetrico)
    MaterialTextView tvPortVolumetrico;
    @BindView(R.id.tvUrlIntegra)
    MaterialTextView tvUrlIntegra;
    @BindView(R.id.tvMacArd)
    MaterialTextView tvMacArd;
    @BindView(R.id.tvMarca)
    MaterialTextView tvMarca;
    @BindView(R.id.tvEstacion)
    MaterialTextView tvEstacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        String title = getResources().getString(R.string.app_name).toUpperCase() + " - " + getResources().getString(R.string.settings1).toUpperCase();
        toolbar.setTitle(title);
        InitializateVals();
    }

    public void CallUpdate(MenuItem menuItem) {
        Toast.makeText(this, R.string.update, Toast.LENGTH_SHORT).show();
        /*Iniciamos la libreria SharedPreferences para guardar datos obtenidos de integra mediante volley*/
        SharedPreferences sharedPreferences =
                getSharedPreferences("Configuraciones", Context.MODE_PRIVATE);
        /*Iniciamos el metodo Request de la libreria Volley*/
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.google.com";
        /*Solicitamos al variable retornada de la url mencionada*/
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w("VolleyResponse", response);
                        Toast.makeText(getApplicationContext(),"Response is : ",Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("VolleyError",error);
                Toast.makeText(getApplication(),"That didn't work!" + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }


    private void InitializateVals() {
        try {
            tvVersion.setText(UtilsVersion.GetVersion(getApplicationContext()));
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public void CallVersion(View view) {
        Toast.makeText(this, R.string.version, Toast.LENGTH_SHORT).show();

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            tvVersion.setText(pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void CallOdbcConnect(View view) {
        Toast.makeText(this, R.string.odbcConnect, Toast.LENGTH_SHORT).show();
    }

    public void CallOdbcVolumetrico(View view) {
        Toast.makeText(this, R.string.odbcVolumetrico, Toast.LENGTH_SHORT).show();
    }

    public void CallIntegra(View view) {
        Toast.makeText(this, R.string.facturacion, Toast.LENGTH_SHORT).show();
    }



}
