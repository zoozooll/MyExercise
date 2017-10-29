package com.iskinfor.servicedata.study.service;

import com.dcfs.esb.client.exception.TimeoutException;

public interface IManagerStudyOperater0100030001 {
	/**
	 * 删除书签
	 * @param userId 用户的ID
	 * @param bookMarkId 书签的id
	 * @return true删除成功 false删除失败
	 * @throws Exception
	 */
	public boolean deleteBookMark(String userId,String bookMarkId) throws Exception;
	/**
	 * 删除笔记
	 * @param userId 用户的ID
	 * @param noteId 书签的id
	 * @return true删除成功 false删除失败
	 * @throws Exception
	 */
	public boolean deleteNote(String userId,String noteId) throws Exception;
	/**
	 * 添加书签
	 * @param userId 用户ID
	 * @param bookMarkContext 书签内容
	 * @return
	 * @throws Exception
	 */
	public boolean addBookMark(String userId,String bookMarkContext) throws  Exception;
	/**
	 * 添加笔记
	 * @param userId 用户ID
	 * @param noteContext 笔记内容
	 * @return
	 * @throws Exception
	 */
	public boolean addNotes(String userId,String noteContext) throws  Exception;
	/**
	 * 看过的书
	 * @param userId用户ID
	 * @return
	 * @throws Exception
	 */
	public boolean readedBook(String userId) throws  Exception;
	/**
	 * 商品评价
	 * @param userId用户ID
	 * @param assInfor评价信息
	 * @return
	 * @throws Exception
	 */
	public boolean assProduct(String userId,String assInfor) throws Exception;
	/**
	 * 商品赠送
	 * @param userId  用户id
	 * @param proId   商品id
	 * @param operateType 操作类型
	 * 00：公开赠送01：私密赠送02：推荐03：公开推荐；07 08场景使用
	 * @param reason 赠送理由
	 * @param object 赠送的目标的ID
	 * @param ispublic 是否公开,0公开,1非公开
	 * @return
	 * @throws Exception
	 */
	public String giveBookToOther(String userId,String proId,String operateType,String reason,String[] object,String ispublic) throws Exception;
	/**
	 * 商品推荐
	 * @param userId 用户id
	 * @param proId  商品id
	 * @param operateType操作类型
	 * 00：公开赠送01：私密赠送02：推荐03：公开推荐；07 08场景使用
	 * @param reason推荐理由
	 * @param object推荐的目标的ID
	 * @param ispublic是否公开,0公开,1非公开
	 * @return
	 * @throws Exception
	 */
	public String recommProduct(String userId,String proId,String operateType,String reason,String[] object,String ispublic) throws Exception;
}
