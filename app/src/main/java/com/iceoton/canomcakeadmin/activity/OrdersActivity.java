package com.iceoton.canomcakeadmin.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.fragment.OrdersFragment;

public class OrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        String tag = getIntent().getStringExtra("tag");

        Bundle args = new Bundle();
        args.putString("tag", tag);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, OrdersFragment.newInstance(args))
                    .commit();
        }

    }
}
