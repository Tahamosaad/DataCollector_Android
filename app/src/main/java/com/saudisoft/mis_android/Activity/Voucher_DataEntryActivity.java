package com.saudisoft.mis_android.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.saudisoft.mis_android.adapter.SwipeDismissListViewTouchListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Voucher_DataEntryActivity extends AppCompatActivity {
    public static final String TAG = "Voucher_DataEntry";
    private final String DEFAULT = "N/A";
    public ListView Header_LV,Details_LV,Serials_LV;
    ArrayAdapter<String> arrayadapter;
    String serialnum,mid,MISUser,mQty;
    Set<String> not_repeated_serial;
    ArrayList<String> repeated_serial= new ArrayList<>();
    List<ItemsInOutH> headerList;
    List<ItemSerials> saved_serials;
//    List<Map<String,String>> item_serials;
//     boolean result;
    FloatingActionButton btn_add_serial;
    EditText scanner_input;
    int listitemcount;
    TextView listcount,listcount2,tv_count;
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
        listcount2 = (TextView) findViewById(R.id.TV_count3);
        tv_count=(TextView)findViewById(R.id.tv_count1);
        btn_add_serial = (FloatingActionButton) findViewById(R.id.btn_save_srl);
        scanner_input = (EditText) findViewById(R.id.txt_serial);

        //load data from saved shared preferences to this activity
        SharedPreferences UN = getSharedPreferences( "User_data",MODE_PRIVATE );
        MISUser= UN.getString("name",DEFAULT) ;
//        SharedPreferences VD = getSharedPreferences( "voucher_data",MODE_PRIVATE );
//        serialnum=VD.getString("Vserial",DEFAULT);
//        mid=VD.getString("Vid",DEFAULT);
//        if(serialnum!=null&&mid !=null){
//            select_details= ItemDetail_DAO.getItemDetails(serialnum);
//            String[] fromwhere1 = {"ID","Serial","PartNo","Itemname","ItemCode","QTY"};
//            int[] viewswhere1 = { R.id.voucher_ID_txt, R.id.voucher_serial1_txt,R.id.Part_No_txt,R.id.Item_Name_txt,R.id.Item_Code_txt,R.id.voucher_QTY_txt};
//            SimpleAdapter itemsAdapter =  new SimpleAdapter(Voucher_DataEntryActivity.this, select_details,R.layout.voucher_dtl_layout, fromwhere1, viewswhere1);
//            Details_LV.setAdapter(itemsAdapter);
//        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_dataentry);
       //1. initilize all views
        initViews();
        // Get HeaderList from Intent
        //2. get (selected vouchers Header) from previous activity or locally

        if( ItemHeader_DAO.getAllItemheader() !=null)
            headerList =ItemHeader_DAO.getAllItemheader();
        else
            Toast.makeText(getApplicationContext(), "No Voucher Selected", Toast.LENGTH_SHORT).show();
//     headerList = (List<ItemsInOutH>) getIntent().getSerializableExtra("Header");
        adapter = new GridListAdapter(Voucher_DataEntryActivity.this, headerList, false );
        Header_LV.setAdapter(adapter);
        listcount =(TextView) findViewById(R.id.TV_count1) ;
        listcount.setText(getlistcount(Header_LV));
    ///*****************************************************************************
       //region load data from add serial activity
        if(getIntent().getStringArrayListExtra("Serials") !=null)
        {
        newserial = getIntent().getStringArrayListExtra("Serials");
        arrayadapter = new ArrayAdapter<>(this,R.layout.list_ltem,R.id.voucher_serials_txt,newserial);
        arrayadapter.notifyDataSetChanged();
        Serials_LV.setAdapter(arrayadapter);
        listcount2.setText(getlistcount(Serials_LV));//show the list count
        Serials_LV.setSelection(Serials_LV.getCount());// restore the position of listview
        }
       //endregion
        //region disable view keybad on activity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            scanner_input.setShowSoftInputOnFocus(false);
        else
            scanner_input.setTextIsSelectable(true);
//		//endregion
      //region  add new serials button
//        Add_serial.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(serialnum!=null&& mQty!= null){
//                SharedPreferences sharedPreferences = getSharedPreferences( "voucher_data", Context.MODE_PRIVATE );
//                //edit my data which i get
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                //put string take two value(Key ,value)
//                editor.putString("Vserial",serialnum );
//                editor.putString( "Vid",mid);
//                editor.putString("Vqty",mQty);
//                editor.apply();
//                Intent i=new Intent(Voucher_DataEntryActivity.this, AddSerials.class);
//                Voucher_DataEntryActivity.this.finish();
//                //send all serial list to serial entry Activity to view it
//                i.putExtra("CurrentSerials",newserial);
//                startActivity(i);}
//                else Toast.makeText(Voucher_DataEntryActivity.this,"Please Select Any voucher", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        endregion
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
//                mid = select_details.get(position).get("ID");

                String[] fromwhere1 = {"ID","Serial","PartNo","Itemname","ItemCode","QTY"};
                int[] viewswhere1 = { R.id.voucher_ID_txt, R.id.voucher_serial1_txt,R.id.Part_No_txt,R.id.Item_Name_txt,R.id.Item_Code_txt,R.id.voucher_QTY_txt};
                SimpleAdapter itemsAdapter =  new SimpleAdapter(Voucher_DataEntryActivity.this, select_details,R.layout.voucher_dtl_layout, fromwhere1, viewswhere1);
                Details_LV.setAdapter(itemsAdapter);
                view.setSelected(true);
     }});
