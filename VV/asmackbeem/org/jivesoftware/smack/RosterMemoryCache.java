package org.jivesoftware.smack;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import com.nostra13.universalimageloader.utils.L;

public abstract class RosterMemoryCache<K, V> {
	// hardCache和softMap需要加锁
	private Map<K, V> hardCache = Collections
			.synchronizedMap(new LinkedHashMap<K, V>(10, 0.75f, true));
	private final Map<K, Reference<V>> softMap = Collections
			.synchronizedMap(new HashMap<K, Reference<V>>());
	private final int sizeLimit;
	private final AtomicInteger cacheSize;
	private static final int MAX_NORMAL_CACHE_SIZE_IN_MB = 2;
	private static final int MAX_NORMAL_CACHE_SIZE = MAX_NORMAL_CACHE_SIZE_IN_MB * 1024 * 1024;

	public RosterMemoryCache(int sizeLimit) {
		this.sizeLimit = sizeLimit;
		cacheSize = new AtomicInteger();
		if (sizeLimit > MAX_NORMAL_CACHE_SIZE) {
			// %1$d,表示第一个参数按整形格式化，即MAX_NORMAL_CACHE_SIZE_IN_MB这个值
			L.w("You set too large memory cache size (more than %1$d Mb)",
					MAX_NORMAL_CACHE_SIZE_IN_MB);
		}
	}
	protected Reference<V> createReference(V value) {
		return new WeakReference<V>(value);
	}
	public boolean put(K key, V value) {
		boolean putSuccessfully = false;
		// Try to add value to hard cache
		int valueSize = getSize(value);
		int sizeLimit = getSizeLimit();
		int curCacheSize = cacheSize.get();
		if (valueSize < sizeLimit) {
			while (curCacheSize + valueSize > sizeLimit) {
				V removedValue = removeNext();
				curCacheSize = cacheSize.addAndGet(-getSize(removedValue));
			}
			hardCache.put(key, value);
			cacheSize.addAndGet(valueSize);
			putSuccessfully = true;
		}
		// Add value to soft cache
		softMap.put(key, createReference(value));
		return putSuccessfully;
	}
	public void remove(K key) {
		softMap.remove(key);
	}
	public V get(K key) {
		V result = null;
		Reference<V> reference = softMap.get(key);
		if (reference != null) {
			result = reference.get();
		}
		return result;
	}
	protected V removeNext() {
		V mostLongUsedValue = null;
		Iterator<Entry<K, V>> it = hardCache.entrySet().iterator();
		if (it.hasNext()) {
			Entry<K, V> entry = it.next();
			mostLongUsedValue = entry.getValue();
			it.remove();
		}
		return mostLongUsedValue;
	}
	private int getSizeLimit() {
		return sizeLimit;
	}
	protected abstract int getSize(V value);
}
