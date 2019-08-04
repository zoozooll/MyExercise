/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * {@code AssetPropertyResourceBundle} loads resources from the application's raw assets.
 * All resources are Strings. The resources must be of the form {@code key=value}, one
 * resource per line (see Properties).
 *
 * @see java.util.PropertyResourceBundle
 */
public class AssetPropertyResourceBundle extends PropertyResourceBundle {
	
	private AssetPropertyResourceBundle(Reader reader) throws IOException {
		super(reader);
	}
	
	/**
	 * Returns an instance of {@code AssetPropertyResourceBundle} for the specified baseName and locale
	 * 
	 * @param ctx the context to retrieve the {@link java.util.ResourceBundle}
	 * @param baseName the base name of the resource bundle
	 * @param locale the target locale of the resource bundle
	 * @return the named resource bundle if found, {@code null} otherwise
	 */
	public static ResourceBundle getInstance(Context ctx, String baseName, Locale locale) {
		ResourceBundle bundle = handleGetInstance(ctx, false, baseName, locale);
		if (bundle == null) {
			bundle = handleGetInstance(ctx, true, baseName, locale);
		}
		return bundle;
	}
	
	private static ResourceBundle handleGetInstance(Context ctx, boolean loadBase, String baseName, Locale locale) {
		if (ctx == null) {
			throw new NullPointerException("ctx == null");
		}
		
		AssetManager am = ctx.getAssets();
		locale = locale == null ? Locale.getDefault() : locale;
		String localeName = strip(locale).toString();
		String bundleName = localeName.isEmpty() || loadBase ? baseName : (baseName + "_" + localeName);
		String fileName = bundleName.replace('.', '/') + ".properties";
		
		ResourceBundle bundle = null;
		InputStream stream = null;
		try {
			stream = am.open(fileName);
			bundle = new AssetPropertyResourceBundle(new InputStreamReader(stream, "UTF8"));
		} catch (IOException ignored) {
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException ignore) {
				}
			}
		}
		
		return bundle;
	}
	
	/**
     * Returns a locale with the most-specific field removed, or null if this
     * locale had an empty language, country and variant.
     */
	private static Locale strip(Locale locale) {
		String language = locale.getLanguage();
		String country = locale.getCountry();
		String variant = locale.getVariant();
		if (!variant.isEmpty()) {
			variant = "";
		} else if (!country.isEmpty()) {
			country = "";
		} else if (!language.isEmpty()) {
			language = "";
		} else {
			return null;
		}
		return new Locale(language, country, variant);
	}

}