//endregion
        //region view selected voucher serial in serial list
        Details_LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Serials_LV.setAdapter(null);
                ClearList();
                mid = select_details.get(position).get("ID");
                mQty= select_details.get(position).get("QTY");
//                item_serials= ItemSerial_DAO.getItemSerials(mid);
                saved_serials= ItemSerial_DAO.getSavedSerials(mid);
                  if(saved_serials.size()>0) {
                      for (int i = 0; i < saved_serials.size(); i ++) {
                          addToList(saved_serials.get(i).getSerial());
                      }

                  }else Toast.makeText(getApplicationContext(), "No Serials found", Toast.LENGTH_SHORT).show();
                view.setSelected(true);
                tv_count.setText(mQty);
                view.setSelected(true);

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
        btn_add_serial.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (Details_LV.getCount()<1) {
                    Toast.makeText(Voucher_DataEntryActivity.this, "Please Select Voucher Details", Toast.LENGTH_SHORT).show();

                }
                if(Serials_LV.getCount()>0) {
                    // add all inserted serials to VoucherSerial_list

                        SaveDialog();


                } else Toast.makeText(Voucher_DataEntryActivity.this, "Please Enter Serial", Toast.LENGTH_SHORT).show();

            }
        });
//		region (Edit text) read from text and inserted it  to list view.
        scanner_input.addTextChangedListener(new TextWatcher() {
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
                if(!s.toString().isEmpty())
                {	char lastCharacter = s.charAt(s.length() - 1);
                    if (lastCharacter == '\n' || lastCharacter == '\r')
                    {
                        String barcode ;
                        barcode=s.toString().replaceAll("(\\r|\\n|\\t)", "");
                        if( Integer.valueOf(getlistcount(Serials_LV))< Integer.valueOf(mQty) )
                        {
                            if (checkrepet(barcode)){
                                if (barcode.length() > 0)
                                {
//							char lastCharacter = s.charAt(s.length() - 1);
//							if (lastCharacter == '\n')
//							{
//                                  barcode = s.subSequence(0, s.length() - 1).toString();

                                    addToList(barcode);
//								    tv_count1.setText(scanner_input.getLineCount()+"");
                                }
//							}
                            }else	{
                                Toast.makeText(Voucher_DataEntryActivity.this, "Serial :"+s.toString() +"Repeated", Toast.LENGTH_SHORT).show();
                                scanner_input.getText().clear();
                            }

                        }else	{
                            Toast.makeText(Voucher_DataEntryActivity.this, "Max Serial Is :"+mQty, Toast.LENGTH_SHORT).show();
                            scanner_input.getText().clear();
                        }
                    }
                }}
        });
