/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.vogel.webscraper.copylib.tableio;

import java.sql.*;
import com.vogel.webscraper.copylib.datadef.SBMUSRR;
import com.vogel.common.util.*;
import java.util.ArrayList;

public class SBMUSRRIOX extends Object {

    String db = "";

    public SBMUSRRIOX(String db) {
        this.db = db;
    }

    public SBMUSRR selectUnique(SBMUSRR sbmusrr) {
        String query1 = "SELECT * FROM SBMUSR" + db + " WHERE userid=?";
        DBCM connMgr = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmtSELECT = null;
        
        SBMUSRR rObj = null;

        try {
            // open connection
            connMgr = DBCM.getInstance();
            conn = connMgr.getConnection("NETNAV");
            pstmtSELECT = conn.prepareStatement(query1);
            //System.out.println(sbmusrr.getUserid());
            pstmtSELECT.setInt(1, sbmusrr.getUserid());
            rs = pstmtSELECT.executeQuery();
            
            if (rs.next()) {
                //System.out.println(rs.getInt("userid"));
                rObj = new SBMUSRR();
                SBMUSRRIO cObj = new SBMUSRRIO("");
                cObj.setSBMUSRR(rs, rObj);
            }
        } catch (Exception e) {
            System.out.println("*** ERROR: Could not perform selectUserID on SBMUSRR for user ID " + sbmusrr.getUserid());
            //System.out.println(rs.toString());
            //System.out.println(cObj);
            if (conn == null) {
                System.out.println("*** INFORMATIONAL: Could not obtain connection to database.");
                //System.exit(0);
            }
            //System.exit(0);
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmtSELECT.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            connMgr.freeConnection("NETNAV", conn);
        }
        return rObj;
    }

    public SBMUSRR claimTASK() {
        String query1 = "SELECT * FROM SBMUSR" + db + " WHERE status='' ORDER BY oid LIMIT 1";

        DBCM connMgr = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmtSELECT = null;

        SBMUSRR rObj = null;

        try {
// open connectie
            connMgr = DBCM.getInstance();
            conn = connMgr.getConnection("NETNAV");
            pstmtSELECT = conn.prepareStatement(query1);
            rs = pstmtSELECT.executeQuery();
            if (rs.next()) {
                SBMUSRRIO cObj = new SBMUSRRIO("");
                rObj = new SBMUSRR();
                cObj.setSBMUSRR(rs, rObj);
            }
        } catch (Exception e) {
            System.out.println("*** ERROR: Could not perform claimTASK on SBMUSRR.");
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
            connMgr.freeConnection("NETNAV", conn);
        }
        return rObj;
    }

    public static void main(String args[]) {
        System.out.println("start.");
        System.out.println("done.");
    }

}
