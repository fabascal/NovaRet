package com.example.novaret.Volumetrico;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.example.novaret.R;
import com.example.novaret.Utils.UtilsSettings;
import com.example.novaret.Utils.Variables;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetTicket extends AsyncTask<String, Void, JSONObject> {
    private ProgressDialog mProgressDialog;
    private WeakReference<Activity> mActivity;
    private Context mContext;
    public VolumetricoListener delegate=null;
    JSONObject result = new JSONObject();

    /*peticion 0 es contado y 1 es credito*/
    int peticion=0;
    Statement stmt =null;
    Connection conn = null;

    public GetTicket(Activity activity, VolumetricoListener delegate) {
        mActivity = new WeakReference<Activity>(activity);
        this.mContext = activity.getApplicationContext();
        this.delegate = delegate;
        // Initialize the progress dialog
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setIndeterminate(false);
        // Progress dialog horizontal style
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Progress dialog title
        mProgressDialog.setTitle(mContext.getString(R.string.app_name));
        // Progress dialog message
        mProgressDialog.setMessage("Favor de esperar, estamos obteniendo el servicio...");
    }
    @Override
    protected void onPreExecute() {
        mProgressDialog.show();
    }
    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject sh = null;
        try {
            sh = UtilsSettings.CallSharedSettings(mContext);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://"
                            +sh.getString(mContext.getResources().getString(R.string.spIpVolumetrico))+":"
                            +sh.getString(mContext.getResources().getString(R.string.spPortVolumetrico))
                            +"/"+sh.getString(mContext.getResources().getString(R.string.spDbVolumetrico))+"",
                    sh.getString(mContext.getResources().getString(R.string.spUserVolumetrico)),
                    sh.getString(mContext.getResources().getString(R.string.spPassVolumetrico)));
            String stsql = "SELECT top 1 desp.nrotrn,desp.can,desp.mto,desp.pre,prod.den,desp.nrobom," +
                    "resp.den,Convert(VARCHAR(10), cast(cast(desp.fchtrn-1 as int) as datetime) , 111)" +
                    ",desp.codprd,gas.cveest,desp.mtogto,desp.codcli,cli.den,desp.hratrn,desp.codgas," +
                    "desp.codprd,desp.nroveh,desp.odm,desp.fchcor,desp.nrotur,desp.nrocte," +
                    "(select top 1 pre from ["+sh.getString(mContext.getResources().getString(R.string.spDbVolumetrico))+"].[dbo].[Precios] where codprd=desp.codprd and " +
                    "codgas=desp.codgas and fch<=desp.fchtrn order by fch desc),(select top 1 iva from " +
                    "["+sh.getString(mContext.getResources().getString(R.string.spDbVolumetrico))+"].[dbo].[Precios] where codprd=desp.codprd and codgas=desp.codgas and " +
                    "fch<=desp.fchtrn order by fch desc) ,(select top 1 preiie from " +
                    "["+sh.getString(mContext.getResources().getString(R.string.spDbVolumetrico))+"].[dbo].[Precios] where codprd=desp.codprd and codgas=desp.codgas and " +
                    "fch<=desp.fchtrn order by fch desc) FROM ["+sh.getString(mContext.getResources().getString(R.string.spDbVolumetrico))+"].[dbo].[Despachos] as desp\n" +
                    "left outer join ["+sh.getString(mContext.getResources().getString(R.string.spDbVolumetrico))+"].[dbo].[Productos] as prod on prod.cod=desp.codprd \n" +
                    "left outer join ["+sh.getString(mContext.getResources().getString(R.string.spDbVolumetrico))+"].[dbo].[Responsables] as resp on resp.cod=desp.codres \n" +
                    "left outer join ["+sh.getString(mContext.getResources().getString(R.string.spDbVolumetrico))+"].[dbo].[Gasolineras] as gas on gas.cod=desp.codgas \n" +
                    "left outer join ["+sh.getString(mContext.getResources().getString(R.string.spDbVolumetrico))+"].[dbo].[Clientes] as cli on cli.cod=desp.codcli \n" +
                    "where desp.nrobom =" + params[0] + " order by desp.nrotrn desc";
            //"where desp.nrotrn='58342810' order by desp.nrotrn desc");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(stsql);
            if (!rs.next()){
                result.put(Variables.ERROR_CODE,1);
                result.put(Variables.ERROR_MESSAGE,"Sin datos del despacho");

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
                result.put(Variables.KEY_NROTRN,rs.getInt(1));
                result.put(Variables.KEY_VOLUMEN,(rs.getFloat(3)+a)/rs.getFloat(4));
                result.put(Variables.KEY_PRECIO,rs.getFloat(4));
                result.put(Variables.KEY_TOTAL,String.format("%.2f",rs.getFloat(3)+a));
                result.put(Variables.KEY_PRODUCTO,rs.getString(5));
                result.put(Variables.KEY_BOMBA,rs.getInt(6));
                result.put(Variables.KEY_DESPACHADOR,"");
                result.put(Variables.KEY_FECHA,rs.getString(8));
                result.put(Variables.KEY_IDPRODUCTO,rs.getInt(9));
                result.put(Variables.KEY_CVEEST,rs.getString(10));
                result.put(Variables.KEY_MTOGTO,rs.getFloat(11));
                result.put(Variables.KEY_CODCLI,rs.getInt(12));
                result.put(Variables.KEY_DENCLI,rs.getString(13));
                result.put(Variables.KEY_HORA,hora(String.valueOf(rs.getInt(14))));
                result.put(Variables.KEY_CODGAS,rs.getInt(15));
                result.put(Variables.KEY_CODPRD,rs.getInt(16));
                result.put(Variables.KEY_NROVEH,rs.getInt(17));
                result.put(Variables.KEY_ODM,rs.getString(18));
                result.put(Variables.KEY_FCHCOR,rs.getString(19));
                result.put(Variables.KEY_NROTUR,rs.getString(20));
                result.put(Variables.KEY_NROCTE,rs.getString(21));
                result.put(Variables.KEY_IVA, rs.getDouble(23));
                result.put(Variables.KEY_IEPS, rs.getDouble(24));
                result.put(Variables.ERROR_CODE,0);
            }
            conn.close();
            st.close();
        } catch (JSONException | ClassNotFoundException | SQLException e) {
            try {
                result.put(Variables.ERROR_CODE,1);
                result.put(Variables.ERROR_MESSAGE,e);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            e.printStackTrace();
        }
        return result;
    }
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        if (mProgressDialog!= null){
            mProgressDialog.dismiss();
        }
        try {
            delegate.processFinish(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(jsonObject);
    }
    public String nombre_despachador (Context con, String mac) throws ClassNotFoundException, SQLException,
            InstantiationException, JSONException, IllegalAccessException{
        String res="DESPACHADOR";
        JSONObject sh = null;
        sh = UtilsSettings.CallSharedSettings(mContext);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://"
                        +sh.getString(mContext.getResources().getString(R.string.spIpVolumetrico))+":"
                        +sh.getString(mContext.getResources().getString(R.string.spPortVolumetrico))
                        +"/"+sh.getString(mContext.getResources().getString(R.string.spDbVolumetrico))+"",
                sh.getString(mContext.getResources().getString(R.string.spUserVolumetrico)),
                sh.getString(mContext.getResources().getString(R.string.spPassVolumetrico)));
        String query = "select d.nombre as pass from despachadores d \n" +
                "left outer join corte c on c.id_despachador=d.id \n" +
                "left outer join dispositivos dis on dis.id=c.id_dispositivo\n " +
                "where dis.mac_adr='"+mac+"' and c.status=0";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        if (rs.next()) {
            res = rs.getString("pass");
        }
        conn.close();
        stmt.close();
        rs.close();
        return res;
    }
    public String nip_desp (Context con, String mac) throws ClassNotFoundException, SQLException,
            InstantiationException, JSONException, IllegalAccessException, SocketException {
        String res="";
        JSONObject sh = null;
        sh = UtilsSettings.CallSharedSettings(mContext);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://"
                        +sh.getString(mContext.getResources().getString(R.string.spIpVolumetrico))+":"
                        +sh.getString(mContext.getResources().getString(R.string.spPortVolumetrico))
                        +"/"+sh.getString(mContext.getResources().getString(R.string.spDbVolumetrico))+"",
                sh.getString(mContext.getResources().getString(R.string.spUserVolumetrico)),
                sh.getString(mContext.getResources().getString(R.string.spPassVolumetrico)));
        String query = "select d.pass as pass from despachadores d \n" +
                "left outer join corte c on c.id_despachador=d.id \n" +
                "left outer join dispositivos dis on dis.id=c.id_dispositivo\n " +
                "where dis.mac_adr='"+mac+"' and c.status=0";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        if (rs.next()) {
            res = rs.getString("pass");
        }
        conn.close();
        stmt.close();
        rs.close();
        return res;
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
    public String CalculateMetoPago(String data){
        String res = "Cliente Contado";
        switch (data){
            case "3":
                res = "Cliente Credito";
                break;
            case "4":
                res = "Cliente Debito";
                break;
            case "0":
                res = "Cliente Contado";
                break;
        }
        return res;
    }
}
