package com.lambadam.tuna.poecurrency.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.lambadam.tuna.poecurrency.R;
import com.lambadam.tuna.poecurrency.adapters.CustomLeagueSpinnerAdapter;
import com.lambadam.tuna.poecurrency.adapters.CustomSpinnerAdapter;
import com.lambadam.tuna.poecurrency.adapters.CustomTradeAdapter;
import com.lambadam.tuna.poecurrency.elements.ItemProperties;
import com.lambadam.tuna.poecurrency.elements.TradeTransaction;
import com.lambadam.tuna.poecurrency.network.NetworkAPI;
import com.lambadam.tuna.poecurrency.stringutils.StringHelperAPI;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Trade_Fragment extends Fragment {
    ArrayList<TradeTransaction> transactions;
    Spinner spinner_have, spinner_search, spinner_league;
    CheckBox only_online;
    FloatingActionButton search_button;
    SpinnerAdapter adapter;
    CustomLeagueSpinnerAdapter league_adapter;
    NetworkAPI networkConnection;
    RecyclerView recyclerView;

    ImageView league_icon;

    int spinner_have_pos = 0;
    int spinner_search_pos = 0;
    String spinner_league_title = "Betrayal";
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
        spinner_league = root.findViewById(R.id.league_trade);

        recyclerView = root.findViewById(R.id.trade_list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        only_online = root.findViewById(R.id.only_online_checkbox);

        league_icon = root.findViewById(R.id.league_image_trade);

        league_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner_league.performClick();
            }
        });

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
        if(isNetworkAvailable()){
            String res = prepareUrl();
            StringHelperAPI helperAPI = new StringHelperAPI(res);
            transactions = helperAPI.getAllTradeCurrencyTransactions(res);
            for(TradeTransaction transaction : transactions) System.out.println(transaction.toString());
            setRecyclerAdapter();
            setRecyclerViewWithAdapter();
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Need internet connection!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setRecyclerAdapter() {
        tradeAdapter = new CustomTradeAdapter(getActivity(),transactions);
        tradeAdapter.notifyDataSetChanged();
    }

    private void setRecyclerViewWithAdapter() {
        recyclerView.setAdapter(tradeAdapter);
        tradeAdapter.notifyDataSetChanged();
    }

    private String prepareUrl() {
        String url ="";
        if(only_online.isChecked()){
            if(spinner_league_title.equals("Hardcore Betrayal"))
                url = "http://currency.poe.trade/search?league=Hardcore+Betrayal&online=x&stock=&want="
                        +ItemProperties.itemIds[spinner_search_pos]+"&have="
                        +ItemProperties.itemIds[spinner_have_pos];
            else
                url = "http://currency.poe.trade/search?league="+spinner_league_title+"&online=x&stock=&want="
                        +ItemProperties.itemIds[spinner_search_pos]+"&have="
                        +ItemProperties.itemIds[spinner_have_pos];

        }
        else {
            if(spinner_league_title.equals("Hardcore Betrayal"))
                url = "http://currency.poe.trade/search?league=Hardcore+Betrayal&online=&stock=&want="
                        +ItemProperties.itemIds[spinner_search_pos]+"&have="
                        +ItemProperties.itemIds[spinner_have_pos];
            else
                url = "http://currency.poe.trade/search?league="+spinner_league_title+"&online=&stock=&want="
                        +ItemProperties.itemIds[spinner_search_pos]+"&have="
                        +ItemProperties.itemIds[spinner_have_pos];
        }
        System.out.println("Last url :" +url);
        try {
            return new NetworkAPI().execute(url).get();

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

        spinner_league.setAdapter(league_adapter);
    }

    private void setAdapter(Context context) {
        adapter = new CustomSpinnerAdapter(context, ItemProperties.itemNames
                , ItemProperties.itemImages);

        league_adapter =
                new CustomLeagueSpinnerAdapter(getActivity(),ItemProperties.leagues);
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
        spinner_league.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_league_title = ItemProperties.leagues[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
