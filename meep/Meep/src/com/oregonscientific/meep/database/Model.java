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
package com.oregonscientific.meep.database;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

import org.jasypt.util.password.PasswordEncryptor;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.stream.JsonReader;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.oregonscientific.meep.database.internal.DateTypeAdapter;
import com.oregonscientific.meep.database.internal.ForeignCollectionInstanceCreator;
import com.oregonscientific.meep.database.internal.ForeignCollectionTypeAdapterFactory;
import com.oregonscientific.meep.database.internal.ModelSerializationPolicy;
import com.oregonscientific.meep.database.internal.ModelSerializationStrategy;
import com.oregonscientific.meep.database.internal.ModelTypeAdapterFactory;

/**
 * The Model object that represents an object in the underlying datastore
 */
public abstract class Model<T, ID> extends BaseDaoEnabled<T, ID> implements Parcelable {
	
	private static final String LOG_TAG = "Model";
	
	// For QueryBuilder to be able to find the fields
	public static final String CREATED_DATE_FIELD_NAME = "createddate";
	public static final String LAST_MODIFIED_DATE_FIELD_NAME = "lastmoddate";
	
	@DatabaseField(
			columnName = CREATED_DATE_FIELD_NAME,
			canBeNull = true,
			dataType = DataType.DATE_STRING,
			format = "yyyy-MM-dd HH:mm:ss")
	@SerializedName(CREATED_DATE_FIELD_NAME)
	@Expose
	@Omit
	private Date createdDate;
	
	@DatabaseField(
			columnName = LAST_MODIFIED_DATE_FIELD_NAME,
			canBeNull = false,
			dataType = DataType.DATE_STRING,
			version = true,
			format = "yyyy-MM-dd HH:mm:ss")
	@SerializedName(LAST_MODIFIED_DATE_FIELD_NAME)
	@Expose
	@Omit
	private Date lastModifiedDate;
	
	protected Model() {
		setCreatedDate();
		setLastModifiedDate(new Date());
	}
	
	/**
	 * Sets the field represented by the field object with the given name
	 * 
	 * @param fieldName
	 *          the name of the field
	 * @param value
	 *          the value of the field
	 */
	public void setFieldValue(String fieldName, Object value) {
		try {
			ModelAttributes attributes = Schema.getAttributes(getClass());
			Field field = attributes.getColumns().get(fieldName);
			if (attributes.isPasswordColumn(fieldName)) {
				setPasswordFieldValue(field, value);
			} else {
				field.setAccessible(true);
				field.set(this, value);
			}
		} catch (Exception ex) {
			// ignores any error that may occur while accessing the field
		}
	}

	/**
	 * Sets the password field represented by the field object
	 * 
	 * @param f
	 *          the password field
	 * @param value
	 *          the value of the field
	 */
	protected void setPasswordFieldValue(Field f, Object value) {
		try {
			if (value instanceof String) {
				PasswordField pwdField = f.getAnnotation(PasswordField.class);
				PasswordEncryptor encryptor = pwdField.encryptorClass().newInstance();
				value = encryptor.encryptPassword((String) value);

				f.setAccessible(true);
				f.set(this, value);
			}
		} catch (Exception ex) {
			// Ignored
		}
	}
	
	private void setCreatedDate() {
		this.createdDate = new Date();
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setLastModifiedDate(Date newValue) {
		if (newValue != null) {
			lastModifiedDate = newValue;
		}
	}
	
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	
	/**
	 * Returns whether or not the field should be excluded in equality comparison 
	 * @return true if the field should be excluded, false otherwise
	 */
	protected boolean excludeFieldInEqualComparison(Field field) {
		ModelAttributes attrs = Schema.getAttributes(getClass());
		return attrs.shouldFieldBeOmitted(field);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != getClass()) {
			return false;
		}

		boolean result = true;
		ModelAttributes attributes = Schema.getAttributes(getClass());
		Map<String, Field> columns = attributes.getColumns();
		for (Field field : columns.values()) {
			try {
				// Skip comparing the fields to be excluded in comparison
				if (excludeFieldInEqualComparison(field)) {
					continue;
				}
				
				field.setAccessible(true);
				Object value = field.get(this);
				Object otherValue = field.get(other);

				if (value == null || otherValue == null) {
					result = value == otherValue;
				} else {
					result = value.equals(otherValue);
				}
				if (!result) {
					break;
				}
			} catch (Exception ex) {
				// The field(s) were not comparable
				result = false;
				break;
			}
		}
		return result;
	}
	
