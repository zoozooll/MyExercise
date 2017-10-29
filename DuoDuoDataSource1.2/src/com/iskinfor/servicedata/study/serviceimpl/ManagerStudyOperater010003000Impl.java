package com.iskinfor.servicedata.study.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;

import com.dc.eai.data.CompositeData;
import com.dcfs.esb.client.ESBClient;
import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.datahelp.CdUtil;
import com.iskinfor.servicedata.datahelp.SysHeader;
import com.iskinfor.servicedata.study.service.IManagerStudyOperater0100030001;

public class ManagerStudyOperater010003000Impl implements
		IManagerStudyOperater0100030001 {
	/**
	 * @param userId
	 *            用户ID
	 * @param proId
	 *            商品ID
	 * @param InforArray
	 *            信息数组 MARK_ID STRING 书签号 NOTE_ID STRING 笔记号
	 * @param showLineNum
	 *            显示信息条数
	 * @param markContent
	 *            标签内容
	 * @param noteContent
	 *            笔记内容
	 * @param assInfor
	 *            评价内容
	 * @param odetType
	 *            操作类型
	 * @param reason
	 *            推荐、赠送理由
	 * @param operate_object
	 *            赠送、推荐目标
	 * @param note_total
	 *            笔记总数
	 * @param mark_total
	 *            书签总数
	 * @param ispublic
	 *            是否公开
	 * @return
	 */
	private Map<String, Object> operater0100030001(String scern, String userId,
			String proId, ArrayList<Map<String, Object>> InforArray,
			String showLineNum, String markContent, String noteContent,
			String assInfor, String odetType, String reason,
			String[] operate_object, int note_total, int mark_total,
			String ispublic) {
		Map<String, Object> resulut = new HashMap<String, Object>();
		// 组装请求头
		CompositeData header = CdUtil.sysHeaderInstance("0100030001", scern); // header
		// 组装请求参数

		// 用户id
		Map<String, Object> args = new HashMap<String, Object>();
		if (null != userId && !"".equals(userId)) {
			args.put("USER_ID", userId);
		}
		// 商品的id
		if (null != userId && !"".equals(proId)) {
			args.put("PRO_ID", proId);
		}

		List<Map<String, Object>> inforList = new ArrayList<Map<String, Object>>();
		// 信息数组
		if (InforArray != null && InforArray.size() != 0) {
			for (int i = 0; i < InforArray.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				String markid = (String) InforArray.get(i).get("MARK_ID");
				if (null != markid && !"".equals(markid)) {
					map.put("MARK_ID", markid);
				}
				String noteId = (String) InforArray.get(i).get("NOTE_ID");
				if (null != noteId && !"".equals(noteId)) {
					map.put("NOTE_ID", noteId);
				}
				inforList.add(map);
			}
		}

		args.put("INFO_ARRAY", inforList);
		// 显示行数
		if (null != showLineNum && !"".equals(showLineNum)) {
			args.put("SHOW_LINE_NUM", showLineNum);
		} else {
			args.put("SHOW_LINE_NUM", "12");
		}
		// 标签内容
		if (null != markContent && !"".equals(markContent)) {
			args.put("MARK_CONTENT", markContent);
		}
		// 笔记内容
		if (null != noteContent && !"".equals(noteContent)) {
			args.put("NOTE_CONTENT", noteContent);
		}
		// 评价内容
		if (null != assInfor && !"".equals(assInfor)) {
			args.put("ASSESSMENT_INFO", assInfor);
		}
		// 操作类型
		if (null != odetType && !"".equals(odetType)) {
			args.put("OPERATE_TYPE", odetType);
		}
		// 推荐、赠送理由
		if (null != reason && !"".equals(reason)) {
			args.put("REASON", reason);
		}

		// /推荐、赠送的目标
		if (null != operate_object && operate_object.length>0) {
			List<Map<String, Object>> sendObjectList = new ArrayList<Map<String, Object>>();
			Map<String, Object> sendMaps = new HashMap<String, Object>();
			for(int i=0;i<operate_object.length;i++){
			String id=operate_object[i];
			sendMaps.put("USER_ID", id);
			sendObjectList.add(sendMaps);
			}

			args.put("OPERATE_OBJECT", sendObjectList);
		}
		// 笔记总数
		if (note_total > 0) {
			args.put("NOTE_TOTAL", note_total);
		}
		// 书签总数
		if (mark_total > 0) {
			args.put("MARK_TOTAL", mark_total);
		}
		// 是否公开
		// if(ispublic!=null&&!"".equals(ispublic)){
		// args.put("PUBLIC", ispublic);
		// }
		// 组成请求
		CompositeData body = CdUtil.bodyInstance(args);
		// 封装请求报文
		CompositeData reqData = CdUtil.reqData(header, body);
		try {
			CompositeData rspData = null;
			try {
				rspData = ESBClient.request01(reqData);
				System.out.println("rspData==>" + rspData);
			} catch (Exception e) {
				System.out.println("Exception==>" + e);
				e.printStackTrace();
				// throw e;
			}

			SysHeader sysHeader = CdUtil.verifyHeader(rspData);
			CompositeData cbody = CdUtil.bodyInstance(args);

			if (SysHeader.STATUS.SUCCESS == sysHeader.getStatu()
					&& "000000".equals(sysHeader.getRet().get(0).getCode())) {
				// 成功操作
				resulut.put(DataConstant.OPERAT_RESULT_KEY, 1);
				if ("03".equals(scern) || "04".equals(scern)) {
					if ("03".equals(scern)) {
						int mark_tital = (Integer) cbody.getField("MARK_TOTAL")
								.getValue();
						resulut.put(DataConstant.MARK_TOTAL_KEY, mark_tital);
					} else {
						int note_tital = (Integer) cbody.getField("NOTE_TOTAL")
								.getValue();
						resulut.put(DataConstant.NOTE_TOTAL_KEY, note_tital);
					}
					String pro_id = (String) cbody.getField("PRO_ID")
							.getValue();
					resulut.put(DataConstant.PRODUCT_ID_KEY, pro_id);

				}
			} else {
				// 失败操作
				resulut.put(DataConstant.OPERAT_FAILED_REASON,sysHeader.getRet().get(0).getMsg());
				resulut.put(DataConstant.OPERAT_RESULT_KEY, 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resulut;
	}

	public boolean deleteBookMark(String userId, String bookMarkId)
			throws Exception {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("MARK_ID", bookMarkId);
		ArrayList<Map<String, Object>> inforList = new ArrayList<Map<String, Object>>();
		inforList.add(args);
		Map<String, Object> result = operater0100030001("01", userId, null,
				inforList, null, null, null, null, null, null, null, 0, 0, null);
		int operateResult = (Integer) result
				.get(DataConstant.OPERAT_RESULT_KEY);
		if (operateResult == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteNote(String userId, String noteId) throws Exception {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("NOTE_ID", noteId);
		ArrayList<Map<String, Object>> inforList = new ArrayList<Map<String, Object>>();
		inforList.add(args);
		Map<String, Object> result = operater0100030001("02", userId, null,
				inforList, null, null, null, null, null, null, null, 0, 0, null);
		int operateResult = (Integer) result
				.get(DataConstant.OPERAT_RESULT_KEY);
		if (operateResult == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean addBookMark(String userId, String bookMarkContext)
			throws Exception {
		Map<String, Object> result = operater0100030001("03", userId, null,
				null, null, bookMarkContext, null, null, null, null, null, 0,
				0, null);
		int operateResult = (Integer) result
				.get(DataConstant.OPERAT_RESULT_KEY);
		if (operateResult == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean addNotes(String userId, String noteContext) throws Exception {

		Map<String, Object> result = operater0100030001("04", userId, null,
				null, null, null, noteContext, null, null, null, null, 0, 0,
				null);
		int operateResult = (Integer) result
				.get(DataConstant.OPERAT_RESULT_KEY);
		if (operateResult == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean readedBook(String userId) throws Exception {
		Map<String, Object> result = operater0100030001("05", userId, null,
				null, null, null, null, null, null, null, null, 0, 0, null);
		int operateResult = (Integer) result
				.get(DataConstant.OPERAT_RESULT_KEY);
		if (operateResult == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean assProduct(String userId, String assInfor) throws Exception {
		Map<String, Object> result = operater0100030001("06", userId, null,
				null, null, null, null, assInfor, null, null, null, 0, 0, null);
		int operateResult = (Integer) result
				.get(DataConstant.OPERAT_RESULT_KEY);
		if (operateResult == 1) {
			return true;
		} else {
			return false;
		}
	}

	public String giveBookToOther(String userId, String proId,
			String operateType, String reason, String[] object, String ispublic)
			throws Exception {
		Map<String, Object> result = operater0100030001("07", userId, proId,
				null, null, null, null, null, operateType, reason, object, 0,
				0, ispublic);
		int operateResult = (Integer) result
				.get(DataConstant.OPERAT_RESULT_KEY);
		if (operateResult == 1) {
			return "";
		} else {
			return (String) result.get(DataConstant.OPERAT_FAILED_REASON);
		}
	}

	public String recommProduct(String userId, String proId,
			String operateType, String reason, String[] object, String ispublic)
			throws Exception {
		Map<String, Object> result = operater0100030001("08", userId, proId,
				null, null, null, null, null, operateType, reason, object, 0,
				0, ispublic);
		int operateResult = (Integer) result
				.get(DataConstant.OPERAT_RESULT_KEY);
		if (operateResult == 1) {
			return "";
		} else {
			return (String) result.get(DataConstant.OPERAT_FAILED_REASON);
		}
	}

}
