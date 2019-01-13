package com.saudisoft.mis_android.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saudisoft.mis_android.Model.InvTransTypes;
import com.saudisoft.mis_android.Model.ItemsInOutH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by taha.mosaad on 30/07/2018.
 */

public class ItemInOutH_DAO {
    public static final String TAG = "ItemInOutH_DAO";
    private Map<String,String> datanum;
    // Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private Context mContext;
    private String[] mAllColumns = { DBHelper.COLUMN_ITEM_SERIAL,
            DBHelper.COLUMN_TRANS_NUMBER, DBHelper.COLUMN_TRANS_TYPE,
            DBHelper.COLUMN_TRANS_DATE,
            DBHelper.COLUMN_TRANS_NOTE,
            DBHelper.COLUMN_ITEM_TRANS_CODE,
            DBHelper.COLUMN_IS_NEW};

    public ItemInOutH_DAO(Context context) {
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

    public long addItemHeader(ItemsInOutH item) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        InvTransTypes_DAO dao = new InvTransTypes_DAO(mContext);
        InvTransTypes Trans = dao.getTransBycode(item.getTransCode());

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ITEM_SERIAL, item.getSerial());
        values.put(DBHelper.COLUMN_TRANS_NUMBER, item.getTransNum());
        values.put(DBHelper.COLUMN_TRANS_TYPE, Trans.getTransType());
        values.put(DBHelper.COLUMN_TRANS_DATE, item.getTransDate());
        values.put(DBHelper.COLUMN_TRANS_NOTE, item.getNotes());
        values.put(DBHelper.COLUMN_ITEM_TRANS_CODE, item.getTransCode());
        values.put(DBHelper.COLUMN_IS_NEW, item.getIsNew());

