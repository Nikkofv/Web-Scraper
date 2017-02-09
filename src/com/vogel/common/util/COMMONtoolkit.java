/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.vogel.common.util;

import java.util.*;
import java.text.DecimalFormat;
import java.io.StringReader;
import java.util.List;

import com.vogel.common.tokenFactory.purifyToken;
import com.vogel.common.util.supercsv.io.*;
import com.vogel.common.util.supercsv.prefs.CsvPreference;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

/**
 *
 * @author VOGEL
 * @version
 */
public class COMMONtoolkit extends java.lang.Object {

    private static COMMONtoolkit myInstance = null;
    DecimalFormat df = new DecimalFormat("0000");
    DecimalFormat df3 = new DecimalFormat("000");
    DecimalFormat df2 = new DecimalFormat("00");
    ArrayList arlAGM = null;
    HashMap hmMONTHS = null;
    HashMap hmMONTHS2 = null;
    ArrayList arlLSM2011 = null;
    // remove double space
    Pattern rds = null;
    // get digits
    Pattern gdg = null;
    // trim space
    Pattern trm = null;
    Pattern rmb = null;

    public static synchronized COMMONtoolkit getInstance() {
        if (myInstance == null) {
            myInstance = new COMMONtoolkit();
        }
        return myInstance;
    }

    public COMMONtoolkit() {

        rds = Pattern.compile("^[\\s]+|[\\s]+|[\\s]+$");
        trm = Pattern.compile("^[\\s]+|[\\s]+$");
        gdg = Pattern.compile("\\D");
        rmb = Pattern.compile("\\(.*\\)|\\[.*\\]|\\{.*\\}");

        arlAGM = new ArrayList();
        arlAGM.add("003TODDLER");
        arlAGM.add("006PRESCHOOLER");
        arlAGM.add("011SCHOOL AGE");
        arlAGM.add("016PUBERTY");
        arlAGM.add("022EARLY YOUNG ADULT");
        arlAGM.add("028LATE YOUNG ADULT");
        arlAGM.add("034EARLY THIRTIES");
        arlAGM.add("045LATE THIRTIES");
        arlAGM.add("055MIDDLE AGE");
        arlAGM.add("075LATE ADULTHOOD");
        arlAGM.add("999OLD AGE");

        arlLSM2011 = new ArrayList();
        arlLSM2011.add("LSM01#1493");
        arlLSM2011.add("LSM02#1732");
        arlLSM2011.add("LSM03#2052");
        arlLSM2011.add("LSM04#2829");
        arlLSM2011.add("LSM05#3832");
        arlLSM2011.add("LSM06#6398");
        arlLSM2011.add("LSM07#10824");
        arlLSM2011.add("LSM08#14470");
        arlLSM2011.add("LSM09#20028");
        arlLSM2011.add("LSM10#30323");

        hmMONTHS2 = new HashMap();
        hmMONTHS2.put("JAN", "01");
        hmMONTHS2.put("FEB", "02");
        hmMONTHS2.put("MAR", "03");
        hmMONTHS2.put("APR", "04");
        hmMONTHS2.put("MAY", "05");
        hmMONTHS2.put("JUN", "06");
        hmMONTHS2.put("JUL", "07");
        hmMONTHS2.put("AUG", "08");
        hmMONTHS2.put("SEP", "09");
        hmMONTHS2.put("OCT", "10");
        hmMONTHS2.put("NOV", "11");
        hmMONTHS2.put("DEC", "12");

        hmMONTHS = new HashMap();
        hmMONTHS.put("JAN", "01");
        hmMONTHS.put("FEB", "02");
        hmMONTHS.put("MAR", "03");
        hmMONTHS.put("APR", "04");
        hmMONTHS.put("MAY", "05");
        hmMONTHS.put("JUN", "06");
        hmMONTHS.put("JUL", "07");
        hmMONTHS.put("AUG", "08");
        hmMONTHS.put("SEP", "09");
        hmMONTHS.put("OCT", "10");
        hmMONTHS.put("NOV", "11");
        hmMONTHS.put("DEC", "12");

        hmMONTHS.put("JANUARY", "01");
        hmMONTHS.put("FEBRUARY", "02");
        hmMONTHS.put("MARCH", "03");
        hmMONTHS.put("APRIL", "04");
        hmMONTHS.put("MAY", "05");
        hmMONTHS.put("JUNE", "06");
        hmMONTHS.put("JULY", "07");
        hmMONTHS.put("AUGUST", "08");
        hmMONTHS.put("SEPTEMBER", "09");
        hmMONTHS.put("OCTOBER", "10");
        hmMONTHS.put("NOVEMBER", "11");
        hmMONTHS.put("DECEMBER", "12");

        hmMONTHS.put("JANUARIE", "01");
        hmMONTHS.put("FEBRUARIE", "02");
        hmMONTHS.put("MAART", "03");
        hmMONTHS.put("APRIL", "04");
        hmMONTHS.put("MEI", "05");
        hmMONTHS.put("MEL", "05");
        hmMONTHS.put("JUNIE", "06");
        hmMONTHS.put("JULIE", "07");
        hmMONTHS.put("AUGUSTUS", "08");
        hmMONTHS.put("SEPTEMBER", "09");
        hmMONTHS.put("OCTOBER", "10");
        hmMONTHS.put("OKTOBER", "10");

        hmMONTHS.put("NOVEMBER", "11");
        hmMONTHS.put("DESEMBER", "12");

        hmMONTHS.put("1", "01");
        hmMONTHS.put("2", "02");
        hmMONTHS.put("3", "03");
        hmMONTHS.put("4", "04");
        hmMONTHS.put("5", "05");
        hmMONTHS.put("6", "06");
        hmMONTHS.put("7", "07");
        hmMONTHS.put("8", "08");
        hmMONTHS.put("9", "09");
        hmMONTHS.put("10", "10");
        hmMONTHS.put("11", "11");
        hmMONTHS.put("12", "12");

        hmMONTHS.put("01", "01");
        hmMONTHS.put("02", "02");
        hmMONTHS.put("03", "03");
        hmMONTHS.put("04", "04");
        hmMONTHS.put("05", "05");
        hmMONTHS.put("06", "06");
        hmMONTHS.put("07", "07");
        hmMONTHS.put("08", "08");
        hmMONTHS.put("09", "09");
        hmMONTHS.put("10", "10");
        hmMONTHS.put("11", "11");
        hmMONTHS.put("12", "12");

    }

