package com.example.tuna.poecurrency.async;

import android.os.AsyncTask;

import com.example.tuna.poecurrency.elements.CurrencyTransaction;
import com.example.tuna.poecurrency.interfaces.UpdateList;
import com.example.tuna.poecurrency.stringutils.StringHelperAPI;

import java.util.ArrayList;

public class AllBuyWorker extends AsyncTask<String,Void,ArrayList<CurrencyTransaction>> {
    UpdateList updateList;

    public AllBuyWorker(UpdateList updateList) {
        this.updateList = updateList;
    }
    @Override
    protected ArrayList<CurrencyTransaction> doInBackground(String... strings) {
        StringHelperAPI helperAPI = new StringHelperAPI(strings[0]);
        return helperAPI.getAllCurrencyTransactions();
    }

    @Override
    protected void onPostExecute(ArrayList<CurrencyTransaction> transactions) {
        super.onPostExecute(transactions);
        updateList.updateAllBuyCurrency(transactions);
    }
}
