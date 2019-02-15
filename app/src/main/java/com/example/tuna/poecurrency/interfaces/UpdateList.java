package com.example.tuna.poecurrency.interfaces;

import com.example.tuna.poecurrency.elements.CurrencyTransaction;

import java.util.ArrayList;

public interface UpdateList {
    void updateOneItem(CurrencyTransaction val);
    void updateAllBuyCurrency(ArrayList<CurrencyTransaction> transactions);
    void updateAllSellCurrency(ArrayList<CurrencyTransaction> transactions);
}
