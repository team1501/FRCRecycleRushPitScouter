package org.huntingtonrobotics.frcrecyclerushpitscouter.common.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import org.huntingtonrobotics.frcrecyclerushpitscouter.common.logger.Log;
import org.huntingtonrobotics.frcrecyclerushpitscouter.common.logger.LogWrapper;

/**
 * Base launcher activity, to handle most of the common plumbing for samples.
 */
public class SampleActivityBase extends ActionBarActivity {

    public static final String TAG = "SampleActivityBase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeLogging();
    }

    /**
     * Set up targets to receive log data
     */
    public void initializeLogging() {
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        // Wraps Android's native log framework
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);

        Log.i(TAG, "Ready");
    }
}
