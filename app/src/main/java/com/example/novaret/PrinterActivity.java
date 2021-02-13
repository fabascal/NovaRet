package com.example.novaret;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.discovery.DeviceInfo;
import com.epson.epos2.discovery.Discovery;
import com.epson.epos2.discovery.DiscoveryListener;
import com.epson.epos2.discovery.FilterOption;
import com.example.novaret.Adapters.OnItemClickListener;
import com.example.novaret.Adapters.PrinterAdapter;
import com.example.novaret.Utils.ShowMsg;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrinterActivity extends AppCompatActivity implements OnItemClickListener {

    @BindView(R.id.imgCardSmall)
    AppCompatImageView imgCardSmall;
    @BindView(R.id.btnTicket)
    MaterialButton btnTicket;
    @BindView(R.id.tvPrinterName)
    TextView tvPrinterName;
    @BindView(R.id.tvTarget)
    TextView tvTarget;
    @BindView(R.id.cardContado)
    MaterialCardView cardContado;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;


    private Context mContext = null;
    private ArrayList<HashMap<String, String>> mPrinterList = null;
    private SimpleAdapter mPrinterListAdapter = null;
    private FilterOption mFilterOption = null;
    private static final int REQUEST_PERMISSION = 100;

    private PrinterAdapter adapter;
    List<PrintersList> mPrinterList2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer);
        ButterKnife.bind(this);

        configToolbar();
        mContext = this;

        /*mPrinterList = new ArrayList<HashMap<String, String>>();*/

        /*mPrinterListAdapter = new SimpleAdapter(this, mPrinterList, R.layout.item_printer,
                new String[]{"tvPrinterName", "tvPrinterMac"},
                new int[]{R.id.tvPrinterName, R.id.tvPrinterMac});
        list.setAdapter(mPrinterListAdapter);*/
        /*list.setOnItemClickListener(this);*/
        configAdapter();
        configRecyclerView();
        mFilterOption = new FilterOption();
        mFilterOption.setDeviceType(Discovery.TYPE_PRINTER);
        mFilterOption.setEpsonFilter(Discovery.FILTER_NAME);
        try {
            Discovery.start(this, mFilterOption, mDiscoveryListener);
        } catch (Exception e) {
            ShowMsg.showException(e, "start", mContext);
        }
    }

    private void configToolbar() {
        String title = getResources().getString(R.string.app_name).toUpperCase() + " - " + getResources().getString(R.string.printer).toUpperCase();
        toolbar.setTitle(title);
    }

    private void configAdapter() {
        adapter = new PrinterAdapter(new ArrayList<>(), this);
    }

    private void configRecyclerView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        while (true) {
            try {
                Discovery.stop();
                break;
            } catch (Epos2Exception e) {
                if (e.getErrorStatus() != Epos2Exception.ERR_PROCESSING) {
                    break;
                }
            }
        }

        mFilterOption = null;
    }

    public void CallUpdate(MenuItem menuItem) {
        restartDiscovery();
    }

    private void restartDiscovery() {
        while (true) {
            try {
                Discovery.stop();
                break;
            } catch (Epos2Exception e) {
                if (e.getErrorStatus() != Epos2Exception.ERR_PROCESSING) {
                    ShowMsg.showException(e, "stop", mContext);
                    return;
                }
            }
        }

        mPrinterList2.clear();
        adapter.notifyDataSetChanged();

        try {
            Discovery.start(this, mFilterOption, mDiscoveryListener);
        } catch (Exception e) {
            ShowMsg.showException(e, "stop", mContext);
        }
    }

    private DiscoveryListener mDiscoveryListener = new DiscoveryListener() {
        @Override
        public void onDiscovery(final DeviceInfo deviceInfo) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    /*HashMap<String, String> item = new HashMap<String, String>();*/
                    PrintersList printersList = new PrintersList();
                    printersList.PrinterName = deviceInfo.getDeviceName();
                    printersList.PrinterMac = deviceInfo.getTarget();
                    /*item.put("tvPrinterName", deviceInfo.getDeviceName());*/
                    /*String imp = deviceInfo.getTarget();*/
                    //Toast.makeText(getApplicationContext(), imp, Toast.LENGTH_LONG).show();
                    /*item.put("tvPrinterMac", deviceInfo.getTarget());*/
                    mPrinterList2.add(printersList);
                    Log.w("mPrinterList2", String.valueOf(mPrinterList2));
                    adapter.setList(mPrinterList2);
                    /*adapter.notifyDataSetChanged();*/
                    /*mPrinterList.add(item);
                    mPrinterListAdapter.notifyDataSetChanged();*/
                }
            });
        }
    };

    /******
     *      Métodos implementados por la interface OnItemClickListener
     * ******/
    @Override
    public void onItemClick(PrintersList printersList) {
        Log.w("onItemClick", printersList.PrinterName);
    }

    @Override
    public void onLongItemClick(final PrintersList printersList) {
        Log.w("onLongItemClick", printersList.PrinterName);
    }
}