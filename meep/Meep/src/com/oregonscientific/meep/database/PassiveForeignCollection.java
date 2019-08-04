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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.j256.ormlite.dao.BaseForeignCollection;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.CloseableWrappedIterable;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.mapped.MappedPreparedStmt;
import com.j256.ormlite.support.DatabaseResults;

public class PassiveForeignCollection<T, ID> extends BaseForeignCollection<T, ID> implements
		ForeignCollection<T>, CloseableWrappedIterable<T>, Serializable {
	
	private static final long serialVersionUID = -753001814069713499L;
	
	private transient List<T> collection;
	private transient final Object parent;
	private transient Object parentId;
	private transient final String orderColumn;
	private transient PreparedQuery<T> preparedQuery;
	
	public PassiveForeignCollection(
			Dao<T, ID> dao, 
			Object parent, 
			Object parentId, 
			FieldType foreignFieldType, 
			String orderColumn) {
		
		this(dao, parent, parentId, foreignFieldType, orderColumn, true);
	}
	
	public PassiveForeignCollection(
			Dao<T, ID> dao, 
			Object parent, 
			Object parentId, 
			FieldType foreignFieldType, 
			String orderColumn, 
			boolean orderAscending) {
		
		super(dao, parent, parentId, foreignFieldType, orderColumn, orderAscending);
		
		this.parent = parent;
		this.parentId = parentId;
		this.orderColumn = orderColumn;
		collection = new ArrayList<T>();
	}
	
	/**
	 * Returns the name of the column that relates the collection with the parent
	 * object
	 * 
	 * @return the name of the column that relates the collection with the parent
	 * object
	 */
	private String getColumnName() {
		ModelAttributes attrs = Schema.getAttributes(parent.getClass());
		String parentField = null;
		
		try {
			Map<String, Field> columns = attrs.getColumns();
			for (String name : columns.keySet()) {
				if (attrs.isForeignCollection(name)) {
					Field field = columns.get(name);
					field.setAccessible(true);
					Object foreign = field.get(parent);
					if (foreign.equals(this)) {
						parentField = name;
						break;
					}
				}
			}
		} catch (Exception ex) {
			// Ignored
		}
		
		return parentField == null ? "" : attrs.getForeignCollectionForeignName(parentField);
	}
	
	/**
	 * Add an element to the collection. This will not add the item to the associated 
	 * database table. The item will only be added when {@link #updateAll()} is called
	 * @param data the element to be added
	 * @return Returns true if the item did not already exist in the collection otherwise false.
	 */
	@Override
	public boolean add(T data) {
		if (data == null || data.getClass().isInterface()) {
			return false;
		}
		
		ModelAttributes attributes = Schema.getAttributes(data.getClass());
		Map<String, Field> columns = attributes.getColumns();
		for (String fieldName : columns.keySet()) {
			Field field = columns.get(fieldName);
			if (attributes.isForeignField(fieldName) && field.getType().equals(parent.getClass())) {
				// Sets the parent relationship with the given data
				try {
					field.setAccessible(true);
					field.set(data, parent);
					break;
				} catch (Exception ex) {
					// Ignores the exception(s).
				}
			}
		}
		
		// Adds the element to the collection without adding to the underlying data
		// store
		return collection.contains(data) ? false : collection.add(data);
	}
	
	/**
	 * Add the collection of elements to this collection. This will not add the item to
	 * the associated database table. The items will only be added when {@link #updateAll()}
	 * is called 
	 * @param collection a collection of elements to be added
	 * @return Returns true if the item did not already exist in the collection otherwise false.
	 */
	@Override
	public boolean addAll(Collection<? extends T> collection) {
		boolean result = false;
		for (T data : collection) {
			result = add(data);
			if (!result) {
				break;
			}
		}
		return result;
	}
	
	/**
	 * Removes the data item from the collection. This will not remove the item from the
	 * associated database table
	 * @param data the data item to remove
	 * @return true if the item was removed from the collection, false otherwise
	 */
	@Override
	public boolean remove(Object data) {
		return collection.remove(data);
	}
	
	/**
	 * Remove the items in the collection argument from the foreign collection.
	 * @param collection the foreign collection containing collection arguments where items are to be removed
	 * @return true if the collection was removed, false otherwise
	 */
	@Override
	public boolean removeAll(Collection<?> collection) {
		boolean changed = false;
		for (Object data : collection) {
			if (remove(data)) {
				changed = true;
			}
		}
		return changed;
	}

	/**
	 * @return true if collection is eager, otherwise false
	 */
	@Override
	public boolean isEager() {
		return true;
	}

	/**
	 * Returns the size of the foreign collection.
	 * @return the size of the foreign collection
	 */
	@Override
	public int size() {
		return collection.size();
	}

	/**
	 * @return true if collection is empty, otherwise false
	 */
	@Override
	public boolean isEmpty() {
		return collection.isEmpty();
	}

	/**
	 * @param o - element whose presence in this collection is to be tested.
	 * @return true if collection contains the specified element, otherwise false
	 */
	@Override
	public boolean contains(Object o) {
		return collection.contains(o);
	}

	/**
	 * @param c - collection to be checked for containment in this collection.
	 * @return true if this collection contains all of the elements in the specified collection, false otherwise
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		return collection.containsAll(c);
	}

	/**
	 * @return an array containing all of the elements in this collection
	 */
	@Override
	public Object[] toArray() {
		return collection.toArray();
	}

	/**
	 * @param array - the array into which the elements of this collection are to be
	 *          stored, if it is big enough; otherwise, a new array of the same
	 *          runtime type is allocated for this purpose.
	 * @return an array containing the elements of this collection
	 */
	@Override
	public <E> E[] toArray(E[] array) {
		return collection.toArray(array);
	}
	
	@Override
	public int refreshAll() {
		throw new UnsupportedOperationException("Cannot call refreshAll() on a passive collection.");
	}

	@Override
	public int refreshCollection() throws SQLException {
		if (dao != null) {
			collection = dao.query(getPreparedQuery());
			return collection.size();
		}
		return 0;
	}
	
	/**
	 * Returns the ID of the parent object
	 * @return ID of the parent object
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Object getParentId() {
		if (parentId == null) {
			try {
				Dao da = parent instanceof BaseDaoEnabled ? ((BaseDaoEnabled) parent).getDao() : null;
				da = da == null ? DaoManager.createDao(dao.getConnectionSource(), parent.getClass()) : da;
				parentId = da.extractId(parent);
			} catch (Exception ex) {
				// Ignored
			}
		}
		return parentId;
	}
	
	/**
	 * Returns the query for the foreign collection
	 * @throws SQLException if there was an error
	 */
	@Override
	protected PreparedQuery<T> getPreparedQuery() throws SQLException {
		if (dao == null) {
			return null;
		}
		if (preparedQuery == null) {
			SelectArg fieldArg = new SelectArg();
			fieldArg.setValue(getParentId());
			QueryBuilder<T, ID> qb = dao.queryBuilder();
			if (orderColumn != null && orderColumn.length() > 0) {
				qb.orderBy(orderColumn, true);
			}
			
			preparedQuery = qb.where().eq(getColumnName(), fieldArg).prepare();
			if (preparedQuery instanceof MappedPreparedStmt) {
				@SuppressWarnings("unchecked")
				MappedPreparedStmt<T, Object> mappedStmt = ((MappedPreparedStmt<T, Object>) preparedQuery);
				mappedStmt.setParentInformation(parent, parentId);
			}
		}
		return preparedQuery;
	}
	
	/**
	 * Update all of the items currently in the collection with the database. If 
	 * the item does not already exist in the database, the item is then created.
	 * If the item was removed from the collection, it will be removed from the
	 * database
	 * @return The number of rows updated
	 * @throws SQLException if there was an error
	 */
	@Override
	public int updateAll() throws SQLException {
		return updateAll(this.dao);
	}
	
	/**
	 * Update all of the items currently in the collection with the database. If 
	 * the item does not already exist in the database, the item is then created.
	 * If the item was removed from the collection, it will be removed from the
	 * database
	 * 
	 * @param daoObject The DAO object to update the items
	 * @return The number of rows updated
	 * @throws SQLException if there was an error
	 */
	public int updateAll(Dao<T, ID> daoObject) throws SQLException {
		// TODO: creates the data item if they do not already exist
		ArrayList<String> idList = new ArrayList<String>();
		int count = 0;
		
		if (daoObject != null) {
			Class<T> type = daoObject.getDataClass();
			ModelAttributes attributes = Schema.getAttributes(type);
			
			for (T data : collection) {
				CreateOrUpdateStatus status = daoObject.createOrUpdate(data);
				count += status.getNumLinesChanged();
				
				// Adds the ID of the object to the list
				ID id = daoObject.extractId(data);
				idList.add(id.toString());
			}
			
			DeleteBuilder<T, ID> db = daoObject.deleteBuilder();
			String parentColumnName = attributes.getColumnName(getColumnName());
			db.where().notIn(attributes.getIdField(), idList).and().eq(parentColumnName, getParentId());
			daoObject.delete(db.prepare());
		}
		return count;
	}
	
	/**
	 * Removes all of the elements from this collection (optional operation). This
	 * collection will be empty after this method returns unless it throws an
	 * exception.
	 */
	@Override
	public void clear() {
		collection.clear();
		super.clear();
	}
	
	/**
	 * This is just a call to the equals method of the internal results list.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PassiveForeignCollection)) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		PassiveForeignCollection other = (PassiveForeignCollection) obj;
		return collection.equals(other.collection);
	}

	/**
	 * @return an Iterator over the elements in this collection
	 */
	@Override
	public Iterator<T> iterator() {
		return iteratorThrow();
	}

	/**
	 * @return an iterator over a set of elements of type T which can be closed
	 */
	@Override
	public CloseableIterator<T> closeableIterator() {
		return iteratorThrow();
	}

	/**
	 * No-op since the iterators aren't holding an open connection
	 * @throws SQLException if there was an error
	 */
	@Override
	public void close() throws SQLException {
		// noop since the iterators aren't holding an open connection
	}

	/**
	 * No-op since the iterators aren't holding an open connection
	 * @throws SQLException if there was an error
	 */
	@Override
	public void closeLastIterator() throws SQLException {
		// noop since the iterators aren't holding an open connection
	}

	@Override
	public CloseableWrappedIterable<T> getWrappedIterable() {
		// since the iterators don't have any connections, the collection 
		// can be a wrapped iterable
		return this;
	}

	/**
	 * A passive collection returns an iterator to the underlying collection
	 * of data objects
	 */
	@Override
	public CloseableIterator<T> iteratorThrow() {
		// we have to wrap the iterator since we are returning the List's iterator
		return new CloseableIterator<T>() {
			private int offset = -1;
			
			@Override
			public boolean hasNext() {
				return (offset + 1 < collection.size());
			}
			
			@Override
			public T first() {
				offset = 0;
				if (offset >= collection.size()) {
					return null;
				} else {
					return collection.get(0);
				}
			}
			
			@Override
			public T next() {
				offset++;
				// this should throw if OOB
				return collection.get(offset);
			}
			
			@Override
			public T nextThrow() {
				offset++;
				if (offset >= collection.size()) {
					return null;
				} else {
					return collection.get(offset);
				}
			}
			
			@Override
			public T current() {
				if (offset < 0) {
					offset = 0;
				}
				if (offset >= collection.size()) {
					return null;
				} else {
					return collection.get(offset);
				}
			}
			
			@Override
			public T previous() {
				offset--;
				if (offset < 0 || offset >= collection.size()) {
					return null;
				} else {
					return collection.get(offset);
				}
			}
			
			@Override
			public T moveRelative(int relativeOffset) {
				offset += relativeOffset;
				if (offset < 0 || offset >= collection.size()) {
					return null;
				} else {
					return collection.get(offset);
				}
			}
			
			@Override
			public void remove() {
				if (offset < 0) {
					throw new IllegalStateException("next() must be called before remove()");
				}
				if (offset >= collection.size()) {
					throw new IllegalStateException("current results position (" + offset
							+ ") is out of bounds");
				}
				T removed = collection.remove(offset);
				if (dao != null) {
					try {
						dao.delete(removed);
					} catch (SQLException e) {
						// have to denote this to be runtime
						throw new RuntimeException(e);
					}
				}
			}
			
			@Override
			public void close() {
				// noop since the iterators aren't holding an open connection
			}
			
			@Override
			public DatabaseResults getRawResults() {
				// no results object
				return null;
			}
			
			@Override
			public void moveToNext() {
				offset++;
			}

			@Override
			public void closeQuietly() {
				// noop since the iterators aren't holding an open connection
			}
			
		};
	}
	
}
