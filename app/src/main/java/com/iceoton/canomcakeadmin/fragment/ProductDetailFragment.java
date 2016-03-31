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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.medel.Product;
import com.iceoton.canomcakeadmin.medel.response.DeleteProductResponse;
import com.iceoton.canomcakeadmin.medel.response.GetProductByCodeResponse;
import com.iceoton.canomcakeadmin.service.CanomCakeService;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDetailFragment extends Fragment {
    ImageView ivPhoto;
    TextView txtName, txtDetail, txtPrice, txtUnit;
    Button btnEdit, btnDelete;

    public static ProductDetailFragment newInstance(Bundle args) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_detail, container, false);
        initialView(rootView, savedInstanceState);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initialView(View rootView, Bundle savedInstanceState) {
        Bundle args = getArguments();
        final String productCode = args.getString("product_code");
        ivPhoto = (ImageView) rootView.findViewById(R.id.image_photo);
        txtName = (TextView) rootView.findViewById(R.id.text_name);
        txtDetail = (TextView) rootView.findViewById(R.id.text_detail);
        txtPrice = (TextView) rootView.findViewById(R.id.text_price);
        txtUnit = (TextView) rootView.findViewById(R.id.text_unit);
        btnEdit = (Button) rootView.findViewById(R.id.button_edit);
        btnDelete = (Button) rootView.findViewById(R.id.button_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(productCode);
            }
        });

        loadProductByCode(productCode);
    }

    private void deleteProduct(final String productCode){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_bank_tranfer);
        builder.setMessage("ยืนยันการลบสินค้า")
                .setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendDeleteProductByCode(productCode);
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

    private void loadProductByCode(final String productCode) {
        JSONObject data = new JSONObject();
        try {
            data.put("code", productCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.loadProductByCode("getProductByCode", data.toString());
        call.enqueue(new Callback<GetProductByCodeResponse>() {
            @Override
            public void onResponse(Call<GetProductByCodeResponse> call, Response<GetProductByCodeResponse> response) {
                final Product product = response.body().getResult();
                if (product != null) {
                    txtName.setText(product.getNameThai());
                    getActivity().setTitle(product.getNameThai());
                    txtDetail.setText(product.getDetail());
                    txtPrice.setText(Double.toString(product.getPrice()));
                    txtUnit.setText("บาท/" + product.getUnit());


                    String imageUrl = getActivity().getResources().getString(R.string.api_url)
                            + product.getImageUrl();
                    Glide.with(getActivity()).load(imageUrl).into(ivPhoto);
                }
            }

            @Override
            public void onFailure(Call<GetProductByCodeResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

    private void sendDeleteProductByCode(final String productCode) {
        JSONObject data = new JSONObject();
        try {
            data.put("code", productCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.deleteProductByCode("deleteProduct", data.toString());
        call.enqueue(new Callback<DeleteProductResponse>() {
            @Override
            public void onResponse(Call<DeleteProductResponse> call, Response<DeleteProductResponse> response) {
                DeleteProductResponse deleteProductResponse = response.body();
                if (deleteProductResponse.getSuccessValue() == 1) {
                    Toast.makeText(getActivity(), "ลบสินค้าเรียบร้อย", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getActivity(), "ไม่สามารถลบสินค้าได้ลองใหม่อีกครั้ง", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteProductResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }


}
