package org.huntingtonrobotics.frcrecyclerushpitscouter.MODIFY;

import org.huntingtonrobotics.frcrecyclerushpitscouter.R;

import java.util.ArrayList;

/**
 * Created by 2015H_000 on 3/20/2015.
 */
public class ReportBuilder{
    
    public String getPitScoutReport(Team t) {

        String report = "";
        String SPACE = " ";
        String LINE_BREAK = "\n";
        String DOUBLE_LINE_BREAK = "\n\n";

        try {
            String teamNum = "" + t.getTeamNum();
            report += "---" + R.string.txt_team_num + SPACE + teamNum + "---";
            report += DOUBLE_LINE_BREAK + R.string.txt_scout_name + SPACE + t.getScoutName();

            //auto
            report += DOUBLE_LINE_BREAK + R.string.auto;

            report += DOUBLE_LINE_BREAK + R.string.txt_auto_progs + SPACE + t.getAutoProgs();

            if (t.isAutoMove()) {
                report += LINE_BREAK + R.string.txt_auto_can_move_az;
            } else {
                report += LINE_BREAK + R.string.txt_auto_can_not_move_az;
            }

            report += LINE_BREAK + R.string.txt_auto_totes + SPACE + t.getAutoTotes();

            report += LINE_BREAK + R.string.txt_auto_conatiners + SPACE + t.getAutoContainers();

            if (t.getAutoPos() != 0) {
                report += LINE_BREAK + R.string.txt_auto_pos + SPACE + t.getAutoPos();
            } else {
                report += LINE_BREAK + R.string.txt_auto_pos + SPACE + "Any";
            }

            //coop

            report += DOUBLE_LINE_BREAK + R.string.txt_coop;

            report += DOUBLE_LINE_BREAK + R.string.txt_tele_coop_set + SPACE + t.getTeleCoopSet();

            if (t.isTeleCoopStack()) {
                report += LINE_BREAK + R.string.team_report_coop_stack;
            } else {
                report += LINE_BREAK + R.string.team_report_no_coop_stack;
            }
            //mech
            report += DOUBLE_LINE_BREAK + R.string.txt_has_mech;

            ArrayList<String> mechHas = new ArrayList<String>();
            ArrayList<String> mechDNH = new ArrayList<String>();

            if (t.isMechContainerStepRemover()) {
                mechHas.add("" + R.string.team_report_step_remover);
            } else {
                mechDNH.add("" + R.string.team_report_no_step_remover);
            }

            for (int m = 0; m < mechHas.size(); m++) {
                try {
                    report += LINE_BREAK + mechHas.get(m);
                } catch (ArrayIndexOutOfBoundsException aiobe) {

                }
            }

            report += DOUBLE_LINE_BREAK + R.string.txt_dnh_mech;

            for (int d = 0; d < mechDNH.size(); d++) {
                try {
                    report += LINE_BREAK + mechDNH.get(d);
                } catch (ArrayIndexOutOfBoundsException aiobe) {

                }
            }

            //teleop
            report += DOUBLE_LINE_BREAK + R.string.txt_tele;

            report += DOUBLE_LINE_BREAK + R.string.txt_tele_totes + SPACE + t.getTeleTotes();
            report += LINE_BREAK + R.string.txt_tele_conatiner + SPACE + t.getTeleContainer();
            report += LINE_BREAK + R.string.txt_tele_tote_stack + SPACE + t.getTelePlaceTotes();

            if (t.getTeleFeed() == 1) {
                report += LINE_BREAK + "Feeds from human station.";
            } else if (t.getTeleFeed() == 2) {
                report += LINE_BREAK + "Feeds from landfill.";
            } else {
                report += LINE_BREAK + "Feeds from both landfill and human station.";
            }

            //comments
            report += DOUBLE_LINE_BREAK + R.string.txt_comments + SPACE + t.getComments();

        } catch (ArrayIndexOutOfBoundsException aiobe) {

        }

        return report;
    }
}
