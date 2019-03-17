package com.lambadam.tuna.poecurrency;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lambadam.tuna.poecurrency.fragments.Exchange_Fragment;
import com.lambadam.tuna.poecurrency.fragments.Trade_Fragment;

public class MainActivity extends AppCompatActivity{
    BottomNavigationViewEx nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        setStatusBarColor();

        nav = findViewById(R.id.bottomNavigationView);
        nav.enableAnimation(false);
        nav.enableItemShiftingMode(false);
        nav.enableShiftingMode(false);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.exchangeNav:
                        Exchange_Fragment exchange_fragment = new Exchange_Fragment();
                        switchFragments(exchange_fragment);
                        return true;
                    case R.id.tradeNav:
                        Trade_Fragment trade_fragment = new Trade_Fragment();
                        switchFragments(trade_fragment);
                        return true;
                }
                return false;
            }
        });

        Exchange_Fragment exchange_fragment = new Exchange_Fragment();
        addFragments(exchange_fragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Exchange_Fragment exchange_fragment = new Exchange_Fragment();
        switchFragments(exchange_fragment);
        checkPermissions();

    }

    private void addFragments(Fragment fragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container,fragment,"1");
        transaction.commit();

    }

    private void switchFragments(Fragment fragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container,fragment,"1");
        transaction.commit();

    }
    private void setStatusBarColor() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(ContextCompat.getColor(this,R.color.design_dark));
        }
    }

    @Override
    public void onBackPressed() {
        if(nav.getSelectedItemId() == R.id.tradeNav)nav.setSelectedItemId(R.id.exchangeNav);
        else super.onBackPressed();
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
