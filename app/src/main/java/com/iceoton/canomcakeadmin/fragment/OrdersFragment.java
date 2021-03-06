package com.iceoton.canomcakeadmin.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.activity.OrdersActivity;
import com.iceoton.canomcakeadmin.adapter.OrdersListAdapter;
import com.iceoton.canomcakeadmin.medel.Order;
import com.iceoton.canomcakeadmin.medel.response.GetOrdersResponse;
import com.iceoton.canomcakeadmin.service.CanomCakeService;
import com.iceoton.canomcakeadmin.util.AppPreference;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class OrdersFragment extends Fragment {
    String requestTag;
    ListView lvOrders;

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
        if (requestTag.equals("getWaitingOrders")) {
            getActivity().setTitle("รายการสั่งซื้อที่รอการชำระเงิน");
        } else if (requestTag.equals("getPaidOrders")) {
            getActivity().setTitle("รายการสั่งซื้อทชำระเงินแล้ว");
        } else if (requestTag.equals("getDeliveringOrders")) {
            getActivity().setTitle("รายการสั่งซื้อที่กำลังจัดส่ง");
        } else if (requestTag.equals("getDeliveredOrders")) {
            getActivity().setTitle("รายการสั่งซื้อที่จัดส่งแล้ว");
        } else if (requestTag.equals("getCanceledOrders")) {
            getActivity().setTitle("รายการสั่งซื้อที่มีการยกเลิก");
        }

        lvOrders = (ListView) rootView.findViewById(R.id.list_orders);
        loadOrdersFromServer();
    }

    private void loadOrdersFromServer() {
        AppPreference preference = new AppPreference(getActivity());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(preference.getApiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.loadOrdersByTag(requestTag);
        call.enqueue(new Callback<GetOrdersResponse>() {
            @Override
            public void onResponse(Call<GetOrdersResponse> call, Response<GetOrdersResponse> response) {
                ArrayList<Order> orders = response.body().getResult();
                if (orders != null) {
                    Log.d("DEBUG", "Number of orders: " + orders.size());
                    OrdersListAdapter ordersListAdapter = new OrdersListAdapter(getActivity(), orders);
                    lvOrders.setAdapter(ordersListAdapter);
                    lvOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            showOrderDetail((int) id);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetOrdersResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

    private void showOrderDetail(int orderId) {
        Bundle args = new Bundle();
        args.putInt("order_id", orderId);
        ((OrdersActivity) getActivity()).placeFragmentToContrainer(OrderDetailFragment.newInstance(args));
    }

}
