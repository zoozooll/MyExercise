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

/**
 * An annotation that indicates this member should be digested
 * 
 * @author Stanley Lam
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Encrypt {
	
	/**
	 * The default algorithm to be used for digesting
	 */
	public static final String DEFAULT_ALGORITHM = "MD5";
	
	/**
	 * The algorithm algorithm to be used for digesting, like MD5 or SHA-1. 
	 * By default, the system uses MD5
	 */
	public String algorithm() default DEFAULT_ALGORITHM;
	
	/**
	 * Whether the plain (not hashed) salt bytes are to be appended after the
	 * digest operation result bytes. Default is to insert plain salt before
	 * digest result.
	 */
	public boolean invertPositionOfPlainSaltInEncryptionResults() default false;
	
	/**
	 * Whether the salt bytes are to be appended after the message ones before 
	 * performing the digest operation on the whole. Default is to insert before it
	 */
	public boolean invertPositionOfSaltInMessageBeforeDigesting() default false;
	
	/**
	 * The number of times the hash function will be applied recursively
	 */
	public int iterations() default 1000;
	
	/**
	 * Sets the prefix to be added at the beginning of encryption results, 
	 * and also to be expected at the beginning of plain messages provided 
	 * for matching operations
	 */
	public String prefix() default "";
	
	/**
	 * Sets the suffix to be added at the end of encryption results, and 
	 * also to be expected at the end of plain messages provided for matching 
	 * operations
	 */
	public String suffix() default "";
	
	/**
	 * Sets the size of the salt to be used to compute the digest. Default is 0,
	 * which means no salt will be used
	 */
	public int saltSizeBytes() default 0;
	
	/**
	 * Whether digest matching operations will allow matching digests with a 
	 * salt size different to the one configured in the "saltSizeBytes" property.
	 * Default is true
	 */
	public boolean useLenientSaltSizeCheck() default true;

}
