package org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.activities;

import android.support.v4.app.Fragment;

import org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.fragments.TeamListFragment;

/**
 * Created by 2015H_000 on 1/6/2015.
 */
public class TeamListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return new TeamListFragment();
    }
}
