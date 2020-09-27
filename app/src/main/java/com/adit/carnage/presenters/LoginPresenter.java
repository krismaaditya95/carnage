package com.adit.carnage.presenters;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.adit.carnage.activities.MainActivity;
import com.adit.carnage.apis.classes.User;
import com.adit.carnage.interfaces.LoginView;

public class LoginPresenter extends BasePresenter{
    private User user;
    private LoginView view;
    private String username;
    private String password;

    public LoginPresenter(LoginView view, Activity activity){
        this.user = new User();
        this.view = view;
        this.activity = activity;
    }

    public void updateUsername(String username){
        user.setUsername(username);
        view.updateTextState(user.getUsername());
    }

    public void updatePassword(String password){
        user.setPassword(password);
        view.updateTextState(user.getPassword());
    }

    public void validatePassword() {
        username = user.getUsername();
        password = user.getPassword();

        if(username == null && password == null){
            view.showBothError();
        }else{

            if(username == null){
                // 0: kosong
                // 1: kurang dari 6 karakter
                // 2: username tidak ditemukan
                view.showErrorUsername(0);
            }else{
                if(username.length() < 6){
                    view.showErrorUsername(1);
                }else{
                    if(!username.equals("admin123")){
                        view.showErrorUsername(2);
                    }else{
//                        tvErrorUsername.setVisibility(View.INVISIBLE);
                        if(password == null){
                            view.showErrorPassword(0);
                        }else{

                            if(password.length() < 6){
                                view.showErrorPassword(1);
                            }else{
                                if(!password.equals("123456")){
                                    view.showErrorPassword(2);
                                }else{
                                    view.valid();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void login(){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username", user.getUsername());
        editor.putString("password", user.getPassword());
        editor.putBoolean("isLoggedIn", true);
        editor.commit();
//        view.goToMain();
        checkLogin();
    }

    public void checkLogin(){
        if(checkLoginState() == false){
            Toast.makeText(activity.getApplicationContext(), "Anda belum login", Toast.LENGTH_SHORT).show();
        }else{
            // user sudah login
            Toast.makeText(activity.getApplicationContext(), "Anda sudah login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }
}
