package org.huntingtonrobotics.frcrecyclerushpitscouter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class TeamActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        //Add a CrimeFragment
        FragmentManager fm = getSupportFragmentManager();
        //fragmentContainer is activity_team.xml
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        //fragment transaction adds fragmentContainer
        if(fragment == null){
            fragment = new TeamFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }

}
