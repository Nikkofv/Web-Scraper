/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.vogel.common.tokenFactory;

import java.io.*;
import java.util.*;

import com.vogel.common.util.*;
import com.vogel.common.util.COMMONsys;
import com.vogel.common.util.COMMONtiming;
import com.vogel.common.util.COMMONtoolkit;
import static java.lang.Math.min;

/**
 *
 * @author vogel THE PURPOSE OF THIS MODULE IS TO COMPARE TWO
 * DIFFERENT WORDS AND FIGURE OUT HOW SIMILAR THEY ARE
 *
 */
public class tokenMatcher {

    private boolean bTrace = false;
    private final int MINTOKENLENGTH = 6;
    private int THRESHOLDRATING = 77;
    public int finalScore = 0;
    private int aShortenedWith = 0;

    private class MyToken {

        public String token = "";
        public int iCount = 0;
        public String matchToken = "";
        public int freq = 0;
        public int originalFreq = 0;
        String newToken = "";
        private boolean bHasDigits = false;
        public int iMatchPoints = 0;
        public String ending = "";

        public MyToken() {
            this.token = "";
            this.matchToken = "";
            this.freq = 0;
            this.newToken = "";
            this.originalFreq = 0;
            this.bHasDigits = false;
            this.iMatchPoints = 0;
        }

        public MyToken(MyToken myToken) {
            super();
            this.token = myToken.token;
            this.matchToken = myToken.matchToken;
            this.freq = myToken.freq;
            this.newToken = myToken.matchToken;
            this.iMatchPoints = myToken.iMatchPoints;
        }

        public void reset() {
            this.newToken = "";
            this.matchToken = indexToken(this.token);
            this.freq = this.originalFreq;
            this.iMatchPoints = 0;
        }

        public MyToken(String rString) {
            super();
            if (rString != null) {
                int iIndex = rString.indexOf("\t");
                if (iIndex > -1) {
                    this.token = rString.substring(0, iIndex);
                    this.matchToken = indexToken(this.token);
                    this.freq = Integer.parseInt(rString.substring(iIndex + 1));
                    this.originalFreq = freq;
                } else {
                    this.token = rString;
                    this.matchToken = indexToken(this.token);
                    this.freq = 1;
                    this.originalFreq = freq;
                }
                this.iMatchPoints = 0;
                this.newToken = "";
                determineDigits();
            } else {
                this.token = null;
                this.newToken = null;
            }
        }

        public String IndexToken(String theToken) {
            return indexToken(theToken);
        }

        private String indexToken(String theToken) {

            //  SortedSet output = Collections.synchronizedSortedSet(new TreeSet<String>());
            ArrayList arl = new ArrayList();
            int iIndex = theToken.indexOf('&');
            if (iIndex > -1) {
                theToken = theToken.substring(0, iIndex) + " AND " + theToken.substring(iIndex + 1);
            }
            // rewrite the 'S
            iIndex = theToken.indexOf("'S");
            if (iIndex > -1) {
                theToken = theToken.substring(0, iIndex) + "S" + theToken.substring(iIndex + 2);
            }

            // if ending is 'CES' remove the S
            iIndex = theToken.lastIndexOf("CES");
            if (iIndex > 0 && iIndex == (theToken.length() - 3)) {
                theToken = theToken.substring(0, theToken.length() - 1);
            }

            this.token = theToken;

            StringTokenizer st = new StringTokenizer(theToken, " .,\"`'?!;:#$%()*+-/<>=@[]\\^_{}|~");
            String word = "";
            boolean bConnector = false;
            boolean bFirst = true;
            while (st.hasMoreTokens()) {
                String aToken = st.nextToken();
                // ignore THE and DIE
                if (bFirst && (aToken.equals("THE") || aToken.equals("DE") || aToken.equals("DIE"))) {
                    bFirst = false;
                    this.token = this.token.substring(this.token.indexOf(aToken) + aToken.length()).trim();
                    continue;
                }
                if (aToken.length() == 1) {
                    word = word + aToken;
                } else {
                    if (!word.isEmpty()) {
                        arl.add(indexPhrases(word));
                        word = "";
                    }
                    if (aToken.equals("AND") || aToken.equals("EN")) {
                        aToken = "";
                        bConnector = true;
                    }

                    if (!aToken.isEmpty()) {
                        aToken = indexPhrases(aToken);
                        if (bConnector) {
                            bConnector = false;
                            if (!arl.isEmpty()) {
                                String bToken = (String) arl.get(arl.size() - 1);
                                if (aToken.compareTo(bToken) < 0) {
                                    arl.add(arl.size() - 1, aToken);
                                } else {
                                    arl.add(aToken);
                                }
                            } else {
                                arl.add("AND");
                                arl.add(aToken);
                            }
                        } else {
                            arl.add(aToken);
                        }
                    }
                }
            }
            if (!word.isEmpty()) {
                arl.add(indexPhrases(word));
            }
            String rString = "";
            this.iCount = arl.size();
            for (Object arl1 : arl) {
                rString = rString + ((String) arl1).trim();
            }
            //   System.out.println("****************** "+rString);
            //   rString = indexPhrases(rString);
            /*
             // there are certain endings that need to be respected. Such as bank, dia, sia, cia, der, dra, ta, tis, dria
             if (rString.length()>=("BANK").length() && rString.lastIndexOf("BANK")==(rString.length()-("BANK").length())) {
             rString=rString.substring(0,rString.lastIndexOf("BANK")).trim();
             }
             *
             */
            return rString;
        }

        private String WorkEndings(String rString) {
            System.out.println("*** IN: [" + rString + "]");
            if (rString.charAt(rString.length() - 1) == 'S') {
                rString = rString.substring(0, rString.length() - 1);
            }
            System.out.println("*** OUT: [" + rString + "]");
            return rString;
        }

        private void determineDigits() {
            this.bHasDigits = false;
            for (int i = 0; i < matchToken.length(); i++) {
                if (Character.isDigit(matchToken.charAt(i))) {
                    this.bHasDigits = true;
                    break;
                }
            }
        }

        public boolean hasDigits() {
            return this.bHasDigits;
        }
        // beware of order!!!!

        private String convertNumberToText(String aToken) {
            if (aToken.equals("1")) {
                aToken = "ONE";
            }
            if (aToken.equals("2")) {
                aToken = "TWO";
            }
            if (aToken.equals("3")) {
                aToken = "THREE";
            }
            if (aToken.equals("4")) {
                aToken = "FOUR";
            }
            if (aToken.equals("5")) {
                aToken = "FIVE";
            }
            if (aToken.equals("6")) {
                aToken = "SIX";
            }
            if (aToken.equals("7")) {
                aToken = "SEVEN";
            }
            if (aToken.equals("8")) {
                aToken = "EIGHT";
            }
            if (aToken.equals("9")) {
                aToken = "NINE";
            }
            if (aToken.equals("10")) {
                aToken = "TEN";
            }
            return aToken;
        }

