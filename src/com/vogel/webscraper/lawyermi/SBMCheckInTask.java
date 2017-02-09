/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vogel.webscraper.lawyermi;

import com.vogel.record.util.Realm;
import com.vogel.record.util.Task;
import com.vogel.common.util.COMMONdate;
import com.vogel.common.util.COMMONsys;
import com.vogel.common.util.WebPageGrabberSecure;
import com.vogel.netnav.util.TimeoutController;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author VOGEL
 */
class SBMRealm extends Realm {

    public SBMRealm() {
        super(0);
        super.bhalt = true;
    }

    public SBMRealm(int i) {
        super(i);
    }
}

public class SBMCheckInTask extends Task {

    public static final String TASKNAME = "com.vogel.webscraper.lawyermi.SBMCheckInTask";
    private ArrayBlockingQueue queue;
    private int iWorker = 0;
    private SBMRealm params = null;

    public SBMCheckInTask(ArrayBlockingQueue queue, int iWorker) {
        this.queue = queue;
        this.iWorker = iWorker;
    }

    @Override
    public void run() {
        try {
            while (true) {
                params = (SBMRealm) queue.take();
                if (params != null) {
                    if (params.bhalt) {
                        break;
                    }
                    // business logic start here

                    String htmlPage = fetchPage();
                    if (htmlPage!=""){
                        // finally we can write the page
                        writeMainPage(htmlPage);
                        params.filio.TURF("06 PAGE WRITTEN");
                    }
                    //UOW is numbered via the sequence number which provides the UOW and the Seed for lookup
                    iLastCommittedUoW = params.iSeq;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeMainPage(String htmlPage) throws IOException {
        int ifolder = params.iSeq % 10;
        String path = COMMONsys.getInstance().getCOMMONpath() + "DATA/OUTPUT/NETNAV/SBM_CHECKIN_test/" + ifolder + "/";
        File f = new File(path);
        f.mkdirs();
        // and write file
        BufferedWriter bw = new BufferedWriter(new FileWriter(path + params.iSeq + "_" + COMMONdate.getInstance().getTodayDashed() + "_" + COMMONdate.getInstance().getTimeOfDayDashed() + ".html"));
        bw.write(htmlPage);
        bw.close();
    }

    private String fetchPage() {

        String bToken = "<!-- Start_Module_3315 -->";
        String cToken = "dnn_BottomPane";
        String pToken = "<p>This account is private.<br>You must be friends to view it.</p>";

        int nIndex = -1;
        int cIndex = -1;

        SBMCheckInTaskFetchPage task = new SBMCheckInTaskFetchPage(params.iSeq);

        String htmlPage = "";
        try {
            try {
                TimeoutController.execute(task, 15000);
            } catch (Exception e) {
                task.bTimedOut = true;
            }
            if (task.bTimedOut) {
                params.filio.TURF("01 EMPTY PAGE");
                params.filio.TURF("01 TIMEOUT");
            } else {
                htmlPage = task.getPage();
                if (htmlPage.isEmpty()) {
                    params.filio.TURF("01 EMPTY PAGE");
                    //emptyCounter++;
                } else {
                    //reset
                    params.filio.registerTURF("01 EMPTY PAGE");
                    int pIndex = htmlPage.indexOf(pToken);
                    if (pIndex > -1) {
                        params.filio.TURF("02 Private Profile");
                    } else {
                        nIndex = htmlPage.indexOf(bToken);
                        cIndex = htmlPage.indexOf(cToken);
                    }

                    if ((nIndex > -1 && cIndex > -1) && cIndex > nIndex) {
                        params.filio.TURF("03 Proper Indexes");
                        //htmlPage = htmlPage.substring(nIndex, cIndex);
                        //  retreive section of html with relavent data, condense tabs and spacing to single space, remove spaces before and after \n, remove spaces before < and after >, condense consecutive \n to one \n
                        htmlPage = htmlPage.substring(nIndex, cIndex).replaceAll("[ \t]+"," ").replaceAll(" ?\n ?","\n").replaceAll(" <", "<").replaceAll("> ", ">").replaceAll("\n+", "\n");
                    }else{
                        params.filio.TURF("04 No Proper Indexes  "+nIndex+"  "+cIndex);
                                
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            htmlPage = "";
            params.filio.TURF("01 EMPTY PAGE");
        }
        return htmlPage;
    }

}
