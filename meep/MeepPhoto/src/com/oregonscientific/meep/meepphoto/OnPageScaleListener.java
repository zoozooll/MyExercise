package com.oregonscientific.meep.meepphoto;

public interface OnPageScaleListener {
	/**
	 * 
	 */
    public void onScaleBegin();

    public void onScaleEnd(float scale);
    
    /**
     * on ZoomView 's image to the right and prevent to turn left page
     * @author aaronli Mar12 2013
     */
    public void onImageToLeft();
    
    /**
     * on ZoomView 's image to the right and prevent to turn right page
     * @author aaronli Mar12 2013
     */
    public void onImageToRight();
}
