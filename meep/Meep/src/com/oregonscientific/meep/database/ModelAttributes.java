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

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

public final class ModelAttributes {
	
	private final Class<?> raw;
	private final FieldNamingStrategy fieldNamingPolicy;
	private Map<String, Field> columns;
	
	/**
	 * Constructs a Model Attributes object from the {@code type}
	 * @param type the model to pull attributes from
	 */
	public ModelAttributes(Class<?> type) {
		this(type, FieldNamingPolicy.IDENTITY);
	}
	
	/**
	 * Constructs a Model Attributes object from the {@code type} with the 
	 * given {@code namingPolicy} 
	 * @param type the model to pull attributes from
	 * @param namingPolicy the naming strategy to use for naming the Field(s) 
	 */
	public ModelAttributes(Class<?> type, FieldNamingStrategy namingPolicy) {
		raw = type;
		fieldNamingPolicy = namingPolicy;
		columns = new HashMap<String, Field>();
		
		if (!type.isInterface()) {
			Class<?> rawType = raw;
			TypeToken<?> typeToken = TypeToken.get(rawType);
			while (rawType != Object.class) {
				Field[] fields = rawType.getDeclaredFields();
				for (Field field : fields) {
					if (isDatabaseField(field) || isForeignCollection(field)) {
						columns.put(getFieldSerializedName(field), field);
					}
				}
	  		
				typeToken = TypeToken.get($Gson$Types.resolve(
						typeToken.getType(), 
						rawType,
						rawType.getGenericSuperclass()));
				rawType = typeToken.getRawType();
			}
		}
	}
	
	/**
	 * Returns the name of the database table the class Model represents
	 * @return the name of the database table
	 */
	public String getTableName() {
		DatabaseTable table = raw == null ? null : raw.getAnnotation(DatabaseTable.class);
		return table == null ? raw.getSimpleName() : table.tableName();
	}
	
	/**
	 * Returns a Class object that identifies the declared type for the 
	 * field represented by this Field object. If it was a foreign field, the 
	 * declared type for the ID field associated with the foreign object is
	 * returned
	 * @param fieldName the name of the Field
	 * @return a Class object identifying the declared type of the field
	 * represented by this object 
	 */
	public Class<?> getColumnType(String fieldName) {
		Field field = columns.get(fieldName);
		if (isForeignField(field)) {
			ModelAttributes attr = new ModelAttributes(field.getType());
			return attr.getIdField() == null ? null : attr.getColumns().get(attr.getIdField()).getType();
		}
		
		return field == null ? null : field.getType();
	}
	
