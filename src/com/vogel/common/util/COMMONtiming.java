/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.vogel.common.util;

/**
 *
 * @author  VOGEL
 * @version
 */
public class COMMONtiming extends java.lang.Object {

    java.util.Date startTime = null, endTime = null;
    long lConvTime = 0L;

    public COMMONtiming() {
    }

    public void restart() {
        startTime = null;
        endTime = null;
        start();
    }

    public void start() {
        this.startTime = new java.util.Date();
    }

    public void stop() {
        this.endTime = new java.util.Date();
    }

    public long lapsedTime() {
        if (endTime != null && startTime != null) {
            return this.endTime.getTime() - startTime.getTime();
        } else if (startTime != null) {
            return (new java.util.Date()).getTime() - startTime.getTime();
        } else {
            return 0L;
        }
    }
}
