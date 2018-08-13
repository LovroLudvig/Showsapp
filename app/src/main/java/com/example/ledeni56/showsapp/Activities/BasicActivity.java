package com.example.ledeni56.showsapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import com.example.ledeni56.showsapp.Networking.ApiEpisode;


public class BasicActivity extends AppCompatActivity {
    private static ProgressDialog progressDialog;

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void showError(String text) {
        new AlertDialog.Builder(this)
                .setTitle("Error!")
                .setMessage(text)
                .setPositiveButton("OK", null)
                .create()
                .show();
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void loadingScreenError() {
        new AlertDialog.Builder(this)
                .setTitle("Error!")
                .setMessage("Please turn on your internet connection when you open the app for the first time.")
                .setPositiveButton("OK", null)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                })
                .create()
                .show();
    }

    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "Please wait", "Loading...", true, false);
    }

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


}
