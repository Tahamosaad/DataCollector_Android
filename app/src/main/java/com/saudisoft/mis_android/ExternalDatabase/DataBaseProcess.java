package com.saudisoft.mis_android.ExternalDatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.saudisoft.mis_android.DAO.ItemInOutH_DAO;
import com.saudisoft.mis_android.DAO.Setting_DAO;
import com.saudisoft.mis_android.Model.Settings;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by {Taha.mosaad} on {6/10/2018}.
 */

public class DataBaseProcess extends Activity {
    List<Map<String, String>> headerList;
    String Branchname, DBname, DBserver;
    String Serial, TDate, TType, Tcode, Store, mNote, mIsnew,Tnum, MISUser;
    Date PostDate;
    private ItemInOutH_DAO ItemHeader_DAO;
    private Setting_DAO Setting_DAO;
    CRUD_Operations new_data;
    long DocNum;
    Connection connect;
    String ConnectionResult = "";
    public Boolean isSuccess = false;
    Statement stmt = null;
    public String exString = null;
    ConnectionHelper conStr;

    public DataBaseProcess(ItemInOutH_DAO itemHeader_DAO) {
        ItemHeader_DAO = itemHeader_DAO;
    }

    public void initialization() {

        List<Settings> settings = Setting_DAO.getAllSettings1();
        for (Settings cn : settings) {
            Branchname = cn.getBranchCode();
            DBname = cn.getDatabaseName();
            DBserver = cn.getServerName();
        }
        Store = GetStore(Branchname);
        //SerialValidation = CBool(DLookUp("SELECT SerialNumberValidation FROM BranchCodes WHERE BranchCode='" & BranchCode & "'", SQLcn, False))
        PostDate = GetPostDate(Branchname);

        this.ItemHeader_DAO = new ItemInOutH_DAO(this);
        //  Post ItemsInOutH
        headerList = ItemHeader_DAO.getItemHeader("");

//        for (ItemsInOutH drH : headerList) {
        Serial = headerList.get(0).get("item_serial");
        TType = headerList.get(0).get("trans_type");
        TDate = headerList.get(0).get("trans_date");
        Tnum=headerList.get(0).get("trans_num");
        mNote = headerList.get(0).get("Note");
        mIsnew = headerList.get(0).get("is_new");
        Tcode = headerList.get(0).get("TransCode");

    }

    public final String GetStore(String BranchCode) {
        String storecode = "";
        try {

            connect = conStr.connectionclasss();        // Connect to database
            if (connect == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                // Change below query according to your own database.
                //
                String query = "SELECT DefStoreCode FROM BranchCodes WHERE BranchCode= " + BranchCode;

                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
//                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd ");
//                    Date mydate=new SimpleDateFormat("dd-MM-yyyy").parse(rs.getString("serverdate"));
                    storecode = rs.getString("DefStoreCode");

                }
                ConnectionResult = "successful";
                isSuccess = true;
                connect.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Error", ex.toString());
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        } finally {
            if (stmt != null) try {
                stmt.close();
            } catch (Exception e) {e.getMessage();}
            if (connect != null) try {
                connect.close();
            } catch (Exception e) {e.getMessage();}
        }

        return storecode;


    }

    public final java.sql.Date GetPostDate(String BranchCode) {

        java.sql.Date PostDate = (java.sql.Date) Calendar.getInstance().getTime();
        try {

            connect = conStr.connectionclasss();        // Connect to database
            if (connect == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                // Change below query according to your own database.
                //
                String query = "SELECT PostDate FROM Posting WHERE TransCode='STK' AND BranchCode=" + BranchCode;

                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    PostDate = rs.getDate("PostDate");


                }
                ConnectionResult = "successful";
                isSuccess = true;
                connect.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Error", ex.toString());
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        } finally {
            if (stmt != null) try {
                stmt.close();
            } catch (Exception e) {e.getMessage();}
            if (connect != null) try {
                connect.close();
            } catch (Exception e) {e.getMessage();}
        }

        return PostDate;


    }

