package com.example.tuna.poecurrency.stringutils;

import com.example.tuna.poecurrency.elements.Item;

import java.util.ArrayList;

public class StringHelperAPI {
    String res = "";
    public StringHelperAPI(String res){
        this.res = res;
    }

    public String getCurrencyRate(){
        String[] lines = res.split(System.getProperty("line.separator"));
        String line = "";
        for(int i = 0;i<lines.length;i++){
            if(lines[i].contains("Oopsie! Nothing was found. If you want to buy currency why don't you")){
                return "0 0";
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
    private String[] resToLine(String res) {
        return res.split(System.getProperty("line.separator"));
    }
//--------------------------------------------------------------------------------->
    public ArrayList<Item> findTitleAndId(String res){
        ArrayList<Item> items = new ArrayList<>();
        String [] lines = resToLine(res);
        boolean check = false;
        String name = "";
        StringBuilder namesArray = new StringBuilder();
        StringBuilder idsArray = new StringBuilder();
        for(int i = 0;i<lines.length;i++){
            if(lines[i].contains("<div id=\"cat-want-0\"")){
                check = true;
            }
            else if(lines[i].contains("<div id=\"cat-want-1\"")) break;
            if(check){
                if(!lines[i].contains("data-title") && lines[i].contains("title")){
                    String [] words = lines[i].split("=");
                    String result = words[1];
                    name = checkRegex(result);

                }
                else if(lines[i].contains("data-id")){
                    res = fetchNumRegex(lines[i]);
                    int id = Integer.parseInt(res);
                    Item item = new Item(name,id);
                    if (items.size() == 1) {
                        namesArray.append("{,\"").append(name).append("\"");
                        idsArray.append("{,\"").append(id).append("\"");
                    }
                    else {
                        namesArray.append(",\"").append(name).append("\"");
                        idsArray.append(",").append(id);
                    }
                    items.add(item);
                }

            }
        }
        System.out.println(namesArray.toString());
        System.out.println(idsArray.toString());
        return items;

    }

    private String fetchNumRegex(String res){
        return res.replaceAll("\\D","");
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
