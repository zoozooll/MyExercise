package com.mogoo.market.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.mogoo.market.utils.LogUtils;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * 
 * @author 张永辉
 * @date 2011-10-18
 */
public class Configuration {

	private static final String CONFIG_PATH = "config.prop";

	/**
	 * 内存缓存图片数量
	 */
	public static final String MEMORY_CACHE_SIZE = "memory.cache.size";

	/**
	 * 下载图片的地址集合最大数量
	 */
	public static final String MAX_URL_NUMBER = "max.url.number";

	/**
	 * 拉取应用列表的页大小
	 */
	public static final String KEY_APP_LIST_PAGE_SIZE = "app.list.page.size";

	private static Configuration instance;

	private Properties configProp;

	private Configuration(Context context) {
		loadProp(context);
	}

	/**
	 * 取得配置实例
	 * 
	 * @author 张永辉
	 * @date 2011-10-18
	 * @return
	 */
	public static Configuration getInstance(Context context) {
		if (instance == null) {
			instance = new Configuration(context);
		}
		return instance;
	}

	/**
	 * 取得参数值
	 * 
	 * @author 张永辉
	 * @date 2011-10-18
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getProperty(String key, String defaultValue) {
		return configProp.getProperty(key, defaultValue);
	}

	/**
	 * 取得参数值
	 * 
	 * @author 张永辉
	 * @date 2011-10-18
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public int getPropertyInt(String key, int defaultValue) {
		String str = configProp.getProperty(key);

		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}

		return defaultValue;
	}

	/**
	 * 清空
	 * 
	 * @author 张永辉
	 * @date 2011-10-21
	 */
	public void clear() {
		configProp.clear();
	}

	/**
	 * 加载配置文件
	 * 
	 * @author 张永辉
	 * @date 2011-10-18
	 * @param context
	 */
	private void loadProp(Context context) {
		InputStream is = null;
		try {
			configProp = new Properties();
			AssetManager am = context.getAssets();
			is = am.open(CONFIG_PATH);
			configProp.load(is);
		} catch (IOException e) {
			LogUtils.error(Configuration.class, null, e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				LogUtils.error(Configuration.class, null, e);
			}
		}
	}
}
