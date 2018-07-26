package com.example.ledeni56.showsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    public static final String EMAIL_STRING="email string";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;


    private TextInputLayout emailWrapper;
    private TextInputLayout passwordWrapper;
    private Button registerButton;
    private TextInputLayout confirmPasswordWrapper;
    private ApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        emailWrapper=findViewById(R.id.emailWrapper);
        passwordWrapper=findViewById(R.id.passwordWrapper);
        confirmPasswordWrapper=findViewById(R.id.confirmPasswordWrapper);
        registerButton=findViewById(R.id.registerButton);

        apiService=NetworkingSupportMethods.initApiService();

        setToolbar();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();

                String email = emailWrapper.getEditText().getText().toString();
                String password = passwordWrapper.getEditText().getText().toString();
                String otherPassword = confirmPasswordWrapper.getEditText().getText().toString();
                if (!validateEmail(email)) {
                    emailWrapper.setError("Not a valid email address!");
                } else{
                    emailWrapper.setErrorEnabled(false);
                }
                if (!validatePassword(password)) {
                    passwordWrapper.setError("Password must have at least 5 characters!");
                } else {
                    passwordWrapper.setErrorEnabled(false);
                }
                if(!validatePasswordsMatch(password, otherPassword)){
                    confirmPasswordWrapper.setError("Passwords must mach!");
                }else{
                    confirmPasswordWrapper.setErrorEnabled(false);
                }
                if (validateEmail(email) && validatePassword(password) && validatePasswordsMatch(password,otherPassword)){

                    NetworkingSupportMethods.showProgress(RegisterActivity.this);
                    apiService.register(new UserLogin(email,password)).enqueue(new Callback<ResponseRegister>() {
                        @Override
                        public void onResponse(Call<ResponseRegister> call, Response<ResponseRegister> response) {
                            NetworkingSupportMethods.hideProgress();
                            if (response.isSuccessful()){
                                if (response.code()==200){
                                    NetworkingSupportMethods.showError(RegisterActivity.this,"Email already exists.");
                                }else{
                                    finishMyActivity();
                                }

                            } else{
                                NetworkingSupportMethods.showError(RegisterActivity.this,"Please check your internet connection.");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseRegister> call, Throwable t) {
                            NetworkingSupportMethods.hideProgress();
                            NetworkingSupportMethods.showError(RegisterActivity.this,"Please check your internet connection.");
                        }
                    });
                }
            }
        });
    }

    private void finishMyActivity() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(EMAIL_STRING,emailWrapper.getEditText().getText().toString());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    private void setToolbar() {
        Toolbar toolbar=findViewById(R.id.registerToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_register_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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


    public boolean validatePasswordsMatch(String password, String otherPassword) {
        return password.equals(otherPassword);
    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
        super.onBackPressed();
    }
}
