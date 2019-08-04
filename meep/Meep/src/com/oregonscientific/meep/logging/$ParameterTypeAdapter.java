/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.logging;

import java.io.IOException;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * The adapter that serializes/deserializes a $Parameter object
 */
public class $ParameterTypeAdapter extends TypeAdapter<$Parameter> {

	@Override
	public $Parameter read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		}
		
		// Reads the parameter value
		$Parameter parameter = new $Parameter();
		try {
			parameter.setValue(in.nextString());
		} catch (IllegalStateException e) {
			throw new JsonSyntaxException(e);
		}
		return parameter;
	}

	@Override
	public void write(JsonWriter out, $Parameter value) throws IOException {
		if (value == null) {
			out.nullValue();
			return;
		}
		
		// Writes the value to JSON output
		out.value(value.getValue());
	}

}
