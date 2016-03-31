package com.iceoton.canomcakeadmin.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.activity.AddProductActivity;
import com.iceoton.canomcakeadmin.adapter.RecyclerViewAdapter;
import com.iceoton.canomcakeadmin.medel.Product;
import com.iceoton.canomcakeadmin.medel.response.GetAllProductResponse;
import com.iceoton.canomcakeadmin.medel.response.GetProductByCategoryResponse;
import com.iceoton.canomcakeadmin.service.CanomCakeService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductListFragment extends Fragment {
    Bundle bundle;
    RecyclerView recyclerView;
    int categoryId;
    FloatingActionButton fab;

    public static ProductListFragment newInstance(Bundle args) {
        ProductListFragment fragment = new ProductListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bundle = getArguments();
        categoryId = bundle.getInt("category_id");

        View rootView = inflater.inflate(R.layout.fragment_product_list, container, false);
        initialView(rootView);

        return rootView;
    }

    private void initialView(View rootView) {
        String categoryName = bundle.getString("category_name");
        getActivity().setTitle(categoryName);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddProductActivity.class);
                getActivity().startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProductInCategory(categoryId);
    }

    private void loadProductInCategory(int categoryId) {
        if (categoryId == 2) {
            showAllProduct();
            return;
        }

        JSONObject data = new JSONObject();
        try {
            data.put("category_id", categoryId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.loadProductByCategoryId("getProductsByCatagoryID", data.toString());
        call.enqueue(new Callback<GetProductByCategoryResponse>() {
            @Override
            public void onResponse(Call<GetProductByCategoryResponse> call,
                                   Response<GetProductByCategoryResponse> response) {
                ArrayList<Product> products = response.body().getResult();
                if (products != null) {
                    Log.d("DEBUG", "The number of products is " + products.size());
                    GridLayoutManager lLayout = new GridLayoutManager(getContext(), 2);
                    recyclerView.setLayoutManager(lLayout);
                    RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(getContext(), products);
                    recyclerView.setAdapter(rcAdapter);
                }
            }

            @Override
            public void onFailure(Call<GetProductByCategoryResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

    private void showAllProduct() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.loadAllProduct("getAllProduct");
        call.enqueue(new Callback<GetAllProductResponse>() {
            @Override
            public void onResponse(Call<GetAllProductResponse> call, Response<GetAllProductResponse> response) {
                ArrayList<Product> products = response.body().getResult();
                if (products != null) {
                    Log.d("DEBUG", "The number of products is " + products.size());
                    GridLayoutManager lLayout = new GridLayoutManager(getContext(), 2);
                    recyclerView.setLayoutManager(lLayout);
                    RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(getContext(), products);
                    recyclerView.setAdapter(rcAdapter);
                }
            }

            @Override
            public void onFailure(Call<GetAllProductResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

}
