package com.saudisoft.mis_android.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.Map;

public class Voucher_DataEntryActivity extends AppCompatActivity  {
    public static final String TAG = "Voucher_DataEntry";
    public ListView Header_LV,Details_LV,Serials_LV;
    ArrayAdapter<String> arrayadapter;
//    private CheckBox chkbox;
    String serialnum,mid;
    List<ItemsInOutH> headerList;
    List<ItemSerials> saved_serials;
    Button Add_serial;
    EditText input;
    int listitemcount;
    TextView listcount,listcount2;
    List<Map<String,String>> Selected_serials,select_details;
    List<ItemSerials> serialsList;
    ArrayList<String> VoucherSerial_list =  new ArrayList<>();
    ArrayList<String> newserial = new ArrayList<>();
    private GridListAdapter adapter;
    private ItemInOutH_DAO ItemHeader_DAO;
    private ItemsInOutL_DAO ItemDetail_DAO;
    private ItemsSerials_DAO ItemSerial_DAO;

    private void initViews() {
//        select_detail = (List<ItemsInOutL>) getIntent().getSerializableExtra("Details");
//        serialsList   = (List<ItemSerials>) getIntent().getSerializableExtra("Serials");
        this.ItemHeader_DAO = new ItemInOutH_DAO(this);
        this.ItemDetail_DAO = new ItemsInOutL_DAO(this);
        this.ItemSerial_DAO = new ItemsSerials_DAO(this);
        Header_LV =  findViewById(R.id.voucher_hdr);
        Details_LV =  findViewById(R.id.voucher_dtl);
        Serials_LV =  findViewById(R.id.serials_list);
        input =  findViewById(R.id.txt_serial);
        listcount2 = findViewById(R.id.TV_count3) ;

        Add_serial =  findViewById(R.id.btn_addserial);
        //disable view keybad on activity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            input.setShowSoftInputOnFocus(false);
        else
            input.setTextIsSelectable(true);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_dataentry);
       //1. initilize all views
        initViews();
        // Get HeaderList from Intent
        //2. get (selected vouchers Header) from previous activity or locally
        if( getIntent().getSerializableExtra("Header") ==null)
            headerList =ItemHeader_DAO.getAllItemheader();
        else
            headerList = (List<ItemsInOutH>) getIntent().getSerializableExtra("Header");
        adapter = new GridListAdapter(Voucher_DataEntryActivity.this, headerList, false );
        Header_LV.setAdapter(adapter);
        listcount = findViewById(R.id.TV_count1) ;
        listcount.setText(getlistcount(Header_LV));
    ///*****************************************************************************
    //region : this button is temporary for mobile version
        Add_serial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToList(input.getText().toString());
                 arrayadapter.notifyDataSetChanged();

                input.setText(""); // Clear the input

                Serials_LV.setAdapter(arrayadapter);
                // restore the position of listview
                Serials_LV.setSelection(Serials_LV.getCount());

            }
        });
        //endregion
        //*********************************************************************

        //3. set on voucher header list click view the voucher details on other view list
        Header_LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

//                String main = Header_LV.getSelectedItem().toString();
//                detailsList = (  List<Map<String,String>>) getIntent().getSerializableExtra("Details");
                ItemsInOutH hdr = (ItemsInOutH) Header_LV.getItemAtPosition(position);
                select_details= ItemDetail_DAO.getItemDetails(hdr.getSerial());
                serialnum = select_details.get(0).get("Serial");
                mid = select_details.get(0).get("ID");

                String[] fromwhere1 = {"ID","Serial","PartNo","Itemname","ItemCode","QTY"};
                int[] viewswhere1 = { R.id.voucher_ID_txt, R.id.voucher_serial1_txt,R.id.Part_No_txt,R.id.Item_Name_txt,R.id.Item_Code_txt,R.id.voucher_QTY_txt};
                SimpleAdapter itemsAdapter =  new SimpleAdapter(Voucher_DataEntryActivity.this, select_details,R.layout.voucher_dtl_layout, fromwhere1, viewswhere1);
                Details_LV.setAdapter(itemsAdapter);

                //read from text and inserted it  to list view.
                input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if( -1 != input.getText().toString().indexOf( "\n" ) ){
//                    input.setText("Enter was pressed!");
//                }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() > 0) {

                            char lastCharacter = s.charAt(s.length() - 1);

                            if (lastCharacter == '\n') {
                                String barcode = s.subSequence(0, s.length() - 1).toString();
                                if(!barcode.isEmpty()){

                                    addToList(barcode);
                                    input.requestFocus();
                                    Serials_LV.setSelection(Serials_LV.getCount());

                                    listcount2.setText(getlistcount(Serials_LV));//show the list count
                                }

                            }
                        }}
                });
