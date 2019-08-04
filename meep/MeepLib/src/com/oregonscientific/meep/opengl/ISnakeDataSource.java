/**
 * 
 */
package com.oregonscientific.meep.opengl;

import java.util.List;

import com.oregonscientific.meep.message.common.OsListViewItem;

/**
 * Data source loading always 
 * @author aaronli
 *
 */
public interface ISnakeDataSource {
	
	
	/**
	 * @return: the {@link List} is data source;
	 */
	public List<OsListViewItem> loadDataSource();
	
	/**
	 * loading the all {@link OSButton}'s data source from local or host. And set Dummy textureId.It always run in
	 */
	public void loadButtonWithDummyImages();
	
	/**
	 * loading the {@link OSButton}'s image. <strong>It always run in thread</strong>
	 * @param button: the {@link OSButton} which's image prevent to load.
	 */
	public void loadingButtonImage(OSButton button);
}
