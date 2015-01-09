package org.huntingtonrobotics.frcrecyclerushpitscouter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by 2015H_000 on 1/8/2015.
 */
public class RobotCameraActivity extends SingleFragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        //hide window title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //hide the status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
    }
    @Override
    protected Fragment createFragment(){
        return new RobotCameraFragment();
    }
}
