package com.example.abdullah.mytodolistapp;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<Atask> myTasks = new ArrayList<Atask>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Add Fragment Manager
        android.app.FragmentManager myFragManager = getFragmentManager();
        //Setup Transaction
        android.app.FragmentTransaction myFragTransaction = myFragManager.beginTransaction();
        //let's display fragments
        Configuration configInfo = getResources().getConfiguration();
        if (configInfo.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            FragmentLandscape myLandscapeFragment = new FragmentLandscape();
            myFragTransaction.replace(R.id.main_activity_layout, myLandscapeFragment);
            myLandscapeFragment.setRetainInstance(true);
        }
        else
        {
            FragmentPortrait myPortraitFragment = new FragmentPortrait();
            myFragTransaction.replace(R.id.main_activity_layout, myPortraitFragment);
            myPortraitFragment.setRetainInstance(true);
        }
        myFragTransaction.commit();

    }
}
