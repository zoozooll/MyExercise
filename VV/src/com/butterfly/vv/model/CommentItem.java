package com.butterfly.vv.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.beem.project.btf.service.Contact;
import com.beem.project.btf.utils.SortedList;
import com.beem.project.btf.utils.SortedList.Equalable;

/**
 * @func 评论组合类
 * @author yuedong bao
 * @date 2015-2-11 下午4:32:32
 */
public class CommentItem implements Parcelable {
	// 评论信息
	private Comment coment;
	// 评论者个人信息
	private Contact contactComment;
	// 被评论者个人信息
	private Contact contactCommented;
	// 二级子评论集(降序排序)
	private List<CommentItem> childItems = new SortedList<CommentItem>(
			new ArrayList<CommentItem>(), new Comparator<CommentItem>() {
				@Override
				public int compare(CommentItem lhs, CommentItem rhs) {
					String time_l = lhs.coment.getCommentTime();
					String time_r = rhs.coment.getCommentTime();
					return time_l.compareTo(time_r);
				}
			}, new Equalable<CommentItem>() {
				@Override
				public boolean equal(CommentItem t1, CommentItem t2) {
					return t1.coment.getId().equals(t2.coment.getId());
				}
			});

	public List<CommentItem> getChildItems() {
		return childItems;
	}
	public void setChildItems(List<CommentItem> childItems) {
		this.childItems = childItems;
	}
	public CommentItem(Parcel source) {
		readFromParcel(source);
	}
	public CommentItem() {
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(coment);
		dest.writeValue(contactComment);
		dest.writeValue(contactCommented);
		dest.writeList(childItems);
	}
	public void readFromParcel(Parcel source) {
		coment = (Comment) source.readValue(Comment.class.getClassLoader());
		contactComment = (Contact) source.readValue(Contact.class
				.getClassLoader());
		contactCommented = (Contact) source.readValue(Contact.class
				.getClassLoader());
		source.readList(childItems, CommentItem.class.getClassLoader());
	}
	public void addChildItem(CommentItem item) {
		childItems.add(item);
	}
	public String packageOfflineNext() {
		return coment.getToCid() + "@" + coment.getCid();
	}

	public static final Parcelable.Creator<CommentItem> CREATOR = new Creator<CommentItem>() {
		@Override
		public CommentItem[] newArray(int size) {
			return new CommentItem[size];
		}
		@Override
		public CommentItem createFromParcel(Parcel source) {
			return new CommentItem(source);
		}
	};

	public Comment getComent() {
		return coment;
	}
	public void setComent(Comment comment) {
		this.coment = comment;
	}
	public Contact getCommentContact() {
		return contactComment;
	}
	public void setCommentContact(Contact contactComment) {
		this.contactComment = contactComment;
	}
	public Contact getCommentedContact() {
		return contactCommented;
	}
	public void setCommentedContact(Contact contactCommented) {
		this.contactCommented = contactCommented;
	}
	@Override
	public String toString() {
		return "CommentItem [coment=" + coment + ", contactComment="
				+ contactComment + ", contactCommented=" + contactCommented
				+ "]";
	}
}
