package com.saudisoft.mis_android.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.saudisoft.mis_android.DAO.InvTransTypes_DAO;
import com.saudisoft.mis_android.DAO.Setting_DAO;
import com.saudisoft.mis_android.ExternalDatabase.CRUD_Operations;
import com.saudisoft.mis_android.ExternalDatabase.Sync_Data;
import com.saudisoft.mis_android.Model.InvTransTypes;
import com.saudisoft.mis_android.Model.Settings;
import com.saudisoft.mis_android.R;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    NavigationView navigationView;
    private InvTransTypes_DAO transTypes_dao;
    private ActionBarDrawerToggle mDrawerToggle;
    private final String DEFAULT = "N/A";
    private EditText mUsername,mPassword;
    private TextView Imported_V_tv;
    private TextView Sent_V_tv;
    private TextView Created_V_tv;
    private TextView MISUser_tv;
    private TextView Sys_MSG_tv;
    private TextView totalitem_tv;
    private TextView totalsrl_tv;
    private TextView Datetime_tv;
    private TextView User_nav;
    private TextView Branch_nav;
    private TextView Notification_tv;
    List<Settings> setting;
    CRUD_Operations new_data;
    Sync_Data my_data;
    String Result;
    int sentcount=0;
    private  String DBNAME,DBserver,Branchcode;
    private void initViews() {

        this.transTypes_dao = new InvTransTypes_DAO(this);
        com.saudisoft.mis_android.DAO.Setting_DAO setting_DAO = new Setting_DAO(this);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);

        Imported_V_tv = (TextView) findViewById(R.id.TV_iv);
        Created_V_tv=(TextView)findViewById(R.id.TV_cv) ;
        Sent_V_tv=(TextView)findViewById(R.id.TV_sv) ;
        Notification_tv=(TextView)findViewById(R.id.status_header_TV);
        Sys_MSG_tv = (TextView)findViewById(R.id.status_TV) ;
        totalitem_tv=(TextView)findViewById(R.id.totalitem_TV);
        totalsrl_tv=(TextView)findViewById(R.id.totalserial_TV);
        MISUser_tv=(TextView)findViewById(R.id.username_TV) ;
        User_nav=headerView. findViewById(R.id.user_name_txt) ;
        Branch_nav=headerView.findViewById(R.id.branch_name_txt) ;
        Datetime_tv=(TextView)findViewById(R.id.date_time_TV) ;

        Created_V_tv.setOnClickListener(this);
        Sent_V_tv.setOnClickListener(this);
        Imported_V_tv.setOnClickListener(this);
        this.setting = setting_DAO.getAllSettings1();
        for (Settings cn : setting) {
            DBNAME= cn.getDatabaseName();
            DBserver= cn.getServerName();
            Branchcode = cn.getBranchCode();
        }
        this. new_data = new CRUD_Operations(DBNAME,DBserver);
        this.my_data = new Sync_Data(DBNAME,Branchcode,DBserver,this);
        //load data from saved shared preferences to this activity

        update_txt();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();
        ChkInvTransData();

        // region Check connection dialog
        if (!CheckConnectionSettings()){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                //set icon
                .setIcon(R.drawable.ic_warning)
                //set title
                .setTitle("System Setting Connection")
                //set message
                .setMessage("Connection Data Are'nt Set \n Press 'yes' to Initialize it ")
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
        //endregion
        //region  side navigation

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.import_data:
                        if (isNetworkAvailable())
                            createAndShowDialog(ReadVoucher_Activity.class);
                        else
                            Toast.makeText(MainActivity.this,"Please Check your connection", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.send_data:
                        if (isNetworkAvailable()) {
                            createAndShowDialog(null);
                        }else
                            Toast.makeText(MainActivity.this,"Please Check your connection", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.scan_serial:
                        startActivity(new Intent(MainActivity.this, Voucher_DataEntryActivity.class));
                        MainActivity.this.finish();
                        break;
                    case R.id.About:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom);
                        builder.setTitle("MIS App")
                                .setMessage(R.string.dialog_message)
                                .setCancelable(false)
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });

                        AlertDialog about = builder.create();
                        about.show();
                        ((TextView) about.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                        break;
                    case R.id.system_setting:
                        if (isNetworkAvailable())
                         startActivity(new Intent(MainActivity.this, SettingActivity.class));

                        else
                            Toast.makeText(MainActivity.this,"Please Check your connection", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.Share:
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT, "Please  If you like our APP Share it with your friend Thanks ");
                        startActivity(Intent.createChooser(share, "Access Mate App"));
                        break;
                    case R.id.db_setting:
                        if (isNetworkAvailable())
                        startActivity(new Intent(MainActivity. this, InitializeDataBaseActivity.class));
                        else
                            Toast.makeText(MainActivity. this,"Please Check your connection", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        // endregion
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {


                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }
    public boolean CheckConnectionSettings(){return !(setting.size()<1);}
    // check if invtrans type table is empty or not
    private void ChkInvTransData(){

//         Reading all Trans
//        Log.d("Reading: ", "Reading all Trans..");
        List<InvTransTypes> Trans = transTypes_dao.getAllInvTransType();


        if (Trans.size()<=0 ){
            Imported_V_tv.setEnabled(false);
            Sent_V_tv.setEnabled(false);
            Created_V_tv.setEnabled(false);
            Imported_V_tv.setText("0");
            Sent_V_tv.setText("0");
            Created_V_tv.setText("0");
            totalsrl_tv.setText("0");
            MISUser_tv.setText("");
        }

        else{
            //data not initialized yet
            Imported_V_tv.setEnabled(true);
            Sent_V_tv.setEnabled(true);
            Created_V_tv.setEnabled(true);}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
       if (id == R.id.item_logout) {
           if (isNetworkAvailable())
           { startActivity(new Intent(this, InitializeDataBaseActivity.class));
            return true;}
           else
               Toast.makeText(this,"Please Check your connection", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.item_exit) {
            this.finish();
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
        transTypes_dao.close();
    }
    @Override
    public void onClick(View v) {
        if(v==Created_V_tv) {
            if (isNetworkAvailable()) {
                createAndShowDialog(CreateVoucher.class);
            }else
                Toast.makeText(this,"Please Check your connection", Toast.LENGTH_SHORT).show();

        }
        if(v==Sent_V_tv)
        if (isNetworkAvailable()) {
            createAndShowDialog(null);
        }else
            Toast.makeText(this,"Please Check your connection", Toast.LENGTH_SHORT).show();
        if(v==Imported_V_tv)
        {startActivity(new Intent(MainActivity.this, Voucher_DataEntryActivity.class));
        finish();}




    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void createAndShowDialog( final Class<? extends Activity> ActivityToOpen)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        // Inflate and set the layout for the dialog
        View v = inflater.inflate( R.layout.login_dialog, null );
        // Pass null as the parent view because its going in the dialog layout
        v.findViewById( R.id.panel );
        builder.setTitle("MIS USER LOGIN ");
        builder.setIcon(R.drawable.ic_profile_img);
        builder.setView( v );
        mUsername =  v.findViewById(R.id.username);
        mPassword = v.findViewById(R.id.password);
        // Add the buttons
        builder.setPositiveButton( R.string.login, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked login button

                if (Checkuser())
                {
                    if(ActivityToOpen!=null){
                    startActivity(new Intent(MainActivity.this, ActivityToOpen));
                    MainActivity.this.finish();}
                    else {
                        my_data.sync_data();

                    }update_txt();
                }
                else
                    Toast.makeText(MainActivity.this,"Incorrect user name or password !", Toast.LENGTH_SHORT).show();
            }
        } );
        builder.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked cancel button
                if (isAlertDialogShowing((AlertDialog) dialog)) {
                    dialog.dismiss();
                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public boolean Checkuser() {

        String userName = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        List<String> MyData = new_data.SelectUsers(userName, password);
        return (MyData.size() > 1) && Save();
    }
    public boolean isAlertDialogShowing(AlertDialog thisAlertDialog) {
        return thisAlertDialog != null && thisAlertDialog.isShowing();
    }
    public void update_txt(){

        SharedPreferences get = this.getSharedPreferences( "User_data",MODE_PRIVATE );
        SharedPreferences.Editor editor = get.edit();
        MISUser_tv.setText(get.getString("name",""));
        Calendar cal = Calendar.getInstance();
        String mDate= cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE)+ "\n"+cal.get(Calendar.DATE)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.YEAR);
        Datetime_tv.setText(mDate);
        User_nav.setText(MISUser_tv.getText());
        Branch_nav.setText(get.getString("branch_name",""));
        Sys_MSG_tv.setText( get.getString("sendmsg",""));
        editor.remove("sendmsg").apply();
        Imported_V_tv.setText(String.valueOf(my_data.ItemHeaderCount()));
        totalitem_tv.setText(String.valueOf( my_data.ItemDetailCount()));
        Sent_V_tv.setText(get.getString("sendcount","0"));
        totalsrl_tv.setText(String.valueOf(my_data.ItemSerialCount()));
    }
    public  boolean Save()
    {
        //user_data = mydata , Mode_private => use data only on my app
        SharedPreferences sharedPreferences = getSharedPreferences( "User_data", Context.MODE_PRIVATE );
        //edit my data which i get
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //put string take two value(Key ,value)
        editor.putString("name", mUsername.getText().toString());
        editor.putString( "password",mPassword.getText().toString());
        return editor.commit();
//        editor.apply();
    }
    private void ShowDialog( String MSG )
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        // Inflate and set the layout for the dialog
        View v = inflater.inflate( R.layout.sync_dialog, null );
        // Pass null as the parent view because its going in the dialog layout
        v.findViewById( R.id.panel1 );
        builder.setView( v );
        TextView mMessage = v.findViewById(R.id.msg_txt);
        mMessage.setText(MSG);
        builder .setIcon(R.drawable.ic_warning) .setTitle("Sync Data Message");
                //set title

        // Add the buttons
        builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked login button
                if (isAlertDialogShowing((AlertDialog) dialog)) {
                    dialog.dismiss();
                }
            }
        } );


        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
