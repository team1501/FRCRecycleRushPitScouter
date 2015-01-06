package org.huntingtonrobotics.frcrecyclerushpitscouter;

import java.util.UUID;

/**
 * Created by 2015H_000 on 1/5/2015.
 */
public class Team {
    private UUID mID;
    private String mTitle;

    public Team(){
        //generate unique identifier
        mID = UUID.randomUUID();
    }
}