        private String indexPhrases(String aToken) {
            //aToken=removeDupChar(aToken);
            if ("00000000000000000000000000".contains(aToken)) {
                return "";
            }

            String number = COMMONtoolkit.getInstance().getDigits(aToken);
            if (number.length() == 1 && aToken.indexOf(number) == 0) {
                aToken = (convertNumberToText(number) + aToken.substring(1)).trim();
            }

            aToken = convertNumberToText(aToken);

            // purifyToken pObj = new purifyToken();
            // aToken = pObj.removePunctuationsAndSpace(aToken);
            // FRANS vs FRANCE but how about 
            // SERVICE vx SERVICES 
            // remove common endings such as 'S, S
            if (aToken.length() > 5) {
                // SUPPLIES
                // but..... witkoppies vs witkoppie
                if (aToken.substring(aToken.length() - 4).equals("LIES")) {
                    aToken = aToken.substring(0, aToken.length() - 3) + "Y";
                }

                if (aToken.substring(aToken.length() - 4).equals("RIES")) {
                    aToken = aToken.substring(0, aToken.length() - 3) + "Y";
                }

            }
            if (aToken.length() > 2) {
                if (aToken.substring(aToken.length() - 2).equals("'S")) {
                    aToken = aToken.substring(0, aToken.length() - 2) + "S";
                }
                //  if (aToken.substring(aToken.length() - 1).equals("S")) {
                //      aToken = aToken.substring(0, aToken.length() - 1);
                //  }

                if (aToken.substring(aToken.length() - 2).equals("CE")) {
                    aToken = aToken.substring(0, aToken.length() - 2) + "S";
                }
            }
            if (bTrace) {
                System.out.println("*** aToken was: [" + aToken + "]");
            }
            if (aToken.length() > 5) {
                if (aToken.substring(aToken.length() - 3).equals("NAL")) {
                    aToken = aToken.substring(0, aToken.length() - 2);
                }

            }
            if (bTrace) {
                System.out.println("*** aToken is: [" + aToken + "]");
            }

            String bToken = aToken;

            aToken = replace(aToken, "É", "E");
            aToken = replace(aToken, "È", "E");
            aToken = replace(aToken, "Ë", "E");
            aToken = replace(aToken, "Ê", "E");
            aToken = replace(aToken, "Ç", "C");
            aToken = replace(aToken, "Ä", "A");
            aToken = replace(aToken, "Ã", "A");
            aToken = replace(aToken, "À", "A");
            aToken = replace(aToken, "Á", "A");
            aToken = replace(aToken, "Å", "A");
            aToken = replace(aToken, "Â", "A");
            aToken = replace(aToken, "Ô", "O");
            aToken = replace(aToken, "Ò", "O");
            aToken = replace(aToken, "Ó", "O");
            aToken = replace(aToken, "Ö", "O");
            aToken = replace(aToken, "Õ", "O");
            aToken = replace(aToken, "Ü", "U");
            aToken = replace(aToken, "Ù", "U");
            aToken = replace(aToken, "Ú", "U");
            aToken = replace(aToken, "Š", "S");
            aToken = replace(aToken, "Û", "U");
            aToken = replace(aToken, "Ï", "I");
            aToken = replace(aToken, "Ì", "I");
            aToken = replace(aToken, "Í", "I");
            aToken = replace(aToken, "Î", "I");
            aToken = replace(aToken, "Ñ", "N");

            aToken = replace(aToken, "ELL", "EL");
            /*
             aToken = replace(aToken, "ILL", "IL");
             aToken = replace(aToken, "OLL", "OL");
             aToken = replace(aToken, "ALL", "AL");
             aToken = replace(aToken, "ULL", "UL");
             */

            aToken = replace(aToken, "Z", "S");

            aToken = replace(aToken, "AAL", "AL");
            aToken = replace(aToken, "ACHT", "AGT");
            aToken = replace(aToken, "LEY", "LEI");
            aToken = replace(aToken, "LYT", "LIT");
            aToken = replace(aToken, "IE", "EI");
            aToken = replace(aToken, "HYN", "HEIN");
            aToken = replace(aToken, "TYN", "TEIN");
            aToken = replace(aToken, "TSHI", "CHI");
            aToken = replace(aToken, "TSHE", "TSE");

            //  aToken = replace(aToken, "TS", "CH");
            aToken = replace(aToken, "DLE", "DEL");
            aToken = replace(aToken, "KLE", "KEL");
            aToken = replace(aToken, "TRE", "TER");
            aToken = replace(aToken, "MAC", "MC");
            aToken = replace(aToken, "UI", "EU");

            aToken = replace(aToken, "PH", "P");
            aToken = replace(aToken, "TH", "T");
            aToken = replace(aToken, "LH", "L");
            aToken = replace(aToken, "KH", "K");
            aToken = replace(aToken, "JH", "J");
            //      aToken = replace(aToken, "SH", "J");

            aToken = replace(aToken, "LE", "EL");

            aToken = replace(aToken, "EAR", "ER");

            aToken = replace(aToken, "OE", "EO");

            aToken = replace(aToken, "C", "K");

            //    aToken = replace(aToken, "Y", "I");
            //   aToken = replace(aToken, "IJ", "EI");
            //   aToken = replace(aToken, "TJ", "TS");
            // aToken = replace(aToken, "JIE", "JE");
            aToken = replace(aToken, "VIKES", "VIS");

            if (bToken.length() > 5 && aToken.length() <= 5) {
                aToken = bToken;
            }

            return aToken;
        }

        private String removeDupChar(String rString) {
            String aToken = "";
            int iChar1 = 0;
            int iChar2 = 0;
            for (int i = 0; i < rString.length(); i++) {
                iChar2 = rString.charAt(i);
                if (iChar2 != iChar1) {
                    aToken = aToken + (char) iChar2;
                    iChar1 = iChar2;
                }
            }
            return aToken;
        }

        private String replace(String aToken, String arg1, String arg2) {
            int iIndex = aToken.indexOf(arg1);
            while (iIndex > -1) {
                aToken = aToken.substring(0, iIndex) + arg2 + aToken.substring(iIndex + arg1.length());
                iIndex = aToken.indexOf(arg1);
            }
            return aToken;
        }
    }

    private PrintWriter log = null;

    public tokenMatcher() {
    }

    tokenMatcher(boolean bTrace) {
        this.bTrace = bTrace;
    }

    public tokenMatcher(int iThreshold) {
        this.THRESHOLDRATING = iThreshold;
    }

    public void setThresholdRating(int iThreshold) {
        this.THRESHOLDRATING = iThreshold;
    }

