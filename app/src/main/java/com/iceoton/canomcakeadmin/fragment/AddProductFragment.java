package com.iceoton.canomcakeadmin.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.medel.Product;

import org.json.JSONException;
import org.json.JSONObject;


public class AddProductFragment extends Fragment {
    ImageView ivPhoto;
    EditText etName, etPrice, etUnit, etDetail;
    Button btnSave;
    Spinner spinnerCategory;

    public AddProductFragment() {
        // Required empty public constructor
    }

    public static AddProductFragment newInstance(Bundle args) {
        AddProductFragment fragment = new AddProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        // to hide soft keyboard when fragment started.
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_product, container, false);
        initialView(rootView, savedInstanceState);
        return rootView;
    }

    private void initialView(View rootView, Bundle savedInstanceState) {
        ivPhoto = (ImageView) rootView.findViewById(R.id.image_photo);
        etName = (EditText) rootView.findViewById(R.id.edit_name);
        etPrice = (EditText) rootView.findViewById(R.id.edit_price);
        etUnit = (EditText) rootView.findViewById(R.id.edit_unit);
        etDetail = (EditText) rootView.findViewById(R.id.edit_detail);
        spinnerCategory = (Spinner) rootView.findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        btnSave = (Button) rootView.findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = new Product();
                int selectedCategory = spinnerCategory.getSelectedItemPosition();
                if (selectedCategory == 0) {
                    product.setCategoryId("3");
                } else {
                    product.setCategoryId("4");
                }

                product.setNameThai(etName.getText().toString());
                product.setNameEnglish("");
                product.setPrice(etPrice.getText().toString());
                product.setUnit(etUnit.getText().toString());
                product.setDetail(etDetail.getText().toString());
                product.setImageUrl("uploaded/c000.jpg");
                addProductToServer(product);
            }
        });
    }

    private void addProductToServer(Product product) {
        JSONObject data = new JSONObject();
        try {
            data.put("category_id", product.getCategoryId());
            data.put("name_th", product.getNameThai());
            data.put("name_en", product.getNameEnglish());
            data.put("detail", product.getDetail());
            data.put("price", product.getPrice());
            data.put("unit", product.getUnit());
            data.put("image", product.getImageUrl());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("DEBUG", "json = " + data.toString());
    }

}
