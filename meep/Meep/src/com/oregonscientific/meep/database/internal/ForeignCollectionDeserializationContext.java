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

import com.j256.ormlite.dao.ForeignCollection;

/**
 * Context for deserialization of {@link ForeignCollection} that is passed to an adapter
 * 
 * @author Stanley Lam
 */
public interface ForeignCollectionDeserializationContext {
	
	/**
	 * Returns the parent object of the foreign collection
	 */
	public Object getParent();
	
	/**
	 * Returns the name of the of the column
	 * @return A string of the name of the column 
	 */
	public String getColumnName();
	
	/**
	 * The name of the column in the object that we should order by.
	 * @return A string containing the name of the column in the object
	 * that the result set should be ordered by
	 */
	public String getOrderColumnName();
	
	/**
	 * Specifies whether the order should be ascending (the default) or descending
	 * @return true if order should be ascending, false otherwise
	 */
	public boolean getOrderAscending();

}
