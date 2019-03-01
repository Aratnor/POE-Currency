package com.lambadam.tuna.poecurrency.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lambadam.tuna.poecurrency.R;
import com.lambadam.tuna.poecurrency.elements.CurrencyTransaction;
import com.lambadam.tuna.poecurrency.elements.ItemProperties;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class CustomListViewAdapter extends RecyclerView.Adapter<CustomListViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CurrencyTransaction> transactions;
    private int count;
    private int positionHave;
    public CustomListViewAdapter(@NonNull Context context, ArrayList<CurrencyTransaction> transactions,int positionHave) {
        this.context = context;
        this.transactions = transactions;
        this.positionHave = positionHave;
        count = transactions.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(inflater.inflate(R.layout.custom_listview_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindCurrencyTransaction(transactions.get(i));
    }

    @Override
    public int getItemCount() {
        return count;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemText1;
        ImageView itemImage1;
        TextView itemText2;
        ImageView itemImage2;
        private Context context;
        ViewHolder(View inflate) {
            super(inflate);
            itemText1 = inflate.findViewById(R.id.list_view_row_text1);
            itemImage1 = inflate.findViewById(R.id.list_view_row_image1);
            itemText2 = inflate.findViewById(R.id.list_view_row_text2);
            itemImage2 = inflate.findViewById(R.id.list_view_row_image2);
        }
        public void bindCurrencyTransaction(CurrencyTransaction transaction) {
            itemText1.setText("1 x");
            itemImage1.setImageResource(ItemProperties.itemImages[positionHave]);
            double ratio ;
            if(transaction.getBuyCurrency() == 0) ratio =0;
            else ratio = transaction.getSellCurrency()/ transaction.getBuyCurrency();
            BigDecimal bd = new BigDecimal(ratio);
            bd = bd.setScale(4, RoundingMode.HALF_UP);
            String res = bd.toString() + " x";
            itemText2.setText(res);
            itemImage2.setImageResource(ItemProperties.itemImages[transaction.getPosition()]);
        }
    }
}
