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

public class SBMUSRRIO extends Object {

    String db = "";

    public SBMUSRRIO(String db) {
        this.db = db;
    }

    public int update(SBMUSRR sbmusrr) {
        String query1 = "UPDATE SBMUSR" + db + " SET userid=?,source=?,username=?,statelic1=?,statelic2=?,licadmisdate1=?,licadmisdate2=?,licnum1=?,licnum2=?,phonenum=?,faxnum=?,email=?,occupation=?,status=?,employer=?,jobtitle=?,addrline1=?,county=?,city=?,state=?,postcode=?,website=?,practareas=?,languages=?,sections=?,universities=?,degrees=?,yeargrad=?,barassocname=?,barassocdate=?,barassocrole=?,lat=?,lon=?,fdate=? WHERE oid=?";

        DBCM connMgr = null;
        Connection conn = null;
        PreparedStatement pstmtUPDATE = null;

        int iStatus = 0;

        try {
// open connectie
            connMgr = DBCM.getInstance();
            conn = connMgr.getConnection("NETNAV");
            pstmtUPDATE = conn.prepareStatement(query1);
            pstmtUPDATE.setInt(1, sbmusrr.getUserid());
            pstmtUPDATE.setString(2, sbmusrr.getSource());
            pstmtUPDATE.setString(3, sbmusrr.getUsername());
            pstmtUPDATE.setString(4, sbmusrr.getStatelic1());
            pstmtUPDATE.setString(5, sbmusrr.getStatelic2());
            pstmtUPDATE.setString(6, sbmusrr.getLicadmisdate1());
            pstmtUPDATE.setString(7, sbmusrr.getLicadmisdate2());
            pstmtUPDATE.setString(8, sbmusrr.getLicnum1());
            pstmtUPDATE.setString(9, sbmusrr.getLicnum2());
            pstmtUPDATE.setString(10, sbmusrr.getPhonenum());
            pstmtUPDATE.setString(11, sbmusrr.getFaxnum());
            pstmtUPDATE.setString(12, sbmusrr.getEmail());
            pstmtUPDATE.setString(13, sbmusrr.getOccupation());
            pstmtUPDATE.setString(14, sbmusrr.getStatus());
            pstmtUPDATE.setString(15, sbmusrr.getEmployer());
            pstmtUPDATE.setString(16, sbmusrr.getJobtitle());
            pstmtUPDATE.setString(17, sbmusrr.getAddrline1());
            pstmtUPDATE.setString(18, sbmusrr.getCounty());
            pstmtUPDATE.setString(19, sbmusrr.getCity());
            pstmtUPDATE.setString(20, sbmusrr.getState());
            pstmtUPDATE.setString(21, sbmusrr.getPostcode());
            pstmtUPDATE.setString(22, sbmusrr.getWebsite());
            pstmtUPDATE.setString(23, sbmusrr.getPractareas());
            pstmtUPDATE.setString(24, sbmusrr.getLanguages());
            pstmtUPDATE.setString(25, sbmusrr.getSections());
            pstmtUPDATE.setString(26, sbmusrr.getUniversities());
            pstmtUPDATE.setString(27, sbmusrr.getDegrees());
            pstmtUPDATE.setString(28, sbmusrr.getYeargrad());
            pstmtUPDATE.setString(29, sbmusrr.getBarassocname());
            pstmtUPDATE.setString(30, sbmusrr.getBarassocdate());
            pstmtUPDATE.setString(31, sbmusrr.getBarassocrole());
            pstmtUPDATE.setString(32, sbmusrr.getLat());
            pstmtUPDATE.setString(33, sbmusrr.getLon());
            pstmtUPDATE.setString(34, sbmusrr.getFdate());
            pstmtUPDATE.setInt(35, sbmusrr.getOid());
            iStatus = pstmtUPDATE.executeUpdate();
        } catch (Exception e) {
            System.out.println("*** ERROR: Could not perform UPDATE on SBMUSRR.");
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
            connMgr.freeConnection("NETNAV", conn);
        }
        return iStatus;
    }

