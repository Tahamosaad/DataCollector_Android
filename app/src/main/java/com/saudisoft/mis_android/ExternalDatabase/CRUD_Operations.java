package com.saudisoft.mis_android.ExternalDatabase;

import android.content.Context;
import android.database.SQLException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.saudisoft.mis_android.DAO.ItemInOutH_DAO;
import com.saudisoft.mis_android.DAO.ItemsInOutL_DAO;
import com.saudisoft.mis_android.DAO.ItemsSerials_DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.text.TextUtils.indexOf;


public class CRUD_Operations {

    private Boolean isSuccess = false;
    //    Statement stmt = null;
    public String exString = null;
    public String Sql_MSG;
    private Connection connect;
    private ConnectionHelper conStr;
    private boolean Found;
    private int num = 0;
    private Map<String,String> stringMap ;
    private List<String> datanum ;
    private Context context;
    private TextView mMessage;
    private String ConnectionResult = "";
    List<Map<String, String>> all_serial;
//    public static DataConnection dbc = new DataConnection();
    public CRUD_Operations(String dbname, String dbserver) {
        this.conStr = new ConnectionHelper(dbname, dbserver);
    }
    public CRUD_Operations(){

    }
//    region getconnection
        public java.sql.Connection getconnection (){

        try {
            connect = conStr.connectionclasss();        // Connect to database
            if (connect == null)
            ConnectionResult = "Check Your Internet Access!";
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            Log.e("Error", ex.toString());
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }

             return connect;
    }
    //endregion
    //region
//    public void SelectUsers2(  ) throws SQLException {
//
//
//        java.sql.Connection con = getconnection();
//            Statement stmt = null;
//            String query = "SELECT Users.UserName ,Users.Password  FROM Users INNER JOIN UserRights ON Users.UserName=UserRights.UserName \n" +
//                    "WHERE Users.UserName='"+"admin"+"' AND Password='"+Encrypt("Mtaha@120")+"' AND ObjectID='310' AND CanRun=1";
//            try {
//                stmt = con.createStatement();
//                ResultSet rs = stmt.executeQuery(query);
//                while (rs.next()) {
//
//                    String coffeeName = rs.getString("UserName");
//                    String supplierID = rs.getString("Password");
//
//                    System.out.println(coffeeName + "\t" + supplierID );
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();            }
//            finally {
//                if (stmt != null) {
//                    stmt.close();
//                }
//            }
//
//    }
    //endregion
    public List<String> SelectTrans() {
        Statement stmt = null;
        datanum = new ArrayList<>();
//        String data = "N/A";
        try {
            connect = getconnection ();        // Connect to database
            if (connect != null) {
                // Change below query according to your own database.
                //
                String query = "select * from InvTransTypes order by TransCode";
                stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    datanum.add(rs.getString("TransCode"));
                    datanum.add(rs.getString("TransName"));
                    datanum.add(rs.getString("TransType"));
                    datanum.add(rs.getString("System"));
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
            } catch (Exception e) {
                e.getMessage();
            }
            if (connect != null) try {
                connect.close();
            } catch (Exception e) {
                e.getMessage();
            }
        }

        return datanum;
    }

    public List<String> SelectItems() {
        Statement stmt = null;
//        String data = "N/A";
      datanum = new ArrayList<>();
        try {
            connect = getconnection ();        // Connect to database
            if (connect != null)  {
                // Change below query according to your own database.
                //
                String query = "select ItemCode,ItemName,PartNumber from ItemsDirectory where ItemType =0 and Active=1 and Serialized=1 order by ItemCode ";
                stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    datanum.add(rs.getString("ItemCode"));
                    datanum.add(rs.getString("ItemName"));
                    datanum.add(rs.getString("PartNumber"));
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
            } catch (Exception e) {
                e.getMessage();
            }
            if (connect != null) try {
                connect.close();
            } catch (Exception e) {
                e.getMessage();
            }
        }

        return datanum;
    }

    public List<Map<String,String>> SelectVoucher(String Branchcode, String Startdate) {

        Statement stmt = null;
        List<Map<String, String>> data =new ArrayList<>();

        try {
            connect = conStr.connectionclasss();        // Connect to database
            if (connect == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                // Change below query according to your own database.
                String query = "SELECT TOP (100) Serial, TransNum, TransCode, TransDate,Notes FROM dbo.ItemsInOutH WHERE (IsPosted = 0) AND (BranchCode = '" + Branchcode + "') AND (TransDate >= '" + Startdate + "') AND (Serial IN (SELECT ItemsInOutH_1.Serial FROM dbo.ItemsInOutH AS ItemsInOutH_1 INNER JOIN dbo.ItemsInOutL ON ItemsInOutH_1.Serial = dbo.ItemsInOutL.Serial INNER JOIN dbo.ItemsDirectory ON dbo.ItemsInOutL.ItemCode = dbo.ItemsDirectory.ItemCode WHERE (ItemsInOutH_1.IsPosted = 0) AND (ItemsInOutH_1.BranchCode = '" + Branchcode + "') AND (ItemsInOutH_1.TransDate >= '" + Startdate + "') GROUP BY ItemsInOutH_1.Serial HAVING (SUM(dbo.ItemsDirectory.Serialized) > 0))) ORDER BY TransDate";
//                String query ="SELECT top 100 ItemsInOutH.Serial,TransNum,TransCode,TransDate FROM ItemsInOutH INNER JOIN ItemsInOutL ON ItemsInOutH.Serial = ItemsInOutL.Serial INNER JOIN ItemsDirectory ON ItemsInOutL.ItemCode = ItemsDirectory.ItemCode WHERE (Serialized = 1) and IsPosted=0 AND BranchCode='"+Branchcode+"' AND TransDate>='"+Startdate+"'";
                stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    stringMap = new HashMap<>();
                    stringMap.put("Vserial",rs.getString("Serial"));
//                    datanum.add(rs.getString("TransType"));
                    if (rs.getString("TransNum") == null) {
                        stringMap.put("TransNum","null");
                    } else {
                        stringMap.put("TransNum",rs.getString("TransNum"));
                    }
                    stringMap.put("TransCode",rs.getString("TransCode"));
                    java.sql.Date sqlDate = rs.getDate("TransDate");
                    stringMap.put("TransDate",sqlDate.toString());
                    if (rs.getString("Notes") == null) {
                        stringMap.put("Notes","null");
                    } else {
                        stringMap.put("Notes",rs.getString("Notes"));
                    }
                    data.add(stringMap);
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
            } catch (Exception e) {
                e.getMessage();
            }
            if (connect != null) try {
                connect.close();
            } catch (Exception e) {
                e.getMessage();
            }
        }

        return data;
    }

    public List<Map<String,String>> SelectedVoucherHdr(String Serials) {

        Statement stmt = null;

        List<Map<String, String>> data =new ArrayList<>();

        try {
            connect = conStr.connectionclasss();        // Connect to database
            if (connect == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                // Change below query according to your own database.
                String query = "SELECT Serial,TransNum,TransCode,TransDate,Notes FROM ItemsInOutH WHERE Serial IN (" + Serials + ")";
                stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    stringMap = new HashMap<>();
                    stringMap.put("Vserial",rs.getString("Serial"));
                    if (rs.getString("TransNum") == null) {
                        stringMap.put("TransNum","null");
                    } else {
                        stringMap.put("TransNum",rs.getString("TransNum"));
                    }
//                    if(rs.wasNull()){datanum.add("null");}
                    stringMap.put("TransCode",rs.getString("TransCode"));
//                    rs.getDate("TransDate").toString();
                    stringMap.put("TransDate",rs.getDate("TransDate").toString());
//                    datanum.add(rs.getString("TransType"));
                    if (rs.getString("Notes") == null) {
                        stringMap.put("Notes","null");
                    } else {
                        stringMap.put("Notes",rs.getString("Notes"));
                    }
//                    if(rs.wasNull()){datanum.add("null");}
                    data.add(stringMap);
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
            } catch (Exception e) {
                e.getMessage();
            }
            if (connect != null) try {
                connect.close();
            } catch (Exception e) {
                e.getMessage();
            }
        }

        return data;
    }

    public ArrayList<String> SelectedVoucherDtl(String Serials) {
        Statement stmt = null;

        ArrayList<String> datanum = new ArrayList<>();

        try {
            connect = conStr.connectionclasss();        // Connect to database
            if (connect == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {

                // Change below query according to your own database.
                String query = "SELECT  ID, ItemName, PartNumber,ItemsInOutL.ItemCode,Serial, Qty FROM ItemsInOutL INNER JOIN ItemsDirectory ON ItemsInOutL.ItemCode=ItemsDirectory.ItemCode WHERE ItemType=0 AND Serialized=1  AND Active=1 AND Serial IN (" + Serials + ")";
                stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {

                    datanum.add(rs.getString("ID"));
                    datanum.add(rs.getString("ItemName"));
                    datanum.add(rs.getString("PartNumber"));
                    if (rs.getString("Qty") == null) {
                        datanum.add("null");
                    } else {
                        datanum.add(rs.getString("Qty"));
                    }
                    datanum.add(rs.getString("ItemCode"));
                    datanum.add(rs.getString("Serial"));
//                    if(rs.wasNull()){datanum.add("null");}
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
            } catch (Exception e) {
                e.getMessage();
            }
            if (connect != null) try {
                connect.close();
            } catch (Exception e) {
                e.getMessage();
            }
        }

        return datanum;
    }

    public ArrayList<String> SelectedVoucherSerial(String Serials) {

        Statement stmt = null;

        ArrayList<String> datanum = new ArrayList<>();

        try {
            connect = conStr.connectionclasss();        // Connect to database
            if (connect == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                // Change below query according to your own database.
                String query = "SELECT Serial, SerialNum,ID FROM ItemSerials WHERE Serial IN (" + Serials + ")";
                stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    datanum.add(rs.getString("Serial"));
                    datanum.add(rs.getString("SerialNum"));
                    datanum.add(rs.getString("ID"));
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
            } catch (Exception e) {
                e.getMessage();
            }
            if (connect != null) try {
                connect.close();
            } catch (Exception e) {
                e.getMessage();
            }
        }

        return datanum;
    }

    public List<String> SelectUsers(String user_name, String password) {
        Statement stmt = null;
//        String data = "N/A";
       datanum = new ArrayList<>();
//        Connection connect = null;
        try {

            connect = getconnection();        // Connect to database
            if (connect != null)  {
                // Change below query according to your own database.
                //
                String query = "SELECT Users.UserName ,Users.Password  FROM Users INNER JOIN UserRights ON Users.UserName=UserRights.UserName \n" +
                        "WHERE Users.UserName='" + user_name + "' AND Password='" + Encrypt(password) + "' AND ObjectID='310' AND CanRun=1";

                stmt = connect.createStatement();

                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    datanum.add(rs.getString("UserName"));
                    datanum.add(rs.getString("Password"));
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
            } catch (Exception e) {
                e.getMessage();
            }
            if (connect != null) try {
                connect.close();
            } catch (Exception e) {
                e.getMessage();
            }
        }

        return datanum;
    }

    private String Encrypt(String MyText) {
        String Encrypt = "";
        for (int i = 1; (i <= MyText.length()); i++) {
            int ascii = MyText.charAt(i - 1);
            Encrypt = Encrypt + Character.toString((char) ((ascii - 29 + ((i % 2) == 0 ? 1 : -2))));
            //   Encrypt = Encrypt & Chr(Asc(Mid(MyText, i, 1)) - 29 + IIf(Int(i / 2) = i / 2, 1, -2))
        }
        return Encrypt;
    }

    public final String Decrypt(String MyText) {
        String Decrypt = "";
        for (int i = 1; (i <= MyText.length()); i++) {
            int ascii = MyText.charAt(i - 1);
            Decrypt = Decrypt + Character.toString((char) ((ascii + 29 + ((i % 2) == 0 ? 1 : -2))));
//       Decrypt = Decrypt & Chr(Asc(Mid(MyText, i, 1)) + 29 + IIf(Int(i / 2) = i / 2, -1, 2))
        }
        return Decrypt;
    }

    public final String GetServerDate() {
        Statement stmt = null;

        String serverdate = "";
        try {

            connect = getconnection();        // Connect to database
            if (connect != null)
            {
                // Change below query according to your own database.
                //
                String query = "SELECT CONVERT(Date, GETDATE()) as serverdate";

                stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
//                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd ");
//                    Date mydate=new SimpleDateFormat("dd-MM-yyyy").parse(rs.getString("serverdate"));
                    serverdate = rs.getString("serverdate");

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
            } catch (Exception e) {
                e.getMessage();
            }
            if (connect != null) try {
                connect.close();
            } catch (Exception e) {
                e.getMessage();
            }
        }

        return serverdate;


    }

    private List<Map<String, String>> getItemSerial(String mid, Context con) {
        List<Map<String, String>> saved_serials;

        ItemsSerials_DAO ItemSerial_DAO = new ItemsSerials_DAO(con);
        saved_serials = ItemSerial_DAO.getItemSerials(mid);
//        if (saved_serials.size() > 0)  all_serial.add(saved_serials.get(0));
//
            return saved_serials;
//        else return null;

    }

    private List<Map<String, String>> getItemDetails(String serial, Context con) {
        List<Map<String, String>> select_details;
        ItemsInOutL_DAO ItemDetail_DAO = new ItemsInOutL_DAO(con);
        select_details = ItemDetail_DAO.getItemDetails(serial);
//        if (select_details.size() > 0)
            return select_details;
//        else return null;
    }

    @Nullable
    private List<Map<String, String>> getItemHeader(String serial, Context con) {
        List<Map<String, String>> headerlist;
        ItemInOutH_DAO ItemHeader_DAO = new ItemInOutH_DAO(con);
        headerlist = ItemHeader_DAO.getItemHeader(serial);
        if (headerlist.size() > 0)
            return headerlist;
        else return null;
    }

    boolean InsertNewSerials(Map<String, String> hdr, String Branchcode, String MISUser, Context context) {
        Statement stmt = null;
        String Current_serial="", Current_itemcode ="";
        List<Map<String, String>> new_serial ;
        List<Map<String, String>> select_details;
        boolean isSaved = false;
        Connection connect3 = null;
        try {
            connect3 = conStr.connectionclasss();        // Connect to database
            if (connect3 == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                stmt = connect3.createStatement();
                connect3.setAutoCommit(false);
                select_details = getItemDetails(hdr.get("item_serial"), context);
                if (select_details .size()>0 ) {
                    for (Map<String, String> dtl : select_details)
                    {
                        new_serial = getItemSerial(dtl.get("ID"), context);
                        String DocNum = hdr.get("item_serial").substring(1 + indexOf(hdr.get("item_serial"), (hdr.get("trans_type").equals("1")) ? "r" : "i"));
                        if (new_serial.size()>0) {
                            for (Map<String, String> srl : new_serial)
                                Found = ((hdr.get("trans_type").equals("1")) || CheckSerialIn(srl.get("SerialNum"), dtl.get("ItemCode"), GetStore(Branchcode)));
                        } else continue;

                        for (Map<String, String> srl : new_serial) {
                            String SQL = "INSERT INTO ItemSerials(ID,Serial,TransCode,TransNum,TransDate,DocNum,ItemCode,SerialNum,IssuedBy) VALUES(";
                            //  if drH("IsNew"){
                            //  SQL = SQL & Serial & "_VCH_" & drD("ItemCode") & "_1','"
//                                  SQL = SQL+ (mID+ ",");}
                            //  else{
                            SQL = SQL + "'" + dtl.get("ID") + "'" + ",";
                            SQL = SQL + "'" + dtl.get("Serial") + "'" + "," + "'" + hdr.get("TransCode") + "'" + "," + hdr.get("trans_num") + "," + "'" + hdr.get("trans_date") + "'" + ",";
                            SQL = SQL + DocNum + "," + "'" + dtl.get("ItemCode") + "'" + "," + "'" + srl.get("SerialNum") + "'" + "," + "'" + MISUser + "'" + ")";
                            stmt.addBatch(SQL);
                            num = num + 1;
                            Current_serial=new_serial.get(0).get("SerialNum");
                            Current_itemcode =select_details.get(0).get("ItemCode");
                        }

                    }
                    if (num<1)//case no serial number founded
                    {
                        isSaved=true;
                        return true;
                    }
                    try {
                        if (!Found || stmt.executeBatch().length <0) //case not founded and
                        {
                            Sql_MSG = "Can not add Serial Numbers " + Current_serial + " in Item " + Current_itemcode + " In Voucher " + select_details.get(0).get("Serial") + (!Found ? ", Serial number not found." : ".");
                            isSaved = false;
                            stmt.clearBatch();
                            connect3.rollback();
                            return false;
                        } else {
                            connect3.commit();
                            isSaved = true;
                        }
                    } catch (java.sql.SQLException ex) {
                        isSuccess = false;
                        if (ex.getSQLState().startsWith("23"))
                            Sql_MSG = "Can not add Serial Numbers " + Current_serial + " in Item " + Current_itemcode+ " In Voucher " + select_details.get(0).get("Serial") + (!Found ? ", Serial number not found." : ".");
                        else Sql_MSG = ex.getMessage();
                        throw ex;
                    }

                }



//                        catch (SQLNonTransientException ex) {
//                            isSuccess = false;
//                            connect3.rollback();
//                            ex.getMessage();
//                            ex.getErrorCode();
//                            Sql_MSG ="Serials you try to insert is already saved";
//
////                     try {System.err.print("Transaction is being rolled back");
////                             connect3.rollback();}
////                     catch(SQLException excep) {excep.getMessage();}
//                        }


            }
        } catch (Exception ex) {
            isSuccess = false;
            isSaved = false;
            ConnectionResult = ex.getMessage();
        } finally {
            if (stmt != null) try {
                stmt.close();
            } catch (Exception e) {
                e.getMessage();
            }
            if (connect3 != null) try {connect3.setAutoCommit(true);
                connect3.close();
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return isSaved;
    }

    private boolean CheckSerialIn(String Serial, String Item, String Store) {
        Statement stmt = null;
        Connection connect3 = null;
        boolean CheckSerialIn = true;
        try {

            connect3 = conStr.connectionclasss();        // Connect to database
            if (connect3 == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                String query = "SELECT Sum(ItemsInOutH.TransType) as tsum FROM ItemSerials INNER JOIN InvTransTypes ON ItemSerials.TransCode = InvTransTypes.TransCode " +
                        "INNER JOIN ItemsInOutH ON ItemSerials.Serial = ItemsInOutH.Serial " +
                        "WHERE (SerialNum = '" + Serial + "') AND (StoreCode = '" + Store + "') AND (ItemSerials.ItemCode = '" + Item + "')";

                stmt = connect3.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    if (rs.getString("tsum") == null)
                        CheckSerialIn = false;
                }
                ConnectionResult = "successful";
                isSuccess = true;
                connect3.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Error", ex.toString());
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        } finally {
            if (stmt != null) try {
                stmt.close();
            } catch (Exception e) {e.getMessage();
            }
            if (connect3 != null) try {
                connect3.close();
            } catch (Exception e) {e.getMessage();
            }
        }

        return CheckSerialIn;


    }

    private java.sql.Date GetPostDate(String BranchCode) {
        Statement stmt = null;

        java.sql.Date PostDate = (java.sql.Date) Calendar.getInstance().getTime();
        try {

            connect = conStr.connectionclasss();        // Connect to database
            if (connect == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                // Change below query according to your own database.
                //
                String query = "SELECT PostDate FROM Posting WHERE TransCode='STK' AND BranchCode=" + BranchCode;

                stmt = connect.createStatement();
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
            } catch (Exception e) {
                e.getMessage();
            }
            if (connect != null) try {
                connect.close();
            } catch (Exception e) {
                e.getMessage();
            }
        }

        return PostDate;


    }

    public String GetStore(String BranchCode) {
        Statement stmt = null;
        String storecode = "";
        Connection connect3 = null;
        try {

            connect3 = conStr.connectionclasss();        // Connect to database
            if (connect3 == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                // Change below query according to your own database.
                //
                String query = "SELECT DefStoreCode FROM BranchCodes WHERE BranchCode= '" + BranchCode + "'";

                stmt = connect3.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
//                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd ");
//                    Date mydate=new SimpleDateFormat("dd-MM-yyyy").parse(rs.getString("serverdate"));
                    storecode = rs.getString("DefStoreCode");

                }
                ConnectionResult = "successful";
                isSuccess = true;
                connect3.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Error", ex.toString());
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        } finally {
            if (stmt != null) try {
                stmt.close();
            } catch (Exception e) {
                e.getMessage();
            }
            if (connect3 != null) try {
                connect3.close();
            } catch (Exception e) {
                e.getMessage();
            }
        }

        return storecode;


    }
    //region insert delete update
//    public void Insertion(String P1, String P2 , String P3){
//
//        try
//        {
//            ConnectionHelper conStr=new ConnectionHelper();
//            connect =conStr.connectionclasss();        // Connect to database
//            if (connect == null)
//            {
//                ConnectionResult = "Check Your Internet Access!";
//            }
//            else {
//                try{
//                    String SQL = "INSERT INTO Cities (CityCode, CityName, IssuedBy) VALUES ("+P1+",'"+P2+"','"+P3+"');";//('"+P1+"','"+P2+"','"+P3+"')
//                    stmt = connect.createStatement();
//                    stmt.executeUpdate(SQL);
////                    Log.e("Success", "Success");
//                    isSuccess=true;
//
//                }
//
//                // Handle any errors that may have occurred.
//                catch (Exception exception) {
//                    exception.printStackTrace();
//                    Log.e("Error", exception.toString());
//
//                    exString=exception.toString();
//
//                }
//                finally {
//                    if (stmt != null) try { stmt.close(); } catch(Exception e) {}
//                    if (connect != null) try { connect.close(); } catch(Exception e) {}
//                }
//
//            }
//        }
//        catch (Exception ex)
//        {
//            isSuccess = false;
//            ConnectionResult = ex.getMessage();
//        }
//    }
//    public void Update(String P1, String P2 , String P3){
//
//        try
//        {
//            ConnectionHelper conStr=new ConnectionHelper();
//            connect =conStr.connectionclasss();        // Connect to database
//            if (connect == null)
//            {
//                ConnectionResult = "Check Your Internet Access!";
//            }
//            else {
//                try{
//                    String SQL = "UPDATE Cities SET CityCode = "+P1+" ,CityName = '"+P2+"' ,IssuedBy = '"+P3+"' WHERE CityCode = "+P1+" ";//('"+P1+"','"+P2+"','"+P3+"')
//                    stmt = connect.createStatement();
//                    stmt.executeUpdate(SQL);
////                    Log.e("Success", "Success");
//                    isSuccess=true;
//
//                }
//
//                // Handle any errors that may have occurred.
//                catch (Exception e) {
//                    e.printStackTrace();
//                    Log.e("Error", e.toString());
//                    exString=e.toString();
//                }
//                finally {
//                    if (stmt != null) try { stmt.close(); } catch(Exception e) {}
//                    if (connect != null) try { connect.close(); } catch(Exception e) {}
//                }
//
//            }
//        }
//        catch (Exception ex)
//        {
//            isSuccess = false;
//            ConnectionResult = ex.getMessage();
//            exString=ex.toString();
//        }
//    }
//    public void Delete(String P1, String P2 , String P3){
//
//        try
//        {
//            ConnectionHelper conStr=new ConnectionHelper();
//            connect =conStr.connectionclasss();        // Connect to database
//            if (connect == null)
//            {
//                ConnectionResult = "Check Your Internet Access!";
//            }
//            else {
//                try{
//                    String SQL = "DELETE FROM Cities WHERE CityCode = "+P1+" ";//('"+P1+"','"+P2+"','"+P3+"')
//                    stmt = connect.createStatement();
//                    stmt.executeUpdate(SQL);
////                    Log.e("Success", "Success");
//                    isSuccess=true;
//
//                }
//
//                // Handle any errors that may have occurred.
//                catch (Exception e) {
//                    e.printStackTrace();
//                    Log.e("Error", e.toString());
//                    exString=e.toString();
//                }
//                finally {
//                    if (stmt != null) try { stmt.close(); } catch(Exception e) {}
//                    if (connect != null) try { connect.close(); } catch(Exception e) {}
//
//                }
//
//            }
//        }
//        catch (Exception ex)
//        {
//            ConnectionResult = ex.getMessage();
//            exString=ex.toString();
//        }
//    }
//endregion
    //region select and return with hash map
    //    public List<Map<String,String>> Select() {
//
//        List<Map<String, String>> data = null;
//        data = new ArrayList<Map<String, String>>();
//        try
//        {
//            ConnectionHelper conStr=new ConnectionHelper();
//            connect =conStr.connectionclasss();        // Connect to database
//            if (connect == null)
//            {
//                ConnectionResult = "Check Your Internet Access!";
//            }
//            else
//            {
//                // Change below query according to your own database.
//                String query = "select * from InvTransTypes";
//                Statement stmt = connect.createStatement();
//                ResultSet rs = stmt.executeQuery(query);
//                while (rs.next()){
//                    Map<String,String> datanum=new HashMap<String,String>();
//                    datanum.put("TransCode",rs.getString("TransCode"));
//                    datanum.put("TransName",rs.getString("TransName"));
//                    datanum.put("transType",rs.getString("TransType"));
//                    datanum.put("TransSystem",rs.getString("System"));
//                    data.add(datanum);
//                }
//
//
//                ConnectionResult = "successful";
//                isSuccess=true;
//                connect.close();
//            }
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//            Log.e("Error", ex.toString());
//            isSuccess = false;
//            ConnectionResult = ex.getMessage();
//        }
//        finally {
//            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
//            if (connect != null) try { connect.close(); } catch(Exception e) {}
//        }
//
//        return data;
//    }
    //endregion

    private void Batchinserted(String SQL, Connection conn, Map<String, String> hdr, Map<String, String> new_serial) {
//        List<Map<String ,String >>select_details=null;
//
//// Create PrepareStatement object
//        PreparedStatement pstmt = null;
//        try {
//            pstmt = conn.prepareStatement(SQL);
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//        }
//
////Set auto-commit to false
//        try {
//            conn.setAutoCommit(false);
//            select_details = getItemDetails(hdr.get("item_serial"), context);
//            if (select_details != null) {
//                for (Map<String, String> dtl : select_details) {
//            for (Map<String, String> srl : new_serial) {
//                 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                 java.sql.Date trans_date = new Date(formatter.parse(hdr.get("trans_date")).getTime());
//                 pstmt = connect3.prepareStatement(SQL);
//                SQL = "INSERT INTO ItemSerials(ID,Serial,TransCode,TransNum,TransDate,DocNum,ItemCode,SerialNum,IssuedBy) VALUES(?,?,?,?,?,?,?,?,?)";
//                 pstmt.setString(1, dtl.get("ID")  );pstmt.setString(2,  dtl.get("Serial") );
//                 pstmt.setString(3,  hdr.get("TransCode") ); pstmt.setString(4,hdr.get("trans_num"));  pstmt.setDate(5,trans_date );
//                 pstmt.setInt(6,Integer.valueOf( DocNum));  pstmt.setString(7,  dtl.get("ItemCode")  );  pstmt.setString(8,new_serial.get(0).get("SerialNum"));  pstmt.setString(9, MISUser );
//
//// Add it to the batch
//                pstmt.addBatch();
//            }
//                }
//
//
//
////Create an int[] to hold returned values
//        int[] count = pstmt.executeBatch();
//
////Explicitly commit statements to apply changes
//        conn.commit();
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//        }
//
    }

    public boolean Insert(String SQL) throws Exception {

        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = conStr.connectionclasss();        // Connect to database
            if (con == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                con.setAutoCommit(false);
                pstmt = con.prepareStatement(SQL);
                pstmt.executeUpdate();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Error", ex.toString());
            isSuccess = false;
            Sql_MSG = ex.getMessage();
            if (con != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    con.rollback();
                } catch (SQLException excep) {
                    excep.getMessage();
                }
            }

        } finally {
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
                e.getMessage();
            }
            if (con != null) try {
                con.close();
            } catch (Exception e) {
                e.getMessage();
            }
            if (con != null) {
                con.setAutoCommit(true);
            }
        }

        return isSuccess;
    }

}
