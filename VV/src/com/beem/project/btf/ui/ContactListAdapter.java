/*
    BEEM is a videoconference application on the Android Platform.

    Copyright (C) 2009-2011 by Frederic-Charles Barthelery,
                               Nikita Kozlov,
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
    contact@beem-project.com or http://www.beem-project.com/

 */
package com.beem.project.btf.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.ui.activity.ChatActivity;
import com.beem.project.btf.ui.activity.ContactInfoActivity;
import com.beem.project.btf.ui.activity.OtherTimeFlyMain;
import com.beem.project.btf.ui.activity.base.ActivityController;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.SortedList;
import com.beem.project.btf.utils.SortedList.Equalable;
import com.beem.project.btf.utils.Status;
import com.butterfly.vv.vv.utils.CToast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * An Adapter for the contact list. It displays a list of contact in a particular group. 好友列表适配器
 */
public class ContactListAdapter extends BaseAdapter {
	private final ComparatorContactListByStatusAndName<Contact> mComparator = new ComparatorContactListByStatusAndName<Contact>();
	private Equalable<Contact> equaltor = new Equalable<Contact>() {
		@Override
		public boolean equal(Contact t1, Contact t2) {
			return t1.getJIDParsed().equals(t2.getJIDParsed());
		}
	};
	private List<Contact> mCurrentList;
	private final List<Contact> allContacts = new SortedList<Contact>(
			new LinkedList<Contact>(), mComparator, equaltor);
	private final List<Contact> onlineContacts = new SortedList<Contact>(
			new LinkedList<Contact>(), mComparator, equaltor);
	private final Context context;
	private boolean showOnlineOnly;
	private ContactListType mThisType = ContactListType.FRIEND_ADDED;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	public ContactListAdapter(final Context c) {
		mCurrentList = allContacts;
		context = c;
	}
	public void friendClickHandler(Contact contact) {
		OtherTimeFlyMain.launch(context, contact);
	}
	public void setListType(ContactListType type) {
		mThisType = type;
	}
	@Override
	public int getCount() {
		return mCurrentList.size();
	}
	@Override
	public Object getItem(int position) {
		// contactListAdapter有headView 因此position偏移一位
		return mCurrentList.get(position - 1);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.friendtlistcontact,
					parent, false);
			vh = new ViewHolder();
			vh.profile_tv_agenew = (TextView) convertView
					.findViewById(R.id.profile_tv_agenew);
			vh.head = (ImageView) convertView.findViewById(R.id.avatar);
			vh.userName = (TextView) convertView
					.findViewById(R.id.contactlistpseudo);
			vh.vnote = (TextView) convertView
					.findViewById(R.id.contactlistmsgperso);
			vh.vvzone_wraper = (RelativeLayout) convertView
					.findViewById(R.id.vvzone_wraper);
			vh.list = convertView.findViewById(R.id.list);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		bindView(mCurrentList.get(position), position, vh);
		return convertView;
	}

	private static class ViewHolder {
		public View list;
		public ImageView head;
		public TextView profile_tv_agenew;
		public TextView userName, vnote;
		public RelativeLayout vvzone_wraper;
	}

	/**
	 * Put a contact in the list.
	 * @param c the contact
	 */
	public void put(Contact c) {
		// //LogUtils.i("add contact:" + c.getJid());
		put(c, allContacts);
		if (Status.statusOnline(c.getStatus())) {
			put(c, onlineContacts);
		}
	}
	public void put(List<Contact> contacts) {
		for (Contact c : contacts) {
			put(c);
		}
	}
	/**
	 * Remove a contact from the list.
	 * @param c the contact
	 */
	public void remove(Contact c) {
		allContacts.remove(c);
		onlineContacts.remove(c);
	}
	/**
	 * Clear the contact list.
	 */
	public void clear() {
		allContacts.clear();
		onlineContacts.clear();
	}

	private class ChatListener implements OnClickListener {
		private Contact contact;

		public ChatListener(Contact contact) {
			super();
			this.contact = contact;
		}
		@Override
		public void onClick(View v) {
			if (LoginManager.getInstance().isLogined()) {
				ChatActivity.launch(context, contact);
			} else {
				CToast.showToast(context, R.string.timefly_unlogin, Toast.LENGTH_SHORT);
				ActivityController.getInstance().gotoLogin();
			}
		}
	}

	private class VcardListener implements OnClickListener {
		Contact curContact;

		public VcardListener(Contact curContact) {
			this.curContact = curContact;
		}
		@Override
		public void onClick(View v) {
			ContactInfoActivity.launch(context, curContact);
		}
	}

	private class ZoneListener implements OnClickListener {
		private int mposition;
		Contact curContact;

		public ZoneListener(int position, Contact curContact) {
			// TODO Auto-generated constructor stub
			this.mposition = position;
			this.curContact = curContact;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			friendClickHandler(curContact);
		}
	}

	private void bindView(Contact curContact, int position, ViewHolder vh) {
		// 设置年龄
		vh.profile_tv_agenew.setText(BBSUtils.getAgeByBithday(curContact
				.getBday()));
		// 根据性别来加图像
		vh.profile_tv_agenew.setSelected(curContact.getSexInt() == 0);
		/* 头像 */
		vh.head.setOnClickListener(new VcardListener(curContact));
		curContact.displayPhoto(vh.head, animateFirstListener);
		/* 用户名 */
		vh.userName.setText(curContact.getSuitableNameMix());
		/* 签名 */
		vh.vnote.setText(curContact.getSignature());
		// 看图片组
		vh.vvzone_wraper.setOnClickListener(new ZoneListener(position,
				curContact));
		vh.list.setOnClickListener(new ChatListener(curContact));
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {
		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	/**
	 * Put a contact in a list. Helper method.
	 * @param c the contact
	 * @param list the list
	 */
	private void put(Contact c, List<Contact> list) {
		list.remove(c);
		list.add(c);
	}
	/**
	 * Tell if the list display only online contacts.
	 * @return true if only online contacts are shown
	 */
	public boolean isOnlineOnly() {
		return showOnlineOnly;
	}
	/**
	 * Set the list to display only the online contacts.
	 * @param online true to display only online contacts
	 */
	public void setOnlineOnly(boolean online) {
		if (online != showOnlineOnly) {
			showOnlineOnly = online;
			mCurrentList = showOnlineOnly ? onlineContacts : allContacts;
			notifyDataSetChanged();
		}
	}

	/**
	 * Comparator Contact by status and name.
	 */
	private static class ComparatorContactListByStatusAndName<T> implements
			Comparator<T> {
		/**
		 * Constructor.
		 */
		public ComparatorContactListByStatusAndName() {
		}
		@Override
		public int compare(T c1, T c2) {
			if (((Contact) c1).getStatus() < ((Contact) c2).getStatus()) {
				return 1;
			} else if (((Contact) c1).getStatus() > ((Contact) c2).getStatus()) {
				return -1;
			} else
				return ((Contact) c1).getNickName().compareToIgnoreCase(
						((Contact) c2).getNickName());
		}
	}

	public enum ContactListType {
		FRIEND_ADDED, NEARBY_USER, NEW_FRIEND, STRANGER;
	}
}
