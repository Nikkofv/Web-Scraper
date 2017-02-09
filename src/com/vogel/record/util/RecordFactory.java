/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.vogel.record.util;

import java.util.*;
import com.vogel.common.tokenFactory.purifyToken;
import com.vogel.common.util.COMMONtoolkit;

/**
 *
 * @author VOGEL
 * @version
 */
public class RecordFactory extends Object {

    public ArrayList arlHeader = null;
    public String headers = null;
    private String myString = null;
    private String original = null;
    public String delimiter = null;
    private HashMap hm = null;
    purifyToken pt = null;
    private String idnHeader = null;

    private int iSeqer = 0;

    public RecordFactory(String headers, HashMap hm) throws Exception {
        this(headers);
        this.hm = hm;
    }

    public RecordFactory(String headers) throws Exception {
        arlHeader = new ArrayList();
        this.headers = headers.toUpperCase();
        pt = new purifyToken();
        if (headers.contains("\t")) {
            this.delimiter = "\t";
        } else if (headers.contains("|")) {
            this.delimiter = "|";
        } else if (headers.contains("~")) {
            this.delimiter = "~";
        } else if (headers.contains(",")) {
            this.delimiter = ",";
        } else {
            this.delimiter = "\t";
        }
        registerHeaders();
    }

    public RecordFactory(String headers, String delimiter) {
        arlHeader = new ArrayList();
        this.delimiter = delimiter;
        this.headers = headers.toUpperCase();
        pt = new purifyToken();
        registerHeaders();
    }

    private void registerHeaders() {
        HashSet hsDup = new HashSet();
        int iIndex = headers.indexOf(delimiter);
        String header;
        int iStart = 0;
        while (iIndex > -1) {
            header = headers.substring(iStart, iIndex);
            header = pt.removeEnclosingQuotes(header);
            if (header.indexOf('@') > -1) {
                header = header.substring(0, header.indexOf('@'));

            }
            header = header.trim();
            if (!hsDup.contains(header)) {
                arlHeader.add(header);
                hsDup.add(header);
            } else {
               // System.out.println("***** DUPLICATE HEADER " + header + " BECOMES " + header + "_X" + iSeqer);
                // System.exit(0);
                arlHeader.add(header + "_X" + iSeqer++);
            }
            iStart = iIndex + delimiter.length();
            iIndex = headers.indexOf(delimiter, iStart);
        }
        header = headers.substring(iStart);
        header = pt.removeEnclosingQuotes(header);
        if (header.indexOf('@') > -1) {
            header = header.substring(0, header.indexOf('@'));

        }
        header = header.trim();
        if (!hsDup.contains(header)) {
            arlHeader.add(header);
            hsDup.add(header);
        } else {
            // System.out.println("***** DUPLICATE HEADER " + header + " BECOMES " + header + "_X" + iSeqer);
            // System.exit(0);
            arlHeader.add(header + "_X" + iSeqer++);
        }
    }

    public void setIDNHeader(String header) {
        idnHeader = header;
    }

    public String getIDNHeader() {
        return idnHeader;
    }

    public String getHeader(int i) {
        if (i >= 0 && i < arlHeader.size()) {
            return (String) arlHeader.get(i);
        } else {
            return "";
        }
    }

    public int getHeaderIndex(String header) {
        for (int i = 0; i < arlHeader.size(); i++) {
            if (arlHeader.get(i).equals(header)) {
                return i;
            }
        }
        return -1;
    }
    
      public int dropHeaderColumn(String header) {
          int iDrop=-1;
        for (int i = 0; i < arlHeader.size(); i++) {
            if (arlHeader.get(i).equals(header)) {
                iDrop=i;
            }
        }
        if (iDrop>-1) {
            arlHeader.remove(iDrop);
        }
        return iDrop;
    }

    public String getNextHeader(String leadingHeader) {
        return getHeader(getHeaderIndex(leadingHeader) + 1);
    }

    public String tabPadding(String rString) {
        int iCnt = 0;
        int iIndex = rString.indexOf(delimiter);
        while (iIndex > -1) {
            iCnt++;
            iIndex = rString.indexOf(delimiter, iIndex + delimiter.length());
        }
        if (iCnt < (HeaderCnt() - 1)) {
            for (int i = 0; i < (HeaderCnt() - 1 - iCnt); i++) {
                rString = rString + delimiter;
            }
        }
        return rString;
    }

