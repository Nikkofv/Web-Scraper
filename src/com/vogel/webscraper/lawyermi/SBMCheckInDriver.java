/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2016, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.vogel.webscraper.lawyermi;

import com.vogel.record.util.CHECKPOINT_RESTART;
import com.vogel.record.util.FILIOFactory;
import com.vogel.record.util.QueueMGMT;

/**
 *
 * @author Nicholas "Nikko" Vogel
 * @version THE PURPOSE OF THIS MODULE IS TO SPAWN THREADS ASSIGNED TO A GIVEN WEBPAGE
 * EACH THREAD WILL USE SBMCheckInTask.java and SBMCheckInTaskFetchPage TO FETCH THE 
 * WEBPAGE HTML FILE, EXTRACT THE SECTIONS OF INTEREST AND WRITE THEM TO AN OUTPUT FILE
 *
 * 
 */
public class SBMCheckInDriver extends Object {

    public SBMCheckInDriver() {
    }

    public boolean processBasic(int stNum, int enNum) {

        //Keeps track of UOW succesfully processed
        //CHECKPOINT_RESTART cpr = new CHECKPOINT_RESTART("ACCESS_SBM", "userid" + stNum, 1);
        int iRestart = 0;//cpr.lastCheckpoint();

        //The seed to be provided
        if (iRestart == 0) {
            iRestart = stNum;
        }

        System.out.println("******* RESTARTING WITH " + iRestart);

        // Register location to put files
        FILIOFactory filio = new FILIOFactory();
        filio.register("NETNAV", "");

        try {
            //Ques tasks (taskName, threads, size of queue)
            QueueMGMT qObj = new QueueMGMT(SBMCheckInTask.TASKNAME, 8, 1000);
            //SortedMap output = Collections.synchronizedSortedMap(new TreeMap());

            //iUoW++;
            filio.registerTURF(filio.topCount, iRestart - stNum - 1);
            for (int i = iRestart; i < (enNum); i++) {

                filio.TURF(filio.topCount); //

                long emptyPages = filio.getTURFValue("01 EMPTY PAGE");
                //System.out.println("EmptyCount: " + emptyPages);

                //Not consecutive, cumulative. 8 Threads * 10 emptypages
                if (emptyPages >= 10000) {
                    System.out.println("1000+ Cumulative Empty Pages");
                    return false;
                }

                filio.TURF(filio.topCountSub);
                SBMRealm uow = new SBMRealm(i);
                uow.filio = filio;
                //uow.output = output;
                qObj.addQ(uow);

                filio.SHOWSTATS(100, null, false);

                //Select a good checkpoint count.
                if (i % 100 == 0) {
                    filio.registerTURF("07 LAST COMMITTED UOW FOR " + stNum, qObj.getLastCommittedUoW());
                    //cpr.takeCheckPoint(qObj.getLastCommittedUoW());
                }
            }

            while (qObj.getQueueSize() > 0) {
                qObj.sleep();
                //cpr.takeCheckPoint(qObj.getLastCommittedUoW());
                filio.SHOWSTATS();
            }

            qObj.halt(new SBMRealm());
            filio.registerTURF("07 LAST COMMITTED UOW FOR " + stNum, qObj.getLastCommittedUoW());
            filio.SHOWSTATS();
            //processOutput(output, zipwriter, Integer.MAX_VALUE);

            filio.cleanUp();
            //cpr.clearCheckpoint(enNum);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("started.");
        int startParam = 0;
        int endParam = 0;
        try {
            SBMCheckInDriver obj = new SBMCheckInDriver();
            java.util.Date startTime, endTime;
            long lConvTime;
            startTime = new java.util.Date();
            try {
                startParam = 1000;//Integer.parseInt(args[0].trim());
                endParam = 2000;//Integer.parseInt(args[1].trim());
                //startParam = Integer.parseInt(args[0].trim());// lower bound is 1115
                //endParam = Integer.parseInt(args[1].trim());

                obj.processBasic(startParam, endParam);

                // obj.processLegal(args[0].trim());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("*** PARAMETERS STARTNUM(INT) AND ENDNUM(INT) EXPECTED");
            }

            endTime = new java.util.Date();
            lConvTime = endTime.getTime() - startTime.getTime();
            System.out.println("running time [ms]: " + (lConvTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ended.");
    }
}
