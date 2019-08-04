package com.oregonscientific.meep.browser.database;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * History information object saved to the database.
 * 
 * @author kevingalligan
 */
@DatabaseTable
public class History implements Serializable {

	/**
	 * serialVersion uid
	 */
	private static final long serialVersionUID = -6282177628214233439L;

	public static final String NAME_FIELD_NAME = "name";
	public static final String URL_FIELD_NAME = "url";
	public static final String ID_FIELD_NAME = "_id";

	@DatabaseField(generatedId = true)
	private Integer _id;

	@DatabaseField
	private String name;

	@DatabaseField
	private String url;

	@DatabaseField(dataType = DataType.BYTE_ARRAY, canBeNull = true)
	byte[] favicon;

	@DatabaseField(canBeNull = true)
	private String thumbnail;

	@DatabaseField(canBeNull = true)
	private String description;

	public Integer getId() {
		return _id;
	}

	public void setId(Integer id) {
		this._id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public byte[] getFavicon() {
		return favicon;
	}

	public void setFavicon(byte[] favicon) {
		this.favicon = favicon;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return name + " " + url;
	}

	public History() {

	}

	public History(int id, String name, String url) {
		this._id = id;
		this.name = name;
		this.url = url;
	}

	public History(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public byte[] getByteBitmap(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); // bm is the
																// bitmap object
		return baos.toByteArray();
	}

	public Bitmap decodeByteBitmap(byte[] b) {
		return BitmapFactory.decodeByteArray(b, 0, b.length);
	}

	public Bitmap getFaviconBitmap() {
		return decodeByteBitmap(favicon);
	}
}
