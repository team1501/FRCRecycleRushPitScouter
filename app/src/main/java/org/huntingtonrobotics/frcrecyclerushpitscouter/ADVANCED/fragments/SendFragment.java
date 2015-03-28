package org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.TeamLab;
import org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.bluetoothChat.BluetoothMainActivity;
import org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.FRCRecycleRushPitScouterJSONSerializer;
import org.huntingtonrobotics.frcrecyclerushpitscouter.R;
import org.huntingtonrobotics.frcrecyclerushpitscouter.MODIFY.ReportBuilder;
import org.huntingtonrobotics.frcrecyclerushpitscouter.MODIFY.Team;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by 2015H_000 on 1/15/2015.
 */
public class SendFragment extends Fragment {

    private static final String LINE_BREAK = "\n";
    private static final String DOUBLE_LINE_BREAK = "\n\n";
    private static final String SPACE = " ";
    private String m_Text = "";
    private static final String FILENAME = "teams.json";
    //bluetooth
    private Button mBlueSend;

    private Button mJsonString;
    private EditText mJsonCompetition;
    private Button mJsonLoadButton;
    private EditText mJsonLoadString;
    private FRCRecycleRushPitScouterJSONSerializer mSerializer;
    private ReportBuilder mReportBuilder;

    //txt
    private Button mTXTSendOneTeam;
    private EditText mTXTTeam;

    private Button mTXTSendAllTeams;
    private Button mTXTSendMatch;
    private EditText mTXTMatchNum;
    private EditText mTXTMatchTeam1, mTXTMatchTeam2, mTXTMatchTeam3, mTXTMatchTeam4, mTXTMatchTeam5, mTXTMatchTeam6;

    //attach extra argument to team fragment
    public static SendFragment newInstance() {
        Bundle args = new Bundle();

        SendFragment fragment = new SendFragment();
        fragment.setArguments(args);

        return fragment;
    }
    //---fragment is created

