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

public class InvTransTypes_DAO {

	public static final String TAG = "InvTransTypes_DAO";

	private Context mContext;

	// Database fields
	private SQLiteDatabase mDatabase;
	private DBHelper mDbHelper;
	private String[] mAllColumns = { DBHelper.COLUMN_mTransCode,
			DBHelper.COLUMN_mTransName,
			DBHelper.COLUMN_mTransType,
			DBHelper.COLUMN_mSystem };

	public InvTransTypes_DAO(Context context) {
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
	public void addInvTransTypes(InvTransTypes Trans) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_mTransCode, Trans.getTransCode());
		values.put(DBHelper.COLUMN_mTransName, Trans.getTransName());
		values.put(DBHelper.COLUMN_mTransType, Trans.getTransType());
		values.put(DBHelper.COLUMN_mSystem, Trans.getSystem());

		// Inserting Row
		db.insert(DBHelper.TABLE_InvTransTypes, null, values);
		db.close(); // Closing database connection
	}
	public InvTransTypes createInvTransTypes(String TransCode, String TransName, String TransType, String System) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_mTransCode, TransCode);
		values.put(DBHelper.COLUMN_mTransName, TransName);
		values.put(DBHelper.COLUMN_mTransType, TransType);
		values.put(DBHelper.COLUMN_mSystem, System);
		long insertId = mDatabase
				.insert(DBHelper.TABLE_InvTransTypes, null, values);
		Cursor cursor = mDatabase.query(DBHelper.TABLE_InvTransTypes, mAllColumns,
				DBHelper.COLUMN_mTransCode + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		InvTransTypes newInvTransTypes = cursorToInvTransTypes(cursor);
		cursor.close();
		return newInvTransTypes;
	}

	public void deleteInvTransTypes(InvTransTypes trans) {
		String transCode = trans.getTransCode();
		// delete all employees of this trans
		ItemInOutH_DAO itemDao = new ItemInOutH_DAO(mContext);
		List<ItemsInOutH> listItems = itemDao.getItemsInOutHOfTrans(transCode);
		if (listItems != null && !listItems.isEmpty()) {
			for (ItemsInOutH e : listItems) {
				itemDao.deleteItemsInOutH(e);
			}
		}

		System.out.println("the deleted trans has the id: " + transCode);
		mDatabase.delete(DBHelper.TABLE_InvTransTypes, DBHelper.COLUMN_mTransCode
				+ " = " + transCode, null);
	}

	public List<InvTransTypes> getAllInvTransTypes() {
		List<InvTransTypes> listTrans = new ArrayList<InvTransTypes>();

		Cursor cursor = mDatabase.query(DBHelper.TABLE_InvTransTypes, mAllColumns,
				null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				InvTransTypes trans = cursorToInvTransTypes(cursor);
				listTrans.add(trans);
				cursor.moveToNext();
			}

			// make sure to close the cursor
			cursor.close();
		}
		return listTrans;
	}
//overload
	public List<InvTransTypes> getAllInvTransType() {
		List<InvTransTypes> transtList = new ArrayList<InvTransTypes>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + DBHelper.TABLE_InvTransTypes;

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				InvTransTypes trans = new InvTransTypes();
				trans.setTransCode(cursor.getString(0));
				trans.setTransName(cursor.getString(1));
				trans.setTransType(cursor.getInt(2));
				trans.setSystem(cursor.getInt(3));
				// Adding contact to list
				transtList.add(trans);
			} while (cursor.moveToNext());
		}

		// return contact list
		return transtList;
	}
	public InvTransTypes getTransBycode(String code) {
		Cursor cursor = mDatabase.query(DBHelper.TABLE_InvTransTypes, mAllColumns,
				DBHelper.COLUMN_mTransCode + " = ?",
				new String[] { String.valueOf(code) }, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		InvTransTypes trans = cursorToInvTransTypes(cursor);
		return trans;
	}

	protected InvTransTypes cursorToInvTransTypes(Cursor cursor) {
		InvTransTypes trans = new InvTransTypes();
		trans.setTransCode(cursor.getString(0));
		trans.setTransName(cursor.getString(1));
		trans.setTransType(cursor.getInt(2));
		trans.setSystem(cursor.getInt(3));
		return trans;
	}

}
