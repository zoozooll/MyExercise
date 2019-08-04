package com.butterfly.vv.db.ormhelper;

import java.sql.SQLException;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.beem.project.btf.service.Contact;
import com.btf.push.AddVCard;
import com.butterfly.vv.db.Blacklisted;
import com.butterfly.vv.db.ImMySession;
import com.butterfly.vv.db.ImOwner;
import com.butterfly.vv.db.ImSession;
import com.butterfly.vv.db.ImageDelayedTask;
import com.butterfly.vv.db.ImagesCount;
import com.butterfly.vv.db.Province;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.db.ormhelper.bean.PhoneContact;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.model.Comment;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class VVDBHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "hellonobase.db";
	private static final int DATABASE_VERSION = 1;
	private Dao<ImOwner, Integer> imOwnerDao = null;
	private Dao<Province, Integer> proviceDao = null;
	private Dao<ImMySession, Integer> imMySessionDao = null;
	private Dao<ImSession, Integer> imSessionDao = null;
	private Dao<VVImage, String> vVImageDao = null;
	private Dao<ImageFolder, String> imageFolderDao = null;
	private Dao<ImageDelayedTask, String> imageDelayedTaskDao = null;
	private Dao<Comment, String> commentDao = null;
	private Dao<Contact, String> contactDao = null;
	// 时光图片计数DAO
	private Dao<ImagesCount, String> imagesCountDao = null;

	public VVDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase,
			ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, ImOwner.class);
			TableUtils.createTable(connectionSource, Province.class);
			TableUtils.createTable(connectionSource, ImMySession.class);
			TableUtils.createTable(connectionSource, ImSession.class);
			TableUtils.createTable(connectionSource, VVImage.class);
			TableUtils.createTable(connectionSource, ImageFolder.class);
			TableUtils.createTable(connectionSource, ImageDelayedTask.class);
			TableUtils.createTable(connectionSource, Comment.class);
			TableUtils.createTable(connectionSource, Contact.class);
			TableUtils.createTable(connectionSource, AddVCard.class);
			TableUtils.createTable(connectionSource, Blacklisted.class);
			TableUtils.createTable(connectionSource, PhoneContact.class);
			TableUtils.createTable(connectionSource, ImagesCount.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onUpgrade(SQLiteDatabase arg0,
			ConnectionSource connectionSource, int arg2, int arg3) {
		try {
			TableUtils.dropTable(connectionSource, ImOwner.class, true);
			TableUtils.dropTable(connectionSource, Province.class, true);
			TableUtils.dropTable(connectionSource, ImMySession.class, true);
			TableUtils.dropTable(connectionSource, ImSession.class, true);
			TableUtils.dropTable(connectionSource, VVImage.class, true);
			TableUtils.dropTable(connectionSource, ImageFolder.class, true);
			TableUtils
					.dropTable(connectionSource, ImageDelayedTask.class, true);
			TableUtils.dropTable(connectionSource, Comment.class, true);
			TableUtils.dropTable(connectionSource, Contact.class, true);
			TableUtils.dropTable(connectionSource, AddVCard.class, true);
			TableUtils.dropTable(connectionSource, Blacklisted.class, true);
			TableUtils.dropTable(connectionSource, PhoneContact.class, true);
			onCreate(arg0, connectionSource);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Dao<ImOwner, Integer> getImOwnerDao() throws SQLException {
		if (imOwnerDao == null) {
			imOwnerDao = getDao(ImOwner.class);
		}
		return imOwnerDao;
	}
	public Dao<Province, Integer> getProviceDao() throws SQLException {
		if (proviceDao == null) {
			proviceDao = getDao(Province.class);
		}
		return proviceDao;
	}
	public Dao<ImMySession, Integer> getImMySessionDao() throws SQLException {
		if (imMySessionDao == null) {
			imMySessionDao = getDao(ImMySession.class);
		}
		return imMySessionDao;
	}
	public Dao<ImSession, Integer> getImSessionDao() throws SQLException {
		if (imSessionDao == null) {
			imSessionDao = getDao(ImSession.class);
		}
		return imSessionDao;
	}
	public Dao<VVImage, String> getVVImageDao() throws SQLException {
		if (vVImageDao == null) {
			vVImageDao = getDao(VVImage.class);
		}
		return vVImageDao;
	}
	public Dao<ImageFolder, String> getImageFolderDao() throws SQLException {
		if (imageFolderDao == null) {
			imageFolderDao = getDao(ImageFolder.class);
		}
		return imageFolderDao;
	}
	public Dao<ImageDelayedTask, String> getImageDelayedTaskDao()
			throws SQLException {
		if (imageDelayedTaskDao == null) {
			imageDelayedTaskDao = getDao(ImageDelayedTask.class);
		}
		return imageDelayedTaskDao;
	}
	public Dao<Comment, String> getCommentDao() throws SQLException {
		if (commentDao == null) {
			commentDao = getDao(Comment.class);
		}
		return commentDao;
	}
	public Dao<Contact, String> getContactDao() throws SQLException {
		if (contactDao == null) {
			contactDao = getDao(Contact.class);
		}
		return contactDao;
	}
	@Override
	public void close() {
		// TODO Auto-generated method stub
		super.close();
		// simpledataDao = null;
		// imageDao = null;
		imOwnerDao = null;
		proviceDao = null;
		imMySessionDao = null;
		imSessionDao = null;
		vVImageDao = null;
		imageFolderDao = null;
		imageDelayedTaskDao = null;
		commentDao = null;
		contactDao = null;
	}
	public Dao<ImagesCount, String> getImagesCountDao() throws SQLException {
		if (imagesCountDao == null) {
			imagesCountDao = getDao(ImagesCount.class);
		}
		return imagesCountDao;
	}
}
