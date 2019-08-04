package com.beem.project.btf.utils;

import java.util.ArrayList;

public class UniqueList<E> extends ArrayList<E> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2631966369863384493L;

	@Override
	public boolean add(E object) {
		removeSameObj(object);
		return super.add(object);
	}

	@Override
	public void add(int index, E object) {
		removeSameObj(object);
		super.add(index, object);
	}

	@Override
	public E set(int index, E object) {
		removeSameObj(object);
		return super.set(index, object);
	}
	
	private void removeSameObj(E object) {
		if (contains(object)) {
			remove(object);
		}
	}
	
	
}
