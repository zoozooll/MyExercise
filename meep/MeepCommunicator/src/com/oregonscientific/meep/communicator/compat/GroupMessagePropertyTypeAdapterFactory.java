package com.oregonscientific.meep.communicator.compat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.oregonscientific.meep.serialization.PropertyTypeAdapterFactory;

public class GroupMessagePropertyTypeAdapterFactory implements
PropertyTypeAdapterFactory {

	private String GROUP_FIELD = "friends";
	private String CONTACT_LIST_FIELD = "contactList";
	

	@Override
	public <T> TypeAdapter<T> create(Gson gson, Object key) {
		// only group needs type adapter
		if (key.equals(GROUP_FIELD) || key.equals(CONTACT_LIST_FIELD) ) {
			return (TypeAdapter<T>) new Adapter(gson);
		} else {
			return null;
		}
	}
	
	private final class Adapter extends TypeAdapter<Object> {

		final Gson context;

		Adapter(Gson ctx) {
			context = ctx;
		}

		@Override
		public List<Group> read(JsonReader in) throws IOException {

			JsonToken peek = in.peek();
			if (peek == JsonToken.NULL) {
				in.nextNull();
				return null;
			}

			List<Group> groups = new ArrayList<Group>();
			try {
				in.beginArray();
				while (in.hasNext()) {

					TypeAdapter<Group> typeAdapter = context.getAdapter(Group.class);
					if (typeAdapter != null) {
						groups.add(typeAdapter.read(in));
					}
				}
				in.endArray();
			}
			catch (IllegalStateException e) {
				throw new JsonSyntaxException(e);
			}
			return groups;
		}
		
		@Override
		public void write(JsonWriter out, Object list) throws IOException {

			if (list == null) {
				out.nullValue();
				return;
			}

			out.beginArray();
			if (list instanceof List<?>)  {
				if (list.getClass().getComponentType() == Group.class) {
					List<Group> groupList = (List<Group>) list;
					// write the entire group list with friend list into json
					Iterator<Group> iterator = groupList.iterator();
					while (iterator.hasNext()) {
						TypeAdapter<Group> typeAdapter = context.getAdapter(Group.class);
						typeAdapter.write(out, iterator.next());
					}
				}
			}
			out.endArray();
		}

	}

}