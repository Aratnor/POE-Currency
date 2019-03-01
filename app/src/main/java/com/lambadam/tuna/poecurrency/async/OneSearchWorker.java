package com.lambadam.tuna.poecurrency.async;

import android.os.AsyncTask;

import com.lambadam.tuna.poecurrency.elements.CurrencyTransaction;
import com.lambadam.tuna.poecurrency.interfaces.UpdateList;
import com.lambadam.tuna.poecurrency.stringutils.StringHelperAPI;

public class OneSearchWorker extends AsyncTask<String,Void,CurrencyTransaction> {
    UpdateList updateList;
    public OneSearchWorker(UpdateList updateList) {
        this.updateList = updateList;
    }

    @Override
    protected CurrencyTransaction doInBackground(String... strings) {
        StringHelperAPI helperAPI = new StringHelperAPI(strings[0]);
        String [] vals =  helperAPI.getCurrencyRate().split(" ");
        double value1;
        double value2;
        if(vals[0].equals("No-Trade")){
            value1 = 0;
            value2 = 0;
        }
        else {
            value1 = Double.parseDouble(vals[0]);
            value2 = Double.parseDouble(vals[1]);
        }

        return new CurrencyTransaction(value1,value2
                , Integer.parseInt(strings[1]));
    }

    @Override
    protected void onPostExecute(CurrencyTransaction transaction) {
        super.onPostExecute(transaction);
        updateList.updateOneItem(transaction);
    }

}
