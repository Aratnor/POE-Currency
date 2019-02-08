package com.example.tuna.poecurrency.network;

import android.os.AsyncTask;

import com.example.tuna.poecurrency.MainActivity;
import com.example.tuna.poecurrency.async.OnTaskCompleted;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkAPI extends AsyncTask<String,Integer,String> {
    private OnTaskCompleted listener;


    public NetworkAPI(OnTaskCompleted listener){
        this.listener=listener;
    }
    private  String connectURL(String uri) {
        try {
            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String s = readStream(in);
            urlConnection.disconnect();
            return s;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

    private String readStream(InputStream is) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            /*FileWriter writer = new FileWriter(new File("output.txt"));
            writer.write(sb.toString());*/
            return sb.toString();
        } catch (IOException e) {
            return "";
        }
    }

    @Override
    protected String doInBackground(String... strings) {
       return connectURL(strings[0]);
        //return getItemNames();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.onTaskCompleted();
    }
}
