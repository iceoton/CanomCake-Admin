package com.iceoton.canomcakeadmin.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.activity.OrdersActivity;


public class ManageOrdersFragment extends Fragment {
    CardView cardBankTransfer, cardWaiting, cardPaid,
            cardDelivering, cardDelivered, cardCancel;

    public ManageOrdersFragment() {
        // Required empty public constructor
    }

    public static ManageOrdersFragment newInstance(Bundle args) {
        ManageOrdersFragment fragment = new ManageOrdersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_orders, container, false);
        initialView(rootView, savedInstanceState);
        return rootView;
    }

    private void initialView(View rootView, Bundle savedInstanceState) {
        cardBankTransfer = (CardView) rootView.findViewById(R.id.card_bank_transfer);
        cardBankTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cardWaiting = (CardView) rootView.findViewById(R.id.card_waiting);
        cardWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrdersPageByTag("getWaitingOrders");
            }
        });
        cardPaid = (CardView) rootView.findViewById(R.id.card_paid);
        cardPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrdersPageByTag("getPaidOrders");
            }
        });
        cardDelivering = (CardView) rootView.findViewById(R.id.card_delivering);
        cardDelivering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrdersPageByTag("getDeliveringOrders");
            }
        });
        cardDelivered = (CardView) rootView.findViewById(R.id.card_delivered);
        cardDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrdersPageByTag("getDeliveredOrders");
            }
        });
        cardCancel = (CardView) rootView.findViewById(R.id.card_cancel);
        cardCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrdersPageByTag("getCanceledOrders");
            }
        });

    }

    private void openOrdersPageByTag(String tag){
        Intent intent = new Intent(getActivity(), OrdersActivity.class);
        intent.putExtra("tag",tag);
        getActivity().startActivity(intent);
    }

}
