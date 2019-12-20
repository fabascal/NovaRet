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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.util.Util;
import com.example.novaret.Utils.UtilsDialogError;
import com.example.novaret.Utils.UtilsVersion;
import com.google.android.material.chip.Chip;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONException;
import org.json.JSONObject;

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
        UiGetSharedPreference();
    }

    public void CallUpdate(MenuItem menuItem) {
        /*Iniciamos el metodo Request de la libreria Volley*/
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://189.206.183.110:1390/nova/mobil/settings_mobil.php";
        /*Solicitamos al variable retornada de la url mencionada*/
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            SaveDataPreference(response);

                        } catch (JSONException e) {
                            UtilsDialogError.showAlert(SettingsActivity.this, String.valueOf(e));
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UtilsDialogError.showAlert(SettingsActivity.this, String.valueOf(error));
            }
        });
        queue.add(stringRequest);
    }
    public void SaveDataPreference( String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.getInt("estado")==1){
            SharedPreferences sharedPreferences =
                    getSharedPreferences("Configuraciones", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.spVersionName),jsonObject.getString("versionName"));
            editor.putString(getString(R.string.spVersionDate),jsonObject.getString("versionDate"));
            editor.putString(getString(R.string.spIpConnect),jsonObject.getString("ipConnect"));
            editor.putString(getString(R.string.spUserConnect),jsonObject.getString("userConnect"));
            editor.putString(getString(R.string.spPassConnect),jsonObject.getString("passConnect"));
            editor.putString(getString(R.string.spPortConnect),jsonObject.getString("portConnect"));
            editor.putString(getString(R.string.spIpVolumetrico),jsonObject.getString("ipVolumetrico"));
            editor.putString(getString(R.string.spDbVolumetrico),jsonObject.getString("dbVolumetrico"));
            editor.putString(getString(R.string.spUserVolumetrico),jsonObject.getString("userVolumetrico"));
            editor.putString(getString(R.string.spPassVolumetrico),jsonObject.getString("passVolumetrico"));
            editor.putString(getString(R.string.spPortVolumetrico),jsonObject.getString("portVolumetrico"));
            editor.putBoolean(getString(R.string.spSgpmGatewayVolumetrico),jsonObject.getBoolean("sgpmGateway"));
            editor.putString(getString(R.string.spUrlIntegra),jsonObject.getString("urlIntegra"));
            editor.putString(getString(R.string.spBandera),jsonObject.getString("banderaIntegra"));
            editor.putString(getString(R.string.spCveest),jsonObject.getString("cveestIntegra"));
            editor.commit();
            ShowDataPreference(response);
        }else if(jsonObject.getInt("estado")==2){
            UtilsDialogError.showAlert(SettingsActivity.this, jsonObject.getString("mensaje"));
        }

    }
    public void ShowDataPreference(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.getInt("estado")==1){
            tvVersionNueva.setText(jsonObject.getString("versionName"));
            tvVersionFecha.setText(jsonObject.getString("versionDate"));
            tvIpConnecta.setText(jsonObject.getString("ipConnect"));
            tvUsuarioConnecta.setText(jsonObject.getString("userConnect"));
            tvPasswordConnecta.setText(jsonObject.getString("passConnect"));
            tvPortConnecta.setText(jsonObject.getString("portConnect"));
            tvIpVolumetrico.setText(jsonObject.getString("ipVolumetrico"));
            tvBaseVolumetrico.setText(jsonObject.getString("dbVolumetrico"));
            tvUsuarioVolumetrico.setText(jsonObject.getString("userVolumetrico"));
            tvPasswordVolumetrico.setText(jsonObject.getString("passVolumetrico"));
            tvPortVolumetrico.setText(jsonObject.getString("portVolumetrico"));
            chGateway.setChecked(jsonObject.getBoolean("sgpmGateway"));
            tvUrlIntegra.setText(jsonObject.getString("urlIntegra"));
            tvMarca.setText(jsonObject.getString("banderaIntegra"));
            tvEstacion.setText(jsonObject.getString("cveestIntegra"));
        }

    }

    public void UiGetSharedPreference(){
        SharedPreferences sharedPreferences =
                getSharedPreferences("Configuraciones", Context.MODE_PRIVATE);
        String versionNameD = getResources().getString(R.string.version);
        tvVersionNueva.setText(sharedPreferences.getString(getString(R.string.spVersionName),versionNameD));
        String versionDateD = getResources().getString(R.string.spVersionDate);
        tvVersionFecha.setText(sharedPreferences.getString(getString(R.string.spVersionDate),versionDateD));
        String spIpConnectD = getResources().getString(R.string.ip);
        tvIpConnecta.setText(sharedPreferences.getString(getString(R.string.spIpConnect),spIpConnectD));
        String userConnectD = getResources().getString(R.string.usuario);
        tvUsuarioConnecta.setText(sharedPreferences.getString(getString(R.string.spUserConnect),userConnectD));
        String passConnectD = getResources().getString(R.string.password);
        tvPasswordConnecta.setText(sharedPreferences.getString(getString(R.string.spPassConnect),passConnectD));
        String portConnectD = getResources().getString(R.string.puerto);
        tvPortConnecta.setText(sharedPreferences.getString(getString(R.string.spPortConnect),portConnectD));
        String ipVolumetricoD = getResources().getString(R.string.ip);
        tvIpVolumetrico.setText(sharedPreferences.getString(getString(R.string.spIpVolumetrico),ipVolumetricoD));
        String dbVolumetricoD = getResources().getString(R.string.base);
        tvBaseVolumetrico.setText(sharedPreferences.getString(getString(R.string.spDbVolumetrico),dbVolumetricoD));
        String userVolumetricoD = getResources().getString(R.string.usuario);
        tvUsuarioVolumetrico.setText(sharedPreferences.getString(getString(R.string.spUserVolumetrico),userVolumetricoD));
        String passVolumetricoD = getResources().getString(R.string.password);
        tvPasswordVolumetrico.setText(sharedPreferences.getString(getString(R.string.spPassVolumetrico),passVolumetricoD));
        String portVolumetricoD = getResources().getString(R.string.puerto);
        tvPortVolumetrico.setText(sharedPreferences.getString(getString(R.string.spPortVolumetrico),portVolumetricoD));
        chGateway.setChecked(sharedPreferences.getBoolean(getString(R.string.spSgpmGatewayVolumetrico),false));
        String urlIntegraD = getResources().getString(R.string.ip);
        tvUrlIntegra.setText(sharedPreferences.getString(getString(R.string.spUrlIntegra),urlIntegraD));
        String banderaIntegraD = getResources().getString(R.string.marca);
        tvMarca.setText(sharedPreferences.getString(getString(R.string.spBandera),banderaIntegraD));
        String cveestIntegraD = getResources().getString(R.string.estacion);
        tvEstacion.setText(sharedPreferences.getString(getString(R.string.spCveest),cveestIntegraD));
    }


    private void InitializateVals() {
        try {
            tvVersion.setText(UtilsVersion.GetVersion(getApplicationContext()));
        } catch (PackageManager.NameNotFoundException e) {
            UtilsDialogError.showAlert(SettingsActivity.this, e.toString());
            e.printStackTrace();
        }

    }

    public void CallVersion(View view) {
        Toast.makeText(this, R.string.version, Toast.LENGTH_SHORT).show();

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            tvVersion.setText(pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            UtilsDialogError.showAlert(SettingsActivity.this, e.toString());
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
