/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.vogel.record.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.mozilla.universalchardet.UniversalDetector;

import com.vogel.common.util.COMMONdate;
import com.vogel.common.util.COMMONsys;
import com.vogel.common.util.COMMONtiming;
import com.vogel.common.util.COMMONtoolkit;

/**
 *
 * @author VOGEL
 * @version
 */
public class FILIOFactory extends Object {

    public String theProject = "";
    public String filename = "";
    public String OutputFileName = "";
    private String pathIN = "";
    private String pathOUT = "";
    public String CharacterSet = "";
    private File fi = null;
    private File fo = null;
    public boolean bHeaderAreGood = false;
    private boolean bEOF = false;
    private FileInputStream fis = null;
    private BufferedReader br = null;
    private FileOutputStream fos = null;
    private BufferedWriter bw = null;
    public SortedMap runSTATS = null;
    public SortedMap runSTATSMESSAGE = null;
    public Map runSTATSX = null;
    private int iMaxTURF = 0;
    public int iTURFIT = 0;
    public ArrayList arlWriters = null;
    public String topCount = "00 TOTAL RECORDS PROCESSED";
    public String topCountSub = "00 IN SUBSESSION PROCESSED";
    private int iDynInterval = 0;
    private int totalUnitsOfWork = 0;
    /*
     * TIMING ELEMENTS
     */
    private COMMONtiming startTime = null;
    /*
     * DB STATUSES
     */
    public final static int SKIP_UPDATE = 6;
    public final static int INSERT_FAILED = 0;
    public final static int INSERT_SUCCESSFUL = 1;
    public final static int UPDATE_FAILED = 2;
    public final static int UPDATE_SUCCESSFUL = 3;
    public final static int DUPLICATE_IGNORED = 4;
    public final static int DUPLICATE_REMOVED = 5;

    public FILIOFactory() {
        runSTATS = Collections.synchronizedSortedMap(new TreeMap());
        runSTATSMESSAGE = Collections.synchronizedSortedMap(new TreeMap());
        arlWriters = new ArrayList();
    }

    public String getFullPath() {
        return this.fi.getPath();
    }

    public String makeFileName() {
        String theBody = "";
        int iIndex = theProject.lastIndexOf('/');
        if (iIndex > -1) {
            theBody = theProject.substring(iIndex + 1);
        } else {
            theBody = theProject;
        }
        return theBody + "_" + COMMONdate.getInstance().getTodayDashed() + "_" + COMMONdate.getInstance().getTimeOfDayDashed();
    }

    public boolean registerFolder(String folder) {
        boolean bFound = false;
        fi = new File(COMMONsys.getInstance().getCOMMONpath()
                + "DATA/" + folder);
        if (fi.exists()) {
            File[] inFiles2 = fi.listFiles();
            System.out.println("*** folder [" + folder + "] contains " + inFiles2.length + " files.");
            theProject = folder;
            bFound = true;
        } else {
            System.out.println("*** folder [" + folder + "] does not exist.");
        }
        return bFound;
    }

