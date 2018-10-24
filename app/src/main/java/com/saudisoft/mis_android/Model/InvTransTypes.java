package com.saudisoft.mis_android.Model;
import java.io.Serializable;

public class InvTransTypes implements Serializable {

	public static final String TAG = "InvTransTypes";
	private static final long serialVersionUID = -7406082437623008188L;
	
	private String mTransCode; //[nvarchar](4) NOT NULL,
	private String mTransName; //[nvarchar](20) NOT NULL,
	private int mTransType; //[int] NOT NULL CONSTRAINT [DF_TransTypes_TransType]  DEFAULT (1),
	private int mSystem; //[int] NOT NULL CONSTRAINT [DF_TransTypes_System]  DEFAULT (0),
	
	public InvTransTypes() {}
	
	public InvTransTypes(String TransCode, String TransName, int TransType, int System) {
		this.mTransCode = TransCode;
		this.mTransName = TransName;
		this.mTransType = TransType;
		this.mSystem = System;
	}
	public String getTransCode() {
		return mTransCode;
	}
	public void setTransCode(String mTransCode) {
		this.mTransCode = mTransCode;
	}
	public String getTransName() {
		return mTransName;
	}
	public void setTransName(String mTransName) {
		this.mTransName = mTransName;
	}
	public int getTransType() {
		return mTransType;
	}
	public void setTransType(int mTransType) {
		this.mTransType = mTransType;
	}
	public int getSystem() {
		return mSystem;
	}
	public void setSystem(int mSystem) {
		this.mSystem = mSystem;
	}
}
