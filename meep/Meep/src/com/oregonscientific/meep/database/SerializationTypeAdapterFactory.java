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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;
import com.oregonscientific.meep.database.internal.ModelTypeAdapterFactory;

/**
 * An annotation that marks the type adapter to use to de/serialize the class
 * using GSON
 * 
 * @author Stanley Lam
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface SerializationTypeAdapterFactory {
	
	/**
	 * The TypeAdapterFactory class to use to de/serialize a given class. This is used by 
	 * the {@link Gson} during de/serialization
	 */
	Class<? extends TypeAdapterFactory> value() default ModelTypeAdapterFactory.class;

}
