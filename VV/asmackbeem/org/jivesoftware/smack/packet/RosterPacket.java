/**
 * $RCSfile$
 * $Revision$
 * $Date$
 *
 * Copyright 2003-2007 Jive Software.
 *
 * All rights reserved. Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.util.StringUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Represents XMPP roster packets.
 * @author Matt Tucker
 */
public class RosterPacket extends IQ {
	private final List<Item> rosterItems = new ArrayList<Item>();
	/*
	 * The ver attribute following XEP-0237
	 */
	private String version;
	private String req;

	/**
	 * Adds a roster item to the packet.
	 * @param item a roster item.
	 */
	public void addRosterItem(Item item) {
		synchronized (rosterItems) {
			rosterItems.add(item);
		}
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * Returns the number of roster items in this roster packet.
	 * @return the number of roster items.
	 */
	public int getRosterItemCount() {
		synchronized (rosterItems) {
			return rosterItems.size();
		}
	}
	/**
	 * Returns an unmodifiable collection for the roster items in the packet.
	 * @return an unmodifiable collection for the roster items in the packet.
	 */
	public Collection<Item> getRosterItems() {
		synchronized (rosterItems) {
			return Collections
					.unmodifiableList(new ArrayList<Item>(rosterItems));
		}
	}
	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<query xmlns=\"jabber:iq:roster\" ");
		if (getReq() != null) {// 用于花名册
			buf.append("req=\"").append(StringUtils.escapeForXML(getReq()))
					.append("\" ");
		}
		if (version != null) {
			buf.append(" ver=\"" + version + "\" ");
		}
		buf.append(">");
		synchronized (rosterItems) {
			for (Item entry : rosterItems) {
				buf.append(entry.toXML());
			}
		}
		buf.append("</query>");
		return buf.toString();
	}

	/**
	 * A roster item, which consists of a JID, their name, the type of subscription, and the groups
	 * the roster item belongs to.
	 */
	public static class Item {
		private String jid;
		private String nickname;
		private ItemType itemType;
		private ItemStatus itemStatus;
		private final Set<String> groupNames;
		// VV custom field
		private String logintime;
		private String portrait_small;
		private String signature;
		private String bday;
		private String sex;
		private String alias;
		private String lat;
		private String lon;

		/**
		 * Creates a new roster item.
		 * @param jid the user.
		 * @param nickname the user's name.
		 */
		public Item(String jid, String nickname) {
			this.jid = jid.toLowerCase();
			this.nickname = nickname;
			itemType = null;
			itemStatus = null;
			groupNames = new CopyOnWriteArraySet<String>();
		}
		/**
		 * @func
		 * @param user
		 */
		public void setJid(String user) {
			this.jid = user;
		}
		/**
		 * Returns the user.
		 * @return the user.
		 */
		public String getJid() {
			return jid;
		}
		/**
		 * Returns the user's name.
		 * @return the user's name.
		 */
		public String getNickName() {
			return nickname;
		}
		/**
		 * Sets the user's name.
		 * @param name the user's name.
		 */
		public void setNickname(String name) {
			this.nickname = name;
		}
		/**
		 * Returns the roster item type.
		 * @return the roster item type.
		 */
		public ItemType getItemType() {
			return itemType;
		}
		/**
		 * Sets the roster item type.
		 * @param itemType the roster item type.
		 */
		public void setItemType(ItemType itemType) {
			this.itemType = itemType;
		}
		/**
		 * Returns the roster item status.
		 * @return the roster item status.
		 */
		public ItemStatus getItemStatus() {
			return itemStatus;
		}
		/**
		 * Sets the roster item status.
		 * @param itemStatus the roster item status.
		 */
		public void setItemStatus(ItemStatus itemStatus) {
			this.itemStatus = itemStatus;
		}
		/**
		 * Returns an unmodifiable set of the group names that the roster item belongs to.
		 * @return an unmodifiable set of the group names.
		 */
		public Set<String> getGroupNames() {
			return Collections.unmodifiableSet(groupNames);
		}
		/**
		 * Adds a group name.
		 * @param groupName the group name.
		 */
		public void addGroupName(String groupName) {
			groupNames.add(groupName);
		}
		/**
		 * Removes a group name.
		 * @param groupName the group name.
		 */
		public void removeGroupName(String groupName) {
			groupNames.remove(groupName);
		}
		public String toXML() {
			StringBuilder buf = new StringBuilder();
			buf.append("<item jid=\"").append(jid).append("\"");
			if (nickname != null) {
				buf.append(" name=\"")
						.append(StringUtils.escapeForXML(nickname))
						.append("\"");
			}
			if (itemType != null) {
				buf.append(" subscription=\"").append(itemType).append("\"");
			}
			if (itemStatus != null) {
				buf.append(" ask=\"").append(itemStatus).append("\"");
			}
			if (alias != null) {
				buf.append(" alias=\"").append(alias).append("\"");
			}
			buf.append(">");
			for (String groupName : groupNames) {
				buf.append("<group>")
						.append(StringUtils.escapeForXML(groupName))
						.append("</group>");
			}
			buf.append("</item>");
			return buf.toString();
		}
		public String getLoginTime() {
			return logintime;
		}
		public void setLogintime(String onlineTime) {
			this.logintime = onlineTime;
		}
		public String getHeadPhoto() {
			return portrait_small;
		}
		public void setHeadPhoto(String photo) {
			this.portrait_small = photo;
		}
		public String getSignature() {
			return signature;
		}
		public void setSignature(String signature) {
			this.signature = signature;
		}
		public String getBday() {
			return bday;
		}
		public void setBday(String bday) {
			this.bday = bday;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		public String getAlias() {
			return alias;
		}
		public void setAlias(String alias) {
			this.alias = alias;
		}
		public String getLat() {
			return lat;
		}
		public void setLat(String lat) {
			this.lat = lat;
		}
		public String getLon() {
			return lon;
		}
		public void setLon(String lon) {
			this.lon = lon;
		}
		public String getLogintime() {
			return logintime;
		}
	}

	/**
	 * The subscription status of a roster item. An optional element that indicates the subscription
	 * status if a change request is pending.
	 */
	public static class ItemStatus {
		/**
		 * Request to subcribe.
		 */
		public static final ItemStatus SUBSCRIPTION_PENDING = new ItemStatus(
				"subscribe");
		/**
		 * Request to unsubscribe.
		 */
		public static final ItemStatus UNSUBSCRIPTION_PENDING = new ItemStatus(
				"unsubscribe");

		public static ItemStatus fromString(String value) {
			if (value == null) {
				return null;
			}
			value = value.toLowerCase();
			if ("unsubscribe".equals(value)) {
				return UNSUBSCRIPTION_PENDING;
			} else if ("subscribe".equals(value)) {
				return SUBSCRIPTION_PENDING;
			} else {
				return null;
			}
		}

		private String value;

		/**
		 * Returns the item status associated with the specified string.
		 * @param value the item status.
		 */
		private ItemStatus(String value) {
			this.value = value;
		}
		@Override
		public String toString() {
			return value;
		}
	}

	public static enum ItemType {
		/**
		 * The user and subscriber have no interest in each other's presence.
		 */
		none,
		/**
		 * The user is interested in receiving presence updates from the subscriber.
		 */
		to,
		/**
		 * The subscriber is interested in receiving presence updates from the user.
		 */
		from,
		/**
		 * The user and subscriber have a mutual interest in each other's presence.
		 */
		both,
		/**
		 * The user wishes to stop receiving presence updates from the subscriber.
		 */
		remove,
		/**
		 * The user wishes to modify the alias of his friend.
		 */
		modify,
	}

	public String getReq() {
		return req;
	}
	public void setReq(String req) {
		this.req = req;
	}
}
