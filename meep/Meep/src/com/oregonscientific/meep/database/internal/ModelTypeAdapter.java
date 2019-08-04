/**
 * Adopted from Joy Aether Ltd.
 * 
 * Licensed under the agreement between Joy Aether Ltd. and IDT
 * Internation Ltd. (the "License");
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oregonscientific.meep.database.internal;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.oregonscientific.meep.serialization.BoundField;

/**
 * The adapter that serializes/deserializes a data model
 *  
 * @author Stanley Lam
 *
 * @param <T> The type of Model
 */
public class ModelTypeAdapter<T> extends DaoTypeAdapter<T> {
	
	protected final ObjectConstructor<T> constructor;
	protected final Map<String, $Bound$Field> boundFields;
	protected ModelSerializationStrategy serializationStrategy;
	
	public ModelTypeAdapter(
			ObjectConstructor<T> constructor, 
			ModelSerializationStrategy serializationStrategy, 
			Map<String, $Bound$Field> boundFields) {
		
		this.constructor = constructor;
		this.serializationStrategy = serializationStrategy;
		this.boundFields = boundFields;
	}
	
	protected $Bound$Field getIdField() {
		Collection<$Bound$Field> fields = boundFields.values();
		for ($Bound$Field field : fields) {
			if (field.isIdField())
				return field;
		}
		return null;
	}
	
	@Override
	public ModelSerializationStrategy setSerializationStrategy(
			ModelSerializationStrategy serializationStrategy) {
		
		ModelSerializationStrategy returnValue = this.serializationStrategy;
		this.serializationStrategy = serializationStrategy;
		return returnValue;
	}
	
	@Override
	public T read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		}

		T instance = constructor.construct();

		try {
			JsonToken token = in.peek();
			switch (token) {
			case BEGIN_OBJECT:
				in.beginObject();
				while (in.hasNext()) {
					String name = in.nextName();
					BoundField field = boundFields.get(name);
					if (field == null || !field.shouldDeserialize) {
						in.skipValue();
					} else {
						field.read(in, instance, name);
					}
				}
				in.endObject();
				break;
			case NUMBER:
			case STRING:
				BoundField field = getIdField();
				if (field != null)
					field.read(in, instance, null);
				break;
			}
		} catch (IllegalStateException e) {
			throw new JsonSyntaxException(e);
		} catch (IllegalAccessException e) {
			throw new AssertionError(e);
		}

		return instance;
	}

	@Override
	public void write(JsonWriter out, T value) throws IOException {
		if (value == null) {
			out.nullValue();
			return;
		}

		// should serialize the ID field only if no serialization policy was 
		// specified for the 'Model'
		boolean serializeIdFieldOnly = serializationStrategy == null ? 
				false : serializationStrategy.shouldSerializeIdFieldOnly();
		
		try {
			if (!serializeIdFieldOnly) {
				out.beginObject();
				for ($Bound$Field boundField : boundFields.values()) {
					boolean shouldSkipField = serializationStrategy == null ? 
							false : serializationStrategy.shouldSkipField(boundField.field);
					
					if (boundField.shouldSerialize && !shouldSkipField) {
						out.name(boundField.name);
						boundField.write(out, value, serializationStrategy);
					}
				}
				out.endObject();
			} else {
				$Bound$Field field = getIdField();
				if (field != null) {
					field.write(out, value, serializationStrategy);
				}
			}
		} catch (IllegalAccessException e) {
			throw new AssertionError();
		}
	}
	
}
