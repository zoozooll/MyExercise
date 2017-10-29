package com.iskinfor.servicedata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dcfs.esb.client.exception.TimeoutException;
import com.iskinfor.servicedata.bookshopdataservice.ISearchQuery0200020003;
import com.iskinfor.servicedata.bookshopdataserviceimpl.SearchQuery0200020003;
import com.iskinfor.servicedata.pojo.BookShelf;
import com.iskinfor.servicedata.pojo.Product;
import com.iskinfor.servicedata.pojo.RecommentInfor;
import com.iskinfor.servicedata.pojo.User;
import com.iskinfor.servicedata.study.service.IQuerryStudyOperater0100020001;
import com.iskinfor.servicedata.study.serviceimpl.QuerryStudyOperater0100020001Impl;
import com.iskinfor.servicedata.usercenter.service.IQuerryUserInfor0300020001;
import com.iskinfor.servicedata.usercenter.serviceimpl.QuerryUserInfor0300020001Impl;

public class Test {
	
	public static void main(String[] args){
         System.out.println("彭两件");
         
//         ISearchQuery0200020003 searchquery=new SearchQuery0200020003();
         IQuerryStudyOperater0100020001 querrystudy = new QuerryStudyOperater0100020001Impl();
         
         IQuerryUserInfor0300020001 userinfo=new QuerryUserInfor0300020001Impl();
         
         try {
			//Map<String, Object> map=searchquery.getBaseProduct("01", "PRO_ARRAY", "0002", "", "", "", "", 0, "", "", "", "", "", 0, "", "心灵鸡汤", "");
			//Map<String, Object> map=searchquery.getSearchInfo("05", "PRO_ARRAY", "0002", "", "", "", 0, "", "", 0, "", "凡通过改变");
//        	 Map<String, Object> map = bookshelfData.getBookShelf("0002", "12", "19","", 0, "", "", "", "01", "", "舒服返回不");
//        	 List<Map<String, Object>> results;
   //     	         	 Map<String, Object> result = userinfo.groupQuery("07","0002");
//        	 System.out.print("PLJ==分组查询:>"+result);
//        	 ArrayList<BookShelf> shelfData = (ArrayList<BookShelf>) map
//			.get(DataConstant.LIST);
//			System.out.println("PLJ==>大小："+shelfData.size());
			//System.out.println("PLJ==>name："+shelfData.get(0).getProduct().getProPrice());
        	 
        	 //赠送/推荐
        	 Map<String, Object> result= querrystudy.getRecommentInfor("06", "0002", "", "", "", 0, "00", "", "", "", "", "", "");
        	 
        	 ArrayList<RecommentInfor> shelfData = (ArrayList<RecommentInfor>) result.get(DataConstant.RECOMMENT_INFOR);
        	 int s=(Integer) result.get(DataConstant.TOTAL_NUM);
        	 
        	 System.out.print("PLJ==赠送/推荐:>"+shelfData.size());
        	 System.out.print("PLJ==赠送/推荐:>"+shelfData.get(0).getUserName());
        	 System.out.print("PLJ==大图:>"+shelfData.get(0).getProSmallPic());
        	 System.out.print("PLJ==小图:>"+shelfData.get(0).getProBigPic());
        	 System.out.print("PLJ==原因:>"+shelfData.get(0).getReason());
        	 System.out.print("PLJ==赠送/推荐sssss:>"+s);
//        	IQuerryUserInfor0300020001  querryuser=new QuerryUserInfor0300020001Impl();      	 
//        	User d= querryuser.querryUserBaseInfor("0006", "");
//        	System.out.print("PLJ==user:>"+d.getUserName());             
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	        
         
         
//         Map<String, Object> coutTeacherMap;
//         List<Map<String, Object>> coutTeachArray = new ArrayList<Map<String, Object>>();
//         
//         for(int i=0;i<4;i++)
//         {
//        	 coutTeacherMap = new HashMap<String, Object>();
//        	 coutTeacherMap.put("name", String.valueOf(i));
//        	 
//        	 coutTeachArray.add(coutTeacherMap);
//         }
//
//         System.out.println(coutTeachArray);
//         
	}
}
