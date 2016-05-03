package com.iceoton.canomcakeadmin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.util.AppPreference;

public class SetIpFragment extends Fragment {
    EditText etUrl;
    Button btnSave;

    public SetIpFragment() {
        // Required empty public constructor
    }

    public static SetIpFragment newInstance() {
        SetIpFragment fragment = new SetIpFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_set_ip, container, false);
        initialView(rootView);

        return rootView;
    }


    private void initialView(View rootView) {
        final AppPreference preference = new AppPreference(getActivity());
        etUrl = (EditText)rootView.findViewById(R.id.edit_url);
        etUrl.setText(preference.getApiUrl());
        btnSave = (Button) rootView.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = etUrl.getText().toString().trim();
                preference.saveApiUrl(url);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
