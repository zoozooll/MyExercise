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

import java.lang.reflect.Type;
import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.support.ConnectionSource;
import com.oregonscientific.meep.database.PassiveForeignCollection;

public class ForeignCollectionInstanceCreator {
	
	private final ConnectionSource connectionSource;
	
	public ForeignCollectionInstanceCreator(ConnectionSource connectionSource) {
		this.connectionSource = connectionSource;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ForeignCollection<?> createInstance(
			Type type, 
			Object parent, 
			String columnName, 
			String orderColumnName, 
			boolean orderAscending) {
		
		try {
			Dao<?, ?> dao = (Dao<?, ?>) DaoManager.createDao(connectionSource, (Class<?>) type);
			FieldType fieldType = dao.findForeignFieldType((Class<?>) type);
			return new PassiveForeignCollection(dao, parent, null, fieldType, orderColumnName, orderAscending);
		} catch (SQLException ex) {
			return null;
		}
	}

}
