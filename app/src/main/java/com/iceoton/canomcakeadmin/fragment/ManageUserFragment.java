package com.iceoton.canomcakeadmin.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.activity.UserActivity;
import com.iceoton.canomcakeadmin.adapter.UserListAdapter;
import com.iceoton.canomcakeadmin.medel.User;
import com.iceoton.canomcakeadmin.medel.response.GetAllCustomerResponse;
import com.iceoton.canomcakeadmin.service.CanomCakeService;

import org.parceler.Parcels;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ManageUserFragment extends Fragment {
    ListView listViewUser;

    public ManageUserFragment() {
        // Required empty public constructor
    }

    public static ManageUserFragment newInstance(Bundle args) {
        ManageUserFragment fragment = new ManageUserFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_members, container, false);
        initialView(rootView, savedInstanceState);
        return rootView;
    }

    private void initialView(View rootView, Bundle savedInstanceState) {
        listViewUser = (ListView) rootView.findViewById(R.id.list_user);

    }

    @Override
    public void onResume() {
        super.onResume();
        loadAllUserFromServer();
    }

    private void loadAllUserFromServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.loadAllCustomer("getAllCustomers");
        call.enqueue(new Callback<GetAllCustomerResponse>() {
            @Override
            public void onResponse(Call<GetAllCustomerResponse> call, Response<GetAllCustomerResponse> response) {
                final ArrayList<User> users = response.body().getResult();
                if (users != null) {
                    Log.d("DEBUG", "Number of users: " + users.size());
                    UserListAdapter userListAdapter = new UserListAdapter(getActivity(), users);
                    listViewUser.setAdapter(userListAdapter);
                    listViewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), UserActivity.class);
                            intent.putExtra("user_id", Parcels.wrap(users.get(position)));
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetAllCustomerResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }


}
