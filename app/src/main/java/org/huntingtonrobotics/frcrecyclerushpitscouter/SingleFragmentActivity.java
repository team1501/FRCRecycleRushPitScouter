/*
*Generic code for creating a single fragment
* Nearly every activity created in this book will require the same code
 */
package org.huntingtonrobotics.frcrecyclerushpitscouter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by 2015H_000 on 12/12/2014.
 */
public abstract class SingleFragmentActivity extends ActionBarActivity {
    //abstract method allows you to instantiate the fragment and return an instance of the fragment that the activity is hosting
    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //builds activity_fragment.xml
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment); //changed from activity_crime to activity_fragment

        //comes from import android.support.v4.app.FragmentManager
        FragmentManager fm = getSupportFragmentManager();

        //comes from import android.support.v4.app.Fragment
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);


        /*Summary
        *ask the FragmentManager for the fragment with a container view ID of R.id.fragment container
        *If fragment is already in the list then fragment manager will return it (in case CrimeActivity is being recreated after onDestroy()
        *If no fragment then create a new CrimeFragment and a new fragment transaction adds the fragment to the list
        */

        if (fragment==null){
            //gets CrimeFragment() and puts into fragment
            fragment = createFragment(); //new CrimeFragment is not generic

            /*Container view ID serves two purposes:
            * It tells the Fragment Manager where in the activity's view where the fragment's view should appear
            * It is used as a unique identifier for a fragment in the FragmentManager's List
             */

            //create a new fragment transaction, include one add operation in it, and commit it
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }

    }

}
