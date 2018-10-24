package com.saudisoft.mis_android.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saudisoft.mis_android.Model.ItemsDirectory;
import com.saudisoft.mis_android.Model.ItemsInOutL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by taha.mosaad on 30/07/2018.
 */

public class ItemsInOutL_DAO {
    public static final String TAG = "ItemsInOutL_DAO";
    Map<String,String> datanum;

    // Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private Context mContext;
    private String[] mAllColumns = {
            DBHelper.COL_ITEM_ID,
            DBHelper.COL_ITEMNAME_DTL,
            DBHelper.COL_PARTNUMBER_DTL,
            DBHelper.COL_QTY,
            DBHelper.COL_SERIAL,
            DBHelper.COL_ITEMCODE_DTL};

    public ItemsInOutL_DAO(Context context) {
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
    public void addItemDetail(ItemsInOutL item) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//        ItemInOutH_DAO dao = new ItemInOutH_DAO(mContext);
//        ItemsInOutH serials = dao.getserialByItemId(item.getItemSerial());
        ItemsDirectory_DAO dao1 = new ItemsDirectory_DAO(mContext);
        ItemsDirectory itemDir = dao1.getItemByItemcode(item.getItemcode());
        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_ITEM_ID, item.getID());
        values.put(DBHelper.COL_ITEMNAME_DTL, item.getItemName());
        values.put(DBHelper.COL_PARTNUMBER_DTL, item.getPartNumber());
        values.put(DBHelper.COL_QTY, item.getQty());
        values.put(DBHelper.COL_SERIAL, item.getItemSerial());
        values.put(DBHelper.COL_ITEMCODE_DTL, item.getItemcode());


        // Inserting Row
        db.insert(DBHelper.TABLE_ITEM_InOutL, null, values);
        db.close(); // Closing database connection
    }
    public ItemsInOutL getItemByItemID(String ID) {
        Cursor cursor = mDatabase.query(DBHelper.TABLE_ITEM_InOutL, mAllColumns,
                DBHelper.COL_ITEM_ID + " = ?",
                new String[] { String.valueOf(ID) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        ItemsInOutL Item_id = cursorToItemsInOutL(cursor);
        return Item_id;
    }

    // Getting single Settings
    public  List<Map<String,String>> getItemDetails(String serial) {
        datanum = new HashMap<String,String>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        List<Map<String, String>> data = null;
        data = new ArrayList<Map<String, String>>();
        Cursor cursor = db.query(
                DBHelper.TABLE_ITEM_InOutL,        // The table to query
                mAllColumns,       // The array of columns to return (pass null to get all)
                DBHelper.COL_SERIAL + "=?",                          // The array of columns to return (pass null to get all)
                new String[] { (serial) },                         // The columns for the WHERE clause
                null,                                                        // The values for the WHERE clause
                null,                                                        // don't group the rows
                null,                                                        // don't filter by row groups
                null);                                                       // The sort order
        if (cursor != null)
            cursor.moveToFirst();

        ItemsInOutL itemdtl = new ItemsInOutL(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getFloat(3),cursor.getString(4),cursor.getString(5));
        // return ItemsInOutL
        datanum.put("ID",cursor.getString(0));
        datanum.put("Serial",cursor.getString(4));
        datanum.put("PartNo",cursor.getString(5));
        datanum.put("Itemname",cursor.getString(1));
        datanum.put("ItemCode",cursor.getString(2));
        datanum.put("QTY",cursor.getString(3));
        data.add(datanum);

        return data;
    }
    public List<ItemsInOutL> getAllItemDetails(String serial) {
        List<ItemsInOutL> itemsList = new ArrayList<ItemsInOutL>();
        // Select All Query
//        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_ITEM_InOutL;

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
        Cursor cursor = db.query(
                DBHelper.TABLE_ITEM_InOutL,        // The table to query
                mAllColumns,       // The array of columns to return (pass null to get all)
                DBHelper.COL_SERIAL + "=?",                          // The array of columns to return (pass null to get all)
                new String[] { (serial) },                         // The columns for the WHERE clause
                null,                                                        // The values for the WHERE clause
                null,                                                        // don't group the rows
                null,                                                        // don't filter by row groups
                null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                ItemsInOutL itemdtl = new ItemsInOutL();
                itemdtl.setID(cursor.getString(0));
                itemdtl.setItemSerial(cursor.getString(1));
                itemdtl.setPartNumber(cursor.getString(2));
                itemdtl.setItemName(cursor.getString(3));
                itemdtl.setItemcode(cursor.getString(4));
                itemdtl.setQty(cursor.getInt(5));

//                String Itemcode = cursor.getString(4);
//
//                ItemsDirectory_DAO dao1 = new ItemsDirectory_DAO(mContext);
//                ItemsDirectory Item = dao1.getItemByItemcode(Itemcode);
//
//                if (Item != null)
//                    itemdtl.setItemcode( Itemcode);
                // Adding  to list
                itemsList.add(itemdtl);
            } while (cursor.moveToNext());
            // make sure to close the cursor
            cursor.close();
        }

        // return contact list
        return itemsList;
    }
    public List<Map<String,String>> GetAllItemDetails() {
//        List<ItemsInOutL> itemsList = new ArrayList<ItemsInOutL>();
        datanum = new HashMap<String,String>();
        List<Map<String, String>> data = null;
        data = new ArrayList<Map<String, String>>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_ITEM_InOutL;

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

//                ItemsInOutL itemdtl = new ItemsInOutL();
//                itemdtl.setID(cursor.getString(0));
//                itemdtl.setItemSerial(cursor.getString(1));
//                itemdtl.setPartNumber(cursor.getString(2));
//                itemdtl.setItemName(cursor.getString(3));
//                itemdtl.setItemcode(cursor.getString(4));
//                itemdtl.setQty(cursor.getInt(5));

                datanum.put("ID",cursor.getString(0));
                datanum.put("Serial",cursor.getString(1));
                datanum.put("PartNo",cursor.getString(2));
                datanum.put("Itemname",cursor.getString(3));
                datanum.put("ItemCode",cursor.getString(4));
                datanum.put("QTY",cursor.getString(5));
                data.add(datanum);
            } while (cursor.moveToNext());
            // make sure to close the cursor
            cursor.close();
        }

        // return Item dtl  list
        return data;
    }


    // todo DELETE all table
    public boolean deleteTable() {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        int affectedRows = database.delete(DBHelper.TABLE_ITEM_InOutL, null, null);
        return affectedRows > 0;
    }

    protected ItemsInOutL cursorToItemsInOutL(Cursor cursor) {
        ItemsInOutL itemdtl = new ItemsInOutL();
        itemdtl.setID(cursor.getString(0));
        itemdtl.setItemName(cursor.getString(1));
        itemdtl.setPartNumber(cursor.getString(2));
        itemdtl.setQty(cursor.getInt(3));
        itemdtl.setItemcode(cursor.getString(5));
        itemdtl.setItemSerial(cursor.getString(4));

        String Itemcode = cursor.getString(5);

        ItemsDirectory_DAO dao1 = new ItemsDirectory_DAO(mContext);
        ItemsDirectory Item = dao1.getItemByItemcode(Itemcode);
//        ItemInOutH_DAO dao2 = new ItemInOutH_DAO(mContext);
//        ItemsInOutH serial = dao2.getserialByItemId(ItemID);
        if (Item != null)
            itemdtl.setItemcode( Itemcode);
////        if (serial != null)
//            itemdtl.setItemSerial(cursor.getString(4));
        return itemdtl;
    }
}
