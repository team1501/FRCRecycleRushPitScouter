package org.huntingtonrobotics.frcrecyclerushpitscouter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.huntingtonrobotics.frcrecyclerushpitscouter.bluetoothchat.BluetoothMainActivity;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by 2015H_000 on 1/15/2015.
 */
public class SendFragment extends Fragment {
    private static final String LINE_BREAK = "\n";
    private static final String DOUBLE_LINE_BREAK = "\n\n";
    private static final String SPACE = " ";
    private String m_Text = "";
    //bluetooth
    private Button mBlueOn, mBlueOff, mBlueVisible, mBlueList, mBlueSend;
    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private ListView mPairedDevicesList;

    //sms
    private Button mSMSendOneTeam;
    private EditText mSMSTeam;

    private Button mSMSendAllTeams;
    private Button mSMSendMatch;
    private EditText mSMSMatchNum;
    private EditText mSMSMatchTeam1, mSMSMatchTeam2, mSMSMatchTeam3, mSMSMatchTeam4, mSMSMatchTeam5, mSMSMatchTeam6;

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

        mSMSendOneTeam = (Button) v.findViewById(R.id.smsSendOneTeam);
        mSMSendOneTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((mSMSTeam.isShown())){

                    Team team;
                    int num = 0;
                    try {
                        num = Integer.parseInt(mSMSTeam.getText().toString());
                    }catch (Exception e){

                    }
                    team = TeamLab.get(getActivity().getApplicationContext()).getTeamByNum(num);
                    if (team != null){
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_TEXT, buildReport(team));
                        i.putExtra(Intent.EXTRA_SUBJECT, "Team Report for Team " + num);
                        startActivity(i);
                        mSMSTeam.setText("");
                        mSMSTeam.setVisibility(View.GONE);
                    }else{
                        Vibrator vibrator = (Vibrator) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        vibrator.vibrate(1000);
                        Toast.makeText(getActivity().getApplicationContext(), "Team " + num +" not found",Toast.LENGTH_SHORT).show();
                    }
                    
                }else{
                    mSMSTeam.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity().getApplicationContext(), "Enter any team number your little heart desires and press Send One Team",Toast.LENGTH_LONG).show();
                }
            }
        });

        mSMSTeam = (EditText) v.findViewById(R.id.smsTeam1);
        mSMSTeam.setVisibility(View.GONE);

        mSMSendAllTeams = (Button)v.findViewById(R.id.smsSendAllTeams);
        mSMSendAllTeams.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String beginReport = "PIT SCOUT REPORT FOR ALL TEAMS " + "----------->" + DOUBLE_LINE_BREAK;
                String endReport = "<----------- END OF PIT SCOUT REPORT FOR ALL TEAMS" + mSMSMatchNum.getText().toString();

                String report = beginReport;

                ArrayList<Team> mTeams = TeamLab.get(getActivity().getApplicationContext()).getTeams();
                try {
                    for (int i = 0; i < mTeams.size(); i++) {
                        report = report + buildReport(mTeams.get(i)) + DOUBLE_LINE_BREAK;
                    }
                }catch (ArrayIndexOutOfBoundsException aiobe){

                }

                report = report + endReport;

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, report);
                i.putExtra(Intent.EXTRA_SUBJECT, "Pit Scout Report for All Teams");
                startActivity(i);

            }
        });

        mSMSMatchNum = (EditText) v.findViewById(R.id.smsMatchNum);
        mSMSMatchNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mSMSMatchNum.getText().toString().equals("")) {
                    mSMSendMatch.setEnabled(false);
                } else {
                    mSMSendMatch.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //SMS


        mSMSendMatch = (Button) v.findViewById(R.id.smsSendMatch);
        mSMSendMatch.setEnabled(false);
        mSMSendMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullReport = "";
                String beginReport = "PIT SCOUT REPORT FOR MATCH NUMBER " + mSMSMatchNum.getText().toString() + "----------->";
                String endReport = "<----------- END OF PIT SCOUT REPORT FOR MATCH NUMBER " + mSMSMatchNum.getText().toString();
                Boolean allTeamsFound = true;

                fullReport += beginReport;
                if ((mSMSMatchTeam1.isShown())) {
                    Team team;
                    int num = 0;
                    //1
                    try {
                        num = Integer.parseInt(mSMSMatchTeam1.getText().toString());
                    }catch (Exception e) {
                    }
                     team = TeamLab.get(getActivity().getApplicationContext()).getTeamByNum(num);
                    if (team != null){
                        fullReport += DOUBLE_LINE_BREAK + buildReport(team);
                        mSMSMatchTeam1.setText("");
                        mSMSMatchTeam1.setVisibility(View.GONE);
                    }else{
                        teamNotFound(num);
                        allTeamsFound =false;
                    }
                    //2
                    try {
                        num = Integer.parseInt(mSMSMatchTeam2.getText().toString());
                    }catch (Exception e) {
                    }
                    team = TeamLab.get(getActivity().getApplicationContext()).getTeamByNum(num);
                    if (team != null){
                        fullReport += DOUBLE_LINE_BREAK + buildReport(team);
                        mSMSMatchTeam2.setText("");
                        mSMSMatchTeam2.setVisibility(View.GONE);
                    }else{
                        teamNotFound(num);
                        allTeamsFound =false;
                    }
                    //3
                    try {
                        num = Integer.parseInt(mSMSMatchTeam3.getText().toString());
                    }catch (Exception e) {
                    }
                    team = TeamLab.get(getActivity().getApplicationContext()).getTeamByNum(num);
                    if (team != null){
                        fullReport += DOUBLE_LINE_BREAK + buildReport(team);
                        mSMSMatchTeam3.setText("");
                        mSMSMatchTeam3.setVisibility(View.GONE);
                    }else{
                        teamNotFound(num);
                        allTeamsFound =false;
                    }
                    //4
                    try {
                        num = Integer.parseInt(mSMSMatchTeam4.getText().toString());
                    }catch (Exception e) {
                    }
                    team = TeamLab.get(getActivity().getApplicationContext()).getTeamByNum(num);
                    if (team != null){
                        fullReport += DOUBLE_LINE_BREAK + buildReport(team);
                        mSMSMatchTeam4.setText("");
                        mSMSMatchTeam4.setVisibility(View.GONE);
                    }else{
                        teamNotFound(num);
                        allTeamsFound =false;
                    }
                    //5
                    try {
                        num = Integer.parseInt(mSMSMatchTeam5.getText().toString());
                    }catch (Exception e) {
                    }
                    team = TeamLab.get(getActivity().getApplicationContext()).getTeamByNum(num);
                    if (team != null){
                        fullReport += DOUBLE_LINE_BREAK + buildReport(team);
                        mSMSMatchTeam5.setText("");
                        mSMSMatchTeam5.setVisibility(View.GONE);
                    }else{
                        teamNotFound(num);
                        allTeamsFound =false;
                    }
                    //6
                    try {
                        num = Integer.parseInt(mSMSMatchTeam6.getText().toString());
                    }catch (Exception e) {
                    }
                    team = TeamLab.get(getActivity().getApplicationContext()).getTeamByNum(num);
                    if (team != null){
                        fullReport += DOUBLE_LINE_BREAK + buildReport(team);
                        mSMSMatchTeam6.setText("");
                        mSMSMatchTeam6.setVisibility(View.GONE);
                    }else{
                        teamNotFound(num);
                        allTeamsFound =false;
                    }

                    if(allTeamsFound){
                        fullReport += DOUBLE_LINE_BREAK + endReport;
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_TEXT, fullReport);
                        i.putExtra(Intent.EXTRA_SUBJECT, "Team Report For Match Number " + mSMSMatchNum.getText().toString());
                        startActivity(i);
                        mSMSMatchNum.setText("");

                    }else{

                        mSMSMatchTeam1.setVisibility(View.VISIBLE);
                        mSMSMatchTeam2.setVisibility(View.VISIBLE);
                        mSMSMatchTeam3.setVisibility(View.VISIBLE);
                        mSMSMatchTeam4.setVisibility(View.VISIBLE);
                        mSMSMatchTeam5.setVisibility(View.VISIBLE);
                        mSMSMatchTeam6.setVisibility(View.VISIBLE);
                    }
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Enter any team numbers your little heart desires and press Send Match",Toast.LENGTH_LONG).show();
                    mSMSMatchTeam1.setVisibility(View.VISIBLE);
                    mSMSMatchTeam2.setVisibility(View.VISIBLE);
                    mSMSMatchTeam3.setVisibility(View.VISIBLE);
                    mSMSMatchTeam4.setVisibility(View.VISIBLE);
                    mSMSMatchTeam5.setVisibility(View.VISIBLE);
                    mSMSMatchTeam6.setVisibility(View.VISIBLE);
                }

            }
        });
        mSMSMatchTeam1 = (EditText) v.findViewById(R.id.smsMatchTeam1);
        mSMSMatchTeam1.setVisibility(View.GONE);

        mSMSMatchTeam2 = (EditText) v.findViewById(R.id.smsMatchTeam2);
        mSMSMatchTeam2.setVisibility(View.GONE);

        mSMSMatchTeam3 = (EditText) v.findViewById(R.id.smsMatchTeam3);
        mSMSMatchTeam3.setVisibility(View.GONE);

        mSMSMatchTeam4 = (EditText) v.findViewById(R.id.smsMatchTeam4);
        mSMSMatchTeam4.setVisibility(View.GONE);

        mSMSMatchTeam5 = (EditText) v.findViewById(R.id.smsMatchTeam5);
        mSMSMatchTeam5.setVisibility(View.GONE);

        mSMSMatchTeam6 = (EditText) v.findViewById(R.id.smsMatchTeam6);
        mSMSMatchTeam6.setVisibility(View.GONE);


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

    private String buildReport(Team t) {
        /*
        String beginReport = "PIT SCOUT DATA FOR MATCH NUMBER " + mSMSMatchNum.getText().toString() + "----------->";
        String endReport = "<----------- END OF PIT SCOUT REPORT FOR MATCH NUMBER " + mSMSMatchNum.getText().toString();

        String report = beginReport;
        */
        
        String report = "";
        //for (int i = 0; i < mTeams.size(); i++) {
            try {
                String teamNum = "" + t.getTeamNum();
                report = report +"---"+ getResources().getString(R.string.sms_team_num) + SPACE + teamNum+"---";


                //mech
                report = report + DOUBLE_LINE_BREAK + getResources().getString(R.string.sms_has_mech);

                ArrayList<String> mechHas = new ArrayList<String>();
                ArrayList<String> mechDNH = new ArrayList<String>();

                if (t.isMechLitterPusher()) {
                    mechHas.add("" + getResources().getString(R.string.sms_litter_pusher));
                } else {
                    mechDNH.add("" + getResources().getString(R.string.sms_litter_pusher));
                }
                if (t.isMechLitterInserter()) {
                    mechHas.add("" + getResources().getString(R.string.sms_litter_inserter));
                } else {
                    mechDNH.add("" + getResources().getString(R.string.sms_litter_inserter));
                }

                if (t.isMechToteFeeder()) {
                    mechHas.add("" + getResources().getString(R.string.sms_tote_feeder));
                } else {
                    mechDNH.add("" + getResources().getString(R.string.sms_tote_feeder));
                }

                if (t.isMechContainerFlipper()) {
                    mechHas.add("" + getResources().getString(R.string.sms_container_flipper));
                } else {
                    mechDNH.add("" + getResources().getString(R.string.sms_container_flipper));
                }
                if (t.isMechContainerStepRemover()) {
                    mechHas.add("" + getResources().getString(R.string.sms_container_step_remover));
                } else {
                    mechDNH.add("" + getResources().getString(R.string.sms_container_step_remover));
                }

                for (int m = 0; m < mechHas.size(); m++) {
                    try {
                        report = report + LINE_BREAK + mechHas.get(m);
                    } catch (ArrayIndexOutOfBoundsException aiobe) {

                    }
                }

                report = report + DOUBLE_LINE_BREAK + getResources().getString(R.string.sms_dnh_mech);

                for (int d = 0; d < mechDNH.size(); d++) {
                    try {
                        report = report + LINE_BREAK + mechDNH.get(d);
                    } catch (ArrayIndexOutOfBoundsException aiobe) {

                    }
                }

                //auto
                report = report + DOUBLE_LINE_BREAK + getResources().getString(R.string.sms_auto);

                report = report + DOUBLE_LINE_BREAK + getResources().getString(R.string.sms_auto_progs) + SPACE + t.getAutoProgs();

                if (t.isAutoMove()) {
                    report = report + LINE_BREAK + getResources().getString(R.string.sms_auto_can_move_az);
                } else {
                    report = report + LINE_BREAK + getResources().getString(R.string.sms_auto_can_not_move_az);
                }

                report = report + LINE_BREAK + getResources().getString(R.string.sms_auto_totes) + SPACE + t.getAutoTotes();

                report = report + LINE_BREAK + getResources().getString(R.string.sms_auto_conatiners) + SPACE + t.getAutoContainers();

                if (t.isAutoMoveTote()) {
                    report = report + LINE_BREAK + getResources().getString(R.string.sms_auto_can_move_tote_stack);
                } else {
                    report = report + LINE_BREAK + getResources().getString(R.string.sms_auto_can_not_move_tote_stack);
                }

                report = report + LINE_BREAK + getResources().getString(R.string.sms_auto_pos) + SPACE + t.getAutoPos();


                //teleop
                report = report + DOUBLE_LINE_BREAK + getResources().getString(R.string.sms_tele);

                report = report + DOUBLE_LINE_BREAK + getResources().getString(R.string.sms_tele_totes) + SPACE + t.getTeleTotes();
                report = report + LINE_BREAK + getResources().getString(R.string.sms_tele_conatiner) + SPACE + t.getTeleContainer();
                report = report + LINE_BREAK + getResources().getString(R.string.sms_tele_litter) + SPACE + t.getTeleContainer();
                report = report + LINE_BREAK + getResources().getString(R.string.sms_tele_tote_stack) + SPACE + t.getTelePlaceTotes();
                report = report + LINE_BREAK + getResources().getString(R.string.sms_tele_carry_totes) + SPACE + t.getTeleCarryTotes();

                report = report + LINE_BREAK + getResources().getString(R.string.sms_tele_human_station) + SPACE + t.getTeleHumanStation();
                report = report + LINE_BREAK + getResources().getString(R.string.sms_tele_platfrom) + SPACE + t.getTelePlatform();

            } catch (ArrayIndexOutOfBoundsException aiobe) {

            }
        //}


        return report;
    }

    private void teamNotFound(int num){
        Vibrator vibrator = (Vibrator) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        vibrator.vibrate(1000);
        Toast.makeText(getActivity().getApplicationContext(), "Team " + num +" not found",Toast.LENGTH_SHORT).show();
    }
}
