// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.zakoopilocation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

// Referenced classes of package com.zakoopilocation:
//            SearchPojo

public class SearchStoreByCharHandler extends SQLiteOpenHelper
{

    private static final String DATABASE_NAME = "SearchDB";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_CITY_NAME = "cityname";
    private static final String KEY_ID = "id";
    private static final String KEY_MARKET_NAME = "marketname";
    private static final String KEY_STORE_NAME = "storename";
    private static final String KEY_STORE_SLUG = "slug";
    private static final String TABLE_SEARCH = "SEARCHDATA";

    public SearchStoreByCharHandler(Context context)
    {
        super(context, "SearchDB", null, 1);
    }

    public void addAllDATA(SearchPojo searchpojo)
    {
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("id", searchpojo.getId());
        contentvalues.put("storename", searchpojo.getStorename());
        contentvalues.put("marketname", searchpojo.getStoreadd());
        contentvalues.put("cityname", searchpojo.getStorecity());
        contentvalues.put("slug", searchpojo.getStorslug());
        sqlitedatabase.insert("SEARCHDATA", null, contentvalues);
    }

    public ArrayList getAllDATA()
    {
        ArrayList arraylist = new ArrayList();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM SEARCHDATA", null);
        if (cursor.moveToFirst())
        {
            do
            {
                SearchPojo searchpojo = new SearchPojo();
                searchpojo.setId(cursor.getString(0));
                searchpojo.setStorename(cursor.getString(1));
                searchpojo.setStoreadd(cursor.getString(2));
                searchpojo.setStorslug(cursor.getString(3));
                searchpojo.setStorecity(cursor.getString(4));
                arraylist.add(searchpojo);
            } while (cursor.moveToNext());
        }
        return arraylist;
    }

    public void onCreate(SQLiteDatabase sqlitedatabase)
    {
        sqlitedatabase.execSQL("CREATE TABLE SEARCHDATA(id TEXT PRIMARY KEY unique ,storename TEXT,marketname TEXT, slug TEXT, cityname TEXT)");
    }

    public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
    {
        sqlitedatabase.execSQL("DROP TABLE IF EXISTS SEARCHDATA");
        onCreate(sqlitedatabase);
    }
}
