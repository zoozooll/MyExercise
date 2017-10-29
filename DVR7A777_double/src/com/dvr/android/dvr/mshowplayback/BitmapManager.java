/**
 * 
 */
package com.dvr.android.dvr.mshowplayback;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.util.Log;
import java.io.FileDescriptor;
import java.util.WeakHashMap;

/**
 * Provides utilities to decode bitmap, get thumbnail, and cancel the operations.
 * 
 * <p>
 * The function {@link #decodeFileDescriptor(FileDescriptor, BitmapFactory.Options)} is used to decode a bitmap. During
 * decoding another thread can cancel it using the function {@link #cancelThreadDecoding(Thread, ContentResolver)}
 * specifying the {@code Thread} which is in decoding.
 * 
 * <p>
 * {@code cancelThreadDecoding(Thread,ContentResolver)} is sticky until {@code allowThreadDecoding(Thread) } is called.
 */
public class BitmapManager
{
    private static final String TAG = "BitmapManager";
    private static enum State
    {
        CANCEL, ALLOW
    }

    private static class ThreadStatus
    {
        public State mState = State.ALLOW;
        public BitmapFactory.Options mOptions;
        
        @Override
        public String toString()
        {
            String s;
            if (mState == State.CANCEL)
            {
                s = "Cancel";
            }
            else if (mState == State.ALLOW)
            {
                s = "Allow";
            }
            else
            {
                s = "?";
            }
            s = "thread state = " + s + ", options = " + mOptions;
            return s;
        }
    }

    private final WeakHashMap<Thread, ThreadStatus> mThreadStatus = new WeakHashMap<Thread, ThreadStatus>();
    private static BitmapManager sManager = null;
    private BitmapManager()
    {
    }

    /**
     * Get thread status and create one if specified.
     */
    private synchronized ThreadStatus getOrCreateThreadStatus(Thread t)
    {
        ThreadStatus status = mThreadStatus.get(t);
        if (status == null)
        {
            status = new ThreadStatus();
            mThreadStatus.put(t, status);
        }
        return status;
    }

    public synchronized boolean canThreadDecoding(Thread t)
    {
        ThreadStatus status = mThreadStatus.get(t);
        if (status == null)
        {
            // allow decoding by default
            return true;
        }
        boolean result = (status.mState != State.CANCEL);
        return result;
    }

    /**
     * Gets the thumbnail of the given ID of the original image.
     * 
     * <p>
     * This method wraps around @{code getThumbnail} in {@code android.provider.MediaStore}. It provides the ability to
     * cancel it.
     */
    public Bitmap getThumbnail(ContentResolver cr, long origId, int kind, BitmapFactory.Options options,
        boolean isVideo)
    {
        Thread t = Thread.currentThread();
        ThreadStatus status = getOrCreateThreadStatus(t);

        if (!canThreadDecoding(t))
        {
            Log.d(TAG, "Thread " + t + " is not allowed to decode.");
            return null;
        }

        try
        {
            if (isVideo)
            {
                return Video.Thumbnails.getThumbnail(cr, origId, t.getId(), kind, null);
            }
            else
            {
                return Images.Thumbnails.getThumbnail(cr, origId, t.getId(), kind, null);
            }
        }
        finally
        {
            synchronized (status)
            {
                status.notifyAll();
            }
        }
    }

    public static synchronized BitmapManager instance()
    {
        if (sManager == null)
        {
            sManager = new BitmapManager();
        }
        return sManager;
    }
}
