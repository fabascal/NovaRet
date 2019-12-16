package com.example.novaret.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.novaret.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContadoFragment extends Fragment {

    Unbinder mUnbinder;
    @BindView(R.id.etPremium)
    TextInputEditText etPremium;
    @BindView(R.id.etMagna)
    TextInputEditText etMagna;
    @BindView(R.id.etDiesel)
    TextInputEditText etDiesel;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.tilPremium)
    TextInputLayout tilPremium;
    @BindView(R.id.tilMagna)
    TextInputLayout tilMagna;
    @BindView(R.id.tilDiesel)
    TextInputLayout tilDiesel;


    public ContadoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contado, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        toolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.color_on_primary));
        toolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.color_on_primary));
        /*Listar precios de volumetrico*/
        listPrice();
        adaptUI();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    public void listPrice() {
        etPremium.setText("21.90");
        etMagna.setText("20.90");
        etDiesel.setText("21.50");
    }

    public void adaptUI() {
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    //  Collapsed
                    tilDiesel.setVisibility(View.GONE);
                    tilMagna.setVisibility(View.GONE);
                    tilPremium.setVisibility(View.GONE);
                } else {
                    //Expanded
                    tilDiesel.setVisibility(View.VISIBLE);
                    tilMagna.setVisibility(View.VISIBLE);
                    tilPremium.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
