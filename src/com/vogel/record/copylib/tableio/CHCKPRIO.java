/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.vogel.record.copylib.tableio;

import java.sql.*;
import com.vogel.record.copylib.datadef.CHCKPR;
import com.vogel.common.util.*;

public class CHCKPRIO extends Object {

    String db = "";

    public CHCKPRIO(String db) {
        this.db = db;
    }

    public int update(CHCKPR chckpr) {
        String query1 = "UPDATE CHCKP" + db + " SET rinfo=?,uow=?,sdate=?,fdate=?,status=? WHERE process=? AND file=? AND thread=?";

        DBCM connMgr = null;
        Connection conn = null;
        PreparedStatement pstmtUPDATE = null;

        int iStatus = 0;

        try {
// open connectie
            connMgr = DBCM.getInstance();
            conn = connMgr.getConnection("SYNT");
            pstmtUPDATE = conn.prepareStatement(query1);

            pstmtUPDATE.setString(1, chckpr.getRinfo());
            pstmtUPDATE.setInt(2, chckpr.getUow());
            pstmtUPDATE.setString(3, chckpr.getSdate());
            pstmtUPDATE.setString(4, chckpr.getFdate());
            pstmtUPDATE.setInt(5, chckpr.getStatus());
            pstmtUPDATE.setString(6, chckpr.getProcess());
            pstmtUPDATE.setString(7, chckpr.getFile());
            pstmtUPDATE.setInt(8, chckpr.getThread());
            iStatus = pstmtUPDATE.executeUpdate();
        } catch (Exception e) {
            System.out.println("*** ERROR: Could not perform UPDATE on CHCKPR.");
            if (conn == null) {
                System.out.println("*** INFORMATIONAL: Could not obtain connection to database.");
            }
            e.printStackTrace();
        } finally {
            try {
                pstmtUPDATE.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            connMgr.freeConnection("SYNT", conn);
        }
        return iStatus;
    }

    public int insert(CHCKPR chckpr) {
        String query1 = "INSERT INTO CHCKP" + db + " (process,file,thread,rinfo,uow,sdate,fdate,status) VALUES (?,?,?,?,?,?,?,?)";

        DBCM connMgr = null;
        Connection conn = null;

        PreparedStatement pstmtINSERT = null;

        int iStatus = 0;

        try {
// open connectie
            connMgr = DBCM.getInstance();
            conn = connMgr.getConnection("SYNT");
            pstmtINSERT = conn.prepareStatement(query1);
            pstmtINSERT.setString(1, chckpr.getProcess());
            pstmtINSERT.setString(2, chckpr.getFile());
            pstmtINSERT.setInt(3, chckpr.getThread());
            pstmtINSERT.setString(4, chckpr.getRinfo());
            pstmtINSERT.setInt(5, chckpr.getUow());
            pstmtINSERT.setString(6, chckpr.getSdate());
            pstmtINSERT.setString(7, chckpr.getFdate());
            pstmtINSERT.setInt(8, chckpr.getStatus());
            iStatus = pstmtINSERT.executeUpdate();
        } catch (Exception e) {
            System.out.println("*** ERROR: Could not perform INSERT on CHCKPR.");
            if (conn == null) {
                System.out.println("*** INFORMATIONAL: Could not obtain connection to database.");
            }
            e.printStackTrace();
        } finally {
            try {
                pstmtINSERT.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            connMgr.freeConnection("SYNT", conn);
        }
        return iStatus;
    }

    public int select(CHCKPR chckpr) {
        String query1 = "SELECT * FROM CHCKP" + db + " WHERE process=? AND file=? AND thread=?";

        DBCM connMgr = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmtSELECT = null;

        int iStatus = 0;

        try {
// open connectie
            connMgr = DBCM.getInstance();
            conn = connMgr.getConnection("SYNT");
            pstmtSELECT = conn.prepareStatement(query1);
            pstmtSELECT.setString(1, chckpr.getProcess());
            pstmtSELECT.setString(2, chckpr.getFile());
            pstmtSELECT.setInt(3, chckpr.getThread());
            rs = pstmtSELECT.executeQuery();
            if (rs.next()) {
                setCHCKPR(rs, chckpr);
                iStatus = 1;
            }
        } catch (Exception e) {
            System.out.println("*** ERROR: Could not perform SELECT on CHCKPR.");
            if (conn == null) {
                System.out.println("*** INFORMATIONAL: Could not obtain connection to database.");
            }
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmtSELECT.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            connMgr.freeConnection("SYNT", conn);
        }
        return iStatus;
    }

    public void setCHCKPR(ResultSet rs, CHCKPR chckpr) throws SQLException {
        chckpr.setProcess(rs.getString("process"));
        chckpr.setFile(rs.getString("file"));
        chckpr.setThread(rs.getInt("thread"));
        chckpr.setRinfo(rs.getString("rinfo"));
        chckpr.setUow(rs.getInt("uow"));
        chckpr.setSdate(rs.getString("sdate"));
        chckpr.setFdate(rs.getString("fdate"));
        chckpr.setStatus(rs.getInt("status"));
    }

    public static void main(String args[]) {
        System.out.println("start.");
        CHCKPR obj1 = new CHCKPR();
        obj1.setProcess("12");
        obj1.setFile("13");
        obj1.setThread(14);
        obj1.setRinfo("15");
        obj1.setUow(16);
        obj1.setSdate("17-00-00 00:00:00");
        obj1.setFdate("18-00-00 00:00:00");
        obj1.setStatus(19);

        try {
            CHCKPRIO obj2 = new CHCKPRIO("");
            obj2.insert(obj1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("done.");
    }

}
