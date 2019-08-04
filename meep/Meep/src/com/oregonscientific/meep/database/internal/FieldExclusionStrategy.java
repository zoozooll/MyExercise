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

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public abstract class FieldExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		// Do not exclude a property based on its Type
		return false;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		return shouldSkipField(f.getName());
	}
	
	/**
	 * Determines whether or not a field with the given name should be omitted
	 * @param serializedName the name of the field
	 * @return true if the Field should be omitted, false otherwise
	 */
	public abstract boolean shouldSkipField(String serializedName);

}
