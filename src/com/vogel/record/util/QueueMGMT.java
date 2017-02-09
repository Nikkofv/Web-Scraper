/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vogel.record.util;

import java.lang.reflect.Constructor;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

/**
 *
 * @author VOGEL
 */
public class QueueMGMT {

    private static final Logger logger = Logger.getLogger("TESTER");
    private ArrayBlockingQueue<Realm> queue = null;
    private String taskname = "";
    public Task[] workers;
    private int qSize = 0;
    public Realm staticRealm = null;

    public QueueMGMT(String taskname, int iWorkers) {
        this.qSize = 1000;
        if (iWorkers > 64) {
            System.out.println("*** DEFAULTING FROM " + iWorkers + " TO 8 WORKERS.");
            iWorkers = 8;
        }
        workers = new Task[iWorkers];
        this.taskname = taskname;
        createQueue();
        createWorkers();
    }

    public QueueMGMT(String taskname, int iWorkers, int qSize) {
        this.qSize = qSize;
        if (iWorkers > 64) {
            System.out.println("*** DEFAULTING FROM " + iWorkers + " TO 8 WORKERS.");
            iWorkers = 8;
        }
        workers = new Task[iWorkers];
        this.taskname = taskname;
        createQueue();
        createWorkers();
    }

    private void createQueue() {
        logger.fine("CREATING QUEUE");
        queue = new ArrayBlockingQueue<Realm>(qSize);
    }

    private void createWorkers() {
        try {
            Constructor c = Class.forName(taskname).getDeclaredConstructor(ArrayBlockingQueue.class, int.class);
            c.setAccessible(true);
            int i = 0;
            for (Task k : workers) {
                workers[i] = (Task) c.newInstance(new Object[]{queue, i});
                workers[i].start();
                i++;
            }
        } catch (Exception e) {
            System.out.println("*** ERROR. Workers have not been created. " + e.getMessage());
        }
    }

    public void sleep() {
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            System.out.println("**** pfff....");
        }
    }
    
     public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            System.out.println("**** pfff....");
        }
    }

    public int getLastCommittedUoW() {
        int iLastCommittedUoW = Integer.MAX_VALUE;
        for (Task k : workers) {
            if (k.iLastCommittedUoW < iLastCommittedUoW) {
                iLastCommittedUoW = k.iLastCommittedUoW;
            }
        }
        return iLastCommittedUoW;
    }

    public void addWorkerWith(Task t, int iIndex) {
        workers[iIndex] = t;
        workers[iIndex].start();
    }

    public void addQ(Realm params) throws InterruptedException {
        queue.put(params);
    }

    public int getQueueSize() {
        return this.queue.size();
    }

    public ArrayBlockingQueue<Realm> getQueue() {
        return this.queue;
    }

    private void dowait() {
        while (queue.size() > 0) {
        }
    }

    public void halt() throws InterruptedException {
        for (Task k : workers) {
            addQ((Realm) RealmStopToken.INSTANCE);
        }
        dowait();
    }

    public void halt(Realm x) throws InterruptedException {
        for (Task k : workers) {
            addQ(x);
        }
        dowait();
    }
}
