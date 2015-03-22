//http://www.tutorialspoint.com/android/android_bluetooth.htm

package org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.activities;

import android.support.v4.app.Fragment;

import org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.fragments.SendFragment;

public class SendActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return SendFragment.newInstance();
    }
}
