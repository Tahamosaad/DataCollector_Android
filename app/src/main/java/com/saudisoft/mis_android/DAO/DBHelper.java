package com.saudisoft.mis_android.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	public static final String TAG = "DBHelper";


	//region DataBase Tables Columns
	// columns of the companies table
	public static final String TABLE_COMPANIES = "companies";
	public static final String COLUMN_COMPANY_ID = "_id";
	public static final String COLUMN_COMPANY_NAME = "company_name";
	public static final String COLUMN_COMPANY_ADDRESS = "address";
	public static final String COLUMN_COMPANY_WEBSITE = "website";
	public static final String COLUMN_COMPANY_PHONE_NUMBER = "phone_number";

	public static final String TABLE_SETTINGS = "settings";
	public static final String COLUMN_SERVER_NAME = "server_name";
	public static final String COLUMN_DATABASE_NAME = "database_name";
	public static final String COLUMN_BRANCH_CODE = "branch_code";
	public static final String COLUMN_DATABASE_USER_NAME = "database_user_name";

	// columns of the employees table
	public static final String TABLE_EMPLOYEES = "employees";
	public static final String COLUMN_EMPLOYE_ID = COLUMN_COMPANY_ID;
	public static final String COLUMN_EMPLOYE_FIRST_NAME = "first_name";
	public static final String COLUMN_EMPLOYE_LAST_NAME = "last_name";
	public static final String COLUMN_EMPLOYE_ADDRESS = COLUMN_COMPANY_ADDRESS;
	public static final String COLUMN_EMPLOYE_EMAIL = "email";
	public static final String COLUMN_EMPLOYE_PHONE_NUMBER = COLUMN_COMPANY_PHONE_NUMBER;
	public static final String COLUMN_EMPLOYE_SALARY = "salary";
	public static final String COLUMN_EMPLOYE_COMPANY_ID = "company_id";

	public static final String TABLE_InvTransTypes = "InvTransTypes";
	public static final String COLUMN_mTransCode = "TransCode";
	public static final String COLUMN_mTransName = "TransName";
	public static final String COLUMN_mTransType = "TransType";
	public static final String COLUMN_mSystem = "System";
