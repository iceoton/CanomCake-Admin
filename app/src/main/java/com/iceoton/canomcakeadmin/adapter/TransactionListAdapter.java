package com.iceoton.canomcakeadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.medel.Transaction;

import java.util.ArrayList;

public class TransactionListAdapter extends BaseAdapter{
    Context mContex;
    ArrayList<Transaction> transactions;

    public TransactionListAdapter(Context mContex, ArrayList<Transaction> transactions) {
        this.mContex = mContex;
        this.transactions = transactions;
    }

    static class ViewHolder {
        public TextView txtId;
        public TextView txtOrderId;
    }

    @Override
    public int getCount() {
        return transactions.size();
    }

    @Override
    public Object getItem(int position) {
        return transactions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return transactions.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_transaction, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtId = (TextView) convertView.findViewById(R.id.text_id);
            viewHolder.txtOrderId = (TextView) convertView.findViewById(R.id.text_order_id);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Transaction transaction = transactions.get(position);
        viewHolder.txtId.setText("#" + String.valueOf(transaction.getId()));
        viewHolder.txtOrderId.setText("#" + String.valueOf(transaction.getOrderId()));

        return convertView;
    }
}
