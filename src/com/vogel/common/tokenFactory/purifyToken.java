/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.vogel.common.tokenFactory;

import java.util.*;

/**
 *
 * @author VOGEL THE PURPOSE OF THIS MODULE IS TO PURIFY TOKENS AND
 * ENFORCE UNIFORMITY
 *
 */
public class purifyToken {

    public purifyToken() {
    }

    public String removePunctuationsAndSpace(String rString) {
        String nString = "";
        StringTokenizer st = new StringTokenizer(rString, " .,\"`'?!;:#$%&()*+-/<>=@[]\\^_{}|~");
        while (st.hasMoreTokens()) {
            String aToken = st.nextToken();
            nString = nString + aToken;
        }
        return nString;
    }

    public String removePunctuationsAndSpaceKeepAmpersand(String rString) {
        String nString = "";
        StringTokenizer st = new StringTokenizer(rString, " .,\"`'?!;:#$%()*+-/<>=@[]\\^_{}|~");
        while (st.hasMoreTokens()) {
            String aToken = st.nextToken();
            nString = nString + aToken;
        }
        return nString;
    }

    public String removePunctuationsAndSpaceKeepSlash(String rString) {
        String nString = "";
        StringTokenizer st = new StringTokenizer(rString, " .,\"`'?!;:#$%&()*+-<>=@[]\\^_{}|~");
        while (st.hasMoreTokens()) {
            String aToken = st.nextToken();
            nString = nString + aToken;
        }
        return nString;
    }

    public String removePunctuationsAndSpaceForEmail(String rString) {
        String nString = "";
        StringTokenizer st = new StringTokenizer(rString, " ,\"`'?!;:#$%&()*/<>=[]\\^{}|~");
        while (st.hasMoreTokens()) {
            String aToken = st.nextToken();
            nString = nString + " " + aToken;
        }
        return nString;
    }

    public void test() {
        System.out.println("TEST");
    }

    public String removeEnclosingQuotes(String rString) {
        if (!rString.isEmpty()) {
            while (rString.length() > 0 && rString.charAt(0) == '"') {
                rString = rString.substring(1);
            }
            while (rString.length() > 0 && rString.charAt(rString.length() - 1) == '"') {
                rString = rString.substring(0, rString.length() - 1);
            }
        }
        // remove tabs and carriage returns
        return rString;
    }

    /*
     * THIS ONE IS USED FOR ADDRESS TOKENS
     */
    public String replacePunctuationsKeepDashTick(String rString) {
        String nString = "";
        StringTokenizer st = new StringTokenizer(rString, " .,\"?!;:#$%&()*+/<>=@[]\\^_{}|~");
        while (st.hasMoreTokens()) {
            String aToken = st.nextToken();
            nString = nString + " " + aToken;
        }
        return nString.trim();
    }

    public String replacePunctuationsKeepNothing(String rString) {
        String nString = "";
        StringTokenizer st = new StringTokenizer(rString, " .-',\"?!;:#$%&()*+/<>=@[]\\^_{}|~");
        while (st.hasMoreTokens()) {
            String aToken = st.nextToken();
            nString = nString + " " + aToken;
        }
        return nString.trim();
    }

    public String replacePunctuationsKeepOnlyCharacters(String rString) {
        String nString = "";
        StringTokenizer st = new StringTokenizer(rString, " .-',\"?!;:#$%&()*+/<>=@[]\\^_{}|~0123456789");
        while (st.hasMoreTokens()) {
            String aToken = st.nextToken();
            nString = nString + " " + aToken;
        }
        return nString.trim();
    }

    /*
     * THIS ONE IS USED FOR NAME TOKENS
     */
    public String replacePunctuationsKeepDashTickAmpersand(String rString) {
        String nString = "";
        StringTokenizer st = new StringTokenizer(rString, " .,\"?!;:#$%()*+/<>=@[]\\^_{}|~");
        while (st.hasMoreTokens()) {
            String aToken = st.nextToken().trim();
            nString = nString + " " + aToken;
        }
        return nString.trim();
    }

    public String replacePunctuationsKeepDashTickAmpersandBrackets(String rString) {
        String nString = "";
        StringTokenizer st = new StringTokenizer(rString, " .,\"?!;:#$%)*+/<>=@]\\^_}|~");
        while (st.hasMoreTokens()) {
            String aToken = st.nextToken();
            nString = nString + " " + aToken;
        }
        return nString.trim();
    }

    public String replacePunctuationsKeepForScentence(String rString) {
        String nString = "";
        StringTokenizer st = new StringTokenizer(rString, " #$%()*+/<>=@[]\\^_{}|~");
        while (st.hasMoreTokens()) {
            String aToken = st.nextToken();
            nString = nString + " " + aToken;
        }
        return nString.trim();
    }
}
