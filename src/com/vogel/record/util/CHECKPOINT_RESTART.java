/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.vogel.record.util;

import com.vogel.record.copylib.datadef.CHCKPR;
import com.vogel.record.copylib.tableio.CHCKPRIO;
import com.vogel.record.copylib.tableio.CHCKPRIOX;
import com.vogel.common.util.COMMONdate;

/**
 *
 * @author VOGEL
 * @version
 */
public class CHECKPOINT_RESTART extends Object {

    private CHCKPR rObj = null;

    public CHECKPOINT_RESTART(String process, String file, int iThread) {
        rObj = new CHCKPR();
        rObj.setProcess(process);
        rObj.setFile(file);
        rObj.setThread(iThread);
    }
    
    public CHECKPOINT_RESTART() {
        
    }

    public CHCKPR getUoW(CHCKPR rec) {
        CHCKPRIOX sObj = new CHCKPRIOX("");
        return sObj.getUoW(rec);
    }
    
    public int getStatus() {
        return rObj.getStatus();
    }

    public String getFile() {
        return rObj.getFile();
    }

    public String getRinfo() {
        return rObj.getRinfo();
    }

    public String getProcess() {
        return rObj.getProcess();
    }

    public void clearCheckpoint(int iCnt) {
        // CHCKPRIOX sObj = new CHCKPRIOX("");
        // sObj.purgeCHCKPNT(rObj);
        CHCKPRIO iObj = new CHCKPRIO("");
        int iStatus = iObj.select(rObj);
        if (iStatus == -1) {
            System.out.println("*** FATAL ERROR. CHCKP COULD NOT BE DETERMINED. PROCESS HALTING.");
            System.exit(0);
        }
        rObj.setUow(iCnt);
        if (rObj.getSdate().contains("0000-00-00")) {
            rObj.setSdate(COMMONdate.getInstance().getDateTime());
        }
        rObj.setFdate(COMMONdate.getInstance().getDateTime());
        rObj.setStatus(1);
        if (iStatus == 0) {
            iObj.insert(rObj);
        } else {
            iObj.update(rObj);
        }
    }

    public int lastCheckpoint() {
        int iStatus = 0;
        CHCKPRIO iObj = new CHCKPRIO("");
        CHCKPRIOX sObj = new CHCKPRIOX("");
        if (rObj.getFile().isEmpty() || rObj.getThread() == -10) {
            System.out.println("*** finding last checkpoint with: " + rObj.getProcess() + ", " + rObj.getThread());
            iStatus = sObj.selectOnProcess(rObj);
        } else {
            System.out.println("*** finding last checkpoint with: " + rObj.getProcess() + ", " + rObj.getFile() + ", " + rObj.getThread());
            iStatus = iObj.select(rObj);
        }
        if (iStatus == -1) {
            System.out.println("*** FATAL ERROR. CHCKP COULD NOT BE DETERMINED. PROCESS HALTING.");
            System.exit(0);
        }

        if (rObj.getStatus() != 1) {
            return rObj.getUow();
        } else {
            return 0;
        }
    }

    public void initFileRestart() {
        CHCKPRIO iObj = new CHCKPRIO("");
        CHCKPRIOX sObj = new CHCKPRIOX("");
        int iStatus = sObj.selectOnProcess(rObj);
        if (iStatus == -1) {
            System.out.println("*** FATAL ERROR. CHCKP COULD NOT BE DETERMINED. PROCESS HALTING.");
            System.exit(0);
        }
        if (rObj.getSdate().contains("0000-00-00")) {
            rObj.setSdate(COMMONdate.getInstance().getDateTime());
        }
        rObj.setFdate(COMMONdate.getInstance().getDateTime());
        rObj.setStatus(2);
        if (iStatus == 0) {
            iObj.insert(rObj);
        }
    }

    public void updateFile(String dset) {
        rObj.setFile(dset);
        CHCKPRIOX iObj = new CHCKPRIOX("");
        iObj.updateFile(rObj);
    }

    public void updateFileRestart(String restartSettings) {
        rObj.setRinfo(restartSettings);
        CHCKPRIOX iObj = new CHCKPRIOX("");
        iObj.updateFileRestart(rObj);
    }

    public void takeCheckPoint(int iCnt) {
        CHCKPRIO iObj = new CHCKPRIO("");
        int iStatus = iObj.select(rObj);
        if (iStatus == -1) {
            System.out.println("*** FATAL ERROR. CHCKP COULD NOT BE DETERMINED. PROCESS HALTING.");
            System.exit(0);
        }
        rObj.setUow(iCnt);
        if (rObj.getSdate().contains("0000-00-00")) {
            rObj.setSdate(COMMONdate.getInstance().getDateTime());
        }
        rObj.setFdate(COMMONdate.getInstance().getDateTime());
        rObj.setStatus(2);
        if (iStatus == 0) {
            iObj.insert(rObj);
        } else {
            if (rObj.getUow() > 0) {
                iObj.update(rObj);
            }
        }
    }

    public static void main(String args[]) {

        System.out.println("start.");
        System.out.println("done.");
    }
}
