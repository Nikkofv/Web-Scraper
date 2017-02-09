/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */

package com.vogel.common.util;

/**
 *
 * @author  Ronny Chan
 * @version 
 */
public class SleepyThread extends java.lang.Thread {

    public static final long DEFAULTSLEEPTIME = 500l;
    public static final long DEFAULTSLEEPTIMEEXTENSIONFACTOR = 2l;
    
    private long sleepTime;
    private long sleepTimeExtensionFactor;
    
    /** Creates new WebpageGrabberThread */
    public SleepyThread() {
        sleepTime = DEFAULTSLEEPTIME;
        sleepTimeExtensionFactor = DEFAULTSLEEPTIMEEXTENSIONFACTOR;
    }

    /** Creates new WebpageGrabberThread *
    public SleepyThread( int sleepTime ) {
        this.sleepTime = sleepTime;
        sleepTimeExtensionFactor = DEFAULTSLEEPTIMEEXTENSIONFACTOR;
    }

    /** Creates new WebpageGrabberThread *
    public SleepyThread( int sleepTime, int extensionFactor ) {
        this.sleepTime = sleepTime;
        sleepTimeExtensionFactor = extensionFactor;
    }
     */

    public void setSleepTime( long sleepTime ) {
        this.sleepTime = sleepTime;
    }
    
    public void setSleepTimeExtensionFactor( long sleepTimeExtensionFactor ) {
        this.sleepTimeExtensionFactor = sleepTimeExtensionFactor;
    }
    
    public void run() {
        try {
            sleep( sleepTime );
            sleepTime *= sleepTimeExtensionFactor;
        }
        catch (InterruptedException ie) {
        }
    }
    
}
