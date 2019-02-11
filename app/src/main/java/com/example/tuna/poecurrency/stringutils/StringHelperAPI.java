package com.example.tuna.poecurrency.stringutils;

import android.annotation.SuppressLint;

import com.example.tuna.poecurrency.elements.CurrencyTransaction;
import com.example.tuna.poecurrency.elements.ItemProperties;
import com.example.tuna.poecurrency.elements.TradeTransaction;

import java.util.ArrayList;
import java.util.HashMap;

public class StringHelperAPI {
    String res = "";
    public StringHelperAPI(String res){
        this.res = res;
    }

    public String getCurrencyRate(){
        String[] lines = resToLine(res);
        String line = "";
        for(int i = 0;i<lines.length;i++){
            if(lines[i].contains("Oopsie! Nothing was found. If you want to buy currency why don't you")){
                return "No-Trade No-Trade";
            }
            lines[i] = lines[i].trim();
            if(lines[i].contains("large-3 columns displayoffer-centered")){
                line = lines[i+1];
                break;
            }
        }
        return searchForValuesInLine(line);
    }

    private String searchForValuesInLine(String line){
        String [] words = line.split(" ");
        StringBuilder result = new StringBuilder();
        for(int i = 0;i<words.length;i++){
            if(words[i].contains("&#")){
                String value = words[i-1];
                if(value.contains(">")){
                    value =value.substring(9,value.length());
                }
                result.append(value).append(" ");
            }
        }
        return result.toString().trim();
    }

    public  ArrayList<CurrencyTransaction> getAllCurrencyTransactions() {
        String [] lines = resToLine(res);
        ArrayList<CurrencyTransaction> transactions = new ArrayList<>();
        @SuppressLint("UseSparseArrays") HashMap<Integer,Double> map = new HashMap<>();
        for(int i = 0;i<lines.length;i++) {
            lines[i] = lines[i].trim();
            int currencyID = -1 ;
            double sellValue = 0;
            double buyValue = 0;
            if(lines[i].contains("displayoffer") && !lines[i].contains("large") && lines[i].contains("username")) {
                String[] values = lines[i].split(">"); {
                    String [] words = values[0].split(" ");
                    for(String word : words) {
                        if(word.contains("data-sellcurrency")){
                            String sub = word.substring(19,word.length()-1);
                            currencyID = Integer.parseInt(sub);

                        }
                        else if(word.contains("data-sellvalue")){
                            String sub = word.substring(16,word.length()-1);
                            sellValue = Double.parseDouble(sub);
                        }
                        else if(word.contains("data-buyvalue")) {
                            String sub = word.substring(15,word.length()-1);
                            buyValue = Double.parseDouble(sub);
                        }
                    }
                }
                int currencyPosition = ItemProperties.getPosition(currencyID);
                if(currencyPosition >= 0 && !map.containsKey(currencyPosition)) {
                    map.put(currencyPosition,sellValue);
                    transactions.add(new CurrencyTransaction(buyValue,sellValue,currencyPosition));
                }
            }

        }
        return transactions;
    }

    public ArrayList<TradeTransaction> getAllTradeCurrencyTransactions(String res) {
        String [] lines = resToLine(res);
        ArrayList<TradeTransaction> transactions = new ArrayList<>();
        for(int i = 0;i<lines.length;i++) {
            lines[i] = lines[i].trim();
            int sellCurrencyID = -1;
            int buyCurrencyID = -1;
            int stock = -1;
            double sellValue = 0;
            double buyValue = 0;
            String userName = "";
            String[] values = lines[i].split(">");
            {
                String[] words = values[0].split(" ");
                for (String word : words) {
                    if(word.contains("data-username")) {
                        String sub = word.substring(13, word.length() - 1);
                        userName = sub;
                    } else if(word.contains("data-buycurrency")) {
                        String sub = word.substring(18,word.length()-1);
                        buyCurrencyID = Integer.parseInt(sub);

                    } else if (word.contains("data-sellcurrency")) {
                        String sub = word.substring(19, word.length() - 1);
                        sellCurrencyID = Integer.parseInt(sub);

                    } else if (word.contains("data-sellvalue")) {
                        String sub = word.substring(16, word.length() - 1);
                        sellValue = Double.parseDouble(sub);
                    } else if (word.contains("data-buyvalue")) {
                        String sub = word.substring(15, word.length() - 1);
                        buyValue = Double.parseDouble(sub);
                    } else if (word.contains("stock")) {
                        String sub = word.substring(7,word.length()-1);
                        stock = Integer.parseInt(sub);

                    }
                }
            }
            if(sellCurrencyID <=35 && buyCurrencyID <= 35)
                transactions.add(new TradeTransaction(sellValue,buyValue,
                        sellCurrencyID,buyCurrencyID,stock,userName));
            }
            return transactions;
        }

    private boolean checkIsEmpty(){
        return res.contains("Oopsie! Nothing was found. If you want to buy currency why don't you");
    }

    private String fetchNumRegex(String res){
        return res.replaceAll("\\D","");
    }

    private String[] resToLine(String res) {
        return res.split(System.getProperty("line.separator"));
    }

    public static String checkRegex(String s) {
        String res =  s.replaceAll("^(data-title=\")","");
        res = res.replaceAll("\"$","");
        String [] array = res.split(" ");
        array[0] = array[0].replaceAll("\\P{L}", "");
        StringBuilder builder = new StringBuilder();
        builder.append(array[0]).append(" ");
        for(int i = 1;i<array.length;i++)builder.append(array[i]).append(" ");
        return builder.toString().trim();
    }
}
