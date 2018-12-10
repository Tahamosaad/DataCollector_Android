package com.saudisoft.mis_android.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nlscan.android.scan.ScanManager;
import com.saudisoft.mis_android.DAO.ItemInOutH_DAO;
import com.saudisoft.mis_android.DAO.ItemsInOutL_DAO;
import com.saudisoft.mis_android.DAO.ItemsSerials_DAO;
import com.saudisoft.mis_android.Model.ItemSerials;
import com.saudisoft.mis_android.Model.ItemsInOutH;
import com.saudisoft.mis_android.R;
import com.saudisoft.mis_android.adapter.GridListAdapter;
import com.saudisoft.mis_android.adapter.SwipeDismissListViewTouchListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.saudisoft.mis_android.R.id.txt_serial;

public class Voucher_DataEntryActivity extends AppCompatActivity {
    public static final String TAG = "Voucher_DataEntry";
    public ListView Header_LV,Details_LV,Serials_LV;
    ArrayAdapter<String> arrayadapter;
    String serialnum,mid,MISUser;
    String[] new_serial;
    private final String DEFAULT = "N/A";
    private ScanManager mScanMgr;
    Set<String> not_repeated_serial;
    ArrayList<String> repeated_serial= new ArrayList<>();
    List<ItemsInOutH> headerList;
    List<ItemSerials> saved_serials;
    FloatingActionButton Add_serial;
    EditText input;
    int listitemcount;
    TextView listcount,listcount2;
    List<Map<String,String>> select_details;
//    ArrayList<String> VoucherSerial_list =  new ArrayList<>();
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
        Header_LV = (ListView) findViewById(R.id.voucher_hdr);
        Details_LV = (ListView) findViewById(R.id.voucher_dtl);
        Serials_LV = (ListView) findViewById(R.id.serials_list);
        input = (EditText) findViewById(txt_serial);
        listcount2 = (TextView) findViewById(R.id.TV_count3);
        Add_serial = (FloatingActionButton) findViewById(R.id.btn_addserial);
//        mScanMgr=ScanManager.getInstance();
//        mScanMgr.setOutpuMode(ScanSettings.Global.VALUE_OUT_PUT_MODE_FILLING);
        //region disable view keybad on activity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            input.setShowSoftInputOnFocus(false);
        else
            input.setTextIsSelectable(true);
//endregion
        //load data from saved shared preferences to this activity
        SharedPreferences get = getSharedPreferences( "User_data",MODE_PRIVATE );
        MISUser= get.getString("name",DEFAULT) ;
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
            Toast.makeText(getApplicationContext(), "No Voucher Selected", Toast.LENGTH_SHORT).show();
//            headerList = (List<ItemsInOutH>) getIntent().getSerializableExtra("Header");
        adapter = new GridListAdapter(Voucher_DataEntryActivity.this, headerList, false );
        Header_LV.setAdapter(adapter);
        listcount =(TextView) findViewById(R.id.TV_count1) ;
        listcount.setText(getlistcount(Header_LV));
    ///*****************************************************************************
        if(getIntent().getStringArrayListExtra("Serials") !=null)
        {newserial = getIntent().getStringArrayListExtra("Serials");
        arrayadapter = new ArrayAdapter<>(this,R.layout.list_ltem,R.id.voucher_serials_txt,newserial);
        arrayadapter.notifyDataSetChanged();
            Serials_LV.setAdapter(arrayadapter);
            listcount2.setText(getlistcount(Serials_LV));//show the list count
            Serials_LV.setSelection(Serials_LV.getCount());// restore the position of listview
             }
        //region : this button is temporary for mobile version
        Add_serial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(Voucher_DataEntryActivity.this, AddSerials.class));
                Intent i=new Intent(Voucher_DataEntryActivity.this, AddSerials.class);
                Voucher_DataEntryActivity.this.finish();
                //send all serial list to serial entry Activity to view it
                i.putExtra("CurrentSerials",newserial);
                startActivity(i);
//                addToList(input.getText().toString());
//                 arrayadapter.notifyDataSetChanged();
//
//                input.setText(""); // Clear the input
//
//                Serials_LV.setAdapter(arrayadapter);
//                // restore the position of listview
//                Serials_LV.setSelection(Serials_LV.getCount());

            }
        });
        //endregion
        //*********************************************************************

        //3. set on voucher header list click view the voucher details on other view list
        // region view selected voucher detail data
        Header_LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

