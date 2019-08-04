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
package com.oregonscientific.meep.database;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;

/**
 * This class is the same as {@link com.j256.ormlite.android.apptools.OpenHelperManager} 
 * exception that it allows an application to use multiple {@link OrmLiteSqliteOpenHelper}
 */
public class OpenHelperManager {
	
	private static Logger logger = LoggerFactory.getLogger(OpenHelperManager.class);
	
	private static Map<Class<?>, OpenHelperInstance> helperMap = null;
	
	/**
	 * Lookup a OpenHelper instance from cache
	 * 
	 * @param clazz The class of the OpenHelper
	 */
	private synchronized static OpenHelperInstance lookupInstance(Class<? extends OrmLiteSqliteOpenHelper> clazz) {
		if (helperMap == null) {
			helperMap = new HashMap<Class<?>, OpenHelperInstance>();
		}
		
		OpenHelperInstance instance = helperMap.get(clazz);
		if (instance == null) {
			return null;
		} else {
			return instance;
		}
	}
	
	/**
	 * Adds the given open helper instance of cache
	 * 
	 * @param clazz the helper class
	 * @param instance the helper instance
	 */
	private synchronized static void addHelperToMap(Class<? extends OrmLiteSqliteOpenHelper> clazz, OpenHelperInstance instance) {
		if (helperMap == null) {
			helperMap = new HashMap<Class<?>, OpenHelperInstance>();
		}
		
		if (instance != null && clazz != null) {
			helperMap.put(clazz, instance);
		}
	}
	
	/**
	 * Removes cached instance of the given type from cache 
	 * 
	 * @param clazz The class of the instance to remove
	 */
	private synchronized static void removeHelperFromMap(Class<? extends OrmLiteSqliteOpenHelper> clazz) {
		if (helperMap != null) {
			helperMap.remove(clazz);
		}
	}
	
	/**
	 * Creates an instance of the open helper from the helper class. This has a usage counter on it so make sure
	 * all calls to this method have an associated call to {@link #releaseHelper()}. This should be called during an
	 * onCreate() type of method when the application or service is starting. The caller should then keep the helper
	 * around until it is shutting down when {@link #releaseHelper()} should be called.
	 * 
	 * @param <T> The helper class
	 * @param context the context at which the open helper runs in
	 * @param clazz the helper class
	 * @return  the open helper instance
	 */
	public synchronized static <T extends OrmLiteSqliteOpenHelper> T getHelper(Context context, Class<T> clazz) {
		OpenHelperInstance instance = lookupInstance(clazz);
		if (instance == null) {
			instance = new OpenHelperInstance(clazz);
		}
		
		if (instance != null) {
			addHelperToMap(clazz, instance);
		}
		
		@SuppressWarnings("unchecked")
		T helper = (T) instance.load(context);
		return helper;
	}
	
	/**
	 * Release the helper that was previously loaded by calling {@link #getHelper(Context, Class)}.
	 * This will decrement the reference count, close the helper and remove from cache if the 
	 * reference count reached 0 
	 * 
	 * @param clazz The class of the helper to release
	 */
	public synchronized static void releaseHelper(Class<? extends OrmLiteSqliteOpenHelper> clazz) {
		OpenHelperInstance instance = lookupInstance(clazz);
		if (instance != null) {
			// Release an instance and remove from cache if the number of 
			// references to the instance reached 0
			instance.release();
			if (instance.wasClosed) {
				removeHelperFromMap(clazz);
			}
		}
	}
	
	/**
	 * The cached OtemLiteSqliteOpenHelper instance
	 */
	private static final class OpenHelperInstance {
		
		private int instanceCount = 0;
		private boolean wasClosed = false;
		private OrmLiteSqliteOpenHelper helper = null;
		
		private final Class<? extends OrmLiteSqliteOpenHelper> openHelperClass;
		
		OpenHelperInstance(Class<? extends OrmLiteSqliteOpenHelper> clazz) {
			openHelperClass = clazz;
		}
		
		/**
		 * Construct the SqliteOpenHelper instance 
		 * 
		 * @param context the context at which the instance should be constructed
		 * @return The constructed instance
		 */
		private OrmLiteSqliteOpenHelper construct(Context context) {
			// Finds constructor for the SqliteOpenHelper class
			Constructor<?> constructor;
			try {
				constructor = openHelperClass.getConstructor(Context.class);
			} catch (Exception e) {
				throw new IllegalStateException(
						"Could not find constructor that hast just a (Context) argument for helper class "
								+ openHelperClass, e);
			}
			
			try {
				return (OrmLiteSqliteOpenHelper) constructor.newInstance(context);
			} catch (Exception e) {
				throw new IllegalStateException("Could not construct instance of helper class " + openHelperClass, e);
			}
		}
		
		synchronized void release() {
			instanceCount --;
			logger.trace("Releasing helper {}, instance count = {}", helper, instanceCount);
			if (instanceCount <= 0) {
				if (helper != null) {
					logger.trace("No instance referenced, closing helper {}", helper);
					helper.close();
					helper = null;
					wasClosed = true;
				}
				
				if (instanceCount < 0) {
					logger.error("Too many calls to release helper, instance count = {}", instanceCount);
				}
			}
		}
		
		/**
		 * Loads the helper
		 * @param <T> The OrmLiteSqliteOpenHelper class
		 * @param context the context at which the instance runs in. 
		 * @return the OrmLiteSqliteOpenHelper instance
		 */
		synchronized <T extends OrmLiteSqliteOpenHelper> T load(Context context) {
			if (helper == null) {
				if (wasClosed) {
					// This can happen if you are calling get/release and then get again
					logger.info("Helper was already closed and is being re-opened");
				}
				
				if (context == null) {
					throw new IllegalArgumentException("context argument is null");
				}
				
				Context appContext = context.getApplicationContext();
				helper = construct(appContext);
				logger.trace("No instance, created helper {}", helper);
				
				// Clears all cached DAO objects so that no object is cached against a
				// closed connection 
				BaseDaoImpl.clearAllInternalObjectCaches();
				DaoManager.clearDaoCache();
				instanceCount = 0;
			}
			
			instanceCount ++;
			
			logger.trace("Returning helper {}, instance count = {} ", helper, instanceCount);
			@SuppressWarnings("unchecked")
			T castHelper = (T) helper;
			return castHelper;
		}
	}

}
