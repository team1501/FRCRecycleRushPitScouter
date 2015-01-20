package org.huntingtonrobotics.frcrecyclerushpitscouter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by 2015H_000 on 1/15/2015.
 */
public class SendFragment extends Fragment {

    private Button mOn,mOff,mVisible,mList;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    private ListView lv;

    //fragment is created
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.send_title);
        setHasOptionsMenu(true);
    }
    //---fragment is created

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        final View v = inflater.inflate(R.layout.fragment_send, parent, false);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            //no parent no carret
            if (NavUtils.getParentActivityName(getActivity())==null) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mOn = (Button)v.findViewById(R.id.blueOn);
        mOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BA.isEnabled()) {
                    Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnOn, 0);
                    Toast.makeText(getActivity().getApplicationContext(), "Turned on"
                            , Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"Already on",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


        mOff = (Button)v.findViewById(R.id.blueOff);
        mOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BA.disable();
                Toast.makeText(getActivity().getApplicationContext(),"Turned off" ,
                        Toast.LENGTH_LONG).show();
            }
        });


        mVisible = (Button)v.findViewById(R.id.blueVisible);
        mVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getVisible = new Intent(BluetoothAdapter.
                        ACTION_REQUEST_DISCOVERABLE);
                startActivityForResult(getVisible, 0);
            }
        });

        mList = (Button)v.findViewById(R.id.blueList);
        mList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevices = BA.getBondedDevices();

                ArrayList list = new ArrayList();
                for(BluetoothDevice bt : pairedDevices)
                    list.add(bt.getName());

                Toast.makeText(getActivity().getApplicationContext(),"Showing Paired Devices",
                        Toast.LENGTH_SHORT).show();
                ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, list);
                lv.setAdapter(adapter);
            }
        });

        lv = (ListView)v.findViewById(R.id.blueListView);

        BA = BluetoothAdapter.getDefaultAdapter();

        return v;
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    */

    //attach extra argument to team fragment
    public static SendFragment newInstance(){
        Bundle args = new Bundle();

        SendFragment fragment = new SendFragment();
        fragment.setArguments(args);

        return fragment;
    }
    //---attach extra argument to team fragment

    //Respond to menu items being clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity())!=null){
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //---Respond to menu items being clicked
}
