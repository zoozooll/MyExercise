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
import java.util.HashMap;
import java.util.Map;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

/**
 * A policy that defines how a data model should be serialized and select the 
 * fields that should be expanded during serialization
 * @author Stanley Lam
 */
public class ModelSerializationPolicy implements ModelSerializationStrategy, Cloneable {
	public static final ModelSerializationPolicy DEFAULT = new ModelSerializationPolicy(FieldNamingPolicy.IDENTITY);
	
	private FieldNamingStrategy fieldNamingPolicy;
	private boolean serializeIdFieldOnly = true;
	private FieldExclusionStrategy exclusionStrategy;
	private Map<String, ModelSerializationStrategy> fieldSerializationStrategies;
	private boolean refreshField = false;
	
	private ModelSerializationPolicy(FieldNamingStrategy fieldNamingPolicy) {
		this.fieldNamingPolicy = fieldNamingPolicy;
		fieldSerializationStrategies = new HashMap<String, ModelSerializationStrategy>();
	}
	
	@Override
	protected ModelSerializationPolicy clone() {
		try {
			return (ModelSerializationPolicy) super.clone();
		} catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
	}
	
	public ModelSerializationPolicy disableIdFieldOnlySerialization() {
		ModelSerializationPolicy result = clone();
		result.serializeIdFieldOnly = false;
		return result;
	}
	
	public ModelSerializationPolicy withFieldNamingPolicy(FieldNamingStrategy namingPolicy) {
		ModelSerializationPolicy result = clone();
		result.fieldNamingPolicy = namingPolicy;
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public ModelSerializationPolicy withSelectingFields(Map<String, ?> fields) {
		ModelSerializationPolicy result = clone();
		
		if (fields != null) {
			result.exclusionStrategy = new ModelFieldExclusionStrategy(fields.keySet());
			for (String field : fields.keySet()) {
				Map<String, ?> selections = (Map<String, ?>) fields.get(field);
				if (selections != null) {
					ModelSerializationPolicy policy = (ModelSerializationPolicy) result.fieldSerializationStrategies.get(field);
					if (policy == null) {
						policy = ModelSerializationPolicy
							.DEFAULT
							.disableIdFieldOnlySerialization();
					}
					result.fieldSerializationStrategies.put(field, policy.withSelectingFields(selections));
				}
			}
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public ModelSerializationPolicy withExpansionTree(Map<String, ?> fields) {
		ModelSerializationPolicy result = clone();
		// serialization strategies for the fields should not be cloned with an expansion tree
		result.fieldSerializationStrategies = new HashMap<String, ModelSerializationStrategy>();
		
		if (fields != null) {
			for (String field : fields.keySet()) {
				Map<String, ?> expansionTreeForField = (Map<String, ?>) fields.get(field);
				ModelSerializationPolicy policy = (ModelSerializationPolicy) result.fieldSerializationStrategies.get(field);
				if (policy == null) {
					policy = ModelSerializationPolicy
						.DEFAULT
						.disableIdFieldOnlySerialization();
				}
				result.fieldSerializationStrategies.put(field, policy.withExpansionTree(expansionTreeForField));
			}
		}
		
		return result;
	}
	
	/**
	 * Determines whether or not a given field is a foreign object
	 * @param field the field to determine whether or not it is a foreign object
	 * @return true if the field represents a foreign object, false otherwise
	 */
	private boolean isForeignField(Field field) {
		DatabaseField dbField = field.getAnnotation(DatabaseField.class);
		return dbField == null ? false : dbField.foreign();
	}
	
	/**
	 * Determines whether or not a given field is a foreign collection
	 * @param field the field to determine whether or not it is a foreign collection
	 * @return true if the field represents a foreign collection, false otherwise
	 */
	public boolean isForeignCollection(Field field) {
		ForeignCollectionField foreignCollectionField = field.getAnnotation(ForeignCollectionField.class);
		return foreignCollectionField != null;
	}
	
	/**
	 * Determines whether or not a given field is an eager foreign collection
	 * @param field the field to determine whether or not it is a foreign collection
	 * @return true if the field represents a eager foreign collection, false otherwise
	 */
	private boolean isEagerForeignCollection(Field field) {
		ForeignCollectionField foreignCollectionField = field.getAnnotation(ForeignCollectionField.class);
		return foreignCollectionField == null ? false : foreignCollectionField.eager();
	}
	
	/**
	 * Retrieves the serialized name of a given field
	 * @param field the field to retrieve the serialized name
	 * @return A String that represents the serialize name of a given Field
	 */
	private String getFieldSerializedName(Field field) {
		SerializedName serializedName = field.getAnnotation(SerializedName.class);
		return serializedName == null ? fieldNamingPolicy.translateName(field) : serializedName.value();
	}

	/* (non-Javadoc)
	 * @see ModelSerializationStrategy#shouldExpandField(java.lang.reflect.Field)
	 */
	@Override
	public boolean shouldExpandField(Field field) {
		String fieldName = getFieldSerializedName(field);
		
		// Refreshes children DAO if the collection was a lazy foreign collection
		if (!isEagerForeignCollection(field)) {
			for (ModelSerializationStrategy strategy : fieldSerializationStrategies.values()) {
				if (strategy instanceof ModelSerializationPolicy) {
					((ModelSerializationPolicy) strategy).refreshField = true;
				}
			}
		}
		
		return fieldSerializationStrategies.containsKey(fieldName) 
			&& (isForeignField(field) || isForeignCollection(field));
	}
	
	/* (non-Javadoc)
	 * @see ModelSerializationStrategy#shouldRefreshField(java.lang.reflect.Field)
	 */
	@Override
	public boolean shouldRefreshField(Field field) {
		return refreshField;
	}

	/* (non-Javadoc)
	 * @see ModelSerializationStrategy#getFieldSerializationStrategy(java.lang.reflect.Field)
	 */
	@Override
	public ModelSerializationStrategy getFieldSerializationStrategy(Field field) {
		return fieldSerializationStrategies.get(getFieldSerializedName(field));
	}

	@Override
	public boolean shouldSerializeIdFieldOnly() {
		return serializeIdFieldOnly;
	}
	
	@Override 
	public boolean shouldSkipField(Field field) {
		String fieldName = getFieldSerializedName(field);
		return exclusionStrategy == null ? false : exclusionStrategy.shouldSkipField(fieldName);
	}

}
