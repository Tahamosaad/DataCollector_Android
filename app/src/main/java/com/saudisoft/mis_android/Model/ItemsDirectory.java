package com.saudisoft.mis_android.Model;
import java.io.Serializable;

public class ItemsDirectory implements Serializable {

	public static final String TAG = "ItemsDirectory_DAO";
	private static final long serialVersionUID = -7406082437623008161L;
	
	
	private String mItemCode;	// [nvarchar](12) NOT NULL,
	private String mItemName;	//[nvarchar](200) NULL,
	private String mPartNumber;	// [nvarchar](50) NULL,
	
	public ItemsDirectory() {}
	
	public ItemsDirectory(String ItemCode, String ItemName, String PartNumber) {
		this.mItemCode = ItemCode;
		this.mItemName = ItemName;
		this.mPartNumber = PartNumber;
	}
	public String getItemCode() {
		return mItemCode;
	}
	public void setItemCode(String mItemCode) {
		this.mItemCode = mItemCode;
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
	}}