    public int insert(SBMUSRR sbmusrr) {
        String query1 = "INSERT INTO SBMUSR" + db + " (userid,source,username,statelic1,statelic2,licadmisdate1,licadmisdate2,licnum1,licnum2,phonenum,faxnum,email,occupation,status,employer,jobtitle,addrline1,county,city,state,postcode,website,practareas,languages,sections,universities,degrees,yeargrad,barassocname,barassocdate,barassocrole,lat,lon,fdate) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        DBCM connMgr = null;
        Connection conn = null;

        PreparedStatement pstmtINSERT = null;
        // open connectie
        connMgr = DBCM.getInstance();
        conn = connMgr.getConnection("NETNAV");
        //System.out.println(sbmusrr.getUserid() + " Connected");
        //pstmtINSERT = conn.prepareStatement(query1);
        int iStatus = 0;

        try {
            // open connectie
            connMgr = DBCM.getInstance();
            conn = connMgr.getConnection("NETNAV");
            System.out.println(sbmusrr.getUserid() + " Connected");
            pstmtINSERT = conn.prepareStatement(query1);
            
            pstmtINSERT.setInt(1, sbmusrr.getUserid());
            pstmtINSERT.setString(2, sbmusrr.getSource());
            pstmtINSERT.setString(3, sbmusrr.getUsername());
            pstmtINSERT.setString(4, sbmusrr.getStatelic1());
            pstmtINSERT.setString(5, sbmusrr.getStatelic2());
            pstmtINSERT.setString(6, sbmusrr.getLicadmisdate1());
            pstmtINSERT.setString(7, sbmusrr.getLicadmisdate2());
            pstmtINSERT.setString(8, sbmusrr.getLicnum1());
            pstmtINSERT.setString(9, sbmusrr.getLicnum2());
            pstmtINSERT.setString(10, sbmusrr.getPhonenum());
            pstmtINSERT.setString(11, sbmusrr.getFaxnum());
            pstmtINSERT.setString(12, sbmusrr.getEmail());
            pstmtINSERT.setString(13, sbmusrr.getOccupation());
            pstmtINSERT.setString(14, sbmusrr.getStatus());
            pstmtINSERT.setString(15, sbmusrr.getEmployer());
            pstmtINSERT.setString(16, sbmusrr.getJobtitle());
            pstmtINSERT.setString(17, sbmusrr.getAddrline1());
            pstmtINSERT.setString(18, sbmusrr.getCounty());
            pstmtINSERT.setString(19, sbmusrr.getCity());
            pstmtINSERT.setString(20, sbmusrr.getState());
            pstmtINSERT.setString(21, sbmusrr.getPostcode());
            pstmtINSERT.setString(22, sbmusrr.getWebsite());
            pstmtINSERT.setString(23, sbmusrr.getPractareas());
            pstmtINSERT.setString(24, sbmusrr.getLanguages());
            pstmtINSERT.setString(25, sbmusrr.getSections());
            pstmtINSERT.setString(26, sbmusrr.getUniversities());
            pstmtINSERT.setString(27, sbmusrr.getDegrees());
            pstmtINSERT.setString(28, sbmusrr.getYeargrad());
            pstmtINSERT.setString(29, sbmusrr.getBarassocname());
            pstmtINSERT.setString(30, sbmusrr.getBarassocdate());
            pstmtINSERT.setString(31, sbmusrr.getBarassocrole());
            pstmtINSERT.setString(32, sbmusrr.getLat());
            pstmtINSERT.setString(33, sbmusrr.getLon());
            pstmtINSERT.setString(34, sbmusrr.getFdate());
            iStatus = pstmtINSERT.executeUpdate();
        } catch (Exception e) {
            System.out.println("*** ERROR: Could not perform INSERT on SBMUSRR for UserID " + sbmusrr.getUserid());
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
            connMgr.freeConnection("NETNAV", conn);
        }
        return iStatus;
    }

