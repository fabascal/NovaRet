package com.example.novaret.Utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.novaret.R;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilsSettings extends Application {
    Context context;
    public static JSONObject CallSharedSettings(Context context) throws JSONException{
        UtilsSettings utilsSettings = new UtilsSettings();
        return utilsSettings.GetSharedSettings(context);
    }


    private  JSONObject GetSharedSettings(Context context) throws JSONException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Configuraciones",
                Context.MODE_PRIVATE);

        JSONObject result = new JSONObject();
        /*Log.w("prueba1",context.getResources().getString(R.string.spVersionName));*/
        result.put(context.getResources().getString(R.string.spVersionName),sharedPreferences.getString(context.getResources().getString(R.string.spVersionName),""));
        result.put(context.getResources().getString(R.string.spVersionDate),sharedPreferences.getString(context.getResources().getString(R.string.spVersionDate),""));
        result.put(context.getResources().getString(R.string.spIpConnect),sharedPreferences.getString(context.getResources().getString(R.string.spIpConnect),""));
        result.put(context.getResources().getString(R.string.spUserConnect),sharedPreferences.getString(context.getResources().getString(R.string.spUserConnect),""));
        result.put(context.getResources().getString(R.string.spPassConnect),sharedPreferences.getString(context.getResources().getString(R.string.spPassConnect),""));
        result.put(context.getResources().getString(R.string.spPortConnect),sharedPreferences.getString(context.getResources().getString(R.string.spPortConnect),""));
        result.put(context.getResources().getString(R.string.spIpVolumetrico),sharedPreferences.getString(context.getResources().getString(R.string.spIpVolumetrico),""));
        result.put(context.getResources().getString(R.string.spDbVolumetrico),sharedPreferences.getString(context.getResources().getString(R.string.spDbVolumetrico),""));
        result.put(context.getResources().getString(R.string.spUserVolumetrico),sharedPreferences.getString(context.getResources().getString(R.string.spUserVolumetrico),""));
        result.put(context.getResources().getString(R.string.spPassVolumetrico),sharedPreferences.getString(context.getResources().getString(R.string.spPassVolumetrico),""));
        result.put(context.getResources().getString(R.string.spPortVolumetrico),sharedPreferences.getString(context.getResources().getString(R.string.spPortVolumetrico),""));
        result.put(context.getResources().getString(R.string.spSgpmGatewayVolumetrico),sharedPreferences.getBoolean(context.getResources().getString(R.string.spSgpmGatewayVolumetrico),false));
        result.put(context.getResources().getString(R.string.spUrlIntegra),sharedPreferences.getString(context.getResources().getString(R.string.spUrlIntegra),""));
        result.put(context.getResources().getString(R.string.spBandera),sharedPreferences.getString(context.getResources().getString(R.string.spBandera),""));
        result.put(context.getResources().getString(R.string.spCveest),sharedPreferences.getString(context.getResources().getString(R.string.spCveest),""));
        /*Log.w("ResultShared",String.valueOf(result));*/
        return result;
    }
}
