package com.iceoton.canomcakeadmin.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.medel.Dashboard;
import com.iceoton.canomcakeadmin.medel.response.GetDashboardResponse;
import com.iceoton.canomcakeadmin.service.CanomCakeService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {
    TextView txtMontCirculation, txtTotalWaitingOrder, txtTotalTransfer,
            txtBestSeller, txtCountProduct, txtCountCategory, txtCountMember, txtCountAdmin;
    private SwipeRefreshLayout swipeContainer;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(Bundle args) {
        HomeFragment fragment = new HomeFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initialView(rootView, savedInstanceState);
        return rootView;
    }

    private void initialView(View rootView, Bundle savedInstanceState) {
        txtMontCirculation = (TextView) rootView.findViewById(R.id.text_mont_circulation);
        txtTotalWaitingOrder = (TextView) rootView.findViewById(R.id.text_total_waiting);
        txtTotalTransfer = (TextView) rootView.findViewById(R.id.text_total_transfer);
        txtBestSeller = (TextView) rootView.findViewById(R.id.text_best_seller);
        txtCountProduct = (TextView) rootView.findViewById(R.id.text_count_product);
        txtCountCategory = (TextView) rootView.findViewById(R.id.text_count_category);
        txtCountMember = (TextView) rootView.findViewById(R.id.text_count_member);
        txtCountAdmin = (TextView) rootView.findViewById(R.id.text_count_admin);

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDashboardFromServer();
            }
        });


        loadDashboardFromServer();
    }

    private void loadDashboardFromServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.loadDashboard("getDashboard");
        call.enqueue(new Callback<GetDashboardResponse>() {
            @Override
            public void onResponse(Call<GetDashboardResponse> call, Response<GetDashboardResponse> response) {
                Dashboard dashboard = response.body().getResult();
                if (dashboard != null) {
                    txtMontCirculation.setText("฿ " + dashboard.getMontCirculation());
                    txtTotalWaitingOrder.setText(String.valueOf(dashboard.getNumberOfWaitingOrder()));
                    txtTotalTransfer.setText(String.valueOf(dashboard.getNumberOfTransactions()));
                    txtBestSeller.setText(dashboard.getBestSeller().getNameThai());
                    txtCountProduct.setText(String.valueOf(dashboard.getNumberOfProducts()));
                    txtCountCategory.setText(String.valueOf(dashboard.getNumberOfCategories()));
                    txtCountMember.setText(String.valueOf(dashboard.getNumberOfCustomers()));
                    txtCountAdmin.setText(String.valueOf(dashboard.getNumberOfAdmin()));

                    swipeContainer.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<GetDashboardResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

}
