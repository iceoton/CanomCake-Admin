package com.iceoton.canomcakeadmin.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.fragment.AddAdminFragment;
import com.iceoton.canomcakeadmin.fragment.EditAdminFragment;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initialActionBar();

        String tag = getIntent().getStringExtra("tag");

        Bundle args = new Bundle();
        args.putParcelable("admin", getIntent().getParcelableExtra("admin"));
        if(savedInstanceState == null){
            if(tag.equals("add")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.contentContainer, AddAdminFragment.newInstance(null))
                        .commit();
            }else if(tag.equals("edit")){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.contentContainer, EditAdminFragment.newInstance(args))
                        .commit();
            }
        }
    }

    public void placeFragmentToContrainer(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void initialActionBar(){
        // Initializing Toolbar and setting it as the actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("ผู้ดูแลระบบ");
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}
