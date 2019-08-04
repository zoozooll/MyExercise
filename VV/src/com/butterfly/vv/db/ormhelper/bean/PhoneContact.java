package com.butterfly.vv.db.ormhelper.bean;

import java.util.List;

import com.beem.project.btf.service.Contact;
import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBOrder;
import com.butterfly.vv.db.ormhelper.DBHelper.DBOrder.DBOrderType;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.butterfly.vv.service.ContactService;
import com.j256.ormlite.field.DatabaseField;

/**
 * 保存已经上传的手机通讯录号码到数据库
 * @author xun zhong
 */
public class PhoneContact extends BaseDB {
	@DatabaseField(id = true)
	private String phoneNum;
	@DatabaseField
	private String name;
	@DatabaseField
	private String nameOld;
	@DatabaseField
	private PhoneNumWhere phoneNumWhere;
	@DatabaseField
	private PhoneNumState phoneNumState = PhoneNumState.add;
	@DatabaseField
	private PhoneNumRelation phoneNumRelation = PhoneNumRelation.unregister;
	@DatabaseField
	private long birthTime;

	public enum PhoneNumState {
		add, uploaded, modify, remove;
	}

	public enum PhoneNumRelation {
		unregister, register;
	}

	public enum PhoneNumWhere {
		phone, sim, all;
	}

	public PhoneContact() {
	}
	@Override
	public String toString() {
		return "PhoneContact [phoneNum=" + phoneNum + ", name=" + name
				+ ", nameOld=" + nameOld + ", phoneNumWhere=" + phoneNumWhere
				+ ", phoneNumState=" + phoneNumState + ", phoneNumRelation="
				+ phoneNumRelation + "]";
	}
	@Override
	public void saveToDatabase() {
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.phoneNum, DBWhereType.eq, phoneNum));
	}
	public static void delete(String phoneNum) {
		DBHelper.getInstance().delete(PhoneContact.class,
				new DBWhere(DBKey.phoneNum, DBWhereType.eq, phoneNum));
	}
	public static List<PhoneContact> queryForAll() {
		return DBHelper.getInstance().queryAll(PhoneContact.class);
	}
	public static List<PhoneContact> querySomeUnregister(String phoneNum,
			int limit) {
		if (phoneNum == null) {
			phoneNum = "";
		}
		return DBHelper.getInstance().queryAll(
				PhoneContact.class,
				new DBOrder(DBKey.name, DBOrderType.asc),
				limit,
				new DBWhere[] {
						new DBWhere(DBKey.phoneNum, DBWhereType.gt, phoneNum),
						new DBWhere(DBKey.phoneNumRelation, DBWhereType.eq,
								PhoneNumRelation.unregister), });
	}
	public static PhoneContact queryForFirst(String phoneNum) {
		return DBHelper.getInstance().queryForFirst(PhoneContact.class,
				new DBWhere(DBKey.phoneNum, DBWhereType.eq, phoneNum));
	}
	//更改手机联系人的关系
	protected static void correctPhoneNumRelation() {
		List<PhoneContact> phoneContacts = DBHelper.getInstance().queryAll(
				PhoneContact.class,
				new DBWhere[] { new DBWhere(DBKey.phoneNumRelation,
						DBWhereType.eq, PhoneNumRelation.unregister) });
		if (phoneContacts != null) {
			for (PhoneContact phonecontactOne : phoneContacts) {
				Contact contact = ContactService.getInstance().getContact(
						phonecontactOne.phoneNum);
				if (contact != null) {
					phonecontactOne.setField(DBKey.phoneNumRelation,
							PhoneNumRelation.register);
					phonecontactOne.setField(DBKey.phoneNum,
							phonecontactOne.phoneNum);
					phonecontactOne.saveToDatabase();
				}
			}
		}
	}
}
