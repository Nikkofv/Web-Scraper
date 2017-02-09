/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vogel.record.util;

/**
 *
 * @author VOGEL
 */
public final class RealmStopToken extends Realm {

    public static final RealmStopToken INSTANCE = new RealmStopToken();

    private RealmStopToken() {
        super.bhalt = true;
    }
}
