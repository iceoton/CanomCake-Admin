package com.iceoton.canomcakeadmin.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.medel.User;

import java.util.ArrayList;

public class UserListAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<User> users;

    public UserListAdapter(Context mContext, ArrayList<User> users) {
        this.mContext = mContext;
        this.users = users;
    }

    static class ViewHolder {
        public TextView txtUserId;
        public TextView txtFullName;
        public ImageView imageActive;

    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(users.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_user, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtUserId = (TextView) convertView.findViewById(R.id.text_id);
            viewHolder.txtFullName = (TextView) convertView.findViewById(R.id.text_full_name);
            viewHolder.imageActive = (ImageView) convertView.findViewById(R.id.image_active);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        User user = users.get(position);
        viewHolder.txtUserId.setText(user.getId());
        viewHolder.txtFullName.setText(user.getFullname());
        if (user.isActive()) {
            viewHolder.imageActive.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
