package com.saudisoft.mis_android.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.saudisoft.mis_android.DAO.ItemInOutH_DAO;
import com.saudisoft.mis_android.DAO.ItemsInOutL_DAO;
import com.saudisoft.mis_android.DAO.ItemsSerials_DAO;
import com.saudisoft.mis_android.DAO.Setting_DAO;
import com.saudisoft.mis_android.ExternalDatabase.CRUD_Operations;
import com.saudisoft.mis_android.Model.ItemSerials;
import com.saudisoft.mis_android.Model.ItemsInOutH;
import com.saudisoft.mis_android.Model.Settings;
import com.saudisoft.mis_android.R;
import com.saudisoft.mis_android.adapter.GridListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendDataToMIS extends AppCompatActivity {
    public ListView Header_LV,Serials_LV;
    ArrayAdapter<String> arrayadapter;
    private GridListAdapter adapter;
    private Setting_DAO setting_dao;
    String TType,TDate,Tnum,Tcode,mNote,mIsnew,mSerial;
    private  String DBNAME,Branchcode,DBserver,serialnum,mid,itemcode, MISUser;
    private ItemInOutH_DAO ItemHeader_DAO;
    private ItemsInOutL_DAO ItemDetail_DAO;
    private ItemsSerials_DAO ItemSerial_DAO;
    List<ItemSerials> saved_serials;
    List<ItemsInOutH> headerList;
    List<Map<String, String>> headerList2;
    int listitemcount;
    private final String DEFAULT = "N/A";
    Map<String,String> myMap = new HashMap<>();
    CRUD_Operations new_data;
    TextView listcount,LCount;
    ArrayList<String> newserial = new ArrayList<>();
    List<Map<String,String>> select_details;
    private void initViews() {
        this.ItemHeader_DAO = new ItemInOutH_DAO(this);
        this.ItemDetail_DAO = new ItemsInOutL_DAO(this);
        this.ItemSerial_DAO = new ItemsSerials_DAO(this);
        this.setting_dao = new Setting_DAO(this);
        Header_LV = (ListView) findViewById(R.id.saved_voucher_hdr);
        Serials_LV = (ListView) findViewById(R.id.saved_serial_list);
        LCount = (TextView) findViewById(R.id.TV_saved_count);
        listcount = (TextView) findViewById(R.id.TV_serial_count);
        List<Settings> settings = setting_dao.getAllSettings1();
        for (Settings cn : settings) {
            DBNAME= cn.getDatabaseName();
            DBserver = cn.getServerName();
            Branchcode = cn.getBranchCode();
        }

        this. new_data = new CRUD_Operations(DBNAME,DBserver);
    }

    private  void LoadData(){
        headerList =ItemHeader_DAO.getAllItemheader();
        adapter = new GridListAdapter(SendDataToMIS.this, headerList, false );
        Header_LV.setAdapter(adapter);
        LCount.setText(getlistcount(Header_LV));
        //load data from saved shared preferences to this activity
        SharedPreferences get = getSharedPreferences( "User_data",MODE_PRIVATE );
        MISUser= get.getString("name",DEFAULT) ;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sendbtn, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle add serials button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
        if (Serials_LV.getCount()<1) {
            Toast.makeText(this, "Please Select Voucher", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
        if (Serials_LV.getCount()>0) {
//            if (id == R.id.add_serial_btn) {}
            if (send_serials()){
                Toast.makeText(SendDataToMIS.this, "New Serials has been Saved Successfully", Toast.LENGTH_SHORT).show();
                Serials_LV.setAdapter(null);}
            else
                Toast.makeText(SendDataToMIS.this, new_data.Sql_MSG, Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "Please Enter Serial", Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);
        initViews();
        LoadData();
        Header_LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Serials_LV.setAdapter(null);
                ClearList();
                ItemsInOutH hdr = (ItemsInOutH) Header_LV.getItemAtPosition(position);

                    select_details= ItemDetail_DAO.getItemDetails(hdr.getSerial());
                    saved_serials= ItemSerial_DAO.getSavedSerials(hdr.getSerial());
                    serialnum = select_details.get(0).get("Serial");
                    mid = select_details.get(0).get("ID");
                    itemcode = select_details.get(0).get("ItemCode");
                    headerList2 = ItemHeader_DAO.getItemHeader(serialnum);
                    mSerial = headerList2.get(0).get("item_serial");
                    TType = headerList2.get(0).get("trans_type");
                    TDate = headerList2.get(0).get("trans_date");
                    Tnum=headerList2.get(0).get("trans_num");
                    mNote = headerList2.get(0).get("Note");
                    mIsnew = headerList2.get(0).get("is_new");
                    Tcode = headerList2.get(0).get("TransCode");
                    newserial.clear();
                    for (int i = 0; i < saved_serials.size(); i ++) {
                       newserial.add( saved_serials.get(i).getSerial());
                    }
                arrayadapter = new ArrayAdapter<>(getApplicationContext(),R.layout.list_ltem,R.id.voucher_serials_txt,newserial);

                Serials_LV.setAdapter(arrayadapter);
                listcount.setText(getlistcount(Serials_LV));

            }
        });
    }
    private  boolean  send_serials(){
         if(new_data.InsertNewSerials(newserial,mid,serialnum,Tcode,Tnum,TDate,TType,itemcode,Branchcode, MISUser))
         {for(String srl :newserial)
             ItemSerial_DAO.deleteItemSerials(new ItemSerials(srl, serialnum, mid));
        return true;}
        else
            return false;
    }

    private String getlistcount(ListView Ls){
        listitemcount= Ls.getCount();
        return listitemcount+"";
    }

    public void ClearList() {

        newserial.clear();
        arrayadapter = new ArrayAdapter<>(this,R.layout.list_ltem,R.id.voucher_serials_txt,newserial);
        arrayadapter.notifyDataSetChanged();

        Serials_LV.setAdapter(arrayadapter);
        listcount.setText(getlistcount(Serials_LV));//show the list count
        Serials_LV.setSelection(Serials_LV.getCount());// restore the position of listview

    }

    @Override
    public void onBackPressed() {
//        moveTaskToBack(true);
        startActivity(new Intent(SendDataToMIS.this, MainActivity.class));
        this.finish(); // Destroy activity A and not exist in Back stack
    }
}