    public String removeBetweenBrackets(String rString) {
        //()
        int iIndex = rString.lastIndexOf('(');
        //     System.out.println("**** index: "+iIndex);
        int iSubIndex = rString.indexOf(')', iIndex);
        //    System.out.println("**** subindex: "+iSubIndex);
        while (iIndex > -1 && iSubIndex > -1 && iIndex < iSubIndex) {
            rString = rString.substring(0, iIndex).trim() + " " + rString.substring(iSubIndex + 1).trim();
            iIndex = rString.lastIndexOf('(');
            iSubIndex = rString.indexOf(')', iIndex);
        }

        //{}
        iIndex = rString.lastIndexOf('{');
        iSubIndex = rString.indexOf('}', iIndex);
        while (iIndex > -1 && iSubIndex > -1 && iIndex < iSubIndex) {
            rString = rString.substring(0, iIndex).trim() + " " + rString.substring(iSubIndex + 1).trim();
            iIndex = rString.lastIndexOf('{');
            iSubIndex = rString.indexOf('}', iIndex);
        }
        //[]
        iIndex = rString.lastIndexOf('[');
        iSubIndex = rString.indexOf(']', iIndex);
        while (iIndex > -1 && iSubIndex > -1 && iIndex < iSubIndex) {
            rString = rString.substring(0, iIndex).trim() + " " + rString.substring(iSubIndex + 1).trim();
            iIndex = rString.lastIndexOf('[');
            iSubIndex = rString.indexOf(']', iIndex);
        }
        return rString;
    }

    public String getRatio(long i, int j, int sig) {
        String rString = "0.0%";
        DecimalFormat df2 = new DecimalFormat("00");
        double r = ((double) i) / ((double) j) * 100.0;
        int iR = 0;
        if (sig == 1) {
            r = (r + 0.05) * 10.0;
            iR = (int) r;
            rString = "" + iR / 10 + "." + iR % 10 + "%";
        } else {
            r = (r + 0.005) * 100.0;
            iR = (int) r;
            rString = "" + iR / 100 + "." + df2.format(iR % 100) + "%";
        }
        return rString;
    }

    public int getRatioD(double i, double j, int sig) {
        double r = (((double) i) / ((double) j)) * 100.0;
        int iR = 0;
        if (sig == 1) {
            r = (r * 10.0) + 0.5;
            iR = (int) r;
        } else {
            if (sig == 0) {
                r = (r + 0.5) / 100.0;
                iR = (int) r;
            } else {
                r = (r * 100.0) + 0.5;
                iR = (int) r;
            }
        }
        return iR;
    }

