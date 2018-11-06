package com.saudisoft.mis_android.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.saudisoft.mis_android.DAO.InvTransTypes_DAO;
import com.saudisoft.mis_android.DAO.Setting_DAO;
import com.saudisoft.mis_android.ExternalDatabase.CRUD_Operations;
import com.saudisoft.mis_android.Model.InvTransTypes;
import com.saudisoft.mis_android.Model.Settings;
import com.saudisoft.mis_android.R;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button DataEntry_btn,SendData_btn,ReadVoucher_btn,Exit_btn;
    private InvTransTypes_DAO db;
    private Setting_DAO Setting_DAO;
    List<Settings> setting;
    CRUD_Operations new_data;
    private  String DBNAME,DBserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        ChkInvTransData();
        if (!CheckConnectionSettings()){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                //set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
                //set title
                .setTitle("System Setting Connection")
                //set message
                .setMessage("Connection Data Are'nt Set Press 'yes' to Initialize it ")
                //set positive button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                         startActivity(intent);
                        finish();
                    }
                })
                //set negative button
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                        finish();
//                        Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                    }
                }).show();
        }
    }

    private void initViews()
    {
        this.db = new InvTransTypes_DAO(this);
        this.Setting_DAO = new Setting_DAO(this);
        DataEntry_btn=findViewById(R.id.button_data_entry);
        SendData_btn=findViewById(R.id.button_send_data);
        ReadVoucher_btn=findViewById(R.id.button_read_voucher);
        Exit_btn=findViewById(R.id.button_exit);
        DataEntry_btn.setOnClickListener(this);
        SendData_btn.setOnClickListener(this);
        ReadVoucher_btn.setOnClickListener(this);
        Exit_btn.setOnClickListener(this);


        this.setting = Setting_DAO.getAllSettings1();
        for (Settings cn : setting) {
            DBNAME= cn.getDatabaseName();
        DBserver= cn.getServerName();}
        this. new_data = new CRUD_Operations(DBNAME,DBserver);

    }
    public boolean CheckConnectionSettings(){
//        if(setting.size()<1)
//            return false;
//        else return true;
        return !(setting.size()<1);

//        for (Settings cn : setting) {
//          cn.getServerName().toString();}
    }
    // check if invtrans type table is empty or not
    private void ChkInvTransData(){

//         Reading all Trans
//        Log.d("Reading: ", "Reading all Trans..");
        List<InvTransTypes> Trans = db.getAllInvTransType();


        if (Trans.size()<=0 ){
            DataEntry_btn.setEnabled(false);
            SendData_btn.setEnabled(false);
            ReadVoucher_btn.setEnabled(false);}

        else{
            //data not initialized yet
            DataEntry_btn.setEnabled(true);
            SendData_btn.setEnabled(true);
            ReadVoucher_btn.setEnabled(true);}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
       if (id == R.id.menu_item_settings) {
           if (isNetworkAvailable())
           { startActivity(new Intent(this, SettingActivity.class));
            return true;}
           else
               Toast.makeText(this,"Please Check your connection", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.menu_init_db) {
            if (isNetworkAvailable())
            {startActivity(new Intent(this, InitializeDataBaseActivity.class));
            return true;}
            else
                Toast.makeText(this,"Please Check your connection", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean Getdata(){
        List<String> MyData = new_data.SelectItems();

        return (MyData.size()>0) ;
    }

    public void onPause(){
        super.onPause();

    }
    @Override
    public void onResume(){
        super.onResume();


    }
    @Override
    public void onStart(){
        super.onStart();
        ChkInvTransData();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
    @Override
    public void onClick(View v) {
        if(v==DataEntry_btn)
            startActivity(new Intent(this, Voucher_DataEntryActivity.class));
        if(v==SendData_btn)
        if (isNetworkAvailable())
            startActivity(new Intent(this, SendDataToMIS.class));
        else
            Toast.makeText(this,"Please Check your connection", Toast.LENGTH_SHORT).show();
        if(v==ReadVoucher_btn)
            if (isNetworkAvailable())
            startActivity(new Intent(this, ReadVoucher_Activity.class));
        else
            Toast.makeText(this,"Please Check your connection", Toast.LENGTH_SHORT).show();

        if (v== Exit_btn)
            this.finish();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