        // Inserting Row
       long ER = db.insert(DBHelper.TABLE_ITEM_HEADER, null, values);
        db.close(); // Closing database connection
        return ER;
    }
    //Overload
    public ItemsInOutH createItemHeader(String itemSerial, int transType, String transNumber, String transDate, String transNote, int isNew,
                                        String  transCode) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ITEM_SERIAL, itemSerial);
        values.put(DBHelper.COLUMN_TRANS_TYPE, transType);
        values.put(DBHelper.COLUMN_TRANS_NUMBER, transNumber);
        values.put(DBHelper.COLUMN_TRANS_DATE, transDate);
        values.put(DBHelper.COLUMN_TRANS_NOTE, transNote);
        values.put(DBHelper.COLUMN_IS_NEW, isNew);
        values.put(DBHelper.COLUMN_ITEM_TRANS_CODE, transCode);
        long insertId = mDatabase
                .insert(DBHelper.TABLE_ITEM_HEADER, null, values);
        Cursor cursor = mDatabase.query(DBHelper.TABLE_ITEM_HEADER, mAllColumns,
                DBHelper.COLUMN_ITEM_SERIAL + " = " + insertId, null, null,
                null, null);
        cursor.moveToFirst();
        ItemsInOutH newItemHdr = cursorToItemHdr(cursor);
        cursor.close();
        return newItemHdr;
    }

    public int deleteItemsInOutH(ItemsInOutH item) {
        if (!mDatabase.isOpen())
            open();
        int isdeleted=   mDatabase.delete(DBHelper.TABLE_ITEM_HEADER,DBHelper.COLUMN_ITEM_SERIAL+" = ? ",new String[]{item.getSerial()});
        mDatabase.close();
        return isdeleted;
    }
    public  List<Map<String,String>> getItemHeader(String serial) {
        datanum = new HashMap<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        List<Map<String, String>> data = new ArrayList<>();
        Cursor cursor = db.query(
                DBHelper.TABLE_ITEM_HEADER,        // The table to query
                mAllColumns,       // The array of columns to return (pass null to get all)
                DBHelper.COL_SERIAL + "=?",                          // The array of columns to return (pass null to get all)
                new String[] { (serial) },                         // The columns for the WHERE clause
                null,                                                        // The values for the WHERE clause
                null,                                                        // don't group the rows
                null,                                                        // don't filter by row groups
                null);                                                       // The sort order
        if (cursor != null)
            cursor.moveToFirst();

//        ItemsInOutL itemdtl = new ItemsInOutL(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getFloat(3),cursor.getString(4),cursor.getString(5));
        // return ItemsInOutL

        assert cursor != null;
        datanum.put("item_serial",cursor.getString(0));
        datanum.put("trans_num",cursor.getString(1));
        datanum.put("trans_type",cursor.getString(2));
        datanum.put("trans_date",cursor.getString(3));
        datanum.put("note",cursor.getString(4));
        datanum.put("is_new",cursor.getString(6));
        datanum.put("TransCode",cursor.getString(5));
        data.add(datanum);
        cursor.close();
        return data;
    }
    public List<Map<String,String>>  getAllItemsInOutH() {
        datanum = new HashMap<>();
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_ITEM_HEADER;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        List<Map<String, String>> data = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);                                                       // The sort order

         if (cursor != null&& cursor.moveToFirst()){
do{
//
         datanum = new HashMap<>();
        datanum.put("Vserial",cursor.getString(0));
        datanum.put("TransNum",cursor.getString(1));
        datanum.put("trans_type",cursor.getString(2));
        datanum.put("TransDate",cursor.getString(3));
        datanum.put("Notes",cursor.getString(4));
        datanum.put("is_new",cursor.getString(5));
        datanum.put("TransCode",cursor.getString(6));
        data.add(datanum);
        } while (cursor.moveToNext());
        cursor.close();}

        return data;
    }
    public List<ItemsInOutH> getAllItemheader() {
        List<ItemsInOutH> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_ITEM_HEADER;

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {

                ItemsInOutH item = new ItemsInOutH();
                item.setSerial(cursor.getString(0));
//                item.setTransType(cursor.getInt(1));
                item.setTransNum(cursor.getString(1));
                item.setTransCode(cursor.getString(6));
                item.setTransDate(cursor.getString(3));
                item.setNotes(cursor.getString(4));
                item.setIsNew(cursor.getInt(5));

                String Transcode = cursor.getString(6);
                InvTransTypes_DAO dao = new InvTransTypes_DAO(mContext);
                InvTransTypes Trans = dao.getTransBycode(Transcode);
                if (Trans != null)
                    item.setmTranstype(Trans);
                // Adding contact to list
                contactList.add(item);
            } while (cursor.moveToNext());

            // make sure to close the cursor
            cursor.close();
        }

        // return contact list
        return contactList;
    }
    // todo DELETE all table
    public boolean deleteTable() {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        int affectedRows = database.delete(DBHelper.TABLE_ITEM_HEADER, null, null);
        return affectedRows > 0;
    }
    List<ItemsInOutH> getItemsInOutHOfTrans(String Transcode) {
        List<ItemsInOutH> listItemsInOutH = new ArrayList<>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_ITEM_HEADER, mAllColumns,
                DBHelper.COLUMN_mTransCode + " = ?",
                new String[] { String.valueOf(Transcode) }, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ItemsInOutH item = cursorToItemHdr(cursor);
            listItemsInOutH.add(item);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return listItemsInOutH;
    }

    public ItemsInOutH getserialByItemId(String ID) {
        Cursor cursor = mDatabase.query(DBHelper.TABLE_ITEM_HEADER, mAllColumns,
                DBHelper.COLUMN_ITEM_SERIAL + " = ?",
                new String[] { String.valueOf(ID) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursorToItemHdr (cursor);
    }

    private ItemsInOutH cursorToItemHdr(Cursor cursor) {
        ItemsInOutH item = new ItemsInOutH();
        item.setSerial(cursor.getString(0));
//        item.setTransType(cursor.getInt(1)); //fk
        item.setTransNum(cursor.getString(1));
        item.setTransCode(cursor.getString(6));
        item.setTransDate(cursor.getString(3));
        item.setNotes(cursor.getString(4));
        item.setIsNew(cursor.getInt(5));

        // get The trans type  by transcode
        String Transcode = cursor.getString(6);
        InvTransTypes_DAO dao = new InvTransTypes_DAO(mContext);
        InvTransTypes Trans = dao.getTransBycode(Transcode);
        if (Trans != null)
            item.setmTranstype(Trans);

        return item;
    }
}