    public String convertDate(String date) {
        try {
            date = date.toUpperCase();
            StringTokenizer st = new StringTokenizer(date, "- ./\\");
            if (st.countTokens() <= 2) {
                // seperate digits from characters as an attempt to save the day
                String aToken = "";
                boolean bSwap = false;
                for (int i = 0; i < date.length(); i++) {
                    if (Character.isDigit(date.charAt(i))) {
                        if (bSwap) {
                            aToken = aToken + " " + date.charAt(i);
                            bSwap = false;
                        } else {
                            aToken = aToken + date.charAt(i);
                        }
                    } else {
                        if (!bSwap) {
                            aToken = aToken + " " + date.charAt(i);
                            bSwap = true;
                        } else {
                            aToken = aToken + date.charAt(i);
                        }
                    }
                }
                date = aToken;
                st = new StringTokenizer(date, "- /\\");
            }
            if (st.countTokens() >= 3) {
                // mar 9 2014
                // 9 mar 14
                // 21/JAN/08
                // 10/28/2013
                String dum = st.nextToken();
                String part1 = getDigits(dum);
                String part2 = st.nextToken();
                String month = "";
                if (part1.isEmpty() && hmMONTHS.get(dum) != null) {
                    month = (String) hmMONTHS.get(dum);
                    part1 = part2;
                    part2 = dum;
                } else {
                    month = (String) hmMONTHS.get(part2);
                }
                if (month == null) {
                    //  System.out.println("*** UNKNOW MONTH: " + part2);
                    return "0000-00-00";

                }
                // we must assume the last part to be year of size == 2;
                String part3 = getDigits(st.nextToken());
                if (part1.length() <= part3.length()) {
                    dum = part3;
                    part3 = part1;
                    part1 = dum;
                }

                if (part1.length() > 4 || part1.length() == 0) {
                    return "0000-00-00";
                }
                if (part3.length() > 2 || part3.length() == 0) {
                    return "0000-00-00";
                }
                if (part1.length() == 2) {
                    int iYear = Integer.parseInt(part1);
                    if (iYear >= 0 && iYear <= (COMMONdate.getInstance().getThisYear() % 2000)) {
                        part1 = "20" + part1;
                    } else {
                        part1 = "19" + part1;
                    }
                }
                String dateNew = part1 + "-" + month + "-" + part3;
                return reformatDate(dateNew);
            } else {
                if (st.countTokens() == 1) {
                    String aToken = st.nextToken();
                    if (aToken.length() == 8) {
                        try {
                            int iYear = Integer.parseInt(aToken.substring(4));
                            if (iYear >= 1900 && iYear <= 2020) {
                                aToken = aToken.substring(4) + "-" + aToken.substring(2, 4) + "-" + aToken.substring(0, 2);
                            } else {
                                iYear = Integer.parseInt(aToken.substring(0, 4));
                                if (iYear >= 1900 && iYear <= 2020) {
                                    aToken = aToken.substring(0, 4) + "-" + aToken.substring(4, 6) + "-" + aToken.substring(6, 8);
                                }
                            }
                        } catch (Exception e) {
                        }
                        aToken = COMMONdate.getInstance().isDatum(aToken);
                        return aToken;
                    } else {
                        if (aToken.length() >= 6) {
                            // first six digits of an id-number
                            String bToken = "0000-00-00";
                            try {
                                bToken = COMMONtoolkit.getInstance().getBirthdDashedFromIDN(aToken);
                            } catch (Exception e) {
                            }
                            bToken = COMMONdate.getInstance().isDatum(bToken);
                            if (bToken.equals("0000-00-00") && aToken.length() == 7) {
                                if (aToken.length() == 7) {
                                    aToken = "0" + aToken;
                                    try {
                                        int iYear = Integer.parseInt(aToken.substring(4));
                                        if (iYear >= 1900 && iYear <= 2020) {
                                            aToken = aToken.substring(4) + "-" + aToken.substring(2, 4) + "-" + aToken.substring(0, 2);
                                        }
                                    } catch (Exception e) {
                                    }
                                    aToken = COMMONdate.getInstance().isDatum(aToken);
                                    return aToken;
                                }
                            } else {
                                return bToken;
                            }
                        }
                    }
                }
                return "0000-00-00";
            }
        } catch (Exception e) {
            return "0000-00-00";
        }
    }

    public String getDigits(String rString) {
        return gdg.matcher(rString).replaceAll("");
    }

    public String removeBrackets(String rString) {
        return removeDoubleSpacesTrim(rString.
                replace('[', ' ').
                replace(']', ' ').
                replace('{', ' ').
                replace('}', ' ').
                replace('(', ' ').
                replace(')', ' '));
    }

    public String removeDoubleSpacesTrim(String rString) {
        //  rString=rString.trim();
        rString = trm.matcher(rds.matcher(rString).replaceAll(" ")).replaceAll("");
        return rString;
    }

    public String removeLeadingZeros(String rString) {
        boolean bDone = false;
        while (!bDone && !rString.isEmpty()) {
            if (rString.charAt(0) == '0') {
                rString = rString.substring(1);
            } else {
                bDone = true;
            }
        }
        return rString;
    }

