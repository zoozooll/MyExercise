/**
 * new recycle bin for snack view items.
 * It managed the all of snack view' items. Some of them showing were in {@link Map}  mActiveViews, and some of them not shown should change to {@link List}  mScrapViews
 * added by aaronli at May22,2013.
 */
package com.oregonscientific.meep.opengl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.R.integer;

/**
 * The RecycleBin facilitates reuse of views across layouts. The RecycleBin has two levels of
 * storage: ActiveViews and ScrapViews. ActiveViews are those views which were onscreen at the
 * start of a layout. By construction, they are displaying current information. At the end of
 * layout, all views in ActiveViews are demoted to ScrapViews. ScrapViews are old views that
 * could potentially be used by the adapter to avoid allocating views unnecessarily.
 *
 * @see android.widget.AbsListView#setRecyclerListener(android.widget.AbsListView.RecyclerListener)
 * @see android.widget.AbsListView.RecyclerListener
 */
class RecycleBin {
	
	
    private RecyclerListener mRecyclerListener;

    /**
     * The position of the first view stored in mActiveViews.
     */
    private int mFirstActivePosition;

    private int mViewTypeCount;

    private List<OSButton> mScrapViews;
    
    private Map<Integer, OSButton> mActiveViews;
    
    RecycleBin() {
		super();
		mScrapViews = new Vector<OSButton>();
		mActiveViews = new HashMap<Integer,OSButton>();
	}


	boolean shouldRecycleViewType(int viewType) {
        return viewType >= 0;
    }


    /**
     * @return A view from the ScrapViews collection. These are unordered.
     */
    OSButton getScrapView(int position) {
        return retrieveFromScrap(position);
    }
    
    /**
     * @return A view from the ScrapViews collection. These are unordered.
     */
    OSButton getActiveView(int position) {
        return mActiveViews.get(position);
    }
    
    
    /*void addActiveViewToEnd(OSButton view) {
    	mActiveViews.add(view);
    }
    
    void addActiveViewToFirst(OSButton view) {
    	mActiveViews.add(0, view);
    }
    
    void setActiveView(OSButton view, int showIndex) {
    	mActiveViews.set(showIndex, view);
    }
    
    void removeFirestActiveView () {
    	mActiveViews.remove(0);
    }
    
    void removeEndActiveView () {
    	mActiveViews.remove(mActiveViews.size() - 1);
    }*/
    
    /**
     * adds a view to actvie.
     * @param view the {@link OSButton} added to active map
     * @param positionIndex the showing index in all items 
     */
    void addActiveView(OSButton view, int positionIndex) {
    	OSButton flag = mActiveViews.get(positionIndex);
    	if (flag != null && flag == view) {
    		
    	} else {
    		flag = view;
    		mActiveViews.put(positionIndex, flag);
    	}
    	if (flag.getShowIndex() != positionIndex) {
    		flag.setShowIndex(positionIndex);
    	}
    	if (mScrapViews.contains(flag)) {
    		mScrapViews.remove(flag);
    	}
    	if (mRecyclerListener != null) {
    		mRecyclerListener.onAddActiveView(view);
    	}
    }
    
    /**
     * Removed the active view and put it to mScrapViews
     * @param positionIndex 
     */
    void removeActiveView(int positionIndex) {
    	if (mActiveViews.containsKey(positionIndex)) {
    		OSButton flag = mActiveViews.remove(positionIndex);
    		if (flag != null) {
    			if (flag.getPosition() != positionIndex) {
    				flag.setPosition(positionIndex);
    			}
    			mScrapViews.add(flag);
    			if (mRecyclerListener != null) {
    				mRecyclerListener.onRemoveActiveView(flag);
    			}
    		}
    	}
    }
    
    /**
     * make all of the scrapAllActive
     */
    void scrapAllActiveView() {
    	Collection<OSButton> actives = mActiveViews.values();
    	for (OSButton view : actives) {
    		if (mRecyclerListener != null) {
				mRecyclerListener.onRemoveActiveView(view);
			}
    		
    		mScrapViews.add(view);
    	}
    	mActiveViews.clear();
    }
    
/*    void resetAllScrapViewsTexture() {
    	for (OSButton view : mScrapViews) { 
    		view.setTexureID(0);
    		view.setDimTexId(0);
    	}
    }*/
    
    void removeAllScrapViews() {
    	
    	mScrapViews.clear();
    }

    /**
     * Put a view into the ScapViews list. These views are unordered.
     *
     * @param scrap The view to add
     */
    void addScrapView(OSButton scrap, int positionIndex) {

    	scrap.setShowIndex(positionIndex);

        mScrapViews.add(scrap);

        if (mRecyclerListener != null) {
            mRecyclerListener.onMovedToScrapHeap(scrap);
        }
    }

    void removeScrapView(OSButton scrap) {

        mScrapViews.remove(scrap);

    }

    private OSButton retrieveFromScrap(int position) {
        int size = mScrapViews.size();
        if (size > 0) {
            // See if we still have a view for this position.
            /*for (int i=0; i<size; i++) {
            	OSButton view = mScrapViews.get(i);
                if (view.getShowIndex() == position) {
                	mScrapViews.remove(i);
                    return view;
                }
            }
            return mScrapViews.remove(size - 1);*/
        	return mScrapViews.remove(0);
        } else {
            return null;
        }
    }
    
    private OSButton retrieveFromScrap(List<OSButton> scrapViews, int position) {
        int size = scrapViews.size();
        if (size > 0) {
            // See if we still have a view for this position.
            for (int i=0; i<size; i++) {
            	OSButton view = scrapViews.get(i);
                if (view.getShowIndex() == position) {
                    scrapViews.remove(i);
                    return view;
                }
            }
            return scrapViews.remove(size - 1);
        } else {
            return null;
        }
    }


    /**
	 * @param mRecyclerListener the mRecyclerListener to set
	 */
	public void setRecyclerListener(RecyclerListener recyclerListener) {
		this.mRecyclerListener = recyclerListener;
	}


	/**
     * A RecyclerListener is used to receive a notification whenever a View is placed
     * inside the RecycleBin's scrap heap. This listener is used to free resources
     * associated to Views placed in the RecycleBin.
     *
     * @see android.widget.AbsListView.RecycleBin
     * @see android.widget.AbsListView#setRecyclerListener(android.widget.AbsListView.RecyclerListener)
     */
    public static interface RecyclerListener {
        /**
         * Indicates that the specified View was moved into the recycler's scrap heap.
         * The view is not displayed on screen any more and any expensive resource
         * associated with the view should be discarded.
         *
         * @param view
         */
        void onMovedToScrapHeap(OSButton view);
        
        /**
         * when one view add to the active contain 
         * @param view The view to active contain
         */
        void onAddActiveView(OSButton view);
        
        /**
         * 
         * @param view
         */
        void onRemoveActiveView(OSButton view);
    }
    
    
}