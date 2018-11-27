package com.saudisoft.mis_android.Activity;

import android.content.Intent;
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
import com.saudisoft.mis_android.Model.ItemSerials;
import com.saudisoft.mis_android.Model.ItemsInOutH;
import com.saudisoft.mis_android.R;
import com.saudisoft.mis_android.adapter.GridListAdapter;

import java.util.ArrayList;
import java.util.List;

public class SendDataToMIS extends AppCompatActivity {
    public ListView Header_LV,Serials_LV;
    ArrayAdapter<String> arrayadapter;
    private GridListAdapter adapter;
    private ItemInOutH_DAO ItemHeader_DAO;
    private ItemsInOutL_DAO ItemDetail_DAO;
    private ItemsSerials_DAO ItemSerial_DAO;
    List<ItemSerials> saved_serials;
    List<ItemsInOutH> headerList;
    int listitemcount;
    TextView listcount,LCount;
    ArrayList<String> newserial = new ArrayList<>();

    private void initViews() {
        this.ItemHeader_DAO = new ItemInOutH_DAO(this);
        this.ItemDetail_DAO = new ItemsInOutL_DAO(this);
        this.ItemSerial_DAO = new ItemsSerials_DAO(this);
        Header_LV = (ListView) findViewById(R.id.saved_voucher_hdr);
        Serials_LV = (ListView) findViewById(R.id.saved_serial_list);
        LCount = (TextView) findViewById(R.id.TV_saved_count);
        listcount = (TextView) findViewById(R.id.TV_serial_count);
    }

    private  void LoadData(){
        headerList =ItemHeader_DAO.getAllItemheader();
        adapter = new GridListAdapter(SendDataToMIS.this, headerList, false );
        Header_LV.setAdapter(adapter);
        LCount.setText(getlistcount(Header_LV));
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
            Toast.makeText(this, "New Serials has been Saved Successfully", Toast.LENGTH_SHORT).show();
            Serials_LV.setAdapter(null);
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
                if (Serials_LV.getCount()<1){
                ItemsInOutH hdr = (ItemsInOutH) Header_LV.getItemAtPosition(position);
                saved_serials= ItemSerial_DAO.getSavedSerials(hdr.getSerial());
                    for (int i = 0; i < saved_serials.size(); i ++) {
                       newserial.add( saved_serials.get(i).getSerial());
                    }
                arrayadapter = new ArrayAdapter<>(getApplicationContext(),R.layout.list_ltem,R.id.voucher_serials_txt,newserial);

                Serials_LV.setAdapter(arrayadapter);
                listcount.setText(getlistcount(Serials_LV));
            }
            }
        });
    }
    private String getlistcount(ListView Ls){
        listitemcount= Ls.getCount();
        return listitemcount+"";
    }
    @Override
    public void onBackPressed() {
//        moveTaskToBack(true);
        startActivity(new Intent(SendDataToMIS.this, MainActivity.class));
        this.finish(); // Destroy activity A and not exist in Back stack
    }
}