    public String getLSMForIncome(int iIncome) {
        String rString = "LSM00";
        if (iIncome >= 1493) {
            for (int i = arlLSM2011.size() - 1; i >= 0; i--) {
                String aToken = (String) arlLSM2011.get(i);
                int iThreshold = Integer.parseInt(aToken.substring(6));
                if (iIncome >= iThreshold) {
                    rString = aToken.substring(0, 5);
                    break;
                }
            }
        }
        return rString;
    }

    public int getLSMRatingForIncome(StringBuilder log, int iIncome) {
        String lsm = getLSMForIncome(iIncome);
        log.append("******* DETERMINED LSM: " + lsm);
        int rated = Integer.parseInt(getDigits(getLSMForIncome(iIncome))) << 2;
        log.append("******* SCORED WITH ").append(rated).append(" POINTS\n");
        return rated;
    }

    public int getDelphiRating(StringBuilder log, int iScore) {
        int rated = Math.min(20, (int) ((0.067 * (iScore - Math.min(iScore, 434) + 0.5))));
        log.append("******* DELPHPI SCORE IS RATED: ").append(rated).append("\n");
        return rated;
    }

    public int getARRating(StringBuilder log, int arr) {
        int rated = 0;
        if (arr == 0) {
            rated = 10;
        } else {
            rated = 0;
        }
        log.append("******* ARREARS INSTALLMENTS IS RATED: ").append(rated).append("\n");
        return rated;
    }

    public int getIRRating(StringBuilder log, int irr) {
        int rated = Math.max(0, 21 - irr / 10);
        log.append("******* INSTALLMENT/EXPOSURE RATIO IS RATED: ").append(rated).append("\n");
        return rated;
    }

    public int getCSPRating(StringBuilder log, ArrayList arlOpen, ArrayList arlCurrent) {
        int iScoreOpen = 0;
        if (arlOpen.isEmpty()) {
            log.append("***** NO OPEN ACCOUNTS FOUND.\n");
        } else {
            log.append("***** TESTING IF ACCOUNTS WERE OPENED IN THE LAST 365 DAYS.\n");
        }
        for (int i = 0; i < arlOpen.size(); i++) {
            String date = (String) arlOpen.get(i);
            if (COMMONdate.getInstance().getDifferenceInDays(COMMONdate.getInstance().getTodayDashed(), date) <= 365) {
                iScoreOpen += 3;
                log.append("******* POINTS EARNED: ").append(iScoreOpen).append("\n");
            }
        }
        iScoreOpen = Math.min(10, iScoreOpen);
        log.append("***** POINTS AWARDED: ").append(iScoreOpen).append("\n");
        /*
         * int iScoreCurrent = 0; for (int i = 0; i < arlOpen.size(); i++) {
         * String date = (String) arlOpen.get(i); if
         * (COMMONdate.getInstance().getDifferenceInDays(COMMONdate.getInstance().getTodayDashed(),
         * date) <= 180) { iScoreCurrent += 3; } }
         * iScoreCurrent=Math.min(10,iScoreCurrent);
         *
         */
        return iScoreOpen;
    }

    public String getLastBlockDigits(String rString) {
        String aToken = "";
        boolean bDone = false;
        purifyToken pObj = new purifyToken();
        rString = pObj.removePunctuationsAndSpace(rString);
        // we need to take the right most digits, and if letter 'O' is used amidst digits or at end, translate into '0' (zero)
        for (int i = (rString.length() - 1); i >= 0; i--) {
            if (Character.isDigit(rString.charAt(i))) {
                aToken = rString.charAt(i) + aToken;
                bDone = true;
            } else {
                //System.out.println("**("+rString.charAt(i)+")");
                if (bDone && (rString.charAt(i) == 'o' || rString.charAt(i) == 'O') && aToken.length() > 0) {
                    aToken = "0" + aToken;
                } else if (bDone) {
                    break;
                }
            }
        }
        // remove trailing zero's
        while (aToken.length() > 0) {
            if (aToken.charAt(0) == '0') {
                aToken = aToken.substring(1);
            } else {
                break;
            }
        }
        return aToken;
    }

    public String getLastBlockDigitsProper(String rString) {
        String aToken = "";
        boolean bDone = false;

        // we need to take the right most digits, and if letter 'O' is used amidst digits or at end, translate into '0' (zero)
        for (int i = (rString.length() - 1); i >= 0; i--) {
            if (Character.isDigit(rString.charAt(i))) {
                aToken = rString.charAt(i) + aToken;
                bDone = true;
            } else {
                //System.out.println("**("+rString.charAt(i)+")");
                if (bDone && (rString.charAt(i) == 'o' || rString.charAt(i) == 'O') && aToken.length() > 0) {
                    aToken = "0" + aToken;
                } else if (bDone) {
                    break;
                }
            }
        }
        // remove trailing zero's
        while (aToken.length() > 0) {
            if (aToken.charAt(0) == '0') {
                aToken = aToken.substring(1);
            } else {
                break;
            }
        }
        return aToken;
    }

