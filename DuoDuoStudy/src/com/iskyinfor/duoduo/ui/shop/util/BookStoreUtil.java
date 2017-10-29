package com.iskyinfor.duoduo.ui.shop.util;

public class BookStoreUtil {

	/***
	 * 获得正确的需要显示的钱数；
	 * 可以扩展为所有数字的正确显示方式
	 * @param money
	 * @return
	 * @Author Aaron Lee
	 * @date 2011-8-7 下午02:53:45
	 */
	public static String getRealMoney(String money){
		if (money==null || money.equals("")){
			money="0";
		}
		try{
			if (money.charAt(0)=='.'){
				money="0"+money;
			}
		}catch(IndexOutOfBoundsException e){
			money="0";
		}
		return money;
	}
	
	/***
	 * 获得正确的需要显示的作者名；
	 * 可以扩展为所有文字的正确显示方式
	 * @param money
	 * @return
	 * @Author Aaron Lee
	 * @date 2011-8-7 下午02:53:45
	 */
	public static String getRealAuthor(String author){
		if(author==null || author.equals("")){
			author = "未知";
		}
		return author;
	}
}
