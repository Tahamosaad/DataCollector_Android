package com.saudisoft.mis_android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.saudisoft.mis_android.DAO.InvTransTypes_DAO;
import com.saudisoft.mis_android.DAO.ItemsDirectory_DAO;
import com.saudisoft.mis_android.DAO.Setting_DAO;
import com.saudisoft.mis_android.ExternalDatabase.CRUD_Operations;
import com.saudisoft.mis_android.Model.InvTransTypes;
import com.saudisoft.mis_android.Model.ItemsDirectory;
import com.saudisoft.mis_android.Model.Settings;
import com.saudisoft.mis_android.R;

import java.util.List;

public class InitializeDataBaseActivity extends Activity implements View.OnClickListener{
    private InvTransTypes_DAO TransType_DAO;
    private ItemsDirectory_DAO ItemDir_DAO;
    private Setting_DAO setting_dao;
    private  String DBNAME,DBserver;
    private Button btn_loginDB;
    private EditText mTxtUsername,mTXTPassword;
    CRUD_Operations new_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize_data_base);
        initViews();

    }
    public boolean Checkuser() {
        String userName = mTxtUsername.getText().toString();
        String password = mTXTPassword.getText().toString();

        List<String> MyData = new_data.SelectUsers(userName,password);
        return((MyData.size()>1));
//        if (!userName.equalsIgnoreCase(MyData.get(0).toString())||!password.equals(MyData.get(1).toString())) {
//            return false;


    }
    private void initViews() {
        this.TransType_DAO = new InvTransTypes_DAO(this);
        this.ItemDir_DAO = new ItemsDirectory_DAO(this);
        this.setting_dao = new Setting_DAO(this);
        this.mTxtUsername =  findViewById(R.id.txt_user_name_DB);
        this.mTXTPassword = findViewById(R.id.txt_password_DB);
        this.btn_loginDB =  findViewById(R.id.btn_loginDB);
        this.btn_loginDB.setOnClickListener(this);
        List<Settings> settings = setting_dao.getAllSettings1();
        for (Settings cn : settings) {
            DBNAME= cn.getDatabaseName();
            DBserver = cn.getServerName();
        }
        this. new_data = new CRUD_Operations(DBNAME,DBserver);
    }

    // Set data in local database with data loaded from MIS Database
    private void Set_data()
    {

//
//        List<Map<String,String>> MyData = null; // return array of select query values
//        MyData= new_data.Select();
//        for (Map<String,String> map:MyData)
// {
//            for (Map.Entry<String,String> e:map.entrySet()) {
//                String key = e.getKey();
//                String value = e.getValue();
//                TransValues.add(value);
//            }
//   }
        List<String> TransValues=new_data.SelectTrans();
        List<String> Itemslist= new_data.SelectItems();
        for  (int i=0;i< TransValues.size();i+=4)
        {
            for(int j=i;j<=i;j+=4) {
                TransType_DAO.addInvTransTypes(new InvTransTypes(TransValues.get(j), TransValues.get(j+1), Integer.parseInt(TransValues.get(j+2)), Integer.parseInt(TransValues.get(j+3))));
            }
        }
        for  (int i=0;i< Itemslist.size();i+=3) {
            for(int j=i;j<=i;j+=3){
                ItemDir_DAO.addItemsDirectory(new ItemsDirectory(Itemslist.get(j),Itemslist.get(j+1),Itemslist.get(j+2)));
        }
        }
//        List<ItemsDirectory> Trans = ItemDir_DAO.getAllItemsDirectory();
//
//        for  (int i=0;i< Itemslist.size();i+=3) {
//            for(int j=i;j<=i;j+=3){
//                String log = "Id: "+Trans.get(j)+" ,Name: " +Trans.get(j+1)  + " ,Phone: " + Trans.get(j+2);
//
//                Log.d("Name: ", log);
//            }
//        }
    }

    @Override
    public void onClick(View v) {
        if(v==btn_loginDB){
            if (Checkuser())
            {
                Set_data();
                Toast.makeText(this,"Database has been initialized Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(InitializeDataBaseActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Destroy activity A and not exist in Back stack
            }

            else {
                Toast.makeText(this,"Wrong User name or Password", Toast.LENGTH_SHORT).show();
                mTxtUsername.setError("");
                mTXTPassword.setError("");}
        }

    }
}
