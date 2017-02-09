/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2001, BERKFONTEIN
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.vogel.common.util;

;


import java.net.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

import com.sun.net.ssl.internal.www.protocol.https.*;

import java.security.Security;
import com.sun.net.ssl.*;
import java.util.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

/**
 *
 * @author Gerrit
 */


public class WebPageGrabberSecure {

    /**
     * Creates a new instance of WebPageGrabber
     */
    private static final String HEADERPREFIX = "header.";
    public int TheResponseCode = 0;
    public String redirectURL = "";
    public String cookies = "";
    public HashMap hmCookie = new HashMap();

    public WebPageGrabberSecure() {
    }

    /**
     * Set request properties.
     *
     * @param urlConn URLConnection to set the standard request properties for.
     * @param propertyTable Hashtable with the properties to set the header with
     */
    protected void setRequestProperties(HttpsURLConnection urlConn, Hashtable propertyTable) {
        Hashtable props = propertyTable;
        Enumeration e = props.keys();
        String key;
        String RequestPropertyName;
        String RequestPropertyValue;

        while (e.hasMoreElements()) {
            key = (String) (e.nextElement());

            if (key.toLowerCase().startsWith(HEADERPREFIX)) {
                RequestPropertyName = key.substring(HEADERPREFIX.length());
                RequestPropertyValue = (String) (props.get(key));
                //           System.out.println(RequestPropertyName + " -> " + RequestPropertyValue );
                urlConn.setRequestProperty(RequestPropertyName, RequestPropertyValue);
            }
        }
    }

