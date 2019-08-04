package com.btf.push;

import java.util.List;

import com.btf.push.base.BaseIQ;
import com.butterfly.vv.db.ormhelper.bean.PhoneContact;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.db.ormhelper.bean.PhoneContact.PhoneNumState;

public class PhoneContactPacket extends BaseIQ {
	public static final String element = "query";
	public static final String xmlns = "contact:phone";

	@Override
	public String getChildElementXML() {
		return toXml(new StringBuilder(), element, xmlns);
	}
	// 将pContact数据转成PhoneContactPacket，写入列表中
	public void pushPacktContact(List<PhoneContactPacket> pcPackets,
			PhoneContact pContact) {
		switch (pContact.getField(DBKey.phoneNumState, PhoneNumState.class)) {
			case modify: {
				PhoneContactPacket removePContact = new PhoneContactPacket();
				removePContact.setField(BaseIQKey.name,
						pContact.getField(DBKey.nameOld));
				removePContact.setField(BaseIQKey.phonenum,
						pContact.getField(DBKey.phoneNum));
				removePContact.setField(BaseIQKey.subscription,
						PhoneNumState.remove.toString());
				pcPackets.add(removePContact);
				PhoneContactPacket addPContact = new PhoneContactPacket();
				addPContact.setField(BaseIQKey.name,
						pContact.getField(DBKey.name));
				addPContact.setField(BaseIQKey.phonenum,
						pContact.getField(DBKey.phoneNum));
				addPContact.setField(BaseIQKey.subscription,
						PhoneNumState.add.toString());
				pcPackets.add(addPContact);
				break;
			}
			case add:
			case remove: {
				PhoneContactPacket phoneContact = new PhoneContactPacket();
				phoneContact.setField(BaseIQKey.name,
						pContact.getFieldStr(DBKey.name));
				phoneContact.setField(BaseIQKey.phonenum,
						pContact.getField(DBKey.phoneNum));
				phoneContact.setField(BaseIQKey.subscription, pContact
						.getField(DBKey.phoneNumState, PhoneNumState.class)
						.toString());
				pcPackets.add(phoneContact);
				break;
			}
			default:
				throw new IllegalArgumentException(
						"Error: pContact 's phoneNumSate must in [modify,add,remove],error val:"
								+ pContact.getField(DBKey.phoneNumState,
										PhoneNumState.class));
		}
	}
}
