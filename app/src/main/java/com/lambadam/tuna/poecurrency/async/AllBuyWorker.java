package com.lambadam.tuna.poecurrency.async;

import android.os.AsyncTask;

import com.lambadam.tuna.poecurrency.elements.CurrencyTransaction;
import com.lambadam.tuna.poecurrency.interfaces.UpdateList;
import com.lambadam.tuna.poecurrency.stringutils.StringHelperAPI;

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
