package com.saudisoft.mis_android.Model;
import java.io.Serializable;

public class ItemSerials implements Serializable {

	public static final String TAG = "ItemSerials";
	private static final long serialVersionUID = -7406082437623008161L;




	private String mSerial;	// PK[nvarchar](20) NOT NULL,
	private String mSerialNum;	// [nvarchar](100) NOT NULL,
	private String  mItemID;		// FK [nvarchar](50) NULL,
	private ItemsInOutL mID;
	
	public ItemSerials() {}
	
	public ItemSerials( String Serial, String SerialNum,String ID) {
		this.mSerial = Serial;
		this.mSerialNum = SerialNum;
		this.mItemID=ID;
	}

	public String getSerial() {return mSerial;}
	public void setSerial(String mSerial) {
		this.mSerial = mSerial;
	}
	public String getSerialNum() {
		return mSerialNum;
	}
	public void setSerialNum(String mSerialNum) {
		this.mSerialNum = mSerialNum;
	}
	public String getmItemID() {
		return mItemID;
	}
	public void setmItemID(String mItemID) {
		this.mItemID = mItemID;
	}
	public ItemsInOutL getmID() {
		return mID;
	}

	public void setmID(ItemsInOutL mID) {
		this.mID = mID;
	}
}
