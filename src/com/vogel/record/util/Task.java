/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vogel.record.util;

/**
 *
 * @author VOGEL
 */
public abstract class Task extends Thread {

    protected int iLastCommittedUoW = 0;

}
