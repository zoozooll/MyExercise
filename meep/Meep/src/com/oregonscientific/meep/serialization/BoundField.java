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
package com.oregonscientific.meep.serialization;

import java.io.IOException;
import java.lang.reflect.Field;

import android.annotation.TargetApi;
import android.os.Build;

import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

public abstract class BoundField {
	
	public final String name;
	public final Field field;
	public final boolean shouldSerialize;
	public final boolean shouldDeserialize;
	
	protected BoundField(
			String name,
			Field field,
			boolean shouldSerialize,
			boolean shouldDeserialize) {
		
		this.name = name;
		this.field = field;
		this.shouldSerialize = shouldSerialize;
		this.shouldDeserialize = shouldDeserialize;
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public boolean isIdField() {
		DatabaseField dbField = field.getAnnotation(DatabaseField.class);
		return dbField == null ? false : dbField.id() || dbField.generatedId() || !dbField.generatedIdSequence().isEmpty();
	}
	
	protected boolean isSerializeField() {
		SerializedName serializeField = field.getAnnotation(SerializedName.class);
		return serializeField == null ? false : true;
	}
	
	protected boolean isForeignField() {
		DatabaseField dbField = field.getAnnotation(DatabaseField.class);
		return dbField == null ? false : dbField.foreign();
	}
	
	protected boolean isForeignCollection() {
		ForeignCollectionField foreignCollectionField = field.getAnnotation(ForeignCollectionField.class);
		return foreignCollectionField != null;
	}
	
	public abstract void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException;
	
	public abstract void read(JsonReader reader, Object value, String name) throws IOException, IllegalAccessException;
	
}
