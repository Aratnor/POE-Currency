package com.example.tuna.poecurrency.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.tuna.poecurrency.R;
import com.example.tuna.poecurrency.adapters.CustomSpinnerAdapter;
import com.example.tuna.poecurrency.elements.ItemProperties;


public class Trade_Fragment extends Fragment {
    Spinner spinner_have, spinner_search;
    FloatingActionButton search_button;
    SpinnerAdapter adapter;

    public Trade_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trade_, container, false);
        spinner_have = root.findViewById(R.id.trade_spinner_have);
        spinner_search = root.findViewById(R.id.trade_spinner_want);

        search_button = root.findViewById(R.id.trade_search_button);
        setAdapter(getActivity());
        setSpinner();
        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void setSpinner() {

        spinner_have.setAdapter(adapter);

        spinner_search.setAdapter(adapter);
    }

    private void setAdapter(Context context) {
        adapter = new CustomSpinnerAdapter(context, ItemProperties.itemNames
                , ItemProperties.itemImages);
    }
}
