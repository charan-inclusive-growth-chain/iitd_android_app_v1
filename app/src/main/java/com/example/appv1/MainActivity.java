package com.example.appv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        replaceFragment(new DashboardFrag());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                int id = item.getItemId();
                item.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);
                if (id == R.id.menu_loan) {
                    replaceFragment(new LoanListFrag());
                }
                else if(id == R.id.menu_dashboard) {
                    replaceFragment(new DashboardFrag());
                }else if (id == R.id.menu_store) {
                    replaceFragment(new StoreFrag());
                } else if (id == R.id.menu_sell) {
                    replaceFragment(new SellFrag());
                } else if (id == R.id.menu_transaction) {
                    replaceFragment(new TransactionHistoryFrag());
                } else if (id == R.id.menu_cropadvisory) {
                    Toast.makeText(MainActivity.this, "Crop Advisory is Clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.menu_chatbot) {
                    Toast.makeText(MainActivity.this, "Chatbot is Clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.menu_logout) {
                    Toast.makeText(MainActivity.this, "Logout is Clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.menu_settings) {
                    Toast.makeText(MainActivity.this, "Settings is Clicked", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.menu_profile) {
                    replaceFragment(new ProfileFrag());
                }

                return true;
            }
        });


    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();


    }

}