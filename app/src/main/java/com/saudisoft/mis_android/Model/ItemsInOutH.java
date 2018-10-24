package com.saudisoft.mis_android.Model;
import java.io.Serializable;

public class ItemsInOutH implements Serializable {

	public static final String TAG = "ItemsInOutH";
	private static final long serialVersionUID = -7406082437623008161L;
	
	private String mSerial;			// [nvarchar](20) NOT NULL,
//	private int mTransType;			// [int] NOT NULL CONSTRAINT [DF_ItemsInOutH_TransType]  DEFAULT ((1)),
	private String mTransNum;		// [nvarchar](20) NULL,
	private String mTransCode;		// [nvarchar](4) NOT NULL,
	private String mTransDate;		// [datetime] NOT NULL CONSTRAINT [DF_ItemsInOutH_TransDate]  DEFAULT (getdate()),
	private String mNotes;			// [nvarchar](300) NULL,
	private int	mIsNew;
	private InvTransTypes mTranstype;
	
	public ItemsInOutH() {

	}
	
	public ItemsInOutH(String Serial, String TransNum, String TransCode, String TransDate, String Notes, int IsNew) {
		this.mSerial = Serial;
//		this.mTransType = TransType;
		this.mTransNum = TransNum;
		this.mTransCode = TransCode;
		this.mTransDate = TransDate;
		this.mNotes = Notes;
		this.mIsNew = IsNew;
	}
	public String getSerial() {
		return mSerial;
	}
	public void setSerial(String mSerial) {
		this.mSerial = mSerial;
	}

	
	public String getTransNum() {
		return mTransNum;
	}
	public void setTransNum(String mTransNum) {
		this.mTransNum = mTransNum;
	}
	public String getTransCode() {
		return mTransCode;
	}
	public void setTransCode(String mTransCode) {
		this.mTransCode = mTransCode;
	}
	public String getTransDate() {
		return mTransDate;
	}
	public void setTransDate(String mTransDate) {
		this.mTransDate = mTransDate;
	}
	public String getNotes() {
		return mNotes;
	}
	public void setNotes(String mNotes) {
		this.mNotes = mNotes;
	}
	public int getIsNew() {
		return mIsNew;
	}
	public void setIsNew(int mIsNew) {
		this.mIsNew = mIsNew;
	}
	public InvTransTypes getmTranstype() {return mTranstype;}
	public void setmTranstype(InvTransTypes mTranstype) {this.mTranstype = mTranstype;}
//	public int getTransType() {
//		return mTransType;
//	}
//	public void setTransType(int mTransType) {
//		this.mTransType = mTransType;
//	}
}
	