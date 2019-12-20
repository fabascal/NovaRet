package com.example.novaret.Utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.novaret.R;

import net.sourceforge.jtds.jdbc.JtdsConnection;
import net.sourceforge.jtds.jdbc.JtdsDatabaseMetaData;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UtilsVolumetrico extends Application {
    Context context;


    public JSONObject GetService(String posicion, Context context) throws JSONException, SQLException,
            ClassNotFoundException {
        JSONObject result = new JSONObject();
        JSONObject sh = UtilsSettings.CallSharedSettings(context);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://"
                        +sh.getString(context.getResources().getString(R.string.spIpVolumetrico))+":"
                        +sh.getString(context.getResources().getString(R.string.spPortVolumetrico))
                        +"/"+sh.getString(context.getResources().getString(R.string.spDbVolumetrico))+"",
                sh.getString(context.getResources().getString(R.string.spUserVolumetrico)),
                sh.getString(context.getResources().getString(R.string.spPassVolumetrico)));

        String stsql = "SELECT top 1 desp.nrotrn,desp.can,desp.mto,desp.pre,prod.den,desp.nrobom," +
                "resp.den,Convert(VARCHAR(10), cast(cast(desp.fchtrn-1 as int) as datetime) , 111)" +
                ",desp.codprd,gas.cveest,desp.mtogto,desp.codcli,cli.den,desp.hratrn,desp.codgas," +
                "desp.codprd,desp.nroveh,desp.odm,desp.fchcor,desp.nrotur,desp.nrocte," +
                "(select top 1 pre from ["+sh.getString(context.getResources().getString(R.string.spDbVolumetrico))+"].[dbo].[Precios] where codprd=desp.codprd and " +
                "codgas=desp.codgas and fch<=desp.fchtrn order by fch desc),(select top 1 iva from " +
                "["+sh.getString(context.getResources().getString(R.string.spDbVolumetrico))+"].[dbo].[Precios] where codprd=desp.codprd and codgas=desp.codgas and " +
                "fch<=desp.fchtrn order by fch desc) ,(select top 1 preiie from " +
                "["+sh.getString(context.getResources().getString(R.string.spDbVolumetrico))+"].[dbo].[Precios] where codprd=desp.codprd and codgas=desp.codgas and " +
                "fch<=desp.fchtrn order by fch desc) FROM ["+sh.getString(context.getResources().getString(R.string.spDbVolumetrico))+"].[dbo].[Despachos] as desp\n" +
                "left outer join ["+sh.getString(context.getResources().getString(R.string.spDbVolumetrico))+"].[dbo].[Productos] as prod on prod.cod=desp.codprd \n" +
                "left outer join ["+sh.getString(context.getResources().getString(R.string.spDbVolumetrico))+"].[dbo].[Responsables] as resp on resp.cod=desp.codres \n" +
                "left outer join ["+sh.getString(context.getResources().getString(R.string.spDbVolumetrico))+"].[dbo].[Gasolineras] as gas on gas.cod=desp.codgas \n" +
                "left outer join ["+sh.getString(context.getResources().getString(R.string.spDbVolumetrico))+"].[dbo].[Clientes] as cli on cli.cod=desp.codcli \n" +
                "where desp.nrobom =" + posicion + " order by desp.nrotrn desc";
                //"where desp.nrotrn='58342810' order by desp.nrotrn desc");
        Log.w("query",stsql);

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(stsql);
        if (!rs.next()){
            result.put("error",1);
            result.put("mensaje","Sin datos de volumetrico");

        }else{

                float a = rs.getFloat(11);
                if (a<0){
                    a=a*-1;

                }
                String despachador;
                if (rs.getString(7) == null){
                    despachador="DESPACHADOR";
                }else{
                    despachador=rs.getString(7);
                }
                result.put("nrotrn",rs.getInt(1));
                result.put("volumen",(rs.getFloat(3)+a)/rs.getFloat(4));
                result.put("precio",rs.getFloat(4));
                result.put("total",String.format("%.2f",rs.getFloat(3)+a));
                result.put("producto",rs.getString(5));
                result.put("bomba",rs.getInt(6));
                //result.put("despachador",nombre_depsachador(con));
                result.put("fecha",rs.getString(8));
                result.put("id_producto",rs.getInt(9));
                result.put("cveest",rs.getString(10));
                result.put("mtogto",rs.getFloat(11));
                result.put("codcli",rs.getInt(12));
                result.put("dencli",rs.getString(13));
                result.put("hora",hora(String.valueOf(rs.getInt(14))));
                result.put("codgas",rs.getInt(15));
                result.put("codprd",rs.getInt(16));
                result.put("nroveh",rs.getInt(17));
                result.put("odm",rs.getString(18));
                result.put("fchcor",rs.getString(19));
                result.put("nrotur",rs.getString(20));
                result.put("nrocte",rs.getString(21));
                result.put("mtogto",0);
                result.put("logusu",1);
                result.put("iva", rs.getDouble(23));
                result.put("ieps", rs.getDouble(24));
            }
        conn.close();
        st.close();

        Log.w("Result",String.valueOf(result));
        return result;
    }
    public String hora (String hora){
        if (hora.length()==1){
            hora="000"+hora;
        }else if (hora.length()==2){
            hora="00"+hora;
        }else if (hora.length()==3){
            hora="0"+hora;
        }
        Log.w("hora",hora);
        String hora_impresa="";
        hora_impresa=hora.substring(0,2)+":"+hora.substring(2,4);
        return hora_impresa;
    }


}
