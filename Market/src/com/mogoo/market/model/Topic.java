package com.mogoo.market.model;

import java.util.ArrayList;

import org.xml.sax.Attributes;

import android.database.Cursor;

import com.mogoo.market.database.TopicSQLTable;
import com.mogoo.parser.XmlResultCallback;

public class Topic {

	private String mId;
	private String mName;
	private String mImgUrl;
	private String mDescription;
	private String mType;

	public Topic() {
		
	}
	
	public Topic(String id, String name, String coverUrl, String desc, String type) {
		this.mId = id;
		this.mName = name;
		this.mImgUrl = coverUrl;
		this.mDescription = desc;
		this.mType = type;
	}
	
	public String getId() {
		return mId;
	}

	public void setId(String id) {
		mId = id;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getImgUrl() {
		return mImgUrl;
	}

	public void setImgUrl(String url) {
		mImgUrl = url;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		mDescription = description;
	}
	
	public String getType() {
		return mType;
	}

	public void setType(String type) {
		mType = type;
	}
	
	public static Topic getTopic(Cursor cursor) {
		String id = cursor.getString(TopicSQLTable.NUMBER_TOPIC_ID);
		String coverUrl = cursor.getString(TopicSQLTable.NUMBER_TOPIC_COVER_URL);
		String type = cursor.getString(TopicSQLTable.NUMBER_TOPIC_TYPE);
		String desc = cursor.getString(TopicSQLTable.NUMBER_TOPIC_DESC);
		String name = cursor.getString(TopicSQLTable.NUMBER_TOPIC_NAME);
		Topic topic = new Topic(id, name, coverUrl, desc, type);
		return topic;
	}
	
	// 解析类
	public static class TopicsCallback extends XmlResultCallback {
		private Topic topic;
		private ArrayList<Topic> topicList = new ArrayList<Topic>();
		private StringBuilder mStringBuilder = new StringBuilder();
		
		@Override
		public Object getResult() {
			return topicList;
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) {
			mStringBuilder.setLength(0);
			if (localName.equalsIgnoreCase(TopicTag.NODE)) {
				for (int i = 0; i < attributes.getLength(); i++) {
					String iQName = attributes.getQName(i);
					if (iQName.equalsIgnoreCase(TopicTag.ID)) {
						String iQNameValue = attributes.getValue(iQName);
						topic = new Topic();
						topic.setId(iQNameValue);
					}
				}
			}
			super.startElement(uri, localName, qName, attributes);
		}

		@Override
		public void endElement(String uri, String localName, String qName) {
			String data = mStringBuilder.toString();

			if (localName.equals(TopicTag.NAME)) {
				topic.setName(data);
			} else if (localName.equals(TopicTag.IMG_URL)) {
				topic.setImgUrl(data);
			} else if (localName.equals(TopicTag.DESCRIPTION)) {
				topic.setDescription(data);
			} else if (localName.equals(TopicTag.TYPE)) {
				topic.setType(data);
			}
			if (localName.equalsIgnoreCase(TopicTag.NODE)) {
				topicList.add(topic);
				topic = null;
			}

			super.endElement(uri, localName, qName);
		}

		@Override
		public void characters(char[] ch, int start, int length) {
			super.characters(ch, start, length);
			mStringBuilder.append(ch, start, length);
		}

	}

	static class TopicTag {
		static final String NODE = "rc";
		static final String ID = "id";
		static final String NAME = "n";
		static final String IMG_URL = "icp";
		static final String DESCRIPTION = "s";
		static final String TYPE = "t";
	}
}
