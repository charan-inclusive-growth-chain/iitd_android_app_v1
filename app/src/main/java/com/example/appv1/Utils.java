package com.example.appv1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.appv1.InitialSplashScreenActivity;
//import com.iitd.login.LoginActivity;

public class Utils
{

    public static void loginUserBasedOnRole(Context context, Activity parentActivity)
    {
        Intent userBasedIntent = new Intent(parentActivity, LoginActivity.class);

        //        LoginResponse loginResponse = new Gson().fromJson(PreferenceManager.getDefaultSharedPreferences(context).getString("user", ""), LoginResponse.class);
        //
        //        if (Objects.isNull(loginResponse) || !loginResponse.isSuccess()) {
        //            Intent loginIntent = new Intent(parentActivity, LoginActivity.class);
        //            parentActivity.startActivityForResult(loginIntent, 1);
        //        } else {
        //            //todo based on role should send
        //            if (Objects.equals(loginResponse.getRole(), "associate")) {
        //                Intent associateActivityIntent = new Intent(parentActivity, AssociateMainActivity.class);
        //                parentActivity.startActivityForResult(associateActivityIntent, 2);
        //            }
        //            else if (Objects.equals(loginResponse.getRole(), "transporter")) {
        //                Intent transporterActivityIntent = new Intent(parentActivity, TransporterListActivity.class);
        //                parentActivity.startActivityForResult(transporterActivityIntent, 2);
        //            }
        //
        //            else if (Objects.equals(loginResponse.getRole(), "security")) {
        //                Intent transporterActivityIntent = new Intent(parentActivity, SecurityListActivity.class);
        //                parentActivity.startActivityForResult(transporterActivityIntent, 1);
        //            }
        //        }

        parentActivity.startActivityForResult(userBasedIntent, 1);
    }
}