    public void CheckItemHdr() {

//        Cursor.Current = Cursors.WaitCursor;
//            Date mypostdate = new SimpleDateFormat("dd-MM-yyyy").parse(TDate);
        Date mypostdate = null;
        if (mypostdate.compareTo(PostDate) < 1) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    //set icon
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    //set title
                    .setTitle("Cannot Add Voucher ")
                    //set message
                    .setMessage(Serial + "(. Document date must be more than last post date " + PostDate + "  Continue Adding Next?")
                    //set positive button
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //set what would happen when positive button is clicked
//                                 goto SkipVoucher;
                        }
                    })
                    //set negative button
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //set what should happen when negative button is clicked
//                                goto ExitSub;
//                        Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                        }
                    }).show();
//                if ((Toast.makeText(("Cannot Add Voucher " + (Serial + (". Document date must be more than last post date (" + (PostDate + (")  (SyncStateContract.Constants.vbCrLf"+"  Continue Adding Next?"))))

        }


    }


    public void HDR() {
        try {
            connect = conStr.connectionclasss();        // Connect to database
            if (connect == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {

                String SQL = "INSERT INTO ItemsInOutH (Serial,BranchCode,DocNum,TransType,TransCode,TransDate,StoreCode,DestBranchCode,CurrencyCode,ChangeRate,Notes,IssuedBy,ServerCode) VALUES ('";
                SQL = SQL + Serial + "','" + Branchname + "','" + DocNum + "','" + TType + "','";
                SQL = SQL + Tcode + "','" + new SimpleDateFormat(TDate) + "','";
                SQL = SQL + Store + "','" + Branchname + "','001','1','PPC " + mNote + "','";
                SQL = SQL + MISUser + "','" + Branchname + "')";
                stmt = connect.createStatement();
                stmt.executeUpdate(SQL);
                Log.e("Success", "Success");
                isSuccess = true;

            }
        }
//                 Handle any errors that may have occurred.
        catch (Exception exception) {
            exception.printStackTrace();
            Log.e("Error", exception.toString());

            exString = exception.toString();

        } finally {
            if (stmt != null) try {
                stmt.close();
            } catch (Exception e) {e.getMessage();}
            if (connect != null) try {
                connect.close();
            } catch (Exception e) {e.getMessage();}
        }

    //  ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    //////            SqlTransaction t = c.BeginTransaction;
    //  If drH("IsNew") Then
    //  DocNum = Val(DLookUp("SELECT MAX(DocNum) FROM ItemsInOutH WHERE TransType='" & TType & "'", c, t)) + 1
    //  Serial = BranchCode & BranchCode & IIf(TType = 1, "r", "i")
    //  Serial = Serial & DocNum
    //  '==============================================================================================================================================
    //  SQL = "INSERT INTO ItemsInOutH (Serial,BranchCode,DocNum,TransType,TransCode,TransDate,StoreCode,DestBranchCode,CurrencyCode,ChangeRate,Notes,IssuedBy,ServerCode) VALUES ('"
    //  SQL = SQL & Serial & "','" & BranchCode & "','" & DocNum & "','" & TType & "','"
    //  SQL = SQL & drH("TransCode") & "','" & Format(drH("TransDate"), "MM/dd/yyyy") & "','"
    //  SQL = SQL & Store & "','" & BranchCode & "','001','1','PPC " & nz(drH("Notes"), "") & "','"
    //  SQL = SQL & MISUser & "','" & BranchCode & "')"
    //  If ExecuteSQL(c, SQL, t) <= 0 Then
    //  t.Rollback()
    //  If MsgBox("Cannot Add Voucher " & drH("Serial") & vbCrLf & "Continue Adding Next?", MsgBoxStyle.YesNo) = MsgBoxResult.Yes Then
    //  GoTo SkipVoucher
    //  Else
    //  GoTo ExitSub
    //  End If
    //  End If
    //  Else
//            DocNum = Conversion.Val(VisualBasic.Strings.Mid(Serial, (1 + Strings.InStrRev(Serial, Interaction.( (TType == 1) ? "r" : "i" )))));
//            ExecuteSQL(c, ("DELETE FROM ItemSerials WHERE Serial="+ (Serial + "")), t);
//            DataRow drD;
    //  End If
}
/*
    public void DTL(){
            //  ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------

            DataTable dtItemsDtl = DLookUp(("SELECT * FROM ItemsInOutL WHERE Serial=" + (drH["Serial"] + "")));
//            for (var drD : dtItemsDtl.Rows) {
                //  If drH("IsNew") Then
                //  If TType = -1 Then
                //  SQL = "SELECT SUM(ItemsInOutL.Qty * ItemsInOutH.TransType) FROM ItemsInOutL, ItemsInOutH  "
                //  SQL = SQL & "WHERE (ItemsInOutL.Serial=ItemsInOutH.Serial) AND  "
                //  SQL = SQL & "(ItemsInOutH.StoreCode ='" & Store & "') AND "
                //  SQL = SQL & "(ItemsInOutL.ItemCode='" & drD("ItemCode") & "')"
                //  Dim B As Double = Val(DLookUp(SQL, c, t))
                //  If B >= Val(drD("Qty")) Then
                //  SQL = SQL & " AND (ItemsInOutH.TransDate<='" & Format(drH("TransDate"), "MM/dd/yyyy") & "')"
                //  B = Val(DLookUp(SQL, c, t))
                //  End If
                //  If B < Val(drD("Qty")) Then
                //  t.Rollback()
                //  If MsgBox("Item " & drD("ItemCode") & " In Voucher " & drH("Serial") & " is out of stock" & vbCrLf & "Continue Adding Next Voucher?", MsgBoxStyle.YesNo) = MsgBoxResult.Yes Then
                //  GoTo SkipVoucher
                //  Else
                //  GoTo ExitSub
                //  End If
                //  End If
                //  End If
                //  SQL = "INSERT INTO ItemsInOutL (Serial,ID,ItemCode,Qty,StoreCode) VALUES ('" ''3
                //  SQL = SQL & Serial & "','" & Serial & "_VCH_" & drD("ItemCode") & "_1','"
                //  SQL = SQL & drD("ItemCode") & "','" & drD("Qty") & "','" & Store & "')"
                //  If ExecuteSQL(c, SQL, t) <= 0 Then
                //  t.Rollback()
                //  If MsgBox("Can not add Item " & drD("ItemCode") & " In Voucher " & drH("Serial") & vbCrLf & "Continue Adding Next Voucher?", MsgBoxStyle.YesNo) = MsgBoxResult.Yes Then
                //  GoTo SkipVoucher
                //  Else
                //  GoTo ExitSub
                //  End If
                //  End If
                //  End If
                DataTable dtItemsSer = DLookUp(("SELECT * FROM ItemSerials WHERE ID="+ (drD["ID"] )));



                   String SQL = "INSERT INTO ItemSerials(ID,Serial,TransCode,TransNum,TransDate,DocNum,ItemCode,SerialNum,IssuedBy) VALUES(";

                //  If drH("IsNew") Then
                //  SQL = SQL & Serial & "_VCH_" & drD("ItemCode") & "_1','"
                //  Else
                SQL = (SQL+ (drD["ID"] + ","));
                //  End If
                SQL = (SQL+ (Serial + (","+ (drH["TransCode"] + ("," + (nz(drH["TransNum"], "") + ("," + (Format(drH["TransDate"], "MM/dd/yyyy") + ","))))))));
                SQL = (SQL+ (DocNum + (","+ (drD["ItemCode"] + (","+ (drS["SerialNum"] + ("," + (MISUser + ")"))))))));
                boolean Found = ((TType == "1")|| CheckSerialIn(drS["SerialNum"].ToString, drD["ItemCode"].ToString, Store, c, t));
                if ((!Found|| (ExecuteSQL(c, SQL, t) <= 0))) {
                    t.Rollback();
                    if ((MsgBox(("Can not add Serial Number "+ (drS["SerialNum"] + (" in Item "
                            + (drD["ItemCode"] + (" In Voucher "
                            + (drH["Serial"]
                            + (Interaction.( !Found ? ", Serial number not found." : "." )
                            + (Constants.vbCrLf + "Continue Adding Next Serial Numbers?")))))))),
                    MsgBoxStyle.YesNo) == MsgBoxResult.Yes)) {
                            goto SkipVoucher;
                        }
                        else {
                            goto ExitSub;
                        }

                    }

                }
            t.Commit();
            ExecuteSQL(("DELETE FROM ItemSerials WHERE Serial=" + (drH["Serial"] )));
            ExecuteSQL(("DELETE FROM ItemsInOutL WHERE Serial=" + (drH["Serial"] )));
            ExecuteSQL(("DELETE FROM ItemsInOutH WHERE Serial=" + (drH["Serial"] )));
            SkipVoucher:


        ExitSub:
        c.Close();
        Cursor.Current = Cursors.Default;
        Interaction.MsgBox("Done");
    }
    catch (Exception){}
*/

}


