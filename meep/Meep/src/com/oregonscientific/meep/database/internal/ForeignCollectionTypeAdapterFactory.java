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

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.oregonscientific.meep.database.Model;
import com.oregonscientific.meep.database.PassiveForeignCollection;
import com.oregonscientific.meep.serialization.TypeAdapterRuntimeTypeWrapper;

/**
 * Type adapter that reflects over the fields and methods of a 'ForeignCollection' class.
 * @author Stanley Lam
 */
public class ForeignCollectionTypeAdapterFactory implements TypeAdapterFactory {
	
	private final ForeignCollectionInstanceCreator creator;
	
	public ForeignCollectionTypeAdapterFactory() {
		this.creator = null;
	}
	
	public ForeignCollectionTypeAdapterFactory(ForeignCollectionInstanceCreator creator) {
		this.creator = creator;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gson.TypeAdapterFactory#create(com.google.gson.Gson, com.google.gson.reflect.TypeToken)
	 */
	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
		Type type = typeToken.getType();
		
		Class<? super T> rawType = typeToken.getRawType();
		if (!ForeignCollection.class.isAssignableFrom(rawType)) {
			return null;
		}
		
		final Type elementType = $Gson$Types.getCollectionElementType(type, rawType);
		TypeToken<?> elementTypeToken = TypeToken.get(elementType);
		
		final Class<?> elementRawType = elementTypeToken.getRawType();
		TypeAdapter<?> elementTypeAdapter = Model.class.isAssignableFrom(elementRawType) ? gson.getAdapter(elementTypeToken) : null;
    
		@SuppressWarnings({ "unchecked", "rawtypes" })
		TypeAdapter<T> result = new Adapter(gson, elementType, elementTypeAdapter) {
    	
    	@Override
    	public ObjectConstructor<T> getConstructor(final ForeignCollectionDeserializationContext context) {
				return new ObjectConstructor<T>() {
					public T construct() {
						if (creator == null) {
							return (T) new PassiveForeignCollection(
									null, 
									context.getParent(), 
									null, 
									null, 
									context.getOrderColumnName(), 
									context.getOrderAscending());
						} else {
							return (T) creator.createInstance(
									elementType,
									context.getParent(),
									context.getColumnName(),
									context.getOrderColumnName(),
									context.getOrderAscending());
						}
					}
				};
			}

		};
		return result;
	}
	
	public abstract class Adapter<E> extends ForeignCollectionTypeAdapter<ForeignCollection<E>> {
		
		private final TypeAdapterRuntimeTypeWrapper<E> elementTypeAdapter;
		private ModelSerializationStrategy serializationStrategy;
		private ForeignCollectionDeserializationContext context;
    
		public Adapter(Gson context, Type elementType, TypeAdapter<E> elementTypeAdapter) {

			this.elementTypeAdapter = new TypeAdapterRuntimeTypeWrapper<E>(context, elementTypeAdapter, elementType);
			this.serializationStrategy = ModelSerializationPolicy.DEFAULT;
		}
    
		public abstract ObjectConstructor<? extends ForeignCollection<E>> getConstructor(ForeignCollectionDeserializationContext context);
    
		@Override
		public ModelSerializationStrategy setSerializationStrategy(
				ModelSerializationStrategy serializationStrategy) {

			ModelSerializationStrategy returnValue = this.serializationStrategy;
			this.serializationStrategy = serializationStrategy == null 
				? ModelSerializationPolicy.DEFAULT
				: serializationStrategy;
			return returnValue;
		}

		@Override
		public void setDeserializationContext(ForeignCollectionDeserializationContext context) {
			this.context = context;
		}

		public ForeignCollection<E> read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}

			ForeignCollection<E> collection = getConstructor(context).construct();
			if (collection != null) {
				in.beginArray();
				while (in.hasNext()) {
					E instance = elementTypeAdapter.read(in);
					collection.add(instance);
				}
				in.endArray();
			}
			return collection;
		}

		@SuppressWarnings("unchecked")
		public void write(JsonWriter out, ForeignCollection<E> collection)
				throws IOException {
			if (collection == null) {
				out.nullValue();
				return;
			}

			out.beginArray();
			CloseableIterator<E> iterator = collection.closeableIterator();
			try {
				while (iterator.hasNext()) {
					E element = iterator.next();
					if (elementTypeAdapter.getRuntimeTypeAdapter(element) instanceof DaoTypeAdapter) {
						DaoTypeAdapter<E> adapter = (DaoTypeAdapter<E>) elementTypeAdapter.getRuntimeTypeAdapter(element);

						// Field serialization policy takes precedence of the
						// serialization
						// policy specified for the type adapter factory
						ModelSerializationStrategy strategy = adapter.setSerializationStrategy(serializationStrategy);
						adapter.write(out, element);
						adapter.setSerializationStrategy(strategy);
					} else {
						elementTypeAdapter.write(out, element);
					}
				}
			} finally {
				try {
					iterator.close();
				} catch (SQLException ex) {
					// Ignored
				}
			}
			out.endArray();
		}
	}
}
