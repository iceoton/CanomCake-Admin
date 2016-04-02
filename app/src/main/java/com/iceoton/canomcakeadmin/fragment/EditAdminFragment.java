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
import com.iceoton.canomcakeadmin.medel.Admin;
import com.iceoton.canomcakeadmin.medel.response.EditAdminResponse;
import com.iceoton.canomcakeadmin.service.CanomCakeService;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EditAdminFragment extends Fragment {
    EditText etFullname, etEmail, etPassword, etConfirmPassword;
    Button btnAdd;
    Admin admin;

    public EditAdminFragment() {
        // Required empty public constructor
    }

    public static EditAdminFragment newInstance(Bundle args) {
        EditAdminFragment fragment = new EditAdminFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            admin = Parcels.unwrap(getArguments().getParcelable("admin"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_admin, container, false);
        initialView(rootView, savedInstanceState);
        setupAdminInfo();
        return rootView;
    }

    private void setupAdminInfo() {
        etFullname.setText(admin.getFullname().replace(",", "  "));
        etEmail.setText(admin.getEmail());
    }

    private void initialView(View rootView, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.edit_admin));
        etFullname = (EditText) rootView.findViewById(R.id.edit_fullname);
        etEmail = (EditText) rootView.findViewById(R.id.edit_email);
        etPassword = (EditText) rootView.findViewById(R.id.edit_password);
        etConfirmPassword = (EditText) rootView.findViewById(R.id.edit_confirm_password);
        btnAdd = (Button) rootView.findViewById(R.id.button_save);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputData()) {
                    sendEditAdminToServer();
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
        } else if(!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())){
            Toast.makeText(getActivity(), "รหัสผ่านกับยืนยันรหัสผ่านไม่ตรงกัน", Toast.LENGTH_SHORT).show();
            flag = false;
        }

        return flag;
    }

    private void sendEditAdminToServer(){
        JSONObject data = new JSONObject();
        try {
            data.put("id", admin.getId());
            data.put("email", etEmail.getText().toString());
            data.put("password", etPassword.getText().toString());
            data.put("fullname", etFullname.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DEBUG", "json = " + data.toString());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.editAdmin("editAdmin", data.toString());
        call.enqueue(new Callback<EditAdminResponse>() {
            @Override
            public void onResponse(Call<EditAdminResponse> call, Response<EditAdminResponse> response) {
                EditAdminResponse editAdminResponse = response.body();
                if(editAdminResponse.getSuccessValue() == 1){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setIcon(R.drawable.ic_admin);
                    builder.setMessage("แก้ไขข้อมูลเรียบร้อยแล้ว")
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
                    Toast.makeText(getActivity(), "อีเมลซ้ำ ลองใหม่อีกครั้ง", Toast.LENGTH_LONG).show();
                    Log.d("DEBUG", "error message: " + editAdminResponse.getErrorMessage());
                }

            }

            @Override
            public void onFailure(Call<EditAdminResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

}
