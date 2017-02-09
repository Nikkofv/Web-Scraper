/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.vogel.common.tokenFactory;

import java.util.*;
import com.vogel.common.util.COMMONtoolkit;
import com.vogel.common.util.COMMONtoolkit;

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
public class MyReplaceTokenFactory {

    private LinkedHashMap<String, MyReplaceToken> hmHolder = null;

    public MyReplaceTokenFactory() {
        hmHolder = new LinkedHashMap();
    }

    public void register(MyReplaceToken obj) {
        hmHolder.put(obj.token, obj);
    }

    public String rephrase(String rString) {
        // check if we can find the word else we spin through all the options
      //  MyReplaceToken obj = hmHolder.get(rString);
      //  if (obj != null) {
      //      rString = obj.rephrase(rString);
      //  } else {
            Set s=hmHolder.keySet();
            Iterator<String> ite2=s.iterator();
            while (ite2.hasNext()) {
                MyReplaceToken rObj=hmHolder.get(ite2.next());
                rString = rObj.rephrase(rString);
            }
     //   }
        return COMMONtoolkit.getInstance().removeDoubleSpacesTrim(rString);
    }

    public static void main(String args[]) {

        MyReplaceTokenFactory obj = new MyReplaceTokenFactory();
         obj.register(new MyReplaceToken("(S[ ]{0,}A[ ]{0,}B[ ]{0,}) ", "SAB", "", true));
        String myPhrase = "ABSA BANK";
        System.out.println("processing: " + myPhrase);
        System.out.println("rephrases: " + obj.rephrase(myPhrase));
    }
}