    public File[] fetchFilesOrderedFromFolder(String project) {
        File f1 = new File(COMMONsys.getInstance().getCOMMONpath()
                + "DATA/INPUT/" + project + "/");
        File[] inFiles2 = f1.listFiles();
        try {
            // we need to order them first come, first serve
            Arrays.sort(inFiles2, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    if (f1.lastModified() < f2.lastModified()) {
                        return -1;
                    } else if (f1.lastModified() == f2.lastModified()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("*** NO FILES IN FOLDER?");
            System.out.println(f1.getAbsolutePath());
            //e.printStackTrace();
        }
        return inFiles2;
    }

    public File[] fetchFilesOrderedFromFolder(String base, String project) {
        File f1 = new File(COMMONsys.getInstance().getCOMMONpath()
                + "DATA/" + base + "/" + project + "/");
        File[] inFiles2 = f1.listFiles();
        try {
            // we need to order them first come, first serve
            Arrays.sort(inFiles2, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    if (f1.lastModified() < f2.lastModified()) {
                        return -1;
                    } else if (f1.lastModified() == f2.lastModified()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("*** NO FILES IN FOLDER?");
            System.out.println(f1.getAbsolutePath());
            //e.printStackTrace();
        }
        return inFiles2;
    }

    public File[] fetchFilesOrderedFromFolder(String volume, String base, String project) {
        String fullPath = volume
                + "DATA/" + base + "/" + project + "/";
        System.out.println("**** " + fullPath);
        File f1 = new File(fullPath);
        File[] inFiles2 = f1.listFiles();
        try {
            // we need to order them first come, first serve
            Arrays.sort(inFiles2, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    if (f1.lastModified() < f2.lastModified()) {
                        return -1;
                    } else if (f1.lastModified() == f2.lastModified()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("*** NO FILES IN FOLDER?");
            System.out.println(f1.getAbsolutePath());
            // e.printStackTrace();
        }
        return inFiles2;
    }

    public File[] fetchFilesOrderedFromFolderWin(String volume, String base, String project) {
        File f1 = new File("C:/" + volume + "/DEPLOYMENT/"
                + "DATA/" + base + "/" + project + "/");
        File[] inFiles2 = f1.listFiles();
        try {
            // we need to order them first come, first serve
            Arrays.sort(inFiles2, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    if (f1.lastModified() < f2.lastModified()) {
                        return -1;
                    } else if (f1.lastModified() == f2.lastModified()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("*** NO FILES IN FOLDER?");
            System.out.println(f1.getAbsolutePath());
            e.printStackTrace();
        }
        return inFiles2;
    }

    public int getTotalUOW() {
        return totalUnitsOfWork - 1;
    }

    private String detect(File fileName) {
        byte[] buf = new byte[4096];
        String encoding = "";
        try {
            java.io.FileInputStream fis = new java.io.FileInputStream(fileName);
            UniversalDetector detector = new UniversalDetector(null);

            int nread;
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            fis.close();
            detector.dataEnd();

            encoding = detector.getDetectedCharset();

            if (encoding == null) {
                encoding = "WINDOWS-1252";
            }

            if (encoding.equals("WINDOWS-1252") || encoding.equals("")) {
                encoding = "WINDOWS-1252";
            } else {
                if (encoding.equals("UTF-8")) {
                    encoding = "UTF-8";
                    // check for BOM
                } else {
                    if (encoding.contains("UTF-16")) {
                        encoding = "UTF-16";
                    } else {
                        if (encoding.contains("UTF-32")) {
                            encoding = "UTF-32";
                        }
                    }
                }
            }
            detector.reset();
        } catch (Exception e) {
        }
        return encoding;
    }

    public boolean register(String project, String filename) {
        this.filename = filename;
        pathIN = COMMONsys.getInstance().getCOMMONpath() + "DATA/INPUT/" + project + "/";
        pathOUT = COMMONsys.getInstance().getCOMMONpath() + "DATA/OUTPUT/" + project + "/";
        theProject = project + '/' + filename;
        String extention = ".txt";
        if (filename.indexOf('.') > -1) {
            extention = filename.substring(filename.indexOf('.'));
            filename = filename.substring(0, filename.indexOf('.'));
        }
        fo = new File(pathOUT + filename + "_" + COMMONdate.getInstance().getTodayDashed() + "_" + COMMONdate.getInstance().getTimeOfDayDashed() + extention);
        this.OutputFileName = fo.getName();
        fi = new File(pathIN + filename + extention);
        CharacterSet = detect(fi);
        return fi.exists();
    }

    public boolean registerFromVol(String volume, String project, String filename) {
        this.filename = filename;
        pathIN = "/" + volume + "DATA/INPUT/" + project + "/";
        pathOUT = "/" + volume + "DATA/OUTPUT/" + project + "/";
        theProject = project + '/' + filename;
        String extention = ".txt";
        if (filename.indexOf('.') > -1) {
            extention = filename.substring(filename.indexOf('.'));
            filename = filename.substring(0, filename.indexOf('.'));
        }
        fo = new File(pathOUT + filename + "_" + COMMONdate.getInstance().getTodayDashed() + "_" + COMMONdate.getInstance().getTimeOfDayDashed() + extention);
        this.OutputFileName = fo.getName();
        fi = new File(pathIN + filename + extention);
        CharacterSet = detect(fi);
        return fi.exists();
    }

    public boolean register(String base, String project, String filename) {
        this.filename = filename;
        pathIN = COMMONsys.getInstance().getCOMMONpath() + "DATA/" + base + "/" + project + "/";
        pathOUT = COMMONsys.getInstance().getCOMMONpath() + "DATA/" + base + "/" + project + "/";
        theProject = project + '/' + filename;
        String extention = ".txt";
        if (filename.indexOf('.') > -1) {
            extention = filename.substring(filename.indexOf('.'));
            filename = filename.substring(0, filename.indexOf('.'));
        }
        fo = new File(pathOUT + filename + "_" + COMMONdate.getInstance().getTodayDashed() + "_" + COMMONdate.getInstance().getTimeOfDayDashed() + extention);
        this.OutputFileName = fo.getName();
        fi = new File(pathIN + filename + extention);
        CharacterSet = detect(fi);
        return fi.exists();
    }

    public boolean registerNoInput(String project, String filename) {
        pathOUT = COMMONsys.getInstance().getCOMMONpath() + "DATA/OUTPUT/" + project + "/";
        String extention = ".txt";
        fo = new File(pathOUT + filename + "_" + COMMONdate.getInstance().getTodayDashed() + "_" + COMMONdate.getInstance().getTimeOfDayDashed() + extention);
        this.OutputFileName = fo.getName();
        return true;
    }
    
    public boolean registerOutFolder(String project, String subProject, String filename) {
        pathIN = COMMONsys.getInstance().getCOMMONpath() + "DATA/INPUT/" + project + "/";
        pathOUT = COMMONsys.getInstance().getCOMMONpath() + "DATA/OUTPUT/" + project + "/" + subProject.toUpperCase() + "/";
        File f = new File(pathOUT);
        f.mkdirs();

        String extention = ".txt";
        if (filename.indexOf('.') > -1) {
            extention = filename.substring(filename.indexOf('.'));
            filename = filename.substring(0, filename.indexOf('.'));
        }
        theProject = project + '/' + subProject.toUpperCase() + '/' + filename.toUpperCase();
        if (filename.toUpperCase().equals(subProject.toUpperCase())) {
            fo = new File(pathOUT + subProject.toUpperCase() + "_" + COMMONdate.getInstance().getTodayDashed() + extention);
        } else {
            fo = new File(pathOUT + filename.toUpperCase() + "_" + subProject.toUpperCase() + "_" + COMMONdate.getInstance().getTodayDashed() + extention);
        }
        this.OutputFileName = fo.getName();
        fi = new File(pathIN + filename + extention);
        CharacterSet = detect(fi);
        return fi.exists();
    }

    public boolean register(File fi) {
        this.filename = fi.getName();
        if (filename.lastIndexOf('.') > 0) {
            filename = filename.substring(0, filename.lastIndexOf('.'));
        }
        this.fi = fi;
        CharacterSet = detect(fi);
        return fi.exists();
    }

    public void setReader() throws IOException {
        fis = new FileInputStream(fi);
        br = new BufferedReader(new InputStreamReader(fis, CharacterSet));
        while (br.readLine() != null) {
            totalUnitsOfWork++;
        }
        fis.close();
        br.close();
        fis = new FileInputStream(fi);
        br = new BufferedReader(new InputStreamReader(fis, CharacterSet));
    }

    public void setWriter() throws IOException {
        final byte bom[] = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        fos = new FileOutputStream(fo);
        bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
        fos.write(bom);
    }

    public void setWriter(String encoding) throws IOException {
        fos = new FileOutputStream(fo);
        bw = new BufferedWriter(new OutputStreamWriter(fos, "WINDOWS-1252"));
        if (encoding.equals("UTF-8")) {
            final byte bom[] = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
            fos.write(bom);
        }
    }

    public void setWriter(boolean bAppend) throws IOException {
        final byte bom[] = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        fos = new FileOutputStream(fo, bAppend);
        bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
        fos.write(bom);
    }

     public boolean registerOutputAnywhere(String fullpath, String filename) {
        pathOUT = COMMONsys.getInstance().getCOMMONpath() + fullpath+"/";
        String extention = ".txt";
        fo = new File(pathOUT + filename + "_" + COMMONdate.getInstance().getTodayDashed() + "_" + COMMONdate.getInstance().getTimeOfDayDashed() + extention);
        this.OutputFileName = fo.getName();
        return true;
    }
    
    public FILIOFactory registerAdditionalWritersToAnywhere(String fullpath, String filename) throws IOException {
        FILIOFactory filio = new FILIOFactory();
        filio.registerOutputAnywhere(fullpath, filename);
        filio.setWriter();
        arlWriters.add(filio);
        return filio;
    }
    
   public FILIOFactory registerAdditionalWritersFromVol(String volume, String project, String filename) throws IOException {
        FILIOFactory filio = new FILIOFactory();
        filio.registerFromVol(volume, project, filename);
        filio.setWriter();
        arlWriters.add(filio);
        return filio;
    }

    public FILIOFactory registerAdditionalWriters(String project, String filename) throws IOException {
        FILIOFactory filio = new FILIOFactory();
        filio.register(project, filename);
        filio.setWriter();
        arlWriters.add(filio);
        return filio;
    }

    public FILIOFactory registerAdditionalWriters(String project, String filename, String encoding) throws IOException {
        FILIOFactory filio = new FILIOFactory();
        filio.register(project, filename);
        filio.setWriter(encoding);
        arlWriters.add(filio);
        return filio;
    }

    public FILIOFactory registerAdditionalWritersOutFolder(String project, String subProject, String filename) throws IOException {
        FILIOFactory filio = new FILIOFactory();
        filio.registerOutFolder(project, subProject, filename);
        filio.setWriter();
        arlWriters.add(filio);
        return filio;
    }

    public String readHeadersGiven(String rString, String sHeadersContain) throws IOException {
        //  String rString = br.readLine();
        if (rString != null) {
            rString = rString.toUpperCase();
            if (rString.length() > 0) {
                // remove UTF8 BOM
                if (65279 == (int) rString.charAt(0)) {
                    rString = rString.substring(1);
                }
            }
        } else {
            rString = "";
            bEOF = true;
        }
        bHeaderAreGood = true;
        if (sHeadersContain.length() > 0 && !bEOF) {
            sHeadersContain = sHeadersContain.toUpperCase();
            StringTokenizer st = new StringTokenizer(sHeadersContain, ",");
            while (st.hasMoreTokens()) {
                String aToken = st.nextToken().trim();
                if (!rString.contains(aToken)) {
                    System.out.println("*** column " + aToken + " not in headers.");
                    System.out.println("*** headers: " + rString);

                    bHeaderAreGood = false;
                    break;
                }
            }
        } else {
            // its a first read
            if (bEOF) {
                rString = null;
                bHeaderAreGood = false;
            } else {
                iTURFIT++;
                TURF(topCount);
            }
        }
        return rString;
    }

    public String readHeaders(String sHeadersContain) throws IOException {
        String rString = br.readLine();
        if (rString != null) {
            rString = rString.toUpperCase();
            if (rString.length() > 0) {
                // remove UTF8 BOM
                if (65279 == (int) rString.charAt(0)) {
                    rString = rString.substring(1);
                }
            }
        } else {
            rString = "";
            bEOF = true;
        }
        bHeaderAreGood = true;
        if (sHeadersContain.length() > 0 && !bEOF) {
            sHeadersContain = sHeadersContain.toUpperCase();
            StringTokenizer st = new StringTokenizer(sHeadersContain, ",");
            while (st.hasMoreTokens()) {
                String aToken = st.nextToken().trim();
                if (!rString.contains(aToken)) {
                    System.out.println("*** column " + aToken + " not in headers.");
                    System.out.println("*** headers: " + rString);

                    bHeaderAreGood = false;
                    break;
                }
            }
        } else {
            // its a first read
            if (bEOF) {
                rString = null;
                bHeaderAreGood = false;
            } else {
                iTURFIT++;
                TURF(topCount);
            }
        }
        return rString;
    }

    public String readHeadersSilent(String sHeadersContain) throws IOException {
        String rString = br.readLine();
        if (rString != null) {
            rString = rString.toUpperCase();
            if (rString.length() > 0) {
                // remove UTF8 BOM
                if (65279 == (int) rString.charAt(0)) {
                    rString = rString.substring(1);
                }
            }
        } else {
            rString = "";
            bEOF = true;
        }
        bHeaderAreGood = true;
        if (sHeadersContain.length() > 0 && !bEOF) {
            sHeadersContain = sHeadersContain.toUpperCase();
            StringTokenizer st = new StringTokenizer(sHeadersContain, ",");
            while (st.hasMoreTokens()) {
                String aToken = st.nextToken().trim();
                if (!rString.contains(aToken)) {
                    bHeaderAreGood = false;
                    break;
                }
            }
        } else {
            // its a first read
            if (bEOF) {
                rString = null;
                bHeaderAreGood = false;
            } else {
                iTURFIT++;
                TURF(topCount);
            }
        }
        return rString;
    }

    public ArrayList readPage() throws IOException {
        ArrayList arl = new ArrayList();
        String rString = br.readLine();
        if (rString != null) {
            if (rString.length() > 0) {
                // remove UTF8 BOM
                if (65279 == (int) rString.charAt(0)) {
                    rString = rString.substring(1);
                }
            }
        } else {
            rString = "";
            bEOF = true;
        }
        bHeaderAreGood = true;
        if (!bEOF) {
            while (rString != null) {
                arl.add(rString);
                rString = br.readLine();
            }
        }
        return arl;
    }

    public String readHTMLPageOriginalFormat() throws IOException {
        StringBuilder sb = new StringBuilder();
        String rString = br.readLine();
        if (rString != null) {
            if (rString.length() > 0) {
                // remove UTF8 BOM
                if (65279 == (int) rString.charAt(0)) {
                    rString = rString.substring(1);
                }
            }
        } else {
            rString = "";
            bEOF = true;
        }
        bHeaderAreGood = true;
        if (!bEOF) {
            while (rString != null) {
                sb.append(rString).append("\n");
                rString = br.readLine();
            }
        }
        return sb.toString();
    }

    public String readHTMLPageOriginalFormatNoLineBreaks() throws IOException {
        StringBuilder sb = new StringBuilder();
        String rString = br.readLine();
        if (rString != null) {
            if (rString.length() > 0) {
                // remove UTF8 BOM
                if (65279 == (int) rString.charAt(0)) {
                    rString = rString.substring(1);
                }
            }
        } else {
            rString = "";
            bEOF = true;
        }
        bHeaderAreGood = true;
        if (!bEOF) {
            while (rString != null) {
                rString = COMMONtoolkit.getInstance().removeDoubleSpacesTrim(rString.replace('\t', ' '));
                sb.append(rString.trim()).append(' ');
                rString = br.readLine();
            }
        }
        return sb.toString();
    }

    public String readHTMLPage() throws IOException {
        StringBuilder sb = new StringBuilder();
        String rString = br.readLine();
        if (rString != null) {
            if (rString.length() > 0) {
                // remove UTF8 BOM
                if (65279 == (int) rString.charAt(0)) {
                    rString = rString.substring(1);
                }
            }
        } else {
            rString = "";
            bEOF = true;
        }
        bHeaderAreGood = true;
        if (!bEOF) {
            while (rString != null) {
                sb.append(rString.trim().toUpperCase());
                rString = br.readLine();
            }
        }
        return sb.toString();
    }

    public int countInfileLines() throws IOException, Exception {
        //return (int) br.lines().count()-1;    //Use when Noah is updated to Java 1.8
        int counter = 0;
        while (br.readLine() != null) {
            counter++;
        }
        return counter - 1;
    }

    public String readLine() throws IOException, Exception {
        if (!bHeaderAreGood) {
            throw new Exception();
        }
        String rString = br.readLine();
        if (rString != null) {
            iTURFIT++;
            TURF(topCount);
        } else {
            bEOF = true;
        }
        return rString;
    }

    public void write(String rString) throws IOException {
        bw.write(rString);
    }

    public void flush() throws IOException {
        bw.flush();
    }

    public void cleanUp() throws IOException {
        if (bw != null) {
            bw.close();
        }
        if (br != null) {
            br.close();
        }
        if (fos != null) {
            fos.close();
        }
        if (fis != null) {
            fis.close();
        }
        for (int i = 0; i < arlWriters.size(); i++) {
            FILIOFactory filio = (FILIOFactory) arlWriters.get(i);
            filio.cleanUp();
        }
    }

    public synchronized long getTURFValue(String key) {
        Long INT = (Long) runSTATS.get(key);
        if (INT == null) {
            return 0;
        } else {
            return INT.longValue();
        }
    }

    public synchronized void TURF(String key) {

        if (key.length() > 0) {
            if (key.length() > iMaxTURF) {
                iMaxTURF = key.length();
            }
            Long INT = (Long) runSTATS.get(key);
            if (INT == null) {
                INT = new Long(1);
            } else {
                INT = INT + 1;
            }
            runSTATS.put(key, INT);
        }
    }

    public void registeredXCounter(Map hm) {
        runSTATSX = hm;
    }

    public synchronized void TURFS(String key) {

        if (key.length() > 0) {
            if (key.length() > iMaxTURF) {
                iMaxTURF = key.length();
            }
            Long INT = (Long) runSTATSX.get(key);
            if (INT == null) {
                INT = new Long(1);
            } else {
                INT = INT + 1;
            }
            runSTATSX.put(key, INT);
        }
    }

    public synchronized void TURFSN(String key) {

        if (key.length() > 0) {
            Long INT = (Long) runSTATSX.get(key);
            if (INT == null) {
                INT = 0L;
            } else {
                INT = INT - 1;
            }
            runSTATSX.put(key, INT);
        }
    }

    public synchronized void TURF(String key, int iValue) {

        if (key.length() > 0) {
            if (key.length() > iMaxTURF) {
                iMaxTURF = key.length();
            }
            Long INT = (Long) runSTATS.get(key);
            if (INT == null) {
                INT = new Long(iValue);
            } else {
                INT = INT + iValue;
            }
            runSTATS.put(key, INT);
        }
    }

    public synchronized void TURF(String key, long iValue) {

        if (key.length() > 0) {
            if (key.length() > iMaxTURF) {
                iMaxTURF = key.length();
            }
            Long INT = (Long) runSTATS.get(key);
            if (INT == null) {
                INT = iValue;
            } else {
                INT = INT + iValue;
            }
            runSTATS.put(key, INT);
        }
    }

    public synchronized void registerTURF(String key) {

        if (key.length() > 0) {
            if (key.length() > iMaxTURF) {
                iMaxTURF = key.length();
            }
            runSTATS.put(key, (long) 0);
        }
    }

    public synchronized void registerTURF(String key, long iValue) {

        if (key.length() > 0) {
            if (key.length() > iMaxTURF) {
                iMaxTURF = key.length();
            }
            runSTATS.put(key, iValue);
        }
    }

    public synchronized void registerTURF(String key, long iValue, String message) {

        if (key.length() > 0) {
            if (key.length() > iMaxTURF) {
                iMaxTURF = key.length();
            }
            String newkey = (key.contains("999 UOW RECEIVED")) ? (message + key) : (key + message);
            // fetch previous key
            String previousKey = (String) runSTATSMESSAGE.get(key);
            if (previousKey != null) {
                runSTATS.remove(previousKey);
            }
            runSTATSMESSAGE.put(key, newkey);
            runSTATS.put(newkey, iValue);
        }
    }

    public ArrayList<String> getTURFsWithKeyStartingWith(String partialKey) {
        ArrayList<String> keys = new ArrayList<>();
        Set<String> keySet = runSTATS.keySet();
        for (String key : keySet) {
            if (key.startsWith(partialKey)) {
                keys.add(key);
            }
        }
        return keys;
    }

    public long getTotalFor(String key) {
        Long INT = (Long) runSTATS.get(key);
        if (INT != null) {
            return INT;
        } else {
            return 0;
        }
    }

    public boolean SHOWSTATS(int iInterval, ArrayList arl) {
        boolean bShowed = false;
        if (startTime == null) {
            startTime = new COMMONtiming();
            startTime.start();
        }
        if (iDynInterval != 0) {
            iInterval = iDynInterval;
        }

        long iCnt = 0;
        Long INTTopCount = (Long) runSTATS.get(topCount);
        Long INTTopSubCount = (Long) runSTATS.get(topCountSub);
        if (INTTopSubCount != null) {
            iCnt = INTTopSubCount;
        } else {
            iCnt = Math.max(iTURFIT, INTTopCount);
        }

        Long XX = (Long) runSTATS.get("XX UOW WORKED");
        if (XX != null) {
            iCnt = XX;
        }

        if (iCnt % iInterval == 0) {
            iDynInterval = iInterval;
            if (arl != null) {
                waitReleaseALL(arl);
            }
            SHOWSTATS();
            bShowed = true;
        }
        return bShowed;
    }

    public boolean SHOWSTATS(int iInterval, ArrayList arl, boolean bDynamic) {
        boolean bShowed = false;
        if (startTime == null) {
            startTime = new COMMONtiming();
            startTime.start();
        }
        if (iDynInterval != 0 && bDynamic) {
            iInterval = iDynInterval;
        }

        long iCnt;
        Long INTTopCount = (Long) runSTATS.get(topCount);
        Long INTTopSubCount = (Long) runSTATS.get(topCountSub);
        if (INTTopSubCount != null) {
            iCnt = INTTopSubCount;
        } else {
            iCnt = Math.max(iTURFIT, INTTopCount);
        }
        Long XX = (Long) runSTATS.get("XX UOW WORKED");
        if (XX != null) {
            iCnt = XX;
        }

        if (iCnt % iInterval == 0) {
            iDynInterval = iInterval;
            if (arl != null) {
                waitReleaseALL(arl);
            }
            SHOWSTATS();
            bShowed = true;
        }
        return bShowed;
    }

    private void waitReleaseALL(ArrayList arl) {
        while (arl.size() > 0) {
            for (int i = 0; i < arl.size(); i++) {
                Thread thread = (Thread) arl.get(i);
                if (!thread.isAlive()) {
                    arl.remove(i);
                }
            }
        }
    }

    public void SHOWSTATS() {
        if (startTime == null) {
            startTime = new COMMONtiming();
            startTime.start();
        }
        if (bEOF) {
            System.out.println(fetchStars() + " COMPLETED RUNSTATS");
        }
        System.out.println(printTURF());
    }

    public void TURFDBUPDATE(int iStatus, String TABLE) {
        if (iStatus == 0) {
            TURF(TABLE + "-03-ADD FAILED");
        } else {
            if (iStatus == 1) {
                TURF(TABLE + "-01-ADD SUCCESS");
            } else {
                if (iStatus == UPDATE_FAILED) {
                    TURF(TABLE + "-04-UPDATE FAILED");
                } else {
                    if (iStatus == UPDATE_SUCCESSFUL) {
                        TURF(TABLE + "-02-UPDATE SUCCESS");
                    } else {
                        if (iStatus == 4) {
                            TURF(TABLE + "-05-DUPLICATE IGNORED");
                        } else {
                            if (iStatus == 5) {
                                TURF(TABLE + "-06-RECORD REMOVED");
                            } else {
                                if (iStatus == SKIP_UPDATE) {
                                    TURF(TABLE + "-07-UPDATE SKIPPED");
                                } else {
                                    TURF(TABLE + "-08-STATUS UNKNOWN");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String printTURF() {
        String stars = fetchStars();
        StringBuilder sb = new StringBuilder();
        sb.append(stars);
        sb.append(" FOR ");
        sb.append(theProject);
        sb.append(' ');
        sb.append(COMMONdate.getInstance().getTimeDayForDisplay());
        sb.append('\n');
        Set s = runSTATS.keySet();
        synchronized (runSTATS) {
            Iterator ite = s.iterator();
            while (ite.hasNext()) {
                String key = (String) ite.next();
                Long value = (Long) runSTATS.get(key);
                sb.append("***");
                try {
                    sb.append(stars.substring(5, Math.max(0, stars.length() - key.length())));
                } catch (Exception e) {
                    sb.append("***");
                }
                sb.append(" ");
                sb.append(key);
                sb.append(": ");
                sb.append(value);
                sb.append('\n');
            }
        }
        sb.append(stars);
        sb.append('\n');
        long iCnt;
        Long INTTopCount = (Long) runSTATS.get(topCount);
        Long INTTopSubCount = (Long) runSTATS.get(topCountSub);
        if (INTTopSubCount != null) {
            iCnt = INTTopSubCount;
        } else {
            if (INTTopCount != null) {
                iCnt = Math.max(iTURFIT, INTTopCount);
            } else {
                iCnt = iTURFIT;
            }
        }
        long lapsedTime = startTime.lapsedTime();
        sb.append("*** running: ");
        sb.append(lapsedTime);
        sb.append("[ms]; ");
        sb.append(lapsedTime / 1000);
        sb.append("[s]; ");
        int iMinutes = (int) lapsedTime / 1000 / 60;
        int iSeconds = ((int) lapsedTime / 1000 - (iMinutes * 60)) * 10 / 60;
        sb.append(iMinutes);
        sb.append(".");
        sb.append(iSeconds);
        sb.append("[min]\n");
        if (lapsedTime / 1000 > 0) {
            Long XX = (Long) runSTATS.get("XX UOW WORKED");
            if (XX == null) {
                sb.append("*** UoW/s = ");
            } else {
                sb.append("*** Loading @ UoW/s = ");
            }
            // the dynamic interval is set to stats per second, let's make it per 12 seconds....
            iDynInterval = (int) (iCnt / (int) (Math.ceil(lapsedTime / 12000.0)));
            sb.append((int) (iCnt / (int) (Math.ceil(lapsedTime / 1000))));
            sb.append('\n');

            if (XX != null) {
                sb.append("*** Working @ UoW/s = ");
                // the dynamic interval is set to stats per second, let's make it per 12 seconds....
                iDynInterval = (int) (XX / (int) (Math.ceil(lapsedTime / 12000.0)));
                sb.append((int) (XX / (int) (Math.ceil(lapsedTime / 1000))));
                sb.append('\n');
            }

        }
        return sb.toString();
    }

    private String fetchStars() {
        try {
            return "***********************************************************************************************************************************************************************************************************************************************************************************".substring(0, iMaxTURF + 5);
        } catch (Exception e) {
            return "*****";
        }
    }

    public static void main(String args[]) {

        System.out.println("start.");
        FILIOFactory filio = new FILIOFactory();
        filio.TURF("IDN");
        filio.TURF("FRANS");
        filio.TURF("IDN");
        filio.TURF("IDN");
        filio.TURF("FRANS");
        System.out.println(filio.printTURF());
        System.out.println("done.");
    }
}
