package com.btf.push;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import com.btf.push.UserInfoPacket.Item;

/**
 * @func 资料更新
 * @author yuedong bao
 * @time 2014-12-25 下午7:47:05
 */
public class InfoUpdatePresence extends Presence {
	public InfoUpdatePresence(Type type) {
		super(type);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void appendCustomAttr(StringBuilder buf) {
		// TODO Auto-generated method stub
		super.appendCustomAttr(buf);
	}
	@Override
	protected void appendCustomElem(StringBuilder buf) {
		// TODO Auto-generated method stub
		// <vCard xmlns="jabber:client">
		//
		// <ADR></ADR>
		// <SCH></SCH>
		// <NOTE>阿鲁</NOTE>
		// <PHOTO></PHOTO>
		// buf.append("<vCard  jid=\"").append(StringUtils.escapeForXML(user))
		// .append("\"/>");
		new VCardWriter(buf, new Item(this.getFrom()));
	}

	private class VCardWriter {
		private final StringBuilder sb;
		Item item;

		VCardWriter(StringBuilder sb, Item item) {
			this.sb = sb;
			this.item = item;
		}
		public void write() {
			boolean b = false;
			if (item != null)
				b = hasContent();
			appendTag("vCard", "xmlns", "infoUpdate", b, new ContentBuilder() {
				@Override
				public void addTagContent() {
					System.out.println(" write " + item);
					buildActualContent();
				}
			});
		}
		private boolean hasContent() {
			// noinspection OverlyComplexBooleanExpression
			return hasGeoFields() || hasSchoolFields() || hasCityFields()
					|| hasPhotoFields()
			// 20121229
			;
		}
		private void buildActualContent() {
			System.out.println(" buildActualContent " + item);
			if (item != null) {
				// 201212229
				appendGeo();
				appendPhoto();
				appendSchool();
				appendCity();
				// 20121229
				appendGenericFields();
			}
		}
		private void appendPhones(Map<String, String> phones, final String code) {
			Iterator it = phones.entrySet().iterator();
			while (it.hasNext()) {
				final Map.Entry entry = (Map.Entry) it.next();
				appendTag("TEL", true, new ContentBuilder() {
					@Override
					public void addTagContent() {
						appendEmptyTag(entry.getKey());
						appendEmptyTag(code);
						appendTag("NUMBER", StringUtils
								.escapeForXML((String) entry.getValue()));
					}
				});
			}
		}
		private void appendAddress(final Map<String, String> addr,
				final String code) {
			if (addr.size() > 0) {
				appendTag("ADR", true, new ContentBuilder() {
					@Override
					public void addTagContent() {
						appendEmptyTag(code);
						Iterator it = addr.entrySet().iterator();
						while (it.hasNext()) {
							final Map.Entry entry = (Map.Entry) it.next();
							appendTag((String) entry.getKey(), StringUtils
									.escapeForXML((String) entry.getValue()));
						}
					}
				});
			}
		}
		private void appendEmptyTag(Object tag) {
			sb.append('<').append(tag).append("/>");
		}
		private void appendGenericFields() {
			Map<String, String> otherSimpleFields = new HashMap<String, String>();
			if (item.nickName != null)
				otherSimpleFields.put("NICKNAME", item.nickName);
			if (item.bday != null)
				otherSimpleFields.put("BDAY", item.bday);
			if (item.sex != null)
				otherSimpleFields.put("ROLE", item.sex);
			if (item.note != null)
				otherSimpleFields.put("NOTE", item.note);
			if (item.desc != null)
				otherSimpleFields.put("DESC", item.desc);
			Iterator it = otherSimpleFields.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				appendTag(entry.getKey().toString(),
						StringUtils.escapeForXML((String) entry.getValue()));
			}
			// it = otherUnescapableFields.entrySet().iterator();
			// while (it.hasNext()) {
			// Map.Entry entry = (Map.Entry) it.next();
			// appendTag(entry.getKey().toString(), (String) entry.getValue());
			// }
		}
		private boolean hasGeoFields() {
			System.out.println("hasGeoFields " + item);
			return item.lat != null || item.lon != null;
		}
		private boolean hasCityFields() {
			return item.city != null;
		}
		private boolean hasSchoolFields() {
			return item.school != null || item.time != null
					|| item.spec != null;
		}
		private boolean hasPhotoFields() {
			return item.binval != null;
		}
		private void appendGeo() {
			if (hasGeoFields()) {
				appendTag("GEO", true, new ContentBuilder() {
					@Override
					public void addTagContent() {
						appendTag("LAT", StringUtils.escapeForXML(item.lat));
						appendTag("LON", StringUtils.escapeForXML(item.lon));
					}
				});
			}
		}
		private void appendCity() {
			if (hasCityFields()) {
				appendTag("ADR", true, new ContentBuilder() {
					@Override
					public void addTagContent() {
						appendTag("REGION", StringUtils.escapeForXML(item.city));
						appendTag("LOCALITY",
								StringUtils.escapeForXML(item.locality));
						appendTag("STREET",
								StringUtils.escapeForXML(item.street));
						appendTag("CURPOS",
								StringUtils.escapeForXML(item.curpos));
					}
				});
			}
		}
		private void appendSchool() {
			if (hasSchoolFields()) {
				appendTag("SCH", true, new ContentBuilder() {
					@Override
					public void addTagContent() {
						appendTag("SCHOOL",
								StringUtils.escapeForXML(item.school));
						appendTag("TIME", StringUtils.escapeForXML(item.time));
						appendTag("SPEC", StringUtils.escapeForXML(item.spec));
					}
				});
			}
		}
		private void appendPhoto() {
			if (hasPhotoFields()) {
				appendTag("PHOTO", true, new ContentBuilder() {
					@Override
					public void addTagContent() {
						appendTag("BINVAL",
								StringUtils.escapeForXML(item.binval));
					}
				});
			}
		}
		private void appendTag(String tag, String attr, String attrValue,
				boolean hasContent, ContentBuilder builder) {
			sb.append('<').append(tag);
			if (attr != null) {
				sb.append(' ').append(attr).append('=').append('\'')
						.append(attrValue).append('\'');
			}
			if (hasContent) {
				sb.append('>');
				builder.addTagContent();
				sb.append("</").append(tag).append(">\n");
			} else {
				sb.append("/>\n");
			}
		}
		private void appendTag(String tag, boolean hasContent,
				ContentBuilder builder) {
			appendTag(tag, null, null, hasContent, builder);
		}
		private void appendTag(String tag, final String tagText) {
			if (tagText == null)
				return;
			final ContentBuilder contentBuilder = new ContentBuilder() {
				@Override
				public void addTagContent() {
					sb.append(tagText.trim());
				}
			};
			appendTag(tag, true, contentBuilder);
		}
	}

	private interface ContentBuilder {
		void addTagContent();
	}
}
