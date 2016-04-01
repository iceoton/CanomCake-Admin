package com.iceoton.canomcakeadmin.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.activity.AdminActivity;
import com.iceoton.canomcakeadmin.adapter.AdminListAdapter;
import com.iceoton.canomcakeadmin.medel.Admin;
import com.iceoton.canomcakeadmin.medel.response.DeleteResponse;
import com.iceoton.canomcakeadmin.medel.response.GetAllAdminResponse;
import com.iceoton.canomcakeadmin.service.CanomCakeService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ManageAdminFragment extends Fragment {
    ListView listViewAdmin;
    FloatingActionButton fab;

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
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdminActivity.class);
                getActivity().startActivity(intent);
            }
        });
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
                    AdminListAdapter adminListAdapter = new AdminListAdapter(getActivity(), admins);
                    listViewAdmin.setAdapter(adminListAdapter);
                    listViewAdmin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    });
                    registerForContextMenu(listViewAdmin);
                }
            }

            @Override
            public void onFailure(Call<GetAllAdminResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.admin_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete_admin:
                deleteAdmin(info.id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteAdmin(final long adminId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_bank_tranfer);
        builder.setMessage("ต้องการลบใช่หรือไม่?")
                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendDeleteAdminToServer(adminId);
                    }
                })
                .setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // Create the AlertDialog object
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void sendDeleteAdminToServer(long id) {

        JSONObject data = new JSONObject();
        try {
            data.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DEBUG", "json = " + data.toString());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.deleteAdminById("deleteAdmin", data.toString());
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                DeleteResponse deleteResponse = response.body();
                if (deleteResponse.getSuccessValue() == 1) {
                    Toast.makeText(getActivity(), "ลบผู้ดูแลระบบเรียบร้อยแล้ว", Toast.LENGTH_LONG).show();
                    onResume();
                } else {
                    Toast.makeText(getActivity(), "ไม่สามารถลบได้ ลองใหม่อีกครั้ง", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }
}
