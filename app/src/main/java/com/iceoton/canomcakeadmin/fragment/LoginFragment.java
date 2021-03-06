package com.iceoton.canomcakeadmin.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.activity.MainActivity;
import com.iceoton.canomcakeadmin.activity.SetIpActivity;
import com.iceoton.canomcakeadmin.medel.Admin;
import com.iceoton.canomcakeadmin.medel.response.LoginAdminResponse;
import com.iceoton.canomcakeadmin.service.CanomCakeService;
import com.iceoton.canomcakeadmin.service.ServiceGenerator;
import com.iceoton.canomcakeadmin.util.AppPreference;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    EditText etUsername, etPassword;
    Button btnLogin, btnForgetPassword;
    ImageView imgLogo;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initialView(rootView);

        return rootView;
    }

    private void initialView(View rootView) {
        etUsername = (EditText) rootView.findViewById(R.id.username);
        etPassword = (EditText) rootView.findViewById(R.id.password);
        btnLogin = (Button) rootView.findViewById(R.id.button_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginToServer(etUsername.getText().toString().trim()
                        , etPassword.getText().toString());
            }
        });
        btnForgetPassword = (Button) rootView.findViewById(R.id.button_forget_password);
        btnForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentContainer, ForgetPasswordFragment.newInstance(null))
                        .addToBackStack(null)
                        .commit();
            }
        });

        imgLogo = (ImageView) rootView.findViewById(R.id.image_logo);
        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetIpActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loginToServer(String username, String password) {
        JSONObject data = new JSONObject();
        try {
            data.put("username", username);
            data.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CanomCakeService canomCakeService = ServiceGenerator.createService(CanomCakeService.class);
        Call call = canomCakeService.loginAdminToserver("adminLogin", data.toString());
        call.enqueue(new Callback<LoginAdminResponse>() {
            @Override
            public void onResponse(Call<LoginAdminResponse> call, Response<LoginAdminResponse> response) {
                Admin admin = response.body().getResult();
                if (admin != null) {
                    Log.d("DEBUG", "id = " + admin.getId());
                    AppPreference appPreference = new AppPreference(getActivity());
                    appPreference.saveUserId(admin.getId());
                    appPreference.saveUserName(admin.getEmail());
                    appPreference.saveFullName(admin.getFullname());
                    appPreference.saveLoginStatus(true);

                    startMainActivity();
                } else {
                    Log.d("DEBUG","Login error: " + response.body().getErrorMessage());
                    Toast.makeText(getActivity(), "ไม่พบอีเมลนี้หรือรหัสผ่านไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginAdminResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }


    private void startMainActivity() {
        Intent intentToMain = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(intentToMain);
        getActivity().finish();
    }

}
