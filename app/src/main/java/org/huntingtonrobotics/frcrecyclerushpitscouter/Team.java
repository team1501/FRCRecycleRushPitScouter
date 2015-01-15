package org.huntingtonrobotics.frcrecyclerushpitscouter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;


/**
 * Created by 2015H_000 on 1/5/2015.
 */
public class Team {

    private static final String JSON_ID = "id";
    private static final String JSON_TEAM_NUM = "teamNum";
    private static final String JSON_PHOTO = "photo";

    //mech
    private static final String JSON_MECH_LITTER_INSERTER = "mechLitterInserter";
    private static final String JSON_MECH_LITTER_PUSHER = "mechLitterPusher";

    private static final String JSON_MECH_TOTE_FEEDER = "mechToteFeeder";

    private static final String JSON_MECH_CONTAINER_FLIPPER = "mechContainerFlipper";
    private static final String JSON_MECH_CONTAINER_STEP_REMOVER = "mechContainerStepRemover";

    //auto
    private static final String JSON_AUTO_PROGS = "autoProgs";
    private static final String JSON_AUTO_MOVE = "autoMove";
    private static final String JSON_AUTO_TOTES = "autoTotes";
    private static final String JSON_AUTO_CONTAINERS = "autoContainers";
    //private static final String JSON_AUTO_HEIGHT = "autoHeight";      //deleted from auto
    private static final String JSON_AUTO_MOVE_TOTE = "autoMoveTote";
    private static final String JSON_AUTO_POS = "autoPos";

    //include other things to save here

    private UUID mID;

    //team info
    private int mTeamNum;
    private Photo mPhoto;

    //mech
    private boolean mMechLitterInserter;
    private boolean mMechLitterPusher;

    private boolean mMechToteFeeder;

    private boolean mMechContainerFlipper;
    private boolean mMechContainerStepRemover;

    //auto
    private int mAutoProgs;
    private boolean mAutoMove;
    private int mAutoTotes;
    private int mAutoContainers;
    //private int mAutoHeight;  //deleted from auto
    private boolean mAutoMoveTote;
    private int mAutoPos;

    //Constructor for Team
    public Team(){
        //generate unique identifier
        mID = UUID.randomUUID();
    }
    //---Constructor for Team

