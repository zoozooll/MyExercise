package com.butterfly.vv.db.ormhelper.bean;

import java.util.ArrayList;
import java.util.List;

import com.btf.push.NeighborHoodPacket.NeighborHoodType;
import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBOrder;
import com.butterfly.vv.db.ormhelper.DBHelper.DBOrder.DBOrderType;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.butterfly.vv.model.Start;
import com.j256.ormlite.field.DatabaseField;

/**
 * @ClassName: NeighborDB
 * @Description: 附近的人离线数据
 * @author: yuedong bao
 * @date: 2015-4-7 下午3:30:37
 */
public class UserNeighborDB extends BaseDB {
	@DatabaseField(id = true)
	private String id;
	@DatabaseField
	private String jid;
	@DatabaseField
	private String jid_neighbor;
	@DatabaseField
	private double distance;
	@DatabaseField
	protected NeighborHoodType neighborType;
	@DatabaseField
	protected long birthTime;
	@DatabaseField
	private String bday;
	@DatabaseField
	private double lon;
	@DatabaseField
	private double lat;
	@DatabaseField
	private String nickName;
	@DatabaseField
	private String portrait_small;
	@DatabaseField
	private String logintime;

	@Override
	public void saveToDatabase() {
		id = new StringBuffer().append(jid).append(jid_neighbor).toString();
		fields.put(DBKey.id.toString(), id);
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}
	/**
	 * @Title: queryAll
	 * @Description: 此处的offset传id
	 * @param: @param jid
	 * @param: @param limit
	 * @param: @param offSet
	 * @param: @param type
	 * @param: @return
	 * @return: List<UserNeighborDB>
	 * @throws:
	 */
	public static List<UserNeighborDB> queryAll(String jid, int limit,
			String offSet, NeighborHoodType type) {
		List<UserNeighborDB> retVal = new ArrayList<UserNeighborDB>();
		double start_distance = 0;
		String id_start = offSet;
		//先查出起始位置,获取数据偏移量
		if (id_start != null && !id_start.equals(Start.END_VAL)) {
			UserNeighborDB start_Data = DBHelper.getInstance().queryForFirst(
					UserNeighborDB.class,
					new DBWhere(DBKey.id, DBWhereType.eq, id_start));
			start_distance = start_Data.distance;
		}
		DBWhere[] wheres = type == NeighborHoodType.all ? new DBWhere[] {
				new DBWhere(DBKey.jid, DBWhereType.ge, jid),
				new DBWhere(DBKey.distance, DBWhereType.ge, start_distance) }
				: new DBWhere[] {
						new DBWhere(DBKey.jid, DBWhereType.eq, jid),
						new DBWhere(DBKey.neighborType, DBWhereType.eq, type),
						new DBWhere(DBKey.distance, DBWhereType.ge,
								start_distance), };
		List<UserNeighborDB> result = DBHelper.getInstance().queryAll(
				UserNeighborDB.class,
				new DBOrder[] { new DBOrder(DBKey.distance, DBOrderType.asc) },
				limit * 3, wheres);
		boolean isCatchStart = id_start == null;
		for (int i = 0; i < result.size(); i++) {
			if (retVal.size() >= limit) {
				break;
			}
			UserNeighborDB dbOne = result.get(i);
			if (!isCatchStart && dbOne.id.equals(id_start)) {
				isCatchStart = true;
			} else if (isCatchStart) {
				retVal.add(dbOne);
			}
		}
		return retVal;
	}
	@Override
	public String toString() {
		return "UserNeighborDB [id=" + id + ", jid=" + jid + ", jid_neighbor="
				+ jid_neighbor + ", distance=" + distance + "]";
	}
}
