package com.saudisoft.mis_android.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.saudisoft.mis_android.DAO.ItemInOutH_DAO;
import com.saudisoft.mis_android.DAO.ItemsInOutL_DAO;
import com.saudisoft.mis_android.DAO.ItemsSerials_DAO;
import com.saudisoft.mis_android.DAO.Setting_DAO;
import com.saudisoft.mis_android.DatePickerFragment;
import com.saudisoft.mis_android.ExternalDatabase.CRUD_Operations;
import com.saudisoft.mis_android.Model.ItemSerials;
import com.saudisoft.mis_android.Model.ItemsInOutH;
import com.saudisoft.mis_android.Model.ItemsInOutL;
import com.saudisoft.mis_android.Model.Settings;
import com.saudisoft.mis_android.R;
import com.saudisoft.mis_android.adapter.CustomListAdapter;
import com.saudisoft.mis_android.adapter.GridListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReadVoucher_Activity extends AppCompatActivity implements DatePickerFragment.DateDialogListener, View.OnClickListener {
    private ImageView calendarImage;
    private ItemInOutH_DAO ItemHeader_DAO;
    private ItemsInOutL_DAO ItemDetail_DAO;
    private ItemsSerials_DAO ItemSerial_DAO;
    private Handler handler;
    private ProgressDialog progress;
    private Context context;
    CRUD_Operations new_data;
    private ArrayList<String> Serial_List;
    private EditText voucherDateEditText;
    private FloatingActionButton save_local;
    private static final String DIALOG_DATE = "DialogDate";
    String VoucherDate;
    ListView LV_Voucher;
    List<ItemsInOutH> voucherlist;
    //    List<String> VoucherHdr_list =new ArrayList<>() ;
    List<String> VoucherDtl_list;
    List<String> VoucherSerial_list;
    Map<String, String> hdr;
    List<ItemsInOutH> SelectedItems;
    List<Map<String, String>> SelectedSerials;
    List<Map<String, String>> VoucherHeader = new ArrayList<>();
    List<Map<String, String>> VoucherHeader2 = new ArrayList<>();
    //    Map<String, ItemsInOutH> hdrData = new HashMap<>();
    //    List<ItemSerials>        Selected_serials;
    List<Map<String, String>> SelectedDetails;
    private GridListAdapter adapter;
    private CustomListAdapter my_adapter;
    String Branchname, DBname, DBserver;

    private void initViews() {
        voucherDateEditText = (EditText) findViewById(R.id.edit_text_start_date);
        calendarImage = (ImageView) findViewById(R.id.image_view_start_date);
        LV_Voucher = (ListView) findViewById(R.id.list_vouchers);
        this.ItemHeader_DAO = new ItemInOutH_DAO(this);
        this.ItemDetail_DAO = new ItemsInOutL_DAO(this);
        this.ItemSerial_DAO = new ItemsSerials_DAO(this);
        Setting_DAO setting_DAO = new Setting_DAO(this);
        voucherDateEditText.setOnClickListener(this);
        List<Settings> settings = setting_DAO.getAllSettings1();
        for (Settings cn : settings) {
            Branchname = cn.getBranchCode();
            DBname = cn.getDatabaseName();
            DBserver = cn.getServerName();
        }
        this.new_data = new CRUD_Operations(DBname, DBserver);
        voucherDateEditText.setText(new_data.GetServerDate());
        save_local = (FloatingActionButton) findViewById(R.id.btn_save_local);
        save_local.setOnClickListener(this);
        calendarImage.setOnClickListener(this);
        context = ReadVoucher_Activity.this;
        progress = new ProgressDialog(this);
        progress.setTitle("Processing !");
        progress.setMessage("Loading...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        voucherDateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(!hasFocus) {
//                    FragmentManager manager = getSupportFragmentManager();
//                    DatePickerFragment dialog = new DatePickerFragment();
//                    dialog.show(manager, DIALOG_DATE);
//                }
//            }
//        });
//
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_voucher);
        initViews();
        LoadSavedData();
    }

    private String GetSelectedSerials() {
        SparseBooleanArray selectedRows = my_adapter.getSelectedIds();//Get the selected ids from adapter

        if (selectedRows.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            //Loop to all the selected rows array
            for (int i = 0; i < selectedRows.size(); i++) {

                //Check if selected rows have value i.e. checked item
                if (selectedRows.valueAt(i)) {

                    //Get the checked item text from array list by getting keyAt method of selected Rows array
                    String selectedRowLabel = Serial_List.get(selectedRows.keyAt(i));

                    //append the row label text
                    stringBuilder.append("'").append(selectedRowLabel).append("'").append(",");

                    //Selected_item.add(selectedRowLabel);

                }

            }
            //5- get selected serials in serials string sperated by ','
            //          Toast.makeText(ReadVoucher_Activity.this, "Selected Rows\n" + serials, Toast.LENGTH_SHORT).show();
            return Serials(stringBuilder.toString());
        } else
            return "";

    }

    private void SaveSelectedVouchers(String serials) {
        //7- select all VoucherDetails where current selected serials
        VoucherDtl_list = new_data.SelectedVoucherDtl(serials);
        for (int i = 0; i < VoucherDtl_list.size(); i += 6) {
            for (int j = i; j <= i; j += 6) {
                //8- 	Save voucher Details Locally
                ItemDetail_DAO.addItemDetail(new ItemsInOutL(VoucherDtl_list.get(j), VoucherDtl_list.get(j + 1), VoucherDtl_list.get(j + 2), Float.parseFloat(VoucherDtl_list.get(j + 3)), VoucherDtl_list.get(j + 4), VoucherDtl_list.get(j + 5)));
            }
        }
        //9- SELECT Serial, SerialNum,ID form serial table where serial in (selected serials)
        VoucherSerial_list = new_data.SelectedVoucherSerial(serials);
        for (int i = 0; i < VoucherSerial_list.size(); i += 3) {
            for (int j = i; j <= i; j += 3) {
                //10- savemenu Serials in Locally serial table
                ItemSerial_DAO.addItemSerials(new ItemSerials(VoucherSerial_list.get(j), VoucherSerial_list.get(j + 1), VoucherSerial_list.get(j + 2)));
            }
        }
        //11- SELECT Serial,TransNum,TransCode,TransDate,Notes from voucherHeader
        VoucherHeader = new_data.SelectedVoucherHdr(serials);
        for (Map<String, String> hdr : VoucherHeader) {
//            for (int j = i; j <= i; j += 5) {
            //12- savemenu Voucher Header in Locally serial table
            ItemHeader_DAO.addItemHeader(new ItemsInOutH(hdr.get("Vserial"), hdr.get("TransNum"), hdr.get("TransCode"), hdr.get("TransDate"), hdr.get("Notes"), 0));
//            }
        }
    }
    //region old
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.editbtn, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//     handle button activities
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        ItemHeader_DAO.deleteTable();
//        ItemDetail_DAO.deleteTable();
//        ItemSerial_DAO.deleteTable();
//        int id = item.getItemId();
//
//        if (id == R.id.edit_btn) {
//
//            if (VoucherHdr_list!= null ) {
//                //Check if item is selected or not via size
//                Serial_List = new ArrayList<>();
//                SelectedItems = new ArrayList<>();
//                SelectedSerials = new ArrayList<>();
//                SelectedDetails = new ArrayList<>();
//                for (int i = 0; i < VoucherHdr_list.size(); i += 4) {
//                    for (int j = i; j <= i; j += 4) {
//                        Serial_List.add(VoucherHdr_list.get(j));
//                    }
//                }
//            SparseBooleanArray selectedRows = adapter.getSelectedIds();//Get the selected ids from adapter
//
//            if (selectedRows.size() > 0) {
//                StringBuilder stringBuilder = new StringBuilder();
//                //Loop to all the selected rows array
//                for (int i = 0; i < selectedRows.size(); i++) {
//
//                    //Check if selected rows have value i.e. checked item
//                    if (selectedRows.valueAt(i)) {
//
//                        //Get the checked item text from array list by getting keyAt method of selected Rows array
//                        String selectedRowLabel = Serial_List.get(selectedRows.keyAt(i));
//
//                        //append the row label text
//                        stringBuilder.append("'").append(selectedRowLabel).append("'").append(",");
//
//
////                        Selected_item.add(selectedRowLabel);
//
//                    }
//
//                }
//                //5- get selected serials in serials string sperated by ','
//                String serials = Serials(stringBuilder.toString());//to remove last ','
////                Toast.makeText(ReadVoucher_Activity.this, "Selected Rows\n" + serials, Toast.LENGTH_SHORT).show();
//
//
//                //7- select all VoucherDetails where current selected serials
//                VoucherDtl_list =new_data.SelectedVoucherDtl(serials);
//                for (int i = 0; i < VoucherDtl_list.size(); i += 6) {
//                    for (int j = i; j <= i; j += 6) {
//                        //8- 	Save voucher Details Locally
//                        ItemDetail_DAO.addItemDetail(new ItemsInOutL(VoucherDtl_list.get(j), VoucherDtl_list.get(j +1), VoucherDtl_list.get(j + 2),Float.parseFloat( VoucherDtl_list.get(j +3)),VoucherDtl_list.get(j + 4),VoucherDtl_list.get(j + 5)));
//                    }
//                }
//                //9- SELECT Serial, SerialNum,ID form serial table where serial in (selected serials)
//                VoucherSerial_list =new_data.SelectedVoucherSerial(serials);
//                for (int i = 0; i < VoucherSerial_list.size(); i += 3) {
//                    for (int j = i; j <= i; j += 3) {
//                        //10- savemenu Serials in Locally serial table
//                        ItemSerial_DAO.addItemSerials(new ItemSerials(VoucherSerial_list.get(j), VoucherSerial_list.get(j +1),VoucherSerial_list.get(j +2)));
//                    }
//                }
//                //11- SELECT Serial,TransNum,TransCode,TransDate,Notes from voucherHeader
//                VoucherHdr_list =new_data.SelectedVoucherHdr(serials);
//                for (int i = 0; i < VoucherHdr_list.size(); i += 5) {
//                    for (int j = i; j <= i; j += 5) {
//                        //12- savemenu Voucher Header in Locally serial table
//                        ItemHeader_DAO.addItemHeader(new ItemsInOutH(VoucherHdr_list.get(j), VoucherHdr_list.get(j +1), VoucherHdr_list.get(j + 2), VoucherHdr_list.get(j +3), VoucherHdr_list.get(j + 4),0));
//                    }
//                }
//                //13-Get all saved table and savemenu it on temp list
////                SelectedItems = ItemHeader_DAO.getAllItemheader();
////                SelectedDetails=ItemDetail_DAO.GetAllItemDetails();
////                SelectedSerials=ItemSerial_DAO.GetAllItemSerials();
////                Selected_serials=ItemSerial_DAO.getAllItemSerials();
//                Intent i=new Intent(ReadVoucher_Activity.this, Voucher_DataEntryActivity.class);
//                this.finish();
//                //14- send all temp list to Voucher data entry  Activity to view it
////                i.putExtra("Header",  (Serializable) SelectedItems);
////                i.putExtra("Details", (Serializable) SelectedDetails);
////                i.putExtra("Serials", (Serializable) Selected_serials);
//                startActivity(i);
//
//            }
//
//        else {Toast.makeText(this,"No Voucher selected", Toast.LENGTH_SHORT).show();
//               return super.onOptionsItemSelected(item);}
//            }
//else {Toast.makeText(this,"No Voucher founded", Toast.LENGTH_SHORT).show();
//       return super.onOptionsItemSelected(item);}
//        }
//        return super.onOptionsItemSelected(item);
//    }
    //endregion

    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        return VoucherDate = sdf.format(date);

    }

    @Override
    public void onFinishDialog(Date date) {
        voucherDateEditText.setText(formatDate(date));
//        handler = new Handler() {
//
//            @Override
//            public void handleMessage(Message msg) {
//                progress.dismiss();
//                super.handleMessage(msg);
//            }
//
//        };
//        progress.show();
//        new Thread() {
//            public void run() {
//                Getdata();
//                handler.sendEmptyMessage(0);}
//
//        }.start();
        Getdata();
    }

    private void Getdata() {
        long ER =0;
        try {


//      0- clear all Locally (item header,item details, item serials)tables
//        ItemHeader_DAO.deleteTable();
//        ItemDetail_DAO.deleteTable();
//        ItemSerial_DAO.deleteTable();
            VoucherHeader.clear();
        //1- select top 100 of Voucher header where branchcode='j',VoucherDate='Selected Date'
        VoucherHeader = new_data.SelectVoucher(Branchname, voucherDateEditText.getText().toString());
//        for (Map<String, String> hdr : VoucherHeader)
//            {
////          2- Save voucher header Locally
//         ER =   ItemHeader_DAO.addItemHeader(new ItemsInOutH(hdr.get("Vserial"), hdr.get("TransNum"), hdr.get("TransCode"), hdr.get("TransDate"), hdr.get("Notes"), 0));
//        }
        //3- load local data ,4 view it
            my_adapter = new CustomListAdapter(ReadVoucher_Activity.this, VoucherHeader, true);
            LV_Voucher.setAdapter(my_adapter);
//        LoadSavedData();


        if (VoucherHeader.size()>0)
            Toast.makeText(ReadVoucher_Activity.this, "Data Loaded success", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(ReadVoucher_Activity.this, " Load Data Fail !", Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            Toast.makeText(ReadVoucher_Activity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
    }
    private void LoadSavedData() {
        VoucherHeader = new ArrayList<>();
//3- Get Local Voucher and savemenu it in temp list voucherlist
        if (ItemHeader_DAO.getAllItemsInOutH() != null)
            VoucherHeader = ItemHeader_DAO.getAllItemsInOutH();
        else
            Toast.makeText(getApplicationContext(), "No Voucher Founded ", Toast.LENGTH_SHORT).show();

        //4- view it as list view to make user select from it
        my_adapter = new CustomListAdapter(ReadVoucher_Activity.this, VoucherHeader, true);
        LV_Voucher.setAdapter(my_adapter);
        //
//        if (VoucherHeader.size() < 1) {
//            for (ItemsInOutH item : voucherlist) {
//                hdr = new HashMap<>();
//                hdr.put("Vserial", item.getSerial());
//                hdr.put("TransNum", item.getTransNum());
//                hdr.put("TransCode", item.getTransCode());
//                hdr.put("Notes", item.getNotes());
//                hdr.put("TransDate", item.getTransDate());
//                VoucherHeader.add(hdr);
//
//            }
//
//        }
    }

    public String Serials(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    @Override
    public void onClick(View v) {
        //region view date fragment when click on date edit text
        //
        if (isNetworkAvailable()) {
            if (v == voucherDateEditText || v == calendarImage) {

                FragmentManager manager = getSupportFragmentManager();
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.show(manager, DIALOG_DATE);
            }
        } else
            Toast.makeText(this, "Please Check your Network Connection !", Toast.LENGTH_SHORT).show();
        //endregion
        if (v == save_local) {
            handler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    progress.dismiss();
                    Intent mainIntent = new Intent(context, MainActivity.class);
                    startActivity(mainIntent);
                    super.handleMessage(msg);
                    finish();
                }

            };
            progress.show();
            new Thread() {
                public void run() {
                    if (VoucherHeader != null) {
                        Set_Data();
                        handler.sendEmptyMessage(0);
                    } else
                        Toast.makeText(ReadVoucher_Activity.this, "No Voucher founded", Toast.LENGTH_SHORT).show();
                }

            }.start();
//
        }
    }

    private void Set_Data() {
        //Check if item is selected or not via size
        Serial_List = new ArrayList<>();
        SelectedItems = new ArrayList<>();
        SelectedSerials = new ArrayList<>();
        SelectedDetails = new ArrayList<>();

//                for (int i = 0; i < VoucherHdr_list.size(); i += VoucherHdr_list.size()/2) {
        for (Map<String, String> item : VoucherHeader) {
            Serial_List.add(item.get("Vserial"));
        }

//                }
        if (!GetSelectedSerials().isEmpty()) {
            ItemHeader_DAO.deleteTable();
            ItemDetail_DAO.deleteTable();
            ItemSerial_DAO.deleteTable();
            SaveSelectedVouchers(GetSelectedSerials());
            Intent i = new Intent(ReadVoucher_Activity.this, MainActivity.class);
            ReadVoucher_Activity.this.finish();

            startActivity(i);

        } else
            Toast.makeText(ReadVoucher_Activity.this, "No Voucher selected", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStart() {
        super.onStart();

    }



    @Override
    public void onBackPressed() {
//        moveTaskToBack(true);
        startActivity(new Intent(ReadVoucher_Activity.this, MainActivity.class));
        this.finish(); // Destroy activity A and not exist in Back stack
    }
}
