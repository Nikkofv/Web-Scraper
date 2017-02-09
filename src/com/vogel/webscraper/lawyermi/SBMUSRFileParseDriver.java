/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.vogel.webscraper.lawyermi;

import com.vogel.record.util.FILIOFactory;
import com.vogel.record.util.RecordFactory;
import com.vogel.record.util.QueueMGMT;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author VOGEL
 * @version 2.0  
 * @summary CHECKS THE LOCATIONS AND FILENAMES O OUTPUT FILES PRODUCED BY SBMCheckInDriver.java
 * SPAWNS THREADS WITH EACH THREAD BEING ASSIGNED TO A FILE AND IS SENT TO COMPLETE THE TASK 
 *  SBMUSRFileParseDriver.java
 * EACH THREAD READS THEIR DESIGNATED FILE, PARSES THE THROUGH THE STRINGS EXTRACTING THE DESIRED
 *  DATA, AND UPLOADS THE DATA INTO THE SBMUSR TABLE
 *
 */
public class SBMUSRFileParseDriver extends Object {

    public SBMUSRFileParseDriver() {
    }

    public static void main(String args[]) {

        System.out.println("started.");
        SBMUSRFileParseDriver obj = new SBMUSRFileParseDriver();

        java.util.Date startTime, endTime;
        long lConvTime;
        startTime = new java.util.Date();
        /*LocStatus determines whether or not we are extracing data from htmlpages for a flat file,
        * or if we are reading flat files for upload to the DataBase tables
        */
        String dset="";
        String locStatus = args[2].trim();
        //System.out.println("Process Type: " + locStatus);
        
        //String locStatus = "upload";//"extract";

        //            String dset = "201502FEB_DEEDS_BASELINE";
        //           String dset = "DEEDS_BL2";
        //   String dset = "DEEDS_CITY";
        //String dset = "USR_SBM_CHECKIN_Full_2015-09-17_18-41-41-717";
        //String dset = "SBM_CHECKIN_Full";
        
        try {
            if (dset.isEmpty() && locStatus.equals("extract")) {

                obj.scheduler(args[0].trim(), args[1].trim());

            } else if(!dset.isEmpty() && locStatus.equals("extract")){
                obj.scheduler(dset,"DATA");
            }
            
            if (dset.isEmpty() && locStatus.equals("upload")) {
                dset = args[0].trim();
                
                System.out.println("DataSet: "+dset);
                
                obj.schedulerUpload(args[0].trim(), args[1].trim());

            } else if(!dset.isEmpty() && locStatus.equals("upload")){
                System.out.println("DataSet: "+dset);
                obj.schedulerUpload(dset, "DATA");
            }
        } catch (Exception e) {
            System.out.println("*** PARAMETER EXPECTED: <dset> <volume>");
            e.printStackTrace();
        }

        endTime = new java.util.Date();
        lConvTime = endTime.getTime() - startTime.getTime();
        System.out.println("running time [ms]: " + (lConvTime));
        System.out.println("ended.");
    }

    public void scheduler(String dset, String volume) {

        int iCntUOW = 0;
        FILIOFactory filio = new FILIOFactory();
        File[] inFolders = filio.fetchFilesOrderedFromFolder(volume, "INPUT", "NETNAV/" + dset);

        try {
            filio.register("NETNAV",dset);

            QueueMGMT qObj = new QueueMGMT(SBMUSRFileParseTask.TASKNAME, 8);
            SortedMap output = Collections.synchronizedSortedMap(new TreeMap());
            FILIOFactory writeRAT = filio.registerAdditionalWriters("NETNAV", "USR_"+dset);
            String headers = "OID\tUSER ID\tSOURCE\tUSERNAME\tSTATE LICENSE 1\tSTATE LICENSE 2\tLICENSE ADMIS DATE 1\tLICENSE ADMIS DATE 2\tLICENSE NUMBER 1\tLICENSE NUMBER 2\tPHONE NUMBER\tFAX NUMBER\tEMAIL\tOCCUPATION\tSTATUS\tEMPLOYER\tJOB TITLE\tADDRESS LINE 1\tCOUNTY\tCITY\tSTATE\tPOSTAL CODE\tWEBSITE\tPRACTICE AREAS\tLANGUAGES\tSECTIONS\tUNIVERSITIES\tDEGREES\tYEAR GRADUATED\tBAR ASSOCIATIONS\tBA ENTRY DATE\tBA ROLE\tLAT\tLON\tFDATE\n";
            writeRAT.write(headers);

            for (int i = 0; i < inFolders.length; i++) {
                //for (int i = 0; i < 1; i++) {
                File f1 = inFolders[i];
                
                //   System.out.println("*** processing folder " + f1.getName() + ": " + (i + 1) + " of " + inFolders.length);
                if (f1.getName().contains("DS_Store")) {
                    continue;
                }
                FILIOFactory filio2 = new FILIOFactory();
                File[] inFiles = filio2.fetchFilesOrderedFromFolder(volume, "OUTPUT", "NETNAV/" + dset + "/" + f1.getName());
                for (int j = 0; j < inFiles.length; j++) {
                    File f2 = inFiles[j];
                    //   System.out.println("*** processing file: " + f2.getName() + ": " + (j + 1) + " of " + inFiles.length);

                    filio.theProject = f1.getName() + "/" + f2.getName()
                            + " (folder " + (i + 1) + " of " + inFolders.length
                            + "/file " + (j + 1) + " of " + inFiles.length + ")";

                    iCntUOW++;
                    filio.TURF(filio.topCount);

                    filio.TURF(filio.topCountSub);
                    // define the task at hand and put on the bus  
                    SBMUSRFileParseRealm uow = new SBMUSRFileParseRealm(iCntUOW);
                    FILIOFactory rInput = new FILIOFactory();
                    rInput.register(f2);
                    rInput.setReader();
                    uow.filio = filio;
                    uow.file = f1.getName() + "/" + f2.getName();
                    uow.htmlPage = rInput.readHTMLPageOriginalFormat();
                    uow.output = output;
                    qObj.addQ(uow);

                    filio.SHOWSTATS(100, null, false);
                    if (iCntUOW % 100 == 0) {
                        // System.out.println("*** processed so far: " + iCntUOW);
                        filio.registerTURF("01 LAST COMMITTED UOW", qObj.getLastCommittedUoW());
                        processOutput(output, writeRAT, qObj.getLastCommittedUoW());
                    }
                }
            }

            while (qObj.getQueueSize() > 0) {
                qObj.sleep();
                filio.SHOWSTATS();
            }

            qObj.halt(new SBMUSRFileParseRealm());

            filio.registerTURF("01 LAST COMMITTED UOW FOR " + iCntUOW, qObj.getLastCommittedUoW());
            filio.SHOWSTATS();
            processOutput(output, writeRAT, Integer.MAX_VALUE);
            writeRAT.cleanUp();
            filio.cleanUp();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("*** FILE TYPE NOT RECOGNIZED?");
        }
    }
    