    public String getFirstBlockDigits(String rString) {
        String aToken = "";
        boolean bDone = false;
        purifyToken pObj = new purifyToken();
        rString = pObj.removePunctuationsAndSpace(rString);
        // we need to take the right most digits, and if letter 'O' is used amidst digits or at end, translate into '0' (zero)
        for (int i = 0; i < rString.length(); i++) {
            if (Character.isDigit(rString.charAt(i))) {
                if (!(rString.charAt(i) == '0' && aToken.length() == 0)) {
                    aToken = aToken + rString.charAt(i);
                }
                // skip leading zero's
                if (rString.charAt(i) != '0') {
                    bDone = true;
                }
            } else {
                if (bDone && (rString.charAt(i) == 'o' || rString.charAt(i) == 'O') && aToken.length() > 0) {
                    aToken = aToken + "0";
                } else if (bDone) {
                    break;
                }
            }
        }
        // remove trailing zero's
        while (aToken.length() > 0) {
            if (aToken.charAt(0) == '0') {
                aToken = aToken.substring(1);
            } else {
                break;
            }
        }
        return aToken;
    }

    public String getFirstBlockDigitsProper(String rString) {
        String aToken = "";
        boolean bDone = false;

        // we need to take the right most digits, and if letter 'O' is used amidst digits or at end, translate into '0' (zero)
        for (int i = 0; i < rString.length(); i++) {
            if (Character.isDigit(rString.charAt(i))) {
                if (!(rString.charAt(i) == '0' && aToken.length() == 0)) {
                    aToken = aToken + rString.charAt(i);
                }
                // skip leading zero's
                if (rString.charAt(i) != '0') {
                    bDone = true;
                }
            } else {
                if (bDone && (rString.charAt(i) == 'o' || rString.charAt(i) == 'O') && aToken.length() > 0) {
                    aToken = aToken + "0";
                } else if (bDone) {
                    break;
                }
            }
        }
        // remove trailing zero's
        while (aToken.length() > 0) {
            if (aToken.charAt(0) == '0') {
                aToken = aToken.substring(1);
            } else {
                break;
            }
        }
        return aToken;
    }

    public String getFirstBlockErfNumber(String rString) {
        StringTokenizer st = new StringTokenizer(rString, "\t ");
        rString = "";
        while (st.hasMoreTokens()) {
            String aToken = st.nextToken();
            if (aToken.matches("^[0-9]+$")) {
                return aToken;
            } else if (aToken.matches("^[A-Z]{1}[0-9]+$")) {
                return getDigits(aToken);
            } else if (aToken.matches("^[0-9]+[A-Z ]+$")) {
                return getDigits(aToken);
            } else if (aToken.matches("^[0-9]+/[0-9]+$")) {
                return aToken;
            }
        }

        return "";
    }

    public String removeDoubleQuotes(String rString) {
        return rString.replaceAll("\"", "");
    }

    public String formatPostalCode(int iPostalcode) {
        return df.format(new Integer(iPostalcode));
    }

    public String formatTHREE(int iPostalcode) {
        return df3.format(new Integer(iPostalcode));
    }

    public String formatTWO(int iPostalcode) {
        return df2.format(new Integer(iPostalcode));
    }

    public String formatZeroFilled(long number, int maxLength) {
        String max = "0000000000000";
        DecimalFormat dfSpecial = new DecimalFormat(max.substring(0, Math.min(max.length(), maxLength)));
        return dfSpecial.format(new Long(number));
    }

    public String getAgeGroupForCode(int i) {
        if (i >= 0 && i < arlAGM.size()) {
            return ((String) arlAGM.get(i)).substring(3);
        } else {
            return "";
        }
    }

    public String getPopulationGroupForCode(int i) {
        i = i / 100;
        if (i == 0) {
            return "WHITE";
        } else if (i == 1) {
            return "COLOURED";
        } else if (i == 5) {
            return "INDIAN";
        } else if (i == 9) {
            return "BLACK";
        } else {
            return "";
        }
    }

    public String getMaritalStatusForCode(int i) {
        if (i == 0) {
            return "UNKNOWN";
        } else if (i == 1) {
            return "SINGLE";
        } else if (i == 2) {
            return "MARRIED";
        } else if (i == 3) {
            return "DIVORCED";
        } else if (i == 4) {
            return "WIDOWED";
        } else {
            return "UNKNOWN";
        }
    }

