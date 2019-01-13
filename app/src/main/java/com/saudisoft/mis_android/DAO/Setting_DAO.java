package com.saudisoft.mis_android.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saudisoft.mis_android.Model.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taha.mosaad on 30/07/2018.
 */

public class Setting_DAO {
    public static final String TAG = "Setting_DAO";

    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private String[] mAllColumns = {DBHelper.COLUMN_SERVER_NAME,
            DBHelper.COLUMN_DATABASE_NAME,
            DBHelper.COLUMN_BRANCH_CODE,
            DBHelper.COLUMN_DATABASE_USER_NAME,

    };

    public Setting_DAO(Context context) {
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
    private void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }
    public void close() {
        mDbHelper.close();
    }
    // Adding new settings
   public long addSetting(Settings setting) {
//       deleteSettings(setting);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_SERVER_NAME, setting.getServerName());
        values.put(DBHelper.COLUMN_DATABASE_NAME, setting.getDatabaseName());
        values.put(DBHelper.COLUMN_BRANCH_CODE, setting.getBranchCode());
        values.put(DBHelper.COLUMN_DATABASE_USER_NAME, setting.getDataBaseUserName());

        // Inserting Row
       long ER= db.insert(DBHelper.TABLE_SETTINGS, null, values);
        db.close(); // Closing database connection
       return ER;
    }
    public int updateSettings(Settings setting) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_BRANCH_CODE, setting.getBranchCode());
        values.put(DBHelper.COLUMN_DATABASE_NAME, setting.getDatabaseName());
        values.put(DBHelper.COLUMN_DATABASE_USER_NAME, setting.getDatabaseName());

        // updating row
        return db.update(DBHelper.TABLE_SETTINGS, values, DBHelper.COLUMN_SERVER_NAME + " = ?",
                new String[] { String.valueOf(setting.getServerName()) });
    }
    public List<Settings> getAllSettings1() {
        List<Settings> contactList = new ArrayList<>();
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
                contactList.add(setting);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contactList;
    }
    public List<Settings> getAllSettings() {
        List<Settings> listSettings = new ArrayList<>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_SETTINGS, mAllColumns,null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Settings setting = cursorToSettings(cursor);
            listSettings.add(setting);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return listSettings;
    }
    public Settings createSettings(String servername, String databasename,String branchcode, String databaseusername,String databasepassword) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_SERVER_NAME, servername);
        values.put(DBHelper.COLUMN_DATABASE_NAME, databasename);
        values.put(DBHelper.COLUMN_BRANCH_CODE, branchcode);
        values.put(DBHelper.COLUMN_DATABASE_USER_NAME, databaseusername);



        Cursor cursor = mDatabase.query(DBHelper.TABLE_SETTINGS, mAllColumns,null, null, null, null, null);
        cursor.moveToFirst();
        Settings newsettings = cursorToSettings(cursor);
        cursor.close();
        return newsettings;
    }
    public void deleteSettings(Settings setting) {
        String servername = setting.getServerName();
//        System.out.println("the deleted settings has the servername: " + servername);
        mDatabase.delete(DBHelper.TABLE_SETTINGS, DBHelper.COLUMN_SERVER_NAME
                + " = " + servername, null);
    }
    // Updating single setting
    public List<Settings> getSettingsOfApplication(String servername) {
        List<Settings> listSetting = new ArrayList<>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_SETTINGS, mAllColumns,
                DBHelper.COLUMN_SERVER_NAME + " = ?",
                new String[] { servername }, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Settings setting = cursorToSettings(cursor);
            listSetting.add(setting);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return listSetting;
    }
    private Settings cursorToSettings(Cursor cursor) {

        Settings setting = new Settings();
        setting.setServerName(cursor.getString(0));
        setting.setDatabaseName(cursor.getString(1));
        setting.setBranchCode(cursor.getString(2));
        setting.setDataBaseUserName(cursor.getString(3));

        return  setting;
    }
}
