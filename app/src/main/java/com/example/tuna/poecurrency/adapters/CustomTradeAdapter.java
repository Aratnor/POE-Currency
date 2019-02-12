package com.example.tuna.poecurrency.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.tuna.poecurrency.R;
import com.example.tuna.poecurrency.elements.ItemProperties;
import com.example.tuna.poecurrency.elements.TradeTransaction;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CustomTradeAdapter extends RecyclerView.Adapter<CustomTradeAdapter.ViewHolder> {
    ArrayList<TradeTransaction> transactions;
    private Context activityContext;
    private int count ;
    private PopupWindow mPopupWindow;
    public CustomTradeAdapter(Context context,ArrayList<TradeTransaction> transactions) {
        this.transactions = transactions;
        this.activityContext = context;
        count = transactions.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(inflater.inflate(R.layout.custom_trade_list_view_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) activityContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.popup_stock_trade,null);

                mPopupWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
            }
        });

        viewHolder.bindView(transactions.get(i));
    }

    @Override
    public int getItemCount() {
        return count;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sell,buy;
        TextView sellAmount,buyAmount;
        ImageButton stock;


        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            //stockAmount =itemView.findViewById(R.id.trade_stock_text);
            sell = itemView.findViewById(R.id.list_trade_view_row_sell_image);
            sellAmount = itemView.findViewById(R.id.list_trade_view_row_sell_text);
            buy = itemView.findViewById(R.id.list_trade_view_row_buy_image);
            buyAmount = itemView.findViewById(R.id.list_trade_view_row_buy_text);
            //userName = itemView.findViewById(R.id.list_trade_view_username);

            stock = itemView.findViewById(R.id.trade_stock_button);
        }

        private void bindView(TradeTransaction transaction) {
            String sAmount = transaction.getStock()+"";
            //stockAmount.setText(sAmount);
            String sellVal = transaction.getSellValue()+" x";
            sellAmount.setText(sellVal);
            sell.setImageResource(ItemProperties.itemImages[transaction.getSellId()-1]);
            String buyVal = transaction.getBuyValue()+" x";
            buyAmount.setText(buyVal);
            buy.setImageResource(ItemProperties.itemImages[transaction.getBuyId()-1]);
            //userName.setText(transaction.getUserName());
        }
    }
}