    public String getMaritalStatusForCode(String code) {
        if (code.equals("U")) {
            return "UNKNOWN";
        } else if (code.equals("S")) {
            return "SINGLE";
        } else if (code.equals("M")) {
            return "MARRIED";
        } else if (code.equals("D")) {
            return "DIVORCED";
        } else if (code.equals("W")) {
            return "WIDOWED";
        } else {
            return "UNKNOWN";
        }
    }

    public int getAgeGroupForAge(int theAge) {
        int iAgeIndex = -1;
        if (theAge < 0) {
            return iAgeIndex;
        } else {
            for (int i = 0; i < arlAGM.size(); i++) {
                int iAge = Integer.parseInt(((String) arlAGM.get(i)).substring(0, 3));
                if (theAge <= iAge) {
                    return i;
                }
            }
        }
        return iAgeIndex;
    }

    /*
     * This needs to be rewritten, and take the actual birthday
     *
     */
    public int getAgeFromIDN(String idn) {

        int theAge = -1;
        try {
            if (idn.length() >= 6) {
                int iTodayDate = Integer.parseInt(COMMONdate.getInstance().getToday());
                int iYearToDay = Integer.parseInt((COMMONdate.getInstance().getTodayDashed()).substring(2, 4));
                int iYearIdn = Integer.parseInt(idn.substring(0, 2));
                String ageDate = "";
                if (iYearIdn <= iYearToDay) {
                    ageDate = "20" + idn.substring(0, 6);
                } else {
                    ageDate = "19" + idn.substring(0, 6);
                }
                int iAgeDate = Integer.parseInt(ageDate);
                theAge = (iTodayDate - iAgeDate) / 10000;
            }
        } catch (Exception e) {
            theAge = -1;
        }
        return theAge;
    }

    public int getAgeFromBirthDay(String birthday) {

        int theAge = -1;
        try {
            if (birthday.length() >= 10) {
                int iTodayDate = Integer.parseInt(COMMONdate.getInstance().getToday());
                String ageDate = birthday.substring(0, 4) + birthday.substring(5, 7) + birthday.substring(8, 10);
                int iAgeDate = Integer.parseInt(ageDate);
                theAge = (iTodayDate - iAgeDate) / 10000;
            }
        } catch (Exception e) {
            theAge = -1;
        }
        return theAge;
    }

    public String getBirthdDashedFromIDN(String idn) {
        String ageDate = "";
        if (idn.length() >= 6) {
            int iYearToDay = Integer.parseInt((COMMONdate.getInstance().getTodayDashed()).substring(2, 4));
            int iYearIdn = Integer.parseInt(idn.substring(0, 2));
            if (iYearIdn <= iYearToDay) {
                ageDate = "20" + idn.substring(0, 6);
            } else {
                ageDate = "19" + idn.substring(0, 6);
            }
            ageDate = ageDate.substring(0, 4) + "-" + ageDate.substring(4, 6) + "-" + ageDate.substring(6, 8);
        }
        return ageDate;
    }

    public String reformatDate(String date) {
        // check for datetime format as well
        try {
            String myDate = date.trim();
            String myTime = "";
            int iIndex = date.indexOf(' ');
            if (iIndex > -1) {
                myTime = date.substring(iIndex + 1);
                myDate = date.substring(0, iIndex);
            }
            StringTokenizer st = new StringTokenizer(myDate, "-/\\");
            if (st.countTokens() == 3) {
                date = st.nextToken() + "-" + df2.format(new Integer(st.nextToken())) + "-" + df2.format(new Integer(st.nextToken()));
                if (myTime.length() > 0) {
                    date = date + " " + myTime;
                }
            }
        } catch (Exception e) {
            date = "0000-00-00";
        }
        return date;
    }

    // make sure you don't just replace ',' with '\t' as the ',' may be used as text....
    public String makeTabDelimited(String rString) {
        String newString = "";
        if (rString.length() == 0) {
            return rString;
        }
        try {
            CsvListReader inFile = (new CsvListReader(new StringReader(rString), new CsvPreference('"', ',', "\n")));
            List<String> l;

            l = inFile.read();
            for (int i = 0; i < l.size(); i++) {
                if (i == 0) {
                    newString = (l.get(i));
                } else {
                    newString = newString + "\t" + l.get(i);
                }
            }
            rString = newString;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("**** UNEXPECTED ERROR PROCESSING: " + rString);
        }
        return rString;
    }

    public String formatNumber(int iCnt) {
        DecimalFormat df0 = new DecimalFormat("#,###,###,###");
        return df0.format(iCnt);
    }

    public String formatNumberPercentage(int i, int j, int sig) {
        DecimalFormat df0 = new DecimalFormat();
        df0.setMaximumFractionDigits(sig);
        df0.setRoundingMode(RoundingMode.HALF_UP);
        if (j == 0) {
            return "0%";
        }
        return df0.format(((double) i * 100.0 / (double) j)) + "%";
    }

