package com.saudisoft.mis_android.ExternalDatabase;

import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taha.mosaad on 02/04/2018.
 */

public class CRUD_Operations {

    Connection connect;
    String ConnectionResult = "";
    public Boolean isSuccess = false;
    Statement stmt = null;
    public String exString =null;
    ConnectionHelper conStr;

    public CRUD_Operations(String dbname ,String dbserver) {
       this.conStr=new ConnectionHelper(dbname,dbserver);
    }


    public List<String> SelectTrans() {

//        String data = "N/A";
        List<String> datanum = new ArrayList<String>();
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
                Statement stmt = connect.createStatement();
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
            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
            if (connect != null) try { connect.close(); } catch(Exception e) {}
        }

        return datanum;
    }
    public List<String> SelectItems() {

//        String data = "N/A";
        List<String> datanum = new ArrayList<String>();
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
                Statement stmt = connect.createStatement();
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
            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
            if (connect != null) try { connect.close(); } catch(Exception e) {}
        }

        return datanum;
    }
    public List<String> SelectVoucher(String Branchcode,String Startdate) {


            List<String> datanum = new ArrayList<String>();

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
                Statement stmt = connect.createStatement();
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
            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
            if (connect != null) try { connect.close(); } catch(Exception e) {}
        }

        return datanum;
    }
    public ArrayList<String> SelectedVoucherHdr(String Serials) {


        ArrayList<String> datanum = new ArrayList<String>();

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
                Statement stmt = connect.createStatement();
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
            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
            if (connect != null) try { connect.close(); } catch(Exception e) {}
        }

        return datanum;
    }
    public ArrayList<String> SelectedVoucherDtl(String Serials) {


        ArrayList<String> datanum = new ArrayList<String>();

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
                Statement stmt = connect.createStatement();
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
            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
            if (connect != null) try { connect.close(); } catch(Exception e) {}
        }

        return datanum;
    }
    public ArrayList<String> SelectedVoucherSerial(String Serials) {


        ArrayList<String> datanum = new ArrayList<String>();

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
                Statement stmt = connect.createStatement();
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
            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
            if (connect != null) try { connect.close(); } catch(Exception e) {}
        }

        return datanum;
    }
    public List<String> SelectUsers(String user_name,String password) {

//        String data = "N/A";
        List<String> datanum = new ArrayList<String>();
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
                String query = "SELECT Users.UserName ,Users.Password  FROM Users INNER JOIN UserRights ON Users.UserName=UserRights.UserName \n" +
                        "WHERE Users.UserName='"+user_name+"' AND Password='"+password+"' AND ObjectID='1' AND CanRun=1";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()){
                    datanum.add(rs.getString("UserName")) ;
                    datanum.add(rs.getString("Password"));
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
            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
            if (connect != null) try { connect.close(); } catch(Exception e) {}
        }

        return datanum;
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
