package com.saudisoft.mis_android.ExternalDatabase;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Tahamosaad on 5/21/2017.
 */

public class ConnectionHelper {

    String ip,db,DBUserNameStr,DBPasswordStr;


    public ConnectionHelper(String dbname,String dbserver) {
        this.ip =dbserver;
        this.db =dbname;
    }

    public String getIp() {
        return ip;
        }

    public void setIp(String ip) {
            this.ip = ip;
        }


    @SuppressLint("NewApi")
    public Connection connectionclasss( )
    {


        // Declaring Server ip, username, database name and password
//        ip ="41.131.19.20";
//        ip = "172.16.0.48";
//        ip = "10.0.2.2";
//        ip ="41.131.19.22";
//        List<Settings> settings = DB.getAllSettings1();
//
//        for (Settings cn : settings) {
//           db = cn.getDatabaseName();
//
//        }
//        db = "JEDMISDB";
        DBUserNameStr = "sa";
        DBPasswordStr = "MSSTSandURV1";
        // Declaring Server ip, username, database name and password


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        java.sql.Connection connection = null;
        String ConnectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip +";databaseName="+ db + ";user=" + DBUserNameStr+ ";password=" + DBPasswordStr + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }


}
