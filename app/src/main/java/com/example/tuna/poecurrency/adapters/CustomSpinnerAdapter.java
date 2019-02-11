package com.example.tuna.poecurrency.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tuna.poecurrency.R;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private String [] itemNames;
    int [] itemIds;
    private int [] spinnerIcons;
    private Context activityContext;
    public CustomSpinnerAdapter(@NonNull Context context,
                                String [] itemNames, int [] spinnerIcons) {
        super(context, R.layout.custom_spinner_row);
        this.itemNames = itemNames;
        this.spinnerIcons = spinnerIcons;
        this.activityContext =context;
    }


    @Override
    public int getCount() {
        return itemNames.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)activityContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.custom_spinner_row, parent, false);
            viewHolder.mFlag = convertView.findViewById(R.id.item_image);
            viewHolder.mName = convertView.findViewById(R.id.item_name);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mName.setText(itemNames[position]);
        viewHolder.mFlag.setImageResource(spinnerIcons[position]);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    private static class ViewHolder {
        ImageView mFlag;
        TextView mName;
    }
}
