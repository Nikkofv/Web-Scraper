/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.vogel.common.util;

import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.image.*;
import java.util.zip.GZIPInputStream;
import javax.imageio.*;
import javax.xml.transform.Source;

/**
 *
 * @author Ronny Chan
 * @version
 */
public class WebpageGrabber extends java.lang.Object {

    /**
     * Creates new WebpageGrabber
     */
    public WebpageGrabber() {
        sleepyT = new SleepyThread();
    }

    private static final String HEADERPREFIX = "header.";
    private static Hashtable props = null;

    private SleepyThread sleepyT = null;
    private FileOutputStream fos = null;
    public int TheResponseCode = 0;
    public String redirectURL = "";
    public HashMap hmCookie = new HashMap();

    protected Hashtable getStandardRequestProperties() {
        Hashtable headers = new Hashtable();
        headers.put("header.Content-Type", "application/x-www-form-urlencoded");
        headers.put("header.Pragma", "no-cache");
        return headers;
    }

    /**
     * Set request properties.
     *
     * @param urlConn URLConnection to set the standard request properties for.
     * @param propertyTable Hashtable with the properties to set the header with
     */
    protected void setRequestProperties(URLConnection urlConn, Hashtable propertyTable) {
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
                //               print( RequestPropertyName + " -> " + RequestPropertyValue );
                urlConn.setRequestProperty(RequestPropertyName,
                        RequestPropertyValue);
            }
        }
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

    public Hashtable makeHeaders(String rString, String delim) {
        Hashtable headers = new Hashtable();
        try {
            StringReader sr = new StringReader(rString);
            BufferedReader br = new BufferedReader(sr);
            String aToken = br.readLine();
            while (aToken != null) {
                if (aToken.indexOf(delim) > -1) {
                    String header = "header." + aToken.substring(0, aToken.indexOf(delim)).trim();
                    String value = aToken.substring(aToken.indexOf(delim) + delim.length()).trim();
                    //    System.out.println("*** " + header + ":" + value);
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

    /**
     * Grabs a page with the use of a referer.
     *
     * @param page Webpage to grab the content from
     * @param referer The referer to supply for grabbing the page
     */
    public String grab(String page, String referer) {
        Hashtable headers = new Hashtable();
        headers.put("header.Referer", referer);
        headers.put("header.Content-Type", "application/x-www-form-urlencoded");
        headers.put("header.Pragma", "no-cache");

        return postGrab(page, headers, null);
    }

    /**
     * Grabs a page with the use of a referer.
     *
     * @param page Webpage to grab the content from
     * @param propertyTable Hashtable with the properties to set the header with
     */
    public String grab(String page, Hashtable propertyTable) {
        return postGrab(page, propertyTable, null);
    }

    public void grabPDF(String page, String path) {
        URL sampleUrl;
        HttpURLConnection urlConn = null;
        InputStream is = null;
        try {
            // make url-connection
            sampleUrl = new URL(page);
            urlConn = (HttpURLConnection) (sampleUrl.openConnection());

            // file not found response
            if (urlConn.getResponseCode() == 404) {
                System.out.println("**** 404 **** ");
            }

            if (urlConn.getContentLength() != 0) {
                is = urlConn.getInputStream();
                //               ImageIO.read(urlConn.getInputStream());
                System.out.println("reading....");
                File f1 = new File(path.substring(0, path.lastIndexOf("/") + 1));
                f1.mkdirs();
                FileOutputStream fos = new FileOutputStream(path);
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

                //  is.close();
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
    }

    public BufferedImage grabImage(String page, Hashtable propertyTable, String postMessage) {
        URL sampleUrl;
        HttpURLConnection urlConn = null;
        InputStream is = null;
        OutputStream os;
        BufferedImage image = null;
        try {
            // make url-connection
            sampleUrl = new URL(page);
            urlConn = (HttpURLConnection) (sampleUrl.openConnection());

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
                     System.out.println("reading....");
                    
                     FileOutputStream fos=new FileOutputStream(path);
                     int c=0;
                     int iLength=0;
                     c=is.read();
                     System.out.println("*** reading all in..."+urlConn.getContentLength());
                     while(c != -1 && iLength<urlConn.getContentLength()) {
                     fos.write(c);
                     iLength++;
                     c=is.read();
                     }
                     System.out.println("*** done reading all in..."+iLength);
                     fos.close();
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

    /**
     * Grabs a page.
     *
     * @param page Webpage to grab the content from
     */
    public String grabHP(String sURL) {

        URL sampleUrl;
        HttpURLConnection urlConn = null;
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
                sampleUrl = new URL(sURL);
                urlConn = (HttpURLConnection) (sampleUrl.openConnection());

                if (urlConn.getContentLength() != 0) {
                    is = urlConn.getInputStream();
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);
                    String line;

                    if (urlConn.getResponseCode() == 404) {
                        return "Page not found";
                    }
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
                if (elapsedTime < 50l) { // threshold is 50 ms (l stands for long)
                    // POSSIBLE NETWORK FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 3;
                        sleepyT.setSleepTime(500);
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
                        maxRetries = 3;
                        sleepyT.setSleepTime(1000);
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

    public String getRedirectionURL() {
        return redirectURL;
    }

    public String getMyCookie() {
        return myCookie;
    }

    /**
     * Grabs a page.
     *
     * @param page Webpage to grab the content from
     * @param propertyTable Hashtable with the properties to set the header with
     * @param postMessage The message to post before getting the page
     */
    public String postGrab(String page, Hashtable propertyTable, String postMessage) {

        URL sampleUrl;
        HttpURLConnection urlConn = null;
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

        redirectURL = "";
        myCookie = "";
        TheResponseCode = 0;
        do {
            startTime = new java.util.Date();
            try {
                // make url-connection
                sampleUrl = new URL(page);
                urlConn = (HttpURLConnection) (sampleUrl.openConnection());
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

                //    if (urlConn.getContentLength()!=0) {
                try {
                    TheResponseCode = urlConn.getResponseCode();

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
                            if (myKey.indexOf(';') > -1 && myKey.indexOf('=') > -1) {
                                myKey = myKey.substring(0, myKey.indexOf(';'));
                                String value = myKey.substring(myKey.indexOf('=') + 1);
                                myKey = myKey.substring(0, myKey.indexOf('='));
                                hmCookie.put(myKey, value);
                            }
                        }
                        i++;
                    }

                    Map m = urlConn.getHeaderFields();
                    Set s = m.keySet();
                    Iterator ite = s.iterator();
                    myCookie = "";
                    while (ite.hasNext()) {
                        String aKey = (String) ite.next();
                        if (aKey != null) {
                            if (aKey.toLowerCase().equals("set-cookie")) {
                                List el = (List) m.get(aKey);
                                Iterator iter = el.iterator();
                                while (iter.hasNext()) {
                                    String aToken = (String) iter.next();
                                    if (aToken.indexOf(";") > -1) {
                                        aToken = aToken.substring(0, aToken.indexOf(';') + 1);
                                    }
                                    if (myCookie.isEmpty()) {
                                        myCookie = aToken;
                                    } else {
                                        myCookie = myCookie + " " + aToken;
                                    }
                                }
                            }
                        }
                        //  System.out.println("*** key: " + aKey + " has: " + m.get(aKey));
                    }

                    if (TheResponseCode >= 200 && TheResponseCode < 300) {
                        is = urlConn.getInputStream();
                        if ("gzip".equals(urlConn.getContentEncoding())) {
                            is = new GZIPInputStream(is);
                        }

                        isr = new InputStreamReader(is);
                        br = new BufferedReader(isr);
                        String line;
                        // get response out of connection
                        line = br.readLine();
                        while (line != null) {
                            sw.write(line);
                            sw.write('\n');
                            line = br.readLine();
                        }
                    }
                } catch (java.net.UnknownHostException e) {
                    TheResponseCode = -1;
                } catch (java.net.ConnectException e) {
                    TheResponseCode = -2;
                } catch (java.net.SocketException e) {
                    TheResponseCode = -4;
                } catch (java.net.SocketTimeoutException e) {
                    TheResponseCode = -5;
                } catch (java.net.MalformedURLException e) {
                    TheResponseCode = -6;
                } catch (java.lang.IllegalArgumentException e) {
                    TheResponseCode = -6;
                }
              //  } else {
                //      System.out.println("**** failed....");
                //  }

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
            } catch (java.net.MalformedURLException e) {
                TheResponseCode = -6;
                inError = false; // jump out.
            } catch (Exception e) {

                System.out.println("GRABBER FAILED FOR: " + page);
                e.printStackTrace();
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
                        sleepyT.setSleepTime(500);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 500ms -> 1000ms -> 2000ms
                    }
                    if (retries == 3) {
                        System.out.println("ERROR: POSSIBLE NETWORK FAILURE during attempt #"
                                + retries + "(" + elapsedTime + "): " + e.getMessage());
                        if (e.getMessage().contains("no protocol")) {
                            System.exit(0);
                        }
                    }
                } else {
                    // POSSIBLE REMOTE SERVER FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 1;
                        sleepyT.setSleepTime(1000);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 1000ms
                    }
                    if (retries == 3) {
                        System.out.println("ERROR: POSSIBLE NETWORK FAILURE during attempt #"
                                + retries + "(" + elapsedTime + "): " + e.getMessage());
                        if (e.getMessage().contains("no protocol")) {
                            System.exit(0);
                        }
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

    public net.htmlparser.jericho.Source postGrabSource(String page, Hashtable propertyTable, String postMessage) {

        URL sampleUrl;
        HttpURLConnection urlConn = null;
        OutputStream os;
        OutputStreamWriter osw;
        InputStream is = null;
        net.htmlparser.jericho.Source source = null;

        java.util.Date startTime, endTime;
        boolean inError;
        int retries = 0;
        int maxRetries = 0;

        redirectURL = "";
        myCookie = "";
        TheResponseCode = 0;
        do {
            startTime = new java.util.Date();
            try {
                // make url-connection
                sampleUrl = new URL(page);
                urlConn = (HttpURLConnection) (sampleUrl.openConnection());
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

                //    if (urlConn.getContentLength()!=0) {
                try {
                    TheResponseCode = urlConn.getResponseCode();

                    if (urlConn.getResponseCode() == 302 || (urlConn.getResponseCode() >= 300 && urlConn.getResponseCode() < 400)) {

                        redirectURL = urlConn.getHeaderField("location");
                        if (redirectURL != null) {
                            System.out.println("************************************************* Redirection to " + redirectURL + " must occur manually!");
                        } else {
                            System.out.println("**** NO REDIRECT");
                        }
                    }

                    myCookie = urlConn.getHeaderField("set-cookie");

                    if (TheResponseCode >= 200 && TheResponseCode < 300) {
                        is = urlConn.getInputStream();
                        if ("gzip".equals(urlConn.getContentEncoding())) {
                            is = new GZIPInputStream(is);
                        }

                        source = new net.htmlparser.jericho.Source(is);
                        source.setLogger(null);
                        source.fullSequentialParse();
                    }
                } catch (java.net.UnknownHostException e) {
                    TheResponseCode = -1;
                } catch (java.net.ConnectException e) {
                    TheResponseCode = -2;
                } catch (java.net.SocketException e) {
                    TheResponseCode = -4;
                } catch (java.net.SocketTimeoutException e) {
                    TheResponseCode = -5;
                } catch (java.net.MalformedURLException e) {
                    TheResponseCode = -6;
                } catch (java.lang.IllegalArgumentException e) {
                    TheResponseCode = -6;
                }
              //  } else {
                //      System.out.println("**** failed....");
                //  }

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
            } catch (java.net.MalformedURLException e) {
                TheResponseCode = -6;
                inError = false; // jump out.
            } catch (Exception e) {

                System.out.println("GRABBER FAILED FOR: " + page);
                e.printStackTrace();
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
                        sleepyT.setSleepTime(500);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 500ms -> 1000ms -> 2000ms
                    }
                    if (retries == 3) {
                        System.out.println("ERROR: POSSIBLE NETWORK FAILURE during attempt #"
                                + retries + "(" + elapsedTime + "): " + e.getMessage());
                        if (e.getMessage().contains("no protocol")) {
                            System.exit(0);
                        }
                    }
                } else {
                    // POSSIBLE REMOTE SERVER FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 1;
                        sleepyT.setSleepTime(1000);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 1000ms
                    }
                    if (retries == 3) {
                        System.out.println("ERROR: POSSIBLE NETWORK FAILURE during attempt #"
                                + retries + "(" + elapsedTime + "): " + e.getMessage());
                        if (e.getMessage().contains("no protocol")) {
                            System.exit(0);
                        }
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

        return source;
    }

    public String postGrabCHUNKED(String page, Hashtable propertyTable, String postMessage) {

        URL sampleUrl;
        HttpURLConnection urlConn = null;
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

        redirectURL = "";
        myCookie = "";

        do {

            startTime = new java.util.Date();
            try {
                // make url-connection
                sampleUrl = new URL(page);
                urlConn = (HttpURLConnection) (sampleUrl.openConnection());
                // java screws up the automatic redirection
                // urlConn.setInstanceFollowRedirects(false);

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

                if (urlConn.getContentLength() != 0) {
                    is = urlConn.getInputStream();
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);
                    String line;

                    if (urlConn.getResponseCode() == 302) {

                        redirectURL = urlConn.getHeaderField("location");
                        if (redirectURL != null) {
                            System.out.println("************************************************* Redirection to " + redirectURL + " must occur manually!");
                        }
                    } else {
                        // System.out.println("********************* RESPONSE CODE IS: "+urlConn.getResponseCode());
                        // redirectURL = urlConn.getHeaderField("location");
                        // System.out.println("************************************************* Redirection to " + redirectURL + " must occur manually!");
                    }

                    myCookie = urlConn.getHeaderField("set-cookie");

                    // get response out of connection
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

                //  System.out.println("GRABBER FAILED.");
                //  e.printStackTrace();
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
                        sleepyT.setSleepTime(500);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 500ms -> 1000ms -> 2000ms
                    }
                    if (retries == 3) {
                        System.out.println("ERROR: POSSIBLE NETWORK FAILURE during attempt #"
                                + retries + "(" + elapsedTime + "): " + e.getMessage());
                        if (e.getMessage().contains("no protocol")) {
                            System.exit(0);
                        }
                    }
                } else {
                    // POSSIBLE REMOTE SERVER FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 1;
                        sleepyT.setSleepTime(1000);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 1000ms
                    }
                    if (retries == 3) {
                        System.out.println("ERROR: POSSIBLE NETWORK FAILURE during attempt #"
                                + retries + "(" + elapsedTime + "): " + e.getMessage());
                        if (e.getMessage().contains("no protocol")) {
                            System.exit(0);
                        }
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

    public String postGrabWithAutoRedirect(String page, Hashtable propertyTable, String postMessage, StringWriter returnCookie) {

        URL sampleUrl;
        HttpURLConnection urlConn = null;
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

        redirectURL = "";
        myCookie = "";

        do {
            startTime = new java.util.Date();
            try {
                // make url-connection
                sampleUrl = new URL(page);
                urlConn = (HttpURLConnection) (sampleUrl.openConnection());
                // java screws up the automatic redirection
                // urlConn.setInstanceFollowRedirects(false);

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

                if (urlConn.getContentLength() != 0) {
                    is = urlConn.getInputStream();
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);
                    String line;

                    if (urlConn.getResponseCode() == 302) {

                        redirectURL = urlConn.getHeaderField("location");
                        if (redirectURL != null) {
                            System.out.println("************************************************* Redirection to " + redirectURL + " must occur manually!");
                        }
                    } else {
                        // System.out.println("********************* RESPONSE CODE IS: "+urlConn.getResponseCode());
                        // redirectURL = urlConn.getHeaderField("location");
                        // System.out.println("************************************************* Redirection to " + redirectURL + " must occur manually!");
                    }

                    myCookie = urlConn.getHeaderField("set-cookie");

                    // get response out of connection
                    line = br.readLine();
                    while (line != null) {
                        sw.write(line);
                        sw.write('\n');
                        line = br.readLine();
                    }
                } else {
                    System.out.println("***** CONTENT LENGTH IS ZERO.........");
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
                if (elapsedTime < 50l) { // threshold is 50 ms (l stands for long)
                    // POSSIBLE NETWORK FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 3;
                        sleepyT.setSleepTime(500);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 500ms -> 1000ms -> 2000ms
                    }
                    if (retries == 3) {
                        System.out.println("ERROR: POSSIBLE NETWORK FAILURE during attempt #"
                                + retries + "(" + elapsedTime + "): " + e.getMessage());
                        if (e.getMessage().contains("no protocol")) {
                            System.exit(0);
                        }
                    }
                } else {
                    // POSSIBLE REMOTE SERVER FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 1;
                        sleepyT.setSleepTime(1000);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 1000ms
                    }
                    if (retries == 3) {
                        System.out.println("ERROR: POSSIBLE NETWORK FAILURE during attempt #"
                                + retries + "(" + elapsedTime + "): " + e.getMessage());
                        if (e.getMessage().contains("no protocol")) {
                            System.exit(0);
                        }
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

    // only implemented for funda
    /**
     * Grabs a page.
     *
     * @param page Webpage to grab the content from
     * @param propertyTable Hashtable with the properties to set the header with
     * @param postMessage The message to post before getting the page
     */
    public String postGrabWithRedirectFunda(String page, Hashtable propertyTable, String postMessage) {

        //        System.out.println( "Going to: " + page );
        URL sampleUrl;
        HttpURLConnection urlConn = null;
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

        redirectURL = "";

        do {
            startTime = new java.util.Date();
            try {
                // make url-connection
                sampleUrl = new URL(page);
                urlConn = (HttpURLConnection) (sampleUrl.openConnection());
                // java screws up the automatic redirection
                urlConn.setInstanceFollowRedirects(false);

                if (propertyTable != null) {
                    setRequestProperties(urlConn, propertyTable);
                    setRequestProperties(urlConn, getStandardRequestProperties());
                }

                if (postMessage != null) {
                    urlConn.setDoOutput(true);

                    os = urlConn.getOutputStream();
                    os.write(postMessage.getBytes());
                    os.flush();
                    os.close();
                }

                if (urlConn.getContentLength() != 0) {
                    is = urlConn.getInputStream();
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);
                    String line;

                    if (urlConn.getResponseCode() == 302) {

                        redirectURL = urlConn.getHeaderField("location");
                        if (redirectURL != null) {
                            Hashtable pTable = getStandardRequestProperties();
                            pTable.put(HEADERPREFIX + "Referer", page);

                            //       System.out.println("************************************************* Redirection to " + redirectURL + " must occur manually!");
                            return postGrabWithRedirect("http://www.funda.nl" + redirectURL, pTable, null);
                        }
                    }

                    // get response out of connection
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
                if (elapsedTime < 50l) { // threshold is 50 ms (l stands for long)
                    // POSSIBLE NETWORK FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 3;
                        sleepyT.setSleepTime(500);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 500ms -> 1000ms -> 2000ms
                    }

                    System.out.println("POSSIBLE NETWORK FAILURE during attempt #"
                            + retries + "(" + elapsedTime + "): " + e.getMessage());
                } else {
                    // POSSIBLE REMOTE SERVER FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 1;
                        sleepyT.setSleepTime(1000);
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

    /**
     * Grabs a page.
     *
     * @param page Webpage to grab the content from
     * @param propertyTable Hashtable with the properties to set the header with
     * @param postMessage The message to post before getting the page
     */
    public String postGrabWithRedirect(String page, Hashtable propertyTable, String postMessage) {

        //        System.out.println( "Going to: " + page );
        URL sampleUrl;
        HttpURLConnection urlConn = null;
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

        redirectURL = "";

        do {
            startTime = new java.util.Date();
            try {
                // make url-connection
                sampleUrl = new URL(page);
                urlConn = (HttpURLConnection) (sampleUrl.openConnection());
                // java screws up the automatic redirection
                urlConn.setInstanceFollowRedirects(false);

                if (propertyTable != null) {
                    setRequestProperties(urlConn, propertyTable);
                    setRequestProperties(urlConn, getStandardRequestProperties());
                }

                if (postMessage != null) {
                    urlConn.setDoOutput(true);

                    os = urlConn.getOutputStream();
                    os.write(postMessage.getBytes());
                    os.flush();
                    os.close();
                }

                if (urlConn.getContentLength() != 0) {
                    is = urlConn.getInputStream();
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);
                    String line;

                    if (urlConn.getResponseCode() == 302) {

                        redirectURL = urlConn.getHeaderField("location");
                        if (redirectURL != null) {
                            System.out.println("************************************************* Redirection to " + redirectURL + " must occur manually!");
                        }
                    }

                    // get response out of connection
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
                if (elapsedTime < 50l) { // threshold is 50 ms (l stands for long)
                    // POSSIBLE NETWORK FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 3;
                        sleepyT.setSleepTime(500);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 500ms -> 1000ms -> 2000ms
                    }

                    System.out.println("POSSIBLE NETWORK FAILURE during attempt #"
                            + retries + "(" + elapsedTime + "): " + e.getMessage());
                } else {
                    // POSSIBLE REMOTE SERVER FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 1;
                        sleepyT.setSleepTime(1000);
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

    /**
     * Grabs a page.
     *
     * @param page Webpage to grab the content from
     * @param propertyTable Hashtable with the properties to set the header with
     * @param postMessage The message to post before getting the page
     */
    public String postGrabCookie(String page, Hashtable propertyTable, String postMessage, StringWriter returnCookie) {

        URL sampleUrl;
        HttpURLConnection urlConn = null;
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
                urlConn = (HttpURLConnection) (sampleUrl.openConnection());

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

                //            int iHeaderNumber;
                //           String sHeaderName;
                //           String sHeaderValue;
                //                is = urlConn.getInputStream();
                ////////
                TheResponseCode = urlConn.getResponseCode();
                int i = 0;
                while (urlConn.getHeaderFieldKey(i) == null) {
                    i++;
                }

                String key = null, headerfield = null, cookie = null;
                int iSubCnt = 0;
                while ((key = urlConn.getHeaderFieldKey(i)) != null) {
                    //    System.out.println("key: "+key+" = "+urlConn.getHeaderField(i));
                    if (key.equalsIgnoreCase("set-cookie")) {

                        if (iSubCnt == 0) {
                            returnCookie.write(cleanUpCookie(urlConn.getHeaderField(i)));
                            iSubCnt++;
                        } else {
                            returnCookie.write("; " + cleanUpCookie(urlConn.getHeaderField(i)));
                        }

                    }
                    i++;
                }

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
                if (elapsedTime < 50l) { // threshold is 50 ms (l stands for long)
                    // POSSIBLE NETWORK FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 3;
                        sleepyT.setSleepTime(500);
                        sleepyT.setSleepTimeExtensionFactor(2);
                        // 0 -> 500ms -> 1000ms -> 2000ms
                    }

                    System.out.println("POSSIBLE NETWORK FAILURE during attempt #"
                            + retries + "(" + elapsedTime + "): " + e.getMessage());
                } else {
                    // POSSIBLE REMOTE SERVER FAILURE
                    if (retries == 0) {
                        // initialize maxAttempt and sleepyT
                        maxRetries = 1;
                        sleepyT.setSleepTime(1000);
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

    private String cleanUpCookie(String rString) {
        if (rString.indexOf(";") > -1) {
            return rString.substring(0, rString.indexOf(';'));
        } else {
            return rString;
        }
    }

    /**
     * Grabs a page.
     *
     * @param page Webpage to grab the content from
     * @param propertyTable Hashtable with the properties to set the header with
     * @param postMessage The message to post before getting the page
     *
     * public String postGrab2( String page, Hashtable propertyTable, String
     * postMessage, StringWriter returnCookie ) { print( "postGrab(" + page + ",
     * " + propertyTable + ", " + postMessage + ")" );
     *
     * URL sampleUrl; HttpURLConnection urlConn = null; OutputStream os;
     * OutputStreamWriter osw; InputStream is = null; InputStreamReader isr;
     * BufferedReader br; StringWriter sw = new StringWriter();
     *
     * try { // make url-connection sampleUrl = new URL( page ); urlConn =
     * (HttpURLConnection)(sampleUrl.openConnection());
     *
     * if (propertyTable!=null) { setRequestProperties( urlConn, propertyTable
     * ); }
     *
     * if (postMessage!=null) { urlConn.setDoOutput( true );
     *
     * os = urlConn.getOutputStream(); os.write( postMessage.getBytes() );
     * os.flush(); os.close(); }
     *
     * int iHeaderNumber; String sHeaderName; String sHeaderValue; is =
     * urlConn.getInputStream(); ////////
     *
     * int i = 0; while( urlConn.getHeaderFieldKey( i ) == null ) i++;
     *
     * String key = null, headerfield = null, cookie = null; while( ( key =
     * urlConn.getHeaderFieldKey( i ) ) != null ) { if ( key.equalsIgnoreCase(
     * "set-cookie" ) ) returnCookie.write( urlConn.getHeaderField( i ) ); i++;
     * }
     *
     * //////// isr = new InputStreamReader( is ); br = new BufferedReader( isr
     * ); String line;
     *
     * // get response out of connection line = br.readLine(); while ( line !=
     * null ) { sw.write( line ); sw.write( '\n' ); line = br.readLine(); } }
     * catch ( Exception e ) { try { FileOutputStream fos = new
     * FileOutputStream( "/metonymy/log/" + getClass().getName() + ".log", true
     * ); } catch (FileNotFoundException fnfe) { fnfe.printStackTrace(); }
     *
     * PrintWriter pw = new PrintWriter( fos );
     *
     * e.printStackTrace( pw ); }
     *
     * // close inputstream if any open try { if ( is!=null ) { is.close(); } }
     * catch ( IOException ioe ) { ioe.printStackTrace(); }
     *
     * // close urlconnection if any open if (urlConn!=null)
     * urlConn.disconnect();
     *
     * // Return the content of the webpage as a string return sw.toString(); }
     */
    private static final String HREFEQ = "href=";

    private String makeAbsoluteURLString(String base, String urlString) {
        String res;
        try {
            URL baseURL = new URL(base);
            URL absURL = new URL(baseURL, urlString);

            res = absURL.toString();
        } catch (MalformedURLException urle) {
            res = "";
        }

        return res;
    }

    private static final String HTMLTAG = "<HTML>";
    private static final char EUROSIGN = '�';
    private static final char FLORINSIGN = '�';
    private static final String ENCODEDEURO = "&euro;";
    private static final String ENCODEDFLORIN = "&fnof;";

    private String formatWithBase(String content, String base) {
        String htmlContent = content.toUpperCase(); // to search in

        int foundIndex = 0;
        StringWriter sw = new StringWriter();

        foundIndex = htmlContent.indexOf(HTMLTAG);

        if (foundIndex >= 0) {
            foundIndex += HTMLTAG.length();

            sw.write(content.substring(0, foundIndex)
                    + "<HEAD><BASE href=\"" + base + "\"></HEAD>"
                    + content.substring(foundIndex));
        } else {
            //            print("ERROR: Couldn't find head-tag in [" + content + "]");
            sw.write("<HTML><BODY>" + content + "</BODY></HTML>");
        }

        // search and replace euro-characters and florin-characters
        String wholeContent = sw.toString();
        sw = new StringWriter();
        int lastIndex = 0;
        int indexEuro;
        int indexFlorin;

        indexEuro = wholeContent.indexOf(EUROSIGN);
        indexFlorin = wholeContent.indexOf(FLORINSIGN);

        while (indexEuro >= 0 || indexFlorin >= 0) {
            // one of indexEuro or indexFlorin is non-negative
            int index;
            if (indexEuro < 0) {
                index = indexFlorin;
            } else if (indexFlorin < 0) {
                index = indexEuro;
            } else { // indexEuro>=0 && indexFlorin>=0
                index = Math.min(indexEuro, indexFlorin);
            }

            sw.write(wholeContent.substring(lastIndex, index));
            if (index == indexEuro) {
                sw.write(ENCODEDEURO);
            } else {
                sw.write(ENCODEDFLORIN);
            }

            lastIndex = index + 1;
            indexEuro = wholeContent.indexOf(EUROSIGN, lastIndex);
            indexFlorin = wholeContent.indexOf(FLORINSIGN, lastIndex);
            //            print( lastIndex + "@" + indexEuro + "@" + indexFlorin );
        }

        if (lastIndex < wholeContent.length()) {
            sw.write(wholeContent.substring(lastIndex));
        }

        return sw.toString();
    }

    /**
     * Grabs a webpage and adds a base-tag to it
     */
    public String grabWithBase(String page, String referer) {
        String response = grab(page, referer); // to write to results from
        String base = referer;

        int lastSlash = base.lastIndexOf('/');
        base = base.substring(0, lastSlash) + '/';

        String formattedContent = formatWithBase(response, base);

        return formattedContent;
    }

    /**
     * Grabs a webpage and adds a base-tag to it
     */
    public String grabWithBasePure(String page) {
        String response = grabHP(page); // to write to results from
        String base = page;

        int lastSlash = base.lastIndexOf('/');
        base = base.substring(0, lastSlash) + '/';

        String formattedContent = formatWithBase(response, base);

        return formattedContent;
    }

    // for funda only
    public String grabWithBaseWithRedirect(String page) {
        String response = postGrabWithRedirectFunda(page, null, null); // to write to results from
        String base = page;

        int lastSlash = base.lastIndexOf('/');
        base = base.substring(0, lastSlash) + '/';

        String formattedContent = formatWithBase(response, base);

        return formattedContent;
    }

    private static final String WHERERWESUBSTR = "descBUY";
    private static final String WHERERWEPATTERN = "var whereRWe";
    private static final String MONSTERCOOKIEPATTERN = "var monsterCookie";
    private String myCookie = "";

    /**
     * Grabs a webpage and adds a base-tag to it
     */
    public String grabWithBaseForFunda(String page, String referer) {
        String pageContent = grabWithBase(page, referer);
        StringWriter sw = new StringWriter();
        String whereRWe = page.substring(0,
                page.indexOf(WHERERWESUBSTR)
                + WHERERWESUBSTR.length());
        String monsterCookie = page.substring(
                whereRWe.length() + 1);
        String whereRWeLine = "var whereRWe = \"" + whereRWe + "\"";
        String monsterCookieLine = "var monsterCookie = \"" + monsterCookie
                + "\"//get cookie containg other files in search results";

        StringTokenizer st = new StringTokenizer(pageContent, "\n");
        String line;
        while (st.hasMoreTokens()) {
            line = st.nextToken();
            if (line.indexOf(WHERERWEPATTERN) >= 0) {
                sw.write(whereRWeLine);
            } else if (line.indexOf(MONSTERCOOKIEPATTERN) >= 0) {
                sw.write(monsterCookieLine);
            } else {
                sw.write(line);
            }

            sw.write("\n");
        }

        return sw.toString();
    }

    /**
     * Grabs a webpage and adds a base-tag to it
     */
    public String postGrabWithBase(String page, Hashtable headers, String postMessage, String referrer) {
        String pageContent = postGrab(page, headers, postMessage);
        String base = referrer;

        int lastSlash = base.lastIndexOf('/');
        base = base.substring(0, lastSlash) + '/';

        String formattedContent = formatWithBase(pageContent, base);

        return formattedContent;
    }

    /**
     * Grabs a webpage and adds a base-tag to it
     */
    public String postGrabWithBaseXXX(String sUDI) {
        // prepare post-request
        Hashtable headers = new Hashtable();
        headers.put("header.Host", "www.koophuizen.com");
        String postdata = null;

        String htmlPage = postGrab("http://www.koophuizen.com/detail.asp?idhuis=" + sUDI, headers, postdata);

        //        int lastSlash = base.lastIndexOf('/');
        //     base = base.substring(0, lastSlash) + '/';
        //     String formattedContent = formatWithBase( pageContent, base );
        //    return formattedContent;
        return htmlPage;
    }

    protected final void print(String s) {
        logToFile(s);
    }

    private final void logToFile(String s) {
        if (fos == null) {
            try {
                fos = new FileOutputStream("/metonymy/log/" + getClass().getName()
                        + ".log", true);
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        try {
            fos.write((GregorianCalendar.getInstance().getTime() + " @@ " + s + "\n").getBytes());
            fos.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
