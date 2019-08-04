package com.butterfly.vv.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.butterfly.vv.db.ormhelper.bean.VVImage;

public class ImageItemComments implements Parcelable {
	VVImage vvImage;
	ArrayList<Comment> comments;

	public ImageItemComments(VVImage vvImage, ArrayList<Comment> comments) {
		super();
		this.vvImage = vvImage;
		this.comments = comments;
	}
	public VVImage getVvImage() {
		return vvImage;
	}
	public void setVvImage(VVImage vvImage) {
		this.vvImage = vvImage;
	}
	public ArrayList<Comment> getComments() {
		return comments;
	}
	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeSerializable(vvImage);
		dest.writeList(comments);
	}

	public static final Parcelable.Creator<ImageItemComments> CREATOR = new Creator<ImageItemComments>() {
		@Override
		public ImageItemComments[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ImageItemComments[size];
		}
		@Override
		public ImageItemComments createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new ImageItemComments((VVImage) source.readSerializable(),
					source.readArrayList(Comment.class.getClassLoader()));
		}
	};
}
