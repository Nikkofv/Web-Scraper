/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.vogel.common.tokenFactory;

import com.vogel.common.util.COMMONtoolkit;
import com.vogel.common.util.COMMONtoolkit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author VOGEL
 *
 * THIS FACTORY WILL ASSIST IN TRANSLATIONS OF TOKENS INTO A MORE USEFUL TOKEN
 * YOU REGISTER TRANSLATION RULES WITH THIS FACTORY BEFORE USE.
 *
 * TRANSLATIONS HAPPEN FOR SINGLE WORDS, ENDING AND BEGINNING WORDS
 *
 *
 */
public class MyReplaceToken {

    public String token = "";
    private String matchToken = "";
    private String newToken = "";
    private String sAsType = "";
    private boolean bReplaceALL = false;
    private boolean bReplaceFirst = true;
    private boolean bReplaceLast = false;
    private boolean bReplaceAsType = false;
    private boolean bReplaceSelf = false;

    Pattern pMatcher = null;

    public MyReplaceToken() {
        this.matchToken = "";
        this.newToken = "";
        this.token = "";
        bReplaceALL = false;
    }

    public MyReplaceToken(String token, String newToken, String type, boolean bReplaceAll) {
        this.token = token;
        this.newToken = newToken;
        this.bReplaceALL = bReplaceAll;

        if (type.equals("WORD")) {
            this.matchToken = regexWORD(token);
        } else {
            if (type.equals("ENDING")) {
                this.matchToken = regexWORD(token) + "|" + regexENDING(token);
            } else {
                if (type.equals("BEGINNING")) {
                    this.matchToken = regexWORD(token) + "|" + regexBEGINNING(token);
                } else {
                    if (type.equals("ALL")) {
                        this.matchToken = regexWORD(token) + "|" + regexBEGINNING(token) + "|" + regexENDING(token);
                    } else {
                        this.matchToken = token;
                    }
                }
            }
        }
        pMatcher = Pattern.compile(this.matchToken);
    }
    
   

    public MyReplaceToken(String token, String newToken, String type, boolean bReplaceAll, boolean bReplaceFirst) {
        this.token = token;
        this.newToken = newToken;
        this.bReplaceALL = bReplaceAll;
        this.bReplaceFirst = bReplaceFirst;

        if (type.equals("WORD")) {
            bReplaceAsType = bReplaceFirst;
            this.matchToken = regexWORD(token);
        } else {
            if (type.equals("ENDING")) {
                this.matchToken = regexWORD(token) + "|" + regexENDING(token);

            } else {
                if (type.equals("ENDINGV")) {
                    this.matchToken = regexENDINGV(token);
                    bReplaceLast = true;
                } else {
                    if (type.equals("BEGINNING")) {
                        this.matchToken = regexWORD(token) + "|" + regexBEGINNING(token);
                    } else {
                        if (type.equals("ALL")) {
                            this.matchToken = regexWORD(token) + "|" + regexBEGINNING(token) + "|" + regexENDING(token);
                        } else {
                            this.matchToken = token;
                        }
                    }
                }
            }
        }
        pMatcher = Pattern.compile(this.matchToken);
    }

    public MyReplaceToken(String token, String matchToken, String newToken, String type, boolean bReplaceAll) {
        this.token = token;
        this.newToken = newToken;
        this.bReplaceALL = bReplaceAll;

        if (type.equals("WORD")) {
            this.matchToken = regexWORD(matchToken);
        } else {
            if (type.equals("ENDING")) {
                this.matchToken = regexWORD(matchToken) + "|" + regexENDING(matchToken);
            } else {
                if (type.equals("BEGINNING")) {
                    this.matchToken = regexWORD(matchToken) + "|" + regexBEGINNING(matchToken);
                } else {
                    if (type.equals("ALL")) {
                        this.matchToken = regexWORD(matchToken) + "|" + regexBEGINNING(matchToken) + "|" + regexENDING(matchToken);
                    } else {
                        if (token.length() > 0) {
                            this.matchToken = token;
                        }
                    }
                }
            }
        }
        pMatcher = Pattern.compile(this.matchToken);
    }

