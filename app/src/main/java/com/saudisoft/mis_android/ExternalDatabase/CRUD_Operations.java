package com.saudisoft.mis_android.ExternalDatabase;

import android.database.SQLException;
import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.text.TextUtils.indexOf;

//import static android.icu.text.MessagePattern.ArgType.SELECT;

/**
 * Created by Taha mosaad on {6/10/2018}.
 */

public class CRUD_Operations {

    private Connection connect;
    private String ConnectionResult = "";
    public Boolean isSuccess = false;
//    Statement stmt = null;
    public String exString =null;
    public String Sql_MSG;
    private ConnectionHelper conStr;
    private boolean Found;
    public CRUD_Operations(String dbname ,String dbserver) {
        this.conStr = new ConnectionHelper(dbname, dbserver);
    }
//    public java.sql.Connection getconnection (){
//
//        try {
//            connect = conStr.connectionclasss();        // Connect to database
//            if (connect == null)
//            ConnectionResult = "Check Your Internet Access!";
//        }
//        catch(Exception ex)
//        {
//            ex.printStackTrace();
//            Log.e("Error", ex.toString());
//            isSuccess = false;
//            ConnectionResult = ex.getMessage();
//        }
//
//             return connect;
//    }
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

    public List<String> SelectTrans() {
        Statement stmt=null;

//        String data = "N/A";
        List<String> datanum = new ArrayList<>();
        try
        {
            connect =conStr.connectionclasss();        // Connect to database
            if (connect == null)
            {
                ConnectionResult = "Check Your Internet Access!";
            }
            else
            {
                // Change below query according to your own database.
                //
                String query = "select * from InvTransTypes order by TransCode";
                 stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()){
                    datanum.add(rs.getString("TransCode"));
                    datanum.add(rs.getString("TransName"));
                    datanum.add(rs.getString("TransType"));
                    datanum.add(rs.getString("System"));
                }
                ConnectionResult = "successful";
                isSuccess=true;
                connect.close();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.e("Error", ex.toString());
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        finally {
            if (stmt != null) try { stmt.close(); } catch(Exception e) {e.getMessage();}
            if (connect != null) try { connect.close(); } catch(Exception e) {e.getMessage();}
        }

        return datanum;
    }
    public List<String> SelectItems() {
        Statement stmt=null;
//        String data = "N/A";
        List<String> datanum = new ArrayList<>();
        try
        {
            connect =conStr.connectionclasss();        // Connect to database
            if (connect == null)
            {
                ConnectionResult = "Check Your Internet Access!";
            }
            else
            {
                // Change below query according to your own database.
                //
                String query = "select ItemCode,ItemName,PartNumber from ItemsDirectory where ItemType =0 and Active=1 and Serialized=1 order by ItemCode ";
                 stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()){
                    datanum.add(rs.getString("ItemCode")) ;
                    datanum.add(rs.getString("ItemName"));
                    datanum.add(rs.getString("PartNumber"));
                }
                ConnectionResult = "successful";
                isSuccess=true;
                connect.close();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.e("Error", ex.toString());
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        finally {
            if (stmt != null) try { stmt.close(); } catch(Exception e) {e.getMessage();}
            if (connect != null) try { connect.close(); } catch(Exception e) {e.getMessage();}
        }

        return datanum;
    }
    public List<String> SelectVoucher(String Branchcode,String Startdate) {

        Statement stmt=null;
            List<String> datanum = new ArrayList<>();

        try
        {
            connect =conStr.connectionclasss();        // Connect to database
            if (connect == null)
            {
                ConnectionResult = "Check Your Internet Access!";
            }
            else
            {
                // Change below query according to your own database.
                String query ="SELECT top 100 Serial,TransNum,TransCode,TransDate FROM ItemsInOutH WHERE IsPosted=0 AND BranchCode='"+Branchcode+"' AND TransDate>='"+Startdate+"' order by TransDate";
//                String query ="SELECT top 100 ItemsInOutH.Serial,TransNum,TransCode,TransDate FROM ItemsInOutH INNER JOIN ItemsInOutL ON ItemsInOutH.Serial = ItemsInOutL.Serial INNER JOIN ItemsDirectory ON ItemsInOutL.ItemCode = ItemsDirectory.ItemCode WHERE (Serialized = 1) and IsPosted=0 AND BranchCode='"+Branchcode+"' AND TransDate>='"+Startdate+"'";
                stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()){

                    datanum.add(rs.getString("Serial"));
//                    datanum.add(rs.getString("TransType"));
                    if(rs.getString("TransNum")== null){datanum.add("null");}
                    else {datanum.add(rs.getString("TransNum"));}
                    datanum.add(rs.getString("TransCode"));
                    datanum.add(rs.getString("TransDate"));
                }


                ConnectionResult = "successful";
                isSuccess=true;
                connect.close();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.e("Error", ex.toString());
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        finally {
            if (stmt != null) try { stmt.close(); } catch(Exception e) {e.getMessage();}
            if (connect != null) try { connect.close(); } catch(Exception e) {e.getMessage();}
        }

        return datanum;
    }
    public ArrayList<String> SelectedVoucherHdr(String Serials) {

        Statement stmt=null;
        ArrayList<String> datanum = new ArrayList<>();

        try
        {
            connect =conStr.connectionclasss();        // Connect to database
            if (connect == null)
            {
                ConnectionResult = "Check Your Internet Access!";
            }
            else
            {
                // Change below query according to your own database.
                String query = "SELECT Serial,TransNum,TransCode,TransDate,Notes FROM ItemsInOutH WHERE Serial IN ("+ Serials +")";
                 stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()){

                    datanum.add(rs.getString("Serial"));
                    if(rs.getString("TransNum")== null){datanum.add("null");}
                    else {datanum.add(rs.getString("TransNum"));}
//                    if(rs.wasNull()){datanum.add("null");}
                    datanum.add(rs.getString("TransCode"));
                    datanum.add(rs.getString("TransDate"));
//                    datanum.add(rs.getString("TransType"));
                    if(rs.getString("Notes")== null){datanum.add("null");}
                    else {datanum.add(rs.getString("Notes"));}
//                    if(rs.wasNull()){datanum.add("null");}
                }


                ConnectionResult = "successful";
                isSuccess=true;
                connect.close();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.e("Error", ex.toString());
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        finally {
            if (stmt != null) try { stmt.close(); } catch(Exception e) {e.getMessage();}
            if (connect != null) try { connect.close(); } catch(Exception e) {e.getMessage();}
        }

        return datanum;
    }
    public ArrayList<String> SelectedVoucherDtl(String Serials) {
        Statement stmt=null;

        ArrayList<String> datanum = new ArrayList<>();

        try
        {
            connect =conStr.connectionclasss();        // Connect to database
            if (connect == null)
            {
                ConnectionResult = "Check Your Internet Access!";
            }
            else
            {

                // Change below query according to your own database.
                String query = "SELECT  ID, ItemName, PartNumber,ItemsInOutL.ItemCode,Serial, Qty FROM ItemsInOutL INNER JOIN ItemsDirectory ON ItemsInOutL.ItemCode=ItemsDirectory.ItemCode WHERE ItemType=0  AND Active=1 AND Serial IN ("+ Serials +")";
                stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()){

                    datanum.add(rs.getString("ID"));
                    datanum.add(rs.getString("ItemName"));
                    datanum.add(rs.getString("PartNumber"));
                    if(rs.getString("Qty")== null){datanum.add("null");}
                    else {datanum.add(rs.getString("Qty"));}
                    datanum.add(rs.getString("ItemCode"));
                    datanum.add(rs.getString("Serial"));
//                    if(rs.wasNull()){datanum.add("null");}
                }


                ConnectionResult = "successful";
                isSuccess=true;
                connect.close();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.e("Error", ex.toString());
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        finally {
            if (stmt != null) try { stmt.close(); } catch(Exception e) {e.getMessage();}
            if (connect != null) try { connect.close(); } catch(Exception e) {e.getMessage();}
        }

        return datanum;
    }
    public ArrayList<String> SelectedVoucherSerial(String Serials) {

        Statement stmt=null;

        ArrayList<String> datanum = new ArrayList<>();

        try
        {
            connect =conStr.connectionclasss();        // Connect to database
            if (connect == null)
            {
                ConnectionResult = "Check Your Internet Access!";
            }
            else
            {
                // Change below query according to your own database.
                String query =  "SELECT Serial, SerialNum,ID FROM ItemSerials WHERE Serial IN ("+ Serials +")";
                 stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()){
                    datanum.add(rs.getString("Serial"));
                    datanum.add(rs.getString("SerialNum"));
                    datanum.add(rs.getString("ID"));
                }
                ConnectionResult = "successful";
                isSuccess=true;
                connect.close();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.e("Error", ex.toString());
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        finally {
            if (stmt != null) try { stmt.close(); } catch(Exception e) {e.getMessage();}
            if (connect != null) try { connect.close(); } catch(Exception e) {e.getMessage();}
        }

        return datanum;
    }
    public List<String> SelectUsers(String user_name,String password) {
        Statement stmt=null;

//        String data = "N/A";
        List<String> datanum = new ArrayList<>();
        Connection  connect2 =null;
        try
        {

              connect2 =conStr.connectionclasss();        // Connect to database
            if (connect2 == null)
            {
                ConnectionResult = "Check Your Internet Access!";
            }
            else
            {
                // Change below query according to your own database.
                //
                String query = "SELECT Users.UserName ,Users.Password  FROM Users INNER JOIN UserRights ON Users.UserName=UserRights.UserName \n" +
                        "WHERE Users.UserName='"+user_name+"' AND Password='"+Encrypt(password)+"' AND ObjectID='310' AND CanRun=1";

                 stmt = connect2.createStatement();

                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()){
                    datanum.add(rs.getString("UserName")) ;
                    datanum.add(rs.getString("Password"));
                }
                ConnectionResult = "successful";
                isSuccess=true;
                connect2.close();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.e("Error", ex.toString());
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        finally {
            if (stmt != null) try { stmt.close(); } catch(Exception e) {e.getMessage();}
            if (connect2 != null) try { connect2.close(); } catch(Exception e) {e.getMessage();}
        }

        return datanum;
    }
    private String Encrypt(String MyText) {
        String Encrypt = "";
        for (int i = 1; (i <= MyText.length()); i++) {
            int ascii =  MyText.charAt(i-1);
            Encrypt = Encrypt +  Character.toString ((char) ((ascii - 29 +((i % 2) == 0 ? 1 : -2))));
            //   Encrypt = Encrypt & Chr(Asc(Mid(MyText, i, 1)) - 29 + IIf(Int(i / 2) = i / 2, 1, -2))
        }     return Encrypt;
    }
    public final String Decrypt(String MyText) {
         String  Decrypt="";
        for ( int i = 1; (i <= MyText.length()); i++) {
            int ascii = MyText.charAt(i-1);
           Decrypt = Decrypt +  Character.toString ((char) ((ascii + 29 +((i % 2) == 0 ? 1 : -2))));
//       Decrypt = Decrypt & Chr(Asc(Mid(MyText, i, 1)) + 29 + IIf(Int(i / 2) = i / 2, -1, 2))
        }
            return Decrypt;
    }
    public final String GetServerDate( ) {
        Statement stmt=null;

        String serverdate ="" ;
        try
        {

            connect =conStr.connectionclasss();        // Connect to database
            if (connect == null)
            {
                ConnectionResult = "Check Your Internet Access!";
            }
            else
            {
                // Change below query according to your own database.
                //
                String query = "SELECT CONVERT(Date, GETDATE()) as serverdate";

                 stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()){
//                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd ");
//                    Date mydate=new SimpleDateFormat("dd-MM-yyyy").parse(rs.getString("serverdate"));
                     serverdate= rs.getString("serverdate") ;

                }
                ConnectionResult = "successful";
                isSuccess=true;
                connect.close();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.e("Error", ex.toString());
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        finally {
            if (stmt != null) try { stmt.close(); } catch(Exception e) {e.getMessage();}
            if (connect != null) try { connect.close(); } catch(Exception e) {e.getMessage();}
        }

        return serverdate;


    }
    public  boolean InsertNewSerials(List<String> new_serial,String mID,String mserial,String mTcode,String mTnum,String mTdate,String TType,String ItemCode,String Branchcode,String MISUser){
        PreparedStatement pstmt = null;
        boolean isSaved=false;
        Connection  connect3 =null;
        try {
            connect3 = conStr.connectionclasss();        // Connect to database
            if (connect3 == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
//                connect3.setAutoCommit(false);
                String DocNum = mserial.substring( 1 + indexOf(mserial, ( (Integer.valueOf(TType) == 1) ? "r" : "i" )));

                for(String srl : new_serial){

                    Found = ((TType.equals("1"))|| CheckSerialIn(srl, ItemCode, GetStore(Branchcode)));

                }
                if (Found){
                    for(String srl : new_serial)
                    {
                    String SQL ="INSERT INTO ItemSerials(ID,Serial,TransCode,TransNum,TransDate,DocNum,ItemCode,SerialNum,IssuedBy) VALUES(";
                    //  if drH("IsNew"){
                    //  SQL = SQL & Serial & "_VCH_" & drD("ItemCode") & "_1','"
//                        SQL = SQL+ (mID+ ",");}
                    //  else{
                    SQL = SQL+"'" +mID +"'" + ",";
                    SQL = SQL+ "'"+mserial +"'"+","+ mTcode + "," + mTnum +"," +"'" +mTdate+ "'"+",";
                    SQL = SQL+ DocNum + ","+"'"+ ItemCode +"'"+ ","+"'" + srl +"'"+ "," +"'" +MISUser+"'" +")";
//                   isSaved= Insert(SQL);
                 try {
                     connect3.setAutoCommit(false);
                     pstmt = connect3.prepareStatement(SQL);
                     pstmt.executeUpdate();
                     connect3.commit();
                     isSaved=true;
                    }
                    catch (Exception ex)
                 {
                     ex.printStackTrace();
                     Log.e("Error", ex.toString());
                     isSuccess = false;
                     Sql_MSG = ex.getMessage();
                     try {System.err.print("Transaction is being rolled back");
                             connect3.rollback();}
                     catch(SQLException excep) {excep.getMessage();}
                 }
                    }

                }
                else{
                    isSaved = false;
                    Sql_MSG= "Can not add Serial Numbers " + new_serial.toString() + " in Item " + ItemCode + " In Voucher " + mserial + ( !Found ? ", Serial number not found." : "." );
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.e("Error", ex.toString());
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        finally {
            if (pstmt != null) try { pstmt.close(); } catch(Exception e) {e.getMessage();}
            if (connect3 != null) try { connect3.close(); } catch(Exception e) {e.getMessage();}
        }
        return isSaved;
    }
    public boolean Insert(String SQL) throws Exception {

        PreparedStatement pstmt = null;
        Connection  con =null;
        try {
            con = conStr.connectionclasss();        // Connect to database
            if (con == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                con.setAutoCommit(false);
                pstmt = con.prepareStatement(SQL);
                pstmt.executeUpdate();

                }
        }

        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.e("Error", ex.toString());
            isSuccess = false;
            Sql_MSG = ex.getMessage();
            if (con != null) {
                try {System.err.print("Transaction is being rolled back");
                    con.rollback();}
                catch(SQLException excep) {excep.getMessage();}
            }

        }finally {
            if (pstmt != null) try { pstmt.close(); } catch(Exception e) {e.getMessage();}
            if (con != null) try { con.close(); } catch(Exception e) {e.getMessage();}
            con.setAutoCommit(true);
        }

        return  isSuccess;
    }
    public  boolean CheckSerialIn(String Serial, String Item, String Store) {
        Statement stmt=null;
        Connection  connect3 =null;
        boolean CheckSerialIn = true;
        try
        {

            connect3 =conStr.connectionclasss();        // Connect to database
            if (connect3 == null)
            {
                ConnectionResult = "Check Your Internet Access!";
            }
            else
            {
                String query = "SELECT Sum(ItemsInOutH.TransType) as tsum FROM ItemSerials INNER JOIN InvTransTypes ON ItemSerials.TransCode = InvTransTypes.TransCode " +
                "INNER JOIN ItemsInOutH ON ItemSerials.Serial = ItemsInOutH.Serial " +
                "WHERE (SerialNum = '" + Serial + "') AND (StoreCode = '" + Store + "') AND (ItemSerials.ItemCode = '" + Item + "')";

                 stmt = connect3.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()){
                    if (rs.getString("tsum") ==null)
                    CheckSerialIn = false;
                }
                ConnectionResult = "successful";
                isSuccess=true;
                connect3.close();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.e("Error", ex.toString());
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        finally {
            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
            if (connect3 != null) try { connect3.close(); } catch(Exception e) {}
        }

        return CheckSerialIn;


    }
    private  java.sql.Date GetPostDate(String BranchCode) {
        Statement stmt=null;

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
            } catch (Exception e) {e.getMessage();}
            if (connect != null) try {
                connect.close();
            } catch (Exception e) {e.getMessage();}
        }

        return PostDate;


    }
    private  String GetStore(String BranchCode) {
        Statement stmt=null;
        String storecode = "";
        Connection  connect3 =null;
        try {

            connect3 = conStr.connectionclasss();        // Connect to database
            if (connect3 == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                // Change below query according to your own database.
                //
                String query = "SELECT DefStoreCode FROM BranchCodes WHERE BranchCode= '" + BranchCode+"'";

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
            } catch (Exception e) {e.getMessage();}
            if (connect3 != null) try {
                connect3.close();
            } catch (Exception e) {e.getMessage();}
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
}
