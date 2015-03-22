package org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.TeamLab;
import org.huntingtonrobotics.frcrecyclerushpitscouter.R;
import org.huntingtonrobotics.frcrecyclerushpitscouter.MODIFY.Team;
import org.huntingtonrobotics.frcrecyclerushpitscouter.MODIFY.TeamFragment;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by 2015H_000 on 12/15/2014.
 * added in chapter 10
 * It will create and manage the ViewPager
 */
public class TeamPagerActivity extends ActionBarActivity {
    private ViewPager mViewPager;
    private ArrayList<Team> mTeams;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        //get ArrayList of Teams from TeamLab
        mTeams = TeamLab.get(this).getTeams();

        //get the activity's instance of FragmentManager
        FragmentManager fm = getSupportFragmentManager();


        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
           //returns how many items there are in the array list
            @Override
            public int getCount() {
                return mTeams.size();
            }

            //fetches the Team instance for the given position in the dataset
            @Override
            public Fragment getItem(int pos) {
                Team Team = mTeams.get(pos);
                //uses that Team's ID to create and return a properly configured TeamFragment
                return TeamFragment.newInstance(Team.getID());
            }


        });

        //replace TeamPagerActivity's title on action bar with the title of the current Team
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            //tells whether the page animation is being actively dragged
            public void onPageScrollStateChanged(int state){}

            //telss you exactly whre page is going to be
            public void onPageScrolled(int pos, float posOffset, int posOffsetPixels){}

            //only interested in which page is currently selected
            public void onPageSelected(int pos){
                Team Team = mTeams.get(pos);
                try{
                    setTitle(""+Team.getTeamNum());
                }catch (Exception e){
                    setTitle(e.toString());
                }

            }
        });

        //find the index of the Team to display by looping and checking each Team's ID
        UUID TeamId = (UUID)getIntent().getSerializableExtra(TeamFragment.EXTRA_TEAM_ID);
        for (int i = 0; i < mTeams.size(); i++){
            if (mTeams.get(i).getID().equals(TeamId)){
                //set ViewPager's current item to the index of the selected Team
                mViewPager.setCurrentItem(i);
                break;
            }
        }





    }


}
