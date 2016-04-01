package com.iceoton.canomcakeadmin.fragment;


import android.Manifest;
import android.app.Activity;
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

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.medel.Product;
import com.iceoton.canomcakeadmin.medel.response.UploadFileResponse;
import com.iceoton.canomcakeadmin.service.FileUploadService;
import com.iceoton.canomcakeadmin.service.ServiceGenerator;
import com.iceoton.canomcakeadmin.util.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddProductFragment extends Fragment {
    private static final int PICTURE_REQUEST_CODE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    ImageView ivPhoto;
    EditText etName, etPrice, etUnit, etDetail;
    Button btnSave;
    Spinner spinnerCategory;
    Uri imageUri;

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
        if (imageUri != null) {
            uploadFile(imageUri);
        }

    }

    private void uploadFile(Uri fileUri) {
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
        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);

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
