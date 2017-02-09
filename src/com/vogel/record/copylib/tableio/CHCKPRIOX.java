/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.vogel.record.copylib.tableio;

import com.vogel.record.copylib.datadef.CHCKPR;
import com.vogel.common.util.DBCM;
import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class CHCKPRIOX extends Object {

    String db = "";
    String defaultServer = "SYNT";

    public CHCKPRIOX(String db) {
        this.db = db;
    }

    public void setNewServer(String server) {
        defaultServer = server;
    }

    public int purgeCHCKPNT(CHCKPR chckpr) {
        String query1 = "DELETE FROM CHCKP" + db + " WHERE process=? AND file=? AND thread=?";

        DBCM connMgr = null;
        Connection conn = null;
        PreparedStatement pstmtUPDATE = null;

        int iStatus = 0;

        try {
// open connectie
            connMgr = DBCM.getInstance();
            conn = connMgr.getConnection(defaultServer);
            pstmtUPDATE = conn.prepareStatement(query1);
            pstmtUPDATE.setString(1, chckpr.getProcess());
            pstmtUPDATE.setString(2, chckpr.getFile());
            pstmtUPDATE.setInt(3, chckpr.getThread());
            iStatus = pstmtUPDATE.executeUpdate();
        } catch (Exception e) {
            System.out.println("*** ERROR: Could not perform purgeCHCKPNT on CHCKPR.");
            if (conn == null) {
                System.out.println("*** INFORMATIONAL: Could not obtain connection to database.");
            }
            //   e.printStackTrace();
        } finally {
            try {
                pstmtUPDATE.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            connMgr.freeConnection(defaultServer, conn);
        }
        return iStatus;
    }
    
       public int selectOnProcess(CHCKPR chckpr) {
        String query1 = "SELECT * FROM CHCKP" + db + " WHERE process=? AND thread=? LIMIT 1";

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
            pstmtSELECT.setInt(2, chckpr.getThread());
            rs = pstmtSELECT.executeQuery();
            if (rs.next()) {
                iStatus = 1;
                CHCKPRIO cObj=new CHCKPRIO("");
                cObj.setCHCKPR(rs, chckpr);
            }
        } catch (Exception e) {
            iStatus = -1;
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
       
    public int updateFile(CHCKPR chckpr) {
        String query1 = "UPDATE CHCKP" + db + " SET file=? WHERE process=? AND file='' AND thread=?";

        DBCM connMgr = null;
        Connection conn = null;
        PreparedStatement pstmtUPDATE = null;

        int iStatus = 0;

        try {
// open connectie
            connMgr = DBCM.getInstance();
            conn = connMgr.getConnection("SYNT");
            pstmtUPDATE = conn.prepareStatement(query1);

            pstmtUPDATE.setString(1, chckpr.getFile());
            pstmtUPDATE.setString(2, chckpr.getProcess());
            pstmtUPDATE.setInt(3, chckpr.getThread());
            iStatus = pstmtUPDATE.executeUpdate();
        } catch (Exception e) {
            System.out.println("*** ERROR: Could not perform updateFile on CHCKPR.");
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

       public int updateFileRestart(CHCKPR chckpr) {
        String query1 = "UPDATE CHCKP" + db + " SET rinfo=? WHERE process=? AND thread=?";

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
            pstmtUPDATE.setString(2, chckpr.getProcess());
            pstmtUPDATE.setInt(3, chckpr.getThread());
            iStatus = pstmtUPDATE.executeUpdate();
        } catch (Exception e) {
            System.out.println("*** ERROR: Could not perform updateFile on CHCKPR.");
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

    
    public ArrayList getAllCheckPoints() {
        String query1 = "SELECT * FROM CHCKP" + db + " ORDER BY status DESC, fdate DESC, process DESC, file DESC, thread DESC";

        DBCM connMgr = null;
        Connection conn = null;
        PreparedStatement pstmtSELECT = null;
        ResultSet rs = null;

        ArrayList arl = new ArrayList();
        CHCKPRIO cObj = new CHCKPRIO("");

        try {
// open connectie
            connMgr = DBCM.getInstance();
            conn = connMgr.getConnection(defaultServer);
            pstmtSELECT = conn.prepareStatement(query1);
            rs = pstmtSELECT.executeQuery();
            while (rs.next()) {
                CHCKPR rec = new CHCKPR();
                cObj.setCHCKPR(rs, rec);
                arl.add(rec);
            }
        } catch (Exception e) {
            System.out.println("*** ERROR: Could not perform getAllCheckPoints on CHCKPR.");
            if (conn == null) {
                System.out.println("*** INFORMATIONAL: Could not obtain connection to database.");
            }
            // e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmtSELECT != null) {
                    pstmtSELECT.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            connMgr.freeConnection(defaultServer, conn);
        }
        return arl;
    }
    
    public CHCKPR getUoW(CHCKPR chckpr) {
         String query1 = "SELECT * FROM CHCKP" + db + " WHERE process=? AND file=? AND thread=? LIMIT 1";

        DBCM connMgr = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmtSELECT = null;

        CHCKPR rObj=null;
        CHCKPRIO cObj=new CHCKPRIO("");

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
                rObj=new CHCKPR();
                cObj.setCHCKPR(rs, rObj);
            }
        } catch (Exception e) {            
            System.out.println("*** ERROR: Could not perform getUoW on CHCKPR.");
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
        return rObj;
    }

    public static void main(String args[]) {
        System.out.println("start.");
        System.out.println("done.");
    }
}