      public MyReplaceToken(String type) {
        this.newToken = "CO";
        this.bReplaceALL = false;
        this.bReplaceSelf = true;


        this.matchToken="(COMPANY[A-Z]+)";
    
        pMatcher = Pattern.compile(this.matchToken);
    }
      
    public String rephrase(String aToken) {
        purifyToken pToken=new purifyToken();
         aToken = pToken.replacePunctuationsKeepNothing(aToken);
        try {

            
            Matcher myMatcher = pMatcher.matcher(aToken);
            if (myMatcher.find()) {
                
         //  System.out.println("**** rephrasing ["+aToken+"] with ["+this.matchToken+"]");

                if (bReplaceSelf) {
                    aToken = COMMONtoolkit.getInstance().removeDoubleSpacesTrim(aToken.substring(0, myMatcher.start()) + "CO" + aToken.substring(aToken.indexOf("COMPANY")+"COMPANY".length()));
                    return aToken;
                }
                
                if (!this.bReplaceALL) {
                    if (this.bReplaceFirst) {
                        if (bReplaceAsType) {
                            //System.out.println("************************ ATOKEN " + aToken + "sAsType(" + sAsType + ")");
                            aToken = aToken.replaceFirst(this.sAsType, this.newToken);
                            //System.out.println("************************ ATOKEN " + aToken);
                        } else {
                            if (bReplaceLast) {
                                int iIndex = aToken.lastIndexOf(this.token);
                                if (iIndex > -1) {
                                    aToken = aToken.substring(0, iIndex) + this.newToken + aToken.substring(iIndex + this.token.length());
                                }
                            } else {
                                // this might not at all be where the match was done.....
                                // aToken = COMMONtoolkit.getInstance().removeDoubleSpacesTrim(aToken.substring(myMatcher.start(),myMatcher.end())+" "+this.newToken+" "+aToken.substring(myMatcher.end()+this.token.length()));
                                aToken = COMMONtoolkit.getInstance().removeDoubleSpacesTrim(aToken.substring(0, myMatcher.start()) + " " + this.newToken + " " + aToken.substring(myMatcher.end()));
                                // aToken = aToken.replaceFirst(this.token, this.newToken);
                            }
                        }
                    } else {
                        if (bReplaceAsType) {
                            aToken = aToken.replaceAll(this.sAsType, this.newToken);
                        } else {
                            aToken = aToken.replaceAll(this.token, this.newToken);
                        }
                    }
                } else {
                    aToken = this.newToken;
                }
            }
        } catch (Exception e) {
            System.out.println("*** FAILED on aToken: " + aToken + " matching with: " + matchToken);
            e.printStackTrace();
            System.exit(0);
        }
        
        return aToken;
    }

    private String regexWORD(String matchToken) {
        // just a single word
        String rString = "(^" + matchToken + "$)";
        // starting word
        rString = "(^" + matchToken + "[ ])|" + rString;
        // ending word
        rString = "([ ]" + matchToken + "$)|" + rString;
        // middle word
        rString = "([ ]" + matchToken + "[ ])|" + rString;

        // the as type definition
        if (bReplaceAsType) {
            sAsType = "(^" + matchToken + "$)|(^" + matchToken + "[ ])|([ ]" + matchToken + "$)|([ ]" + matchToken + "[ ])";
            newToken = " " + newToken + " ";
        }
        return rString;
    }
    
    private String regexENDING(String matchToken) {
        // just a single word
        // STR$
        // STR asdf
        String rString = "(" + matchToken + "$)";
        rString = "(" + matchToken + "[ ])|" + rString;
        return rString;
    }

    private String regexENDINGV(String matchToken) {
        // just a single word
        // STR$
        // STR asdf
        //   String rString = "(.*" + matchToken + "$)";
        //  return rString;
        String rString = "(" + matchToken + "$)";
        rString = "(" + matchToken + "[ ])|" + rString;
        return rString;
    }

    private String regexBEGINNING(String matchToken) {
        // just a single word
        String rString = "(^" + matchToken + ")";
        rString = "([ ]" + matchToken + ")|" + rString;
        return rString;
    }
}
