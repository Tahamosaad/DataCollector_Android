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
    // Declaring Server ip, username, database name and password
    private String ip,db,DBUserNameStr,DBPasswordStr;
   // public static DataConnection dbc = new DataConnection();

    //this is connection helper constructor with two parameter server name and db name
    // note :must add db name and password for more security
    ConnectionHelper(String dbname, String dbserver) {
        this.ip =dbserver;//dbc.setServerName(dbserver);
        this.db =dbname; //dbc.setDBName(dbname);
        //region user option to make db username and server static
        //for more secure it must taken from user as db name and server name
        this.DBUserNameStr = "sa";//dbc.setDB_Username("sa");
       this.DBPasswordStr = "MSSTSandURV1";//dbc.setServerPWD("MSSTSandURV1");
        //endregion
    }

    @SuppressLint("NewApi")
    Connection connectionclasss()
    {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        java.sql.Connection connection = null;
        String ConnectionURL ;
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
