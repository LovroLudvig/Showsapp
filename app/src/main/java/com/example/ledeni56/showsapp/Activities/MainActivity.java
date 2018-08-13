package com.example.ledeni56.showsapp.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.ledeni56.showsapp.Fragments.AddEpisodeFragment;
import com.example.ledeni56.showsapp.Fragments.ShowSelectFragment;
import com.example.ledeni56.showsapp.Interfaces.ToolbarProvider;
import com.example.ledeni56.showsapp.R;

public class MainActivity extends BasicActivity implements ToolbarProvider {
    public static final String TOKEN_KEY = "token key";
    public static final String PREFS_NAME = "prefs name";
    private FragmentManager fragmentManager;
    private static boolean closeDialog = false;
    private String userToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        fragmentManager = getSupportFragmentManager();

        setUserToken();

        if (fragmentManager.findFragmentById(R.id.frameLayoutRight) == null) {
            ShowSelectFragment showSelectFragment = new ShowSelectFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frameLayoutRight, showSelectFragment);
            fragmentTransaction.commit();
        }

    }


    private void setUserToken() {
        if (getIntent().getExtras() != null) {
            userToken = getIntent().getExtras().getString(LoginActivity.SENDING_TOKEN_KEY);
            if (getIntent().getExtras().getBoolean(LoginActivity.REMEMBER_KEY)) {
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString(TOKEN_KEY, userToken);
                editor.commit();
            }
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
            userToken = sharedPreferences.getString(MainActivity.TOKEN_KEY, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (fragmentManager.getBackStackEntryCount()>1){
//            getToolbar().setVisibility(View.GONE);
//        }
    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
        Fragment frag = fragmentManager.findFragmentById(R.id.frameLayoutRight);
        if (closeDialog) {
            closeDialog = false;
            super.onBackPressed();
        } else if (frag instanceof AddEpisodeFragment) {
            AddEpisodeFragment fragment = (AddEpisodeFragment) frag;
            if (checkIfCanExit(fragment)) {
                super.onBackPressed();
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Warning");
                alertDialog.setMessage("You have unsaved changes, are you sure you want to exit?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                MainActivity.closeDialog = true;
                                onBackPressed();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        } else {
            super.onBackPressed();
        }

    }

    public String getUserToken() {
        return userToken;
    }

    private boolean checkIfCanExit(AddEpisodeFragment frag) {
        Bundle bun = frag.getCurrentFields();
        return bun.getParcelable("uri") == null && bun.getString("episode and season text").equals("Unknown") && bun.getString("episode desc").equals("") && bun.getString("episode name").equals("");
    }

    @Override
    public Toolbar getToolbar() {
        return findViewById(R.id.Toolbar);
    }
}
