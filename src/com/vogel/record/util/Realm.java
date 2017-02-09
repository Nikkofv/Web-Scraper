/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vogel.record.util;

import java.io.File;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedMap;

/**
 *
 * @author VOGEL
 */
public abstract class Realm {

    static final int HALT = -1;
    public boolean bhalt = false;
    int iThreadNumber = 0;
    public String rString = "";
    public String file = "";
    public HashMap<String, String> hm = null;
    public Set dups = null;
    public FILIOFactory filio = null;
    public File folder = null;
    public String delimiter = "";
    public int iSeq = 0;
    public SortedMap output = null;
    public SortedMap output2 = null;
    public SortedMap sunburst = null;
    public boolean bDryRun  = false;
    public String db = "";
    public String table = "";
    public RecordFactory rf = null;
    public Realm() {
    }
    
    public Realm(int i) {
        if (i == -1) {
            bhalt = true;
        } else {
            iSeq = i;
        }
    }
}
