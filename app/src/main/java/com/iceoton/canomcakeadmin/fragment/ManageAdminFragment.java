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
import com.iceoton.canomcakeadmin.adapter.AdminListAdapter;
import com.iceoton.canomcakeadmin.medel.Admin;
import com.iceoton.canomcakeadmin.medel.response.GetAllAdminResponse;
import com.iceoton.canomcakeadmin.service.CanomCakeService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ManageAdminFragment extends Fragment {
    ListView listViewAdmin;

    public ManageAdminFragment() {
        // Required empty public constructor
    }

    public static ManageAdminFragment newInstance(Bundle args) {
        ManageAdminFragment fragment = new ManageAdminFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_manage_admin, container, false);
        initialView(rootView, savedInstanceState);
        return rootView;
    }

    private void initialView(View rootView, Bundle savedInstanceState) {
        listViewAdmin = (ListView) rootView.findViewById(R.id.list_admin);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAllAdminFromServer();
    }

    private void loadAllAdminFromServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.loadAllAdmin("getAllAdmins");
        call.enqueue(new Callback<GetAllAdminResponse>() {
            @Override
            public void onResponse(Call<GetAllAdminResponse> call, Response<GetAllAdminResponse> response) {
                ArrayList<Admin> admins = response.body().getResult();
                if (admins != null) {
                    Log.d("DEBUG", "Number of admin: " + admins.size());
                    AdminListAdapter adminListAdapter = new AdminListAdapter(getActivity(),admins);
                    listViewAdmin.setAdapter(adminListAdapter);
                    listViewAdmin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetAllAdminResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

}
