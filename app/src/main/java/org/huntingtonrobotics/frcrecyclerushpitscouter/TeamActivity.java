package org.huntingtonrobotics.frcrecyclerushpitscouter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class TeamActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new TeamFragment();
    }

}