    //constructor that accepts JSON object
    public Team(JSONObject json) throws JSONException{
        mID = UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_TEAM_NUM)){
            mTeamNum = json.getInt(JSON_TEAM_NUM);
        }
        if (json.has(JSON_PHOTO)){
            mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
        }

        //mech
        if(json.has(JSON_MECH_LITTER_INSERTER)){
            mMechLitterInserter = json.getBoolean(JSON_MECH_LITTER_INSERTER);
        }
        if(json.has(JSON_MECH_LITTER_PUSHER)){
            mMechLitterPusher = json.getBoolean(JSON_MECH_LITTER_PUSHER);
        }

        if(json.has(JSON_MECH_TOTE_FEEDER)){
            mMechToteFeeder = json.getBoolean(JSON_MECH_TOTE_FEEDER);
        }

        if(json.has(JSON_MECH_CONTAINER_FLIPPER)){
            mMechContainerFlipper = json.getBoolean(JSON_MECH_CONTAINER_FLIPPER);
        }
        if(json.has(JSON_MECH_CONTAINER_STEP_REMOVER)){
            mMechContainerStepRemover = json.getBoolean(JSON_MECH_CONTAINER_STEP_REMOVER);
        }

        //auto
        if (json.has(JSON_AUTO_PROGS)){
            mAutoProgs = json.getInt(JSON_AUTO_PROGS);
        }
        if (json.has(JSON_AUTO_MOVE)){
            mAutoMove = json.getBoolean(JSON_AUTO_MOVE);
        }
        if (json.has(JSON_AUTO_TOTES)){
            mAutoTotes = json.getInt(JSON_AUTO_TOTES);
        }
        if (json.has(JSON_AUTO_CONTAINERS)){
            mAutoContainers = json.getInt(JSON_AUTO_CONTAINERS);
        }
        /*deleted from auto
        if (json.has(JSON_AUTO_HEIGHT)){
            mAutoHeight = json.getInt(JSON_AUTO_HEIGHT);
        }
        */
        if (json.has(JSON_AUTO_MOVE_TOTE)){
            mAutoMoveTote = json.getBoolean(JSON_AUTO_MOVE_TOTE);
        }
        if (json.has(JSON_AUTO_POS)){
            mAutoPos = json.getInt(JSON_AUTO_POS);
        }

    }
    //---constructor that accepts JSON object

    //puts teams to JSON format to be put in JSON file
    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mID.toString());
        json.put(JSON_TEAM_NUM, mTeamNum);
        if (mPhoto!=null){
            json.put(JSON_PHOTO, mPhoto.toJSON());
        }

        //mech
        json.put(JSON_MECH_LITTER_INSERTER, mMechLitterInserter);
        json.put(JSON_MECH_LITTER_PUSHER, mMechLitterPusher);

        json.put(JSON_MECH_TOTE_FEEDER, mMechToteFeeder);

        json.put(JSON_MECH_CONTAINER_FLIPPER, mMechContainerFlipper);
        json.put(JSON_MECH_CONTAINER_STEP_REMOVER, mMechContainerStepRemover);

        //auto
        json.put(JSON_AUTO_PROGS, mAutoProgs);
        json.put(JSON_AUTO_MOVE, mAutoMove);
        json.put(JSON_AUTO_TOTES, mAutoTotes);
        json.put(JSON_AUTO_CONTAINERS, mAutoContainers);
        //json.put(JSON_AUTO_HEIGHT, mAutoHeight);      //deleted from auto
        json.put(JSON_AUTO_MOVE_TOTE, mAutoMoveTote);
        json.put(JSON_AUTO_POS, mAutoPos);

        return json;
    }
    //---puts teams to JSON format to be put in JSON file

    @Override
    public String toString() {
        return "Team #" + mTeamNum;
    }

    //Getter and Setter for mID
    public UUID getID() {
        return mID;
    }
    public void setID(UUID ID) {
        mID = ID;
    }
    //---Getter and Setter for mID

    //Getter and Setter for mTeamNum
    public int getTeamNum() {
        return mTeamNum;
    }
    public void setTeamNum(int teamNum) {
        mTeamNum = teamNum;
    }
    //---Getter and Setter for mTeamNum


    //Getter and Setter for mPhoto
    public Photo getPhoto() {
        return mPhoto;
    }
    public void setPhoto(Photo p) {
        this.mPhoto = p;
    }
    //---Getter and Setter for mPhoto


    //Getter and Setters for Mech
    public boolean isMechLitterInserter() {
        return mMechLitterInserter;
    }

    public void setMechLitterInserter(boolean li) {
        this.mMechLitterInserter = li;
    }

    public boolean isMechLitterPusher() {
        return mMechLitterPusher;
    }

    public void setMechLitterPusher(boolean lp) {
        this.mMechLitterPusher = lp;
    }

    public boolean isMechToteFeeder() {
        return mMechToteFeeder;
    }

    public void setMechToteFeeder(boolean tf) {
        this.mMechToteFeeder = tf;
    }

    public boolean isMechContainerFlipper() {
        return mMechContainerFlipper;
    }

    public void setMechContainerFlipper(boolean cf) {
        this.mMechContainerFlipper = cf;
    }

    public boolean isMechContainerStepRemover() {
        return mMechContainerStepRemover;
    }

    public void setMechContainerStepRemover(boolean sr) {
        this.mMechContainerStepRemover = sr;
    }

    //---Getter and Setters for Mech


    //Getter and Setters for Auto

    public int getAutoProgs() {
        return mAutoProgs;
    }

    public void setAutoProgs(int ap) {
        this.mAutoProgs = ap;
    }

    public boolean isAutoMove() {
        return mAutoMove;
    }

    public void setAutoMove(boolean am) {
        this.mAutoMove = am;
    }

    public int getAutoTotes() {
        return mAutoTotes;
    }

    public void setAutoTotes(int at) {
        this.mAutoTotes = at;
    }

    public int getAutoContainers() {
        return mAutoContainers;
    }

    public void setAutoContainers(int ac) {
        this.mAutoContainers = ac;
    }

    /*deleted from auto
    public int getAutoHeight() {
        return mAutoHeight;
    }

    public void setAutoHeight(int ah) {
        this.mAutoHeight = ah;
    }
    */

    public boolean isAutoMoveTote() {
        return mAutoMoveTote;
    }

    public void setAutoMoveTote(boolean mt) {
        this.mAutoMoveTote = mt;
    }

    public int getAutoPos() {
        return mAutoPos;
    }

    public void setAutoPos(int ap) {
        this.mAutoPos = ap;
    }

    //----Getter and Setters for Auto

    //for bubble sort
    public int compareTo(Team t){
        int res=0;
        if (mTeamNum < t.getTeamNum()) {res=-1;  }
        if (mTeamNum > t.getTeamNum()){res=1;}
        return res;
    }
}
