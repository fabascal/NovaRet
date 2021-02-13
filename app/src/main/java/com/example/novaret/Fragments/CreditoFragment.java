package com.example.novaret.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.example.novaret.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreditoFragment extends Fragment {


    Unbinder mUnbinder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.posicion)
    AutoCompleteTextView posicion;
    @BindView(R.id.imgCardSmall)
    AppCompatImageView imgCardSmall;
    @BindView(R.id.btnCfdi)
    MaterialButton btnCfdi;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.cardRfid)
    MaterialCardView cardRfid;
    @BindView(R.id.imgCardSmallNombre)
    AppCompatImageView imgCardSmallNombre;
    @BindView(R.id.btnNombre)
    MaterialButton btnNombre;
    @BindView(R.id.tvTitleNombre)
    TextView tvTitleNombre;
    @BindView(R.id.cardNombre)
    MaterialCardView cardNombre;
    @BindView(R.id.imgCardSmallNip)
    AppCompatImageView imgCardSmallNip;
    @BindView(R.id.btnNip)
    MaterialButton btnNip;
    @BindView(R.id.tvTitleNip)
    TextView tvTitleNip;
    @BindView(R.id.cardNip)
    MaterialCardView cardNip;

    public CreditoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_credito, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        configToolbar();
        fillPosicion();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    public void configToolbar() {
        String title = getResources().getString(R.string.app_name).toUpperCase() + " - " + getResources().getString(R.string.credito).toUpperCase();
        toolbar.setTitle(title);
    }
    public void fillPosicion(){
        String[] courses = new String[]{"1",
                "2",
                "3",
                "4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.item_menu_dropdown,
                courses);
        posicion.setAdapter(adapter);
    }

}
