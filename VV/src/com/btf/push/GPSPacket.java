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

import java.util.*;

/**
 * Represents XMPP roster packets.
 * @author Matt Tucker
 */
public class GPSPacket extends IQ {
	private final List<Item> rosterItems = new ArrayList<Item>();
	private String limit;
	private String dist_params;// <DIST>500</DIST> *
	private String lat_params;// <LAT>32.34</LAT> * //γ��
	private String lon_params;// <LON>65.26</LON>
	private String role_params;

	/*
	 * The ver attribute following XEP-0237
	 */
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
		// buf.append("<vCard xmlns=\"stranger-temp\"");
		buf.append("<vCard xmlns=\"distance\"");
		buf.append(">");
		if (limit != null) {
			buf.append("<TIMES>").append(limit).append("</TIMES>");
		}
		if (role_params != null) {
			buf.append("<ROLE>").append(role_params).append("</ROLE>");
		}
		if (role_params != null) {
			buf.append("<DIST>").append(dist_params).append("</DIST>");
		}
		if (role_params != null) {
			buf.append("<LAT>").append(lat_params).append("</LAT>");
		}
		if (role_params != null) {
			buf.append("<LON>").append(lon_params).append("</LON>");
		}
		synchronized (rosterItems) {
			for (Item entry : rosterItems) {
				buf.append(entry.toXML());
			}
		}
		buf.append("</vCard>");
		return buf.toString();
	}
	public String getLimit() {
		return limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	public String getRole_params() {
		return role_params;
	}
	public void setRole_params(String role_params) {
		this.role_params = role_params;
	}
	public String getDist_params() {
		return dist_params;
	}
	public void setDist_params(String dist_params) {
		this.dist_params = dist_params;
	}
	public String getLat_params() {
		return lat_params;
	}
	public void setLat_params(String lat_params) {
		this.lat_params = lat_params;
	}
	public String getLon_params() {
		return lon_params;
	}
	public void setLon_params(String lon_params) {
		this.lon_params = lon_params;
	}
}
