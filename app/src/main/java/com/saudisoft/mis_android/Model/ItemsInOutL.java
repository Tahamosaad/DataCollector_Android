package com.saudisoft.mis_android.Model;
import java.io.Serializable;

public class ItemsInOutL implements Serializable {

	public static final String TAG = "ItemsInOutL_DAO";
	private static final long serialVersionUID = -7406082437623008161L;

	private String mID;				// PK [nvarchar](50) NOT NULL,
	private String mItemName;		//[nvarchar](200) NULL,
	private String mPartNumber;		// [nvarchar](50) NULL,
	private float mQty;				// [float] NOT NULL CONSTRAINT [DF_ItemsInOutL_Qty]  DEFAULT (1),
	private String  mItemSerial;// FK[nvarchar](20) NOT NULL,
	private String  mItemcode;// FK[nvarchar](12) NOT NULL,




	public ItemsInOutL() {}


	public ItemsInOutL(String ID, String ItemName, String PartNumber, float Qty, String ItemCode, String ItemSerial)	{
		this.mID=ID;
		this.mItemName = ItemName;
		this.mPartNumber = PartNumber;
		this.mQty = Qty;
		this.mItemSerial=ItemSerial;
		this.mItemcode=ItemCode;


	}
	public String getID() {
		return mID;
	}
	public void setID(String mID) {
		this.mID = mID;
	}

	public float getQty() {
		return mQty;
	}
	public void setQty(float mQty) {
		this.mQty = mQty;
	}

	public String getItemName() {
		return mItemName;
	}
	public void setItemName(String mItemName) {
		this.mItemName = mItemName;
	}

	public String getPartNumber() {
		return mPartNumber;
	}
	public void setPartNumber(String mPartNumber) {
		this.mPartNumber = mPartNumber;
	}

	public String getItemSerial() {
		return mItemSerial;
	}

	public void setItemSerial(String mItemSerial) {
		this.mItemSerial = mItemSerial;
	}

	public String getItemcode() {
		return mItemcode;
	}

	public void setItemcode(String mItemcode) {
		this.mItemcode = mItemcode;
	}

//	public ItemsInOutH getmItemSerial() {
//		return mItemSerial;
//	}
//	public void setmItemSerial(ItemsInOutH mItemSerial) {
//		this.mItemSerial = mItemSerial;
//	}
//
//	public ItemsDirectory getmItemcode() {
//		return mItemcode;
//	}
//	public void setmItemcode(ItemsDirectory mItemcode) {
//		this.mItemcode = mItemcode;
//	}
}