    public int select(SBMUSRR sbmusrr) {
        String query1 = "SELECT * FROM SBMUSR" + db + " WHERE oid=?";

        DBCM connMgr = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmtSELECT = null;

        int iStatus = 0;

        try {
// open connectie
            connMgr = DBCM.getInstance();
            conn = connMgr.getConnection("NETNAV");
            pstmtSELECT = conn.prepareStatement(query1);
            pstmtSELECT.setInt(1, sbmusrr.getOid());
            rs = pstmtSELECT.executeQuery();
            if (rs.next()) {
                setSBMUSRR(rs, sbmusrr);
                iStatus = 1;
            }
        } catch (Exception e) {
            System.out.println("*** ERROR: Could not perform SELECT on SBMUSRR.");
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
        return iStatus;
    }

    public void setSBMUSRR(ResultSet rs, SBMUSRR sbmusrr) throws SQLException {
//System.out.println(rs.getInt("userid"));
        sbmusrr.setUserid(rs.getInt("userid"));
        sbmusrr.setSource(rs.getString("source"));
        sbmusrr.setUsername(rs.getString("username"));
        sbmusrr.setStatelic1(rs.getString("statelic1"));
        sbmusrr.setStatelic2(rs.getString("statelic2"));
        sbmusrr.setLicadmisdate1(rs.getString("licadmisdate1"));
        sbmusrr.setLicadmisdate2(rs.getString("licadmisdate2"));
        sbmusrr.setLicnum1(rs.getString("licnum1"));
        sbmusrr.setLicnum2(rs.getString("licnum2"));
        sbmusrr.setPhonenum(rs.getString("phonenum"));
        sbmusrr.setFaxnum(rs.getString("faxnum"));
        sbmusrr.setEmail(rs.getString("email"));
        sbmusrr.setOccupation(rs.getString("occupation"));
        sbmusrr.setStatus(rs.getString("status"));
        sbmusrr.setEmployer(rs.getString("employer"));
        sbmusrr.setJobtitle(rs.getString("jobtitle"));
        sbmusrr.setAddrline1(rs.getString("addrline1"));
        sbmusrr.setCounty(rs.getString("county"));
        sbmusrr.setCity(rs.getString("city"));
        sbmusrr.setState(rs.getString("state"));
        sbmusrr.setPostcode(rs.getString("postcode"));
        sbmusrr.setWebsite(rs.getString("website"));
        sbmusrr.setPractareas(rs.getString("practareas"));
        sbmusrr.setLanguages(rs.getString("languages"));
        sbmusrr.setSections(rs.getString("sections"));
        sbmusrr.setUniversities(rs.getString("universities"));
        sbmusrr.setDegrees(rs.getString("degrees"));
        sbmusrr.setYeargrad(rs.getString("yeargrad"));
        sbmusrr.setBarassocname(rs.getString("barassocname"));
        sbmusrr.setBarassocdate(rs.getString("barassocdate"));
        sbmusrr.setBarassocrole(rs.getString("barassocrole"));
        sbmusrr.setLat(rs.getString("lat"));
        sbmusrr.setLon(rs.getString("lon"));
        sbmusrr.setFdate(rs.getString("fdate"));
    }

    public static void main(String args[]) {
        System.out.println("start.");
        SBMUSRR obj1 = new SBMUSRR();
        obj1.setOid(12);
        obj1.setUserid(13);
        obj1.setSource("14");
        obj1.setUsername("15");
        obj1.setStatelic1("16");
        obj1.setStatelic2("17");
        obj1.setLicadmisdate1("18-00-00");
        obj1.setLicadmisdate2("19-00-00");
        obj1.setLicnum1("20");
        obj1.setLicnum2("21");
        obj1.setPhonenum("22");
        obj1.setFaxnum("23");
        obj1.setEmail("24");
        obj1.setOccupation("25");
        obj1.setStatus("26");
        obj1.setEmployer("27");
        obj1.setJobtitle("28");
        obj1.setAddrline1("29");
        obj1.setCounty("30");
        obj1.setCity("31");
        obj1.setState("32");
        obj1.setPostcode("33");
        obj1.setWebsite("34");
        obj1.setPractareas("35");
        obj1.setLanguages("36");
        obj1.setSections("37");
        obj1.setUniversities("38");
        obj1.setDegrees("39");
        obj1.setYeargrad("40");
        obj1.setBarassocname("41");
        obj1.setBarassocdate("42");
        obj1.setBarassocrole("43");
        obj1.setLat("44");
        obj1.setLon("45");
        obj1.setFdate("46-00-00");

        try {
            SBMUSRRIO obj2 = new SBMUSRRIO("");
            obj2.insert(obj1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("done.");
    }

}
