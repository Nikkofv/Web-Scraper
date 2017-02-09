/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.vogel.record.copylib.tabledef;

import com.vogel.common.util.*;

import java.sql.*;

/**
 *
 * @author VOGEL
 * @version 0.1
 */
public class createTable_CHCKP extends Object {

    public createTable_CHCKP() {
    }

    public void createQUER() {

        DBCM connMgr = null;
        Connection conn = null;

        try {
            // open connectie naar CDOS
            connMgr = DBCM.getInstance();
            try {
                conn = connMgr.getConnection("SYNT");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String sQUER =
                    "create table CHCKP ("
                    + "process CHAR(64) NOT NULL,"
                    + "file CHAR(255) NOT NULL,"
                    + "thread TINYINT NOT NULL,"
                    + "rinfo TEXT NOT NULL,"
                    + "uow INTEGER NOT NULL,"
                    + "sdate DATETIME NOT NULL,"
                    + "fdate DATETIME NOT NULL,"
                    + "status TINYINT NOT NULL," // 0=dead, 1=completed, 2=alive, 3=idle
                    + "PRIMARY KEY(process,file,thread)) ENGINE=INNODB CHARACTER SET latin1 COLLATE latin1_general_ci";

            System.out.println("sQUER: " + sQUER);
            DBW.executeUpdate(conn, sQUER);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connMgr.freeConnection("SYNT", conn);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        COMMONtiming obj = new COMMONtiming();
        obj.start();
        createTable_CHCKP tp = new createTable_CHCKP();
        tp.createQUER();
        System.out.println("lapsed time: " + obj.lapsedTime());
    }
}
