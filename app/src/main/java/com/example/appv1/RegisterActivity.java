package com.example.appv1;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.util.Log;
import android.widget.Toast;


import com.example.appv1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity
{
    List<Fragment> fragmentOrder = Arrays.asList(new RegisterPersonalDetailsFragment(), new RegisterAddressDetailsFragment(), new RegisterBankDetailsFragment(), new RegisterIdProofsFragment());
    int currentFragmentNumber = 0;

    Button next, prev, submit;

    LinearLayout nextLayout, prevLayout, submitLayout;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static JSONObject registerAsFarmerJson = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        addOnClickListenerForNextButton();
        addOnClickListenerForPrevButton();
        addOnClickListenerForSubmitButton();
        resetButtons();
        handleNextFragments(fragmentOrder.get(currentFragmentNumber));

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
        }
        verifyStoragePermissions(RegisterActivity.this);
    }

    public static JSONObject getRegisterAsFarmerJson() {
        return registerAsFarmerJson;
    }


    private void addOnClickListenerForSubmitButton()
    {
        submitLayout = findViewById(R.id.register_submit_layout);
        submit = findViewById(R.id.register_submit);
        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v) {
                boolean isValid = ((RegisterIdProofsFragment) fragmentOrder.get(3)).checkFields();
                if (!isValid) return;

                new AlertDialog.Builder(RegisterActivity.this)
                        .setTitle("Register As a Farmer")
                        .setMessage("Do you want to submit this form?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    String url = getString(R.string.url) + "/signup/farmer";
                                    String jsonPayload = registerAsFarmerJson.toString();
                                    OkHttpClient client = new OkHttpClient();

                                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                    RequestBody requestBody = RequestBody.create(JSON, jsonPayload);

                                    Log.d("RequestBody", requestBody.toString());

                                    Request request = new Request.Builder()
                                            .url(url)
                                            .post(requestBody)
                                            .build();


                                    Response response = client.newCall(request).execute();
                                    String responseBody = response.body().string();
                                    Log.d("Response", responseBody);



                                    JSONObject jsonResponse = new JSONObject(responseBody);
                                    JSONArray errors = jsonResponse.optJSONArray("errors");
                                    if (errors != null && errors.length() > 0) {
                                        JSONObject errorObject = errors.getJSONObject(0);
                                        String errorMessage = errorObject.optString("msg", "");
                                        if (errorMessage.equals("User with given username already exists")) {
                                            new AlertDialog.Builder(RegisterActivity.this)
                                                    .setTitle("Error")
                                                    .setMessage("User with given username already exists")
                                                    .setPositiveButton("Yes", null);
                                        } else {
                                            finish();
                                        }
                                    }



                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        }).show();
            }
        });
    }

    private void addOnClickListenerForPrevButton()
    {
        prevLayout = findViewById(R.id.register_prev_layout);
        prev = findViewById(R.id.register_previous);
        prev.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                currentFragmentNumber--;
                resetButtons();
                handlePreviousFragments(fragmentOrder.get(currentFragmentNumber));
            }
        });

    }

    private void addOnClickListenerForNextButton()
    {
        nextLayout = findViewById(R.id.register_next_layout);
        next = findViewById(R.id.register_next);
        next.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {

                if(currentFragmentNumber == 0) {
                    boolean isValid = ((RegisterPersonalDetailsFragment) fragmentOrder.get(0)).checkFields();
                    if (!isValid) return;
                }

                else if(currentFragmentNumber == 1) {
                    boolean isValid = ((RegisterAddressDetailsFragment) fragmentOrder.get(1)).checkFields();
                    if (!isValid) return;
                }
                else if(currentFragmentNumber == 2) {
                    boolean isValid = ((RegisterBankDetailsFragment) fragmentOrder.get(2)).checkFields();
                    if (!isValid) return;
                }


                currentFragmentNumber++;
                resetButtons();
                handleNextFragments(fragmentOrder.get(currentFragmentNumber));
            }
        });
    }


    private void resetButtons() {
        if (currentFragmentNumber == fragmentOrder.size() - 1) {
            // Last fragment (IDProofsFragment)
            nextLayout.setVisibility(View.GONE);
            submitLayout.setVisibility(View.VISIBLE);
        } else if (currentFragmentNumber == 0) {
            // First fragment
            prevLayout.setVisibility(View.GONE);
            nextLayout.setVisibility(View.VISIBLE);
            submitLayout.setVisibility(View.GONE);
        } else {
            // Intermediate fragments
            prevLayout.setVisibility(View.VISIBLE);
            nextLayout.setVisibility(View.VISIBLE);
            submitLayout.setVisibility(View.GONE);
        }
    }

    private void handleNextFragments(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.register_fragment_ph, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void handlePreviousFragments(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.register_fragment_ph, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void verifyStoragePermissions(Activity activity)
    {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permission != PackageManager.PERMISSION_GRANTED)
        {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override public void onBackPressed()
    {
        if(currentFragmentNumber==0)
        {
            finish();
        }
        super.onBackPressed();
    }


}
