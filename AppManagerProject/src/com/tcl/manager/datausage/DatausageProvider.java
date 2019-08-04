/**
 * 
 */
package com.tcl.manager.datausage;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;

import com.tcl.framework.log.NLog;
import com.tcl.manager.model.DataHistoryDBManager;
import com.tcl.manager.util.AdbCmdTool;
import com.tcl.manager.util.PkgManagerTool;

/**
 * @author zuokang.li
 *
 */
public class DatausageProvider {
	
	private static final int FILE_MAGIC = 0x414E4554;
	private static final int VERSION_NETWORK_INIT = 1;

    private static final int VERSION_UID_INIT = 1;
    private static final int VERSION_UID_WITH_IDENT = 2;
    private static final int VERSION_UID_WITH_TAG = 3;
    private static final int VERSION_UID_WITH_SET = 4;

    private static final int VERSION_UNIFIED_INIT = 16;

	public HashMap<Integer, NetworkStatsHistory> statsCellular = new HashMap<Integer, NetworkStatsHistory>();
	public HashMap<Integer, NetworkStatsHistory> statsWifi = new HashMap<Integer, NetworkStatsHistory>();
    public long totalCellular;
    public long totalWifi;
    public long startMillis;
    public long endMillis;
    
    
    
    public DatausageProvider() {
		super();
		long now = System.currentTimeMillis();
		Calendar c = Calendar.getInstance(); 
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		this.startMillis = c.getTimeInMillis();
		this.endMillis = now;
	}

	public DatausageProvider(long startMillis, long endMillis) {
		super();
		this.startMillis = startMillis;
		this.endMillis = endMillis;
	}
	
	public List<AppNetstatInfo> getUsageApps(Context context) {
		List<AppNetstatInfo> result = new ArrayList<AppNetstatInfo>();
		Collection<ApplicationInfo> apps = PkgManagerTool.getInstalledAppFilter(context);
		//DatausageProvider provider = new DatausageProvider(startMillis, endMillis);
		parseNetUsage();
		AppNetstatInfo info = null;
		for (ApplicationInfo app : apps) {
			long totalBytes;
			if (statsCellular.containsKey(app.uid) && (totalBytes = statsCellular.get(app.uid).getTotalBytes()) > 0) {
				if (info == null) {
					info = new AppNetstatInfo();
					info.uid = app.uid;
					info.pkgName = app.packageName;
				}
				info.mobiledataBytes = totalBytes;
			}
			if (statsWifi.containsKey(app.uid) && (totalBytes = statsWifi.get(app.uid).getTotalBytes()) > 0) {
				if (info == null) {
					info = new AppNetstatInfo();
					info.uid = app.uid;
					info.pkgName = app.packageName;
				}
				info.wlanBytes = totalBytes;
			}
			
			if (info != null) {
				result.add(info);
				info = null;
			}
		}
		return result;
	}
	
	public void saveDatausage(Date d, Context c) {
		DataHistoryDBManager db = DataHistoryDBManager.getInstance(c);
		parseNetUsage();
		Date realDate = new Date(d.getYear(), d.getMonth(), d.getDate());
		db.deleteUnusefulData(realDate);
		startMillis = realDate.getTime();
		endMillis = startMillis + 1000L * 3600L * 24L; 
		db.addUsageOfDate(realDate, statsWifi, statsCellular, totalWifi, totalCellular);
	}

