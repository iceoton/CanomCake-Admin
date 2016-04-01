package com.iceoton.canomcakeadmin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.iceoton.canomcakeadmin.R;
import com.iceoton.canomcakeadmin.fragment.ContentFragment;
import com.iceoton.canomcakeadmin.fragment.HomeFragment;
import com.iceoton.canomcakeadmin.fragment.ManageAdminFragment;
import com.iceoton.canomcakeadmin.fragment.ManageOrdersFragment;
import com.iceoton.canomcakeadmin.fragment.ManageProductFragment;
import com.iceoton.canomcakeadmin.fragment.ManageUserFragment;
import com.iceoton.canomcakeadmin.util.AppPreference;

public class MainActivity extends AppCompatActivity {
    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Code here will be triggered once the drawer closes as we don't want anything to happen so we leave this blank
                        super.onDrawerClosed(drawerView);
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Code here will be triggered once the drawer open as we don't want anything to happen so we leave this blank
                        super.onDrawerOpened(drawerView);
                    }
                };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon won't show up
        actionBarDrawerToggle.syncState();

        initialNavigationView(savedInstanceState);
    }

    private void initialNavigationView(Bundle savedInstanceState) {
        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        View header = navigationView.getHeaderView(0);
        TextView txtUsername = (TextView) header.findViewById(R.id.username);
        TextView txtEmail = (TextView) header.findViewById(R.id.email);
        Button btnLogout = (Button) header.findViewById(R.id.button_logout);

        final AppPreference appPreference = new AppPreference(MainActivity.this);
        txtUsername.setText(appPreference.getFullName().replace("," , "  "));
        txtEmail.setText(appPreference.getUserName());
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appPreference.saveLoginStatus(false);
                Intent intentToLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentToLogin);
                finish();
            }
        });


        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Closing drawer on item click
                drawerLayout.closeDrawers();
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    return true;
                }

                Fragment fragment = new ContentFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.home:
                        fragment = new HomeFragment();
                        toolbar.setTitle(R.string.menu_home);
                        break;
                    case R.id.orders:
                        fragment = new ManageOrdersFragment();
                        toolbar.setTitle(R.string.menu_orders);
                        break;
                    case R.id.product:
                        fragment = new ManageProductFragment();
                        toolbar.setTitle(R.string.menu_product);
                        break;
                    case R.id.members:
                        fragment = new ManageUserFragment();
                        toolbar.setTitle(R.string.menu_members);
                        break;
                    case R.id.admin:
                        fragment = new ManageAdminFragment();
                        toolbar.setTitle(R.string.menu_admin);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();

                }
                fragmentTransaction.replace(R.id.content_container, fragment);
                fragmentTransaction.commit();
                // Returns: true to display the item as the selected item
                return true;
            }
        });

        // Set Home page as startup page.
        if (savedInstanceState == null) {
            navigationView.getMenu().performIdentifierAction(R.id.home, 0);
            navigationView.setCheckedItem(R.id.home);
        }
    }

    private static long back_pressed;

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Log.d("DEBUG", "popping BackStack");
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            if (back_pressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed(); // Exit
            } else {
                Toast.makeText(getBaseContext(), R.string.press_one_again,
                        Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        }
    }

}
