package com.iceoton.canomcakeadmin.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.medel.Transaction;
import com.iceoton.canomcakeadmin.medel.response.DeleteTransactionResponse;
import com.iceoton.canomcakeadmin.medel.response.SetOrderStatusResponse;
import com.iceoton.canomcakeadmin.service.CanomCakeService;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TransactionDetailFragment extends Fragment {
    Transaction transaction;
    TextView txtOrderId, txtCustomerId, txtBankAccount, txtBankName,
            txtAmount, txtDateTime, txtDescription;
    Button btnAccept;
    LinearLayout containerLayout;

    public TransactionDetailFragment() {
        // Required empty public constructor
    }

    public static TransactionDetailFragment newInstance(Bundle args) {
        TransactionDetailFragment fragment = new TransactionDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            transaction = Transaction.formBundle(getArguments());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transaction_detail, container, false);
        initialView(rootView, savedInstanceState);

        return rootView;
    }

    private void initialView(View rootView, Bundle savedInstanceState) {
        getActivity().setTitle("หมายเลขแจ้งโอน #" + transaction.getId());
        containerLayout = (LinearLayout) rootView.findViewById(R.id.containerLayout);
        txtOrderId = (TextView) rootView.findViewById(R.id.text_order_id);
        txtCustomerId = (TextView) rootView.findViewById(R.id.text_customer_id);
        txtBankAccount = (TextView) rootView.findViewById(R.id.text_bank_account);
        txtBankName = (TextView) rootView.findViewById(R.id.text_bank_name);
        txtAmount = (TextView) rootView.findViewById(R.id.text_amount);
        txtDateTime = (TextView) rootView.findViewById(R.id.text_date_time);
        txtDescription = (TextView) rootView.findViewById(R.id.text_description);
        btnAccept = (Button) rootView.findViewById(R.id.button_accept);

        txtOrderId.setText("#" + String.valueOf(transaction.getOrderId()));
        txtCustomerId.setText("#" + String.valueOf(transaction.getCustomerId()));
        txtBankAccount.setText(transaction.getBankAccount());
        txtBankName.setText(transaction.getBankName());
        txtAmount.setText(String.valueOf(transaction.getAmount()) + " บาท");
        txtDateTime.setText(transaction.getDateTime());
        txtDescription.setText(transaction.getDescription());

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentAcceptBankTransfer();
            }
        });
    }

    private void sentAcceptBankTransfer(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("ข้อมูลการโอนเงิน");
        builder.setIcon(R.drawable.ic_bank_tranfer);
        builder.setMessage("ยืนยันการทำรายการว่าได้รับเงินเรียบร้อยแล้ว")
                .setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setPaidOrderById(transaction.getOrderId());
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

    private void deleteTransactionById(final int transactionId){
        JSONObject data = new JSONObject();
        try {
            data.put("id", transactionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.deleteTransactionById("deleteTransactionByID", data.toString());
        call.enqueue(new Callback<DeleteTransactionResponse>() {
            @Override
            public void onResponse(Call<DeleteTransactionResponse> call, Response<DeleteTransactionResponse> response) {
                DeleteTransactionResponse deleteTransactionResponse = response.body();
                if(deleteTransactionResponse.getSuccessValue() == 1){

                } else {

                }
            }

            @Override
            public void onFailure(Call<DeleteTransactionResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
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
                    deleteTransactionById(transaction.getId());
                    Snackbar snackbar = Snackbar
                            .make(containerLayout, "รายการสั่งซื้อ#" + orderId + "\nถูกตั้งค่าว่าชำระเงินแล้ว", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    getActivity().onBackPressed();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(containerLayout, "ไม่พบรายการสั่งซื้อ#" + orderId, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<SetOrderStatusResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

}
