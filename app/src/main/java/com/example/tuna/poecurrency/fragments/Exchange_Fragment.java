package com.example.tuna.poecurrency.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.app.Fragment;
import com.example.tuna.poecurrency.R;
import com.example.tuna.poecurrency.adapters.CustomListViewAdapter;
import com.example.tuna.poecurrency.adapters.CustomSpinnerAdapter;
import com.example.tuna.poecurrency.elements.CurrencyTransaction;
import com.example.tuna.poecurrency.elements.ItemProperties;
import com.example.tuna.poecurrency.network.NetworkAPI;
import com.example.tuna.poecurrency.stringutils.StringHelperAPI;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Exchange_Fragment extends Fragment{
    CheckBox sell ,buy ;
    Spinner spinnerSell,spinnerBuy,spinnerLeague;
    RecyclerView mRecyclerView;

    CustomSpinnerAdapter adapter;
    int spinner_sell_position = 0;
    int spinner_buy_position = 0;
    String spinner_league_title;
    static ArrayList<CurrencyTransaction> transactions;
    CustomListViewAdapter listViewAdapter;
    NetworkAPI networkConnection;
    View rootView;
    public Exchange_Fragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_exchange_, container, false);
        buy = rootView.findViewById(R.id.search_all_buy_checkBox);
        sell = rootView.findViewById(R.id.search_all_sell_checkBox);

        spinnerSell = rootView.findViewById(R.id.spinnerSell);
        spinnerBuy = rootView.findViewById(R.id.spinnerBuy);
        spinnerLeague = rootView.findViewById(R.id.league);

        setAdapter(getActivity());
        setSpinner();
        setCheckBoxes();

        FloatingActionButton search_button = rootView.findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
        mRecyclerView = rootView.findViewById(R.id.transactions);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private String getCurrencyRate(String uri){
        networkConnection = new NetworkAPI();
        String res = null;
        try {
            res = networkConnection.execute(uri).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StringHelperAPI helper = new StringHelperAPI(res);
        return helper.getCurrencyRate();
    }

    private void getAllCurrencyRate(String uri,int position,int flag) {
        switch (flag) {
            case 1 :
                getAllSellCurrency(uri,position);
                break;
            case 2: getAllBuyCurrency(uri,position);
            break;
        }

    }
    private void getAllSellCurrency(String uri,int position) {
        networkConnection = new NetworkAPI();
        String res = null;
        try {
            res = networkConnection.execute(uri).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StringHelperAPI helperAPI = new StringHelperAPI(res);
        transactions = helperAPI.getAllBuyCurrencyTransactions();
        listViewAdapter = new CustomListViewAdapter(getActivity(),transactions,position);
        listViewAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();
    }

    private void getAllBuyCurrency(String uri,int position) {
        networkConnection = new NetworkAPI();
        String res = null;
        try {
            res = networkConnection.execute(uri).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StringHelperAPI helperAPI = new StringHelperAPI(res);
        transactions = helperAPI.getAllCurrencyTransactions();
        listViewAdapter = new CustomListViewAdapter(getActivity(),transactions,position);
        listViewAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();
    }

    public void search() {
        spinner_league_title = spinnerLeague.getSelectedItem().toString();
        if(getAllSellCheckBoxStatus()) {
            String url = prepareAllBuyURL(ItemProperties.itemIds[spinner_buy_position]);
            System.out.println("All Select url is :" + url);
            getAllCurrencyRate(url, spinner_buy_position,1);
        }
        else if(getAllBuyCheckBoxStatus()) {
            String url = prepareAllSearchURL(ItemProperties.itemIds[spinner_sell_position]);
            System.out.println("URl of allbuy :" + url);
            getAllCurrencyRate(url, spinner_sell_position,2);
        }
        else {
            String url = prepareURL(ItemProperties.itemIds[spinner_sell_position],ItemProperties.itemIds[spinner_buy_position]);

            String [] vals = getCurrencyRate(url).split(" ");
            double value1 = Double.parseDouble(vals[0]);
            double value2 = Double.parseDouble(vals[1]);
            transactions = new ArrayList<>();

            CurrencyTransaction transaction = new CurrencyTransaction(value1,value2, spinner_sell_position);
            transactions.add(transaction);
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            listViewAdapter = new CustomListViewAdapter(getActivity(),transactions, spinner_buy_position);
            listViewAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(listViewAdapter);
        }
    }

    private void setAdapter(Context context) {
       adapter = new CustomSpinnerAdapter(context,ItemProperties.itemNames
                , ItemProperties.itemImages);
    }

    private void setCheckBoxes() {
        sell.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked) {
                    buy.setChecked(false);
                }
            }
        });
        buy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked) {
                    sell.setChecked(false);
                }
            }
        });
    }

    private String prepareURL(int id1,int id2){
        return "http://currency.poe.trade/search?league="+spinner_league_title+"&online=x&stock=&want="
                + id2 +"&have=" + id1;
    }

    private String prepareAllSearchURL(int id2) {
        return "http://currency.poe.trade/search?league="+spinner_league_title+"&online=x&stock=&want=&have=" + id2;
    }
    private String prepareAllBuyURL(int id1) {
        return "http://currency.poe.trade/search?league="+spinner_league_title+"&online=x&stock=&want="+id1 +"&have=";
    }

    private boolean getAllSellCheckBoxStatus() {
        return sell.isChecked();
    }
    private  boolean getAllBuyCheckBoxStatus() {
        return buy.isChecked();
    }
    private void setSpinner(){

        spinnerSell.setAdapter(adapter);

        spinnerBuy.setAdapter(adapter);

        ArrayAdapter<CharSequence> league_adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.league_array, android.R.layout.simple_spinner_item);
        league_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLeague.setAdapter(league_adapter);
        spinnerOnClickPrepare();
    }

    private void spinnerOnClickPrepare() {
        spinnerSell.setSelection(0);
        spinnerBuy.setSelection(0);
        spinnerLeague.setSelection(0);
        spinnerSell.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spinner_sell_position = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        spinnerBuy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spinner_buy_position = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }
}