    public boolean isSimilar(String aToken, String bToken) {

        if (aToken.isEmpty() && bToken.isEmpty()) {
            return false;
        }
        StringTokenizer st = new StringTokenizer(aToken, " ");
        if (st.countTokens() == 2) {
            StringTokenizer st2 = new StringTokenizer(bToken, " ");
            if (st2.countTokens() == 2) {
                String aaToken = st.nextToken();
                String bbToken = st2.nextToken();
                String aaaToken = st.nextToken();
                String bbbToken = st2.nextToken();
                if (aaToken.equals(bbToken) && bbbToken.length() > 3 && aaaToken.length() > 3) {
                    aToken = aaaToken;
                    bToken = bbbToken;
                } else {

                    if (aaaToken.equals(bbbToken) && aaToken.length() > 3 && bbToken.length() > 3) {
                        aToken = aaToken;
                        bToken = bbToken;
                    }
                }
            }
        }

        MyToken aTook = new MyToken(aToken);
        MyToken bTook = new MyToken(bToken);
        int matchPoints = isSimilar(aTook, bTook, new HashSet(), false);
        if (matchPoints > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int isSimilarOnPoints(String aToken, String bToken) {
        // int origMatchPoints = isSimilar(new MyToken(aToken), new MyToken(bToken), new HashSet(), true);
        if (aToken.isEmpty() && bToken.isEmpty()) {
            return 0;
        }
        //  System.out.println("*** orig points: " + origMatchPoints);
        StringTokenizer st = new StringTokenizer(aToken, " ");
        if (st.countTokens() == 2) {
            StringTokenizer st2 = new StringTokenizer(bToken, " ");
            if (st2.countTokens() == 2) {
                String aaToken = st.nextToken();
                String bbToken = st2.nextToken();
                String aaaToken = st.nextToken();
                String bbbToken = st2.nextToken();
                if (aaToken.equals(bbToken) && bbbToken.length() > 3 && aaaToken.length() > 3) {
                    aToken = aaaToken;
                    bToken = bbbToken;
                } else {

                    if (aaaToken.equals(bbbToken) && aaToken.length() > 3 && bbToken.length() > 3) {
                        aToken = aaToken;
                        bToken = bbToken;
                    }
                }
            }
        }
        MyToken aTook = new MyToken(aToken);
        MyToken bTook = new MyToken(bToken);
        int matchPoints = isSimilar(aTook, bTook, new HashSet(), true);
        return matchPoints;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void processMassCompare() {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            int iCnt = 0;
            int iMatched = 0;

            File fi = new File(COMMONsys.getInstance().getCOMMONpath() + "DATA/INPUT/source.txt");
            br = new BufferedReader(new FileReader(fi));
            ArrayList arlTokens = new ArrayList();
            MyToken aToken = new MyToken(br.readLine());
            while (aToken.token != null) {
                if (!aToken.hasDigits()) {
                    arlTokens.add(aToken);
                }
                aToken = new MyToken(br.readLine());
            }

            File fo = new File(COMMONsys.getInstance().getCOMMONpath() + "DATA/OUTPUT/tokenMatcherOutput.txt");
            bw = new BufferedWriter(new FileWriter(fo));
            bw.write("TOKEN\tFREQUENCY\tPROPOSED TOKEN\n");
            HashSet hsAncorToken = new HashSet();

            for (int i = 0; i < arlTokens.size(); i++) {
                aToken = (MyToken) arlTokens.get(i);
                if (bTrace) {
                    System.out.println("****************************************** processing: " + i + "(" + aToken.token + ")");
                }

                HashSet hsDependencies = new HashSet();
                for (int k = i; k < (arlTokens.size() - 1); k++) {
                    iCnt++;
                    MyToken bToken = (MyToken) arlTokens.get(k + 1);
                    if (aToken.token.length() < MINTOKENLENGTH || bToken.token.length() < MINTOKENLENGTH) {
                    } else {
                        if (bTrace) {
                            System.out.println("comparing " + aToken.token + " with " + bToken.token);
                        }
                        int matchPoints = isSimilar(aToken, bToken, hsDependencies, false);
                        if (matchPoints > 0) {
                            hsDependencies.add(new Integer(k + 1));
                            iMatched++;
                            // who will dominate?                            
                            if (aToken.freq > bToken.freq || ((aToken.freq == bToken.freq) && aToken.iMatchPoints > bToken.iMatchPoints)) {
                                if (bTrace) {
                                    System.out.print("****************************"
                                            + "************** " + aToken.matchToken + "(" + aToken.iMatchPoints + ")" + " matches > " + bToken.matchToken + "(" + bToken.iMatchPoints + ")");
                                }
                                // aToken dominates, so bToken inherits from aToken so it can pass on the message
                                bToken.newToken = (aToken.newToken.length() > 0) ? aToken.newToken : aToken.token;
                                bToken.freq = aToken.freq;
                                bToken.matchToken = aToken.matchToken;
                                // complements to the chef
                                if (aToken.newToken.length() == 0) {
                                    aToken.newToken = aToken.token;
                                }
                                if (bToken.iMatchPoints < matchPoints) {
                                    bToken.iMatchPoints = matchPoints;
                                }
                                if (aToken.iMatchPoints < matchPoints) {
                                    aToken.iMatchPoints = matchPoints;
                                }
                                if (bTrace) {
                                    System.out.println(": new token for bToken = " + bToken.newToken + "(" + bToken.iMatchPoints + ")");
                                }
                            } else {
                                if (bTrace) {
                                    System.out.print("****************************"
                                            + "************** " + aToken.matchToken + "(" + aToken.iMatchPoints + ")" + " matches < " + bToken.matchToken + "(" + bToken.iMatchPoints + ")");
                                }
                                // bToken dominates, so aToken carries on with stronger information
                                aToken.newToken = (bToken.newToken.length() > 0) ? bToken.newToken : bToken.token;
                                aToken.freq = bToken.freq;
                                aToken.matchToken = bToken.matchToken;
                                // complements to the chef
                                if (bToken.newToken.length() == 0) {
                                    bToken.newToken = bToken.token;
                                }
                                if (bToken.iMatchPoints < matchPoints) {
                                    bToken.iMatchPoints = matchPoints;
                                }
                                if (aToken.iMatchPoints < matchPoints) {
                                    aToken.iMatchPoints = matchPoints;
                                }
                                if (bTrace) {
                                    System.out.println(": new token for aToken = " + aToken.newToken + "(" + aToken.iMatchPoints + ")");
                                }
                            }
                        } else {
                            if (bTrace) {
                                System.out.println("****************************************** no match.....");
                            }
                        }
                    }
                    if (iCnt % 1000 == 0) {
                        System.out.println("**************** TOKENMATCHER ***");
                        System.out.println("***** processed: " + iCnt);
                        System.out.println("******* matched: " + iMatched);
                    }
                }

                // prevent an ancor token to be changed
                if (hsAncorToken.contains(aToken.IndexToken(aToken.token))) {
                    aToken.matchToken = aToken.IndexToken(aToken.token);
                    aToken.freq = aToken.originalFreq;
                    aToken.newToken = aToken.token;
                    aToken.iMatchPoints = 0;
                    hsDependencies.clear();
                }

                if (!hsDependencies.isEmpty()) {
                    hsAncorToken.add(aToken.matchToken);
                }
                if (bTrace) {
                    System.out.println("ancorToken added: " + aToken.matchToken);
                }

                // we need to update all dependencies on this UoW
                Iterator ite = hsDependencies.iterator();
                while (ite.hasNext()) {
                    Integer iINT = (Integer) ite.next();
                    MyToken bToken = (MyToken) arlTokens.get(iINT.intValue());
                    bToken.newToken = (aToken.newToken.length() > 0) ? aToken.newToken : aToken.token;
                    bToken.freq = aToken.freq;
                    bToken.matchToken = aToken.matchToken;
                    //  if (bToken.iMatchPoints < aToken.iMatchPoints) {
                    bToken.iMatchPoints = aToken.iMatchPoints;
                    // }
                }
            }
            // write out the UoW
            for (int i = 0; i < arlTokens.size(); i++) {
                aToken = (MyToken) arlTokens.get(i);
                bw.write(aToken.token + "\t" + aToken.originalFreq + "\t" + aToken.newToken + "\n");
                if (bTrace) {
                    System.out.println(aToken.token + "\t" + aToken.originalFreq + "\t" + aToken.newToken);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bw != null) {
                try {
                    bw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void processTokenFile() {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            int iCnt = 0;
            int iMatched = 0;

            File fi = new File(COMMONsys.getInstance().getCOMMONpath() + "DATA/INPUT/source.txt");
            br = new BufferedReader(new FileReader(fi));
            File fo = new File(COMMONsys.getInstance().getCOMMONpath() + "DATA/OUTPUT/tokenMatcherOutput.txt");
            bw = new BufferedWriter(new FileWriter(fo));
            bw.write("TOKEN\tFREQUENCY\tPROPOSED TOKEN\n");
            ArrayList arlDelayed = new ArrayList();
            MyToken bToken = new MyToken(br.readLine());
            MyToken aToken = new MyToken(br.readLine());
            String maxFreqToken = "";
            int iMaxFreq = 0;
            boolean bSimilar = false;
            while (aToken.token != null && bToken.token != null) {
                iCnt++;
                if (aToken.token.length() < MINTOKENLENGTH || bToken.token.length() < MINTOKENLENGTH) {
                    // write out bToken
                    arlDelayed.add(bToken);
                    // write out the arlDelayed
                    for (int i = 0; i < arlDelayed.size(); i++) {
                        MyToken token = (MyToken) arlDelayed.get(i);
                        bw.write(token.token + "\t" + token.freq + "\t" + ((bSimilar) ? maxFreqToken : "") + "\n");
                    }
                    arlDelayed.clear();
                    maxFreqToken = "";
                    iMaxFreq = 0;
                    bSimilar = false;
                } else {
                    arlDelayed.add(bToken);
                    // lets determine similarity
                    int iMatchPoints = isSimilar(aToken, bToken, new HashSet(), false);
                    if (iMatchPoints > 0) {
                        bSimilar = true;
                        iMatched++;
                        if (aToken.freq > bToken.freq) {
                            if (aToken.freq > iMaxFreq) {
                                maxFreqToken = aToken.token;
                                iMaxFreq = aToken.freq;
                            }
                        } else {
                            if (bToken.freq > iMaxFreq) {
                                maxFreqToken = bToken.token;
                                iMaxFreq = bToken.freq;
                            }
                        }
                    } else {
                        // write out the arlDelayed
                        for (int i = 0; i < arlDelayed.size(); i++) {
                            MyToken token = (MyToken) arlDelayed.get(i);
                            bw.write(token.token + "\t" + token.freq + "\t" + ((bSimilar) ? maxFreqToken : "") + "\n");
                        }
                        arlDelayed.clear();
                        maxFreqToken = "";
                        iMaxFreq = 0;
                        bSimilar = false;
                    }
                }

                bToken = new MyToken(aToken);

                if (iCnt % 1000 == 0) {
                    System.out.println("**************** TOKENMATCHER ***");
                    System.out.println("***** processed: " + iCnt);
                    System.out.println("******** tokens: " + iMatched);
                }
                aToken = new MyToken(br.readLine());
            }
            // write out the last bToken
            arlDelayed.add(bToken);
            for (int i = 0; i < arlDelayed.size(); i++) {
                MyToken token = (MyToken) arlDelayed.get(i);
                bw.write(token.token + "\t" + token.freq + "\t" + ((bSimilar) ? maxFreqToken : "") + "\n");
            }
            System.out.println("**************** TOTALS ***");
            System.out.println("***** processed: " + iCnt);
            System.out.println("******** matched: " + iMatched);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bw != null) {
                try {
                    bw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private double getMultiplier(int iclose, int ilength) {
        if (iclose == 0 && ilength >= 4) {
            return 1.10;
        } else if (iclose == 0) {
            return 1.0;
        } else if (iclose == 1) {
            return 1.07;
        } else if (iclose == 2 && ilength > 12) {
            return 1.05;
        }
        return 1.0;
    }

    private int similarityRating(String aToken, String bToken, int iMatchpoints) {
        int iSR = 0;
        if (iMatchpoints > 0) {
            iMatchpoints += aShortenedWith;
        }
        double aLength = aToken.length();
        double bLength = bToken.length();

        /*
         There is something unfair about 9 matches on a length of 10 and 11 ==> 86
         VENTERSPOST vs VENTERPOST
         so we must reward closeness
         */
        //  double aLength = aToken.length() - aShortenedWith;
        double ap = ((double) iMatchpoints) * getMultiplier(aToken.length() - iMatchpoints, aToken.length()) / aLength;
        double bp = ((double) iMatchpoints) * getMultiplier(bToken.length() - iMatchpoints, bToken.length()) / bLength;
        ap = ap * 1000.0 + 5.0;
        bp = bp * 1000.0 + 5.0;
        ap = ((int) ap) / 10;
        bp = ((int) bp) / 10;
        // do we really need to average when both are super high as is?
        double ap2 = 0.0;
        if (Math.max(ap, bp) > 95 && ap > 82 && bp > 82) {
            ap2 = Math.max(ap, bp);
        } else {
            // do we really need to average is the difference in length is just one?
            if (Math.abs(aLength - bLength) > 1) {
                ap2 = ((ap + bp) / 2.0) + 0.5;
            } else {
                ap2 = Math.max(ap, bp);
            }
        }
        if (bTrace) {
            System.out.println("*** matchpoints: " + iMatchpoints);
            System.out.println("*** adjusted length aWord: " + aLength);
            System.out.println("*** adjusted length bWord: " + bLength);
            System.out.println("ap: " + ap + " and bp: " + bp + " averages: " + ap2);
        }

        return (int) ap2;
    }

    /* Similar Test scheduler
     * 
     */
    private int isSimilar(MyToken aToken, MyToken bToken, HashSet hsDependencies, boolean bFullScore) {

        // similarity test on abridging tokens
        if (removeSpace(aToken.token).equals(removeSpace(bToken.token))) {
            finalScore = 100;
            return finalScore;
        }

        int iMatchPoint1 = isSimilar1(aToken.matchToken, bToken.matchToken);
        int iMatchPoint2 = 0;

        if (bTrace) {
            System.out.println("****************** matching " + aToken.matchToken + " with " + bToken.matchToken + " yields " + iMatchPoint1 + " matchpoints.");
        }

        // we also do a similarity on non bridging, to see if the escalation/greediness can be broken.
        String aMatchToken = aToken.IndexToken(aToken.token);
        String bMatchToken = bToken.IndexToken(bToken.token);

        if (!aMatchToken.equals(aToken.matchToken) || !bMatchToken.equals(bToken.matchToken)) {
            iMatchPoint2 = isSimilar1(aMatchToken, bMatchToken);
            if (bTrace) {
                System.out.println("****************** original matching " + aMatchToken + " with " + bMatchToken + " yields " + iMatchPoint2 + " matchpoints.");
            }
        }
        /*
         Question is do we measure the MatchPoints with the indexed versions or the given versions?
         */

        int iSR1 = similarityRating(aToken.matchToken, bToken.matchToken, iMatchPoint1);
        if (bTrace) {
            System.out.println("*** aToken: " + aToken.token + " <--> " + aToken.matchToken + " <--> " + aToken.newToken);
        }
        if (Math.abs(removeSpace(aToken.token).length() - removeSpace(bToken.token).length()) < 3) {
            int iSR3 = similarityRating(removeSpace(aToken.token), removeSpace(bToken.token), iMatchPoint1);
            if (bTrace) {
                System.out.println("*** iSR1: " + iSR1);
                System.out.println("*** iSR3: " + iSR3);
            }
            iSR1 = (int) ((((double) iSR1 + (double) iSR3) * 1000.0 + 5.0) / 2.0 / 10.0) / 100;
            if (bTrace) {
                System.out.println("*** iSR1 averaged: " + iSR1);
            }
        }

        int iSR2 = similarityRating(aMatchToken, bMatchToken, iMatchPoint2);

        if (bTrace) {
            System.out.println("SR1=" + iSR1 + " AND SR2=" + iSR2);
        }
        // require more than 77sr.
        if (iSR2 >= iSR1 && iSR2 > THRESHOLDRATING && hsDependencies.isEmpty()) {
            // it seems that the originals are closer than the abridged token chain.
            // repair actions are needed on both, like a reset.
            aToken.reset();
            bToken.reset();
            iSR1 = iSR2;
        }
        if (iSR2 > 0) {
            iSR1 = (iSR1 + iSR2) / 2;
        }

        iSR1 = Math.min(100, iSR1);

        // the finalScore cannot be 100 if the tokens are not exactly the same.....
        if (iSR1 == 100) {
            purifyToken pObj = new purifyToken();
            if (!pObj.removePunctuationsAndSpace(aToken.token).equals(pObj.removePunctuationsAndSpace(bToken.token))) {
                iSR1 = 99;
            }
        }
        /*
         final
         */
        finalScore = iSR1;
        if (bFullScore) {
            return iSR1;
        } else {
            if (iSR1 > THRESHOLDRATING) {
                return iSR1;
            } else {
                return 0;
            }
        }
    }

    private String removeSpace(String rString) {

        return rString.replaceAll(" +", "");
    }

    private int isSimilar1(String aToken, String bToken) {

        // delta length cannot be of more than 1 count for short words
        // delta length can be 2 for long words >2
        int iCnt = 0;
        int iCnt2 = 0;

        aShortenedWith = 0;
        // we  need to chop off common endings
        if (aToken.length() > 5 && bToken.length() > 5) {
            if (aToken.substring(aToken.length() - 3).equals("NAL") && bToken.substring(bToken.length() - 3).equals("NAL")) {
                aToken = aToken.substring(0, aToken.length() - 2);
                bToken = bToken.substring(0, bToken.length() - 2);
                aShortenedWith = 2;
            }
            if (aToken.substring(aToken.length() - 3).equals("NIA") && bToken.substring(bToken.length() - 3).equals("NIA")) {
                aToken = aToken.substring(0, aToken.length() - 2);
                bToken = bToken.substring(0, bToken.length() - 2);
                aShortenedWith = 2;
            }
        }

        // this does not work with digits....
        String digitsa = COMMONtoolkit.getInstance().getDigits(aToken);
        int iDiffs = aToken.length() - digitsa.length();
        int thisDiffs = Math.abs(aToken.length() - bToken.length());
        if (iDiffs > 2 && aToken.length() > 9 && bToken.length() > 9 && !aToken.equals(bToken)) {
            //int iLength = Math.max(aToken.length(), bToken.length());
            if (aToken.substring(0, 5).equals(bToken.substring(0, 5)) && thisDiffs < 5) {
                aToken = aToken.substring(3);
                bToken = bToken.substring(3);
                aShortenedWith = 3;

            } else if (aToken.substring(aToken.length() - 5).equals(bToken.substring(bToken.length() - 5))) {
                aToken = aToken.substring(0, aToken.length() - 4);
                bToken = bToken.substring(0, bToken.length() - 4);
                aShortenedWith = 4;
            }
        }
        if (bTrace) {
            System.out.println("******* SHORTENED WITH " + aShortenedWith);
        }

        int iLengthA = aToken.length();
        int iLengthB = bToken.length();

        boolean bLongWords = (iLengthA > 12 && iLengthB > 12) || (iLengthA > 9 && iLengthB > 9 && (iLengthA + iLengthB) > 22);
        boolean bMediumWords = (((iLengthA >= 8 && iLengthB > 8) || (iLengthA > 8 && iLengthB >= 8)) && (iLengthA + iLengthB) >= 18);

        int iDiff = Math.abs(iLengthA - iLengthB);
        if (bTrace) {
            System.out.println("*** the difference in length: [" + aToken + "]" + iLengthA + "- [" + bToken + "]" + iLengthB + "=" + iDiff);

            /*
             * introduce levenschtein distance to this to see if it helps.
             */
        }
        int iDistance = 0;
        if (iDiff < 2 || (iDiff < 3 && (bLongWords || bMediumWords || ((aToken.contains(bToken) || bToken.contains(aToken)) || (aToken.length() >= 6 && bToken.length() >= 6))))
                || (iDiff < 4 && bLongWords && beginMatch(aToken, bToken, 4) && endMatch(aToken, bToken, 3))
                || (iDiff <= 8 && bLongWords && ((beginMatch(aToken, bToken, 8) && endMatch(aToken, bToken, 7))) || beginMatch(aToken, bToken, 14))
                || (iDiff < 5 && bMediumWords && beginMatch(aToken, bToken, 4) && endMatch(aToken, bToken, 4))
                || (iDiff < 5 && bMediumWords && beginMatch(aToken, bToken, 3) && endMatch(aToken, bToken, 5))
                || (iDiff < 5 && bMediumWords && endMatch(aToken, bToken, 8))
                || ((aToken.contains(bToken) || bToken.contains(aToken)) && (iDiff < 3 || aToken.indexOf(bToken) == 0 || bToken.indexOf(aToken) == 0))
                || (iDiff < 3 && !bMediumWords && !bLongWords && beginMatch(aToken, bToken, 3) && endMatch(aToken, bToken, 4))
                || (iDiff < 3 && !bMediumWords && !bLongWords && beginMatch(aToken, bToken, 4) && endMatch(aToken, bToken, 3))
                || (aToken.length() > 5 && bToken.length() > 5 && (aToken.indexOf(bToken) == 0 || bToken.indexOf(aToken) == 0))
                || (aToken.length() > 5 && bToken.length() > 5
                && (aToken.lastIndexOf(bToken) == (aToken.length() - bToken.length()) || bToken.lastIndexOf(aToken) == (bToken.length() - aToken.length())))) {

            String theLonger = "";
            String theShorter = "";
            if (aToken.length() > bToken.length()) {
                theLonger = aToken;
                theShorter = bToken;
            } else {
                theShorter = aToken;
                theLonger = bToken;
            }
            boolean bEqualLength = theLonger.length() == theShorter.length();

            iDistance = levenshteinDistance(theLonger, theShorter);
            if (bTrace) {
                System.out.println("*** THE LVENSHTEIN DISTANCE BETWEEN [" + theLonger + "] AND [" + theShorter + "] IS " + iDistance);
            }

            // measure from the left and right, allow to skip one
            iCnt = measureFromLeft2Right(theShorter, theLonger, (bMediumWords || bLongWords || bEqualLength));
            if (bEqualLength) {
                int iCnt11 = measureFromLeft2Right(theLonger, theShorter, (bMediumWords || bLongWords || bEqualLength));
                iCnt = Math.max(iCnt, iCnt11);
            } else {
                if (bTrace) {
                    System.out.println("*** not equal length");
                }
                // 1 is not a match.....
                if (iCnt <= 1) {
                    iCnt = 0;
                }
            }

            // if both tokens are equal length, there could be a bias: KIDSBROOKE KIDDSBROOK, 
            // so we must test in both directions and take the max score
            if (bTrace) {
                System.out.println("*** !" + theShorter + "!");
                System.out.println("*** !" + theLonger + "!");
                System.out.println("TEST ONE: COUNTING LEFT2RIGHT AND BACK: " + iCnt);
            }

            // we got to make them both same length.
            // ..UITZIT
            // UITZICHT this does not work well for short words
            // BROEK RIVIERS", "BREERIVIER")); are different but similar endings make is very similar.....
            // chopped words should nullify the shorter length issue
            if ((theShorter.length() > 4 && iDiff < 3) || bLongWords) {
                for (int i = 0; i < iDiff; i++) {
                    theShorter = " " + theShorter;
                }

                boolean bFirst = true;
                int iKeep = 0;
                int iMissed = 0;
                for (int i = theShorter.length() - 1; i >= 0; i--) {
                    if (theShorter.charAt(i) == theLonger.charAt(i)) {
                        if (bTrace) {
                            System.out.println("matched " + theShorter.charAt(i) + "-" + theLonger.charAt(i));
                        }
                        if (!bFirst) {
                            iKeep++;
                        } else {

                            bFirst = false;
                        }
                    } else {
                        if (bLongWords) {
                            iMissed++;
                            if (iMissed > 1) {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
                if (iKeep > 0) {
                    iCnt += (iKeep + 1);
                }

            }

            if (bTrace) {
                System.out.println("TEST TWO: COUNTING RIGHT2LEFT AND BACK: " + iCnt);
            }
            if (iCnt >= theLonger.length()) {
                // iCnt = theLonger.length();
                if (iCnt / 2 >= theLonger.length()) {
                    iCnt = theLonger.length();
                } else {
                    iCnt = theLonger.length() - 1;
                }
            }

            // back to normal
            theShorter = theShorter.trim();
            if (bTrace) {
                System.out.println("TEST THREE: COUNTING RIGHT2LEFT AND BACK: " + iCnt);
                System.out.println("*** the shorter: " + theShorter.trim());
                System.out.println("***  the longer: " + theLonger.trim());
            }
            // alternatively we can match one on one and count that.
            // example: "WELGEHUIWEN", "WILGEHEUWEL"
            int iCnt3 = seeff(theShorter, theLonger);
            if (iCnt3 > iCnt) {
                iCnt = iCnt3;
                //    System.out.println("*** "+iCnt3);
                // iCnt+=1;
            }

            if (bTrace) {
                System.out.println("TEST FOUR: COUNTING RIGHT2LEFT AND BACK: " + iCnt);
            }

            // alternatively we can measure differently, by allowing one letter to be dropped from theLonger one, which must be longer.
            // we allow two letters dropped from long words
            // and determine which measure yield most match points.
            // theShorter.length()>=(MINTOKENLENGTH+1
            if (iDiff > 0) {

                if (bTrace) {
                    System.out.println("lets drop a letter test on " + theShorter + " and " + theLonger + "(bLongWords: " + bLongWords + ")");
                }
                int iSkip = 0;
                int iThreshold = (bLongWords) ? 2 : 1;
                for (int i = 0; i < theShorter.length(); i++) {
                    if (bTrace) {
                        System.out.println("matching " + theShorter.charAt(i) + " and " + theLonger.charAt(i + iSkip));
                    }
                    if ((i + iSkip) < theLonger.length()) {
                        if (theShorter.charAt(i) == theLonger.charAt(i + iSkip)) {
                            iCnt2++;
                        } else {
                            if (bTrace) {
                                System.out.println("we are here");
                            }
                            iSkip++;
                            if ((iSkip <= iThreshold && (i + iSkip) < theLonger.length()) && theShorter.charAt(i) == theLonger.charAt(i + iSkip)) {
                                iCnt2++;
                                if (bTrace) {
                                    System.out.println("dropping the letter: " + theLonger.charAt(i));
                                }
                            } else {
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }
                if (bTrace) {
                    System.out.println("iCnt2: " + iCnt2);
                }
                if (bTrace) {
                    System.out.println("iCnt: " + iCnt);
                }
                iCnt = Math.max(iCnt, iCnt2);
            }

            // typically a case of swapped letters.
            if (iDiff == 0 && iCnt == (iLengthA - 2)) {
                // we need to demonstrate swapped letters.
                // the character sorted versions are equal
                String theShorterSorted = charSort(theShorter);
                String theLongerSorted = charSort(theLonger);
                if (theShorterSorted.equals(theLongerSorted)) {
                    iCnt = theShorter.length();
                }
            }

        } else {
            if (bTrace) {
                System.out.println("**** DID NOT EVALUATE: UNEQUAL LENGTH");
            }
        }
        if (bTrace) {
            System.out.println("*** TOTAL COUNT RETURNED: " + iCnt);
        }
        return iCnt;
    }

    private int measureFromLeft2Right(String theShorter, String theLonger, boolean bCanSkip) {

        // System.out.println("*** 1 ("+theShorter+")");
        // System.out.println("*** 2 ("+theLonger+")");
        int iCnt = 0;
        boolean bSkipped = false;
        boolean bShifted = false;
        int iOffset = 0;
        if (bTrace) {
            System.out.println("*** the shorter: " + theShorter);
            System.out.println("*** theLonger: " + theLonger);
        }
        for (int i = 0; i < theShorter.length(); i++) {
            if ((i + iOffset) >= theLonger.length()) {
                break;
            }
            if (bTrace) {
                System.out.println("matching: " + theShorter.charAt(i) + "-" + theLonger.charAt(i + iOffset));
            }
            if (theShorter.charAt(i) == theLonger.charAt(i + iOffset)) {
                iCnt++;
            } else {
                // a misfit, but bear in mind the shorter is shorter... and we could have a misspelling.
                if (theShorter.length() > (i + 1) && !bSkipped && bCanSkip) {
                    if (theShorter.charAt(i + 1) == theLonger.charAt(i + 1)) {
                        bSkipped = true;
                        continue;
                    }
                }
                // check if we can play the offset card.

                if (!bShifted) {

                    iOffset = 1;
                    if ((i + iOffset) >= theShorter.length()) {
                        break;
                    }
                    if (theShorter.charAt(i) == theLonger.charAt(i + iOffset)) {
                        iCnt++;
                        bShifted = true;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }

        int iDistance = levenshteinDistance(theLonger, theShorter);

        if (bTrace) {
            System.out.println("**** shifted: " + bShifted);
            System.out.println("**** skipped: " + bSkipped);
            System.out.println("*** distance: " + iDistance);
        }

        if (THRESHOLDRATING < 77 && !bSkipped && !bShifted && iCnt < theShorter.length() && theLonger.length()>6) {
            // check from right to left until misfit
            int iShort = theShorter.length();
            int iLonger = theLonger.length();
            while (iShort > 0) {
                if (bTrace) {
                    System.out.println("***** backwards matching: " + theShorter.charAt(iShort - 1) + "-" + theLonger.charAt(iLonger - 1));
                }
                if (theShorter.charAt(iShort - 1) == theLonger.charAt(iLonger - 1)) {
                    iCnt++;
                } else {
                    break;
                }
                iShort--;
            }
        }

        if (bSkipped && bShifted) {
            iCnt = iCnt - iDistance;
        }

        return iCnt;
    }

    private String charSort(String aToken) {
        boolean bDone = false;
        char[] ca = aToken.toCharArray();
        while (!bDone) {
            bDone = true;
            for (int i = 0; i < ca.length; i++) {
                int iNext = i + 1;
                if (iNext < ca.length) {
                    if (ca[i] > ca[iNext]) {
                        // swap places
                        char c = ca[i];
                        ca[i] = ca[iNext];
                        ca[iNext] = c;
                        bDone = false;
                    }
                }
            }
        }

        return String.valueOf(ca);
    }

    private boolean beginMatch(String s1, String s2, int count) {
        boolean bMatch = false;
        if (s1.length() >= count && s2.length() >= count) {
            if (s1.substring(0, count).equals(s2.substring(0, count))) {
                bMatch = true;
            }
        }
        return bMatch;
    }

    private boolean endMatch(String s1, String s2, int count) {
        boolean bMatch = false;
        if (s1.length() >= count && s2.length() >= count) {
            if (s1.substring(s1.length() - count).equals(s2.substring(s2.length() - count))) {
                bMatch = true;
            }
        }
        return bMatch;
    }

    private int seeff(String s1, String s2) {
        // the matching characters must be evenly distributed.....
        int iCnt = 0;
        int iCntTwoGap = 0;
        int iCntThreeGap = 0;
        int iCntGap = 0;
        for (int i = 0; i < Math.min(s1.length(), s2.length()); i++) {
            if (s1.charAt(i) == s2.charAt(i)) {
                iCnt++;
                if (iCntGap == 2) {
                    iCntTwoGap++;
                } else if (iCntGap > 2) {
                    iCntThreeGap++;
                }
                iCntGap = 0;
            } else {
                iCntGap++;
            }
        }

        if (iCntGap == 2) {
            iCntTwoGap++;
        } else if (iCntGap > 2) {
            iCntThreeGap++;
        }

        iCnt = iCnt - iCntTwoGap - iCntThreeGap;

        return iCnt;
    }

    public int levenshteinDistance(String s, String t) {
        // degenerate cases
        if (s == t) {
            return 0;
        }
        if (s.length() == 0) {
            return t.length();
        }
        if (t.length() == 0) {
            return s.length();
        }

        // create two work vectors of integer distances
        int[] v0 = new int[t.length() + 1];
        int[] v1 = new int[t.length() + 1];

        // initialize v0 (the previous row of distances)
        // this row is A[0][i]: edit distance for an empty s
        // the distance is just the number of characters to delete from t
        for (int i = 0; i < v0.length; i++) {
            v0[i] = i;
        }
        for (int i = 0; i < s.length(); i++) {
            // calculate v1 (current row distances) from the previous row v0

            // first element of v1 is A[i+1][0]
            //   edit distance is delete (i+1) chars from s to match empty t
            v1[0] = i + 1;
            // use formula to fill in the rest of the row
            for (int j = 0; j < t.length(); j++) {
                int cost = 1;
                if (s.charAt(i) == t.charAt(j)) {
                    cost = 0;
                }
                v1[j + 1] = (int) min(min(v1[j] + 1, v0[j + 1] + 1), v0[j] + cost);
            }

            // copy v1 (current row) to v0 (previous row) for next iteration
            for (int j = 0; j < v0.length; j++) {
                v0[j] = v1[j];
            }
        }

        return v1[t.length()];
    }

    /*
     * A perfect swap should not be counted a distance of two
     */
    public int levenshteinDistanceWithSwapDiscount(String s, String t) {
        // degenerate cases
        if (s.equals(t)) {
            return 0;
        }
        if (s.length() == 0) {
            return t.length();
        }
        if (t.length() == 0) {
            return s.length();
        }

        // create two work vectors of integer distances
        int[] v0 = new int[t.length() + 1];
        int[] v1 = new int[t.length() + 1];

        // initialize v0 (the previous row of distances)
        // this row is A[0][i]: edit distance for an empty s
        // the distance is just the number of characters to delete from t
        for (int i = 0; i < v0.length; i++) {
            v0[i] = i;
        }
        for (int i = 0; i < s.length(); i++) {
            // calculate v1 (current row distances) from the previous row v0

            // first element of v1 is A[i+1][0]
            //   edit distance is delete (i+1) chars from s to match empty t
            v1[0] = i + 1;
            // use formula to fill in the rest of the row
            for (int j = 0; j < t.length(); j++) {
                int cost = 1;
                if (s.charAt(i) == t.charAt(j)) {
                    cost = 0;
                }
                v1[j + 1] = (int) min(min(v1[j] + 1, v0[j + 1] + 1), v0[j] + cost);
            }

            // copy v1 (current row) to v0 (previous row) for next iteration
            for (int j = 0; j < v0.length; j++) {
                v0[j] = v1[j];
            }
        }

        return v1[t.length()] - bSwapped(s, t);
    }

    private int bSwapped(String a, String b) {
         // 0813848998
        // 0183848989
        int iCntSwaps = 0;
        if (a.length()!=b.length()) return 0;
        for (int i = 0; i < (a.length() - 1); i++) {
            // leave room for a possible swap
            if (i < (b.length() - 1)) {
                if (a.charAt(i) != b.charAt(i)) {
                    // check if this is the first for two in a swap.
                    if (a.charAt(i + 1) == b.charAt(i) && b.charAt(i + 1) == a.charAt(i)) {

                       
                        iCntSwaps++;
                        i++;
                    }
                }
            }
        }
        return iCntSwaps;
    }

    public static void main(String args[]) {

        COMMONtiming objTimer = new COMMONtiming();
        boolean bTrace = false;
        tokenMatcher obj = new tokenMatcher(bTrace);
        objTimer.start();

        if (!bTrace) {
            System.out.println("SIMILAR TEST 001: " + ((obj.isSimilarOnPoints("CAMPBROOK", "KIDSBROOKE") == 56) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("CAMPBROOK", "KIDSBROOKE")));
            System.out.println("SIMILAR TEST 002: " + ((obj.isSimilarOnPoints("KIDSBROOKE", "KIDDSBROOK") == 96) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("KIDSBROOKE", "KIDDSBROOK")));
            System.out.println("SIMILAR TEST 003: " + ((obj.isSimilarOnPoints("KIDSBROOKE", "TIDEBROOK") == 78) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("KIDSBROOKE", "TIDEBROOK")));
            System.out.println("SIMILAR TEST 004: " + ((obj.isSimilarOnPoints("KIDDSBROOK", "KIDSBROOKE") == 96) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("KIDDSBROOK", "KIDSBROOKE")));
            System.out.println("SIMILAR TEST 005: " + ((obj.isSimilarOnPoints("BRAMLEYVIEW", "BRAMLEY VIEW EXT 2") == 88) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("BRAMLEYVIEW", "BRAMLEY VIEW EXT 2")));
            System.out.println("SIMILAR TEST 006: " + ((obj.isSimilarOnPoints("BRAMLEY VIEW EXT 2", "BRAMLEYVIEW") == 88) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("BRAMLEY VIEW EXT 2", "BRAMLEYVIEW")));
            System.out.println("SIMILAR TEST 007: " + ((obj.isSimilarOnPoints("OAKDAY GARDEN", "COVENT GARDEN") == 17) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("OAKDAY GARDEN", "COVENT GARDEN")));
            System.out.println("SIMILAR TEST 008: " + ((obj.isSimilarOnPoints("WESTERN AREA", "WESTERN CAPE") == 25) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("WESTERN AREA", "WESTERN CAPE")));
            System.out.println("SIMILAR TEST 009: " + ((obj.isSimilarOnPoints("MOUNT FLETCH", "MOUNT FLETCHER") == 93) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("MOUNT FLETCH", "MOUNT FLETCHER")));
            System.out.println("SIMILAR TEST 010: " + ((obj.isSimilarOnPoints("CLARK ESTATE", "CLARE ESTATE") == 86) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("CLARK ESTATE", "CLARE ESTATE")));
            System.out.println("SIMILAR TEST 012: " + ((obj.isSimilarOnPoints("CLARK ESTATE", "CLARKES ESTATE") == 91) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("CLARK ESTATE", "CLARKES ESTATE")));
            System.out.println("SIMILAR TEST 013: " + ((obj.isSimilarOnPoints("WESTERN AREA", "WESTERN EXT") == 70) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("WESTERN AREA", "WESTERN EXT")));
            System.out.println("SIMILAR TEST 014: " + ((obj.isSimilarOnPoints("WESTERN AREA", "WESTERING") == 61) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("WESTERN AREA", "WESTERING")));

            System.out.println("SIMILAR TEST 015: " + ((obj.isSimilarOnPoints("WITBANK", "WYEBANK") == 71) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("WITBANK", "WYEBANK")));
            System.out.println("SIMILAR TEST 016: " + ((obj.isSimilarOnPoints("WITBANK", "WESBANK") == 71) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("WITBANK", "WESBANK")));
            System.out.println("SIMILAR TEST 017: " + ((obj.isSimilarOnPoints("WITBANK", "WIIBANK") == 92) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("WITBANK", "WIIBANK")));
            System.out.println("SIMILAR TEST 018: " + ((obj.isSimilarOnPoints("WITBANK", "WITIBANK") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("WITBANK", "WITIBANK")));

            System.out.println("SIMILAR TEST 019: " + ((obj.isSimilarOnPoints("MOOLMAN", "ALDWYN MALCOLM MOOLMAN") == 0) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("MOOLMAN", "ALDWYN MALCOLM MOOLMAN")));
            System.out.println("SIMILAR TEST 020: " + ((obj.isSimilarOnPoints("MOOLMAN", "MARVIN ALARIG MOOLMAN") == 10) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("MOOLMAN", "MARVIN ALARIG MOOLMAN")));

            System.out.println("SIMILAR TEST 021: " + ((obj.isSimilarOnPoints("OLIVER", "OLWETHU") == 33) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("OLIVER", "OLWETHU")));
            System.out.println("SIMILAR TEST 022: " + ((obj.isSimilarOnPoints("WILLINGTON MBAMBO", "WELLINGTON MBAMBO") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("WILLINGTON MBAMBO", "WELLINGTON MBAMBO")));
            System.out.println("SIMILAR TEST 023: " + ((obj.isSimilarOnPoints("ENSIE", "EMMERENTIA") == 0) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("ENSIE", "EMMERENTIA")));
            System.out.println("SIMILAR TEST 024: " + ((obj.isSimilarOnPoints("MONOLIZA", "MONALISA") == 94) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("MONOLIZA", "MONALISA")));
            System.out.println("SIMILAR TEST 025: " + ((obj.isSimilarOnPoints("CAZEL", "CAZLEY") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("CAZEL", "CAZLEY")));
            System.out.println("SIMILAR TEST 026: " + ((obj.isSimilarOnPoints("ERNST", "EARNEST") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("ERNST", "EARNEST")));
            System.out.println("SIMILAR TEST 027: " + ((obj.isSimilarOnPoints("GOEGE", "GEORGE") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("GOEGE", "GEORGE")));
            System.out.println("SIMILAR TEST 028: " + ((obj.isSimilarOnPoints("JOHANNAH", "JOHANNES") == 75) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("JOHANNAH", "JOHANNES")));
            System.out.println("SIMILAR TEST 029: " + ((obj.isSimilarOnPoints("RAMAKGOLLO", "RAMAKGOLO") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("RAMAKGOLLO", "RAMAKGOLO")));
            System.out.println("SIMILAR TEST 030: " + ((obj.isSimilarOnPoints("MAGDALENE", "MAGDELINA") == 44) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("MAGDALENE", "MAGDELINA")));
            System.out.println("SIMILAR TEST 031: " + ((obj.isSimilarOnPoints("NOLUVUYO", "NOLUYOLO") == 50) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("NOLUVUYO", "NOLUYOLO")));
            System.out.println("SIMILAR TEST 032: " + ((obj.isSimilarOnPoints("RATSHIDI", "RACHIDI") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("RATSHIDI", "RACHIDI")));
            System.out.println("SIMILAR TEST 033: " + ((obj.isSimilarOnPoints("SONE", "SONÉ") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("SONE", "SONÉ")));
            System.out.println("SIMILAR TEST 034: " + ((obj.isSimilarOnPoints("MZIKA", "MZIKALANGA") == 80) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("MZIKA", "MZIKALANGA")));
            System.out.println("SIMILAR TEST 035: " + ((obj.isSimilarOnPoints("SICELELILIE", "SICELILE") == 0) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("SICELELILIE", "SICELILE")));
            System.out.println("SIMILAR TEST 036: " + ((obj.isSimilarOnPoints("TSELISO", "TSHILISO") == 57) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("TSELISO", "TSHILISO")));
            System.out.println("SIMILAR TEST 037: " + ((obj.isSimilarOnPoints("TSHEPISO", "TSEPISO") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("TSHEPISO", "TSEPISO")));
            System.out.println("SIMILAR TEST 038: " + ((obj.isSimilarOnPoints("TSHEKURE", "TJEKURE") == 92) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("TSHEKURE", "TJEKURE")));
            System.out.println("SIMILAR TEST 039: " + ((obj.isSimilarOnPoints("FRANS", "FRANCE") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("FRANS", "FRANCE")));
            System.out.println("SIMILAR TEST 040: " + ((obj.isSimilarOnPoints("ELESA", "MVELESA") == 91) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("ELESA", "MVELESA")));
            System.out.println("SIMILAR TEST 041: " + ((obj.isSimilarOnPoints("DIEKETSENG A", "DIEKETSENG ALETTA NHLAPHO") == 80) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("DIEKETSENG A", "DIEKETSENG ALETTA NHLAPHO")));
            System.out.println("SIMILAR TEST 042: " + ((obj.isSimilarOnPoints("VUYO MBULAWA", "VUYOKAZI MBULAWA") == 80) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("VUYO MBULAWA", "VUYOKAZI MBULAWA")));
            System.out.println("SIMILAR TEST 043: " + ((obj.isSimilarOnPoints("MTHABE", "MTSHABE") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("MTHABE", "MTSHABE")));
            System.out.println("SIMILAR TEST 043: " + ((obj.isSimilarOnPoints("ITAMARA MICHAEL", "IVAN MARK MICHAEL") == 64) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("ITAMARA MICHAEL", "IVAN MARK MICHAEL")));

            System.out.println("***************************** REPRESENTING THE ADDRESS ALGO");
            System.out.println("SIMILAR TEST 040: " + ((obj.isSimilarOnPoints("ROAD742", "ROAD742") == 100) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("ROAD742", "ROAD742")));
            System.out.println("SIMILAR TEST 041: " + ((obj.isSimilarOnPoints("ROAD742", "ROAD732") == 92) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("ROAD742", "ROAD732")));
            System.out.println("SIMILAR TEST 042: " + ((obj.isSimilarOnPoints("MELL VILLE", "MELLVILLE") == 100) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("MELL VILLE", "MELLVILLE")));
            System.out.println("SIMILAR TEST 043: " + ((obj.isSimilarOnPoints("MELL VILLE", "BELLVILLE") == 86) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("MELL VILLE", "BELLVILLE")));
            System.out.println("SIMILAR TEST 044: " + ((obj.isSimilarOnPoints("PELSVILLE", "PETSVALE") == 75) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("PELSVILLE", "PETSVALE")));
            System.out.println("SIMILAR TEST 045: " + ((obj.isSimilarOnPoints("PELSVILLE", "PEARLSVLEI") == 33) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("PELSVILLE", "PEARLSVLEI")));
            System.out.println("SIMILAR TEST 046: " + ((obj.isSimilarOnPoints("WESTERN AREA", "WESTONARIA") == 40) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("WESTERN AREA", "WESTONARIA")));
            System.out.println("SIMILAR TEST 047: " + ((obj.isSimilarOnPoints("WESTERN AREA", "WESTGATE") == 0) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("WESTERN AREA", "WESTGATE")));
            System.out.println("SIMILAR TEST 048: " + ((obj.isSimilarOnPoints("KLEIN WINDHOEK N", "KLEINMONDE EAST") == 43) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("KLEIN WINDHOEK N", "KLEINMONDE EAST")));
            System.out.println("SIMILAR TEST 049: " + ((obj.isSimilarOnPoints("THE WHITE RIVER", "WHITE RIVER") == 100) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("THE WHITE RIVER", "WHITE RIVER")));
            System.out.println("SIMILAR TEST 050: " + ((obj.isSimilarOnPoints("ROODE ELS BERG", "ROODBERG") == 53) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("ROODE ELS BERG", "ROODBERG")));
            System.out.println("SIMILAR TEST 051: " + ((obj.isSimilarOnPoints("UITZIT", "UITZICHT") == 76) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("UITZIT", "UITZICHT")));
            System.out.println("SIMILAR TEST 052: " + ((obj.isSimilarOnPoints("OLYPHANTS FONTYN", "OLYPHANTS FONTEIN") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("OLYPHANTS FONTYN", "OLYPHANTS FONTEIN")));
            System.out.println("SIMILAR TEST 053: " + ((obj.isSimilarOnPoints("MORGENWAGT", "MORGENWACHT") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("MORGENWAGT", "MORGENWACHT")));
            System.out.println("SIMILAR TEST 054: " + ((obj.isSimilarOnPoints("PIETERSDEEL", "PIETERSDAL") == 80) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("PIETERSDEEL", "PIETERSDAL")));
            System.out.println("SIMILAR TEST 055: " + ((obj.isSimilarOnPoints("GRASVLEY", "GRASVLEI") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("GRASVLEY", "GRASVLEI")));
            System.out.println("SIMILAR TEST 056: " + ((obj.isSimilarOnPoints("HESTERS RUST", "HESTER'S RUST") == 100) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("HESTERS RUST", "HESTER'S RUST")));
            System.out.println("SIMILAR TEST 057: " + ((obj.isSimilarOnPoints("CALEFORNIA", "CALEDONIA") == 78) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("CALEFORNIA", "CALEDONIA")));
            System.out.println("SIMILAR TEST 058: " + ((obj.isSimilarOnPoints("BROEK RIVIERS", "BREERIVIER") == 28) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("BROEK RIVIERS", "BREERIVIER")));
            System.out.println("SIMILAR TEST 059: " + ((obj.isSimilarOnPoints("WESSELSKOP", "WESSELSHOOP") == 96) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("WESSELSKOP", "WESSELSHOOP")));
            System.out.println("SIMILAR TEST 061: " + ((obj.isSimilarOnPoints("VAALKOPFONTEIN", "VAALFONTEIN") == 0) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("VAALKOPFONTEIN", "VAALFONTEIN")));
            System.out.println("SIMILAR TEST 062: " + ((obj.isSimilarOnPoints("STEENBRAS CATEMANT AREA", "STEENBRAS CATCHMENT AREA") == 95) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("STEENBRAS CATEMANT AREA", "STEENBRAS CATCHMENT AREA")));
            System.out.println("SIMILAR TEST 063: " + ((obj.isSimilarOnPoints("JACKALS KRAAL", "JACKALSKRAAL") == 100) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("JACKALS KRAAL", "JACKALSKRAAL")));
            System.out.println("SIMILAR TEST 064: " + ((obj.isSimilarOnPoints("VENTERPOST", "VENTERSPOST") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("VENTERPOST", "VENTERSPOST")));
            System.out.println("SIMILAR TEST 065: " + ((obj.isSimilarOnPoints("HARTBEESTFONTEIN", "HARTEBEESFONTEIN") == 81) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("HARTBEESTFONTEIN", "HARTEBEESFONTEIN")));
            System.out.println("SIMILAR TEST 066: " + ((obj.isSimilarOnPoints("WITPOORTJE", "WITPOORTJIE") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("WITPOORTJE", "WITPOORTJIE")));
            System.out.println("SIMILAR TEST 067: " + ((obj.isSimilarOnPoints("HARTSENBERGFONTEIN", "HARTZENBERGFONTEIN") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("HARTSENBERGFONTEIN", "HARTZENBERGFONTEIN")));
            System.out.println("SIMILAR TEST 068: " + ((obj.isSimilarOnPoints("KLIPRIVIERSBERG", "KLIPRIVIERSBERG ESTATE") == 91) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("KLIPRIVIERSBERG", "KLIPRIVIERSBERG ESTATE")));
            System.out.println("SIMILAR TEST 069: " + ((obj.isSimilarOnPoints("WITKOPPIE", "WITKOPPIES") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("WITKOPPIE", "WITKOPPIES")));
            System.out.println("SIMILAR TEST 070: " + ((obj.isSimilarOnPoints("WITPOORTJE", "WITPOORTJIE") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("WITPOORTJE", "WITPOORTJIE")));
            System.out.println("SIMILAR TEST 071: " + ((obj.isSimilarOnPoints("POORTJE", "POORTJIE") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("POORTJE", "POORTJIE")));
            System.out.println("SIMILAR TEST 072: " + ((obj.isSimilarOnPoints("POORTIJIE", "POORTJIE") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("POORTIJIE", "POORTJIE")));
            System.out.println("SIMILAR TEST 073: " + ((obj.isSimilarOnPoints("MIDSTREAM ESTATE", "MIDSTREAM ESTATES") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("MIDSTREAM ESTATE", "MIDSTREAM ESTATES")));
            System.out.println("SIMILAR TEST 074: " + ((obj.isSimilarOnPoints("MIDSTREAM ESTATE", "MIDSTREAM ESTATS") == 89) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("MIDSTREAM ESTATE", "MIDSTREAM ESTATS")));
            System.out.println("SIMILAR TEST 075: " + ((obj.isSimilarOnPoints("MIDSTREAM ESTATS", "MIDSTREAM ESTATES") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("MIDSTREAM ESTATS", "MIDSTREAM ESTATES")));
            System.out.println("SIMILAR TEST 076: " + ((obj.isSimilarOnPoints("KLIPPOORTIJIE", "KLIPPOORTJE") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("KLIPPOORTIJIE", "KLIPPOORTJE")));
            System.out.println("SIMILAR TEST 077: " + ((obj.isSimilarOnPoints("KLIPPOORTIJIE", "KLIPPOORTJIE") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("KLIPPOORTIJIE", "KLIPPOORTJIE")));
            System.out.println("SIMILAR TEST 078: " + ((obj.isSimilarOnPoints("KLIPPOORTJIE", "KLIPPOORTJE") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("KLIPPOORTJIE", "KLIPPOORTJE")));
            System.out.println("SIMILAR TEST 079: " + ((obj.isSimilarOnPoints("TEANONG", "TEMONG") == 89) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("TEANONG", "TEMONG")));
            System.out.println("SIMILAR TEST 079: " + ((obj.isSimilarOnPoints("THE RAND COLLIERIES", "THE RAND COLLIERS") == 93) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("THE RAND COLLIERIES", "THE RAND COLLIERS")));

            System.out.println("***************************** REPRESENTING TRUST NUMBERS");

            System.out.println("SIMILAR TEST 100: " + ((obj.isSimilarOnPoints("001411/2004", "000411/2004") == 89) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("001411/2004", "000411/2004")));
            System.out.println("SIMILAR TEST 101: " + ((obj.isSimilarOnPoints("001141/2004", "000411/2004") == 67) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("001141/2004", "000411/2004")));
            System.out.println("SIMILAR TEST 102: " + ((obj.isSimilarOnPoints("001411/2004", "000411/2005") == 76) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("001411/2005", "000411/2004")));
            System.out.println("SIMILAR TEST 103: " + ((obj.isSimilarOnPoints("001411/20043", "000411/2004") == 89) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("001411/20043", "000411/2004")));
            System.out.println("SIMILAR TEST 104: " + ((obj.isSimilarOnPoints("000411/2014", "000411/2004") == 89) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("000411/2014", "000411/2004")));

            System.out.println("***************************** REPRESENTING COMPANY NAMES");
            System.out.println("SIMILAR TEST 041: " + ((obj.isSimilarOnPoints("W N VIS", "PATRICK WILLIAM KEMPTHORNE") == 0) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("W N VIS", "PATRICK WILLIAM KEMPTHORNE")));
            System.out.println("SIMILAR TEST 042: " + ((obj.isSimilarOnPoints("PACIFIC HEIGTHS INVESTMENTS 72", "PACIFIC HEIGHTS INVESTMENT") == 44) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("PACIFIC HEIGTHS INVESTMENTS 72", "PACIFIC HEIGHTS INVESTMENT")));
            System.out.println("SIMILAR TEST 043: " + ((obj.isSimilarOnPoints("3 MP SALES AND EXUCATIONAL SERVICE", "3MP SALES AND EDUCATION SERVICES") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("3 MP SALES AND EXUCATIONAL SERVICE", "3MP SALES AND EDUCATION SERVICES")));
            System.out.println("SIMILAR TEST 044: " + ((obj.isSimilarOnPoints("REVOLUTION SKATEBOARDING SUPPLY", "(R)EVOLUTION SKATEBOARDING SUPPLIES") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("REVOLUTION SKATEBOARDING SUPPLY", "(R)EVOLUTION SKATEBOARDING SUPPLIES")));
            System.out.println("SIMILAR TEST 045: " + ((obj.isSimilarOnPoints("ALAKAMAR APPLIANCE", "000 ALKMAR APPLIANCES") == 69) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("ALAKAMAR APPLIANCE", "000 ALKMAR APPLIANCES")));
            System.out.println("SIMILAR TEST 046: " + ((obj.isSimilarOnPoints("TWO SHOES NOW", "2 SHOES NOW TRADING") == 86) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("TWO SHOES NOW", "2 SHOES NOW TRADING")));
            System.out.println("SIMILAR TEST 047: " + ((obj.isSimilarOnPoints("21ST CENTURY BUSINESS & PAY SOLUTION ", "21ST CENTURY PAY SOLUTION") == 92) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("21ST CENTURY BUSINESS & PAY SOLUTION ", "21ST CENTURY PAY SOLUTION")));
            System.out.println("SIMILAR TEST 048: " + ((obj.isSimilarOnPoints("B I T DISTRIBUTIONS AND CONSULTING", "B I T CONSULTING AND DISTRIBUTION") == 99) ? "PASSED" : "************* FAILED " + obj.isSimilarOnPoints("B I T DISTRIBUTIONS AND CONSULTING", "B I T CONSULTING AND DISTRIBUTION")));
            // NAGDAELNE
            // NAGDELINA
            System.out.println("**************************");
        }
        obj.setThresholdRating(72);
        System.out.println("******* THE SCORE: " + obj.isSimilarOnPoints("GREATER LETABA LOCAL MUNICIPALITY", "GREATER LETABA MUNICIPALITY"));

        /*
         * Levenshtein tesing with swaps
         */
        System.out.println(obj.levenshteinDistance("SELEMA", "THELMA"));
        System.out.println(obj.levenshteinDistanceWithSwapDiscount("SELEMA",
                                                                   "THELMA"));

      //  System.out.println("******* THE SCORE: " + obj.isSimilar("ITAMARA MICHAEL","IVAN MARK MICHAEL")); [SELEMA] with formal [THELMA]

        
         System.out.println(obj.isSimilarOnPoints("SELEMA", "THELMA"));
         System.out.println(obj.isSimilar("SELEMA", "THELMA"));
         

        /*
         System.out.println("**************************");
         System.out.println(obj.isSimilarOnPoints("GROBERSDAAL", "GROBLERSDAL"));
         System.out.println(obj.isSimilar("GROBERSDAAL", "GROBLERSDAL"));
        
         System.out.println("**************************");
         System.out.println(obj.isSimilarOnPoints("BELLA-BELLA", "BELA BELA"));
         System.out.println(obj.isSimilar("BELLA-BELLA", "BELA BELA"));
         System.out.println("**************************");
         System.out.println(obj.isSimilarOnPoints("KIMBERLEY", "KIMBERLY"));
         System.out.println(obj.isSimilar("KIMBERLEY", "KIMBERLY"));
        
         System.out.println("**************************");
         System.out.println(obj.isSimilarOnPoints("WELGEHUIWEN", "WILGEHEUWEL"));
         System.out.println(obj.isSimilar("WELGEHUIWEN", "WILGEHEUWEL"));
        
         System.out.println("**************************");
         System.out.println(obj.isSimilarOnPoints("LUTHULI PARK", "CHIEF LUTHULI PARK"));
         System.out.println(obj.isSimilar("LUTHULI PARK", "CHIEF LUTHULI PARK"));
        
         System.out.println("**************************");
         System.out.println(obj.isSimilarOnPoints("TRAMSHED", "THE TRAMSHED"));
         System.out.println(obj.isSimilar("TRAMSHED", "THE TRAMSHED"));
         System.out.println("**************************");
         System.out.println(obj.isSimilarOnPoints("MILLER LANE", "MALELANE"));
         System.out.println(obj.isSimilar("MILLER LAAN", "MALELANE"));
         * */
        //obj.processTokenFile();
        //obj.processMassCompare();
        System.out.println("lapsed time: " + objTimer.lapsedTime());
    }
}
