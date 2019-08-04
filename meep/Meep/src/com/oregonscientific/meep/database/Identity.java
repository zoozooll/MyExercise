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

/**
 * This is an abstract API that data models implementations must conform to.
 * 
 * @author Stanley Lam
 * @param <T>
 *
 */
public interface Identity<T> {
  
  /**
   * Returns a unique identifier for an item.  The return value will be
   * either a string or something that has a toString() method
   * @return
   * 	The value of the identity
   */
  public T getIdentity();
  
  /**
   * Returns the attribute name that is used to generate the identity.
   * For most stores, this is a single attribute, but for some complex stores
   * such as RDB backed stores that use compound (multi-attribute) identifiers
   * it can be more than one.  If the identity is not composed of attributes
   * on the item, it will return null.
   * @return
   * 	An array of attribute names that are used to generate the identity of
   * 	a model
   */
  public String getIdentityAttribute();

}
