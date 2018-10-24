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
import java.util.List;

/**
 * Created by taha.mosaad on 30/07/2018.
 */

public class ItemInOutH_DAO {
    public static final String TAG = "ItemInOutH_DAO";

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

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public void addItemHeader(ItemsInOutH item) {
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
        db.insert(DBHelper.TABLE_ITEM_HEADER, null, values);
        db.close(); // Closing database connection
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

    public void deleteItemsInOutH(ItemsInOutH item) {
        String id = item.getSerial();
        System.out.println("the deleted item has the id: " + id);
        mDatabase.delete(DBHelper.TABLE_ITEM_HEADER, DBHelper.COLUMN_ITEM_SERIAL
                + " = " + id, null);
    }

    public List<ItemsInOutH> getAllItemsInOutH() {
        List<ItemsInOutH> listItemsInOutHs = new ArrayList<ItemsInOutH>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_ITEM_HEADER, mAllColumns,
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ItemsInOutH item = cursorToItemHdr(cursor);
            listItemsInOutHs.add(item);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return listItemsInOutHs;
    }
    public List<ItemsInOutH> getAllItemheader() {
        List<ItemsInOutH> contactList = new ArrayList<ItemsInOutH>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_ITEM_HEADER;

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
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
    public List<ItemsInOutH> getItemsInOutHOfTrans(String  Transcode) {
        List<ItemsInOutH> listItemsInOutH = new ArrayList<ItemsInOutH>();

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
        ItemsInOutH item = cursorToItemHdr (cursor);
        return item;
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
