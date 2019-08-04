/**
 * 
 */
package com.tcl.manager.blackwhitelist;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import android.text.TextUtils;

import com.tcl.base.http.IProviderCallback;
import com.tcl.framework.notification.NotificationCenter;
import com.tcl.manager.firewall.FirewallManager;
import com.tcl.manager.optimize.AutoStartBlackList;
import com.tcl.manager.protocol.WhiteListProtocol;
import com.tcl.manager.protocol.data.WhiteListPojo;
import com.tcl.manager.protocol.data.WhiteListPojo.WhiteInfo;
import com.tcl.manager.util.SharedStorekeeper;

/**
 * @author zuokang.li
 * 
 */
public class WhitelistSync {

    public static void checkWhiteList() {

        long prevSync = SharedStorekeeper.getLong(SharedStorekeeper.WHITELIST_LAST_REPORTED_TIME);
        if (Math.abs(System.currentTimeMillis() - prevSync) >= 1000 * 3600 * 24) {
            new WhiteListProtocol(new IProviderCallback<WhiteListPojo>() {

                @Override
                public void onSuccess(WhiteListPojo obj) {
                    SharedStorekeeper.save(SharedStorekeeper.WHITELIST_LAST_REPORTED_TIME, System.currentTimeMillis());
                    if (obj == null || obj.data == null || obj.data.infos == null) {
                        return;
                    }
                    WhiteListSharedManager wlsm = WhiteListSharedManager.getSingleInstance();
                    /*
                     * wlsm.clearWhiteList(); for (WhiteInfo item :
                     * obj.data.infos) { if (!TextUtils.isEmpty(item.pkn)) {
                     * wlsm.appendWhiteList(item.pkn);
                     * FirewallManager.getSingleInstance().remove(item.pkn);
                     * AutoStartBlackList
                     * .getInstance().removeFromBlackList(item.pkn); } }
                     */
                    Collection<String> localList = wlsm.getAllWhiteList();
                    Set<String> newList = new HashSet<String>();
                    Set<String> olderList = new HashSet<String>();
                    for (WhiteInfo item : obj.data.infos) {
                        if (localList.contains(item.pkn)) {
                            olderList.add(item.pkn);
                        } else {
                            newList.add(item.pkn);
                        }
                    }
                    localList.removeAll(olderList);
                    if (newList.isEmpty() && localList.isEmpty()) {
                        return;
                    }
                    for (String item : newList) {
                        wlsm.appendWhiteList(item);
                    }
                    for (String item : localList) {
                        wlsm.removeWhiteList(item);
                    }
                    WhiteListRefreshEvent event = new WhiteListRefreshEvent();
                    event.pkgName = newList;
                    NotificationCenter.defaultCenter().publish(event);
                    localList.clear();
                    olderList.clear();
                }

                @Override
                public void onFailed(int code, String msg, Object obj) {

                }

                @Override
                public void onCancel() {

                }
            }).send();
        }

    }
}
