/**
 * Adopted from ORMLite
 * 
 * Licensed under the agreement as set out in the License for ORMLite 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oregonscientific.meep;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.oregonscientific.meep.database.OpenHelperManager;

import android.app.Service;
import android.content.Context;

/**
 * Base class to use for services that requires database access through ORMLite
 */
public abstract class DatabaseService<H extends OrmLiteSqliteOpenHelper> extends Service {
	
	private volatile H helper;
	private volatile boolean created = false;
	private volatile boolean destroyed = false;
	
	/**
	 * Lookup the helper class by looking for a generic type parameter
	 * 
	 * @param context The context at which to lookup for the helper class
	 * @param clazz The {@link Class} of the helper
	 * @return The {@link Class} of the open helper
	 */
	private Class<? extends OrmLiteSqliteOpenHelper> lookupHelperClass(Context context, Class<?> clazz) {
		// Try walking the context class to see if we can get the
		// OrmLiteSqliteOpenHelper from a generic parameter
		for (Class<?> clazzWalk = clazz; clazzWalk != null; clazzWalk = clazzWalk.getSuperclass()) {
			Type superType = clazzWalk.getGenericSuperclass();
			if (superType == null || !(superType instanceof ParameterizedType)) {
				continue;
			}
			
			// Get the generic type arguments
			Type[] types = ((ParameterizedType) superType).getActualTypeArguments();
			// defense
			if (types == null || types.length == 0) {
				continue;
			}
			for (Type type : types) {
				// defense
				if (!(type instanceof Class)) {
					continue;
				}
				Class<?> helperClass = (Class<?>) type;
				if (OrmLiteSqliteOpenHelper.class.isAssignableFrom(helperClass)) {
					@SuppressWarnings("unchecked")
					Class<? extends OrmLiteSqliteOpenHelper> castOpenHelperClass = (Class<? extends OrmLiteSqliteOpenHelper>) helperClass;
					return castOpenHelperClass;
				}
			}
		}
		
		// Throw an exception if the SqliteOpenHelper class cannot be found
		throw new IllegalStateException(
				"Could not find OpenHelperClass because none of the generic parameters of class " + clazz
						+ " extends OrmLiteSqliteOpenHelper.  You should use getHelper(Context, Class) instead.");
	}
	
	/**
	 * Get a helper for this action.
	 */
	public H getHelper() {
		if (helper == null) {
			if (!created) {
				throw new IllegalStateException("A call has not been made to onCreate() yet so the helper is null");
			} else if (destroyed) {
				throw new IllegalStateException("A call to onDestroy has already been made and the helper cannot be used after that point");
			} else {
				throw new IllegalStateException("Helper is null for some unknown reason");
			}
		} else {
			return helper;
		}
	}

	/**
	 * Get a connection source for this action.
	 */
	public ConnectionSource getConnectionSource() {
		return getHelper().getConnectionSource();
	}
	
	@Override
	public void onCreate() {
		if (helper == null) {
			helper = getHelperInternal(this);
			created = true;
		}
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		releaseHelper(helper);
		destroyed = true;
	}
	
	/**
	 * Returns the type of the helper class. This method walk through the parameterized type of 
	 * the class to retrieve the helper class. User can override this method to return the helper
	 * class directly
	 */
	@SuppressWarnings("unchecked")
	protected Class<H> getHelperClass() {
		return (Class<H>) lookupHelperClass(this, this.getClass());
	}
	
	/**
	 * This is called internally by the class to populate the helper object instance. This should not be called directly
	 * by client code unless you know what you are doing. Use {@link #getHelper()} to get a helper instance. If you are
	 * managing your own helper creation, override this method to supply this activity with a helper instance.
	 * 
	 * <p>
	 * <b> NOTE: </b> If you override this method, you most likely will need to override the
	 * {@link #releaseHelper(OrmLiteSqliteOpenHelper)} method as well.
	 * </p>
	 */
	protected H getHelperInternal(Context context) {
		H newHelper = (H) OpenHelperManager.getHelper(context, getHelperClass());
		return newHelper;
	}
	
	/**
	 * Release the helper instance created in {@link #getHelperInternal(Context)}. You most likely will not need to call
	 * this directly since {@link #onDestroy()} does it for you.
	 * 
	 * <p>
	 * <b> NOTE: </b> If you override this method, you most likely will need to override the
	 * {@link #getHelperInternal(Context)} method as well.
	 * </p>
	 */
	protected void releaseHelper(H helper) {
		OpenHelperManager.releaseHelper(getHelperClass());
		this.helper = null;
	}

}
