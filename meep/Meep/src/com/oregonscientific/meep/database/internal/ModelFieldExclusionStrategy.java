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

import java.util.ArrayList;
import java.util.List;

/**
 * The exclusion strategy to use while de/serializing a data model
 * 
 * @author Stanley Lam
 */
public class ModelFieldExclusionStrategy extends FieldExclusionStrategy {
	
	private final List<String> selectedFields;
	
	public ModelFieldExclusionStrategy(List<String> selectedFields) {
		this.selectedFields = selectedFields;
	}
	
	public ModelFieldExclusionStrategy(Iterable<String> selectedFields) {
		this.selectedFields = new ArrayList<String>();
		for (String field : selectedFields) {
			this.selectedFields.add(field);
		}
	}
	
	/* (non-Javadoc)
	 * @see FieldExclusionStrategy#shouldSkipField(String)
	 */
	@Override
	public boolean shouldSkipField(String serializedName) {
		// Exclude a field if it was not one of the selected fields
		return selectedFields.isEmpty() ? false : !selectedFields.contains(serializedName);
	}

}