    public BufferedImage grabImage(String page, String path, String postMessage, Hashtable propertyTable) {
        URL sampleUrl;
        HttpsURLConnection urlConn = null;
        InputStream is = null;
        OutputStream os;
        BufferedImage image = null;
        try {
            // make url-connection
            sampleUrl = new URL(page);
            urlConn = (HttpsURLConnection) (sampleUrl.openConnection());

            if (propertyTable != null) {
                setRequestProperties(urlConn, propertyTable);
            }

            if (postMessage != null) {
                urlConn.setDoOutput(true);

                os = urlConn.getOutputStream();
                os.write(postMessage.getBytes());
                os.flush();
                os.close();
            }

            // file not found response
            if (urlConn.getResponseCode() == 404) {
                System.out.println("**** 404 **** ");
            }
            StringWriter sw = new StringWriter();
            // file not found response
            if (urlConn.getResponseCode() == 500) {
                System.out.println("**** 500 **** ");
                System.out.println(urlConn.getHeaderFields());
                if (urlConn.getContentLength() != 0) {
                    is = urlConn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String line;

                    line = br.readLine();
                    System.out.println("line: " + line);

                    while (line != null) {
                        sw.write(line);
                        sw.write('\n');
                        line = br.readLine();
                        System.out.println("line: " + line);
                    }
                }
                // close inputstream if any open
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ioe) {
                }

                // close urlconnection if any open
                if (urlConn != null) {
                    urlConn.disconnect();
                }
                //     return sw.toString();
            } else {
                if (urlConn.getContentLength() != 0) {
                    //   is = urlConn.getInputStream();
                    image = ImageIO.read(urlConn.getInputStream());
                    if (image == null) {
                        System.out.println("fuck it, image is null");
                    }
                    /*
                     * System.out.println("reading....");
                     *
                     * FileOutputStream fos=new FileOutputStream(path); int c=0;
                     * int iLength=0; c=is.read(); System.out.println("***
                     * reading all in..."+urlConn.getContentLength()); while(c
                     * != -1 && iLength<urlConn.getContentLength()) {
                     * fos.write(c); iLength++; c=is.read(); }
                     * System.out.println("*** done reading all in..."+iLength);
                     * fos.close();
                     *///
                    //   is.close();
                }
            }
            // close inputstream if any open
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ioe) {
            }
            // close urlconnection if any open
            if (urlConn != null) {
                urlConn.disconnect();
            }
        } catch (Exception e) {
            System.out.println("*** Something went wrong.");
            e.printStackTrace();
        }
        return image;
    }

    private void print_https_cert(HttpsURLConnection con) throws SSLPeerUnverifiedException, IOException {

        if (con != null) {

            // try {
            con.getResponseCode();
            System.out.println("******* RESPONSE CODE: "+con.getResponseCode());
            con.getCipherSuite();

            Certificate[] certs = con.getServerCertificates();

            // } catch (SSLPeerUnverifiedException e) {
            //     e.printStackTrace();
            //  } catch (IOException e) {
            //      e.printStackTrace();
            //  }
        }

    }

    public String postGrabCookie(String page, Hashtable propertyTable, String postMessage, StringWriter returnCookie) {

        URL sampleUrl;
        HttpsURLConnection urlConn = null;
        OutputStream os;
        OutputStreamWriter osw;
        InputStream is = null;
        InputStreamReader isr;
        BufferedReader br;
        StringWriter sw = new StringWriter();

        java.util.Date startTime, endTime;
        boolean inError;
        int retries = 0;
        int maxRetries = 0;

        do {
            startTime = new java.util.Date();
            try {
                // make url-connection
                sampleUrl = new URL(page);
                urlConn = (HttpsURLConnection) (sampleUrl.openConnection());

                if (propertyTable != null) {
                    setRequestProperties(urlConn, propertyTable);
                }

                if (postMessage != null) {
                    urlConn.setDoOutput(true);

                    os = urlConn.getOutputStream();
                    os.write(postMessage.getBytes());
                    os.flush();
                    os.close();
                }

                //   print_https_cert(urlConn);
                //            int iHeaderNumber;
                //           String sHeaderName;
                //           String sHeaderValue;
                //                is = urlConn.getInputStream();
                ////////
                int i = 0;
                while (urlConn.getHeaderFieldKey(i) == null) {
                    i++;
                }

                String key = null, headerfield = null, cookie = null;
                while ((key = urlConn.getHeaderFieldKey(i)) != null) {
                    if (key.equalsIgnoreCase("set-cookie")) {
                        returnCookie.write(urlConn.getHeaderField(i));
                    }
                    i++;
                }

                ////////
/*
                 * if (urlConn.getContentLength()!=0) { isr = new
                 * InputStreamReader( is ); br = new BufferedReader( isr );
                 * String line;
                 *
                 * // get response out of connection line = br.readLine(); while
                 * ( line != null ) { sw.write( line ); sw.write( '\n' ); line =
                 * br.readLine(); } }
                 *
                 */
                // close inputstream if any open
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ioe) {
                }

                // close urlconnection if any open
                if (urlConn != null) {
                    urlConn.disconnect();
                }

                inError = false;
            } catch (Exception e) {
                //System.out.println("GRABBER FAILED for: "+sURL);
                //e.printStackTrace();

                endTime = new java.util.Date();

                // close inputstream if any open
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ioe) {
                }

                // close urlconnection if any open
                if (urlConn != null) {
                    urlConn.disconnect();
                }

                long elapsedTime = endTime.getTime() - startTime.getTime();
                if (elapsedTime < 50l) { // threshold is 50 ms (l stands for long)
                    // POSSIBLE NETWORK FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 3;
//                        sleepyT.setSleepTime( 500 );
                        //                       sleepyT.setSleepTimeExtensionFactor( 2 );
                        // 0 -> 500ms -> 1000ms -> 2000ms
                    }

                    System.out.println("POSSIBLE NETWORK FAILURE during attempt #"
                            + retries + "(" + elapsedTime + "): " + e.getMessage());
                } else {
                    // POSSIBLE REMOTE SERVER FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 1;
//                        sleepyT.setSleepTime( 1000 );
//                        sleepyT.setSleepTimeExtensionFactor( 2 );
                        // 0 -> 1000ms
                    }

                    System.out.println("-POSSIBLE REMOTE SERVER FAILURE"
                            + " during attempt #" + retries + "(" + elapsedTime + "): " + e.getMessage());
                    e.printStackTrace();
                }

                if (retries < maxRetries) {
                    startTime = new java.util.Date();
//                    sleepyT.run(); // sleep for a while before retrying
                    endTime = new java.util.Date();
                    elapsedTime = endTime.getTime() - startTime.getTime();
                    //System.out.println( "slept for " + ( endTime.getTime() - startTime.getTime() ) + " ms" );
                }

                retries++;

                inError = true;
            } // catch
        } // do
        while (inError && retries <= maxRetries);

        // Return the content of the webpage as a string
        return sw.toString();
    }

    public Hashtable makeHeaders(String rString) {
        Hashtable headers = new Hashtable();
        try {
            StringReader sr = new StringReader(rString);
            BufferedReader br = new BufferedReader(sr);
            String aToken = br.readLine();
            while (aToken != null) {
                if (aToken.indexOf(':') > -1) {
                    String header = "header." + aToken.substring(0, aToken.indexOf(':')).trim();
                    String value = aToken.substring(aToken.indexOf(':') + 1).trim();
                    // System.out.println("*** " + header + ":" + value);
                    headers.put(header, value);
                }
                aToken = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return headers;
    }

    public String makeCookie(HashMap hm) {
        String cookie = "";
        Set s = hm.keySet();
        Iterator ite = s.iterator();
        while (ite.hasNext()) {
            String key = (String) ite.next();
            String value = (String) hm.get(key);
            String aToken = key + "=" + value;
            if (cookie.isEmpty()) {
                cookie = aToken;
            } else {
                cookie = cookie + "; " + aToken;
            }
        }
        return cookie;
    }

    public String makeCookie(HashMap hm, String sAllow) {
        String cookie = "";
        Set s = hm.keySet();
        Iterator ite = s.iterator();
        while (ite.hasNext()) {
            String key = (String) ite.next();
            String value = (String) hm.get(key);
            if (sAllow.contains(key)) {
                String aToken = key + "=" + value;
                if (cookie.isEmpty()) {
                    cookie = aToken;
                } else {
                    cookie = cookie + "; " + aToken;
                }
            }
        }
        return cookie;
    }

    public String pageGrab(String page, Hashtable propertyTable) throws Exception {

        OutputStreamWriter osw;
        InputStream is = null;
        InputStreamReader isr;
        BufferedReader br;
        StringWriter sw = new StringWriter();
        SleepyThread sleepyT = new SleepyThread();

        java.util.Date startTime, endTime;
        boolean inError;
        int retries = 0;
        int maxRetries = 0;
        HttpsURLConnection urlConn = null;

        do {
            startTime = new java.util.Date();
            try {
                // make url-connection
                URL url = new URL(page);

                urlConn = (HttpsURLConnection) (url.openConnection());

                if (propertyTable != null) {
                    setRequestProperties(urlConn, propertyTable);
                }

                print_https_cert(urlConn);

                if (urlConn.getContentLength() != 0) {
                    is = urlConn.getInputStream();
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);
                    String line;

                    line = br.readLine();
                    while (line != null) {
                        sw.write(line);
                        sw.write('\n');
                        line = br.readLine();
                    }
                }
                // close inputstream if any open
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ioe) {
                }

                // close urlconnection if any open
                if (urlConn != null) {
                    urlConn.disconnect();
                }

                inError = false;
            } catch (Exception e) {

                //System.out.println("GRABBER FAILED for: "+sURL);
                //e.printStackTrace();
                endTime = new java.util.Date();

                // close inputstream if any open
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ioe) {
                }

                // close urlconnection if any open
                if (urlConn != null) {
                    urlConn.disconnect();
                }

                long elapsedTime = endTime.getTime() - startTime.getTime();
                if (elapsedTime < 5000) { // threshold is 50 ms (l stands for long)
                    // POSSIBLE NETWORK FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 10;
                        sleepyT.setSleepTime(10000);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 500ms -> 1000ms -> 2000ms
                    }
                    if (retries == 3) {
                        System.out.println("ERROR: POSSIBLE NETWORK FAILURE during attempt #"
                                + retries + "(" + elapsedTime + "): " + e.getMessage());
                    }
                } else {
                    // POSSIBLE REMOTE SERVER FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 10;
                        sleepyT.setSleepTime(10000);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 1000ms
                    }
                    if (retries == 3) {
                        System.out.println("ERROR: POSSIBLE SERVER FAILURE during attempt #"
                                + retries + "(" + elapsedTime + "): " + e.getMessage());
                    }
                }

                if (retries < maxRetries) {
                    startTime = new java.util.Date();
                    sleepyT.run(); // sleep for a while before retrying
                    endTime = new java.util.Date();
                    elapsedTime = endTime.getTime() - startTime.getTime();
                    //System.out.println( "slept for " + ( endTime.getTime() - startTime.getTime() ) + " ms" );
                }

                retries++;

                inError = true;
            } // catch
        } // do
        while (inError && retries <= maxRetries);

        String content = sw.toString();
        return content;

    }
    
    public String logInTest(String page, Hashtable propertyTable) throws Exception {

        OutputStreamWriter osw;
        InputStream is = null;
        InputStreamReader isr;
        BufferedReader br;
        StringWriter sw = new StringWriter();
        SleepyThread sleepyT = new SleepyThread();

        java.util.Date startTime, endTime;
        boolean inError;
        int retries = 0;
        int maxRetries = 0;
        HttpsURLConnection urlConn = null;

        do {
            startTime = new java.util.Date();
            try {
                // make url-connection
                URL url = new URL(page);

                urlConn = (HttpsURLConnection) (url.openConnection());

                if (propertyTable != null) {
                    setRequestProperties(urlConn, propertyTable);
                }

                print_https_cert(urlConn);

                if (urlConn.getContentLength() != 0) {
                    is = urlConn.getInputStream();
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);
                    String line;

                    //writes the content of the entire page
                    line = br.readLine();
                    while (line != null) {
                        sw.write(line);
                        sw.write('\n');
                        line = br.readLine();
                    }
                }
                // close inputstream if any open
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ioe) {
                }

                // close urlconnection if any open
                if (urlConn != null) {
                    urlConn.disconnect();
                }

                inError = false;
            } catch (Exception e) {

                //System.out.println("GRABBER FAILED for: "+sURL);
                //e.printStackTrace();
                endTime = new java.util.Date();

                // close inputstream if any open
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ioe) {
                }

                // close urlconnection if any open
                if (urlConn != null) {
                    urlConn.disconnect();
                }

                long elapsedTime = endTime.getTime() - startTime.getTime();
                if (elapsedTime < 5000) { // threshold is 50 ms (l stands for long)
                    // POSSIBLE NETWORK FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 10;
                        sleepyT.setSleepTime(10000);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 500ms -> 1000ms -> 2000ms
                    }
                    if (retries == 3) {
                        System.out.println("ERROR: POSSIBLE NETWORK FAILURE during attempt #"
                                + retries + "(" + elapsedTime + "): " + e.getMessage());
                    }
                } else {
                    // POSSIBLE REMOTE SERVER FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 10;
                        sleepyT.setSleepTime(10000);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 1000ms
                    }
                    if (retries == 3) {
                        System.out.println("ERROR: POSSIBLE SERVER FAILURE during attempt #"
                                + retries + "(" + elapsedTime + "): " + e.getMessage());
                    }
                }

                if (retries < maxRetries) {
                    startTime = new java.util.Date();
                    sleepyT.run(); // sleep for a while before retrying
                    endTime = new java.util.Date();
                    elapsedTime = endTime.getTime() - startTime.getTime();
                    //System.out.println( "slept for " + ( endTime.getTime() - startTime.getTime() ) + " ms" );
                }

                retries++;

                inError = true;
            } // catch
        } // do
        while (inError && retries <= maxRetries);

        String content = sw.toString();
        return content;

    }

    public String postGrab(String page, String postMessage, Hashtable propertyTable) throws Exception {
        URL sampleUrl;
        HttpsURLConnection urlConn = null;
        OutputStream os;
        OutputStreamWriter osw;
        InputStream is = null;
        InputStreamReader isr;
        BufferedReader br;
        StringWriter sw = new StringWriter();

        java.util.Date startTime, endTime;
        boolean inError;
        int retries = 0;
        int maxRetries = 0;

   //     System.out.println("*** url: "+page);
   //     trustAllHttpsCertificates();
        redirectURL = "";
        SleepyThread sleepyT = new SleepyThread();

        TheResponseCode = 0;
        do {
            startTime = new java.util.Date();
            try {
                // make url-connection
                sampleUrl = new URL(page);
                urlConn = (HttpsURLConnection) (sampleUrl.openConnection());
                // java screws up the automatic redirection
                urlConn.setInstanceFollowRedirects(false);

                if (propertyTable != null) {
                    setRequestProperties(urlConn, propertyTable);
                }

                if (postMessage != null) {
                    urlConn.setDoOutput(true);

                    os = urlConn.getOutputStream();
                    os.write(postMessage.getBytes());
                    os.flush();
                    os.close();
                }

                try {
                    //print_https_cert(urlConn);

                    if (true) {

                        TheResponseCode = urlConn.getResponseCode();;
                        if (urlConn.getResponseCode() == 302 || (urlConn.getResponseCode() >= 300 && urlConn.getResponseCode() < 400)) {

                            redirectURL = urlConn.getHeaderField("location");

                        }

                        int i = 0;
                        while (urlConn.getHeaderFieldKey(i) == null) {
                            i++;
                        }

                        String key = null, headerfield = null, cookie = null;
                        int iSubCnt = 0;
                        while ((key = urlConn.getHeaderFieldKey(i)) != null) {
                            //  System.out.println("key: "+key+" = "+urlConn.getHeaderField(i));
                            if (key.equalsIgnoreCase("set-cookie")) {
                                String myKey = urlConn.getHeaderField(i);
                                myKey = myKey.substring(0, myKey.indexOf(';'));
                                String value = myKey.substring(myKey.indexOf('=') + 1);
                                myKey = myKey.substring(0, myKey.indexOf('='));
                                hmCookie.put(myKey, value);

                            }
                            i++;
                        }

                        // get response out of connection
                        if (TheResponseCode >= 200 && TheResponseCode < 300) {
                            is = urlConn.getInputStream();
                            if ("gzip".equals(urlConn.getContentEncoding())) {
                                is = new GZIPInputStream(is);
                            }
                            isr = new InputStreamReader(is);
                            br = new BufferedReader(isr);
                            String line;
                            line = br.readLine();
                            while (line != null) {
                                sw.write(line);
                                sw.write('\n');
                                line = br.readLine();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    TheResponseCode = -3;
                }
                // close inputstream if any open
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ioe) {
                }

                // close urlconnection if any open
                if (urlConn != null) {
                    urlConn.disconnect();
                }

                inError = false;
            } catch (Exception e) {

                //System.out.println("GRABBER FAILED.");
                //e.printStackTrace();
                endTime = new java.util.Date();

                // close inputstream if any open
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ioe) {
                }

                // close urlconnection if any open
                if (urlConn != null) {
                    urlConn.disconnect();
                }

                long elapsedTime = endTime.getTime() - startTime.getTime();
                if (elapsedTime < 5000) { // threshold is 50 ms (l stands for long)
                    // POSSIBLE NETWORK FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 10;
                        sleepyT.setSleepTime(10000);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 500ms -> 1000ms -> 2000ms
                    }
                    if (retries == 3) {
                        System.out.println("ERROR: POSSIBLE NETWORK FAILURE during attempt #"
                                + retries + "(" + elapsedTime + "): " + e.getMessage());
                    }
                } else {
                    // POSSIBLE REMOTE SERVER FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 10;
                        sleepyT.setSleepTime(10000);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 1000ms
                    }
                    if (retries == 3) {
                        System.out.println("ERROR: POSSIBLE NETWORK FAILURE during attempt #"
                                + retries + "(" + elapsedTime + "): " + e.getMessage());
                    }
                }

                if (retries < maxRetries) {
                    startTime = new java.util.Date();
                    sleepyT.run(); // sleep for a while before retrying
                    endTime = new java.util.Date();
                    elapsedTime = endTime.getTime() - startTime.getTime();
                    //System.out.println( "slept for " + ( endTime.getTime() - startTime.getTime() ) + " ms" );
                }

                retries++;

                inError = true;
            } // catch
        } // do
        while (inError && retries <= maxRetries);

        String content = sw.toString();

        return content;
    }

    private String cleanUpCookie(String rString) {
        if (rString.indexOf(";") > -1) {
            return rString.substring(0, rString.indexOf(';'));
        } else {
            return rString;
        }
    }

    public void postGrabImage(BufferedImage image, String page, String postMessage, Hashtable propertyTable) throws Exception {
        URL sampleUrl;
        HttpsURLConnection urlConn = null;
        OutputStream os;
        OutputStreamWriter osw;

        java.util.Date startTime, endTime;
        boolean inError;
        int retries = 0;
        int maxRetries = 0;

        String redirectURL = "";
        SleepyThread sleepyT = new SleepyThread();

        do {
            startTime = new java.util.Date();
            try {
                // make url-connection
                sampleUrl = new URL(page);
                urlConn = (HttpsURLConnection) (sampleUrl.openConnection());
                // java screws up the automatic redirection

                if (propertyTable != null) {
                    setRequestProperties(urlConn, propertyTable);
                }

                if (postMessage != null) {
                    urlConn.setDoOutput(true);

                    os = urlConn.getOutputStream();
                    os.write(postMessage.getBytes());
                    os.flush();
                    os.close();
                }

                if (true) {
                    InputStream is = urlConn.getInputStream();
                    FileOutputStream fos = new FileOutputStream("c:/FRANSISTHEMAN.jpg");
                    int c = 0;
                    int iLength = 0;
                    c = is.read();
                    System.out.println("*** reading all in..." + urlConn.getContentLength());
                    while (c != -1 && iLength < urlConn.getContentLength()) {
                        fos.write(c);
                        iLength++;
                        c = is.read();
                    }
                    System.out.println("*** done reading all in..." + iLength);
                    fos.close();
                }

                // close urlconnection if any open
                if (urlConn != null) {
                    urlConn.disconnect();
                }

                inError = false;
            } catch (Exception e) {

                //System.out.println("GRABBER FAILED.");
                //e.printStackTrace();
                endTime = new java.util.Date();

                // close urlconnection if any open
                if (urlConn != null) {
                    urlConn.disconnect();
                }

                long elapsedTime = endTime.getTime() - startTime.getTime();
                if (elapsedTime < 5000) { // threshold is 50 ms (l stands for long)
                    // POSSIBLE NETWORK FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 10;
                        sleepyT.setSleepTime(10000);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 500ms -> 1000ms -> 2000ms
                    }
                    if (retries == 3) {
                        System.out.println("ERROR: POSSIBLE NETWORK FAILURE during attempt #"
                                + retries + "(" + elapsedTime + "): " + e.getMessage());
                    }
                } else {
                    // POSSIBLE REMOTE SERVER FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 10;
                        sleepyT.setSleepTime(10000);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 1000ms
                    }
                    if (retries == 3) {
                        System.out.println("ERROR: POSSIBLE NETWORK FAILURE during attempt #"
                                + retries + "(" + elapsedTime + "): " + e.getMessage());
                    }
                }

                if (retries < maxRetries) {
                    startTime = new java.util.Date();
                    sleepyT.run(); // sleep for a while before retrying
                    endTime = new java.util.Date();
                    elapsedTime = endTime.getTime() - startTime.getTime();
                    //System.out.println( "slept for " + ( endTime.getTime() - startTime.getTime() ) + " ms" );
                }

                retries++;

                inError = true;
            } // catch
        } // do
        while (inError && retries <= maxRetries);
    }

    public String getCookie(String page, Hashtable propertyTable) {

        URL sampleUrl;
        HttpsURLConnection urlConn = null;
        StringWriter returnCookie = new StringWriter();

        java.util.Date startTime, endTime;
        boolean inError;
        int retries = 0;
        int maxRetries = 0;
        SleepyThread sleepyT = new SleepyThread();
        InputStream is = null;

        do {
            startTime = new java.util.Date();
            try {

                // make url-connection
                sampleUrl = new URL(page);
                urlConn = (HttpsURLConnection) (sampleUrl.openConnection());

                int i = 0;
                while (urlConn.getHeaderFieldKey(i) == null) {
                    i++;
                }

                String key = null, headerfield = null, cookie = null;
                while ((key = urlConn.getHeaderFieldKey(i)) != null) {
                    if (key.equalsIgnoreCase("set-cookie")) {
                        returnCookie.write(urlConn.getHeaderField(i));
                    }
                    i++;
                }

                // close inputstream if any open
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ioe) {
                }

                // close urlconnection if any open
                if (urlConn != null) {
                    urlConn.disconnect();
                }

                inError = false;
            } catch (Exception e) {

                endTime = new java.util.Date();

                // close inputstream if any open
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ioe) {
                }

                // close urlconnection if any open
                if (urlConn != null) {
                    urlConn.disconnect();
                }

                long elapsedTime = endTime.getTime() - startTime.getTime();
                if (elapsedTime < 5000) { // threshold is 50 ms (l stands for long)
                    // POSSIBLE NETWORK FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 10;
                        sleepyT.setSleepTime(10000);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 500ms -> 1000ms -> 2000ms
                    }

                    System.out.println("POSSIBLE NETWORK FAILURE during attempt #"
                            + retries + "(" + elapsedTime + "): " + e.getMessage());
                } else {
                    // POSSIBLE REMOTE SERVER FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 10;
                        sleepyT.setSleepTime(10000);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 1000ms
                    }

                    System.out.println("-POSSIBLE REMOTE SERVER FAILURE"
                            + " during attempt #" + retries + "(" + elapsedTime + "): " + e.getMessage());
                    e.printStackTrace();
                }

                if (retries < maxRetries) {
                    startTime = new java.util.Date();
                    sleepyT.run(); // sleep for a while before retrying
                    endTime = new java.util.Date();
                    elapsedTime = endTime.getTime() - startTime.getTime();
                }

                retries++;

                inError = true;
            } // catch
        } // do
        while (inError && retries <= maxRetries);

        // Return the content of the webpage as a string
        return returnCookie.toString();
    }

    public void trustAllHttpsCertificates() throws Exception {
        // log.info("reached trustAllHttpsCertificates");
        System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol"); //add https protocol handler
        java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider()); //dynamic registration of SunJSSE provider

        //Create a trust manager that does not validate certificate chains:
        com.sun.net.ssl.TrustManager[] trustAllCerts = new com.sun.net.ssl.TrustManager[]{
            new com.sun.net.ssl.X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
                    return true;
                }

                public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
                    return true;
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) throws javax.security.cert.CertificateException {
                    return;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) throws javax.security.cert.CertificateException {
                    return;
                }
            }
        };

        //Install the all-trusting trust manager:
        com.sun.net.ssl.SSLContext sc = com.sun.net.ssl.SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        com.sun.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    }
}
