package com.iceoton.canomcakeadmin.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.activity.BankTransferActivity;
import com.iceoton.canomcakeadmin.adapter.TransactionListAdapter;
import com.iceoton.canomcakeadmin.medel.Transaction;
import com.iceoton.canomcakeadmin.medel.response.GetAllTransactionsResponse;
import com.iceoton.canomcakeadmin.service.CanomCakeService;
import com.iceoton.canomcakeadmin.util.AppPreference;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BankTransferFragment extends Fragment {
    ListView lvBankTransfer;

    public BankTransferFragment() {
        // Required empty public constructor
    }

    public static BankTransferFragment newInstance(Bundle args) {
        BankTransferFragment fragment = new BankTransferFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_bank_transfer, container, false);
        initialView(rootView, savedInstanceState);
        return rootView;
    }

    private void initialView(View rootView, Bundle savedInstanceState) {
        lvBankTransfer = (ListView) rootView.findViewById(R.id.list_bank_transfer);
        loadBankTransferFromServer();
    }

    private void loadBankTransferFromServer() {
        AppPreference preference = new AppPreference(getActivity());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(preference.getApiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.loadAllTransactions("getAllTransactions");
        call.enqueue(new Callback<GetAllTransactionsResponse>() {
            @Override
            public void onResponse(Call<GetAllTransactionsResponse> call, Response<GetAllTransactionsResponse> response) {
                final ArrayList<Transaction> transactions = response.body().getResult();
                if (transactions != null) {
                    Log.d("DEBUG", "Number of transactions: " + transactions.size());
                    TransactionListAdapter transactionListAdapter = new TransactionListAdapter(getActivity(), transactions);
                    lvBankTransfer.setAdapter(transactionListAdapter);
                    lvBankTransfer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            showDetailOfTransaction(transactions.get(position));
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetAllTransactionsResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

    private void showDetailOfTransaction(Transaction transaction){
        Bundle args = transaction.parseToBundle();
        ((BankTransferActivity)getActivity()).placeFragmentToContrainer(
                TransactionDetailFragment.newInstance(args));
    }

}
