package com.lambadam.tuna.poecurrency.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.app.Fragment;

import com.lambadam.tuna.poecurrency.R;
import com.lambadam.tuna.poecurrency.adapters.CustomLeagueSpinnerAdapter;
import com.lambadam.tuna.poecurrency.adapters.CustomListViewAdapter;
import com.lambadam.tuna.poecurrency.adapters.CustomSpinnerAdapter;
import com.lambadam.tuna.poecurrency.async.AllBuyWorker;
import com.lambadam.tuna.poecurrency.async.AllSellWorker;
import com.lambadam.tuna.poecurrency.async.OneSearchWorker;
import com.lambadam.tuna.poecurrency.elements.CurrencyTransaction;
import com.lambadam.tuna.poecurrency.elements.ItemProperties;
import com.lambadam.tuna.poecurrency.interfaces.UpdateList;
import com.lambadam.tuna.poecurrency.network.NetworkAPI;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Exchange_Fragment extends Fragment implements UpdateList {
    CheckBox sell_checkBox, buy_checkBox;
    Spinner spinnerSell,spinnerBuy,spinnerLeague;
    RecyclerView mRecyclerView;
    RelativeLayout loadingPanel;
    ImageView league_icon;

    CustomSpinnerAdapter adapter;
    CustomLeagueSpinnerAdapter league_adapter;
    int spinner_sell_position = 0;
    int spinner_buy_position = 0;
    String spinner_league_title = "Betrayal";
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
        buy_checkBox = rootView.findViewById(R.id.search_all_buy_checkBox);
        sell_checkBox = rootView.findViewById(R.id.search_all_sell_checkBox);

        spinnerSell = rootView.findViewById(R.id.spinnerSell);
        spinnerBuy = rootView.findViewById(R.id.spinnerBuy);
        spinnerLeague = rootView.findViewById(R.id.league);

        loadingPanel = rootView.findViewById(R.id.loadingPanel);

        league_icon = rootView.findViewById(R.id.league_image);

        setAdapter(getActivity());
        setSpinner();
        setCheckBoxes();

        FloatingActionButton search_button = rootView.findViewById(R.id.search_button);

        league_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerLeague.performClick();
            }
        });


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

    private void getOneCurrencyTransaction(){
        networkConnection = new NetworkAPI();
        String res = null;
        String url = prepareURL(ItemProperties.itemIds[spinner_sell_position],ItemProperties.itemIds[spinner_buy_position]);
        try {
            res = networkConnection.execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String spinnerSelPos = spinner_sell_position+"";
        OneSearchWorker oneSearchWorker = new OneSearchWorker(this);
        oneSearchWorker.execute(res,spinnerSelPos);
    }

    private void getAllCurrencyRate(String uri,int position,int flag) {
        switch (flag) {
            case 1 :
                getAllSellCurrency(uri);
                break;
            case 2: getAllBuyCurrency(uri);
            break;
        }

    }
    private void getAllSellCurrency(String uri) {
        networkConnection = new NetworkAPI();
        String res = null;
        try {
            res = networkConnection.execute(uri).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AllSellWorker allSellWorker = new AllSellWorker(this);
        allSellWorker.execute(res);
    }

    private void getAllBuyCurrency(String uri) {
        networkConnection = new NetworkAPI();
        String res = null;
        try {
            res = networkConnection.execute(uri).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        AllBuyWorker allBuyWorker = new AllBuyWorker(this);
        allBuyWorker.execute(res);
    }

    public void search() {
        loadingPanel.setVisibility(View.VISIBLE);
        if(getAllSellCheckBoxStatus()) {
            String url = prepareAllBuyURL(ItemProperties.itemIds[spinner_buy_position]);
            System.out.println("All Select url is :" + url);
            getAllCurrencyRate(url, spinner_buy_position,1);
        }
        else if(getAllBuyCheckBoxStatus()) {
            //spinnerSell.setSelection(ItemProperties.itemNames.length-1);
            String url = prepareAllSearchURL(ItemProperties.itemIds[spinner_sell_position]);
            System.out.println("URl of allbuy :" + url);
            getAllCurrencyRate(url, spinner_sell_position,2);
        }
        else {
            getOneCurrencyTransaction();
        }
    }

    private void setAdapter(Context context) {
       adapter = new CustomSpinnerAdapter(context,ItemProperties.itemNames
                , ItemProperties.itemImages);
        league_adapter =
                new CustomLeagueSpinnerAdapter(getActivity(),ItemProperties.leagues);
    }

    private void setCheckBoxes() {
        sell_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked) {
                    buy_checkBox.setChecked(false);
                }
            }
        });
        buy_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked) {
                    sell_checkBox.setChecked(false);
                }
            }
        });
    }

    private String prepareURL(int id1,int id2){
        if(spinner_league_title.equals("Hardcore Betrayal"))
        return "http://currency.poe.trade/search?league=Hardcore+Betrayal&online=x&stock=&want="
                + id2 +"&have=" + id1;
        else
            return "http://currency.poe.trade/search?league="+spinner_league_title+"&online=x&stock=&want="
                + id2 +"&have=" + id1;
    }

    private String prepareAllSearchURL(int id2) {
        if(spinner_league_title.equals("Hardcore Betrayal"))
            return "http://currency.poe.trade/search?league=Hardcore+Betrayal&online=x&stock=&want=&have=" + id2;
        else
        return "http://currency.poe.trade/search?league="+spinner_league_title+"&online=x&stock=&want=&have=" + id2;
    }
    private String prepareAllBuyURL(int id1) {
        if(spinner_league_title.equals("Hardcore Betrayal"))
            return "http://currency.poe.trade/search?league=Hardcore+Betrayal&online=x&stock=&want="+id1 +"&have=";
        else
        return "http://currency.poe.trade/search?league="+spinner_league_title+"&online=x&stock=&want="+id1 +"&have=";
    }

    private boolean getAllSellCheckBoxStatus() {
        return sell_checkBox.isChecked();
    }
    private  boolean getAllBuyCheckBoxStatus() {
        return buy_checkBox.isChecked();
    }
    private void setSpinner(){

        spinnerSell.setAdapter(adapter);

        spinnerBuy.setAdapter(adapter);

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
        spinnerLeague.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_league_title = ItemProperties.leagues[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void updateOneItem(CurrencyTransaction val) {

        transactions = new ArrayList<>();
        transactions.add(val);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        listViewAdapter = new CustomListViewAdapter(getActivity(),transactions, spinner_buy_position);
        listViewAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(listViewAdapter);
        loadingPanel.setVisibility(View.GONE);
    }

    @Override
    public void updateAllBuyCurrency(ArrayList<CurrencyTransaction> transactions) {
        listViewAdapter = new CustomListViewAdapter(getActivity(),transactions,spinner_sell_position);
        listViewAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();
        loadingPanel.setVisibility(View.GONE);
    }

    @Override
    public void updateAllSellCurrency(ArrayList<CurrencyTransaction> transactions) {
        //buy pos
        listViewAdapter = new CustomListViewAdapter(getActivity(),transactions,spinner_buy_position);
        listViewAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();
        loadingPanel.setVisibility(View.GONE);
    }
}
