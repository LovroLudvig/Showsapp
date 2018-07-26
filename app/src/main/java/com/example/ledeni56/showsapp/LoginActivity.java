package com.example.ledeni56.showsapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private static final int RESULT_CODE_EMAIL = 1;

    public static final String SENDING_TOKEN_KEY = "user token SENDING";
    public static final java.lang.String REMEMBER_KEY = "remember key";

    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    private TextInputLayout emailWrapper;
    private TextInputLayout passwordWrapper;
    private Button loginButton;
    private View createAnAccount;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        if (sharedPreferences.getString(MainActivity.TOKEN_KEY, null)!=null){
            Intent i=new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }

        setContentView(R.layout.activity_login);

        emailWrapper=findViewById(R.id.emailWrapper);
        passwordWrapper=findViewById(R.id.passwordWrapper);
        loginButton=findViewById(R.id.loginButton);
        createAnAccount=findViewById(R.id.createAnAccount);
        apiService= NetworkingSupportMethods.initApiService();

        createAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(i, RESULT_CODE_EMAIL);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();

                String email = emailWrapper.getEditText().getText().toString();
                final String password = passwordWrapper.getEditText().getText().toString();
                if (!validateEmail(email)) {
                    emailWrapper.setError("Not a valid email address!");
                } else{
                    emailWrapper.setErrorEnabled(false);
                } if (!validatePassword(password)) {
                    passwordWrapper.setError("Password must have at least 5 characters!");
                } else {
                    passwordWrapper.setErrorEnabled(false);
                }
                if (validateEmail(email) && validatePassword(password)){
                    NetworkingSupportMethods.showProgress(LoginActivity.this);
                    apiService.login(new UserLogin(email,password)).enqueue(new Callback<ResponseLogin>() {
                        @Override
                        public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                            NetworkingSupportMethods.hideProgress();
                            if (response.isSuccessful()){
                                startMain(response.body().getData().getToken());
                            } else{
                                NetworkingSupportMethods.showError(LoginActivity.this,"Have you created an account?\n\nYou can create an account bellow.");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseLogin> call, Throwable t) {
                            NetworkingSupportMethods.hideProgress();
                            NetworkingSupportMethods.showError(LoginActivity.this,"Have you created an account?\n\nYou can create an account bellow.");
                        }
                    });
                }
            }
        });
    }

    private void startMain(String token) {
        Intent i=new Intent(this,MainActivity.class);
        i.putExtra(SENDING_TOKEN_KEY, token);
        i.putExtra(REMEMBER_KEY, ((CheckBox)findViewById(R.id.checkBox)).isChecked());
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==RESULT_CODE_EMAIL && resultCode== Activity.RESULT_OK){
            emailWrapper.getEditText().setText(data.getExtras().getString(RegisterActivity.EMAIL_STRING));
            passwordWrapper.getEditText().setText("");
        }
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validatePassword(String password) {
        return password.length() >= 5;
    }



}
