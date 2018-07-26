package com.example.ledeni56.showsapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class BasicActivity extends AppCompatActivity {
    private static ProgressDialog progressDialog;

    protected void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void showError(String text) {
        new AlertDialog.Builder(this)
                .setTitle("Error!")
                .setMessage(text)
                .setPositiveButton("OK", null)
                .create()
                .show();
    }
    protected void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    protected void showProgress() {
        progressDialog = ProgressDialog.show(this, "Please wait", "Loading...", true, false);
    }
}
