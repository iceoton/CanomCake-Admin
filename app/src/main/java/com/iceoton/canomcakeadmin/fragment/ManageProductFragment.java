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
import com.iceoton.canomcakeadmin.adapter.CategoryListAdapter;
import com.iceoton.canomcakeadmin.medel.Category;
import com.iceoton.canomcakeadmin.medel.response.GetAllCategoryResponse;
import com.iceoton.canomcakeadmin.service.CanomCakeService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ManageProductFragment extends Fragment {
    private ListView listViewCategory;

    public ManageProductFragment() {
        // Required empty public constructor
    }

    public static ManageProductFragment newInstance(Bundle args) {
        ManageProductFragment fragment = new ManageProductFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_product, container, false);
        initialView(rootView, savedInstanceState);
        return rootView;
    }

    private void initialView(View rootView, Bundle savedInstanceState) {
        listViewCategory = (ListView) rootView.findViewById(R.id.list_category);

        loadCategories();
    }

    private void loadCategories() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.loadCategories("getAllCategory");
        call.enqueue(new Callback<GetAllCategoryResponse>() {
            @Override
            public void onResponse(Call<GetAllCategoryResponse> call, Response<GetAllCategoryResponse> response) {
                Log.d("DEBUG", "Number of category = " + response.body().getResult().size());
                final ArrayList<Category> categories = response.body().getResult();
                if (categories != null) {
                    CategoryListAdapter categoryListAdapter = new CategoryListAdapter(getActivity(), categories);
                    listViewCategory.setAdapter(categoryListAdapter);
                    listViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            showProductByCategory(categories.get(position));
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetAllCategoryResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

    public void showProductByCategory(Category category) {
        Log.d("DEBUG", "Show product in category id = " + category.getId());
    }

}

