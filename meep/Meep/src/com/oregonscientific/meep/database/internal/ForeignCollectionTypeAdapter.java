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

/**
 * The type adapter for ORMLite ForeignCollection objects in GSON serialization/deserialization
 * 
 * @author Stanley Lam
 */
public abstract class ForeignCollectionTypeAdapter<T> extends DaoTypeAdapter<T> {

	abstract void setDeserializationContext(ForeignCollectionDeserializationContext context);
}
