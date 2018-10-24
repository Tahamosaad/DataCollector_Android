package com.saudisoft.mis_android.Model;
import java.io.Serializable;
public class Settings implements Serializable {

	public static final String TAG = "Settings";
	private static final long serialVersionUID = -7406082437623008161L;
	
	private String mServerName;			
	private String mDatabaseName;
	private String mBranchCode;		
	private String mDataBaseUserName;

	public Settings() {}
	
	public Settings(String ServerName,String DatabaseName, String BranchCode, String DataBaseUserName)	{
		this.mServerName = ServerName;
		this.mDatabaseName = DatabaseName;
		this.mBranchCode = BranchCode;
		this.mDataBaseUserName = DataBaseUserName;
	}
	public String getServerName() {
		return mServerName;
	}
	public void setServerName(String mServerName) {
		this.mServerName = mServerName;
	}
	public String getDatabaseName() {
		return mDatabaseName;
	}
	public void setDatabaseName(String mDatabaseName) {
		this.mDatabaseName = mDatabaseName;
	}
	public String getBranchCode() {
		return mBranchCode;
	}
	public void setBranchCode(String mBranchCode) {
		this.mBranchCode = mBranchCode;
	}
	public String getDataBaseUserName() {
		return mDataBaseUserName;
	}
	public void setDataBaseUserName(String mDataBaseUserName) {
		this.mDataBaseUserName = mDataBaseUserName;
	}

}

