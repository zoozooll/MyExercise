package com.aarom.test;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import com.iskinfor.servicedata.bookshopdataservice.IOperaterProduct0200030001;
import com.iskinfor.servicedata.bookshopdataserviceimpl.OperaterProduct020003001Impl;
import com.iskinfor.servicedata.pojo.Product;

public class ShoppingTest {

	/**
	 * @param args
	 * @Author Aaron Lee
	 * @date 2011-7-12 下午02:05:31
	 */
	public static void main(String[] args) {
		IOperaterProduct0200030001 opearter = new OperaterProduct020003001Impl();
		ArrayList<Product> list = new ArrayList<Product>();
		//list.add(new Product());
		try {
			opearter.putBuyedProducetToShelf("0004", list);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