	/**
	 * Returns the serialized name in the foreign collection that defines the relationship
	 * between the type of the ModelAttribute and the objects on the foreign collection
	 * 
	 * @param fieldName the serialzied name of the foreign collection
	 * @return the column name that defines the relationship, null if the field specified
	 * was not a foreign collection
	 */
	public String getForeignCollectionForeignName(String fieldName) {
		String result = null;
		String foreignFieldName = null;
		
		ModelAttributes attrs = Schema.getAttributes((Class<?>) getColumnGenericType(fieldName));
		if (attrs == null || !isForeignCollection(fieldName)) {
			return result;
		}
		
		// Determines whether or not the a foreign collection field name is 
		// specified in the annotation
		ForeignCollectionField fcField = columns.get(fieldName).getAnnotation(ForeignCollectionField.class);
		foreignFieldName = fcField == null ? null : fcField.foreignFieldName();
		
		// Loop through the foreign collection object to find the field
		// that corresponds to the {@code} raw type of the model attributes
		for (String name : attrs.columns.keySet()) {
			Field field = attrs.columns.get(name);
			boolean isRequestedField = false;
			if (foreignFieldName == null || foreignFieldName.isEmpty()) {
				isRequestedField = field.getType().equals(raw);
			} else {
				isRequestedField = field.getName().equals(foreignFieldName);
			}
			
			// Found the foreign field
			if (isRequestedField) {
				result = name;
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * Returns a Type object that represents the declared type of the given column. 
	 * If the column was a foreign collection, the Type object returned reflects the 
	 * type of the containing element
	 * 
	 * @param columnName the name of the database column
	 * @return the type of the given column, null if the column was not found
	 */
	public Type getColumnGenericType(String columnName) {
		Field field = columns.get(columnName);
		
		if (isForeignCollection(field)) {
			Type genericFieldType = field.getGenericType();
				
			if (genericFieldType instanceof ParameterizedType) {
				ParameterizedType foreignCollectionType = (ParameterizedType) genericFieldType;
		    Type[] fieldArgTypes = foreignCollectionType.getActualTypeArguments();
		    return fieldArgTypes.length > 0 ? fieldArgTypes[0] : null; 
			}
		} else {
			return field == null ? null : field.getGenericType();
		}
		
		return null;
	}
	
	/**
	 * Retrieves the database column name of the a given Field
	 * @param fieldName the name of the Field
	 * @return the database column name of a given Field
	 */
	public String getColumnName(String fieldName) {
		Field field = columns.get(fieldName);
		return field == null ? "" : getColumnName(field);
	}
	
	/**
	 * Returns the {@link Field} with the given {@code fieldName}
	 * 
	 * @param fieldName the name of the field
	 * @return the {@link Field} with the given name
	 */
	public Field getField(String fieldName) {
		// Quick return if there is nothing to process 
		if (fieldName == null) {
			return null;
		}
		
		Field result = columns.get(fieldName);
		if (result == null) {
			Collection<Field> fields = columns.values();
			for (Field field : fields) {
				if (field.getName().equals(fieldName)) {
					result = field;
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * Returns the name of the ID field of the data class
	 * @return The name of the ID field, null if no ID field is found
	 */
	public String getIdField() {
		for (String fieldName : columns.keySet()) {
			if (isIdField(columns.get(fieldName))) {
				return fieldName;
			}
		}
		// Can be null if there was no ID field in a data class
		return null;
	}
	
	/**
	 * Returns whether or not the ID field of the data class is generated
	 * @return true if the ID column was auto generated, false otherwise
	 */
	public boolean isIdFieldGenerated() {
		String idFieldName = getIdField();
		return idFieldName == null ? false : isGeneratedId(columns.get(idFieldName));
	}
	
	/**
	 * Returns the columns in a data model
	 * @return a map of columns the data model represents.
	 */
	public Map<String, Field> getColumns() {
		return Collections.unmodifiableMap(columns);
	}
	
	/**
	 * Determines whether or not a Field represents an ID column
	 * @param columnName the column name of field being examined
	 * @return true if the Field represents an ID column, false otherwise
	 */
	public boolean isIdColumn(String columnName) {
		return isIdField(columns.get(columnName));
	}
	
	/**
	 * Determines whether a given column represents a foreign field
	 * @param columnName the column name of field being examined
	 * @return true if the Field represents a foreign field, false otherwise
	 */
	public boolean isForeignField(String columnName) {
		return isForeignField(columns.get(columnName));
	}
	
	/**
	 * Determines whether or not a given column is a foreign collection
	 * 
	 * @param columnName
	 *            the column name of field to determine whether it is a foreign
	 *            collection
	 * @return true if the field is a foreign collection, false otherwise
	 */
	public boolean isForeignCollection(String columnName) {
		return isForeignCollection(columns.get(columnName));
	}

	/**
	 * Determines whether or not a given field's value should be encrypted
	 * 
	 * @param columnName
	 *            the name of the database column
	 * @return true if the field should be encrypted, false otherwise
	 */
	public boolean shouldEncryptColumn(String columnName) {
		return shouldEncryptField(columns.get(columnName));
	}

	/**
	 * Determines whether or not a given column is exposed for de/serialization
	 * 
	 * @param columnName
	 *            the column name of field to determine whether it is expose
	 * @return true if the field was exposed, false otherwise
	 */
	public boolean isColumnExposed(String columnName) {
		return isFieldExposed(columns.get(columnName));
	}

	/**
	 * Determines whether or not a given field is a password field
	 * 
	 * @param columnName
	 *            the column name of field to determine whether it is a password
	 *            field
	 * @return true if the field is a password field, false otherwise
	 */
	public boolean isPasswordColumn(String columnName) {
		return isPasswordField(columns.get(columnName));
	}
	
	/**
	 * Determines whether or not the given field should be omitted in equality comparison
	 * 
	 * @param field the field to determine whether it should be omitted
	 * @return true if the field should be omitted, false otherwise
	 */
	public boolean shouldFieldBeOmitted(Field f) {
		Omit omit = f == null ? null : f.getAnnotation(Omit.class);
		return omit == null ? false : omit.value();
	}

	/**
	 * Determines whether or not a given field is a password field
	 * 
	 * @param f
	 *            the field to determine whether it is a password field
	 * @return true if the field is a password field, false otherwise
	 */
	private boolean isPasswordField(Field f) {
		PasswordField pwdField = f == null ? null : f.getAnnotation(PasswordField.class);
		return pwdField != null;
	}

	/**
	 * Determines whether or not a given field is exposed for de/serialization
	 * 
	 * @param f
	 *            the field to determine whether it is expose
	 * @return true if the field was exposed, false otherwise
	 */
	private boolean isFieldExposed(Field f) {
		Expose expose = f == null ? null : f.getAnnotation(Expose.class);
		return expose == null ? false : true;
	}

	/**
	 * Determines whether a Field object is a foreign field
	 * 
	 * @param f
	 *            the field being examined
	 * @return true if the Field represents a foreign field, false otherwise
	 */
	private boolean isForeignField(Field f) {
		DatabaseField databaseField = f == null ? null : f.getAnnotation(DatabaseField.class);
		return databaseField == null ? false : databaseField.foreign();
	}
	
	/**
	 * Determines whether or not a given field is a foreign collection
	 * 
	 * @param f
	 *            the field to determine whether it is a foreign collection
	 * @return true if the field is a foreign collection, false otherwise
	 */
	private boolean isForeignCollection(Field f) {
		ForeignCollectionField foreignCollectionField = f == null ? null : f
				.getAnnotation(ForeignCollectionField.class);
		return foreignCollectionField != null;
	}

	/**
	 * Determines whether or not a given field's value should be encrypted
	 * 
	 * @param f
	 *            the field to determine whether its value should be encrypted
	 * @return true if the field should be encrypted, false otherwise
	 */
	private boolean shouldEncryptField(Field f) {
		Encrypt encrypt = f == null ? null : f.getAnnotation(Encrypt.class);
		return encrypt != null;
	}

	/**
	 * Determines whether or not a Field represents an ID column
	 * 
	 * @param f
	 *            the field being examined
	 * @return true if the Field represents an ID column, false otherwise
	 */
	private boolean isIdField(Field f) {
		DatabaseField databaseField = f == null ? null : f.getAnnotation(DatabaseField.class);
		return databaseField == null ? false
				: databaseField.id()
						|| databaseField.generatedId()
						|| (databaseField.generatedIdSequence() != null && !databaseField.generatedIdSequence().isEmpty());
	}

	/**
	 * Determines whether or not a Field represents an auto generated ID column
	 * 
	 * @param f
	 *            the field being examined
	 * @return true if the Field represents an auto generated ID column, false
	 *         otherwise
	 */
	private boolean isGeneratedId(Field f) {
		DatabaseField databaseField = f == null ? null : f.getAnnotation(DatabaseField.class);
		return databaseField == null ? false : databaseField.generatedId() || !databaseField.generatedIdSequence().isEmpty();
	}

	/**
	 * Determines whether or not a Field represents a column in the database
	 * table
	 * 
	 * @param f
	 *            the field being examined
	 * @return true if the Field represents a database column, false otherwise
	 */
	private boolean isDatabaseField(Field f) {
		DatabaseField databaseField = f == null ? null : f.getAnnotation(DatabaseField.class);
		return databaseField != null;
	}

	/**
	 * Retrieves the database column name of the a given Field
	 * 
	 * @param f
	 *            the Field object
	 * @return the database column name of a given Field
	 */
	private String getColumnName(Field f) {
		DatabaseField databaseField = f == null ? null : f.getAnnotation(DatabaseField.class);
		return databaseField == null ? f == null ? "" : f.getName() : databaseField.columnName();
	}

	/**
	 * Returns the name of the given column
	 * 
	 * @param field
	 *            the field to return the name
	 * @return the name of the field
	 */
	private String getFieldSerializedName(Field field) {
		return getFieldSerializedName(field, fieldNamingPolicy);
	}

	/**
	 * Returns the serialized name of the column using the given naming policy
	 * 
	 * @param f
	 *            the field to return the name
	 * @param namingPolicy
	 *            the naming policy used to serialize name of the column
	 * @return the name of the field
	 */
	private String getFieldSerializedName(Field f, FieldNamingStrategy namingPolicy) {
		SerializedName serializedName = f == null ? null : f.getAnnotation(SerializedName.class);
		return serializedName == null ? namingPolicy.translateName(f) : serializedName.value();
	}

}
