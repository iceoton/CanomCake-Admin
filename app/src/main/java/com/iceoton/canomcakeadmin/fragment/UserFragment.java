package com.iceoton.canomcakeadmin.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.medel.User;
import com.iceoton.canomcakeadmin.medel.response.DeleteResponse;
import com.iceoton.canomcakeadmin.service.CanomCakeService;
import com.iceoton.canomcakeadmin.util.AppPreference;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UserFragment extends Fragment {
    User user;
    EditText etName, etSurname, etMobile, etEmail, etAddress;
    Button btnDelete;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(Bundle args) {
        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = Parcels.unwrap(getArguments().getParcelable("user_id"));
        }

        // to hide soft keyboard when fragment started.
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        initialView(rootView, savedInstanceState);
        showUserInfo();
        return rootView;
    }

    private void initialView(View rootView, Bundle savedInstanceState) {
        etName = (EditText) rootView.findViewById(R.id.edit_name);
        etSurname = (EditText) rootView.findViewById(R.id.edit_surname);
        etMobile = (EditText) rootView.findViewById(R.id.edit_mobile);
        etEmail = (EditText) rootView.findViewById(R.id.edit_email);
        etAddress = (EditText) rootView.findViewById(R.id.edit_address);
        btnDelete = (Button) rootView.findViewById(R.id.button_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.drawable.ic_bank_tranfer);
                builder.setMessage("ต้องการลบผู้ใช้ใช่หรือไม่?")
                        .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sendDeleteUserToServer();
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
        });
    }

    private void showUserInfo() {
        int cutIndex = user.getFullname().indexOf(",");
        String name = user.getFullname().substring(0, cutIndex);
        String surname = user.getFullname().substring(cutIndex + 1, user.getFullname().length());
        etName.setText(name);
        etSurname.setText(surname);
        etMobile.setText(user.getPhoneNumber());
        etEmail.setText(user.getEmail());
        etAddress.setText(user.getAddress());
    }

    private void sendDeleteUserToServer() {
        JSONObject data = new JSONObject();
        try {
            data.put("id", user.getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DEBUG", "json = " + data.toString());

        AppPreference preference = new AppPreference(getActivity());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(preference.getApiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.deleteCustomerById("deleteCustomer", data.toString());
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                DeleteResponse deleteResponse = response.body();
                if (deleteResponse.getSuccessValue() == 1) {
                    Toast.makeText(getActivity(), "ลบผู้ใช้งานเรียบร้อยแล้ว", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
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