//                String main = Header_LV.getSelectedItem().toString();
                ItemsInOutH hdr = (ItemsInOutH) Header_LV.getItemAtPosition(position);
                select_details= ItemDetail_DAO.getItemDetails(hdr.getSerial());
                serialnum = select_details.get(0).get("Serial");
                mid = select_details.get(0).get("ID");

                String[] fromwhere1 = {"ID","Serial","PartNo","Itemname","ItemCode","QTY"};
                int[] viewswhere1 = { R.id.voucher_ID_txt, R.id.voucher_serial1_txt,R.id.Part_No_txt,R.id.Item_Name_txt,R.id.Item_Code_txt,R.id.voucher_QTY_txt};
                SimpleAdapter itemsAdapter =  new SimpleAdapter(Voucher_DataEntryActivity.this, select_details,R.layout.voucher_dtl_layout, fromwhere1, viewswhere1);
                Details_LV.setAdapter(itemsAdapter);

     }});
//endregion
        //region view selected voucher serial in serial list
        Details_LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Serials_LV.setAdapter(null);
                ClearList();
                saved_serials= ItemSerial_DAO.getSavedSerials(serialnum);
                  if(saved_serials.size()>0) {
                      for (int i = 0; i < saved_serials.size(); i ++) {
                          addToList(saved_serials.get(i).getSerial());
                      }

                  }else Toast.makeText(getApplicationContext(), "No Serials found", Toast.LENGTH_SHORT).show();
                 }
            });
        //endregion
        //region view selected serial on input text view
        Serials_LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String selected = Serials_LV.getAdapter().getItem(position).toString();
//                input.setText(Serials_LV.getAdapter().getItem(position).toString()+'\n');
            }
        });
        //endregion
        //region (Edit text) read from text and inserted it  to list view.
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
//                if (s.length() > 0)
//                {
//                    char lastCharacter = s.charAt(s.length() - 1);
//                    if (lastCharacter == '\n')
//                    {
//                        String barcode = s.subSequence(0, s.length() - 1).toString();
//                        if(!input.getText().toString().isEmpty())
//                            addToList(input.getText().toString());
//                    }