    public int HeaderCnt() {
        return arlHeader.size();
    }

    public HashMap index(String rString) {
        this.myString = rString.toUpperCase();
        this.original = rString;
        hm = new HashMap();
        for (Object arlHeader1 : arlHeader) {
            String header = (String) arlHeader1;
            String value = COMMONtoolkit.getInstance().removeDoubleSpacesTrim(pt.removeEnclosingQuotes(fetch()));
            hm.put(header, value);
        }
        return hm;
    }

    public HashMap indexCaseSensitive(String rString) {
        this.myString = rString;
        this.original = rString;
        hm = new HashMap();
        for (Object arlHeader1 : arlHeader) {
            String header = (String) arlHeader1;
            String value = COMMONtoolkit.getInstance().removeDoubleSpacesTrim(pt.removeEnclosingQuotes(fetch()));
            hm.put(header, value);
        }
        return hm;
    }

    
    
    public static RecordFactory makeRf(HashMap hm) {
        String sHeaders = "";
        String sValues = "";
        Set keys = hm.keySet();
        Iterator ite = keys.iterator();
        while (ite.hasNext()) {
            String key = (String) ite.next();
            if (sHeaders.isEmpty()) {
                sHeaders = key;
                sValues = (String) hm.get(key);
            } else {
                sHeaders += ("\t" + key);
                sValues += ("\t" + (String) hm.get(key));
            }
        }
        RecordFactory rf = null;
        try {
            rf = new RecordFactory(sHeaders);
            rf.index(sValues);
        } catch (Exception e) {
        }
        return rf;
    }
    
    public String getOriginal() {
        return this.original;
    }

    public void replaceValue(String column, String value) {
        hm.put(column, value.toUpperCase().trim());
    }

    public String fetchLEFT(String columnRight) {
        String rString = "";
        columnRight = COMMONtoolkit.getInstance().removeDoubleSpacesTrim(columnRight.toUpperCase());
        for (Object arlHeader1 : arlHeader) {
            String key = (String) arlHeader1;
            if (key.equals(columnRight)) {
                break;
            } else {
                String value = (String) hm.get(key);
                if (rString.isEmpty()) {
                    rString = value;
                } else {
                    rString = rString + delimiter + value;
                }
            }
        }
        return rString;
    }

    public String fetchInclusiveLEFT(String columnLeft) {
        String rString = "";
        columnLeft = COMMONtoolkit.getInstance().removeDoubleSpacesTrim(columnLeft.toUpperCase());
        for (Object arlHeader1 : arlHeader) {
            String key = (String) arlHeader1;
            String value = (String) hm.get(key);
            if (rString.isEmpty()) {
                rString = value;
            } else {
                rString = rString + delimiter + value;
            }
            if (key.equals(columnLeft)) {
                break;
            }
        }
        return rString;
    }

    public String fetchRIGHT(String columnLeft) {
        String rString = "";
        columnLeft = COMMONtoolkit.getInstance().removeDoubleSpacesTrim(columnLeft.toUpperCase());
        boolean bStart = false;
        for (Object arlHeader1 : arlHeader) {
            String key = (String) arlHeader1;
            if (key.equals(columnLeft)) {
                bStart = true;
            }
            if (bStart) {
                String value = (String) hm.get(key);
                if (rString.isEmpty()) {
                    rString = value;
                } else {
                    rString = rString + delimiter + value;
                }
            }
        }
        return rString;
    }

    public String fetchDisclusiveRIGHT(String columnLeft) {
        String rString = "";
        columnLeft = columnLeft.toUpperCase();
        boolean bStart = false;
        for (Object arlHeader1 : arlHeader) {
            String key = (String) arlHeader1;
            if (bStart) {
                String value = (String) hm.get(key);
                if (rString.isEmpty()) {
                    rString = value;
                } else {
                    rString = rString + delimiter + value;
                }
            }
            if (key.equals(columnLeft)) {
                bStart = true;
            }
        }
        return rString;
    }