//		endregion
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.savemenu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//    // handle add serials button activities
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (Details_LV.getCount()<1) {
//            Toast.makeText(this, "Please Select Voucher Details", Toast.LENGTH_SHORT).show();
//            return super.onOptionsItemSelected(item);
//        }
//        if(Serials_LV.getCount()>0) {
//            // add all inserted serials to VoucherSerial_list
//            if (id == R.id.save_serial_btn) {
//                SaveDialog();
//            }
//
//        } else Toast.makeText(this, "Please Enter Serial", Toast.LENGTH_SHORT).show();
//            return super.onOptionsItemSelected(item);
//    }
// add to serial list new item
    public void addToList(String barcode) {
        if(!barcode.isEmpty()){
            newserial.add(barcode);
            arrayadapter = new ArrayAdapter<>(this,R.layout.list_ltem,R.id.voucher_serials_txt,newserial);
            arrayadapter.notifyDataSetChanged();
            Serials_LV.setAdapter(arrayadapter);
            listcount2.setText(getlistcount(Serials_LV));//show the list count
            Serials_LV.setSelection(Serials_LV.getCount());// restore the position of listview
        }
        scanner_input.getText().clear();
    }

    private  void SaveDialog() {
        if(arrayadapter.getCount()< Integer.valueOf(mQty)){
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                //set icon
                .setIcon(R.drawable.ic_alert)
                //set title
                .setTitle("Validation Alert ")
                //set message
                .setMessage("Serials Number are less than the QTY of voucher details \n Save any way ? ")
                //set positive button
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        //10- save or update Serials in Locally serial table
                        if (SaveSerials()) {
                            Toast.makeText(Voucher_DataEntryActivity.this, "New Serials has been Saved Successfully", Toast.LENGTH_SHORT).show();
                            ClearList();
                        } else
                            Toast.makeText(Voucher_DataEntryActivity.this, "Saved Failed", Toast.LENGTH_SHORT).show();
                    }
                })
                //set negative button
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                        //Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                    }
                }).show();

        }
        else
        {
            if(SaveSerials())
            {
                Toast.makeText(Voucher_DataEntryActivity. this, "New Serials has been Saved Successfully", Toast.LENGTH_SHORT).show();
                ClearList();
            } else Toast.makeText(Voucher_DataEntryActivity.this, "Saved Failed", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
//        moveTaskToBack(true);
        deleteSharedPref();
        startActivity(new Intent(Voucher_DataEntryActivity.this, MainActivity.class));
        this.finish(); // Destroy activity A and not exist in Back stack
    }
    private boolean SaveSerials(){
        try{
        for (int i = 0; i < arrayadapter.getCount(); i++) {
            ItemSerial_DAO.addItemSerials(new ItemSerials(serialnum,arrayadapter.getItem(i),  mid));
        }
        saved_serials= ItemSerial_DAO.getSavedSerials(mid);
        return saved_serials.size()>0;
    }catch (Exception ex){Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        return false;}
    }
    private void deleteSharedPref(){
//        SharedPreferences sharedPreferences = getSharedPreferences( "voucher_data", Context.MODE_PRIVATE );
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.remove("Vserial");
//        editor.remove("Vid");
//        editor.apply();
        PreferenceManager.getDefaultSharedPreferences(Voucher_DataEntryActivity.this).edit().clear().apply();
    }
    //check if any repeated serials on serial list before savemenu locally

    private boolean checkrepet(String serial){

        for (String i : newserial)
        {
            if (i.equalsIgnoreCase(serial)) return false;
        }
        return true;
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
        listcount2.setText(getlistcount(Serials_LV));//show the list count
        Serials_LV.setSelection(Serials_LV.getCount());// restore the position of listview

    }
    boolean CheckDuplicates(final ArrayList<String> serial_list) {
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
    private void createAndShowDialog( )
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(Voucher_DataEntryActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        // Inflate and set the layout for the dialog
        View v = inflater.inflate( R.layout.login_dialog, null );
        // Pass null as the parent view because its going in the dialog layout
        v.findViewById( R.id.panel );
        builder.setView( v );

        // Add the buttons
        builder.setPositiveButton( R.string.login, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked login button

                    Toast.makeText(Voucher_DataEntryActivity.this,"Incorrect user name or password !", Toast.LENGTH_SHORT).show();
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
    public boolean isAlertDialogShowing(AlertDialog thisAlertDialog) {
        return thisAlertDialog != null && thisAlertDialog.isShowing();
    }
}
