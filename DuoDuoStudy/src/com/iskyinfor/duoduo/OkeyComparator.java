package com.iskyinfor.duoduo;

import java.util.Comparator;

import com.iskinfor.servicedata.pojo.Product;

public class OkeyComparator implements Comparator<Product> {

	public int compare(Product object1, Product object2) {
//		return object1.getTranNum() - object2.getTranNum();
		return 0;
	}

}
