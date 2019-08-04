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
package com.btf.push;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

import java.util.*;

/**
 * @func 自己拉黑别人的Packet
 * @author yuedong bao
 * @time 2014-11-26 下午3:33:32
 */
public class BlackRosterPacket extends IQ {
	protected final List<Item> rosterItems = new ArrayList<Item>();
	/*
	 * The ver attribute following XEP-0237
	 */
	String doaction = null;

	/**
	 * Adds a roster item to the packet.
	 * @param item a roster item.
	 */
	public void addRosterItem(Item item) {
		synchronized (rosterItems) {
			rosterItems.add(item);
		}
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
		buf.append("<query xmlns=\"blacklist\" ");
		if (doaction != null) {
			buf.append(" subscription=\"").append(doaction).append("\"");
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
		private String user;
		private String name;
		private String action;
		private String note;
		private String role;
		private String bday;
		private String nickname;
		private String photo;
		private String content;
		private String time;

		/**
		 * Creates a new roster item.
		 * @param user the user.
		 * @param name the user's name.
		 */
		public Item(String user, String name, String action) {
			this.user = user.toLowerCase();
			this.name = name;
			this.action = action;
		}
		/**
		 * Returns the user.
		 * @return the user.
		 */
		public String getUser() {
			return user;
		}
		/**
		 * Returns the user's name.
		 * @return the user's name.
		 */
		public String getName() {
			return name;
		}
		/**
		 * Sets the user's name.
		 * @param name the user's name.
		 */
		public void setName(String name) {
			this.name = name;
		}
		public String toXML() {
			StringBuilder buf = new StringBuilder();
			buf.append("<item jid=\"").append(user).append("\"");
			if (name != null) {
				buf.append(" name=\"").append(StringUtils.escapeForXML(name))
						.append("\"");
			}
			if (note != null) {
				buf.append(" note=\"").append(note).append("\"");
			}
			if (role != null) {
				buf.append(" role=\"").append(role).append("\"");
			}
			if (bday != null) {
				buf.append(" bday=\"").append(bday).append("\"");
			}
			if (nickname != null) {
				buf.append(" nickname=\"").append(nickname).append("\"");
			}
			if (photo != null) {
				buf.append(" photo=\"").append(photo).append("\"");
			}
			if (action != null) {
				buf.append(" subscription=\"").append(action).append("\"");
			}
			if (content != null) {
				buf.append(" content=\"").append(content).append("\"");
			}
			if (time != null) {
				buf.append(" time=\"").append(time).append("\"");
			}
			buf.append("/>");
			return buf.toString();
		}
		public String getAction() {
			return action;
		}
		public void setAction(String action) {
			this.action = action;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
		public String getBday() {
			return bday;
		}
		public void setBday(String bday) {
			this.bday = bday;
		}
		public String getNickname() {
			return nickname;
		}
		public void setNickname(String nickname) {
			this.nickname = nickname;
		}
		public String getPhoto() {
			return photo;
		}
		public void setPhoto(String photo) {
			this.photo = photo;
		}
		public String getContent() {
			return content;
		}
		public Item setContent(String content) {
			this.content = content;
			return this;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
	}

	public String getDoaction() {
		return doaction;
	}
	public void setDoaction(String doaction) {
		this.doaction = doaction;
	}
}
