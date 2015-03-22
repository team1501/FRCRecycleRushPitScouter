package org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.robotPhoto;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 2015H_000 on 1/9/2015.
 */
public class Photo {
    private static final String JSON_FILENAME = "filename";

    private String mFilename;

    //create a photo representing an existing file on disk
    public Photo(String filename){
        mFilename = filename;
    }

    public Photo(JSONObject json) throws JSONException{
        mFilename = json.getString(JSON_FILENAME);
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME, mFilename);
        return json;
    }

    public String getFileName(){
        return mFilename;
    }
}
