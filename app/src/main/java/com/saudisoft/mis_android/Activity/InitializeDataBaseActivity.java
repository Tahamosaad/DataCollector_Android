package com.saudisoft.mis_android.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    private  String DBNAME,DBserver,Branchcode;
    private Button btn_loginDB;
    private EditText mTxtUsername,mTXTPassword;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;
    CRUD_Operations new_data;
    private Handler handler;
    private ProgressDialog progress;
    private Context context;
    private void initViews() {
        this.TransType_DAO = new InvTransTypes_DAO(this);
        this.ItemDir_DAO = new ItemsDirectory_DAO(this);
        Setting_DAO setting_dao = new Setting_DAO(this);
        this.mTxtUsername =  findViewById(R.id.txt_user_name_DB);
        this.mTXTPassword = findViewById(R.id.txt_password_DB);
        this.btn_loginDB =  findViewById(R.id.btn_loginDB);
        this.btn_loginDB.setOnClickListener(this);
        context = InitializeDataBaseActivity.this;
        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait !");
        progress.setMessage("Login...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        List<Settings> settings = setting_dao.getAllSettings1();
        for (Settings cn : settings) {
            DBNAME= cn.getDatabaseName();
            DBserver = cn.getServerName();
            Branchcode = cn.getBranchCode();
        }
        this. new_data = new CRUD_Operations(DBNAME,DBserver);
    }
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
                handler = new Handler()
            {

                @Override
                public void handleMessage(Message msg)
                {
                    progress.dismiss();
                    Toast.makeText(InitializeDataBaseActivity. this,"Database has been initialized Successfully", Toast.LENGTH_SHORT).show();
                    Save();
                    Intent mainIntent = new Intent(context, MainActivity.class);
                    startActivity(mainIntent);
                    super.handleMessage(msg);
                    finish();}
            };
            progress.show();
            new Thread()
            {
                public void run()
                {

                        Set_data();
                       handler.sendEmptyMessage(0);

                }

            }.start();

        }else {
                Toast.makeText(InitializeDataBaseActivity. this,"Worng user name or password", Toast.LENGTH_SHORT).show();
                mTxtUsername.setError("");
                mTXTPassword.setError("");
            }
        }



    }
    public  boolean Save()
    {
        //user_data = mydata , Mode_private => use data only on my app
        SharedPreferences sharedPreferences = getSharedPreferences( "User_data", Context.MODE_PRIVATE );
        //edit my data which i get
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //put string take two value(Key ,value)
        editor.putString("name", mTxtUsername.getText().toString());
        editor.putString( "password",mTXTPassword.getText().toString());
        editor.putString("branch_name",new_data.GetStore(Branchcode));
        return editor.commit();
//        editor.apply();
    }
}