//                select_detail= ItemDetail_DAO.getAllItemDetails(hdr.getSerial());


//                Log.i("Item", "Selected: " + hdr.getSerial());

//                  adapter = new GridListAdapter(Voucher_DataEntryActivity.this, serialsList, false );
//        ArrayAdapter<ItemsInOutL> itemsAdapter =
//                new ArrayAdapter<ItemsInOutL>(Voucher_DataEntryActivity.this,R.layout.voucher_dtl_layout,R.id.voucher_serial1_txt, select_details);
     }

        });

        Details_LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                if (Serials_LV.getCount()<1){
                saved_serials= ItemSerial_DAO.getSavedSerials(serialnum);
                  if(saved_serials.size()>0) {
                      for (int i = 0; i < saved_serials.size(); i ++) {
                          String Savedserial = saved_serials.get(i).getSerial();
                          addToList(Savedserial);
                          arrayadapter.notifyDataSetChanged();

                          input.setText(""); // Clear the input
                          listcount2.setText(getlistcount(Serials_LV));//show the list count

                          Serials_LV.setAdapter(arrayadapter);

                      }
//                      String[] fromwhere = {"Serial"};
//                      int[] viewswhere = {R.id.voucher_serials_txt};
//                      SimpleAdapter serialsAdapter = new SimpleAdapter(Voucher_DataEntryActivity.this, Selected_serials, R.layout.list_ltem, fromwhere, viewswhere);
//                      Serials_LV.setAdapter(serialsAdapter);
                  }else Toast.makeText(getApplicationContext(), "No Serials found", Toast.LENGTH_SHORT).show();
                } }
            });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savebtn, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle add serials button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (Details_LV.getCount()<1) {
            Toast.makeText(this, "Please Select Voucher Details", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
        if(Serials_LV.getCount()>0) {
            VoucherSerial_list.clear();
            // add all inserted serials to VoucherSerial_list
            if (id == R.id.add_serial_btn) {
                for (int i = 0; i < arrayadapter.getCount(); i++) {
                    Object obj = arrayadapter.getItem(i);
                    VoucherSerial_list.add(obj.toString());
                }

                for (int i = 0; i <= VoucherSerial_list.size(); i++) {

                    //10- save Serials in Locally serial table
                    ItemSerial_DAO.addItemSerials(new ItemSerials(VoucherSerial_list.get(i), serialnum, mid));

                }
                Toast.makeText(this, "New Serials has been Saved Successfully", Toast.LENGTH_SHORT).show();
                Details_LV.setAdapter(null);
                Serials_LV.setAdapter(null);
//              saved_serials= ItemSerial_DAO.getAllItemSerials();
                newserial.clear();
                listcount2.setText("0");

            }
        } else
            Toast.makeText(this, "Please Enter Serial", Toast.LENGTH_LONG).show();
            return super.onOptionsItemSelected(item);
    }

// add to serial list new item
    public void addToList(String barcode) {
       if(!barcode.isEmpty()){
           newserial.add(barcode);
           arrayadapter = new ArrayAdapter<>(this,R.layout.list_ltem,R.id.voucher_serials_txt,newserial);

           listcount2.setText(getlistcount(Serials_LV));//show the list count
           input.requestFocus();
           Serials_LV.setSelection(Serials_LV.getCount());

       }
     }
    @Override
    public void onBackPressed() {
//        moveTaskToBack(true);
        Intent intent = new Intent(Voucher_DataEntryActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Destroy activity A and not exist in Back stack
    }
    private String getlistcount(ListView Ls){
     listitemcount= Ls.getCount();
        return listitemcount+"";
    }
}
