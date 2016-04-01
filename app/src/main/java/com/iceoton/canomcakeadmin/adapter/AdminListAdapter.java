package com.iceoton.canomcakeadmin.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.medel.Admin;

import java.util.ArrayList;

public class AdminListAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Admin> admins;

    public AdminListAdapter(Context mContext, ArrayList<Admin> admins) {
        this.mContext = mContext;
        this.admins = admins;
    }

    static class ViewHolder {
        public TextView txtFullname;
        public TextView txtEmail;

    }

    @Override
    public int getCount() {
        return admins.size();
    }

    @Override
    public Admin getItem(int position) {
        return admins.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(admins.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_admin, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtFullname = (TextView) convertView.findViewById(R.id.text_fullname);
            viewHolder.txtEmail = (TextView) convertView.findViewById(R.id.text_email);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Admin admin = admins.get(position);
        viewHolder.txtFullname.setText(admin.getFullname());
        viewHolder.txtEmail.setText(admin.getEmail());

        return convertView;
    }
}
