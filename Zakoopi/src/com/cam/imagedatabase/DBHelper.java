package com.cam.imagedatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DBNAME = "LOOKBD";
	public static final String DBTABLE = "lookbook";
	public static final String DBTABLE1 = "Stores";
	public static final int version = 1;

	public DBHelper(Context context) {
		super(context, DBNAME, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL("create table  "
				+ DBTABLE
				+ " (id integer primary key autoincrement,photo blob,tag text,desc text,imagepath text)");
		
		db.execSQL("create table  "
				+ DBTABLE1
				+ " (id text primary key ,market text)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