	private void read(DataInputStream in) throws IOException {
        // verify file magic header intact
        final int magic = in.readInt();
//        System.out.println("magic:"+magic);
        if (magic != FILE_MAGIC) {
            throw new RuntimeException("unexpected magic: " + magic);
        }

        final int version = in.readInt();
//        System.out.println("version:"+version);
        switch (version) {
            case VERSION_UNIFIED_INIT: {
                // uid := size *(NetworkIdentitySet size *(uid set tag NetworkStatsHistory))
                final int identSize = in.readInt();
//                System.out.println("identSize:"+identSize);
                for (int i = 0; i < identSize; i++) {
                    final NetworkIdentitySet ident = new NetworkIdentitySet(in);
                    final int size = in.readInt();
                    for (int j = 0; j < size; j++) {
                        final int uid = in.readInt();
//                        System.out.println("uid:"+uid);
                        final int set = in.readInt();
//                        System.out.println("set:"+set);
                        final int tag = in.readInt();
//                        System.out.println("tag:"+tag);

                        final Key key = new Key(ident, uid, set, tag);
                        final NetworkStatsHistory history = new NetworkStatsHistory(in);
                        recordHistory(key, history, startMillis, endMillis);
                    }
                }
                // Print the totle 
               /* for (Map.Entry<Key, NetworkStatsHistory> entry : mStats.entrySet()) {   
                	Key key = entry.getKey();
                	Set<NetworkIdentity> identities = key.ident;
                	String subscriberId = null;
                	for (NetworkIdentity id : identities) {
                		System.out.println("mSubscriberId "+ id.mSubscriberId);
                		subscriberId = id.mSubscriberId;
                	}
                	if (subscriberId != null && !"".equals(subscriberId) && !"null".equalsIgnoreCase(subscriberId)) {
                		System.out.println("NetworkStatsHistory key= " + key.uid + "  and  value= "  
                				+ entry.getValue().getTotalBytes()); 
                	}
                }*/
                break;
            }
            default: {
                throw new RuntimeException("unexpected version: " + version);
            }
        }
    }
	
	/**
     * Record given {@link NetworkStatsHistory} into this collection.
     */
    private void recordHistory(Key key, NetworkStatsHistory history, long start, long end) {
        if (history.size() == 0) 
        	return;
        //noteRecordedHistory(history.getStart(), history.getEnd(), history.getTotalBytes());

        /*NetworkStatsHistory target = stats.get(key);
        if (target == null) {
            target = new NetworkStatsHistory(history.getBucketDuration());
            stats.put(key, target);
        }
        target.recordEntireHistory(history);*/
        boolean isCellular = false;
        for (NetworkIdentity identity : key.ident) {
        	if (!TextUtils.isEmpty(identity.getSubscriberId()) && !"null".equalsIgnoreCase(identity.getSubscriberId())) {
        		isCellular = true;
        		break;
        	}
        }
        if (isCellular) {
        	NetworkStatsHistory target = statsCellular.get(key.uid);
            if (target == null) {
                target = new NetworkStatsHistory(history.getBucketDuration());
                statsCellular.put(key.uid, target);
            }
            target.recordHistory(history, start, end);
            totalCellular += target.getTotalBytes();
        } else {
        	NetworkStatsHistory target = statsWifi.get(key.uid);
            if (target == null) {
                target = new NetworkStatsHistory(history.getBucketDuration());
                statsWifi.put(key.uid, target);
            }
            target.recordHistory(history, start, end);
            totalWifi += target.getTotalBytes();
        }
 
    }
    
   /* private static void noteRecordedHistory(long startMillis, long endMillis, long totalBytes) {
        if (startMillis < mStartMillis) mStartMillis = startMillis;
        if (endMillis > mEndMillis) mEndMillis = endMillis;
        mTotalBytes += totalBytes;
        mDirty = true;
    }*/
    
    

