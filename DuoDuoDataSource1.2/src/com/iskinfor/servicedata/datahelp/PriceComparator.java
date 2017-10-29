package com.iskinfor.servicedata.datahelp;

import java.util.Comparator;

import com.iskinfor.servicedata.pojo.Product;


public class PriceComparator implements Comparator<Product> {
	
	boolean flag = false;
	
	public PriceComparator() {
	}
	
	public PriceComparator(boolean flag) {
		this.flag = flag;
	}

	public int compare(Product object1, Product object2) {
		double d = -0f;
		if (flag) {
			d = Double.parseDouble(object2.getProPrice()) - Double.parseDouble(object1.getProPrice());
		} else {
			d = Double.parseDouble(object1.getProPrice()) - Double.parseDouble(object2.getProPrice());
		}
		return (int)d;
	}

	
}
