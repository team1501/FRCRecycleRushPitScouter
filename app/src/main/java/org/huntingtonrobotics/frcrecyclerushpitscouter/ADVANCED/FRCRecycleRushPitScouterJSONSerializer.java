package org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED;

/* Saving and loading Local Files
*allow criminal intent to save and load data from a JSON file stored on the device's filesystem
* Each app has a directory in its sandbox
* keeping files in the sandbox protects them from being accessed by other apps and users
* app can also store files in external storage
* EXP SD Card
* chapter focuses on internal storage

 */

/*Saving and Loading Data in CI
*Involves two proccess:
* *to save data into a storable format then write that data to a file
* * In loading first read the formated data and then parse it into what the app needs
 */

import android.content.Context;

import org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.bluetoothCommon.logger.Log;
import org.huntingtonrobotics.frcrecyclerushpitscouter.MODIFY.Team;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by 2015H_000 on 12/24/2014.
 */

public class FRCRecycleRushPitScouterJSONSerializer {
    private static final String TAG = "FRCRecycleRushPitScouterJSONSerializer";
    private Context mContext;
    private String mFileName;
    ArrayList<Team> mTeams = new ArrayList<Team>();


    public FRCRecycleRushPitScouterJSONSerializer(Context c, String f) {
        mContext = c;
        mFileName = f;
    }

    //load teams from file system
    public ArrayList<Team> loadTeams() throws IOException, JSONException {
        BufferedReader reader = null;
        try {
            //open and read the file into a stringBuilder
            InputStream in = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                //line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            //parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            //build the array of teams from JSONObjects

            for (int i = 0; i < array.length(); i++) {
                mTeams.add(new Team(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            //ignore
        } finally {
            //ensures the underlying file handle is freed up
            if (reader != null)
                reader.close();
        }
        return mTeams;
    }

    public void saveTeams(ArrayList<Team> t) throws JSONException, IOException{
        //Build an array in JSON
        JSONArray array = new JSONArray();

        for (Team c : t) {
            array.put(c.toJSON());
        }

        //write file to disk
        Writer writer = null;
        try{
            OutputStream out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally{
            if (writer!= null)
                writer.close();
        }
        mTeams = t;
    }

    //returns string version of json file
    public String getJSONString() throws JSONException, IOException{
        //Build an array in JSON
        JSONArray array = new JSONArray();
        loadTeams();
        for (Team c : mTeams) {
            array.put(c.toJSON());
        }
        return array.toString();
    }

    //load teams from file system
    public Boolean saveBluetoothTeams(String s) throws IOException, JSONException {
        BufferedReader reader = null;
        try {
            //parse the JSON using JSONTokener
            JSONArray jsonArray = new JSONArray(s);
            ArrayList<Team> bluetoothListData = new ArrayList<Team>();
            ArrayList<Team> bluetoothListDataToAdd = new ArrayList<Team>();

            //build the array of teams from JSONObjects from bluetooth
            for (int i = 0; i < jsonArray.length(); i++) {
                bluetoothListData.add(new Team(jsonArray.getJSONObject(i)));
            }

            mTeams.clear();
            loadTeams();
            //loops through list data
            for (int k = 0; k < bluetoothListData.size(); k++) {
                Boolean duplicate = false;
                //loops through list teams
                for(int j = 0; j < mTeams.size(); j++) {
                    if (mTeams.get(j).getTeamNum() == bluetoothListData.get(k).getTeamNum()) {
                        duplicate = true;
                    }
                }
                if(!duplicate){
                try {
                    bluetoothListDataToAdd.add(bluetoothListData.get(k));
                }catch (IndexOutOfBoundsException iobe){
                    //end try
                }
                }
            }

            mTeams.addAll(bluetoothListDataToAdd);
            saveTeams(mTeams);

            return true;

        }catch (FileNotFoundException e) {
            //ignore
            return false;
        }catch (Exception e){
            Log.e(TAG, "Error: " + e);
            return false;

        }finally {
            //ensures the underlying file handle is freed up
            if (reader != null)
                reader.close();
        }
    }
}

