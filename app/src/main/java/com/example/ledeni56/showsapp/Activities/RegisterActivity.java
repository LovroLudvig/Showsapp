package com.example.ledeni56.showsapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;


import com.example.ledeni56.showsapp.Static.InputValidations;
import com.example.ledeni56.showsapp.Networking.ApiServiceFactory;
import com.example.ledeni56.showsapp.Networking.ResponseRegister;
import com.example.ledeni56.showsapp.R;
import com.example.ledeni56.showsapp.Networking.UserLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BasicActivity {
    public static final String EMAIL_STRING="email string";

    private TextInputLayout emailWrapper;
    private TextInputLayout passwordWrapper;
    private Button registerButton;
    private TextInputLayout confirmPasswordWrapper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        emailWrapper=findViewById(R.id.emailWrapper);
        passwordWrapper=findViewById(R.id.passwordWrapper);
        confirmPasswordWrapper=findViewById(R.id.confirmPasswordWrapper);
        registerButton=findViewById(R.id.registerButton);

        setToolbar();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();

                String email = emailWrapper.getEditText().getText().toString();
                String password = passwordWrapper.getEditText().getText().toString();
                String otherPassword = confirmPasswordWrapper.getEditText().getText().toString();
                if (!InputValidations.validateEmail(email)) {
                    emailWrapper.setError("Not a valid email address!");
                } else{
                    emailWrapper.setErrorEnabled(false);
                }
                if (!InputValidations.validatePassword(password)) {
                    passwordWrapper.setError("Password must have at least 5 characters!");
                } else {
                    passwordWrapper.setErrorEnabled(false);
                }
                if(!InputValidations.validatePasswordsMatch(password, otherPassword)){
                    confirmPasswordWrapper.setError("Passwords must mach!");
                }else{
                    confirmPasswordWrapper.setErrorEnabled(false);
                }
                if (InputValidations.validateEmail(email) && InputValidations.validatePassword(password) && InputValidations.validatePasswordsMatch(password,otherPassword)){

                    showProgress();
                    ApiServiceFactory.get().register(new UserLogin(email,password)).enqueue(new Callback<ResponseRegister>() {
                        @Override
                        public void onResponse(Call<ResponseRegister> call, Response<ResponseRegister> response) {
                            hideProgress();
                            if (response.isSuccessful()){
                                if (response.code()==200){
                                    showError("Email already exists.");
                                }else{
                                    finishMyActivity();
                                }

                            } else{
                                showError("Please check your internet connection.");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseRegister> call, Throwable t) {
                            hideProgress();
                            showError("Please check your internet connection.");
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

    @Override
    public void onBackPressed() {
        hideKeyboard();
        super.onBackPressed();
    }
}
