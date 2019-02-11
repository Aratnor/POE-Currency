package com.example.tuna.poecurrency.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.tuna.poecurrency.R;
import com.example.tuna.poecurrency.adapters.CustomSpinnerAdapter;
import com.example.tuna.poecurrency.adapters.CustomTradeAdapter;
import com.example.tuna.poecurrency.elements.ItemProperties;
import com.example.tuna.poecurrency.elements.TradeTransaction;
import com.example.tuna.poecurrency.network.NetworkAPI;
import com.example.tuna.poecurrency.stringutils.StringHelperAPI;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Trade_Fragment extends Fragment {
    ArrayList<TradeTransaction> transactions;
    Spinner spinner_have, spinner_search;
    FloatingActionButton search_button;
    SpinnerAdapter adapter;
    NetworkAPI networkConnection;
    RecyclerView recyclerView;
    int spinner_have_pos = 0;
    int spinner_search_pos = 0;

    CustomTradeAdapter tradeAdapter;
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
        recyclerView = root.findViewById(R.id.trade_list_view);
        setAdapter(getActivity());
        setSpinner();
        spinnerOnClickPrepare();
        search_button = root.findViewById(R.id.trade_search_button);
        networkConnection = new NetworkAPI();
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTransactions();
            }
        });
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

    private void getTransactions() {
        String res = prepareUrl();
        StringHelperAPI helperAPI = new StringHelperAPI(res);
        transactions = helperAPI.getAllTradeCurrencyTransactions(res);
        for(TradeTransaction transaction : transactions) System.out.println(transaction.toString());
        setRecyclerAdapter();
        setRecyclerViewWithAdapter();
    }

    private void setRecyclerAdapter() {
        tradeAdapter = new CustomTradeAdapter(getActivity(),transactions);
    }

    private void setRecyclerViewWithAdapter() {
        recyclerView.setAdapter(tradeAdapter);
    }

    private String prepareUrl() {
        String url = "http://currency.poe.trade/search?league=Betrayal&online=x&stock=&want="
                +ItemProperties.itemIds[spinner_search_pos]+"&have="
                +ItemProperties.itemIds[spinner_have_pos];
        System.out.println("Last url :" +url);
        try {
            return networkConnection.execute(url).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "fail";
    }
    private void setSpinner() {

        spinner_have.setAdapter(adapter);

        spinner_search.setAdapter(adapter);
    }

    private void setAdapter(Context context) {
        adapter = new CustomSpinnerAdapter(context, ItemProperties.itemNames
                , ItemProperties.itemImages);
    }
    private void spinnerOnClickPrepare() {
        spinner_have.setSelection(0);
        spinner_search.setSelection(0);
        spinner_have.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spinner_have_pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        spinner_search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spinner_search_pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }
}
