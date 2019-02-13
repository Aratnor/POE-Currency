package com.example.tuna.poecurrency.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tuna.poecurrency.R;

import static com.example.tuna.poecurrency.elements.ItemProperties.itemNames;

public class CustomLeagueSpinnerAdapter extends ArrayAdapter<String> {
    String[] leagues;
    Context activityContext;

    public CustomLeagueSpinnerAdapter(@NonNull Context context, String [] leagues) {
        super(context, R.layout.spinner_league);
        activityContext = context;
        this.leagues = leagues;
    }

    @Override
    public int getCount() {
        return leagues.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) activityContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.spinner_league, parent, false);
            viewHolder.league = convertView.findViewById(R.id.spinner_league);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.league.setText(leagues[position]);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
    private static class ViewHolder{
        TextView league;
    }

}
