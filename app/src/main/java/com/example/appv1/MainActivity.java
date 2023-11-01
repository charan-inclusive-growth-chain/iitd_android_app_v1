package com.example.appv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_AUDIO_PERMISSION_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.light_green));
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
//
        if(!CheckPermissions()) {
            RequestPermissions();
        }

        replaceFragment(new DashboardFrag());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);

            }
        });

        View headerView = navigationView.getHeaderView(0);
        ShapeableImageView profileImage = headerView.findViewById(R.id.profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                replaceFragment(new ProfileFrag());
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
                    replaceFragment(new VoiceBotFrag());
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

    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        int result1;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_AUDIO);
        }
        else {
            result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
//        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean LogResult = result ==  PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        Log.d("Debug", "The Result of the checkpermission function is " + LogResult);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Toast.makeText(getApplicationContext(), "Android Version 13+", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_MEDIA_AUDIO}, REQUEST_AUDIO_PERMISSION_CODE);

        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
        }
        // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_AUDIO_PERMISSION_CODE) {
            if (grantResults.length > 0) {
                boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (permissionToRecord && permissionToStore) {
                    Toast.makeText(getApplicationContext(), "Permissions Granted", Toast.LENGTH_LONG).show();
                } else {
                    StringBuilder deniedPermissions = new StringBuilder("Denied Permissions: ");

                    if (!permissionToRecord) {
                        deniedPermissions.append("Record Audio ");
                    }

                    if (!permissionToStore) {
                        deniedPermissions.append("Write to Storage ");
                    }

                    Toast.makeText(getApplicationContext(), deniedPermissions.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }


}