    public void schedulerUpload(String dset, String volume) {
        System.out.println("Uploading Has Begun");
        int iCntUOW = 0;
        FILIOFactory filio = new FILIOFactory();
        //File[] inFolders = filio.fetchFilesOrderedFromFolder(volume, "INPUT", "NETNAV/" + dset);

        try {
            filio.register("NETNAV", dset);

            QueueMGMT qObj = new QueueMGMT(SBMUSRFileParseTask.TASKNAME, 4);

            SortedMap output = Collections.synchronizedSortedMap(new TreeMap());
            
            filio.setReader();
            String head = "USER ID\tSOURCE\tUSERNAME\tSTATE LICENSE 1\tSTATE LICENSE 2\tLICENSE ADMIS DATE 1\tLICENSE ADMIS DATE 2\tLICENSE NUMBER 1\tLICENSE NUMBER 2\tPHONE NUMBER\tFAX NUMBER\tEMAIL\tOCCUPATION\tSTATUS\tEMPLOYER\tJOB TITLE\tADDRESS LINE 1\tCOUNTY\tCITY\tSTATE\tPOSTAL CODE\tWEBSITE\tPRACTICE AREAS\tLANGUAGES\tSECTIONS\tUNIVERSITIES\tDEGREES\tYEAR GRADUATED\tBAR ASSOCIATIONS\tBA ENTRY DATE\tBA ROLE\tLAT\tLON\tFDATE";
            filio.readHeaders(head.replaceAll("\t", ","));
            
            if (filio.bHeaderAreGood) {

                //RecordFactory rf = new RecordFactory(headers);
                String rString = filio.readLine();

                while (rString != null) {
                    iCntUOW++;
                    // define the task at hand and put on the bus  
                    SBMUSRFileParseRealm uow = new SBMUSRFileParseRealm(iCntUOW);
                    
                    uow.filio = filio;
                    uow.rString = rString;
                    uow.locstatus = false;
                    qObj.addQ(uow);
                    
                    filio.SHOWSTATS(100, null, false);
                    if (iCntUOW % 1000 == 0) {
                        filio.registerTURF("01 LAST COMMITTED UOW", qObj.getLastCommittedUoW());
                        //processOutput(output, filio, qObj.getLastCommittedUoW());
                    }
                    rString = filio.readLine();
                }
                while (qObj.getQueueSize() > 0) {
                    qObj.sleep();
                    filio.SHOWSTATS();
                    //processOutput(output, filio, qObj.getLastCommittedUoW());
                }
                
            }

            while (qObj.getQueueSize() > 0) {
                qObj.sleep();
                filio.SHOWSTATS();
            }

            qObj.halt(new SBMUSRFileParseRealm());

            filio.registerTURF("01 LAST COMMITTED UOW FOR " + iCntUOW, qObj.getLastCommittedUoW());
            filio.SHOWSTATS();
            //processOutput(output, filio, Integer.MAX_VALUE);
            filio.cleanUp();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("*** FILE TYPE NOT RECOGNIZED?");
        }
    }

    private void processOutput(SortedMap output, FILIOFactory file, int iLastCommittedUoW) throws IOException {
        Set s = output.keySet();
        //    System.out.println("************* PRINTING OUT UP TO " + iLastCommittedUoW);
        synchronized (output) {
            Iterator ite = s.iterator();
            HashSet hsDup = new HashSet();
            while (ite.hasNext()) {
                Integer key = (Integer) ite.next();
                // only write out what has been committed.
                if (key <= iLastCommittedUoW) {
                    String value = (String) output.get(key);
                    file.write(value);
                    hsDup.add(key);
                } else {
                    break;
                }
            }
            file.flush();
            if (iLastCommittedUoW == Integer.MAX_VALUE) {
                output.clear();
            } else {
                // remove what has been written.
                ite = hsDup.iterator();
                while (ite.hasNext()) {
                    output.remove((Integer) ite.next());
                }
            }
        }
    }
}
