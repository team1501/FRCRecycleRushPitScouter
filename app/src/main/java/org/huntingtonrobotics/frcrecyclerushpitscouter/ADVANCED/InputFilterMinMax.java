

package org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;


//from http://stackoverflow.com/questions/14212518/is-there-any-way-to-define-a-min-and-max-value-for-edittext-in-android

public class InputFilterMinMax implements InputFilter {
    private static final String TAG = "TeamListFragment";

    private int min, max;
    private Context mContext;


    public InputFilterMinMax(int min, int max, Context c) {
        this.min = min;
        this.max = max;
        this.mContext = c;
    }

    public InputFilterMinMax(String min, String max) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());

            if (isInRange(min, max, input)) {
                return null;

            }
        } catch (NumberFormatException nfe) { }

            //creates a toast telling user that they entered an invalid number

            /*FIX: toast pops up when switching teams
            CharSequence text = "Nice try hot shot. Number must be between " + min + " and " + max + ".";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(mContext, text, duration);
            toast.show();
            */

        return "";

    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}