//	public static final String COLUMN_InvTransTypes_COMPANY_ID = "company_id";//forgin key

	public static final String TABLE_ITEM_HEADER = "item_header";
	public static final String COLUMN_ITEM_SERIAL = "item_serial";
	public static final String COLUMN_TRANS_NUMBER = "trans_num";
	public static final String COLUMN_TRANS_TYPE = "trans_type";
	public static final String COLUMN_TRANS_DATE = "trans_date";
	public static final String COLUMN_TRANS_NOTE = "note";
	public static final String COLUMN_IS_NEW= "is_new";
	public static final String COLUMN_ITEM_TRANS_CODE = "TransCode";//forgin key

	// Table ItemSerials
	public static final String TABLE_ITEM_SERIAL = "ItemSerials";
	// Table Columns
	public static final String COL_ID = "ID";
	public static final String COL_SERIALNUM = "SerialNum";
	public static final String COLUMN_SERIAL = "VoucherSerial";

	// Table ItemsDirectory
	public static final String TABLE_ITEM_DIRECTORY = "ItemsDirectory";

	// Table Columns
	public static final String COL_ITEMCODE = "ItemCode"; //PK
	public static final String COL_ITEMNAME = "ItemName"; //////////
	public static final String COL_PARTNUMBER = "PartNumber";////////

	// Table ItemsInOutL
	public static final String TABLE_ITEM_InOutL = "ItemsInOutL";
	// Table Columns
	public static final String COL_ITEM_ID = COL_ID;//PK
	public static final String COL_SERIAL = COLUMN_ITEM_SERIAL; //FK
	public static final String COL_ITEMCODE_DTL = COL_ITEMCODE; //FK
	public static final String COL_ITEMNAME_DTL = COL_ITEMNAME;//
	public static final String COL_PARTNUMBER_DTL = COL_PARTNUMBER;
	public static final String COL_QTY = "Qty";

	//endregion

	private static final String DATABASE_NAME = "misdb.db";
	private static final int DATABASE_VERSION = 1;


	//region Create Tables
	// SQL statement of the employees table creation
	private static final String SQL_CREATE_TABLE_EMPLOYEES = "CREATE TABLE IF NOT EXISTS " + TABLE_EMPLOYEES + "("
			+ COLUMN_EMPLOYE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_EMPLOYE_FIRST_NAME + " TEXT NOT NULL, "
			+ COLUMN_EMPLOYE_LAST_NAME + " TEXT NOT NULL, "
			+ COLUMN_EMPLOYE_ADDRESS + " TEXT NOT NULL, "
			+ COLUMN_EMPLOYE_EMAIL + " TEXT NOT NULL, "
			+ COLUMN_EMPLOYE_PHONE_NUMBER + " TEXT NOT NULL, "
			+ COLUMN_EMPLOYE_SALARY + " REAL NOT NULL, "
			+ COLUMN_EMPLOYE_COMPANY_ID + " INTEGER NOT NULL "
			+");";

	// SQL statement of the employees table creation
	private static final String SQL_CREATE_TABLE_InvTransTypes = "CREATE TABLE IF NOT EXISTS " + TABLE_InvTransTypes + "("
			+ COLUMN_mTransCode + " TEXT PRIMARY KEY , "
			+ COLUMN_mTransName + " TEXT NOT NULL, "
			+ COLUMN_mTransType + " INTEGER NOT NULL, "
			+ COLUMN_mSystem + " INTEGER NOT NULL "
			+");";

	// SQL statement of the ITEM_HEADER table creation
	private static final String SQL_CREATE_TABLE_ITEM_HEADER = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEM_HEADER + "("
			+ COLUMN_ITEM_SERIAL + " TEXT PRIMARY KEY , "
			+ COLUMN_TRANS_NUMBER + " TEXT , "
			+ COLUMN_TRANS_TYPE + " INTEGER , "
			+ COLUMN_TRANS_DATE + " TEXT , "
			+ COLUMN_TRANS_NOTE + " TEXT , "
			+ COLUMN_IS_NEW + " INTEGER NOT NULL, "
			+ COLUMN_ITEM_TRANS_CODE + " TEXT NOT NULL "
			+");";
	// Create Table Statement
	public static final String SQL_CREATE_TABLE_ITEM_InOutL = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEM_InOutL + " ( " +
			COL_ITEM_ID        + " TEXT NOT NULL PRIMARY KEY, " +
			COL_SERIAL         + " TEXT, " +
			COL_ITEMCODE_DTL   + " TEXT NOT NULL, " +
			COL_ITEMNAME_DTL   + " TEXT, " +
			COL_PARTNUMBER_DTL + " TEXT, " +
			COL_QTY            + " REAL NOT NULL );";

	// Create Table Statement
	public static final String SQL_CREATE_TABLE_ITEM_SERIAL = "CREATE TABLE IF NOT EXISTS "+TABLE_ITEM_SERIAL+" ( " +
			COL_SERIALNUM + " TEXT NOT NULL PRIMARY KEY," +
			COL_ID + " TEXT NOT NULL," +
			COLUMN_SERIAL + " TEXT );";
	// Create Table Statement
	public static final String SQL_CREATE_TABLE_ITEM_DIRECTORY = "CREATE TABLE IF NOT EXISTS "+TABLE_ITEM_DIRECTORY+" ( " +
			COL_ITEMCODE + " TEXT NOT NULL PRIMARY KEY ," +
			COL_ITEMNAME + " TEXT," +
			COL_PARTNUMBER + " TEXT );";

	// SQL statement of the companies table creation
	private static final String SQL_CREATE_TABLE_COMPANIES = "CREATE TABLE IF NOT EXISTS " + TABLE_COMPANIES + "("
			+ COLUMN_COMPANY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COLUMN_COMPANY_NAME + " TEXT NOT NULL, "
			+ COLUMN_COMPANY_ADDRESS + " TEXT NOT NULL, "
			+ COLUMN_COMPANY_WEBSITE + " TEXT NOT NULL, "
			+ COLUMN_COMPANY_PHONE_NUMBER + " TEXT NOT NULL "
			+");";

	// SQL statement of the TABLE_SETTINGS table creation
	private static final String SQL_CREATE_TABLE_SETTINGS = "CREATE TABLE IF NOT EXISTS " + TABLE_SETTINGS + "("
			+ COLUMN_SERVER_NAME + " TEXT , "
			+ COLUMN_DATABASE_NAME + " TEXT,  "
			+ COLUMN_BRANCH_CODE + " TEXT , "
			+ COLUMN_EMPLOYE_EMAIL + " TEXT , "
			+ COLUMN_DATABASE_USER_NAME + " TEXT  "


			+");";
	//endregion

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(SQL_CREATE_TABLE_COMPANIES);
		database.execSQL(SQL_CREATE_TABLE_InvTransTypes);
		database.execSQL(SQL_CREATE_TABLE_EMPLOYEES);
		database.execSQL(SQL_CREATE_TABLE_SETTINGS);
		database.execSQL(SQL_CREATE_TABLE_ITEM_HEADER);
		database.execSQL(SQL_CREATE_TABLE_ITEM_InOutL);
		database.execSQL(SQL_CREATE_TABLE_ITEM_SERIAL);
		database.execSQL(SQL_CREATE_TABLE_ITEM_DIRECTORY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG,
				"Upgrading the database from version " + oldVersion + " to "+ newVersion);
		// clear all data
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_InvTransTypes);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANIES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM_HEADER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM_InOutL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM_SERIAL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM_DIRECTORY);
		// recreate the tables
		onCreate(db);
	}

	public DBHelper(Context context, String name, CursorFactory factory,int version) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}
}
