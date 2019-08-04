package com.oregonscientific.meep.store2.banner;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public class Banner implements Parcelable {
	private String packageName;
	private String imageUrl;
	private Intent intent;

	public Banner() {
	}
	public Banner(Parcel source) {
		packageName = source.readString();
		imageUrl = source.readString();
		intent = source.readParcelable(Intent.class.getClassLoader());
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(packageName);
		dest.writeString(imageUrl);
		dest.writeParcelable(intent, 0);
	}

	public static final Parcelable.Creator<Banner> CREATOR = new Parcelable.Creator<Banner>() {

		@Override
		public Banner createFromParcel(Parcel source) {
			return new Banner(source);
		}

		@Override
		public Banner[] newArray(int size) {
			return new Banner[size];
		}

	};
}
