package com.oregonscientific.meep.together.library.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.oregonscientific.meep.together.bean.ResponseLogin;
import com.oregonscientific.meep.together.library.database.table.AuthInfo;
import com.oregonscientific.meep.together.library.database.table.TableUser;

public class DatabaseHelper {

    private static SQLiteDatabase db;
    
    public DatabaseHelper(Context context)
    {
	    db = context.openOrCreateDatabase("meep.db", Context.MODE_PRIVATE, null); 
    }
    public void createAll()
    {
    	db.execSQL(TableUser.getCreateSql());
    	db.execSQL(AuthInfo.getCreateSql());
    }
    public void deleteAll()
    {
    	db.execSQL(TableUser.getDeleteSql());
    	db.execSQL(AuthInfo.getDeleteSql());
    }
    public void insertUser(String sql)
    {
    	db.execSQL(sql);
    }
    public void deleteUser()
    {
    	db.execSQL(TableUser.getDeleteSql());
    }
    public void dropAll()
    {
    	db.execSQL(TableUser.getDropTableSql());
    	db.execSQL(AuthInfo.getDropTableSql());
    }
    public AuthInfo queryIsLogin()
    {
    	String sql = AuthInfo.getSelectSql();
    	Cursor c = db.rawQuery(sql, null);
    	if (c.getCount() != 0)
    	{
    		c.moveToLast();
    		AuthInfo auth = new AuthInfo();
    		auth.setEmail(c.getString(1));
    		auth.setPassword(c.getString(2));
    		c.close();
    		return auth;
    	}
    	c.close();
    	return null;
    }
    
    public TableUser getUserInfoFromDB()
    {
    	String sql = TableUser.getSelectSql();
    	Cursor c = db.rawQuery(sql, null);
    	if (c.getCount() != 0)
    	{
    		c.moveToLast();
    		TableUser user = new TableUser();
    		user.setFirstName(c.getString(1));
    		user.setLastName(c.getString(2));
    		user.setToken(c.getString(3));
    		user.setIconAddr(c.getString(4));
    		user.setCoins(c.getString(5));
    		user.setmLastKid(c.getString(6));
    		c.close();
    		return user;
    	}
    	c.close();
    	return null;
    }
    
    
    //Update Table User
    private String userUpdateString = "UPDATE " + TableUser.S_TABLE_NAME +  " SET " ;
    public void updateLastKid(String lastKid)
    {
    	String sql = userUpdateString + TableUser.S_LAST_KID + " = '" + lastKid + "'";
    	db.execSQL(sql);
    }
    public void updateALL(ResponseLogin lr)
    {
    	String sql = userUpdateString 
    			+ TableUser.S_FIRST_NAME + " = '" + lr.getFirstName() + "' , " 
    			+ TableUser.S_LAST_NAME + " = '" + lr.getLastName() + "' , " 
    			+ TableUser.S_ICON_ADDR + " = '" + lr.getAvatar() + "' , " 
    			+ TableUser.S_COINS + " = '" + lr.getCoins() + "'";
    	db.execSQL(sql);
    }
    public static void close()
    {
    	if(db!=null)
    		db.close();
    }
    
    
}
