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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;

import org.jasypt.util.password.PasswordEncryptor;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.j256.ormlite.field.ForeignCollectionField;
import com.oregonscientific.meep.database.Model;
import com.oregonscientific.meep.database.PasswordField;
import com.oregonscientific.meep.serialization.TypeAdapterRuntimeTypeWrapper;

public class ModelBoundField extends $Bound$Field {
	
	final protected Gson context;
	final TypeToken<?> fieldType;
	final TypeAdapter<?> typeAdapter;
	
	protected ModelBoundField(
			Gson context,
			Field field, 
			String name, 
			TypeToken<?> fieldType, 
			boolean serialize,
			boolean deserialize) {
		
		super(name, field, serialize, deserialize);
		this.context = context;
		this.fieldType = fieldType;
		typeAdapter = context.getAdapter(fieldType);
	}
	
	public static ModelBoundField create(
			Gson context,
			Field field, 
			String name, 
			TypeToken<?> fieldType, 
			boolean serialize,
			boolean deserialize) {
		
		return new ModelBoundField(context, field, name, fieldType, serialize, deserialize);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })	// the type adapter and field type always agree
	@Override
	public void write(
			JsonWriter writer, 
			Object value, 
			ModelSerializationStrategy serializationStrategy) throws IOException, IllegalAccessException {
		
		Object fieldValue = this.field.get(value);
		boolean shouldExpand = serializationStrategy == null ? 
				false : serializationStrategy.shouldExpandField(this.field);
		
		if (isForeignField() && !shouldExpand && fieldValue != null) {
			try {
				Method method = fieldValue.getClass().getMethod("getIdentity");
				Object obj = method.invoke(fieldValue);
				
				TypeAdapter t = context.getAdapter(obj.getClass());
				t.write(writer, obj);
			} catch (Exception ex) {
				// ignore the field if there was an error
				writer.nullValue();
			}
		} else if ((isForeignField() || isForeignCollection()) && fieldValue != null) {
			TypeAdapter t = context.getAdapter(fieldValue.getClass());
			
			if (t instanceof DaoTypeAdapter) {
				DaoTypeAdapter<?> adapter = (DaoTypeAdapter<?>) t;
				
				ModelSerializationStrategy policy = serializationStrategy.getFieldSerializationStrategy(this.field);
				if (policy != null && policy.shouldRefreshField(this.field) && fieldValue instanceof Model) {
					try {
						((Model) fieldValue).refresh();
					} catch (SQLException e) {
						// Ignored
					}
				}
				
				// Field serialization policy takes precedence of the serialization
				// policy specified for the type adapter factory
				ModelSerializationStrategy strategy = adapter.setSerializationStrategy(policy);
				t.write(writer, fieldValue);
				adapter.setSerializationStrategy(strategy);
			} else {
				t.write(writer, fieldValue);
			}
		} else {
			TypeAdapter t = new TypeAdapterRuntimeTypeWrapper(
					context, 
					typeAdapter,
					fieldType.getType());
			
			t.write(writer, fieldValue);
		}
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void read(JsonReader reader, final Object value, String fieldName) throws IOException, IllegalAccessException {
		boolean isPrimitive = Primitives.isPrimitive(fieldType.getRawType());
		if (typeAdapter instanceof ForeignCollectionTypeAdapter) {
			((ForeignCollectionTypeAdapter) typeAdapter).setDeserializationContext(new ForeignCollectionDeserializationContext() {
				
				@Override
				public Object getParent() {
					return value;
				}
				
				@Override
				public String getColumnName() {
					ForeignCollectionField fcField = field.getAnnotation(ForeignCollectionField.class);
					return fcField == null ? name : fcField.columnName();
				}
				
				@Override
				public String getOrderColumnName() {
					ForeignCollectionField fcField = field.getAnnotation(ForeignCollectionField.class);
					return fcField == null ? "" : fcField.orderColumnName();
				}
				
				@Override
				public boolean getOrderAscending() {
					ForeignCollectionField fcField = field.getAnnotation(ForeignCollectionField.class);
					return fcField == null ? true : fcField.orderAscending();
				}
				
			});
		}
		
		Object fieldValue = typeAdapter.read(reader);
		if (fieldValue != null || !isPrimitive) {
			if (isPasswordField()) {
				try {
					// Encrypt the password
					PasswordField pwdField = field.getAnnotation(PasswordField.class);
					PasswordEncryptor encryptor = pwdField.encryptorClass().newInstance();
					field.set(value, encryptor.encryptPassword((String) fieldValue));
				} catch (InstantiationException e) {
					// Ignored
				} catch (ClassCastException ex) {
					// Ignored
				}
			} else {
				field.set(value, fieldValue);
			}
		}
	}

}