	/**
	 * 
	 * @param startMillis
	 * @param endMillis
	 */
	public void parseNetUsage() {
		try {
			String[] files = getUsableDataFiles();
			getNetstatUsageForApps(files);
		} catch (IOException e) {
			e.printStackTrace();
		}
/*		Log.i("aaron", "===============mobile data===================");
		for (Map.Entry<Integer, NetworkStatsHistory> entry : statsCellular.entrySet()) {   
			Integer key = entry.getKey();
        	String subscriberId = null;
        	for (NetworkIdentity id : identities) {
        		System.out.println("mSubscriberId "+ id.mSubscriberId);
        		subscriberId = id.mSubscriberId;
        	}
        	//if (subscriberId != null && !"".equals(subscriberId) && !"null".equalsIgnoreCase(subscriberId)) {
        		System.out.println("NetworkStatsHistory key= " + key + "  and  value= "  
        				+ entry.getValue().getTotalBytes()); 
//        	}
        }
		Log.i("aaron", "===============wifi data===================");
		for (Map.Entry<Integer, NetworkStatsHistory> entry : statsWifi.entrySet()) {   
			Integer key = entry.getKey();
        	String subscriberId = null;
        	//for (NetworkIdentity id : identities) {
//        		System.out.println("mSubscriberId "+ id.mSubscriberId);
//        		subscriberId = id.mSubscriberId;
//        	}
//        	if (subscriberId != null && !"".equals(subscriberId) && !"null".equalsIgnoreCase(subscriberId)) {
        		System.out.println("NetworkStatsHistory key= " + key + "  and  value= "  
        				+ entry.getValue().getTotalBytes()); 
//        	}
        }*/
		
//		DatausageLog4.writeFile(statsCellular, statsWifi);
	}
	
	private String[] getUsableDataFiles() throws IOException {
		//Process p = Runtime.getRuntime().exec("ls /sdcard/");
		Process p = AdbCmdTool.cmd("ls /data/system/netstats");
		if (p == null) {
        	// Add some log messages showing shell process's error string,
        	//it will be shown when buildbonfig is debug.
        	// Added by zuokang.li Dec19 2014.
        	NLog.e("Runtime shell", "cannot run files");
            return null;
        }
        InputStream errorStream = p.getErrorStream();
        String errorString = AdbCmdTool.streamToString(errorStream);
        if (!TextUtils.isEmpty(errorString)) {
        	NLog.e("Runtime shell", errorString);
        	errorStream.close();
            return null;
        }
		InputStream is = p.getInputStream();
		BufferedReader linesStream = new BufferedReader(new InputStreamReader(is, Charset.defaultCharset()));
		String line = null;
		List<String> files = new ArrayList<String>();
		while ((line = linesStream.readLine()) != null) {  
            if (line.trim().startsWith("uid")) {
            	try {
            		long lineStart = getNetstatsStartMillis(line);
            		long lineEnd = getNetstatsEndMillis(line);
            		if (startMillis < lineEnd && endMillis > lineStart) {
            			files.add(line);
            		}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					continue;
				}
            }
        }
		String[] array = new String[files.size()];
		files.toArray(array);
		files.clear();
		return array;
	}
	
	private void getNetstatUsageForApps(String[] files) {
		if (files == null) {
			return;
		}
		statsCellular.clear();
		statsWifi .clear();
		totalCellular = 0;
		totalWifi = 0;
		for (String file : files) {
			try {
				Process p = AdbCmdTool.cmd("cat /data/system/netstats/"+file);
				InputStream errorStream = p.getErrorStream();
		        String errorString = AdbCmdTool.streamToString(errorStream);
		        if (!TextUtils.isEmpty(errorString)) {
		        	NLog.e("Runtime shell", errorString);
		        	errorStream.close();
		        	continue;
		        }
				InputStream is = p.getInputStream();
				read(new DataInputStream(is));
				p.waitFor();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private long getNetstatsStartMillis (String fileName) {
		String str = fileName.substring(fileName.indexOf('.') + 1, fileName.indexOf('-'));
		long result = Long.MIN_VALUE;
		try {
			result = Long.parseLong(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private long getNetstatsEndMillis (String fileName) throws NumberFormatException {
		long result = Long.MAX_VALUE;
		String str = fileName.substring(fileName.indexOf('-') + 1);
		if (!TextUtils.isEmpty(str)) {
			try {
				result = Long.parseLong(str);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} 
		return result;
	}
}
