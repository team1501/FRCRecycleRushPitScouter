package org.huntingtonrobotics.frcrecyclerushpitscouter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
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

        /*
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            //no parent no carret
            if (NavUtils.getParentActivityName(getActivity())==null) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mBlueOn = (Button)v.findViewById(R.id.blueOn);
        mBlueOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnOn, 0);
                    Toast.makeText(getActivity().getApplicationContext(), "Turned on"
                            , Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Already on",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


        mBlueOff = (Button)v.findViewById(R.id.blueOff);
        mBlueOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothAdapter.disable();
                Toast.makeText(getActivity().getApplicationContext(), "Turned off",
                        Toast.LENGTH_LONG).show();
            }
        });


        mBlueVisible = (Button)v.findViewById(R.id.blueVisible);
        mBlueVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getVisible = new Intent(BluetoothAdapter.
                        ACTION_REQUEST_DISCOVERABLE);
                startActivityForResult(getVisible, 0);
            }
        });

        mBlueList = (Button)v.findViewById(R.id.blueList);
        mBlueList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPairedDevices = mBluetoothAdapter.getBondedDevices();

                ArrayList list = new ArrayList();
                for (BluetoothDevice bt : mPairedDevices)
                    list.add(bt.getName());

                Toast.makeText(getActivity().getApplicationContext(), "Showing Paired Devices",
                        Toast.LENGTH_SHORT).show();
                ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
                mPairedDevicesList.setAdapter(adapter);
            }
        });

        mPairedDevicesList = (ListView)v.findViewById(R.id.blueListView);
        */

        mBlueSend = (Button) v.findViewById(R.id.blueSend);
        mBlueSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BluetoothMainActivity.class);
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
        mSMSendMatch.setEnabled(true);
        mSMSendMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSMSMatchTeam1.setVisibility(View.VISIBLE);
                mSMSMatchTeam2.setVisibility(View.VISIBLE);
                mSMSMatchTeam3.setVisibility(View.VISIBLE);
                mSMSMatchTeam4.setVisibility(View.VISIBLE);
                mSMSMatchTeam5.setVisibility(View.VISIBLE);
                mSMSMatchTeam6.setVisibility(View.VISIBLE);

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, buildReport());
                i.putExtra(Intent.EXTRA_SUBJECT, "Team Report Subject");
                startActivity(i);
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

    private String buildReport() {

        ArrayList<Team> mTeams = TeamLab.get(getActivity().getApplicationContext()).getTeams();
        String beginReport = "PIT SCOUT DATA FOR MATCH NUMBER " + mSMSMatchNum.getText().toString() + "----------->";
        String endReport = "<----------- END OF PIT SCOUT REPORT FOR MATCH NUMBER " + mSMSMatchNum.getText().toString();

        String report = beginReport;
        for (int i = 0; i < mTeams.size(); i++) {
            try {
                String teamNum = "" + mTeams.get(i).getTeamNum();
                report = report + DOUBLE_LINE_BREAK + getResources().getString(R.string.sms_team_num) + teamNum;

                //mech
                report = report + DOUBLE_LINE_BREAK + getResources().getString(R.string.sms_has_mech);

                ArrayList<String> mechHas = new ArrayList<String>();
                ArrayList<String> mechDNH = new ArrayList<String>();

                if (mTeams.get(i).isMechLitterPusher()) {
                    mechHas.add("" + getResources().getString(R.string.sms_litter_pusher));
                } else {
                    mechDNH.add("" + getResources().getString(R.string.sms_litter_pusher));
                }
                if (mTeams.get(i).isMechLitterInserter()) {
                    mechHas.add("" + getResources().getString(R.string.sms_litter_inserter));
                } else {
                    mechDNH.add("" + getResources().getString(R.string.sms_litter_inserter));
                }

                if (mTeams.get(i).isMechToteFeeder()) {
                    mechHas.add("" + getResources().getString(R.string.sms_tote_feeder));
                } else {
                    mechDNH.add("" + getResources().getString(R.string.sms_tote_feeder));
                }

                if (mTeams.get(i).isMechContainerFlipper()) {
                    mechHas.add("" + getResources().getString(R.string.sms_container_flipper));
                } else {
                    mechDNH.add("" + getResources().getString(R.string.sms_container_flipper));
                }
                if (mTeams.get(i).isMechContainerStepRemover()) {
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

                report = report + DOUBLE_LINE_BREAK + getResources().getString(R.string.sms_auto_progs) + SPACE + mTeams.get(i).getAutoProgs();

                if (mTeams.get(i).isAutoMove()) {
                    report = report + LINE_BREAK + getResources().getString(R.string.sms_auto_can_move_az);
                } else {
                    report = report + LINE_BREAK + getResources().getString(R.string.sms_auto_can_not_move_az);
                }

                report = report + LINE_BREAK + getResources().getString(R.string.sms_auto_totes) + SPACE + mTeams.get(i).getAutoTotes();

                report = report + LINE_BREAK + getResources().getString(R.string.sms_auto_conatiners) + SPACE + mTeams.get(i).getAutoContainers();

                if (mTeams.get(i).isAutoMoveTote()) {
                    report = report + LINE_BREAK + getResources().getString(R.string.sms_auto_can_move_tote_stack);
                } else {
                    report = report + LINE_BREAK + getResources().getString(R.string.sms_auto_can_not_move_tote_stack);
                }

                report = report + LINE_BREAK + getResources().getString(R.string.sms_auto_pos) + SPACE + mTeams.get(i).getAutoPos();


                //teleop
                report = report + DOUBLE_LINE_BREAK + getResources().getString(R.string.sms_tele);

                report = report + DOUBLE_LINE_BREAK + getResources().getString(R.string.sms_tele_totes) + SPACE + mTeams.get(i).getTeleTotes();
                report = report + LINE_BREAK + getResources().getString(R.string.sms_tele_conatiner) + SPACE + mTeams.get(i).getTeleContainer();
                report = report + LINE_BREAK + getResources().getString(R.string.sms_tele_litter) + SPACE + mTeams.get(i).getTeleContainer();
                report = report + LINE_BREAK + getResources().getString(R.string.sms_tele_tote_stack) + SPACE + mTeams.get(i).getTelePlaceTotes();
                report = report + LINE_BREAK + getResources().getString(R.string.sms_tele_carry_totes) + SPACE + mTeams.get(i).getTeleCarryTotes();

                report = report + LINE_BREAK + getResources().getString(R.string.sms_tele_human_station) + SPACE + mTeams.get(i).getTeleHumanStation();
                report = report + LINE_BREAK + getResources().getString(R.string.sms_tele_platfrom) + SPACE + mTeams.get(i).getTelePlatform();

            } catch (ArrayIndexOutOfBoundsException aiobe) {

            }
        }


        return report;
    }
}
