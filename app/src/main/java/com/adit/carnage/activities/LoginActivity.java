package com.adit.carnage.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.adit.carnage.R;
import com.adit.carnage.interfaces.LoginView;
import com.adit.carnage.presenters.LoginPresenter;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvErrorUsername;
    private TextView tvErrorPassword;
    private TextView tvState;

    private String username;
    private String password;

    private LoginPresenter presenter;

    TextWatcher usernameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            presenter.updateUsername(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher passWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            presenter.updatePassword(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvState = findViewById(R.id.tvState);
        tvErrorUsername = findViewById(R.id.tvErrorUsername);
        tvErrorPassword = findViewById(R.id.tvErrorPassword);

        etUsername.addTextChangedListener(usernameWatcher);
        etPassword.addTextChangedListener(passWatcher);

        presenter = new LoginPresenter(this, this);

        presenter.initSharedPreferences();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.validatePassword();
            }
        });
    }

    @Override
    public void updateTextState(String info) {
        tvState.setText(info);
    }

    @Override
    public void showBothError() {
        tvErrorUsername.setVisibility(View.VISIBLE);
        tvErrorPassword.setVisibility(View.VISIBLE);
        tvErrorUsername.setText("Username tidak boleh kosong");
        tvErrorPassword.setText("Password tidak boleh kosong");
    }

    @Override
    public void showErrorUsername(Integer code) {
        tvErrorPassword.setVisibility(View.GONE);

        if(code == 0){
            tvErrorUsername.setText("Username tidak boleh kosong");
        }else if(code == 1){
            tvErrorUsername.setText("Username kurang dari 6 karakter");
        }else if(code == 2){
            tvErrorUsername.setText("Username anda tidak ditemukan");
        }

        tvErrorUsername.setTextColor(getResources().getColor(R.color.maroonred));
        tvErrorUsername.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorPassword(Integer code) {
        tvErrorUsername.setVisibility(View.GONE);

        if(code == 0){
            tvErrorPassword.setText("Password tidak boleh kosong");
        }else if(code == 1){
            tvErrorPassword.setText("Password kurang dari 6 karakter");
        }else if(code == 2){
            tvErrorPassword.setText("Password anda salah");
        }

        tvErrorPassword.setTextColor(getResources().getColor(R.color.maroonred));
        tvErrorPassword.setVisibility(View.VISIBLE);
    }

    @Override
    public void valid() {
        presenter.login();
    }
}