    public String formatNumberPercentage(long i, long j, int sig) {
        DecimalFormat df0 = new DecimalFormat();
        df0.setMaximumFractionDigits(sig);
        df0.setRoundingMode(RoundingMode.HALF_UP);
        if (j == 0) {
            return "0%";
        }
        return df0.format(((double) i * 100 / (double) j)) + "%";
    }

    public String formatNumberPercentage(int i, int j, int sig, boolean bNoPercentage) {
        DecimalFormat df0 = new DecimalFormat();
        df0.setMaximumFractionDigits(sig);
        df0.setRoundingMode(RoundingMode.HALF_UP);
        if (j == 0) {
            return "0";
        }
        return df0.format(((double) i / (double) j) * 100.0);
    }

    public String formatNumberAverage(long i, long j, int sig) {
        DecimalFormat df0 = new DecimalFormat("####0.00");
        df0.setMaximumFractionDigits(sig);
        df0.setRoundingMode(RoundingMode.HALF_UP);
        if (j == 0) {
            return "0";
        }
        return df0.format(((double) i / (double) j));
    }

    public String formatNumberAverageFormat(double i, long j, int sig) {
        DecimalFormat df0 = new DecimalFormat("#,###,###,###");
        df0.setMaximumFractionDigits(sig);
        df0.setRoundingMode(RoundingMode.HALF_UP);
        if (j == 0) {
            return "0";
        }
        return df0.format(((double) i / (double) j));
    }

    public String formatNumberAverage(double i, long j, int sig) {
        DecimalFormat df0 = new DecimalFormat("####0.00");
        df0.setMaximumFractionDigits(sig);
        df0.setRoundingMode(RoundingMode.HALF_UP);
        if (j == 0) {
            return "0";
        }
        return df0.format(((double) i / (double) j));
    }

    public String removeAllSpace(String rString) {
        return rString.replaceAll("\\s+", "");
    }

    public int determineMeanDaysForComeBack(ArrayList arl) {
        int iMean = 0;
        ArrayList arlDiffs = new ArrayList();
        if (arl.size() > 0) {
            String left = (String) arl.get(0);
            for (int i = 1; i < arl.size(); i++) {
                int iDiff = COMMONdate.getInstance().getDifferenceInDays((String) arl.get(i), left);
                arlDiffs.add(new Integer(iDiff));
                left = (String) arl.get(i);
            }
            int iDiff = COMMONdate.getInstance().getDifferenceInDays(COMMONdate.getInstance().getTodayDashed(), left);
            arlDiffs.add(new Integer(iDiff));
            if (arlDiffs.size() == 1) {
                return (Integer) arlDiffs.get(0);
            } else {
                // sort array
                Integer[] sarl = new Integer[arlDiffs.size()];
                arlDiffs.toArray(sarl);
                Arrays.sort(sarl);
                for (int i = 0; i < sarl.length; i++) {
                    System.out.println(sarl[i]);
                }
                if (sarl.length % 2 == 0) {
                    // even
                    int iIndex = sarl.length / 2;
                    System.out.println("iMean has index: " + (iIndex - 1) + " and " + iIndex + ", means: " + (sarl[iIndex - 1] + sarl[iIndex]) / 2);
                    iMean = (sarl[iIndex - 1] + sarl[iIndex]) / 2;
                } else {
                    // uneven
                    int iIndex = sarl.length / 2;
                    System.out.println("iMean has index: " + iIndex + ", means: " + sarl[iIndex]);
                    iMean = sarl[iIndex];
                }
            }
        }
        return iMean;
    }

    public String replaceChunk(String rString, String removeThis, String replaceWith) {
        int iIndex = rString.indexOf(removeThis);
        while (iIndex > -1) {
            rString = rString.substring(0, iIndex) + replaceWith + rString.substring(iIndex + removeThis.length());
            iIndex = rString.indexOf(removeThis);
        }
        return rString;
    }

    public String replaceChunk(String rString, String removeThiswithThat) {
        String removeThis = "";
        String replaceWith = "";
        int iIndex = removeThiswithThat.indexOf(' ');
        if (iIndex > -1) {
            removeThis = removeThiswithThat.substring(0, iIndex);
            replaceWith = removeThiswithThat.substring(iIndex + 1);
        } else {
            return rString;
        }
        iIndex = rString.indexOf(removeThis);
        while (iIndex > -1) {
            rString = rString.substring(0, iIndex) + replaceWith + rString.substring(iIndex + removeThis.length());
            iIndex = rString.indexOf(removeThis);
        }
        return rString;
    }

    public long getCrC(String aToken) {

        CRC32 crc = new CRC32();
        crc.update(aToken.getBytes());
        //  System.out.println("*** TOKEN --> ["+aToken+"] = "+crc.getValue());
        return crc.getValue();
    }

