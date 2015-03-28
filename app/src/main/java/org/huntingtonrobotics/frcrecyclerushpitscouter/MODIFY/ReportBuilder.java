package org.huntingtonrobotics.frcrecyclerushpitscouter.MODIFY;

import org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.fragments.SendFragment;

import java.util.ArrayList;

/**
 * Created by 2015H_000 on 3/20/2015.
 */
public class ReportBuilder extends SendFragment {

    public String getPitScoutReport(Team t) {

        String report = "";
        String SPACE = " ";
        String LINE_BREAK = "\n";
        String DOUBLE_LINE_BREAK = "\n\n";

        try {
            String teamNum = "" + t.getTeamNum();
            report += "---" + "Team Number:" + SPACE + teamNum + "---";
            report += DOUBLE_LINE_BREAK + "Scout Name:" + SPACE + t.getScoutName();

            //autoe
            report += DOUBLE_LINE_BREAK + "AUTO";

            report += DOUBLE_LINE_BREAK + "Num of Auto Programs:" + SPACE + t.getAutoProgs();

            if (t.isAutoMove()) {
                report += LINE_BREAK + "Can move to AZ.";
            } else {
                report += LINE_BREAK + "Can not move to AZ.";
            }

            report += LINE_BREAK + "Num of Auto totes to AZ:" + SPACE + t.getAutoTotes();
            report += LINE_BREAK + "Num of Auto Containers to AZ:" + SPACE + t.getAutoContainers();

            if (t.getAutoPos() != 0) {
                report += LINE_BREAK + "Preferred Auto Pos:" + SPACE + t.getAutoPos();
            } else {
                report += LINE_BREAK + "Preferred Auto Pos:" + SPACE + "Any";
            }

            //coop

            report += DOUBLE_LINE_BREAK + "COOP";

            report += DOUBLE_LINE_BREAK + "Coop set height:" + SPACE + t.getTeleCoopSet();

            if (t.isTeleCoopStack()) {
                report += LINE_BREAK + "Can place bin on 3rd or 4th level of Coop set.";
            } else {
                report += LINE_BREAK + "Can NOT place bin on 3rd or 4th level of Coop set.";
            }
            //mech
            report += DOUBLE_LINE_BREAK + "Has Mechanisms:";

            ArrayList<String> mechHas = new ArrayList<String>();
            ArrayList<String> mechDNH = new ArrayList<String>();

            if (t.isMechContainerStepRemover()) {
                mechHas.add("" +"Step Remover.");
            } else {
                mechDNH.add("" + "Step Remover.");
            }

            for (int m = 0; m < mechHas.size(); m++) {
                try {
                    report += LINE_BREAK + mechHas.get(m);
                } catch (ArrayIndexOutOfBoundsException aiobe) {

                }
            }

            report += DOUBLE_LINE_BREAK + "Does NOT have Mechanisms:";

            for (int d = 0; d < mechDNH.size(); d++) {
                try {
                    report += LINE_BREAK + mechDNH.get(d);
                } catch (ArrayIndexOutOfBoundsException aiobe) {

                }
            }

            report += DOUBLE_LINE_BREAK + "Robot weight:" + SPACE +t.getMechWeight();

            //teleop
            report += DOUBLE_LINE_BREAK + "TELEOP";

            report += DOUBLE_LINE_BREAK + "Tote stack height:" + SPACE + t.getTeleTotes();
            report += LINE_BREAK + "Container stack height:" + SPACE + t.getTeleContainer();
            report += LINE_BREAK + "Place totes on stack height:" + SPACE + t.getTelePlaceTotes();

            if (t.getTeleFeed() == 1) {
                report += LINE_BREAK + "Can feed from human station.";
            } else if (t.getTeleFeed() == 2) {
                report += LINE_BREAK + "Can feed from landfill.";
            } else {
                report += LINE_BREAK + "Can feed from both landfill and human station.";
            }

            if (t.getTelePrefFeed() == 1) {
                report += LINE_BREAK + "Prefers to feed from human station.";
            } else {
                report += LINE_BREAK + "Prefers to feed from landfill.";
            }


            //comments
            report += DOUBLE_LINE_BREAK + "Comments:" + SPACE + t.getComments();

        } catch (ArrayIndexOutOfBoundsException aiobe) {

        }

        return report;
    }
}
