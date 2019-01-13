package com.saudisoft.mis_android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.saudisoft.mis_android.DAO.Setting_DAO;
import com.saudisoft.mis_android.ExternalDatabase.CRUD_Operations;
import com.saudisoft.mis_android.Model.Settings;
import com.saudisoft.mis_android.R;

import java.util.ArrayList;
import java.util.List;

import static com.saudisoft.mis_android.R.id.btn_save;
import static com.saudisoft.mis_android.R.id.txt_branch_code;
import static com.saudisoft.mis_android.R.id.txt_database;
import static com.saudisoft.mis_android.R.id.txt_server_name;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText mTxtServerName,mTxtDatabaseName,mTxtBranchcode,mTxtDB_username;
    private Setting_DAO db;
    private  Button mBtnSave;
    CRUD_Operations new_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        // initialize views
        initViews();



    }
    private void initViews() {

        this.mTxtServerName = (EditText) findViewById(txt_server_name);
        this.mTxtBranchcode = (EditText) findViewById(txt_branch_code);
        this.mTxtDatabaseName = (EditText) findViewById(txt_database);
        mTxtServerName = (EditText)findViewById(txt_server_name);
        mTxtDatabaseName = (EditText)findViewById(txt_database);
        mTxtBranchcode = (EditText)findViewById(txt_branch_code);
//        mTxtDB_username = (EditText)findViewById(R.id.txt_last_name);
        mBtnSave = (Button) findViewById(btn_save);
        mBtnSave.setOnClickListener(this);
        this.db = new Setting_DAO(this);

//         Reading all settings
        List<Settings> settings = db.getAllSettings1();

        for (Settings cn : settings) {
            mTxtServerName.setText(cn.getServerName());
            mTxtDatabaseName.setText(cn.getDatabaseName());
            mTxtBranchcode.setText(cn.getBranchCode());

//            String log = "ServerName: "+cn.getServerName()+" ,DBname: " + cn.getDatabaseName() + " ,Branchcode: " + cn.getBranchCode()+ " ,username: " + cn.getLastUserName();
//            // Writing Contacts to log
//            Log.d("Name: ", log);
        }
    }
    private int checkempForm() {
        EditText[] allFields = {mTxtServerName, mTxtBranchcode,mTxtDatabaseName};
        List<EditText> ErrorFields =new ArrayList<>();
        for(EditText edit : allFields){
            if(TextUtils.isEmpty(edit.getText()))
            {

                ErrorFields.add(edit);
                // EditText was empty
                for(int i = 0; i < ErrorFields.size(); i++)
                {
                    EditText currentField = ErrorFields.get(i);
                    currentField.setError("this field required");
                    currentField.requestFocus();
                }
                return 0;
            }

        }
        return 1;
    }
    @Override
    public void onClick(View v) {
        if (v==mBtnSave) {

                // Inserting settings
                Editable serverName = mTxtServerName.getText();
                Editable branch = mTxtBranchcode.getText();
                Editable dataBase = mTxtDatabaseName.getText();
            if (checkempForm()>0)
            {


            if (!TextUtils.isEmpty(serverName) && !TextUtils.isEmpty(branch) && !TextUtils.isEmpty(dataBase))
                     {
                         this. new_data = new CRUD_Operations(mTxtDatabaseName.getText().toString(),mTxtServerName.getText().toString());
                      if(  new_data.getconnection()!=null){
                         if(isInserted()>0)
                        {
                            Toast.makeText(this,"updata data Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SettingActivity.this, InitializeDataBaseActivity.class);
                            startActivity(intent);
                            this.finish();
                        }
                        }
                        else
                            Toast.makeText(this,"Connection Fail !", Toast.LENGTH_SHORT).show();

                     }
                else { Toast.makeText(this, R.string.empty_fields_message, Toast.LENGTH_LONG).show();}

        }
        }
    }
    private int isUpdated(){
     return db.updateSettings(new Settings(mTxtServerName.getText().toString(),mTxtDatabaseName.getText().toString(),mTxtBranchcode.getText().toString(),"sa"));
    }
    private long isInserted(){
        return db.addSetting (new Settings(mTxtServerName.getText().toString(),mTxtDatabaseName.getText().toString(),mTxtBranchcode.getText().toString(),"sa"));

    }

}
