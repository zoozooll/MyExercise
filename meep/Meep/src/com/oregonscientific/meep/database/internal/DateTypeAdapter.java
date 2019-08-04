/*
 * Adopted from Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oregonscientific.meep.database.internal;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * Type adapter that performs translation between BIGINT and Date data type over
 * a 'Date' class.
 */
public final class DateTypeAdapter extends TypeAdapter<Date> {
	public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
		@SuppressWarnings("unchecked")
		// we use a runtime check to make sure the 'T's equal
		public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
			return typeToken.getRawType() == Date.class ? (TypeAdapter<T>) new DateTypeAdapter()
					: null;
		}
	};

	@Override
	public Date read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		}
		return deserializeToDate(in.nextString());
	}

	private synchronized Date deserializeToDate(String json) {
		String JSONDateToMilliseconds = "\\/(Date\\((.*?)(\\+.*)?\\))\\/";
		Pattern pattern = Pattern.compile(JSONDateToMilliseconds);
		Matcher matcher = pattern.matcher(json);
		String result = matcher.replaceAll("$2");

		try {
			return new Date(Long.valueOf(result));
		} catch (Exception e) {
			throw new JsonSyntaxException(e);
		}
	}

	@Override
	public synchronized void write(JsonWriter out, Date value) throws IOException {
		if (value == null) {
			out.nullValue();
			return;
		}

		out.value(value.getTime());
	}
}