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

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.oregonscientific.meep.database.Model;
import com.oregonscientific.meep.serialization.BoundField;

/**
 * Type adapter that reflects over the fields and methods of a 'Model' class.
 */
public class ModelTypeAdapterFactory implements TypeAdapterFactory {

	protected final ConstructorConstructor constructorConstructor;
	protected final FieldNamingStrategy fieldNamingPolicy;
	protected final Excluder excluder;
	protected final Map<Type, ModelSerializationStrategy> serializationStrategies =
		new HashMap<Type, ModelSerializationStrategy>();

	public ModelTypeAdapterFactory(
			ConstructorConstructor constructorConstructor,
			FieldNamingStrategy fieldNamingPolicy, 
			Excluder excluder) {

		this.constructorConstructor = constructorConstructor;
		this.fieldNamingPolicy = fieldNamingPolicy;
		this.excluder = excluder;
	}
	
	/**
	 * Configures the type adapter factory for custom serialization and deserialization.
	 * This method combines the registration of {@link BlobDeserializer} and 
	 * {@link ModelSerializationStrategy}. This method registers the type specified
	 * and no other types. Users must register related types.
	 * @param type the type definition for the adapter being registered
	 * @param adapter The object must implement at least one of the {@link BlobDeserializer},
	 * or {@link ModelSerializationStrategy} interfaces.
	 */
	public void registerSerializationAdapter(Type type, Object adapter) {
		if (adapter instanceof ModelSerializationStrategy) {
			serializationStrategies.put(type, (ModelSerializationStrategy) adapter);
		}
	}
	
	/**
	 * Returns true if the given field should be excluded in serialization, false
	 * otherwise
	 * @param f the field to serialize
	 * @param serialize true if it was serializing, false if it was deserializing
	 * @return true if the field should be excluded
	 */
	public boolean excludeField(Field f, boolean serialize) {
		return !excluder.excludeClass(f.getType(), serialize) && !excluder.excludeField(f, serialize);
	}
	
	/**
	 * Retrieves the name of the given field
	 * @param f the field to retrieve the name for serialization
	 * @return The name of to use for the given field during serialization
	 */
	private String getFieldName(Field f) {
		SerializedName serializedName = f.getAnnotation(SerializedName.class);
		return serializedName == null ? fieldNamingPolicy.translateName(f) : serializedName.value();
	}
	
	@Override
	public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {
		Class<? super T> raw = type.getRawType();

		if (!Model.class.isAssignableFrom(raw)) {
			return null; // it's not a Model!
		}

		ObjectConstructor<T> constructor = constructorConstructor.get(type);
		ModelSerializationStrategy serializationStrategy = serializationStrategies.get(raw);
		return new ModelTypeAdapter<T>(
				constructor, 
				serializationStrategy,
				getBoundFields(gson, type, raw));
	}

	/**
	 * Creates a bound field
	 * @param context the gson context
	 * @param field the field associated with the bound field
	 * @param name the name of the bound field
	 * @param fieldType the type of the field
	 * @param serialize true to serialize the field, false otherwise
	 * @param deserialize true to deserialzie the field, false otherwise
	 * @return the BoundField object associated with the given Field
	 */
	protected $Bound$Field createBoundField(
			final Gson context,
			final Field field, 
			final String name, 
			final TypeToken<?> fieldType, 
			boolean serialize,
			boolean deserialize) {
		
		return ModelBoundField.create(
				context,
				field,
				name,
				fieldType,
				serialize,
				deserialize);
	}

	/**
	 * Creates the bound fields for the given type
	 * @param context the Gson context
	 * @param type the type of the class
	 * @param raw the raw type of the class
	 * @return a map of bound fields
	 */
	protected Map<String, $Bound$Field> getBoundFields(Gson context, TypeToken<?> type, Class<?> raw) {
		Map<String, $Bound$Field> result = new LinkedHashMap<String, $Bound$Field>();
		if (raw.isInterface()) {
			return result;
		}

		Type declaredType = type.getType();
		while (raw != Object.class) {
			Field[] fields = raw.getDeclaredFields();
			for (Field field : fields) {
				boolean serialize = excludeField(field, true);
				boolean deserialize = excludeField(field, false);
				
				if (!serialize && !deserialize) {
					continue;
				}

				field.setAccessible(true);
				Type fieldType = $Gson$Types.resolve(type.getType(), raw, field.getGenericType());
				$Bound$Field boundField = createBoundField(
						context, 
						field,
						getFieldName(field),
						TypeToken.get(fieldType), 
						serialize, 
						deserialize);
				
				BoundField previous = result.put(boundField.name, boundField);
				if (previous != null) {
					throw new IllegalArgumentException(declaredType + " declares multiple JSON fields named "
							+ previous.name);
				}
			}
			type = TypeToken.get($Gson$Types.resolve(type.getType(), raw, raw.getGenericSuperclass()));
			raw = type.getRawType();
		}
		return result;
	}

}
