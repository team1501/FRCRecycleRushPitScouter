package org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.bluetoothChat;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.FRCRecycleRushPitScouterJSONSerializer;
import org.huntingtonrobotics.frcrecyclerushpitscouter.R;
import org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.bluetoothCommon.logger.Log;
import org.json.JSONException;

import java.io.IOException;

//This fragment controls Bluetooth to communicate with other devices.
public class BluetoothChatFragment extends Fragment {
    //for debugging
    private static final String TAG = "BluetoothChatFragment";
    //to put on end of string being sent over bluetooth
    private static final String SUPER_SECRET_PASSWORD = "~~~";
    //for creating string to transfer
    private static final String FILENAME = "teams.json";
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    private String mConversationJSONString;
    //for accessing Serializer to receive JSON String and sending bluetooth JSON String to Serializer
    private FRCRecycleRushPitScouterJSONSerializer mSerializer;
    //for building final string from bluetooth receiver since JSONString was being received in chunks
    private String mFinalString = "";
    // Layout Views
    private ListView mConversationView;
    private Button mSendButton;

    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    //Name of the connected device
    private String mConnectedDeviceName = null;
    //The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            mConversationArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    //receive message from chat service
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer and write it to the text view
                    String writeMessage = new String(writeBuf);
                    mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    //adds the message to the final string
                    mFinalString = mFinalString + readMessage;
                    //check to see if mFinalString has the super secret password at the end of it
                    if ((mFinalString.substring(mFinalString.length()- SUPER_SECRET_PASSWORD.length()).equals(SUPER_SECRET_PASSWORD))){

                        try {
                            //remove the super secret password from mFinalString
                            mFinalString = mFinalString.substring(0, mFinalString.length()- SUPER_SECRET_PASSWORD.length());
                            //print mFinalString out in the TextView
                            mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + mFinalString);
                            //process mFinalString in the Serializer
                            mSerializer.saveBluetoothTeams(mFinalString);
                            //clear mFinalString
                            mFinalString = "";
                            Toast.makeText(activity, "Teams Received. Please close your app and restart to reload teams.", Toast.LENGTH_LONG).show();
                        }catch (Exception e){
                            Log.e(TAG, "ERROR Saving mFinalString: " + e);
                        }
                    }
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
    //Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;
     // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    //Member object for the chat services
    private BluetoothChatService mChatService = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }

        //create Serializer
        mSerializer = new FRCRecycleRushPitScouterJSONSerializer(getActivity(), FILENAME);

        //attempt to receive teams from JSON
        try {
            mConversationJSONString = mSerializer.getJSONString();
        }catch (JSONException je){
            Log.e(TAG, "ERROR! JSONException thrown attempting to retrieve JSONString: "+je);
        }catch (IOException ie){
            Log.e(TAG, "ERROR! IOException thrown attempting to retrieve JSONString: "+ie);
        }
    }

    //Override onStart onDestroy onResume
    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //kill the chat service if it is not null
        if (mChatService != null) {
            mChatService.stop();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }
    //---Override onStart onDestroy onResume

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the layout
        return inflater.inflate(R.layout.fragment_bluetooth_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mConversationView = (ListView) view.findViewById(R.id.in);

        //Initialize the send button with a listener that for click events
        mSendButton = (Button) view.findViewById(R.id.button_send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send the JSONString message
                View view = getView();
                if (null != view) {
                    String message = mConversationJSONString;
                    sendMessage(message);
                }
            }
        });
    }

    //Set up the UI and background operations for chat.
    private void setupChat() {
        Log.i(TAG, "setupChat() has been called");

        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);
        mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(getActivity(), mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    //Makes this device discoverable.
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    //sends a message
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            //add the super secret password to the end of the string so we know when we have recieved the entire string
            message = message + SUPER_SECRET_PASSWORD;
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();

            //send message to chat service to process
            mChatService.write(send);

            // Reset out string buffer to zero
            mOutStringBuffer.setLength(0);
        }
    }

    //Updates the status on the action bar.
    private void setStatus(int resId) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            final ActionBar actionBar = activity.getActionBar();

            if (null == actionBar) {
                return;
            }
            actionBar.setSubtitle(resId);
        }
    }

    //Updates the status on the action bar
    private void setStatus(CharSequence subTitle) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            final ActionBar actionBar = activity.getActionBar();
            if (null == actionBar) {
                return;
            }

            actionBar.setSubtitle(subTitle);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    }

    //Establish connection with other device
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    //menu stuff
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bluetooth_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.insecure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }
            case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }
        }
        return false;
    }

}

