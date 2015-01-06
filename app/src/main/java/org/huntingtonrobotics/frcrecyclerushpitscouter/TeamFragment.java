package org.huntingtonrobotics.frcrecyclerushpitscouter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by 2015H_000 on 1/6/2015.
 */
public class TeamFragment extends Fragment {

    private Team mTeam;
    private EditText mTeamNum;

    //fragment is created
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mTeam = new Team();
    }
    //---fragment is created


    //inflates the view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_team, parent, false);

        //Team Number Text View
        mTeamNum = (EditText)v.findViewById(R.id.teamNum);
        mTeamNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //left blank
            }

            //user changes text
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                    //use try catch in case nothing is in text view
                     try {
                         //saves text after converting CS to integer
                         mTeam.setTeamNum(Integer.parseInt(s.toString()));
                     }catch (Exception e) {
                         //exception is thrown so setTeamNum to 0 so program can carry on
                         mTeam.setTeamNum(0);
                     }


            }

            @Override
            public void afterTextChanged(Editable s) {
                //also left blank
            }
        });
        return v;
    }
    //---inflates the view



}
