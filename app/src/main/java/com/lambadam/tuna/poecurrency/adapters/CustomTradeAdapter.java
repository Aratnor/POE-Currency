package com.lambadam.tuna.poecurrency.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lambadam.tuna.poecurrency.R;
import com.lambadam.tuna.poecurrency.elements.ItemProperties;
import com.lambadam.tuna.poecurrency.elements.TradeTransaction;
import com.lambadam.tuna.poecurrency.fragments.popups.TradeDialogFragment;

import java.util.ArrayList;

public class CustomTradeAdapter extends RecyclerView.Adapter<CustomTradeAdapter.ViewHolder> {
    ArrayList<TradeTransaction> transactions;
    private Context activityContext;
    private int count ;
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
        final int stock_value = transactions.get(i).getStock();
        final String userName_value = "@"+ transactions.get(i).getUserName()+" selling item";
        viewHolder.stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TradeDialogFragment dialog = new TradeDialogFragment();
                dialog.setText(stock_value);
                dialog.show(((FragmentActivity)activityContext).getSupportFragmentManager(),"Total Stock");
            }
        });
        viewHolder.send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TradeDialogFragment dialog = new TradeDialogFragment();
                dialog.setUserName(userName_value);
                dialog.show(((FragmentActivity)activityContext).getSupportFragmentManager(),"Send message");
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
        ImageButton stock,send_message;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            sell = itemView.findViewById(R.id.list_trade_view_row_sell_image);
            sellAmount = itemView.findViewById(R.id.list_trade_view_row_sell_text);
            buy = itemView.findViewById(R.id.list_trade_view_row_buy_image);
            buyAmount = itemView.findViewById(R.id.list_trade_view_row_buy_text);

            stock = itemView.findViewById(R.id.trade_stock_button);
            send_message = itemView.findViewById(R.id.send_message_button);
        }

        private void bindView(TradeTransaction transaction) {
            String sellVal = transaction.getSellValue()+" x";
            sellAmount.setText(sellVal);
            sell.setImageResource(ItemProperties.itemImages[transaction.getSellId()-1]);
            String buyVal = transaction.getBuyValue()+" x";
            buyAmount.setText(buyVal);
            buy.setImageResource(ItemProperties.itemImages[transaction.getBuyId()-1]);
        }
    }
}
