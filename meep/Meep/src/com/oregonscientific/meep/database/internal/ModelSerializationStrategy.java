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

/**
 * A strategy (or policy) definition that is used to decide whether or not a field or top-level
 * class should be expanded as part of the JSON output. For serialization,
 * if the {@link #shouldExpandField(Field)} method returns false then only the id of of the foreign 
 * field will be serialized. 
 * @author Stanley Lam
 *
 */
public interface ModelSerializationStrategy {
	
	/**
	 * Returns whether or not the only the ID field of the model should be serialized
	 * @return true if only the ID field of the model should be serialized, false otherwise
	 */
	public boolean shouldSerializeIdFieldOnly();
	
	/**
	 * Returns whether or not the given field should be ignored during
	 * serialization
	 * @return true if the field should be ignored, false otherwise
	 */
	public boolean shouldSkipField(Field field);
	
	/**
	 * @param field the field to determine whether or not it should be expanded
	 * @return true if the field should be expanded in the JSON output
	 */
	public boolean shouldExpandField(Field field);
	
	/**
	 * Returns whether or not a given field should be refreshed when de/serialized
	 * @param field the field to determine whether or not it should be refreshed
	 * @return true if the field should be refreshed, false otherwise
	 */
	public boolean shouldRefreshField(Field field);
	
	/**
	 * Returns the inner expansion strategy for the given field
	 * @param field the field to return the expansion strategy
	 * @return the expansion strategy for the given field in a data model
	 */
	public ModelSerializationStrategy getFieldSerializationStrategy(Field field);

}
