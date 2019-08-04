package com.butterfly.vv.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.beem.project.btf.service.Contact;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.db.ormhelper.bean.VVImage;

/**
 * @func 图片组数据结构
 * @author yuedong bao
 * @date 2015-2-11 上午11:17:50
 * @param ImageFolder 图片组信息
 * @param List <VVImage> 图片组的图片信息
 * @param ArrayList <CommentItem>图片组的评论
 * @param Contact 图片组拥有者资料信息
 */
public class ImageFolderItem implements Parcelable {
	private Contact contact;
	private ImageFolder imageFolder;
	private ArrayList<VVImage> vvImages = new ArrayList<VVImage>();
	private ArrayList<CommentItem> comments = new ArrayList<CommentItem>();

	public ImageFolderItem() {
		super();
	}
	@SuppressWarnings("unchecked")
	public ImageFolderItem(Parcel source) {
		super();
		this.comments = source
				.readArrayList(CommentItem.class.getClassLoader());
		this.imageFolder = (ImageFolder) source.readSerializable();
		this.vvImages = source.readArrayList(VVImage.class.getClassLoader());
		// notice: readValue读出的类必须实现Parcelable
		this.contact = (Contact) source.readValue(Contact.class
				.getClassLoader());
	}
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public ArrayList<CommentItem> getComments() {
		return comments;
	}
	public void addComments(List<CommentItem> comments) {
		for (CommentItem commentItemOne : comments) {
			addComment(commentItemOne);
		}
	}
	public void addComment(CommentItem commentItem) {
		if (commentItem.getComent().isCommentLayFirst()) {
			this.comments.add(0, commentItem);
		} else {
			String to_cid = commentItem.getComent().getToCid();
			for (int i = 0; i < this.comments.size(); i++) {
				if (this.comments.get(i) != null
						&& this.comments.get(i).getComent().getToCid() != null) {
					if (this.comments.get(i).getComent().getCid()
							.equals(to_cid)) {
						this.comments.get(i).addChildItem(commentItem);
					}
				}
			}
		}
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(comments);
		dest.writeSerializable(imageFolder);
		dest.writeList(vvImages);
		dest.writeValue(contact);
	}
	public ImageFolder getImageFolder() {
		return imageFolder;
	}
	public void setImageFolder(ImageFolder imageFolder) {
		this.imageFolder = imageFolder;
	}
	public ArrayList<VVImage> getVVImages() {
		return vvImages;
	}
	public void setVVImages(ArrayList<VVImage> imgList) {
		this.vvImages.addAll(imgList);
	}


	public static final Parcelable.Creator<ImageFolderItem> CREATOR = new Creator<ImageFolderItem>() {
		@Override
		public ImageFolderItem[] newArray(int size) {
			return new ImageFolderItem[size];
		}
		@Override
		public ImageFolderItem createFromParcel(Parcel source) {
			ImageFolderItem imageFolderItem = new ImageFolderItem(source);
			return imageFolderItem;
		}
	};

	@Override
	public String toString() {
		return "ImageFolderItem [contact=" + contact + ", imageFolder="
				+ imageFolder + ", vvImages=" + vvImages + ", comments="
				+ comments + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((imageFolder == null) ? 0 : imageFolder.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImageFolderItem other = (ImageFolderItem) obj;
		if (imageFolder == null) {
			if (other.imageFolder != null)
				return false;
		} else if (!imageFolder.equals(other.imageFolder))
			return false;
		return true;
	}
}