    //fragment is created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.send_title);
        setHasOptionsMenu(true);
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //create Serializer
        mSerializer = new FRCRecycleRushPitScouterJSONSerializer(getActivity(), FILENAME);
        mReportBuilder = new ReportBuilder();

    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_send, parent, false);

        mBlueSend = (Button) v.findViewById(R.id.blueSend);
        mBlueSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BluetoothMainActivity.class);
                startActivity(i);
            }
        });



        mJsonString = (Button) v.findViewById(R.id.jsonSend);
        mJsonString.setEnabled(false);
        mJsonString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, mSerializer.getJSONString());
                i.putExtra(Intent.EXTRA_SUBJECT, "JSON String for " + mJsonCompetition.getText().toString());
                startActivity(i);
                    mJsonCompetition.setText("");
                }catch (JSONException je){

                }catch (IOException ie){

                }

            }
        });

        mJsonCompetition = (EditText) v.findViewById(R.id.jsonCompetitionName);
        mJsonCompetition.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mJsonCompetition.getText().toString().equals("")) {
                    mJsonString.setEnabled(false);
                } else {
                    mJsonString.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mJsonLoadButton = (Button) v.findViewById(R.id.jsonLoad);
        mJsonLoadButton.setEnabled(false);
        mJsonLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mSerializer.saveBluetoothTeams(mJsonLoadString.getText().toString());
                    Toast.makeText(getActivity().getApplicationContext(), "Load successful. Please close and reopen app.",Toast.LENGTH_LONG).show();
                }catch (JSONException je){

                }catch (IOException ie){

                }
                mJsonLoadString.setText("");

            }
        });

        mJsonLoadString = (EditText) v.findViewById(R.id.jsonLoadString);
        mJsonLoadString.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mJsonLoadString.getText().toString().equals("")) {
                    mJsonLoadButton.setEnabled(false);
                } else {
                    mJsonLoadButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mTXTSendOneTeam = (Button) v.findViewById(R.id.txtSendOneTeam);
        mTXTSendOneTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((mTXTTeam.isShown())){

                    Team team;
                    int num = 0;
                    try {
                        num = Integer.parseInt(mTXTTeam.getText().toString());
                    }catch (Exception e){

                    }
                    team = TeamLab.get(getActivity().getApplicationContext()).getTeamByNum(num);
                    if (team != null){
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_TEXT, mReportBuilder.getPitScoutReport(team));
                        i.putExtra(Intent.EXTRA_SUBJECT, "Team Report for Team " + num);
                        startActivity(i);
                        mTXTTeam.setText("");
                        mTXTTeam.setVisibility(View.GONE);
                    }else{
                        Vibrator vibrator = (Vibrator) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        vibrator.vibrate(1000);
                        Toast.makeText(getActivity().getApplicationContext(), "Team " + num +" not found",Toast.LENGTH_SHORT).show();
                    }
                    
                }else{
                    mTXTTeam.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity().getApplicationContext(), "Enter any team number your little heart desires and press Send One Team",Toast.LENGTH_LONG).show();
                }
            }
        });

        mTXTTeam = (EditText) v.findViewById(R.id.txtTeam1);
        mTXTTeam.setVisibility(View.GONE);

        mTXTSendAllTeams = (Button)v.findViewById(R.id.txtSendAllTeams);
        mTXTSendAllTeams.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String beginReport = "PIT SCOUT REPORT FOR ALL TEAMS " + "----------->" + DOUBLE_LINE_BREAK;
                String endReport = "<----------- END OF PIT SCOUT REPORT FOR ALL TEAMS" + mTXTMatchNum.getText().toString();

                String report = beginReport;

                ArrayList<Team> mTeams = TeamLab.get(getActivity().getApplicationContext()).getTeams();
                try {
                    for (int i = 0; i < mTeams.size(); i++) {
                        report += mReportBuilder.getPitScoutReport(mTeams.get(i)) + DOUBLE_LINE_BREAK;
                    }
                }catch (ArrayIndexOutOfBoundsException aiobe){

                }

                report += endReport;

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, report);
                i.putExtra(Intent.EXTRA_SUBJECT, "Pit Scout Report for All Teams");
                startActivity(i);

            }
        });

        mTXTMatchNum = (EditText) v.findViewById(R.id.txtMatchNum);
        mTXTMatchNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mTXTMatchNum.getText().toString().equals("")) {
                    mTXTSendMatch.setEnabled(false);
                } else {
                    mTXTSendMatch.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //TXT


        mTXTSendMatch = (Button) v.findViewById(R.id.txtSendMatch);
        mTXTSendMatch.setEnabled(false);
        mTXTSendMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullReport = "";
                String beginReport = "PIT SCOUT REPORT FOR MATCH NUMBER " + mTXTMatchNum.getText().toString() + "----------->";
                String endReport = "<----------- END OF PIT SCOUT REPORT FOR MATCH NUMBER " + mTXTMatchNum.getText().toString();
                Boolean allTeamsFound = true;

                fullReport += beginReport;
                if ((mTXTMatchTeam1.isShown())) {
                    Team team;
                    int num = 0;
                    //1
                    try {
                        num = Integer.parseInt(mTXTMatchTeam1.getText().toString());
                    }catch (Exception e) {
                    }
                     team = TeamLab.get(getActivity().getApplicationContext()).getTeamByNum(num);
                    if (team != null){
                        fullReport += DOUBLE_LINE_BREAK + mReportBuilder.getPitScoutReport(team);


                    }else{
                        teamNotFound(num);
                        allTeamsFound =false;
                    }
                    //2
                    try {
                        num = Integer.parseInt(mTXTMatchTeam2.getText().toString());
                    }catch (Exception e) {
                    }
                    team = TeamLab.get(getActivity().getApplicationContext()).getTeamByNum(num);
                    if (team != null){
                        fullReport += DOUBLE_LINE_BREAK + mReportBuilder.getPitScoutReport(team);


                    }else{
                        teamNotFound(num);
                        allTeamsFound =false;
                    }
                    //3
                    try {
                        num = Integer.parseInt(mTXTMatchTeam3.getText().toString());
                    }catch (Exception e) {
                    }
                    team = TeamLab.get(getActivity().getApplicationContext()).getTeamByNum(num);
                    if (team != null){
                        fullReport += DOUBLE_LINE_BREAK + mReportBuilder.getPitScoutReport(team);


                    }else{
                        teamNotFound(num);
                        allTeamsFound =false;
                    }
                    //4
                    try {
                        num = Integer.parseInt(mTXTMatchTeam4.getText().toString());
                    }catch (Exception e) {
                    }
                    team = TeamLab.get(getActivity().getApplicationContext()).getTeamByNum(num);
                    if (team != null){
                        fullReport += DOUBLE_LINE_BREAK + mReportBuilder.getPitScoutReport(team);

                    }else{
                        teamNotFound(num);
                        allTeamsFound =false;
                    }
                    //5
                    try {
                        num = Integer.parseInt(mTXTMatchTeam5.getText().toString());
                    }catch (Exception e) {
                    }
                    team = TeamLab.get(getActivity().getApplicationContext()).getTeamByNum(num);
                    if (team != null){
                        fullReport += DOUBLE_LINE_BREAK + mReportBuilder.getPitScoutReport(team);

                    }else{
                        teamNotFound(num);
                        allTeamsFound =false;
                    }
                    //6
                    try {
                        num = Integer.parseInt(mTXTMatchTeam6.getText().toString());
                    }catch (Exception e) {
                    }
                    team = TeamLab.get(getActivity().getApplicationContext()).getTeamByNum(num);
                    if (team != null){
                        fullReport += DOUBLE_LINE_BREAK + mReportBuilder.getPitScoutReport(team);

                    }else{
                        teamNotFound(num);
                        allTeamsFound =false;
                    }

                    if(allTeamsFound) {
                        fullReport += DOUBLE_LINE_BREAK + endReport;
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_TEXT, fullReport);
                        i.putExtra(Intent.EXTRA_SUBJECT, "Team Report For Match Number " + mTXTMatchNum.getText().toString());
                        startActivity(i);
                        mTXTMatchNum.setText("");
                        mTXTMatchTeam1.setVisibility(View.GONE);
                        mTXTMatchTeam1.setText("");
                        mTXTMatchTeam2.setVisibility(View.GONE);
                        mTXTMatchTeam2.setText("");
                        mTXTMatchTeam3.setVisibility(View.GONE);
                        mTXTMatchTeam3.setText("");
                        mTXTMatchTeam4.setVisibility(View.GONE);
                        mTXTMatchTeam4.setText("");
                        mTXTMatchTeam5.setVisibility(View.GONE);
                        mTXTMatchTeam5.setText("");
                        mTXTMatchTeam6.setVisibility(View.GONE);
                        mTXTMatchTeam6.setText("");
                    }else{

                        mTXTMatchTeam1.setVisibility(View.VISIBLE);
                        mTXTMatchTeam2.setVisibility(View.VISIBLE);
                        mTXTMatchTeam3.setVisibility(View.VISIBLE);
                        mTXTMatchTeam4.setVisibility(View.VISIBLE);
                        mTXTMatchTeam5.setVisibility(View.VISIBLE);
                        mTXTMatchTeam6.setVisibility(View.VISIBLE);
                    }
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Enter any team numbers your little heart desires and press Send Match",Toast.LENGTH_LONG).show();
                    mTXTMatchTeam1.setVisibility(View.VISIBLE);
                    mTXTMatchTeam2.setVisibility(View.VISIBLE);
                    mTXTMatchTeam3.setVisibility(View.VISIBLE);
                    mTXTMatchTeam4.setVisibility(View.VISIBLE);
                    mTXTMatchTeam5.setVisibility(View.VISIBLE);
                    mTXTMatchTeam6.setVisibility(View.VISIBLE);
                }

            }
        });
        mTXTMatchTeam1 = (EditText) v.findViewById(R.id.txtMatchTeam1);
        mTXTMatchTeam1.setVisibility(View.GONE);

        mTXTMatchTeam2 = (EditText) v.findViewById(R.id.txtMatchTeam2);
        mTXTMatchTeam2.setVisibility(View.GONE);

        mTXTMatchTeam3 = (EditText) v.findViewById(R.id.txtMatchTeam3);
        mTXTMatchTeam3.setVisibility(View.GONE);

        mTXTMatchTeam4 = (EditText) v.findViewById(R.id.txtMatchTeam4);
        mTXTMatchTeam4.setVisibility(View.GONE);

        mTXTMatchTeam5 = (EditText) v.findViewById(R.id.txtMatchTeam5);
        mTXTMatchTeam5.setVisibility(View.GONE);

        mTXTMatchTeam6 = (EditText) v.findViewById(R.id.txtMatchTeam6);
        mTXTMatchTeam6.setVisibility(View.GONE);


        return v;
    }
    //---attach extra argument to team fragment

    //Respond to menu items being clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //---Respond to menu items being clicked

    private void teamNotFound(int num){
        Vibrator vibrator = (Vibrator) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().VIBRATOR_SERVICE);
        // Vibrate for 1 second
        vibrator.vibrate(1000);
        Toast.makeText(getActivity().getApplicationContext(), "Team " + num +" not found",Toast.LENGTH_SHORT).show();
    }
}
