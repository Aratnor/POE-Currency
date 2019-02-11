package com.example.tuna.poecurrency.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
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
    CheckBox want ,have;
    Spinner spinner1,spinner2;
    RecyclerView mRecyclerView;

    CustomSpinnerAdapter adapter;
    int spinner1Pos = 0;
    int spinner2Pos = 0;
    static ArrayList<CurrencyTransaction> transactions;
    CustomListViewAdapter listViewAdapter;
    NetworkAPI networkConnection;
    private RecyclerView.LayoutManager mLayoutManager;
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
        want = rootView.findViewById(R.id.search_all_want_checkBox);
        have = rootView.findViewById(R.id.search_all_have_checkBox);

        spinner1 = rootView.findViewById(R.id.spinner1);
        spinner2 = rootView.findViewById(R.id.spinner2);

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

    private void getAllCurrencyRate(String uri,int position) {
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
        if(getAllWantCheckBoxStatus()) {
            String url = prepareAllSearchURL(ItemProperties.itemIds[spinner2Pos]);
            System.out.println("All Select url is :" + url);
            getAllCurrencyRate(url,spinner2Pos);
        }
        else if(getAllHaveCheckBoxStatus()) {
            String url = prepareAllSearchURL(ItemProperties.itemIds[spinner1Pos]);
            getAllCurrencyRate(url,spinner1Pos);
        }
        else {
            String url = prepareURL(ItemProperties.itemIds[spinner1Pos],ItemProperties.itemIds[spinner2Pos]);

            String [] vals = getCurrencyRate(url).split(" ");
            double value1 = Double.parseDouble(vals[0]);
            double value2 = Double.parseDouble(vals[1]);
            transactions = new ArrayList<>();

            CurrencyTransaction transaction = new CurrencyTransaction(value1,value2,spinner1Pos);
            transactions.add(transaction);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            listViewAdapter = new CustomListViewAdapter(getActivity(),transactions,spinner2Pos);
            listViewAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(listViewAdapter);
        }
    }

    private void setAdapter(Context context) {
       adapter = new CustomSpinnerAdapter(context,ItemProperties.itemNames
                , ItemProperties.itemImages);
    }

    private void setCheckBoxes() {
        want.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked) {
                    have.setChecked(false);
                }
            }
        });
        have.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked) {
                    want.setChecked(false);
                }
            }
        });
    }

    private String prepareURL(int id1,int id2){
        return "http://currency.poe.trade/search?league=Betrayal&online=x&stock=&want="
                + id1 +"&have=" + id2;
    }

    private String prepareAllSearchURL(int id2) {
        return "http://currency.poe.trade/search?league=Betrayal&online=x&stock=&want=&have=" + id2;
    }


    private boolean getAllWantCheckBoxStatus() {
        return want.isChecked();
    }
    private  boolean getAllHaveCheckBoxStatus() {
        return have.isChecked();
    }
    private void setSpinner(){

        spinner1.setAdapter(adapter);

        spinner2.setAdapter(adapter);

        spinnerOnClickPrepare();
    }

    private void spinnerOnClickPrepare() {
        spinner1.setSelection(0);
        spinner2.setSelection(0);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spinner1Pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spinner2Pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }
}
