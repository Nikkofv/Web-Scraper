/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.vogel.common.util;

import java.util.*;
import java.io.*;

/**
 *
 * @author  VOGEL
 * @version TEST CHANGE
 */
public class COMMONsys extends Object {

    private static COMMONsys myInstance = null;
    private static Properties dbProps = null;

    public static COMMONsys getInstance() {
        if (myInstance == null) {
            myInstance = new COMMONsys();
        }
        return myInstance;
    }

    /** Creates new WSTYPatternAnalyzer */
    public COMMONsys() {
        try {
            InputStream is = getClass().getResourceAsStream("/common.properties");
            dbProps = new Properties();
            dbProps.load(is);
        } catch (Exception e) {
            System.out.println("Can't read the COMMON properties file. "
                    + "Did you put the common.properties in the CLASSPATH?");
            dbProps = null;
            return;
        }
    }

    public String getCOMMONpath() {
        if (dbProps != null) {
            return dbProps.getProperty("CommonPath");
        } else {
            System.out.println("common.properties not found");
            return "common.properties not found";
        }
    }
    
     public String getDEVpath() {
        if (dbProps != null) {
            return dbProps.getProperty("DevPath");
        } else {
            System.out.println("common.properties not found");
            return "common.properties not found";
        }
    }
    
   public String getDBPropertiesFileLocation() {
        if (dbProps != null) {
            return dbProps.getProperty("DBProperties");
        } else {
            System.out.println("common.properties not found");
            return "common.properties not found";
        }
    }

    public static void main(String[] args) {
        System.out.println(COMMONsys.getInstance().getCOMMONpath());
    }
}
