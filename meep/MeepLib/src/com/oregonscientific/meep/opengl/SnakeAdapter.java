/**
 * 
 */
package com.oregonscientific.meep.opengl;

import com.oregonscientific.meep.message.common.OsListViewItem;

/**
 * 
 * Add all of something adapter.
 * @author aaronli
 * @date May22,2013
 */
public interface SnakeAdapter<T> {

    /**
     * How many items are in the data set represented by this Adapter.
     * 
     * @return Count of items.
     */
    public int getCount();   
    
    /**
     * Get the data item associated with the specified position in the data set.
     * 
     * @param position Position of the item whose data we want within the adapter's 
     * data set.
     * @return The data at the specified position.
     */
    public T getItem(int position);
    
    /**
     * Get the row id associated with the specified position in the list.
     * 
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    public long getItemId(int position);
    
    
    /**
     * 
     * @param position The index of all items.
     * @param converView The view from caches
     * @return The view to the list
     */
    public OSButton getView(int position, OSButton converView);

    
     /**
      * @return true if this adapter doesn't contain any data.  This is used to determine
      * whether the empty view should be displayed.  A typical implementation will return
      * getCount() == 0 but since getCount() includes the headers and footers, specialized
      * adapters might want a different behavior.
      */
     boolean isEmpty();
     
     public abstract void onDelectedItem(OsListViewItem deleteItem);
		
	 public abstract void onRenameItem(OsListViewItem deleteItem);
}