//                }
            }
        });
        //endregion

        SwipeDismissListViewTouchListener touchListener =
        new SwipeDismissListViewTouchListener( Serials_LV,new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                    try {
                                       ItemSerial_DAO.deleteItemSerials(new ItemSerials(arrayadapter.getItem(position), serialnum, mid));
                                       Toast.makeText(Voucher_DataEntryActivity.this,"serial is Deleted successfully" , Toast.LENGTH_LONG).show();
//                                        else
//                                          Toast.makeText(Voucher_DataEntryActivity.this,"serial cannot Delete " , Toast.LENGTH_LONG).show();

                                    }
                                    catch (Exception ex){
                                        Toast.makeText(Voucher_DataEntryActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    newserial.remove(position);
                                    arrayadapter.notifyDataSetChanged();
                                    listcount2.setText(getlistcount(Serials_LV));//show the list count
                                }


                            }
                        });
        Serials_LV.setOnTouchListener(touchListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savemenu, menu);
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

            // add all inserted serials to VoucherSerial_list
            if (id == R.id.save_serial_btn) {
             if(SaveSerials()){
                 Toast.makeText(this, "New Serials has been Saved Successfully", Toast.LENGTH_SHORT).show();
                 Details_LV.setAdapter(null);
                 Serials_LV.setAdapter(null);
                 newserial.clear();
                 input.setText("");
                 listcount2.setText(getlistcount(Serials_LV));
             } else
                 Toast.makeText(this, "Saved Failed", Toast.LENGTH_SHORT).show();

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
            arrayadapter.notifyDataSetChanged();
            input.requestFocus();
//            input.setText(""); // Clear the input
            Serials_LV.setAdapter(arrayadapter);
            listcount2.setText(getlistcount(Serials_LV));//show the list count
            Serials_LV.setSelection(Serials_LV.getCount());// restore the position of listview
        }
    }
    public void ClearList() {

            newserial.clear();
            arrayadapter = new ArrayAdapter<>(this,R.layout.list_ltem,R.id.voucher_serials_txt,newserial);
            arrayadapter.notifyDataSetChanged();
            input.requestFocus();
            input.setText(""); // Clear the input
            Serials_LV.setAdapter(arrayadapter);
            listcount2.setText(getlistcount(Serials_LV));//show the list count
            Serials_LV.setSelection(Serials_LV.getCount());// restore the position of listview

    }
    private  boolean SaveSerials(){
        ArrayList<String> VoucherSerial_list = new ArrayList<>();
       for (int x = 0 ; x < arrayadapter.getCount() ; x ++){
           VoucherSerial_list.add(arrayadapter.getItem(x));
       }

        if (CheckDuplicates(VoucherSerial_list)){
            Toast.makeText(this,"this serial is repeated" +repeated_serial.toString(), Toast.LENGTH_LONG).show();

            return false;}
         try {
             saved_serials= ItemSerial_DAO.getSavedSerials(serialnum);
          for (int i = 0; i < arrayadapter.getCount(); i++) {
              //10- save or update Serials in Locally serial table
//              if(saved_serials.size()>0)
//              ItemSerial_DAO.updateSerials(new ItemSerials(arrayadapter.getItem(i), serialnum, mid));
//              else
               ItemSerial_DAO.addItemSerials(new ItemSerials(arrayadapter.getItem(i), serialnum, mid));
           }
//             saved_serials= ItemSerial_DAO.getSavedSerials(serialnum);
//             return (saved_serials.size() == arrayadapter.getCount());
return true;
          }
    catch (Exception ex){
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        return false;}
     }
    @Override
    public void onBackPressed() {
//        moveTaskToBack(true);
        startActivity(new Intent(Voucher_DataEntryActivity.this, MainActivity.class));
        this.finish(); // Destroy activity A and not exist in Back stack
    }

    //check if any repeated serials on serial list before savemenu locally
    boolean CheckDuplicates(final ArrayList<String> serial_list)
    {
        not_repeated_serial = new HashSet<>();
        for (String i : serial_list)
        {
            if(!not_repeated_serial.add(i))
            {
                repeated_serial.add(i);
                return true;
            }
        }
        return false;
    }
    private String getlistcount(ListView Ls){
     listitemcount= Ls.getCount();
        return listitemcount+"";
    }

    //region barcode listener not used yet
    /*
    //    @Override
//    public boolean dispatchKeyEvent(KeyEvent KeyPressd) {
////        if (KeyPressd.getCharacters() != null && !KeyPressd.getCharacters().isEmpty())
//            //Add more code...
//        KeyPressd.toString();
////        KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_BCR_SCAN_FRONT, scanCode=744, metaState=0, flags=0x8, repeatCount=0, eventTime=132032420, downTime=132032420, deviceId=5, source=0x101 }
//            return super.dispatchKeyEvent(KeyPressd);
////        else
////            return false;
//    }
    private BroadcastReceiver mResultReceiver=new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            IntentFilter intFilter=new IntentFilter(ScanManager.ACTION_SEND_SCAN_RESULT);
            registerReceiver(mResultReceiver, intFilter);
            String action=intent.getAction();
            if(ScanManager.ACTION_SEND_SCAN_RESULT.equals(action)){
                byte[] bvalue1=intent.getByteArrayExtra(ScanManager.EXTRA_SCAN_RESULT_ONE_BYTES);
//                byte[] bvalue2=intent.getByteArrayExtra(ScanManager.EXTRA_SCAN_RESULT_TWO_BYTES);
                String svalue1=null;
//                String svalue2=null;
                try {
                    if(bvalue1!=null)
                        svalue1=new String(bvalue1,"GBK");
//                    if(bvalue2!=null)
//                        svalue2=new String(bvalue1,"GBK");
                    svalue1=svalue1==null?"":svalue1;
//                    svalue2=svalue2==null?"":svalue2;
                    input.setText(svalue1);
                } catch (Exception e) {
                    e.printStackTrace();
                    input.setText("data encode failed.");
                }

                Random random = new Random();
                input.setTextColor(Color.argb(255, random.nextInt(256),
                        random.nextInt(256), random.nextInt(256)));
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }
    private void registerReceiver()
    {
        IntentFilter intFilter=new IntentFilter(ScanManager.ACTION_SEND_SCAN_RESULT);
        registerReceiver(mResultReceiver, intFilter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unRegisterReceiver();
    }
    private void unRegisterReceiver()
    {
        try {
            unregisterReceiver(mResultReceiver);
        } catch (Exception e) {
        }
    }
*/
    //endregion

}
