/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.permission;

import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.oregonscientific.meep.database.ModelAttributes;
import com.oregonscientific.meep.database.Schema;

/**
 * Database helper class used to manage the creation and upgrading of permission
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class PermissionDatabaseHelper extends OrmLiteSqliteOpenHelper {
	
	private final String TAG = getClass().getName();
	
	// Name of the database file
	private static final String DATABASE_NAME = "permissions.db";
	// Any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 1;
	
	public PermissionDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/**
	 * This is called when the database is first created. Usually you should call
	 * createTable statements here to create the tables that will store your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(TAG, "Creating database tables and insert data...");
			TableUtils.createTable(connectionSource, $User.class);
			TableUtils.createTable(connectionSource, Category.class);
			TableUtils.createTable(connectionSource, Component.class);
			TableUtils.createTable(connectionSource, ComponentCategory.class);
			TableUtils.createTable(connectionSource, Permission.class);
			TableUtils.createTable(connectionSource, Blacklist.class);
			TableUtils.createTable(connectionSource, Locale.class);
			TableUtils.createTable(connectionSource, History.class);
			
			String[] gameComponentNames = new String[] {
					"coolcherrytrees.games.reactor",
					"com.hg.aporkalypsefree",
					"com.sgg.archipdemo",
					"com.hotsourcegames.meep_dunkadelic_lite",
					"com.rocketmind.dinofishing",
					"com.rocketmind.nightfishing",
					"com.rocketmind.fishing",
					"com.cheezia.gearsoffur",
					"com.hg.cloudsandsheepfree",
					"com.chromaclub.doodleclub",
					"doodle.physics.lite",
					"com.hg.farminvasionnobillingfree",
					"com.hg.gunsandgloryfree",
					"com.hg.gunsandglory2nobillingfree",
					"com.hg.vikingfree",
					"com.hg.infecctfree",
					"com.primadawn.lazysnakesdemo",
					"dk.houseonfire.meep.neonzonedemo",
					"com.hg.panzerpanicfree",
					"com.meep.fallentreegames.quell",
					"com.epicpixel.rapidtossfree",
					"com.hg.dynamitefishingfree",
					"com.hg.tattootycoonfree",
					"com.hg.townsmen6free",
					"com.sgg.tfsintro",
					"com.gameloft.android.HEP.GloftUNHP",
					"com.sgg.abcfree",
					"com.sgg.wcdemo",
					"com.chromaclub.zooclub",
					"com.rovio.angrybirds.pg13",
					"com.nahmeenstudios.meowchfree",
					"com.hg.savethepuppiesfree" };
			String[] blockedComponentNames = new String[] {};
			String[] ignoredComponentNames = new String[] {
					"android.process.media",
					"android.process.acore",
					"com.adobe.flashplayer",
					"com.amlogic.HdmiSwitch",
					"com.android.backupconfirm",
					"com.android.bluetooth",
					"com.android.calendar",
					"com.android.certinstaller",
					"com.android.contacts",
					"com.android.defcontainer",
					"com.android.deskclock",
					"com.android.dreams.basic",
					"com.android.dreams.phototable",
					"com.android.htmlviewer",
					"com.android.inputdevices",
					"com.android.inputmethod.latin",
					"com.android.inputmethod.pinyin",
					"com.android.keychain",
					"com.android.launcher",
					"com.android.location.fused",
					"com.android.packageinstaller",
					"com.android.phone",
					"com.android.providers.applications",
					"com.android.providers.calendar",
					"com.android.providers.contacts",
					"com.android.providers.downloads",
					"com.android.providers.downloads.ui",
					"com.android.providers.drm",
					"com.android.providers.media",
					"com.android.providers.settings",
					"com.android.providers.telephony",
					"com.android.providers.userdictionary",
					"com.android.provision",
					"com.android.quicksearchbox",
					"com.android.sharedstoragebackup",
					"com.android.smspush",
					"com.android.systemui",
					"com.android.vending",
					"com.android.vpndialogs",
					"com.emdoor.autotest",
					"com.example.showmessage",
					"com.example.android.softkeyboard",
					"com.google.android.gms",
					"com.google.android.gsf",
					"com.google.android.gsf.login",
					"com.google.android.location",
					"com.google.android.googlequicksearchbox",
					"com.google.process.gapps",
					"com.oregonscientific.meep.store2",
					"com.oregonscientific.meep.home",
					"com.oregonscientific.meep.meepopenbox",
					"com.oregonscientific.meep.notification",
					"com.oregonscientific.meep.together",
					"com.oregonscientific.meep.safty",
					"com.oregonscientific.meep",
					"com.svox.pico",
					"jp.co.omronsoft.openwnn",
					"klye.plugin.ja",
					"kl.ime.oh",
					"system"};
			String[] hiddenComponentNames = new String[] {
					"com.android.camera", 
					"com.android.gallery3d",
					"com.android.settings" };
			
			Dao<Category, Long> categoryDao = getDao(Category.class);
			Dao<Component, Long> componentDao = getDao(Component.class);
			
			// Creates a category object
			Category categoryGames = new Category(Category.CATEGORY_GAMES);
			categoryDao.create(categoryGames);
			
			// Creates a category object
			Category categoryBlocked = new Category(Category.CATEGORY_BLACKLIST);
			categoryDao.create(categoryBlocked);
			
			Category categoryIgnored = new Category(Category.CATEGORY_IGNORED);
			categoryDao.create(categoryIgnored);
			
			Category categoryHidden = new Category(Category.CATEGORY_HIDDEN);
			categoryDao.create(categoryHidden);
			
			// Add components to categories
			addComponentsToCategory(categoryGames, Arrays.asList(gameComponentNames));
			addComponentsToCategory(categoryBlocked, Arrays.asList(blockedComponentNames));
			
			List<String> components = new ArrayList<String>(Arrays.asList(blockedComponentNames));
			components.addAll(Arrays.asList(ignoredComponentNames));
			addComponentsToCategory(categoryIgnored, components);
			
			components.addAll(Arrays.asList(hiddenComponentNames));
			addComponentsToCategory(categoryHidden, components);
			
			Set<SimpleEntry<String, String>> packages = Component.SYSTEM_COMPONENTS.keySet();
			for (SimpleEntry<String, String> pkg : packages) {
				Component component = getComponent(pkg.getValue());
				if (component == null) {
					component = new Component(pkg.getValue(), pkg.getKey());
					componentDao.create(component);
				} else {
					component.setDisplayName(pkg.getKey());
					componentDao.update(component);
				}
			}
			
			// Creates supported locales
			Dao<Locale, Long> localeDao = getDao(Locale.class);
			for (SimpleEntry<String, Boolean> entry : Locale.LOCALES) {
				Locale l = new Locale(entry.getKey(), entry.getValue());
				localeDao.create(l);
			}
		} catch (SQLException e) {
			Log.e(TAG, "Cannot create database: ", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match the new
	 * version number.
	 */
	@Override
	public void onUpgrade(
			SQLiteDatabase db,
			ConnectionSource connectionSource,
			int oldVersion,
			int newVersion) {
		
		// TODO: Alter appropriate database tables
		Log.i(TAG, "Upgrading database tables...");
	}
	
	/**
	 * Adds the application packages to the given category 
	 * 
	 * @param category the category to add the components
	 * @param components the application component to add
	 */
	private void addComponentsToCategory(Category category, List<String> components) throws SQLException {
		// Quick return
		if (category == null || components == null) {
			return;
		}
		
		Dao<Component, Long> componentDao = getDao(Component.class);
		Dao<ComponentCategory, Long> componentCategoryDao = getDao(ComponentCategory.class);
		
		for (String componentName : components) {
			// Creates a component object
			Component component = getComponent(componentName);
			if (component == null) {
				component = new Component(componentName);
				componentDao.create(component);
			}
			
			// Creates a componentCategory object
			ComponentCategory componentCategory = new ComponentCategory(component, category);
			componentCategoryDao.create(componentCategory);
		}
	}
	
	/**
	 * Retrieve the {@link Component} with the given {@code name}
	 * 
	 * @param name The name of the {@link Component}
	 * @return The {@link Component} object if found, {@code null} otherwise
	 */
	private Component getComponent(String name) {
		Component result = null;
		try {
			ModelAttributes attrs = Schema.getAttributes(Component.class);
			Dao<Component, Long> dao = getDao(Component.class);
			QueryBuilder<Component, Long> qb = dao.queryBuilder();
			qb.where().eq(attrs.getColumnName(Component.NAME_FIELD_NAME), name);
			result = qb.queryForFirst();
		} catch (SQLException ex) {
			Log.e(TAG, "Cannot retrieve " + name + " because " + ex);
		}
		return result;
	}
	
}
