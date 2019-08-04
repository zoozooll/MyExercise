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

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * This is the class to retrieve information on individual data model(s)
 * 
 * @author Stanley Lam
 *
 */
public final class Schema {
	
	public static final String UTC_TIME_ZONE	= "UTC";
	
	private static final Map<Type, ModelAttributes> cachedAttributes = 
		Collections.synchronizedMap(new HashMap<Type, ModelAttributes>());
	
	/**
	 * Returns the attribute for {@code} raw
	 * 
	 * @param raw
	 *          The type whose attribute to return
	 * @return The ModelAttributes object for the given type
	 */
	public static ModelAttributes getAttributes(Class<?> raw) {
		ModelAttributes attributes = cachedAttributes.get(raw);
		if (attributes != null) {
			return attributes;
		}
		
		attributes = new ModelAttributes(raw);
		cachedAttributes.put(raw, attributes);
		return attributes;
	}
	
	/**
	 * Returns the current date time in the predefined time zone
	 * @return The current Date in the given time zone
	 * @deprecated Time zone of the schema is set during initialization of the
	 *             application. Use new Date() instead
	 */
	@Deprecated
	public static Date getCurrentDate() {
		TimeZone.setDefault(TimeZone.getTimeZone(UTC_TIME_ZONE));
		return new Date();
	}

}
