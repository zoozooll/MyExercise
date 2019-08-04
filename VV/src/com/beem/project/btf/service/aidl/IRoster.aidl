/*
    BEEM is a videoconference application on the Android Platform.

    Copyright (C) 2009 by Frederic-Charles Barthelery,
                          Jean-Manuel Da Silva,
                          Nikita Kozlov,
                          Philippe Lago,
                          Jean Baptiste Vergely,
                          Vincent Veronis.

    This file is part of BEEM.

    BEEM is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    BEEM is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with BEEM.  If not, see <http://www.gnu.org/licenses/>.

    Please send bug reports with examples or suggestions to
    contact@beem-project.com or http://dev.beem-project.com/

    Epitech, hereby disclaims all copyright interest in the program "Beem"
    written by Frederic-Charles Barthelery,
               Jean-Manuel Da Silva,
               Nikita Kozlov,
               Philippe Lago,
               Jean Baptiste Vergely,
               Vincent Veronis.

    Nicolas Sadirac, November 26, 2009
    President of Epitech.

    Flavien Astraud, November 26, 2009
    Head of the EIP Laboratory.

*/
package com.beem.project.btf.service.aidl;

import com.beem.project.btf.service.aidl.IBeemRosterListener;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.service.PresenceAdapter;
import com.beem.project.btf.service.PacketResult;

interface IRoster {
	//黑名单相关
	List<Contact> getBlacklist();
	PacketResult addBlacklist(in String[] user);
	PacketResult removeBlacklist(in String[] user);
    boolean blackYet(in String user);
 
    //好友相关
    PacketResult addFriend(in String jid, in String content,in String operation);
    PacketResult removeFriend(in String jid);
    PacketResult modifyFriendAlias(in String jid, in String alias);
    boolean friendYet(in String user);
    List<Contact> getFriendList();
    
    //查询
    Contact getContact(in String jid);
    //修改个人资料
    PacketResult modifyContactInfo(in Map modifyMap);
    
    //以下接口未使用
    void setContactName(in String jid, in String name);
    void createGroup(in String groupname);
    void addContactToGroup(in String groupName, in String jid);
    void removeContactFromGroup(in String groupName, in String jid);
    List<String> getGroupsNames();
    PresenceAdapter getPresence(in String jid);
    void addRosterListener(in IBeemRosterListener listen);
    void removeRosterListener(in IBeemRosterListener listen);
	Contact getMyContact();
}
