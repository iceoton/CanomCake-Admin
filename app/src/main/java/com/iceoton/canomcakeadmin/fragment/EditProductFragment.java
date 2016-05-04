package com.iceoton.canomcakeadmin.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.medel.Product;
import com.iceoton.canomcakeadmin.medel.response.EditProductResponse;
import com.iceoton.canomcakeadmin.medel.response.UploadFileResponse;
import com.iceoton.canomcakeadmin.service.CanomCakeService;
import com.iceoton.canomcakeadmin.service.FileUploadService;
import com.iceoton.canomcakeadmin.service.ServiceGenerator;
import com.iceoton.canomcakeadmin.util.AppPreference;
import com.iceoton.canomcakeadmin.util.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EditProductFragment extends Fragment {
    private static final int PICTURE_REQUEST_CODE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    ImageView ivPhoto;
    EditText etName, etPrice, etUnit, etDetail, etProductInStock;
    Button btnSave;
    Spinner spinnerCategory;
    Uri imageUri;
    Product defaultProduct;
    ProgressDialog progressBar;

    public EditProductFragment() {
        // Required empty public constructor
    }

    public static EditProductFragment newInstance(Bundle args) {
        EditProductFragment fragment = new EditProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            defaultProduct = Parcels.unwrap(getArguments().getParcelable("product"));
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
        setupProduct();
        return rootView;
    }

    private void initialView(View rootView, Bundle savedInstanceState) {
        ivPhoto = (ImageView) rootView.findViewById(R.id.image_photo);
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,
                        "Select app to pick picture"), PICTURE_REQUEST_CODE);
            }
        });

        etName = (EditText) rootView.findViewById(R.id.edit_name);
        etPrice = (EditText) rootView.findViewById(R.id.edit_price);
        etUnit = (EditText) rootView.findViewById(R.id.edit_unit);
        etProductInStock = (EditText) rootView.findViewById(R.id.edit_in_stock);
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
                Product product = checkInputData();
                if (product != null) {
                    progressBar.show();
                    editProduct(product);
                }
            }
        });
        etProductInStock = (EditText) rootView.findViewById(R.id.edit_in_stock);

        progressBar = new ProgressDialog(getActivity());
        progressBar.setCancelable(false);
        progressBar.setMessage("กำลังทำงาน...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void setupProduct() {
        etName.setText(defaultProduct.getNameThai());
        etPrice.setText(String.valueOf(defaultProduct.getPrice()));
        etUnit.setText(defaultProduct.getUnit());
        etDetail.setText(defaultProduct.getDetail());
        etProductInStock.setText(String.valueOf(defaultProduct.getAvailable()));
        if (defaultProduct.getCategoryId() == 3) {
            spinnerCategory.setSelection(0);
        } else {
            spinnerCategory.setSelection(1);
        }
        AppPreference preference = new AppPreference(getActivity());
        String imageUrl = preference.getApiUrl()
                + defaultProduct.getImageUrl();
        Glide.with(getActivity()).load(imageUrl).into(ivPhoto);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == PICTURE_REQUEST_CODE) && (resultCode == Activity.RESULT_OK)) {
            imageUri = data.getData();
            Log.d("DEBUG", "select image = " + imageUri);
            Bitmap imageBitmap;
            InputStream imageInputStream;
            try {
                imageInputStream = getActivity().getContentResolver().openInputStream(imageUri);
                imageBitmap = BitmapFactory.decodeStream(imageInputStream);
                Bitmap resized = Bitmap.createScaledBitmap(imageBitmap, 256, 256, true);
                ivPhoto.setImageBitmap(resized);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private Product checkInputData() {
        int selectedCategory = spinnerCategory.getSelectedItemPosition();
        if (selectedCategory == 0) {
            defaultProduct.setCategoryId("3");
        } else {
            defaultProduct.setCategoryId("4");
        }
        String productName = etName.getText().toString();
        if (productName.trim().equals("")) {
            Toast.makeText(getActivity(), "กรุณาใส่ชื่อสินค้า", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            defaultProduct.setNameThai(productName);
        }
        defaultProduct.setNameEnglish("");

        String strPrice = etPrice.getText().toString();
        if (strPrice.equals("")) {
            Toast.makeText(getActivity(), "กรุณาใส่ราคาสินค้า", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            defaultProduct.setPrice(strPrice);
        }

        String unit = etUnit.getText().toString();
        if (unit.trim().equals("")) {
            Toast.makeText(getActivity(), "กรุณาใส่หน่วยสินค้า", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            defaultProduct.setUnit(unit);
        }

        String productInStock = etProductInStock.getText().toString();
        if (unit.trim().equals("")) {
            Toast.makeText(getActivity(), "กรุณาใส่จำนวนสินค้า", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            try {
                defaultProduct.setAvailable(Integer.parseInt(productInStock));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        defaultProduct.setDetail(etDetail.getText().toString());

        return defaultProduct;
    }

    private void editProduct(Product product) {
        if (imageUri != null) {
            //When the product photo has been changed.
            uploadImageAndEditProduct(imageUri, product);
        } else {
            // When the product photo is not changed
            sendEditProductToServer(product);
        }
    }

    private void sendEditProductToServer(Product product) {
        JSONObject data = new JSONObject();
        try {
            data.put("code", product.getCode());
            data.put("category_id", product.getCategoryId());
            data.put("name_th", product.getNameThai());
            data.put("name_en", product.getNameEnglish());
            data.put("detail", product.getDetail());
            data.put("price", product.getPrice());
            data.put("unit", product.getUnit());
            data.put("available", product.getAvailable());
            data.put("image", product.getImageUrl());
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
        Call call = canomCakeService.editProduct("editProduct", data.toString());
        call.enqueue(new Callback<EditProductResponse>() {
            @Override
            public void onResponse(Call<EditProductResponse> call, Response<EditProductResponse> response) {
                progressBar.dismiss();
                EditProductResponse editProductResponse = response.body();
                if (editProductResponse.getSuccessValue() == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setIcon(R.drawable.ic_bank_tranfer);
                    builder.setMessage("แก้ไขสินค้าเรียบร้อยแล้ว")
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

                }
            }

            @Override
            public void onFailure(Call<EditProductResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });

    }

    private void uploadImageAndEditProduct(Uri fileUri, final Product product) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant
        }

        // create upload service client
        AppPreference preference = new AppPreference(getActivity());
        FileUploadService service =
                ServiceGenerator.createService(preference.getApiUrl(), FileUploadService.class);

        // use the FileUtils to get the actual file by uri
        //File file = FileUtils.getFile(this, fileUri);
        File file = new File(FileUtils.getPath(getActivity(), fileUri));
        Log.d("DEBUG", "image path: " + file.getAbsolutePath());

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // finally, execute the request
        Call<UploadFileResponse> call = service.upload(requestFile);
        call.enqueue(new Callback<UploadFileResponse>() {
            @Override
            public void onResponse(Call<UploadFileResponse> call, Response<UploadFileResponse> response) {
                if (response.body().getSuccessValue() == 1) {
                    Log.d("DEBUG", "upload success: " + response.body().getImageUrl());
                    product.setImageUrl(response.body().getImageUrl());
                    sendEditProductToServer(product);
                } else {
                    Log.d("DEBUG", "upload error:" + response.body().getErrorMessage());
                }
            }

            @Override
            public void onFailure(Call<UploadFileResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

}
