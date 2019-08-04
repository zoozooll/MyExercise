package com.btf.push.base;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.packet.IQ;
import org.xmlpull.v1.XmlPullParser;

public abstract class BaseIQ extends IQ {
	public enum BaseIQKey {
		jid, subscription, content, approved, time, alias, lon, lat, start,
		limit, item, nickname, sex, portrait_small, phonenum, bday, logintime,
		school, name, xmlns, syndata_vcard("vcard-temp"), syndata_roster(
				"jabber:iq:roster"), syndata_blacklist("blacklist"),
		syndata_tm("contact:tm"), syndata_systime("systime"), digest, type,
		gid_create_time, gid, cid, next, signature, subject, distance;
		private BaseIQKey(String... val) {
			this.val = val.length > 0 ? val[0] : toString();
		}

		public String val;
	}

	protected Map<String, Object> fields = new Hashtable<String, Object>();

	public void setField(String key, Object value) {
		if (key != null && value != null)
			fields.put(key.toString(), value);
	}
	public Object getField(String key) {
		return fields.get(key.toString());
	}
	@SuppressWarnings("unchecked")
	private <T> T getField(String key, Class<T> cls) {
		return (T) fields.get(key.toString());
	}
	public void setField(BaseIQKey t1, Object val) {
		setField(t1.toString(), val);
	}
	public Object getField(BaseIQKey t1) {
		return getField(t1.toString());
	}
	public String getFieldStr(BaseIQKey t1) {
		return (String) getField(t1.toString());
	}
	@SuppressWarnings("unchecked")
	public <T, D extends T> D getField(BaseIQKey t1, Class<T> cls) {
		return (D) getField(t1.toString(), cls);
	}
	public int size() {
		return fields.size();
	}
	public Boolean containsKey(BaseIQKey key) {
		return fields.containsKey(key.toString());
	}
	public Boolean containsField(Object val) {
		return fields.containsValue(val);
	}
	public Map<String, Object> cloneFieldMaps() {
		Map<String, Object> retVal = new Hashtable<String, Object>();
		for (String key : fields.keySet()) {
			retVal.put(key, fields.get(key));
		}
		return retVal;
	}
	public void clearFileds() {
		fields.clear();
	}
	public void appendTag(StringBuilder sb, String tag, String[] attrKeys,
			String[] attrVals, boolean hasContent, ContentBuilder builder) {
		sb.append('<').append(tag);
		if (attrKeys != null && attrVals != null) {
			for (int i = 0; i < attrKeys.length; i++) {
				if (attrKeys != null && attrVals[i] != null) {
					sb.append(' ').append(attrKeys[i]).append('=').append('\'')
							.append(attrVals[i]).append('\'');
				}
			}
		}
		if (hasContent) {
			sb.append('>');
			builder.addTagContent();
			sb.append("</").append(tag).append('>');
		} else {
			sb.append("/>");
		}
	}
	public void appendTag(StringBuilder sb, String tag, boolean hasContent,
			ContentBuilder builder) {
		appendTag(sb, tag, null, null, hasContent, builder);
	}
	public void appendTag(final StringBuilder sb, String tag,
			final String tagText) {
		if (tagText == null)
			return;
		final ContentBuilder contentBuilder = new ContentBuilder() {
			@Override
			public void addTagContent() {
				sb.append(tagText.trim());
			}
		};
		appendTag(sb, tag, true, contentBuilder);
	}

	public interface ContentBuilder {
		void addTagContent();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [fields=" + fields + "]";
	}
	/**
	 * @func 将BaseIQ数据转换成下面格式 的xml字符串：
	 * @format: <标签 属性...> <item 属性..../> <item 属性.../> </标签>
	 * @eg: <query xmlns='neighborhood' lon='16.329461' limit='20' lat='37.843692'
	 *      start='0'nbType='2'> <item lat='37.843692' lon='16.329461' jid='1234567890@vv'/> <item
	 *      lat='27.843692' lon='26.329461' jid='1234567890@vv'/> </query>
	 * @param sb
	 * @param map
	 * @param element 开始的元素,如上例的query
	 */
	public String toXml(final StringBuilder sb, String element, String xmlns) {
		int j = 0;
		String[] attrKeys = null;
		String[] attrVals = null;
		if (xmlns != null && !xmlns.isEmpty()) {
			attrKeys = new String[fields.size() + 1];
			attrVals = new String[fields.size() + 1];
			attrKeys[0] = "xmlns";
			attrVals[0] = xmlns;
			j++;
		} else {
			attrKeys = new String[fields.size()];
			attrVals = new String[fields.size()];
		}
		final StringBuilder content = new StringBuilder();
		for (Iterator<String> it = fields.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			if (fields.get(key) instanceof String) {
				attrKeys[j] = key.toString();
				attrVals[j] = (String) fields.get(key);
			} else if (fields.get(key) instanceof List) {
				@SuppressWarnings("unchecked")
				List<BaseIQ> list = (List<BaseIQ>) fields.get(key);
				for (BaseIQ item : list) {
					item.toXml(content, "item", null);
				}
			}
			j++;
		}
		appendTag(sb, element, attrKeys, attrVals, content.length() > 0,
				new ContentBuilder() {
					@Override
					public void addTagContent() {
						sb.append(content);
					}
				});
		return sb.toString();
	}
	/**
	 * @func 将下面格式 的xml字符串写入BaseIQ：
	 * @format: <标签 属性...> <item 属性..../> <item 属性.../> </标签>
	 * @eg: <query xmlns='neighborhood' lon='16.329461' limit='20' lat='37.843692'
	 *      start='0'nbType='2'> <item lat='37.843692' lon='16.329461' jid='1234567890@vv'/> <item
	 *      lat='27.843692' lon='26.329461' jid='1234567890@vv'/> </query>
	 * @param parser
	 * @param baseIQ
	 * @param keys
	 * @param element
	 * @param xmlns
	 * @throws Exception
	 */
	public BaseIQ toBaseIQ(XmlPullParser parser, String element, String xmlns)
			throws Exception {
		boolean done = false;
		BaseIQ itemBaseIQ = null;
		List<BaseIQ> itemList = new ArrayList<BaseIQ>();
		while (!done) {
			if (parser.getEventType() == XmlPullParser.START_TAG
					&& parser.getName().equals(element)) {
				for (int i = 0; i < parser.getAttributeCount(); i++) {
					setField(parser.getAttributeName(i),
							parser.getAttributeValue(i));
				}
			}
			int enventType = parser.next();
			if (enventType == XmlPullParser.START_TAG
					&& parser.getName().equals("item")) {
				itemBaseIQ = getClass().newInstance();
				for (int i = 0; i < parser.getAttributeCount(); i++) {
					itemBaseIQ.setField(parser.getAttributeName(i),
							parser.getAttributeValue(i));
				}
			} else if (enventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals(element)) {
					done = true;
					setField("item", itemList);
				} else if (parser.getName().equals("item")) {
					if (itemBaseIQ != null) {
						itemList.add(itemBaseIQ);
					}
				}
			}
		}
		return this;
	}
}
