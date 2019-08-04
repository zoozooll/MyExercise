package com.tcl.manager.statistic.frequency;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.tcl.framework.log.NLog;

import android.os.Parcel;
import android.os.SystemClock;
import android.util.Log;

/**
 * @Description:用户行为记录，从com.android.server.am.UsageStatsService借鉴而来 ;
 * @author jiaquan.huang
 * @date 2014-12-5 下午2:33:23
 * @copyright TCL-MIE
 */
public class UsageStats {
    // static String path = "/data/system/usagestats/usage-20141205";
    final Object mStatsLock;
    private String fileName = "usage-";
    Map<String, PkgUsageStatsExtended> mStats;
    private static final int NUM_LAUNCH_TIME_BINS = 10;
    private static final int[] LAUNCH_TIME_BINS = { 250, 500, 750, 1000, 1500, 2000, 3000, 4000, 5000 };
    private static final int VERSION = 1007;

    public UsageStats() {
        mStatsLock = new Object();
    }

    /*
     * Utility method to convert date into string.
     */
    private String getCurrentDateStr() {
        StringBuilder sb = new StringBuilder();
        Calendar mCal = Calendar.getInstance(TimeZone.getTimeZone("GMT+0"));
        mCal.setTimeInMillis(System.currentTimeMillis());
        sb.append(fileName);
        sb.append(mCal.get(Calendar.YEAR));
        int mm = mCal.get(Calendar.MONTH) - Calendar.JANUARY + 1;
        if (mm < 10) {
            sb.append("0");
        }
        sb.append(mm);
        int dd = mCal.get(Calendar.DAY_OF_MONTH);
        if (dd < 10) {
            sb.append("0");
        }
        sb.append(dd);
        return sb.toString();
    }

    public Map<String, PkgUsageStatsExtended> getUserStats(String path) {
        init();
        read(path);
        return mStats;
    }

    public void init() {
        mStats = null;
        mStats = new HashMap<String, PkgUsageStatsExtended>();
    }

    public void read(String path) {
        String filePath = path;
        File file = new File(filePath);
        if (file != null) {
            try {
                Parcel in = getParcelForFile(file);
                readStatsFParcel(in);
            } catch (IOException e) {
            	NLog.e("e", e.toString());
            }
        }
    }

    private void readStatsFParcel(Parcel in) throws IOException {
        int vers = in.readInt();
        if (vers != VERSION) {
            NLog.w("debug", "UsageStats版本不等于1007");
        }
        int N = in.readInt();
        while (N > 0) {
            N--;
            String pkgName = in.readString();
            if (pkgName == null) {
                break;
            }
            PkgUsageStatsExtended pus = new PkgUsageStatsExtended(in);
            synchronized (mStatsLock) {
                mStats.put(pkgName, pus);
            }
        }
        in.recycle();
    }

    private static Parcel getPareclForStrean(FileInputStream stream) throws IOException {
        byte[] raw;
        raw = readFully(stream);
        Parcel in = Parcel.obtain();
        in.unmarshall(raw, 0, raw.length);
        in.setDataPosition(0);
        stream.close();
        return in;
    }

    private Parcel getParcelForFile(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        return getPareclForStrean(stream);
    }

    static byte[] readFully(FileInputStream stream) throws java.io.IOException {
        int pos = 0;
        int avail = stream.available();
        byte[] data = new byte[avail];
        while (true) {
            int amt = stream.read(data, pos, data.length - pos);
            if (amt <= 0) {
                return data;
            }
            pos += amt;
            avail = stream.available();
            if (avail > data.length - pos) {
                byte[] newData = new byte[pos + avail];
                System.arraycopy(data, 0, newData, 0, pos);
                data = newData;
            }
        }
    }

    public static class PkgUsageStatsExtended {
        final HashMap<String, TimeStats> mLaunchTimes = new HashMap<String, TimeStats>();
        public int mLaunchCount;
        public long mUsageTime;
        long mPausedTime;
        long mResumedTime;

        PkgUsageStatsExtended() {
            mLaunchCount = 0;
            mUsageTime = 0;
        }

        PkgUsageStatsExtended(Parcel in) {
            mLaunchCount = in.readInt();
            mUsageTime = in.readLong();
            final int numTimeStats = in.readInt();
            for (int i = 0; i < numTimeStats; i++) {
                String comp = in.readString();
                TimeStats times = new TimeStats(in);
                mLaunchTimes.put(comp, times);
            }
        }

        void updateResume(String comp, boolean launched) {
            if (launched) {
                mLaunchCount++;
            }
            mResumedTime = SystemClock.elapsedRealtime();
        }

        void updatePause() {
            mPausedTime = SystemClock.elapsedRealtime();
            mUsageTime += (mPausedTime - mResumedTime);
        }

        void addLaunchCount(String comp) {
            TimeStats times = mLaunchTimes.get(comp);
            if (times == null) {
                times = new TimeStats();
                mLaunchTimes.put(comp, times);
            }
            times.incCount();
        }

        void addLaunchTime(String comp, int millis) {
            TimeStats times = mLaunchTimes.get(comp);
            if (times == null) {
                times = new TimeStats();
                mLaunchTimes.put(comp, times);
            }
            times.add(millis);
        }

        void writeToParcel(Parcel out) {
            out.writeInt(mLaunchCount);
            out.writeLong(mUsageTime);
            final int numTimeStats = mLaunchTimes.size();
            out.writeInt(numTimeStats);
            if (numTimeStats > 0) {
                for (Map.Entry<String, TimeStats> ent : mLaunchTimes.entrySet()) {
                    out.writeString(ent.getKey());
                    TimeStats times = ent.getValue();
                    times.writeToParcel(out);
                }
            }
        }

        void clear() {
            mLaunchTimes.clear();
            mLaunchCount = 0;
            mUsageTime = 0;
        }
    }

    static class TimeStats {
        int count;
        int[] times = new int[NUM_LAUNCH_TIME_BINS];

        TimeStats() {
        }

        void incCount() {
            count++;
        }

        void add(int val) {
            final int[] bins = LAUNCH_TIME_BINS;
            for (int i = 0; i < NUM_LAUNCH_TIME_BINS - 1; i++) {
                if (val < bins[i]) {
                    times[i]++;
                    return;
                }
            }
            times[NUM_LAUNCH_TIME_BINS - 1]++;
        }

        TimeStats(Parcel in) {
            count = in.readInt();
            final int[] localTimes = times;
            for (int i = 0; i < NUM_LAUNCH_TIME_BINS; i++) {
                localTimes[i] = in.readInt();
            }
        }

        void writeToParcel(Parcel out) {
            out.writeInt(count);
            final int[] localTimes = times;
            for (int i = 0; i < NUM_LAUNCH_TIME_BINS; i++) {
                out.writeInt(localTimes[i]);
            }
        }
    }

}
