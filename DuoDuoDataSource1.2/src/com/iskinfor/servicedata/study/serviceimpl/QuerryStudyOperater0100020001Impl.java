package com.iskinfor.servicedata.study.serviceimpl;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dcfs.esb.client.ESBClient;
import com.dcfs.esb.client.exception.TimeoutException;
import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.bookshopdataservice.IQurryProductInfor0200020002;
import com.iskinfor.servicedata.bookshopdataserviceimpl.QurryProductInfor0200020002Impl;
import com.iskinfor.servicedata.datahelp.CdUtil;
import com.iskinfor.servicedata.pojo.BookMark;
import com.iskinfor.servicedata.pojo.BookReaded;
import com.iskinfor.servicedata.pojo.BookShelf;
import com.iskinfor.servicedata.pojo.GiftInfo;
import com.iskinfor.servicedata.pojo.Product;
import com.iskinfor.servicedata.pojo.ReadingNotes;
import com.iskinfor.servicedata.pojo.RecommentInfor;
import com.iskinfor.servicedata.pojo.StudyAssessment;
import com.iskinfor.servicedata.pojo.User;
import com.iskinfor.servicedata.pojo.Winner;
import com.iskinfor.servicedata.study.service.IQuerryStudyOperater0100020001;
import com.iskinfor.servicedata.usercenter.service.IQuerryUserInfor0300020001;
import com.iskinfor.servicedata.usercenter.serviceimpl.QuerryUserInfor0300020001Impl;

