package com.saudisoft.mis_android.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saudisoft.mis_android.Model.ItemSerials;
import com.saudisoft.mis_android.Model.ItemsInOutL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${Taha.mosaad} on ${6/10/2018}.
 */

public class ItemsSerials_DAO {
    public static final String TAG = "ItemsSerials_DAO";

    // Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private Context mContext;
//    private String[] mAllColumns = {DBHelper.COLUMN_SERIAL,DBHelper.COL_SERIALNUM, DBHelper.COL_ID};
    private Map<String,String> datanum;

    public ItemsSerials_DAO(Context context) {
        this.mContext = context;
        mDbHelper = new DBHelper(context);
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
    public void addItemSerials(ItemSerials serials) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ItemsInOutL_DAO dao = new ItemsInOutL_DAO(mContext);
        ItemsInOutL Dtl = dao.getItemByItemID(serials.getmItemID());

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_SERIAL, serials.getSerialNum());
        values.put(DBHelper.COL_SERIALNUM,serials.getSerial() );
        values.put(DBHelper.COL_ID, Dtl.getID());
        // Inserting Row
        db.insert(DBHelper.TABLE_ITEM_SERIAL, null, values);
        db.close(); // Closing database connection
    }
    public void deleteItemSerials(ItemSerials item) {
        String id = item.getSerial();
        System.out.println("the deleted item has the id: " + id);
        mDatabase.delete(DBHelper.TABLE_ITEM_SERIAL, DBHelper.COLUMN_ITEM_SERIAL
                + " = " + id, null);
    }
    public List<ItemSerials> getAllItemSerials() {
        List<ItemSerials> contactList = new ArrayList<>();
//
        // Select All Query
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_ITEM_SERIAL;

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                ItemSerials item = new ItemSerials();
                item.setSerial(cursor.getString(0));
//                item.setTransType(cursor.getInt(1));
                item.setSerialNum(cursor.getString(2));


                String ItemID = cursor.getString(1);
                ItemsInOutL_DAO dao = new ItemsInOutL_DAO(mContext);
                ItemsInOutL itemsInOutL = dao.getItemByItemID(ItemID);
                if (itemsInOutL != null)
                    item.setmID(itemsInOutL);
                // Adding contact to list
                contactList.add(item);

            } while (cursor.moveToNext());
            // make sure to close the cursor
            cursor.close();
        }
        // return contact list
        return contactList;
    }
    public List<Map<String,String>> GetAllItemSerials() {
        datanum=new HashMap<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_ITEM_SERIAL;

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<Map<String, String>> data =new ArrayList<>();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                // Adding contact to list
//                contactList.add(item);

                datanum.put("Serialnum",cursor.getString(0));
//                datanum.put("Serial",cursor.getString(2));
//                datanum.put("Serial_ID",cursor.getString(1));
                data.add(datanum);
            } while (cursor.moveToNext());
            // make sure to close the cursor
            cursor.close();
        }

        // return contact list
        return data;
    }


//    public  List<Map<String,String>> getDetailSerials(String serial) {
//        datanum = new HashMap<>();
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//        List<Map<String, String>> data = new ArrayList<>();
//
//        Cursor cursor = db.query(
//                DBHelper.TABLE_ITEM_SERIAL,        // The table to query
//                mAllColumns,       // The array of columns to return (pass null to get all)
//                DBHelper.COL_SERIALNUM + "=?",                          // The array of columns to return (pass null to get all)
//                new String[] { (serial) },                         // The columns for the WHERE clause
//                null,                                                        // The values for the WHERE clause
//                null,                                                        // don't group the rows
//                null,                                                        // don't filter by row groups
//                null);                                                       // The sort order
//        if (cursor != null)
//            cursor.moveToFirst();
//        datanum.put("Serial",cursor.getString(0));
//        datanum.put("Serialnum",cursor.getString(1));
//
//        data.add(datanum);
//        cursor.close();
//        return data;
//    }
    public  List<ItemSerials> getSavedSerials(String serial) {
        datanum = new HashMap<>();
        List<ItemSerials> SavedSerialList = new ArrayList<>();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Select All Query
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_ITEM_SERIAL +" WHERE VoucherSerial = "+ "'"+serial+"'";


        Cursor cursor = db.rawQuery(selectQuery, null);                                                    // The sort order
        if (cursor != null)
            if (cursor.moveToFirst()) {
                do {

                    ItemSerials item = new ItemSerials();
                    item.setSerial(cursor.getString(0));
                    String ItemID = cursor.getString(1);
                    item.setSerialNum(cursor.getString(2));

                    ItemsInOutL_DAO dao = new ItemsInOutL_DAO(mContext);
                    ItemsInOutL itemsInOutL = dao.getItemByItemID(ItemID);
                    if (itemsInOutL != null)
                        item.setmID(itemsInOutL);
                    // Adding contact to list
                    SavedSerialList.add(item);


                }while (cursor.moveToNext());
        cursor.close();
        }
        return SavedSerialList;
    }

    private ItemSerials cursorToItemserial(Cursor cursor) {
        ItemSerials item = new ItemSerials();
        item.setSerial(cursor.getString(0));
        item.setSerialNum(cursor.getString(1));


        // get The Item ID type  by Itemid
        String ItemID = cursor.getString(2);
        ItemsInOutL_DAO dao = new ItemsInOutL_DAO(mContext);
        ItemsInOutL Item = dao.getItemByItemID(ItemID);
        if (Item != null)
            item.setmID(Item);

        return item;
    }
    // todo DELETE all table
    public boolean deleteTable() {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        int affectedRows = database.delete(DBHelper.TABLE_ITEM_SERIAL, null, null);
        return affectedRows > 0;
    }
}
