package com.example.appv1;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import static java.util.Objects.isNull;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity
{

    TextInputEditText userNameNode, passwordNode;
    TextView forgotPassNode, createNewAccountNode;
    ImageView loginBackNode;
    Button loginButtonNode;
    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler;

    //    private static Retrofit retrofit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        // Set the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.primary_green));
        }

        userNameNode = findViewById(R.id.username);
        passwordNode = findViewById(R.id.password);
        passwordNode.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        addOnClickListenerForLoginButton();
        //addOnClickListenerForForgotPass();
        addOnClickListenerForRegisteringFarmer();
        addTouchListenerForPassEye();

        if (!isNetworkConnected()) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

        mHandler = new Handler();
        startRepeatingTask();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        stopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable()
    {
        @Override
        public void run()
        {
            try
            {
                shakeView(createNewAccountNode); //this function can change value of mInterval.
            }
            finally
            {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask()
    {
        mStatusChecker.run();
    }

    void stopRepeatingTask()
    {
        mHandler.removeCallbacks(mStatusChecker);
    }

    private void addOnClickListenerForLoginButton()
    {
        loginButtonNode = findViewById(R.id.loginButton);
        loginButtonNode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "login button", Toast.LENGTH_SHORT).show();
                String userName = String.valueOf(userNameNode.getText());
                String password = String.valueOf(passwordNode.getText());

                try
                {
                    doLogin(userName, password);
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

//	private void addOnClickListenerForForgotPass()
//	{
//		//forgotPassNode = findViewById(R.id.forgotPass);
//		forgotPassNode.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				Toast.makeText(getApplicationContext(), "Forgot Password", Toast.LENGTH_SHORT).show();
//
//			}
//		});
//	}

    private void addOnClickListenerForRegisteringFarmer()
    {
        createNewAccountNode = findViewById(R.id.createNewAccount);
        createNewAccountNode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent registerFarmerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerFarmerIntent);

            }
        });
    }

    @SuppressLint("ClickableViewAccessibility") private void addTouchListenerForPassEye()
    {
        passwordNode.setOnTouchListener(new View.OnTouchListener()
        {
            int x = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_LEFT = 0;
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    if(event.getRawX() >= (passwordNode.getRight() - passwordNode.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
                    {
                        // your action here
                        x++;
                        if(x % 2 != 0)
                        {
                            Toast.makeText(getApplicationContext(), "visible", Toast.LENGTH_SHORT).show();
                            passwordNode.setInputType(InputType.TYPE_CLASS_TEXT);
                            passwordNode.setSelection(passwordNode.getText().length());
                            //passwordNode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass_lock, 0, R.drawable.pass_eye_hide_drawable, 0);

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "hide", Toast.LENGTH_SHORT).show();
                            passwordNode.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            passwordNode.setSelection(passwordNode.getText().length());
                            //passwordNode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass_lock, 0, R.drawable.pass_eye, 0);

                        }
                        return false;
                    }
                }
                return false;
            }
        });
    }

    private void resetToDefaults()
    {
        passwordNode.setText("");
        passwordNode.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //passwordNode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass_lock, 0, R.drawable.pass_eye, 0);

        userNameNode.setText("");
        userNameNode.requestFocus();
    }

    private void doLogin(String userName, String password) throws JSONException
    {
        // login api
        JSONObject requestBody = new JSONObject();
        requestBody.put("userName", userName);
        requestBody.put("password", password);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestB = RequestBody.create(JSON, requestBody.toString());

        String url = getString(R.string.url) + "/login";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .post(requestB)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Handle the failure case
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonResponse = new JSONObject(responseData);
                        Log.d("User", jsonResponse.toString());
                        boolean loginSuccessful = !isNull(jsonResponse.toString());
                        if (loginSuccessful) {

                            String Token = jsonResponse.getString("token");
                            JSONObject dataObject = jsonResponse.getJSONObject("data");
                            String FarmerID = dataObject.getString("_id");
                            String FarmerName = dataObject.getString("userName");
                            String FpoID = dataObject.getString("fpoId");
                            String PinCode = dataObject.getString("pinCode");

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("user_logged_in", true);
                            editor.putString("token", Token);
                            editor.putString("farmerId", FarmerID);
                            editor.putString("farmerName", FarmerName);
                            editor.putString("fpoId", FpoID);
                            editor.putString("pincode", PinCode);
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("Response", jsonResponse.toString());
                                    passwordNode.setError("Login unsuccessful");

                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setTitle("Login unsuccessful")
                                            .setMessage("Your login attempt was unsuccessful.")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            String responseData = null;
                            try {
                                responseData = response.body().string();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            JSONObject jsonResponse = null;
                            try {
                                jsonResponse = new JSONObject(responseData);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            Log.d("Response", jsonResponse.toString());
                            passwordNode.setError("Login unsuccessful");

                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Login unsuccessful")
                                    .setMessage("Your login attempt was unsuccessful.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode)
        {
            case 69:
                resetToDefaults();
                break;
            default:
                finish();
        }
    }

    public void shakeView(View view)
    {
        Animation shake;
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        view.startAnimation(shake); // starts animation
    }

    public static String getToken(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = preferences.getString("token", "");
        return token;
    }

    public static String getFarmerID(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String id = preferences.getString("farmerId", "");
        return id;
    }


}