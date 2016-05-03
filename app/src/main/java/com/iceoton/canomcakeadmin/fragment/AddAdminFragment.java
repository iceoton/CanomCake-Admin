package com.iceoton.canomcakeadmin.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.medel.response.RegisterAdminResponse;
import com.iceoton.canomcakeadmin.service.CanomCakeService;
import com.iceoton.canomcakeadmin.util.AppPreference;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AddAdminFragment extends Fragment {
    EditText etFullname, etEmail, etPassword;
    Button btnAdd;

    public AddAdminFragment() {
        // Required empty public constructor
    }

    public static AddAdminFragment newInstance(Bundle args) {
        AddAdminFragment fragment = new AddAdminFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_add_admin, container, false);
        initialView(rootView, savedInstanceState);
        return rootView;
    }

    private void initialView(View rootView, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.add_admin));
        etFullname = (EditText) rootView.findViewById(R.id.edit_fullname);
        etEmail = (EditText) rootView.findViewById(R.id.edit_email);
        etPassword = (EditText) rootView.findViewById(R.id.edit_password);
        btnAdd = (Button) rootView.findViewById(R.id.button_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputData()) {
                    addNewAdminToServer();
                }
            }
        });
    }

    private boolean checkInputData(){
        boolean flag = true;
        if(etEmail.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), "กรุณากรอกอีเมล", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if(etPassword.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), "กรุณากรอกรหัสผ่าน", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if(etFullname.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), "กรุณากรอกชื่อและนามสกุล", Toast.LENGTH_SHORT).show();
            flag = false;
        }

        return flag;
    }

    private void addNewAdminToServer(){
        JSONObject data = new JSONObject();
        try {
            data.put("email", etEmail.getText().toString());
            data.put("password", etPassword.getText().toString());
            data.put("fullname", etFullname.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AppPreference preference = new AppPreference(getActivity());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(preference.getApiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.registerAdmin("adminRegister", data.toString());
        call.enqueue(new Callback<RegisterAdminResponse>() {
            @Override
            public void onResponse(Call<RegisterAdminResponse> call, Response<RegisterAdminResponse> response) {
                RegisterAdminResponse registerAdminResponse = response.body();
                if(registerAdminResponse.getSuccessValue() == 1){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setIcon(R.drawable.ic_admin);
                    builder.setMessage("เพิ่มผู้ดูแลระบบเรียบร้อยแล้ว")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    getActivity().onBackPressed();
                                }
                            });
                    // Create the AlertDialog object
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                } else {
                    Toast.makeText(getActivity(), "ไม่สามารถเพิ่มผู้ดูแลระบบได้ อีเมลอาจซ้ำ", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<RegisterAdminResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

}
