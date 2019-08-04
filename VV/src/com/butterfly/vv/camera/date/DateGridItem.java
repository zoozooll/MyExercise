package com.butterfly.vv.camera.date;

public class DateGridItem {
	public int[] images_normal;
	public int[] images_select;
	public int background_normal;
	public int background_select;
	public int[] texts;
	private int selection;

	public void setSelection(int position) {
		selection = position;
	}
	public int getSelection() {
		return selection;
	}
}