	@Override
	public int hashCode() {
		int result = 0;
		
		ModelAttributes attributes = Schema.getAttributes(getClass());
		Map<String, Field> columns = attributes.getColumns();
		for (Field field : columns.values()) {
			try {
				// Skip fields to be excluded in comparison
				if (excludeFieldInEqualComparison(field)) {
					continue;
				}
				
				field.setAccessible(true);
				Object value = field.get(this);
				result += value.hashCode();
			} catch (Exception ex) {
				// The field(s) cannot be accessed
				result = 0;
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the type adapter factory to use for JSON de/serialization of the
	 * data model for the given data model
	 * 
	 * @return the type adapter factory to use during serialization
	 */
	public static <T> ModelTypeAdapterFactory getTypeAdapterFactory(Class<T> type) {
		SerializationTypeAdapterFactory adapter = type == null ? null : type.getAnnotation(SerializationTypeAdapterFactory.class);
		Class<?> adapterClass = adapter == null ? ModelTypeAdapterFactory.class : adapter.value();
		ModelTypeAdapterFactory result = null;

		try {
			Constructor<?> constructor = adapterClass.getConstructor(new Class[] {
					ConstructorConstructor.class, 
					FieldNamingStrategy.class, 
					Excluder.class });
			result = (ModelTypeAdapterFactory) constructor.newInstance(new Object[] {
					new ConstructorConstructor(), 
					FieldNamingPolicy.IDENTITY,
					Excluder.DEFAULT.excludeFieldsWithoutExposeAnnotation() });
		} catch (Exception ex) {
			// Creates a custom type factory for data models
			result = new ModelTypeAdapterFactory(
					new ConstructorConstructor(),
					FieldNamingPolicy.IDENTITY, 
					Excluder.DEFAULT.excludeFieldsWithoutExposeAnnotation());
		}

		return result;
	}
	
	/**
	 * Converts the Model into its equivalent JSON representation
	 * 
	 * @return JSON string representation of the Model
	 */
	public String toJson() {
		return toJson(ModelSerializationPolicy.DEFAULT.disableIdFieldOnlySerialization());
	}
	
	/**
	 * Serializes the model object into its JSON equivalent JSON representation.
	 * 
	 * @param serializationStrategy
	 *          A list of properties whose value should be represented inline
	 * @return JSON String representation of the object
	 */
	public String toJson(ModelSerializationStrategy serializationStrategy) {
		// Creates a custom type factory for data models
		ModelTypeAdapterFactory modelTypeAdapterFactory = getTypeAdapterFactory(getClass());
		modelTypeAdapterFactory.registerSerializationAdapter(getClass(), serializationStrategy);
		ForeignCollectionTypeAdapterFactory fcTypeAdapterFactory = new ForeignCollectionTypeAdapterFactory();

		// Creates the JSON builder
		GsonBuilder gsonb = new GsonBuilder();
		gsonb.registerTypeAdapterFactory(modelTypeAdapterFactory);
		gsonb.registerTypeAdapterFactory(fcTypeAdapterFactory);
		gsonb.registerTypeAdapter(Date.class, new DateTypeAdapter());
		Gson gson = gsonb.serializeNulls().create();

		// Serialize the data model into a JSON representation
		return gson.toJson(this);
	}
	
	/**
	 * Deserializes the JSON element into a Model object
	 * @param json The JSON element to deserialize
	 * @param type The class of T
	 * @throws JsonParseException if json is not a valid representation for model
	 * @throws JsonSyntaxException if json is not a valid representation for an
	 *           object of type
	 * @throws IOException
	 */
	public static <T> T fromJson(JsonElement json, Class<T> type) throws JsonSyntaxException, JsonParseException, IOException {
		return fromJson(json, null, type);
	}
	
	/**
	 * Deserializes the JSON element into a Model object
	 * @param json The JSON element to deserialize
	 * @param creator
	 *          the instance creator for creating foreign collection for the Model
	 * @param type The class of T
	 * @throws JsonParseException if json is not a valid representation for model
	 * @throws JsonSyntaxException if json is not a valid representation for an
	 *           object of type
	 * @throws IOException
	 */
	public static <T> T fromJson(
			JsonElement json, 
			ForeignCollectionInstanceCreator creator,  
			Class<T> type) throws JsonSyntaxException, JsonParseException, IOException {
		
		if (json == null || !json.isJsonObject()) {
			throw new JsonParseException("The JSON was not a '" + type.getSimpleName() + "' object");
		}
		return fromJson(json.getAsJsonObject(), creator, type);
	}
	
	/**
	 * Deserializes the JSON element into a Model object
	 * @param <T> The type of the underlying Model object
	 * @param json The JSON element to deserialize
	 * @param type The class of T
	 * @throws JsonSyntaxException if json is not a valid representation for an
	 *           object of type
	 */
	public static <T> T fromJson(JsonObject json, Class<T> type) throws JsonSyntaxException {
		return fromJson(json, null, type);
	}
	
	/**
	 * Deserializes the JSON element into a Model object
	 * @param <T> The type of the underlying Model object
	 * @param json The JSON element to deserialize
	 * @param creator
	 *          the instance creator for creating foreign collection for the Model
	 * @param type The class of T
	 * @throws JsonSyntaxException if json is not a valid representation for an
	 *           object of type
	 */
	public static <T> T fromJson(
			JsonObject json, 
			ForeignCollectionInstanceCreator creator, 
			Class<T> type) throws JsonSyntaxException {
		
		if (json == null) {
			return null;
		}
		return fromJson(json.toString(), creator, type);
	}
	
	/**
	 * Deserializes the JSON element into a Model object
	 * @param <T> The type of the underlying Model object
	 * @param json The JSON element to deserialize
	 * @param type The class of T
	 * @throws JsonSyntaxException if json is not a valid representation for an
	 *           object of type
	 */
	public static <T> T fromJson(String json, Class<T> type) throws JsonSyntaxException {
		return fromJson(json, null, type);
	}
	
	/**
	 * Deserializes the JSON element into a Model object
	 * @param <T> The type of the underlying Model object
	 * @param json The JSON element to deserialize
	 * @param creator
	 *          the instance creator for creating foreign collection for the Model
	 * @param type The class of T
	 * @throws JsonSyntaxException if json is not a valid representation for an
	 *           object of type
	 */
	public static <T> T fromJson(
			String json, 
			ForeignCollectionInstanceCreator creator, 
			Class<T> type) throws JsonSyntaxException {
		
		if (json == null) {
			return null;
		}
		
		StringReader reader = new StringReader(json);
		T result = fromJson(reader, creator, type);
		consumeReader(reader);
		return result;
	}
	
	/**
	 * Deserializes the JSON element into a Model object
	 * @param <T> The type of the underlying Model object
	 * @param json The JSON element to deserialize
	 * @param type The class of T
	 * @throws JsonSyntaxException if json is not a valid representation for an
	 *           object of type
	 */
	public static <T> T fromJson(Reader json, Class<T> type) throws JsonSyntaxException {
		return fromJson(json, null,  type);
	}
	
	/**
	 * Deserializes the JSON element into a Model object
	 * @param <T> The type of the underlying Model object
	 * @param json The JSON element to deserialize
	 * @param creator
	 *          the instance creator for creating foreign collection for the Model
	 * @param type The class of T
	 * @throws JsonSyntaxException if json is not a valid representation for an
	 *           object of type
	 */
	public static <T> T fromJson(
			Reader json, 
			ForeignCollectionInstanceCreator creator, 
			Class<T> type) throws JsonSyntaxException {
		
		if (json == null) {
			return null;
		}
		
		try {
			JsonReader reader = new JsonReader(json);
			T result = fromJson(reader, creator, type);
			reader.close();
			return result;
		} catch (IOException ex) {
			throw new JsonIOException(ex);
		}
	}
	
	/**
	 * Reads the next JSON value from {@code reader} and convert it to a Model
	 * object
	 * @param <T> The type of the underlying Model object
	 * @param reader The reader to read JSON value from
	 * @param type The class of T
	 * @return the object of type T
	 * @throws JsonSyntaxException if json is not a valid representation for an
	 *           object of type
	 */
	public static <T> T fromJson(JsonReader reader, Class<T> type) throws JsonSyntaxException {
		return fromJson(reader, null, type);
	}
	
	/**
	 * Reads the next JSON value from {@code reader} and convert it to a Model
	 * object
	 * @param <T> The type of the underlying Model object
	 * @param reader The reader to read JSON value from
	 * @param creator
	 *          the instance creator for creating foreign collection for the Model
	 * @param type The class of T
	 * @return the object of type T
	 * @throws JsonSyntaxException if json is not a valid representation for an
	 *           object of type
	 */
	public static <T> T fromJson(
			JsonReader reader, 
			ForeignCollectionInstanceCreator creator, 
			Class<T> type) throws JsonSyntaxException {
		
		if (reader == null) {
			return null;
		}
		
		// Creates a custom type factory for data models
		ModelTypeAdapterFactory modelTypeAdapterFactory = getTypeAdapterFactory(type);
		ForeignCollectionTypeAdapterFactory fcTypeAdapterFactory = new ForeignCollectionTypeAdapterFactory(creator);

		// Creates the JSON builder
		GsonBuilder gsonb = new GsonBuilder();
		gsonb.registerTypeAdapterFactory(modelTypeAdapterFactory);
		gsonb.registerTypeAdapterFactory(fcTypeAdapterFactory);
		gsonb.registerTypeAdapter(Date.class, new DateTypeAdapter());
		Gson gson = gsonb.serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
		
		return gson.fromJson(reader, type);
	}
	
	private static void consumeReader(Reader reader) {
		try {
			reader.close();
		} catch (IOException ex) {
			throw new JsonIOException(ex);
		}
	}
		
	/**
	 * Parses the specified Object into this Permission
	 * 
	 * @param object the object to parse
	 */
	public void parseObject(Object object) {
		// Quick return if there is nothing to process
		if (object == null) {
			return;
		}
		
		ModelAttributes attr = Schema.getAttributes(getClass());
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			// Retrieve the Field in this object with the name matching
			// the Field in the parsing object
			Field fld = attr.getField(field.getName());
			
			if (fld != null) {
				try {
					fld.setAccessible(true);
					field.setAccessible(true);
					
					fld.set(this, field.get(object));
				} catch (Exception ex) {
					// Cannot parse the object. Continue
				}
			}
		}
	}
	
	/** Implement the Parcelable interface {@hide} */
	@Override
	public int describeContents() {
		return 0;
	}
	
	/** Implement the Parcelable interface {@hide} */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getClass().getName());
		dest.writeString(toJson());
	}
	
	/** Implement the Parcelable interface {@hide} */
	public static final Creator<Object> CREATOR = new Creator<Object>() {
		
		@Override
		public Object createFromParcel(Parcel source) {
			Object result = null;
			try {
				String className = source.readString();
				result = (Model<?, ?>) Model.fromJson(source.readString(), Class.forName(className));
			} catch (Exception ex) {
				// Cannot create a Model object from a Parcel. Ignored.
				Log.e(LOG_TAG, "Cannot create Model from a Parcel: " + source.toString());
			}
			
			return result;
		}
		
		@Override
		public Object[] newArray(int size) {
			return new Object[size];
		}
		
	};
	
}