    public int getIndexForMatch(String rString, String matchToken) {
        int iIndex = -1;
        Pattern pMatcher = Pattern.compile(matchToken);
        Matcher myMatcher = pMatcher.matcher(rString);
        if (myMatcher.find()) {
            iIndex = myMatcher.start();
        }
        return iIndex;
    }
    
     public ArrayList getIndexedForMatch(String rString, String matchToken) {
        ArrayList arl=new ArrayList();
        arl.add("");
        arl.add("");
        arl.add("");
        Pattern pMatcher = Pattern.compile(matchToken);
        Matcher myMatcher = pMatcher.matcher(rString);
        if (myMatcher.find()) {
            arl.set(0, rString.substring(0,myMatcher.start()));
            arl.set(1, rString.substring(myMatcher.start(),myMatcher.end()));
            arl.set(2, rString.substring(myMatcher.end()));
        }
        return arl;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        COMMONtoolkit obj = new COMMONtoolkit();
        System.out.println(COMMONtoolkit.getInstance().convertDate("Mar 9 2014 01:55:26:327PM"));
        System.out.println(COMMONtoolkit.getInstance().convertDate("9 Mar  2014 01:55:26:327PM"));
        System.out.println(COMMONtoolkit.getInstance().convertDate("09-03-2014"));
        System.out.println(COMMONtoolkit.getInstance().convertDate("05.06.1965"));
        System.out.println(COMMONtoolkit.getInstance().convertDate("01 november 1973"));
        System.out.println(COMMONtoolkit.getInstance().convertDate("1939/09/15  12:00:00 AM"));
        System.out.println(COMMONtoolkit.getInstance().convertDate("Aug 26 2014 01:26:48:360PM"));
        System.out.println(COMMONtoolkit.getInstance().convertDate("7-1-1982"));
        System.out.println(COMMONtoolkit.getInstance().convertDate("21/JAN/08"));
        System.out.println(COMMONtoolkit.getInstance().convertDate("21/01/08"));
        System.out.println(COMMONtoolkit.getInstance().convertDate("22Feb1992"));
        System.out.println(COMMONtoolkit.getInstance().convertDate("02JUL1990:00:00:02"));
        System.out.println(COMMONtoolkit.getInstance().convertDate("03FEB1952:00:00:00"));
        System.out.println(COMMONtoolkit.getInstance().convertDate("670702"));
        System.out.println(COMMONtoolkit.getInstance().convertDate("10/28/2013"));

        // RE: 2306-31
        // RE: 2306-312, 2306-352&C
        // RE: L4579 P315
        // 2942-759, 2942-729, 2942-767 &C GREEN RIDGE RE: 2942-708
        // 2734-1168 & 2734-1162
        // RE: 4669-358 & 5308-184
        // & C or &C
        // 3675-613 RICHARDS SANDRA K. & HUS PAUL F. II 3647-59
        System.out.println("**** key-value on [SEAT,GRAND RAPIDS] --> [" + COMMONtoolkit.myInstance.getCrC("SEAT") + ";" + COMMONtoolkit.myInstance.getCrC("GRAND RAPIDS") + "]");
        System.out.println("**** key-value on [SEAT,GRAND] --> [" + COMMONtoolkit.myInstance.getCrC("SEAT") + ";" + COMMONtoolkit.myInstance.getCrC("GRAND") + "]");
        System.out.println("**** key-value on [SEAT,RAPIDS] --> [" + COMMONtoolkit.myInstance.getCrC("SEAT") + ";" + COMMONtoolkit.myInstance.getCrC("RAPIDS") + "]");
        System.out.println("**** key-value on [SEAT,PRETORIA] --> [" + COMMONtoolkit.myInstance.getCrC("SEAT") + ";" + COMMONtoolkit.myInstance.getCrC("PRETORIA") + "]");
        System.out.println("**** key-value on [SEAT,JOHANNESBURG] --> [" + COMMONtoolkit.myInstance.getCrC("SEAT") + ";" + COMMONtoolkit.myInstance.getCrC("JOHANNESBURG") + "]");
        System.out.println("*** index of SANDRA K. in 3675-613 RICHARDS SANDRA K. & HUS PAUL F. II 3647-59: "+COMMONtoolkit.myInstance.getIndexForMatch("3675-613 RICHARDS SANDRA K. & HUS PAUL F. II 3647-59", "SANDRA[ ][A-Z]{1}\\."));
        System.out.println("*** index of SANDRA K. in 3675-613 RICHARDS SANDRA K. & HUS PAUL F. II 3647-59: "+COMMONtoolkit.myInstance.getIndexedForMatch("3675-613 RICHARDS SANDRA K. & HUS PAUL F. II 3647-59", "SANDRA[ ][A-Z]{1}\\."));
    }
}
