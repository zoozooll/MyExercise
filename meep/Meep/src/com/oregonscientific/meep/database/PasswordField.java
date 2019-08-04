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

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;

/**
 * An annotation that identifies a password field in a class that corresponds to 
 * a column in the database
 * 
 * @author Stanley Lam
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface PasswordField {
	
	/**
	 * Allows user to set a custom password encryptor class. This class contain a constructor that
	 * takes no argument 
	 */
	Class<? extends PasswordEncryptor> encryptorClass() default BasicPasswordEncryptor.class;

}
