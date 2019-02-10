package com.example.tuna.poecurrency;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.tuna.poecurrency.async.OnTaskCompleted;
import com.example.tuna.poecurrency.elements.Item;
import com.example.tuna.poecurrency.network.NetworkAPI;
import com.example.tuna.poecurrency.stringutils.StringHelperAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {
    String [] itemNames = {"Orb of Alteration","Orb of Fusing","Orb of Alchemy",
            "Chaos Orb","Gemcutters Prism","Exalted Orb","Chromatic Orb",
            "Jewellers Orb","Orb of Chance","Cartographers Chisel",
            "Orb of Scouring","Blessed Orb","Orb of Regret","Regal Orb",
            "Divine Orb","Vaal Orb","Scroll of Wisdom","Portal Scroll",
            "Armourers Scrap","Blacksmiths Whetstone","Glassblowers Bauble",
            "Orb of Transmutation","Orb of Augmentation","Mirror of Kalandra",
            "Eternal Orb","Perandus Coin","Silver Coin"};
    int [] itemIds = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18
            ,19,20,21,22,23,24,25,26,35};
    int [] imgSrc = {
            R.drawable.orb_of_alteration_inventory_icon, R.drawable.orb_of_fusing_inventory_icon,
            R.drawable.orb_of_alchemy_inventory_icon,R.drawable.chaos_orb_inventory_icon,
            R.drawable.gemcutters_prism_inventory_icon,R.drawable.exalted_orb_inventory_icon,
            R.drawable.chromatic_orb_inventory_icon,R.drawable.jewellers_orb_inventory_icon,
            R.drawable.orb_of_chance_inventory_icon,R.drawable.cartographers_chisel_inventory_icon,
            R.drawable.orb_of_scouring_inventory_icon,R.drawable.blessed_orb_inventory_icon,
            R.drawable.orb_of_regret_inventory_icon,R.drawable.regal_orb_inventory_icon,
            R.drawable.divine_orb_inventory_icon,R.drawable.vaal_orb_inventory_icon,
            R.drawable.scroll_of_wisdom_inventory_icon,R.drawable.portal_scroll_inventory_icon,
            R.drawable.armourers_scrap_inventory_icon,R.drawable.blacksmiths_whetstone_inventory_icon,
            R.drawable.glassblowers_bauble_inventory_icon,R.drawable.orb_of_transmutation_inventory_icon,
            R.drawable.orb_of_augmentation_inventory_icon,R.drawable.mirror_of_kalandra_inventory_icon,
            R.drawable.eternal_orb_inventory_icon,R.drawable.perandus_coin_inventory_icon,R.drawable.silver_coin_inventory_icon
    };
        ArrayList<Item> array;

    NetworkAPI networkConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getItems();


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

    @Override
    protected void onStart() {
        super.onStart();

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
        } else {
        }

    }

    private void getItems(){
        networkConnection = new NetworkAPI(this);
        String currencyURL = "http://currency.poe.trade/";
        try {
            String res = networkConnection.execute(currencyURL).get();
            StringHelperAPI helper = new StringHelperAPI(res);
            array = helper.findTitleAndId(res);
            for(Item m : array) {
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getCurrencyRate(String uri){
        networkConnection = new NetworkAPI(this);
        String res = null;
        try {
            res = networkConnection.execute(uri).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StringHelperAPI helper = new StringHelperAPI(res);
        String result = helper.getCurrencyRate();
        System.out.println("Return value is : " + result);
    }

    private void setSpinner(){
        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2 = findViewById(R.id.spinner2);

        ArrayList<String> items = getNames();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);

        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);

    }
    private ArrayList<String> getNames(){
        ArrayList<String> result = new ArrayList<>();
        for(int i=0;i<array.size();i++){
            result.add(array.get(i).getName());
        }
        return result;
    }


    public void search(View view) {
        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2 = findViewById(R.id.spinner2);
        String name1 = spinner1.getSelectedItem().toString();
        System.out.println("First item name is :" +name1);
        int id1 = findIDValue(name1);
        String name2 = spinner2.getSelectedItem().toString();
        System.out.println("Second item name is :" +name2);
        int id2 = findIDValue(name2);
        String url = prepareURL(id1,id2);
        getCurrencyRate(url);
    }

    private String prepareURL(int id1,int id2){
        return "http://currency.poe.trade/search?league=Betrayal&online=x&stock=&want="
                + id1 +"&have=" + id2;
    }

    private int findIDValue(String name){
        for(int i = 0;i<array.size();i++){
            if(array.get(i).getName().contains(name))return array.get(i).getId();
        }
        return -1;
    }

    @Override
    public void onTaskCompleted() {
        setSpinner();
    }
}
