package com.adit.carnage.presenters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.adit.carnage.apis.ApiClient;
import com.adit.carnage.interfaces.ApiInterface;

public class BasePresenter {
    public SharedPreferences pref;

    public Activity activity;

    public ApiInterface apiInterface;

    public BasePresenter(){

    }

    public void initSharedPreferences(){
        pref = activity.getApplicationContext().getSharedPreferences("pref", 0);
        Toast.makeText(activity.getApplicationContext(), "Shared Preference Initialized!", Toast.LENGTH_SHORT).show();
    }

    public boolean checkLoginState(){
        boolean isLogin = false;

        if(pref.contains("isLoggedIn")){
            Boolean isLoggedIn = pref.getBoolean("isLoggedIn", true);
            if(isLoggedIn){
                // klo udah login
                isLogin = true;
            }
        }

        return isLogin;
    }

    public void initApi(){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Toast.makeText(activity, "api initialized", Toast.LENGTH_SHORT).show();
    }
}
