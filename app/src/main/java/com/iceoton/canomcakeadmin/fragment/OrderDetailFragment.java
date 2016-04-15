package com.iceoton.canomcakeadmin.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.adapter.OrderDetailListAdapter;
import com.iceoton.canomcakeadmin.medel.OrderDetail;
import com.iceoton.canomcakeadmin.medel.User;
import com.iceoton.canomcakeadmin.medel.response.GetCustomerResponse;
import com.iceoton.canomcakeadmin.medel.response.GetOrderByIdResponse;
import com.iceoton.canomcakeadmin.medel.response.SetOrderStatusResponse;
import com.iceoton.canomcakeadmin.service.CanomCakeService;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderDetailFragment extends Fragment {
    FrameLayout containerLayout;
    ListView listViewDetail;
    TextView txtOrderId, txtOrderTime, txtStatus, txtTotalPrice;
    ImageView imageStatus;
    TextView txtCustomerInfo;
    int orderId;

    public OrderDetailFragment() {
        // Required empty public constructor
    }

    public static OrderDetailFragment newInstance(Bundle args) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            orderId = getArguments().getInt("order_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order_detail, container, false);
        initialView(rootView, savedInstanceState);

        return rootView;
    }

    private void initialView(View rootView, Bundle savedInstanceState) {
        getActivity().setTitle("คำสั่งซื้อ​ #" + orderId);
        containerLayout = (FrameLayout) rootView.findViewById(R.id.containerLayout);
        listViewDetail = (ListView) rootView.findViewById(R.id.list_detail);
        View headerView = getLayoutInflater(savedInstanceState).inflate(R.layout.header_order_detail, null, false);
        txtOrderId = (TextView) headerView.findViewById(R.id.text_order_id);
        txtOrderTime = (TextView) headerView.findViewById(R.id.text_date);
        txtStatus = (TextView) headerView.findViewById(R.id.text_status);
        imageStatus = (ImageView) headerView.findViewById(R.id.image_status);
        listViewDetail.addHeaderView(headerView);

        View footerView = getLayoutInflater(savedInstanceState).inflate(R.layout.footer_order_detail, null, false);
        txtTotalPrice = (TextView) footerView.findViewById(R.id.text_total_price);
        listViewDetail.addFooterView(footerView);

        txtCustomerInfo = (TextView) rootView.findViewById(R.id.text_customer_info);



        loadOrderByIdFromServer(orderId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.order_option_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_paid:
                setPaidOrderById(orderId);
                return true;
            case R.id.set_derivering:
                setDeriveringOrderById(orderId);
                return true;
            case R.id.set_derivered:
                setDeriveredOrderById(orderId);
                return true;
            case R.id.set_cancel:
                setCancelOrderById(orderId);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadOrderByIdFromServer(int orderId) {
        JSONObject data = new JSONObject();
        try {
            data.put("id", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getContext().getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.loadOrderByOrderId("getOrderByOrderId", data.toString());
        call.enqueue(new Callback<GetOrderByIdResponse>() {
            @Override
            public void onResponse(Call<GetOrderByIdResponse> call, Response<GetOrderByIdResponse> response) {
                if (response.body().getResult().size() > 0) {
                    OrderDetail orderDetail = response.body().getResult().get(0);
                    OrderDetailListAdapter orderDetailListAdapter
                            = new OrderDetailListAdapter(getActivity(), orderDetail.getOrderDetailItem());
                    listViewDetail.setAdapter(orderDetailListAdapter);
                    updateHeaderView(orderDetail);
                    updateFooterView(orderDetail);
                }
            }

            @Override
            public void onFailure(Call<GetOrderByIdResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });

    }

    private void loadCustomerInfoFromServer(int customerId){
        JSONObject data = new JSONObject();
        try {
            data.put("customer_id", customerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.loadCustomerByCustomerId("getCustomerByCustomerId", data.toString());
        call.enqueue(new Callback<GetCustomerResponse>() {
            @Override
            public void onResponse(Call<GetCustomerResponse> call, Response<GetCustomerResponse> response) {
                User user = response.body().getResult();
                if (user != null) {
                    showUserInfo(user);
                }
            }

            @Override
            public void onFailure(Call<GetCustomerResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

    private void showUserInfo(final User user) {
        String customerInfo = "ชื่อ: " + user.getFullname().replace(",","  ");;
        customerInfo += "\nเบอร์โทร: " + user.getPhoneNumber() + " อีเมล: " + user.getEmail();
        customerInfo += "\nที่อยู่: " + user.getAddress();
        txtCustomerInfo.setText(customerInfo);
    }

    private void updateHeaderView(OrderDetail orderDetail) {
        txtOrderId.setText("#" + String.valueOf(orderDetail.getId()));
        txtOrderTime.setText(orderDetail.getOrderTime());
        if(orderDetail.getStatus().equalsIgnoreCase("DELIVERED")){
            txtStatus.setText(R.string.order_status_delivered);
            imageStatus.setImageResource(R.drawable.icon_delivered);
        } else if(orderDetail.getStatus().equalsIgnoreCase("WAITING")){
            txtStatus.setText(R.string.order_status_waiting);
            imageStatus.setImageResource(R.drawable.icon_waiting);
        } else if(orderDetail.getStatus().equalsIgnoreCase("DELIVERING")){
            txtStatus.setText(R.string.order_status_delivering);
            imageStatus.setImageResource(R.drawable.icon_delivering);
        } else if(orderDetail.getStatus().equalsIgnoreCase("PAID")){
            txtStatus.setText(R.string.order_status_paid);
            imageStatus.setImageResource(R.drawable.icon_packaging);
        } else if(orderDetail.getStatus().equalsIgnoreCase("CANCELED")){
            txtStatus.setText(R.string.order_status_canceled);
            imageStatus.setImageResource(R.drawable.icon_cancel);
        }

        loadCustomerInfoFromServer(orderDetail.getCustomerId());

    }

    private void updateFooterView(OrderDetail orderDetail){
        txtTotalPrice.setText(String.valueOf(orderDetail.getTotalPrice()) + " บาท");
    }

    private void setPaidOrderById(final int orderId){
        JSONObject data = new JSONObject();
        try {
            data.put("id", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.setOrderStatusById("setPaidOrderByID", data.toString());
        call.enqueue(new Callback<SetOrderStatusResponse>() {
            @Override
            public void onResponse(Call<SetOrderStatusResponse> call, Response<SetOrderStatusResponse> response) {
                SetOrderStatusResponse setOrderStatusResponse = response.body();
                if(setOrderStatusResponse.getSuccessValue() == 1){
                    Snackbar snackbar = Snackbar
                            .make(containerLayout, "รายการสั่งซื้อ#" + orderId + "\nถูกตั้งค่าว่าชำระเงินแล้ว", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    getActivity().onBackPressed();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(containerLayout, "โปรดตรวจสอบคำสั่งซื้อ#" + orderId + " แล้วลองใหม่อีกครั้ง", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<SetOrderStatusResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

    private void setDeriveringOrderById(final int orderId){
        JSONObject data = new JSONObject();
        try {
            data.put("id", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.setOrderStatusById("setDeliveringOrderByID", data.toString());
        call.enqueue(new Callback<SetOrderStatusResponse>() {
            @Override
            public void onResponse(Call<SetOrderStatusResponse> call, Response<SetOrderStatusResponse> response) {
                SetOrderStatusResponse setOrderStatusResponse = response.body();
                if(setOrderStatusResponse.getSuccessValue() == 1){
                    Snackbar snackbar = Snackbar
                            .make(containerLayout, "รายการสั่งซื้อ#" + orderId + "\nถูกตั้งค่าว่ากำลังจัดส่ง", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    getActivity().onBackPressed();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(containerLayout, "โปรดตรวจสอบคำสั่งซื้อ#" + orderId + " ลองใหม่อีกครั้ง", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<SetOrderStatusResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

    private void setDeriveredOrderById(final int orderId){
        JSONObject data = new JSONObject();
        try {
            data.put("id", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.setOrderStatusById("setDeliveredOrderByID", data.toString());
        call.enqueue(new Callback<SetOrderStatusResponse>() {
            @Override
            public void onResponse(Call<SetOrderStatusResponse> call, Response<SetOrderStatusResponse> response) {
                SetOrderStatusResponse setOrderStatusResponse = response.body();
                if(setOrderStatusResponse.getSuccessValue() == 1){
                    Snackbar snackbar = Snackbar
                            .make(containerLayout, "รายการสั่งซื้อ#" + orderId + "\nถูกตั้งค่าว่าจัดส่งแล้ว", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    getActivity().onBackPressed();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(containerLayout, "โปรดตรวจสอบคำสั่งซื้อ#" + orderId + " ลองใหม่อีกครั้ง", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<SetOrderStatusResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

    private void setCancelOrderById(final int orderId){
        JSONObject data = new JSONObject();
        try {
            data.put("id", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.v("DEBUG", message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.setOrderStatusById("cancelOrderByID", data.toString());
        call.enqueue(new Callback<SetOrderStatusResponse>() {
            @Override
            public void onResponse(Call<SetOrderStatusResponse> call, Response<SetOrderStatusResponse> response) {
                SetOrderStatusResponse setOrderStatusResponse = response.body();
                if(setOrderStatusResponse.getSuccessValue() == 1){
                    Snackbar snackbar = Snackbar
                            .make(containerLayout, "รายการสั่งซื้อ#" + orderId + "\nถูกตั้งค่าว่ายกเลิก", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    getActivity().onBackPressed();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(containerLayout, "โปรดตรวจสอบคำสั่งซื้อ#" + orderId + " ลองใหม่อีกครั้ง", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    Log.d("DEBUG","Cancel order error:" + setOrderStatusResponse.getErrorMessage());
                }
            }

            @Override
            public void onFailure(Call<SetOrderStatusResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

}
