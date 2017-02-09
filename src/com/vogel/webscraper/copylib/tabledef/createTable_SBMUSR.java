/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.vogel.webscraper.copylib.tabledef;

import com.vogel.common.util.*;

import java.sql.*;

/**
 *
 * @author Nicholas "Nikko" F. Vogel
 * @version 0.1
 */
public class createTable_SBMUSR extends Object {

    public createTable_SBMUSR() {
    }

    public void createQUER() {

        DBCM connMgr = null;
        Connection conn = null;

        try {
            // open connectie naar CDOS
            connMgr = DBCM.getInstance();
            try {
                conn = connMgr.getConnection("NETNAV");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String sQUER
                    = "create table SBMUSR ("
                    + "oid INTEGER AUTO_INCREMENT,"
                    + "userid INTEGER NOT NULL,"
                    + "source VARCHAR(64),"
                    + "username VARCHAR(128) NOT NULL," 
                    + "statelic1 VARCHAR(16) NOT NULL,"
                    + "statelic2 VARCHAR(16) NOT NULL,"
                    + "licadmisdate1 DATE NOT NULL,"
                    + "licadmisdate2 DATE NOT NULL,"
                    + "licnum1 VARCHAR(128) NOT NULL,"
                    + "licnum2 VARCHAR(128) NOT NULL,"
                    + "phonenum VARCHAR(16) NOT NULL,"
                    + "faxnum VARCHAR(16) NOT NULL,"
                    + "email VARCHAR(128) NOT NULL,"
                    + "occupation VARCHAR(128) NOT NULL," 
                    + "status VARCHAR(32) NOT NULL,"
                    + "employer VARCHAR(128) NOT NULL,"
                    + "jobtitle VARCHAR(128) NOT NULL,"
                    + "addrline1 VARCHAR(256) NOT NULL," 
                    + "county VARCHAR(128) NOT NULL,"
                    + "city VARCHAR(128) NOT NULL,"
                    + "state VARCHAR(8) NOT NULL,"
                    + "postcode VARCHAR(16) NOT NULL,"
                    + "website VARCHAR(128) NOT NULL,"
                    + "practareas VARCHAR(512) NOT NULL,"
                    + "languages VARCHAR(128) NOT NULL,"
                    + "sections VARCHAR(512) NOT NULL,"
                    + "universities VARCHAR(256) NOT NULL,"
                    + "degrees VARCHAR(256) NOT NULL,"
                    + "yeargrad VARCHAR(64) NOT NULL,"
                    + "barassocname VARCHAR(256) NOT NULL,"
                    + "barassocdate VARCHAR(64) NOT NULL,"
                    + "barassocrole VARCHAR(256) NOT NULL,"
                    + "lat VARCHAR(8) NOT NULL,"
                    + "lon VARCHAR(8) NOT NULL,"
                    + "fdate DATE NOT NULL,"
                    + "PRIMARY KEY(oid),"
                    + "INDEX IDEX1(userid,source)) ENGINE=INNODB CHARACTER SET utf16 COLLATE utf16_general_ci";

            System.out.println("sQUER: " + sQUER);
            DBW.executeUpdate(conn, sQUER);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connMgr.freeConnection("NETNAV", conn);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        COMMONtiming obj = new COMMONtiming();
        obj.start();
        createTable_SBMUSR tp = new createTable_SBMUSR();
        tp.createQUER();
        System.out.println("lapsed time: " + obj.lapsedTime());
    }
}
