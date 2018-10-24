package com.saudisoft.mis_android.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saudisoft.mis_android.Model.ItemsDirectory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taha.mosaad on 30/07/2018.
 */

public class ItemsDirectory_DAO {
    public static final String TAG = "ItemsDirectory_DAO";

    private Context mContext;
    // Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private String[] mAllColumns = { DBHelper.COL_ITEMCODE, DBHelper.COL_ITEMNAME,DBHelper.COL_PARTNUMBER};

    public ItemsDirectory_DAO(Context context) {
        mDbHelper = new DBHelper(context);
        this.mContext = context;
        // open the database
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new ItemsDirectory
    public void addItemsDirectory(ItemsDirectory itemsdir) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_ITEMCODE, itemsdir.getItemCode());
        values.put(DBHelper.COL_ITEMNAME, itemsdir.getItemName());
        values.put(DBHelper.COL_PARTNUMBER, itemsdir.getPartNumber());

        // Inserting Row
        db.insert(DBHelper.TABLE_ITEM_DIRECTORY, null, values);
        db.close(); // Closing database connection
    }
    public ItemsDirectory getItemByItemcode(String code) {
        Cursor cursor = mDatabase.query(DBHelper.TABLE_ITEM_DIRECTORY, mAllColumns,
                DBHelper.COL_ITEMCODE + " = ?",
                new String[] { String.valueOf(code) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        ItemsDirectory itemDir = cursorToItemsDirectory (cursor);
        return itemDir;
    }
    // Getting single ItemsDirectory
    ItemsDirectory getItemsDirectory(int id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DBHelper.TABLE_ITEM_DIRECTORY,        // The table to query
                new String[] { DBHelper.COL_ITEMCODE, DBHelper.COL_ITEMNAME, DBHelper.COL_PARTNUMBER},       // The array of columns to return (pass null to get all)
                DBHelper.COL_ITEMCODE+ "=?",                          // The array of columns to return (pass null to get all)
                new String[] { String.valueOf(id) },                         // The columns for the WHERE clause
                null,                                                        // The values for the WHERE clause
                null,                                                        // don't group the rows
                null,                                                        // don't filter by row groups
                null);                                                       // The sort order
        if (cursor != null)
            cursor.moveToFirst();

        ItemsDirectory contact = new ItemsDirectory(cursor.getString(0), cursor.getString(1), cursor.getString(2));
        // return ItemsDirectory
        return contact;
    }

    // Getting All ItemsDirectorys
    public List<ItemsDirectory> getAllItemsDirectory() {
        List<ItemsDirectory> itemsdirList = new ArrayList<ItemsDirectory>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DBHelper.TABLE_ITEM_DIRECTORY;

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ItemsDirectory itemsdir = new ItemsDirectory();
                itemsdir.setItemCode(cursor.getString(0));
                itemsdir.setItemName(cursor.getString(1));
                itemsdir.setPartNumber(cursor.getString(2));

                // Adding contact to list
                itemsdirList.add(itemsdir);
            } while (cursor.moveToNext());
        }

        // return contact list
        return itemsdirList;
    }

    // Updating single itemsdir
    public int updateItemsDirectory(ItemsDirectory itemsdir) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_ITEMCODE, itemsdir.getItemCode());
        values.put(DBHelper.COL_ITEMNAME, itemsdir.getItemName());
        values.put(DBHelper.COL_PARTNUMBER, itemsdir.getPartNumber());
        ;
        // updating row
        return db.update(DBHelper.TABLE_ITEM_DIRECTORY, values, DBHelper.COL_ITEMCODE + " = ?",
                new String[] { String.valueOf(itemsdir.getItemCode()) });
    }

    // Deleting single ItemsDirectory
    public void deleteItemsDirectory(ItemsDirectory itemsdir) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_ITEM_DIRECTORY, DBHelper.COL_ITEMCODE + " = ?",
                new String[] { String.valueOf(itemsdir.getItemCode()) });
        db.close();
    }


    // Getting ItemsDirectorys Count
    public int getItemsDirectoryCount() {
        String countQuery = "SELECT  * FROM " + DBHelper.TABLE_ITEM_DIRECTORY;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    protected ItemsDirectory cursorToItemsDirectory(Cursor cursor) {
        ItemsDirectory ItemDir = new ItemsDirectory();
        ItemDir.setItemCode(cursor.getString(0));
        ItemDir.setItemName(cursor.getString(1));
        ItemDir.setPartNumber(cursor.getString(2));

        return ItemDir;
    }


}
