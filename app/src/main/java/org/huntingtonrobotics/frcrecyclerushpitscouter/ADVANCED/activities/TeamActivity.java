package org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.activities;

import android.support.v4.app.Fragment;


import org.huntingtonrobotics.frcrecyclerushpitscouter.MODIFY.TeamFragment;

import java.util.UUID;


public class TeamActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        UUID teamId = (UUID) getIntent().getSerializableExtra(TeamFragment.EXTRA_TEAM_ID);
        return TeamFragment.newInstance(teamId);
    }
}
