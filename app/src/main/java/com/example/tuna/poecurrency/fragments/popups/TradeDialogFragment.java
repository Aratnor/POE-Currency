package com.example.tuna.poecurrency.fragments.popups;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tuna.poecurrency.R;

public class TradeDialogFragment extends DialogFragment {
    TextView stockValue;
    String text;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.popup_stock_trade, container,
                false);

        return rootView;
    }
    public void setText(int stock) {
        if(stock > 0)
        text = "Total stock :" + stock;
        else
            text = "No stock info";
    }
    public void setUserName(String value) {
        text = value;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stockValue = view.findViewById(R.id.popup_stock_value);
        stockValue.setText(text);


    }
}
