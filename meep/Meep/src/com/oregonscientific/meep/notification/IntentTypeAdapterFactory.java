/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.notification;

import java.io.IOException;

import android.content.Intent;
import android.os.Parcel;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * Adapt an {@link android.content.Intent}
 */
public class IntentTypeAdapterFactory implements TypeAdapterFactory  {

	@SuppressWarnings("unchecked")
	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		Class<? super T> raw = type.getRawType();
		
		if (!Intent.class.isAssignableFrom(raw)) {
			return null; // it's not a PendingIntent!
		}
		
		return (TypeAdapter<T>) new Adapter();
	}
	
	private final class Adapter extends TypeAdapter<Intent> {

		@Override
		public Intent read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			
			Intent result = null;
			try {
				if (in.peek() == JsonToken.STRING) {
					String base64 = in.nextString();
					byte[] data = Base64.decode(base64, Base64.NO_WRAP);
					if (data != null) {
						Parcel parcel = Parcel.obtain();
						parcel.unmarshall(data, 0, data.length);
						parcel.setDataPosition(0);
						result = Intent.CREATOR.createFromParcel(parcel);
						parcel.recycle();
					}
				}
			} catch (Exception e) {
				throw new JsonSyntaxException(e);
			}
			return result;
		}

		@Override
		public void write(JsonWriter out, Intent value) throws IOException {
			if (value == null) {
				out.nullValue();
				return;
			}
			
			// Encodes a {@link PendingIntent} to a base64 string
			Parcel parcel = Parcel.obtain();
			value.writeToParcel(parcel, 0);
			byte[] data = parcel.marshall();
			String stringData = Base64.encodeToString(data, Base64.NO_WRAP);
			out.value(stringData);
			parcel.recycle();
		}
		
	}

}