public class QuerryStudyOperater0100020001Impl implements
		IQuerryStudyOperater0100020001 {

	@Override
	public ArrayList<BookReaded> getReadedBook(String userid,
			String showLineNum, String proId, String orderby, int PageNum,
			String operatetype, String courser, String grade, String user,
			String resource, String proType) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	public ArrayList<BookReaded> getReadedBook(String userid,
			String showLineNum, String proId, String orderby, int PageNum,String operatetype,
			String courser, String grade, String user, String resource,
			String proTyp,String inputInfo) throws Exception {
		Map<String, Object> result = Operater0100020001("01", userid,
				showLineNum, proId, orderby, PageNum, operatetype, courser, grade, user,
				resource, proTyp,inputInfo);
		return (ArrayList<BookReaded>) result
				.get(DataConstant.READED_BOOK_LIST_KEY);
	}

	/**
	 * 用户可以查看自己的一些记录（看过的书籍、做的笔记书签以及对书籍的评价）以及推荐和赠送。
	 * 
	 * @param scern场景
	 *            scern=01：看过的书。 scern=02：看书做的书签。 scern=03：看书做的笔记。
	 *            scern=04：学习评价。 scern=05：书架信息查询。 scern=06：赠送信息查询。
	 *            scern=07：推荐信息查询。 scern=08：学习榜。
	 * @param userid
	 *            用户的ID
	 * @param showLineNum
	 *            每页显示的页数
	 * @param proId
	 *            产品的id
	 * @param orderby
	 *            排序方式 00：按照日期倒序排序
	 * @param PageNum
	 *            页数显示的第几页信息     
	 * @param operatetype           
	 *             00推荐 01赠送  0600推荐 0700被推荐   0601赠送 0701被赠送        
	 * @param courser
	 *            科目 科目包括00:语文01:英语02:数学03:物理04:化学05:科学
	 * @param grade
	 *            年级
	 *            包括00:一年级01:二年级02:三年级03:四年级04:五年级05:六年级06:小学通用07:初一08:初二09:初三10
	 *            :初中通用
	 * @param user
	 *            用途 00：课件01：书籍02：习题03：考题
	 * @param resource
	 *            来源 来源包括00：学校01：个人02：出版社03：培训机构
	 * @param proType
	 *            商品类型 00:书籍01:课件02:习题03：考卷
	 * @return
	 */
	private Map<String, Object> Operater0100020001(String scern, String userid,
			String showLineNum, String proId, String orderby, int PageNum,String operatetype,
			String courser, String grade, String user, String resource,
			String proType,String inputInfo) {
		// 返回的结果
		Map<String, Object> result = new HashMap<String, Object>();
		// 请求头
		CompositeData header = CdUtil.sysHeaderInstance("0100020001", scern);
		// 封装请求参数
		Map<String, Object> args = new HashMap<String, Object>();
		// 将用户ID放入请求参数中
		if (null != userid && !"".equals(userid)) {
			args.put("USER_ID", userid);
		}
		// 将每页显示的条目数放入请求参数中
		if (null != showLineNum && !"".equals(showLineNum)) {
			args.put("SHOW_LINE_NUM", showLineNum);
		} else {
			args.put("SHOW_LINE_NUM", "12");
		}
		// 将商品ID放入到请求参数中
		if (null != proId && !"".equals(proId)) {
			args.put("PRO_ID", proId);
		}
		// 将排序方式放入到请求参数中
		if (null != orderby && !"".equals(orderby)) {
			args.put("ORDER_BY", orderby);
		}
		// 将显示的页码放入请求参数中
		if (0 < PageNum) {
			args.put("PAGE_NUM", PageNum);
		}
		// 推荐还是赠送 00推荐 01赠送  0600推荐 0700被推荐   0601赠送 0701被赠送
		if (null != operatetype && !"".equals(operatetype)) {
			args.put("OPERATE_TYPE", operatetype);
		}
		// 将科目放入到请求参数中
		if (null != courser && !"".equals(courser)) {
			args.put("COURSE", courser);
		}
		// 将年纪放入到请求参数中
		if (null != grade && !"".equals(grade)) {
			args.put("GRADE", grade);
		}
		// 将用途放入到请求参数中
		if (null != user && !"".equals(user)) {
			args.put("USE", user);
		}
		// 将来源放入请求参数中
		if (null != resource && !"".equals(resource)) {
			args.put("RESOURCE", resource);
		}
		// 将商品类型放入到请求参数中
		if (null != proType && !"".equals(proType)) {
			args.put("PRO_TYPE", proType);
		}
		// 将搜索放入到请求参数中
		if (null != inputInfo && !"".equals(inputInfo)) {
			args.put("INPUT_INFOR", inputInfo);
		}
		// 根据请求参数Map生成Body
		CompositeData body = CdUtil.bodyInstance(args);
		// 封装请求报文
		CompositeData reqData = CdUtil.reqData(header, body);

		try {
			CompositeData rspData = null;
			try {
				rspData = ESBClient.request01(reqData);
				System.out.println("PLJ==>rspData:"+rspData);
			} catch (Exception e) {

				throw e;
			}
			CompositeData cbody = CdUtil.getBody(rspData);
			if ("01".equals(scern)) {
				returnReadedBook(result, cbody);
			} else if ("02".equals(scern)) {
				returnBookMark(result, cbody);

			} else if ("03".equals(scern)) {
				returnReadingNotesArray(result, cbody);
			} else if ("04".equals(scern)) {
				returnAssessmentArray(result, cbody);
			} else if ("05".equals(scern)) {
//				每页显示条目数
				int showLines;
				if(!"".equals(showLineNum)&&null!=showLineNum){
					showLines=Integer.valueOf(showLineNum);
				}else{
					showLines=12;
				}
//				总条目数
				int  totNum=(Integer) cbody.getField("TOTAL_NUM").getValue();
				result.put(DataConstant.TOTAL_NUM, totNum);
				System.out.println("totNum==>>"+totNum);
//				总页数
				int pags;
				if(totNum%showLines>0){
					pags=totNum/showLines+1;
				}else{
					pags=totNum/showLines;
				}
				System.out.println("pags==>>"+pags);
				result.put(DataConstant.TOTAL_PAGS, pags);
			
				returnMyShelfData(result, cbody);
			} else if ("06".equals(scern) || "07".equals(scern)) {
				
				returnRecommendInfoArray(result, cbody, showLineNum);
				
			} else if ("08".equals(scern)) {
				returnWinnerList(result, cbody);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void returnWinnerList(Map<String, Object> result,
			CompositeData cbody) {
		Array uArray = cbody.getArray("WINNER_LIST");
		ArrayList<Winner> winnerList = new ArrayList<Winner>();
		for (int i = 0; i < uArray.size(); i++) {
			CompositeData cd = uArray.getStruct(i);
			Winner winner = new Winner();
			// 获奖者姓名
			String userName = (String) cd.getField("USER_NAME")
					.getValue();
			if (!"".equals(userName) && null != userName) {
				winner.setUserName(userName);
			}
			// 获奖日期
			String date = (String) cd.getField("AWARD_DATE").getValue();
			if (!"".equals(date) && null != date) {
				winner.setAwardDate(date);
			}
			// 获奖课程
			String coruse = (String) cd.getField("AWARD_COURSE")
					.getValue();
			if (!"".equals(coruse) && null != coruse) {
				winner.setAwardCourse(coruse);
			}
			// 获奖情况
			String infor = (String) cd.getField("WINNER_INFOR")
					.getValue();
			if (!"".equals(infor) && null != infor) {
				winner.setWinnerInfor(infor);
			}
			// 总条数
			int pag = (Integer) cd.getField("PGS").getValue();
			if (!(pag < 0)) {
				winner.setPags(pag);
			}
			winnerList.add(winner);
		}
		result.put(DataConstant.WINNER_KEY, winnerList);
	}

	private void returnGiftInfoArray(Map<String, Object> result,
			CompositeData cbody) {		
		Array uArray = cbody.getArray("COMMENT_ARRAY");
		System.out.println("彭两件01==>uArray.size()"+uArray.size());
		ArrayList<GiftInfo> giftList = new ArrayList<GiftInfo>();
		for (int i = 0; i < uArray.size(); i++) {
			GiftInfo giftInfo = new GiftInfo();
			CompositeData cd = uArray.getStruct(i);
			// 用户数组
			String[] userArray = (String[]) cd.getField("USER_ARRAY")
					.getValue();
			if (null != userArray && !"".equals(userArray)) {
				giftInfo.setUserArray(userArray);
			}
			//用户ID
			String userId = (String) cd.getField("USER_ID")
					.getValue();
			if (null != userId && !"".equals(userId)) {
				giftInfo.setUserId(userId);
			}
			//时间
			String giftDate = (String) cd.getField("GIFT_DATE").getValue();
			if (null != giftDate && !"".equals(giftDate)) {
				giftInfo.setGiftDate(giftDate);
			}
			//原因
			String reason = (String) cd.getField("REASON").getValue();
			if (!(null!= reason&&!"".equals(reason))) {
				giftInfo.setReason(reason);
			}
			//商品ID
			String proId =(String) cd.getField("PRO_ID").getValue();
			if(!(null!=proId&&!"".equals(proId)))
			{giftInfo.setProId(proId);}
			//商品名称
			String proName =(String) cd.getField("PRO_NAME").getValue();
			if(!(null!=proId&&!"".equals(proName)))
			{giftInfo.setProName(proName);}	
			//商品小图片
			String smallImgPath =(String) cd.getField("SMALL_IMG_PATH").getValue();
			if(!(null!=smallImgPath&&!"".equals(smallImgPath)))
			{giftInfo.setSmallImgPath(smallImgPath);}	
			//总条数
			int totalNum =(Integer) cd.getField("TOTAL_NUM").getValue();
			if(!(totalNum<0))
			{giftInfo.setTotalNum(totalNum);}	
			
			giftList.add(giftInfo);
		}
		result.put(DataConstant.GIFT_INFOR, giftList);
	}
//	我的赠送
	private void returnRecommendInfoArray(Map<String, Object> result,
			CompositeData cbody,String showLineNum) {
		Array uArray = cbody.getArray("COMMENT_ARRAY");
	
//		总条目数
		int  totNum=0;
		try{
			totNum=(Integer) cbody.getField("TOTAL_NUM").getValue();
			result.put(DataConstant.TOTAL_NUM, totNum);
			if(showLineNum.equals("")){showLineNum="12";}			
		}
		catch(Exception e){}
//		总页数
		int showLines=Integer.valueOf(showLineNum).intValue(); 
		int pags;
		if(totNum%showLines>0){
			pags=totNum/showLines+1;
		}else{
			pags=totNum/showLines;
		}
		result.put(DataConstant.TOTAL_PAGS, pags);
	
		IQuerryUserInfor0300020001  querryuser=new QuerryUserInfor0300020001Impl(); 
		ArrayList<RecommentInfor> recommList = new ArrayList<RecommentInfor>();
		for (int i = 0; i < uArray.size(); i++) {
			RecommentInfor rInfor = new RecommentInfor();
			CompositeData cd = uArray.getStruct(i);
			// 用户id
			try {
			String userId = (String) cd.getField("OBJECT_ID").getValue();
			
			if (null != userId && !"".equals(userId)) {
				rInfor.setUserId(userId);
			}
			 String[] userIds = userId.split(","); 	
//			 String strids="";
//			 for(int ii=0;ii<userIds.length; ii++)
//			 {strids+=userIds[ii]+"  ";}
//			 System.out.println("PLJ==>strids:"+strids);
			 
			 Map<String, Object> map=querryuser.BatchUserQuery("08","USERS_ARRAY","0002",userIds);
			 ArrayList<User> userArray=(ArrayList<User>)map.get(DataConstant.USER_ARRAY_KEY);
			 String userNames="";
			 for(int j=0;j<userArray.size();j++)
			 {userNames+=userArray.get(j).getUserName().toString()+",";}
			 userNames = userNames.substring(0, userNames.length()-1);
			 rInfor.setUserName(userNames);
			}catch (Exception e) {
				e.printStackTrace(); }

			// 书ID
			try{
				String proId = (String) cd.getField("PRO_ID")
						.getValue();
				if (null != proId && !"".equals(proId)) {
					rInfor.setProId(proId);
				}
			}catch (Exception e) {
				e.printStackTrace();}
			try{
				String proName = (String) cd.getField("PRO_NAME")
				.getValue();
				if (null != proName && !"".equals(proName)) {
					rInfor.setProName(proName);
				}
			}catch (Exception e) {
				e.printStackTrace();}
			try{
				String smallImgPath = (String) cd.getField("SMALL_IMG_PATH")
				.getValue();
				if (null != smallImgPath && !"".equals(smallImgPath)) {
					rInfor.setProSmallPic(smallImgPath);
				}
			}catch (Exception e) {
				e.printStackTrace();}
			// 时间
			try{
			String reDate = (String) cd.getField("DATE")
					.getValue();
			if (null != reDate && !"".equals(reDate)) {
				rInfor.setRecommendDate(reDate);
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
			// 原因
			try{
			String reason = (String) cd.getField("REASON").getValue();
			if (null != reason && !"".equals(reason)) {
				rInfor.setReason(reason);
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
            
			recommList.add(rInfor);
		}
		
		result.put(DataConstant.RECOMMENT_INFOR, recommList);
	}

	private void returnMyShelfData(Map<String, Object> result,
			CompositeData cbody) {
		Array uArray = cbody.getArray("PRO_ARRAY");
		ArrayList<BookShelf> bookShelfList = new ArrayList<BookShelf>();
		String[] proids=new String[uArray.size()];
		for (int i = 0; i < uArray.size(); i++) {
			BookShelf bookShelf = new BookShelf();
			CompositeData cd = uArray.getStruct(i);
			// 商品ID
			String proId5 = (String) cd.getField("PRO_ID").getValue();
			if (null != proId5 && !"".equals(proId5)) {
				bookShelf.setProId(proId5);
				proids[i]=proId5;
			}
//					商品赠送状态
			try{
			String donateState=(String)cd.getField("DONATE_SATAE").getValue();
			if (null != donateState && !"".equals(donateState)) {
				bookShelf.setDonoteState(donateState);
			}}catch(Exception e){
				e.printStackTrace();
			}
//					设置书签总数
			try{
			String markTotal=(String)cd.getField("MARK_TOTAL").getValue();
			if (null != markTotal && !"".equals(markTotal)) {
				bookShelf.setTotalMark(markTotal);
			}
			}catch(Exception e){
				e.printStackTrace();
				
			}
//					推荐获赠送状态
			try{
			String recommend=(String)cd.getField("RECOMMEND_SATAE").getValue();
			if (null != recommend && !"".equals(recommend)) {
				bookShelf.setRecommendState(recommend);
			}}catch(Exception e){
				e.printStackTrace();
			}
//					最后的阅读时间
			try{
			String readDate=(String)cd.getField("READ_DATE").getValue();
			if (null != readDate && !"".equals(readDate)) {
				bookShelf.setLastReadData(readDate);
			}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
			String proNum=(String)cd.getField("PRO_NUM").getValue();
			if (null != proNum && !"".equals(proNum)) {
				bookShelf.setBookNum(Integer.valueOf(proNum));
			}
			}catch(Exception e){
				e.printStackTrace();
			}
			 //书架的id
			 try{
			 String shelfId=(String) cd.getField("SHELF_ID") .getValue();
			 if(!"".equals(shelfId)&&null!=shelfId){
			 bookShelf.setShiefId(shelfId);
			 }
			 }catch(Exception e){
				 e.printStackTrace();
			 }
			// 笔记总数
			 try{
			 String totalNote = (String) cd.getField("NOTE_TOTAL")
			 .getValue();
			 if (null != totalNote && !"".equals(totalNote)) {
			 bookShelf.setTotalNote(totalNote);
			 }}catch(Exception e){
				 e.printStackTrace();
			 }
			
		
			 
			 
			bookShelfList.add(bookShelf);
		}
		Map<String,Object> resMaps=getProductById(proids);
		for(int i=0;i<bookShelfList.size();i++){
			BookShelf bookShelf=bookShelfList.get(i);
			String id=bookShelf.getProId();
			bookShelf.setProduct((Product) resMaps.get(id));
		}
		
		result.put(DataConstant.SHELF_LIST_KEY, bookShelfList);
	} 

	private void returnAssessmentArray(Map<String, Object> result,
			CompositeData cbody) {
		Array uArray = cbody.getArray("ASSESSMENT_ARRAY");
		ArrayList<StudyAssessment> assList = new ArrayList<StudyAssessment>();
		for (int i = 0; i < uArray.size(); i++) {
			StudyAssessment ass = new StudyAssessment();
			CompositeData cd = uArray.getStruct(i);
			// 评价信息
			String assInfo = (String) cd.getField("ASSESSMENT_INFO").getValue();
			if (null != assInfo && !"".equals(assInfo)) {
				ass.setAssessment_info(assInfo);
			}
			// 评价时间
			String assData = (String) cd.getField("ASSESSMENT_DATE").getValue();
			if (null != assData && !"".equals(assData)) {
				ass.setAssessment_data(assData);
			}
			// 评论条数
			int assNum = (Integer) cd.getField("ASSESSMENT_NUM").getValue();
			if (!(assNum < 0)) {
				ass.setAssessment_num(assNum);
			}
			// 总条数
			int pags = (Integer) cd.getField("PGS").getValue();
			if (!(pags < 0)) {
				ass.setPgs(pags);
			}
			assList.add(ass);
		}
		result.put(DataConstant.ASS_KEY, assList);
	}

	private void returnReadingNotesArray(Map<String, Object> result,
			CompositeData cbody) {
		Array uArray = cbody.getArray("READING_NOTES_ARRAY");
		String[] proids = new String[uArray.size()];
		ArrayList<ReadingNotes> readingNotesList = new ArrayList<ReadingNotes>();
		for (int i = 0; i < uArray.size(); i++) {
			ReadingNotes notes = new ReadingNotes();
			CompositeData cd = uArray.getStruct(i);
			// 商品ID
			String proId3 = (String) cd.getField("PRO_ID").getValue();
			if (null != proId3 && !"".endsWith(proId3)) {
				notes.setProId(proId3);
				proids[i] = proId3;
			}
			// 记笔记的日期
			String noteData = (String) cd.getField("NOTE_DATE").getValue();
			if (null != noteData && !"".equals(noteData)) {
				notes.setNoteData(noteData);
			}
			// 那一页做笔记
			int notePage = (Integer) cd.getField("NOTE_PAGE").getValue();
			if (!(notePage < 0)) {
				notes.setNotePage(notePage);
			}
			// 笔记内容
			String noteContent = (String) cd.getField("NOTE_CONTENT")
					.getValue();
			if (null != noteContent && !"".equals(noteContent)) {
				notes.setNoteContent(noteContent);
			}
			// 总条数
			int pags = (Integer) cd.getField("PGS").getValue();
			if (!(pags < 0)) {
				notes.setPgs(pags);
			}
			readingNotesList.add(notes);
		}
		Map<String, Object> pMap = getProductById(proids);
		for (int i = 0; i < readingNotesList.size(); i++) {
			ReadingNotes readNotes = readingNotesList.get(i);
			String id = readNotes.getProId();
			readNotes.setProduct((Product) pMap.get(id));
		}
		result.put(DataConstant.READING_NOTES_KEY, readingNotesList);
	}

	private void returnBookMark(Map<String, Object> result, CompositeData cbody) {
		// 书签书名
		Field bookName = cbody.getField("BOOK_NAME");
		result.put("BOOK_NAME", bookName);
		// 作者
		Field auther = cbody.getField("AUTHER");
		result.put("AUTHER", auther);
		// 书的简介
		Field bookIntro = cbody.getField("BOOK_INTRO");
		result.put("BOOK_INTRO", bookIntro);

		Array uArray = cbody.getArray("MARK_ARRAY");
		ArrayList<BookMark> bookMarkList = new ArrayList<BookMark>();
		for (int i = 0; i < uArray.size(); i++) {
			BookMark bookMark = new BookMark();
			CompositeData cd = uArray.getStruct(i);
			// 书签页码
			int makepage = (Integer) cd.getField("MARK_PAGE").getValue();
			if (!(makepage < 0)) {
				bookMark.setMakePage(makepage);
			}
			// 书签内容
			String makeContent = (String) cd.getField("MARK_CONTENT")
					.getValue();
			if (!"".equals(makeContent) && null != makeContent) {
				bookMark.setMarkContent(makeContent);
			}
			// 书签日期
			String makeDate = (String) cd.getField("MARK_DATE").getValue();
			if (!"".equals(makeDate) && null != makeDate) {
				bookMark.setMakeDate(makeDate);
			}
			int page = (Integer) cd.getField("PGS").getValue();
			// 总条数
			if (!(makepage < 0)) {
				bookMark.setPgs(page);
			}
			// 将得到的书签对象插入到书签列表中
			bookMarkList.add(bookMark);
		}
		result.put(DataConstant.BOOK_MARKS_KEY, bookMarkList);
	}

	private void returnReadedBook(Map<String, Object> result,
			CompositeData cbody) {
		Array uArray = cbody.getArray("READED_BOOK_ARRAY");
		ArrayList<BookReaded> readedBookList = new ArrayList<BookReaded>();
		String[] proids = new String[uArray.size()];
		for (int i = 0; i < uArray.size(); i++) {
			BookReaded bookRead = new BookReaded();
			CompositeData cData = uArray.getStruct(i);
			String proid = (String) cData.getField("PRO_NAME").getValue();
			if (!"".equals(proid) && null != proid) {
				bookRead.setProId(proid);
				// 根据proID得到书籍的详细信息，并将其添加到bookRead对象中
				proids[i] = proid;
			}
			String totalNote = (String) cData.getField("TOTAL_NOTE").getValue();
			if (!"".equals(totalNote) && null != totalNote) {
				bookRead.setTotalNote(totalNote);
			}
			String bookUserId = (String) cData.getField("USER_ID").getValue();
			if (!"".equals(bookUserId) && null != bookUserId) {
				bookRead.setUserId(bookUserId);
			}
			readedBookList.add(bookRead);
		}
		Map<String, Object> proMap = getProductById(proids);
		for (int i = 0; i < readedBookList.size(); i++) {
			BookReaded bookread = readedBookList.get(i);
			String id = bookread.getProId();
			bookread.setProduct((Product) proMap.get(id));
		}
		result.put(DataConstant.READED_BOOK_LIST_KEY, readedBookList);
	}

	private Map<String, Object> getProductById(String[] proids) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		IQurryProductInfor0200020002 querryProductInfor = new QurryProductInfor0200020002Impl();
		try {
			Map<String, Object> maps = querryProductInfor
					.getProductById(proids);
			
			ArrayList<Product> pList = (ArrayList<Product>) maps
					.get(DataConstant.LIST);
			if (null != pList && 0 != pList.size()) {
				for (int i = 0; i < pList.size(); i++) {
					Product product = pList.get(i);
					String proId = product.getProId();
					resultMap.put(proId, product);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
	}

	public Map<String, Object> getMyBookMark(String userid, String showLineNum,
			String proId, String orderby, int PageNum,String operatetype, String courser,
			String grade, String user, String resource, String proTyp,String inputInfo)
			throws Exception {
		// TODO Auto-generated method stub
		return Operater0100020001("02", userid, showLineNum, proId, orderby,
				PageNum,operatetype, courser, grade, user, resource, proTyp,inputInfo);
	}

	public ArrayList<ReadingNotes> getReadingNotes(String userid,
			String showLineNum, String proId, String orderby, int PageNum,String operatetype,
			String courser, String grade, String user, String resource,
			String proType,String inputInfo) throws Exception {
		Map<String, Object> result = Operater0100020001("03", userid,
				showLineNum, proId, orderby, PageNum, operatetype,courser, grade, user,
				resource, proType,inputInfo);
		return (ArrayList<ReadingNotes>) result
				.get(DataConstant.READING_NOTES_KEY);
	}

	public ArrayList<StudyAssessment> getStudyAssessment(String userid,
			String showLineNum, String proId, String orderby, int PageNum,String operatetype,
			String courser, String grade, String user, String resource,
			String proType,String inputInfo) throws Exception {
		Map<String, Object> result = Operater0100020001("04", userid,
				showLineNum, proId, orderby, PageNum,operatetype, courser, grade, user,
				resource, proType,inputInfo);
		return (ArrayList<StudyAssessment>) result.get(DataConstant.ASS_KEY);
	}

	public Map<String,Object> getBookShelf(String userid, String showLineNum,
			String proId, String orderby, int PageNum, String operatetype,String courser,
			String grade, String user, String resource, String proType,String inputInfo)
			throws Exception {
		 return Operater0100020001("05", userid,
				showLineNum, proId, orderby, PageNum,operatetype, courser, grade, user,
				resource, proType,inputInfo);

	}


///

	@Override
	public Map<String, Object> getMyBookMark(String userid, String showLineNum,
			String proId, String orderby, int PageNum,String operatetype,String courser,
			String grade, String user, String resource, String proType)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<ReadingNotes> getReadingNotes(String userid,
			String showLineNum, String proId, String orderby, int PageNum,String operatetype,
			String courser, String grade, String user, String resource,
			String proType) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<StudyAssessment> getStudyAssessment(String userid,
			String showLineNum, String proId, String orderby, int PageNum,String operatetype,
			String courser, String grade, String user, String resource,
			String proType) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String,Object> getRecommentInfor(String scern,
			String userid, String showLineNum, String proId, String orderby,
			int PageNum, String operatetype,String courser, String grade, String user,
			String resource, String proType,String inputInfo) throws Exception {
		Map<String, Object> result = Operater0100020001(scern, userid,
				showLineNum, proId, orderby, PageNum, operatetype, courser, grade, user,
				resource, proType,inputInfo);
		return (Map<String,Object>) result;
//				.get(DataConstant.RECOMMENT_INFOR);
	}




}
