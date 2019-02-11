package com.example.tuna.poecurrency.elements;

public class TradeTransaction {
    private double sellValue;
    private double buyValue;
    private int sellId;
    private int buyId;
    private int stock;
    private String userName;

    public TradeTransaction(double sellValue, double buyValue, int sellId, int buyId, int stock, String userName) {
        this.sellValue = sellValue;
        this.buyValue = buyValue;
        this.sellId = sellId;
        this.buyId = buyId;
        this.stock = stock;
        this.userName = userName;
    }

    public double getSellValue() {
        return sellValue;
    }

    public double getBuyValue() {
        return buyValue;
    }

    public int getSellId() {
        return sellId;
    }

    public int getBuyId() {
        return buyId;
    }

    public int getStock() {
        return stock;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "SellValue :" + sellValue + "\nSellId:" + sellId
                + "\nBuyValue:" +buyValue + "\nBuyId:" +  buyId
                + "\nStock:" + stock;
    }
}
