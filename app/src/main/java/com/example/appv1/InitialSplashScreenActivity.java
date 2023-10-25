package com.example.appv1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.Locale;

import com.example.appv1.LoginActivity;
import com.example.appv1.MainActivity;
import com.example.appv1.R;
import com.example.appv1.Utils;

public class InitialSplashScreenActivity extends AppCompatActivity
{
    private boolean languageSelected = false;
    String selectedLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_splash_activity);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean userLoggedIn = preferences.getBoolean("user_logged_in", false);
        Log.d("Logged in", String.valueOf(userLoggedIn));
        String savedLanguage = preferences.getString("selected_language", "");

        if(userLoggedIn) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            if (!savedLanguage.isEmpty()) {
                setAppLocale(savedLanguage);
                languageSelected = true;
                Utils.loginUserBasedOnRole(getApplicationContext(), this);
                //restartApp();
            } else {
                showLanguageSelectionDialog();
            }
        }
    }

    private void showLanguageSelectionDialog() {
        final String[] languages = {"English", "हिंदी"}; // Add more languages if needed

        AlertDialog.Builder builder = new AlertDialog.Builder(InitialSplashScreenActivity.this);
        builder.setTitle("Select Language");
        builder.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Save the selected language
                if (which == 0) {
                    selectedLanguage = "en"; // English
                } else if (which == 1) {
                    selectedLanguage = "hi"; // Hindi
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Save the selected language
                setAppLocale(selectedLanguage);
                dialog.dismiss();

                // Save the selected language to SharedPreferences
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(InitialSplashScreenActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("selected_language", selectedLanguage);
                editor.apply();

                com.example.appv1.Utils.loginUserBasedOnRole(getApplicationContext(), InitialSplashScreenActivity.this);
                //restartApp();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Handle dismissal if needed
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void setAppLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.locale = locale;

        Resources resources = getBaseContext().getResources();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    private void restartApp() {
        if (languageSelected) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // ended activity from login
            finish();
        } else if (requestCode == 2) {
            com.example.appv1.Utils.loginUserBasedOnRole(getApplicationContext(), InitialSplashScreenActivity.this);
        }
    }
}