    public String fetchMID(String columnLeft, String columnRight) {
        String rString = "";
        columnLeft = COMMONtoolkit.getInstance().removeDoubleSpacesTrim(columnLeft.toUpperCase());
        columnRight = COMMONtoolkit.getInstance().removeDoubleSpacesTrim(columnRight.toUpperCase());
        boolean bStart = false;
        for (Object arlHeader1 : arlHeader) {
            String key = (String) arlHeader1;
            if (key.equals(columnLeft)) {
                bStart = true;
            }
            if (key.equals(columnRight)) {
                break;
            }
            if (bStart) {
                String value = (String) hm.get(key);
                if (rString.isEmpty()) {
                    rString = value;
                } else {
                    rString = rString + delimiter + value;
                }
            }
        }
        return rString;
    }

    public String fetchInclusiveMID(String columnLeft, String columnRight) {
        String rString = "";
        columnLeft = COMMONtoolkit.getInstance().removeDoubleSpacesTrim(columnLeft.toUpperCase());
        columnRight = COMMONtoolkit.getInstance().removeDoubleSpacesTrim(columnRight.toUpperCase());
        boolean bStart = false;
        String value;
        for (Object arlHeader1 : arlHeader) {
            String key = (String) arlHeader1;
            if (key.equals(columnLeft)) {
                bStart = true;
            }
            if (bStart) {
                value = (String) hm.get(key);
                if (rString.isEmpty()) {
                    rString = value;
                } else {
                    rString = rString + delimiter + value;
                }
            }
            if (key.equals(columnRight)) {
                break;
            }
        }
        return rString;
    }

    @Override
    public String toString() {
        String rString = "";
        int iCnt=0;
        for (Object arlHeader1 : arlHeader) {
            String key = (String) arlHeader1;
            String value = (String) hm.get(key);
            if (iCnt==0) {
                rString = value;
            } else {
                rString = rString + delimiter + value;
            }
            iCnt++;
        }
        return rString;
    }

    public String toString(String myDelimiter) {
        String rString = "";
        int iCnt=0;
        for (Object arlHeader1 : arlHeader) {
            String key = (String) arlHeader1;
            String value = (String) hm.get(key);
            if (iCnt==0) {
                rString = value;
            } else {
                rString = rString + myDelimiter + value;
            }
            iCnt++;
        }
        return rString;
    }
    
    public String fetchTaggedRecord() {
        String rString = "";
        for (Object arlHeader1 : arlHeader) {
            String key = (String) arlHeader1;
            String value = (String) hm.get(key);
            rString = rString + (key + "=" + value + "\n");
        }
        return rString;
    }

    public String fetchValueFor(String rString) {
        String sValue = (String) hm.get(COMMONtoolkit.getInstance().removeDoubleSpacesTrim(rString.toUpperCase()));
        if (sValue == null) {
            sValue = "";
        }
        return sValue.toUpperCase().trim();
    }

    public String fetchValueForCaseSensitive(String rString) {
        String sValue = (String) hm.get(COMMONtoolkit.getInstance().removeDoubleSpacesTrim(rString.toUpperCase()));
        if (sValue == null) {
            sValue = "";
        }
        return sValue.trim();
    }

    public String fetchValueAsIs(String rString) {
        String sValue = (String) hm.get(COMMONtoolkit.getInstance().removeDoubleSpacesTrim(rString.toUpperCase()));
        if (sValue == null) {
            sValue = "";
        }
        return sValue;
    }

    public String fetchValueFor(String rString, HashMap myHM) {
        String sValue = (String) myHM.get(COMMONtoolkit.getInstance().removeDoubleSpacesTrim(rString.toUpperCase()));
        if (sValue == null) {
            sValue = "";
        }
        return sValue.toUpperCase().trim();
    }

    public boolean columnExists(String rString) {
        return hm.containsKey(rString.toUpperCase());
    }

    private String fetch() {
        String aToken;
        int iIndex = myString.indexOf(this.delimiter);
        if (iIndex == -1) {
            aToken = myString;
            myString = "";
        } else {
            aToken = myString.substring(0, iIndex);
            myString = myString.substring(iIndex + delimiter.length());
        }
        //  System.out.println("**** THE TOKEN: "+aToken);
        return aToken;
    }

    public static void main(String args[]) {

        System.out.println("start.");
        try {
            RecordFactory tp = new RecordFactory("IDN\tTEST");
            tp.index("6707025898184\tone|two|three");
            System.out.println(tp.fetchTaggedRecord());
        } catch (Exception e) {
        }
        System.out.println("done.");
    }
}
