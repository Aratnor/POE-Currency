package com.example.tuna.poecurrency.elements;

public class CurrencyTransaction {
    private double buyCurrency;
    private double sellCurrency;
    private int position;

    public double getBuyCurrency() {
        return buyCurrency;
    }

    public void setBuyCurrency(double buyCurrency) {
        this.buyCurrency = buyCurrency;
    }

    public double getSellCurrency() {
        return sellCurrency;
    }

    public void setSellCurrency(double sellCurrency) {
        this.sellCurrency = sellCurrency;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public CurrencyTransaction(double buyCurrency, double sellCurrency, int position) {
        this.buyCurrency = buyCurrency;
        this.sellCurrency = sellCurrency;

        this.position = position;
    }
}
