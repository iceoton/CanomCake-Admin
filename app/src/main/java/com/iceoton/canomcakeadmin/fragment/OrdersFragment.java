package com.iceoton.canomcakeadmin.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iceoton.canomcakeadmin.R;


public class OrdersFragment extends Fragment {
    String requestTag;

    public OrdersFragment() {
        // Required empty public constructor
    }

    public static OrdersFragment newInstance(Bundle args) {
        OrdersFragment fragment = new OrdersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            requestTag = getArguments().getString("tag");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_orders, container, false);
        initialView(rootView, savedInstanceState);
        return rootView;
    }

    private void initialView(View rootView, Bundle savedInstanceState) {

    }

}
