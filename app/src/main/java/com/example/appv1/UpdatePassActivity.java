package com.example.appv1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class UpdatePassActivity extends AppCompatActivity {
    Button updatePass;
    TextInputEditText oldPass, newPass;
    TextView oldPassText, newPassText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_password_layout);
        updatePass = findViewById(R.id.update_pass_submit);
        oldPass = findViewById(R.id.old_pass);
        oldPassText = findViewById(R.id.old_pass_text);
        newPass = findViewById(R.id.new_pass);
        newPassText = findViewById(R.id.new_pass_text);
        addOnClickListenerForSubmit();
    }

    private void addOnClickListenerForSubmit() {
        updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newPass.getText().toString().trim().isEmpty() || oldPass.getText().toString().trim().isEmpty()) {
                    if(newPass.getText().toString().trim().isEmpty()) {
                        newPassText.setError("Enter old password");
                    }
                    else {
                        oldPassText.setError("Enter new password");
                    }
                }
                else {
                    String url = getString(R.string.url) + "/updatepassword";
                    Log.d("URL ACtivity", url);
                    String token = LoginActivity.getToken(getApplicationContext());
                    Log.d("ActivityToken", token);

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("oldPassword", oldPass.getText().toString().trim());
                        jsonObject.put("newPassword", newPass.getText().toString().trim());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .header("Authorization", "Bearer " + token)
                            .patch(requestBody)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String responseBody = response.body().string();
                                JSONObject jsonResponse = null;
                                try {
                                    jsonResponse = new JSONObject(responseBody);
                                    Log.d("Update Password", jsonResponse.toString());
                                    // Toast.makeText(UpdatePassActivity.this, "Password is updated!", Toast.LENGTH_SHORT).show();
                                    response.close();

                                    Intent nextIntent = new Intent(UpdatePassActivity.this, MainActivity.class);
                                    UpdatePassActivity.this.startActivity(nextIntent);

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });
                    finish();
                }
            }
        });
    }

}
