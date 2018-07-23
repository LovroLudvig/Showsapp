package com.example.ledeni56.showsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends FragmentActivity implements ShowSelectFragment.OnShowFragmentInteractionListener, AddEpisodeFragment.OnEpisodeAddFragmentInteractionListener,EpisodeSelectFragment.OnEpisodeFragmentInteractionListener{
    private Toolbar myToolbar;
    private Show currentShow;
    private FragmentManager fragmentManager;

    public static boolean closeDialog=false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        fragmentManager = getSupportFragmentManager();

        if(savedInstanceState!=null){
            Fragment frag=fragmentManager.findFragmentByTag(topFragmentName().toUpperCase());
            if(findViewById(R.id.frameLayoutRight)==null){
                fragmentManager.beginTransaction().replace(R.id.frameLayoutLeft,frag,topFragmentName().toUpperCase()).commit();
                return;
            }else{
                if(topFragmentName().equals("root()=")){
                    return;
                }else{
                    fragmentManager.beginTransaction().replace(R.id.frameLayoutLeft,new ShowSelectFragment(),"ROOT()=").commit();
                    fragmentManager.beginTransaction().replace(R.id.frameLayoutRight,frag,topFragmentName().toUpperCase()).commit();
                }
            }
        }
        setMyActionBar();

        ShowSelectFragment showSelectFragment = new ShowSelectFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayoutLeft,showSelectFragment,"ROOT()=").addToBackStack("root()=");

        fragmentTransaction.commit();

    }

    public String topFragmentName(){
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        return fragmentTag;
    }

    private void setMyActionBar() {
        myToolbar = findViewById(R.id.Toolbar);
        myToolbar.inflateMenu(R.menu.menu_episode);
        Menu menu=myToolbar.getMenu();
        menu.findItem(R.id.action_add).setVisible(false);
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add:
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        int containerViewId = R.id.frameLayoutLeft;

                        if(findViewById(R.id.frameLayoutRight)!=null){
                            containerViewId = R.id.frameLayoutRight;
                        }
                        AddEpisodeFragment addEpisodeFragment=AddEpisodeFragment.newInstance(currentShow.getID());
                        fragmentTransaction.setCustomAnimations(R.anim.enter_up,R.anim.exit_up,R.anim.enter_up,R.anim.exit_up).replace(containerViewId,addEpisodeFragment,("add()="+currentShow.getName()).toUpperCase()).addToBackStack("add()="+currentShow.getName());
                        fragmentTransaction.commit();
                        myToolbar.getMenu().findItem(R.id.action_add).setVisible(false);

                        return true;

                    default:
                        // If we got here, the user's action was not recognized.
                        // Invoke the superclass to handle it.
                        return false;
                }
            }
        });
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onShowSelected(int showId) {
        currentShow=ApplicationShows.getShow(showId);
        if(!topFragmentName().equals("root()=")) {
            if(topFragmentName().startsWith("add()=")){
                fragmentManager.popBackStack();
            }
            fragmentManager.popBackStack();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        int containerViewId = R.id.frameLayoutLeft;

        if(findViewById(R.id.frameLayoutRight)!=null) {
            containerViewId = R.id.frameLayoutRight;
        }
        EpisodeSelectFragment episodeSelectFragment = EpisodeSelectFragment.newInstance(showId);
        fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.enter,R.anim.exit).replace(containerViewId,episodeSelectFragment,currentShow.getName().toUpperCase()).addToBackStack(currentShow.getName());

        fragmentTransaction.commit();
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_white_24dp);
        myToolbar.setTitle(currentShow.getName());
        myToolbar.getMenu().findItem(R.id.action_add).setVisible(true);

    }

    private void setNavigationButton() {
        if (!topFragmentName().equals("root()=")){
            myToolbar.setNavigationIcon(R.drawable.ic_arrow_white_24dp);
            myToolbar.setTitle(currentShow.getName());
            myToolbar.getMenu().findItem(R.id.action_add).setVisible(true);
        }else{
            myToolbar.setNavigationIcon(null);
            myToolbar.setTitle("Shows");
            myToolbar.getMenu().findItem(R.id.action_add).setVisible(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (topFragmentName().equals("root()=")){
            finish();
            return;
        }else if(closeDialog){
            closeDialog=false;
            super.onBackPressed();
            setNavigationButton();
        }
        else if (topFragmentName().startsWith("add()=")){
            AddEpisodeFragment frag=(AddEpisodeFragment) fragmentManager.findFragmentByTag(topFragmentName().toUpperCase());
            if (checkIfCanExit(frag)){
                super.onBackPressed();
                setNavigationButton();
            }else{
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Warning");
                alertDialog.setMessage("You have unsaved changes, are you sure you want to exit?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                MainActivity.closeDialog=true;
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
        } else{
            super.onBackPressed();
            setNavigationButton();
        }

    }

    private boolean checkIfCanExit(AddEpisodeFragment frag) {
        Bundle bun=frag.getCurrentFields();
        return bun.getParcelable("uri")==null && bun.getString("episode and season text").equals("Unknown") && bun.getString("episode desc").equals("") && bun.getString("episode name").equals("");
    }

    @Override
    public void onEpisodeAdded(int showId) {
        onShowSelected(showId);
    }

    @Override
    public void onEpisodeSelected(Episode episode) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        int containerViewId = R.id.frameLayoutLeft;

        if(findViewById(R.id.frameLayoutRight)!=null) {
            containerViewId = R.id.frameLayoutRight;
        }
        EpisodeDetailsFragment episodeSelectFragment = EpisodeDetailsFragment.newInstance(episode);
        fragmentTransaction.setCustomAnimations(R.anim.enter_down,R.anim.exit_down,R.anim.enter_down,R.anim.exit_down).replace(containerViewId,episodeSelectFragment,episode.getName().toUpperCase()).addToBackStack(episode.getName());

        fragmentTransaction.commit();
        myToolbar.getMenu().findItem(R.id.action_add).setVisible(false);
    }
}
