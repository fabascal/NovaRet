package com.example.novaret.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.example.novaret.PrintersList;
import com.example.novaret.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class PrinterAdapter extends RecyclerView.Adapter<PrinterAdapter.ViewHolder> {


    private Context context;
    private OnItemClickListener listener;
    private List<PrintersList> datas;

    public PrinterAdapter(List<PrintersList> datas, OnItemClickListener listener) {
        this.datas = datas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_printer, parent,
                false);
        this.context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PrintersList data = datas.get(position);
        holder.setListener(data, listener);
        holder.tvPrinterName.setText(data.PrinterName);
        holder.tvPrinterMac.setText(data.PrinterMac);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setList(List<PrintersList> data){
        this.datas = data;
        notifyDataSetChanged();
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgFoto)
        CircleImageView imgFoto;
        @BindView(R.id.tvPrinterName)
        AppCompatTextView tvPrinterName;
        @BindView(R.id.tvPrinterMac)
        AppCompatTextView tvPrinterMac;
        @BindView(R.id.containerMain)
        ConstraintLayout containerMain;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        void setListener (final PrintersList printersList, final OnItemClickListener listener){
            containerMain.setOnClickListener(view -> listener.onItemClick(printersList));
            containerMain.setOnLongClickListener( view -> {
                listener.onLongItemClick(printersList);
                return true;
            });
        }
    }
}
