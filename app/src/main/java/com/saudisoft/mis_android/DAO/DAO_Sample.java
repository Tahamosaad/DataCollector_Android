package com.saudisoft.mis_android.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.saudisoft.mis_android.Model.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taha.mosaad on 31/07/2018.
 */

public class DAO_Sample {


    // Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private String[] mAllColumns = { DBHelper.COLUMN_SERVER_NAME, DBHelper.COLUMN_DATABASE_NAME,DBHelper.COLUMN_BRANCH_CODE,
            DBHelper.COLUMN_DATABASE_USER_NAME};


    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new Settings
    void addSettings(Settings setting) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_SERVER_NAME, setting.getServerName());
        values.put(DBHelper.COLUMN_DATABASE_NAME, setting.getDatabaseName());
        values.put(DBHelper.COLUMN_BRANCH_CODE, setting.getBranchCode());
        values.put(DBHelper.COLUMN_DATABASE_USER_NAME, setting.getDataBaseUserName());

        // Inserting Row
        db.insert(DBHelper.TABLE_SETTINGS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single Settings
    Settings getSettings(int id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DBHelper.TABLE_SETTINGS,        // The table to query
                new String[] { DBHelper.COLUMN_SERVER_NAME, DBHelper.COLUMN_DATABASE_NAME, DBHelper.COLUMN_BRANCH_CODE, DBHelper.COLUMN_DATABASE_USER_NAME},       // The array of columns to return (pass null to get all)
                DBHelper.COLUMN_SERVER_NAME + "=?",                          // The array of columns to return (pass null to get all)
                new String[] { String.valueOf(id) },                         // The columns for the WHERE clause
                null,                                                        // The values for the WHERE clause
                null,                                                        // don't group the rows
                null,                                                        // don't filter by row groups
                null);                                                       // The sort order
        if (cursor != null)
            cursor.moveToFirst();

        Settings contact = new Settings(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        // return Settings
        return contact;
    }

    // Getting All Settingss
    public List<Settings> getAllSettings() {
        List<Settings> settingList = new ArrayList<Settings>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DBHelper.TABLE_SETTINGS;

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Settings setting = new Settings();
                setting.setServerName(cursor.getString(0));
                setting.setDatabaseName(cursor.getString(1));
                setting.setBranchCode(cursor.getString(2));
                setting.setDataBaseUserName(cursor.getString(3));
                // Adding contact to list
                settingList.add(setting);
            } while (cursor.moveToNext());
        }

        // return contact list
        return settingList;
    }

    // Updating single setting
    public int updateSettings(Settings setting) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_BRANCH_CODE, setting.getBranchCode());
        values.put(DBHelper.COLUMN_DATABASE_NAME, setting.getDatabaseName());
        values.put(DBHelper.COLUMN_DATABASE_USER_NAME, setting.getDataBaseUserName());

        ;
        // updating row
        return db.update(DBHelper.TABLE_SETTINGS, values, DBHelper.COLUMN_SERVER_NAME + " = ?",
                new String[] { String.valueOf(setting.getServerName()) });
    }

    // Deleting single Settings
    public void deleteSettings(Settings setting) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_SETTINGS, DBHelper.COLUMN_SERVER_NAME + " = ?",
                new String[] { String.valueOf(setting.getServerName()) });
        db.close();
    }


    // Getting Settingss Count
    public int getSettingsCount() {
        String countQuery = "SELECT  * FROM " + DBHelper.TABLE_SETTINGS;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }





}
