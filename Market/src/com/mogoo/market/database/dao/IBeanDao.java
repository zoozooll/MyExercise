package com.mogoo.market.database.dao;

import java.util.ArrayList;

import android.database.Cursor;

public interface IBeanDao<T> {
	public Cursor getAllBean();
	public void clearAllBean();
	public void addBeans(ArrayList<T> beans);
}
