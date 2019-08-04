/**
 * 
 */
package com.tcl.manager.datausage;

import com.tcl.manager.base.BaseInfo;

/**
 * This class for Application's network usage.
 * 
 * @author zuokang.li
 * 
 */
public class AppNetstatInfo extends BaseInfo {

    /** application's uid */
    public int uid;
    /** application's mobile data bytes */
    public long mobiledataBytes;
    /** application's wlan bytes, like WIFI */
    public long wlanBytes;
   // /** application's package name */
   // public String pkgName;

    @Override
    public String toString() {
        return "AppNetstatInfo [uid=" + uid + ", mobiledataBytes=" + mobiledataBytes + ", wlanBytes=" + wlanBytes + ", pkgName=" + pkgName + "]";
    }

}
