package com.example.tuna.poecurrency;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tuna.poecurrency.adapters.CustomListViewAdapter;
import com.example.tuna.poecurrency.adapters.CustomSpinnerAdapter;
import com.example.tuna.poecurrency.elements.CurrencyTransaction;
import com.example.tuna.poecurrency.elements.ItemProperties;
import com.example.tuna.poecurrency.network.NetworkAPI;
import com.example.tuna.poecurrency.stringutils.StringHelperAPI;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity{
    int spinner1Pos = 0;
    int spinner2Pos = 0;
    static ArrayList<CurrencyTransaction> transactions;
    CustomListViewAdapter listViewAdapter;
    NetworkAPI networkConnection;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        setSpinner();
        setStatusBarColor();
        setCheckBoxes();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermissions();

    }
    private void setStatusBarColor() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(ContextCompat.getColor(this,R.color.design_status_bar));
        }
    }

    private void setCheckBoxes() {
        final CheckBox want = findViewById(R.id.search_all_want_checkBox);
        final CheckBox have = findViewById(R.id.search_all_have_checkBox);
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

    private boolean getAllWantCheckBoxStatus() {
        CheckBox checkBox = findViewById(R.id.search_all_want_checkBox);
        return checkBox.isChecked();
    }
    private  boolean getAllHaveCheckBoxStatus() {
        CheckBox checkBox = findViewById(R.id.search_all_have_checkBox);
        return checkBox.isChecked();
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
        for(CurrencyTransaction transaction : transactions) System.out.println("Id:"
                + transaction.getPosition() + " SellValue :" + transaction.getSellCurrency()
                + " BuyValue : " + transaction.getBuyCurrency());
        RecyclerView mRecyclerView = findViewById(R.id.transactions);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        listViewAdapter = new CustomListViewAdapter(this,transactions,position);
        listViewAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(listViewAdapter);

    }

    private void setSpinner(){
        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2 = findViewById(R.id.spinner2);

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this,ItemProperties.itemNames
                , ItemProperties.itemImages);

        spinner1.setAdapter(adapter);

        spinner2.setAdapter(adapter);

        spinnerOnClickPrepare(spinner1,spinner2);
    }


    public void search(View view) {
        if(getAllWantCheckBoxStatus() && getAllHaveCheckBoxStatus()) {
            String url = "http://currency.poe.trade/search?league=Betrayal&online=x&stock=&want=&have=";
        }
        else if(getAllWantCheckBoxStatus()) {
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
            RecyclerView mRecyclerView = findViewById(R.id.transactions);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            listViewAdapter = new CustomListViewAdapter(this,transactions,spinner2Pos);
            listViewAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(listViewAdapter);
        }
    }
    private void spinnerOnClickPrepare(Spinner s1, Spinner s2) {
        s1.setSelection(0);
        s2.setSelection(0);
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spinner1Pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    private String prepareURL(int id1,int id2){
        return "http://currency.poe.trade/search?league=Betrayal&online=x&stock=&want="
                + id1 +"&have=" + id2;
    }

    private String prepareAllSearchURL(int id2) {
        return "http://currency.poe.trade/search?league=Betrayal&online=x&stock=&want=&have=" + id2;
    }

    private void checkPermissions () {
        internetPermission();
    }
    private void internetPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        0x8000000);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

}
