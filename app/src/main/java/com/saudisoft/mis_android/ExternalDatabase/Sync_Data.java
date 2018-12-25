package com.saudisoft.mis_android.ExternalDatabase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.saudisoft.mis_android.DAO.ItemInOutH_DAO;
import com.saudisoft.mis_android.DAO.ItemsInOutL_DAO;
import com.saudisoft.mis_android.DAO.ItemsSerials_DAO;
import com.saudisoft.mis_android.Model.ItemSerials;
import com.saudisoft.mis_android.Model.ItemsInOutH;
import com.saudisoft.mis_android.Model.ItemsInOutL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Taha mosaad on {6/10/2018}.
 */

public class Sync_Data {
    public String  TType,TDate,Tnum,Tcode,mNote,mIsnew,mSerial,mQTY;
    private int total_QTY;
    private String save_MSG = "";
    private  String DBNAME,Branchcode,DBserver,serialnum,mid,itemcode, MISUser;
    private ItemInOutH_DAO ItemHeader_DAO;
    private ItemsInOutL_DAO ItemDetail_DAO;
    private ItemsSerials_DAO ItemSerial_DAO;
    private final String DEFAULT = "N/A";
    List<Map<String,String>> item_serials;
    //    public boolean confirm =false;
    private List<ItemsInOutH> headerList;
    private List<Map<String, String>> headerList2;
    private List<Map<String,String>> select_details;
    private List<Map<String,String>> saved_serials;
    private CRUD_Operations new_data;
    private ArrayList<String> newserial = new ArrayList<>();
    // variable to hold context
    private Context context;
    public Sync_Data(String DBNAME, String branchcode, String DBserver,Context context) {
        this.DBNAME = DBNAME;
        this.Branchcode = branchcode;
        this.DBserver = DBserver;
        this.context=context;
    }
    private void init_Data()
    {
        //load data from saved shared preferences to this activity
        SharedPreferences get = context.getSharedPreferences( "User_data",MODE_PRIVATE );
        MISUser= get.getString("name",DEFAULT) ;

        this.ItemHeader_DAO = new ItemInOutH_DAO(context);
        this.ItemDetail_DAO = new ItemsInOutL_DAO(context);
        this.ItemSerial_DAO = new ItemsSerials_DAO(context);
    }
    //    private  String SaveData(){
//        init_Data();
//        headerList =this.ItemHeader_DAO.getAllItemheader();
//        skipvoucher:  for (ItemsInOutH hdr:headerList) {
//            headerList2 = ItemHeader_DAO.getItemHeader(hdr.getSerial());
//            mSerial = headerList2.get(0).get("item_serial");
//            TType = headerList2.get(0).get("trans_type");
//            TDate = headerList2.get(0).get("trans_date");
//            Tnum=headerList2.get(0).get("trans_num");
//            mNote = headerList2.get(0).get("Note");
//            mIsnew = headerList2.get(0).get("is_new");
//            Tcode = headerList2.get(0).get("TransCode");
//            getItemDetails(hdr.getSerial());
//            if (getItemSerial()){
//                if (send_serials())
//                    save_MSG=save_MSG + "Voucher Serials Saved Successfully\n";
//                else {
//                    save_MSG = save_MSG + new_data.Sql_MSG + "\n";
//                    break;
//                }
//
//            }
////            for(Map<String,String> dtl : getItemDetails(hdr.getSerial()))
////            {
////                 itemcode =dtl.get("ItemCode");
////                serialnum =dtl.get("Serial");
////            }
//        }return save_MSG;
//    }
    private void getData(){
        init_Data();
        headerList =this.ItemHeader_DAO.getAllItemheader();
        if(headerList.size()>0) {
            here:   for (ItemsInOutH hdr : headerList)
            {
                headerList2 = this.ItemHeader_DAO.getItemHeader(hdr.getSerial());

                 for (Map<String ,String > hdr2 : headerList2){
                    if (!send_serials(hdr2))
                    { if(SaveDialog((new_data.Sql_MSG != null ? new_data.Sql_MSG : ".") + "\n" +" Next Voucher ?",context))
                            continue here;
                        else break here;
                    }
                    else {save_MSG = "Vouchers Saved";
                        deleteallvoucher(hdr.getSerial());break here;
                    }
                }
            }
        }
 else save_MSG = "No Voucher Founded \n Please Import Voucher first";
    }

    private List<Map<String,String>> getItemDetails(String serial){
        select_details= ItemDetail_DAO.getItemDetails(serial);
//        mQTY=select_details.get(0).get("QTY");
//        total_QTY+=Integer.valueOf(mQTY);
//        for (String  Key : select_details.get(0).keySet()) {
//            total_QTY +=  Integer.valueOf(select_details.get(0).get("QTY"));
//        }
        return select_details;
    }
    private boolean getItemSerial(){
        saved_serials = ItemSerial_DAO.getItemSerials(mid);
        if(saved_serials.size()>0){
//            if(saved_serials.size()!=Integer.valueOf(mQTY))
//            {save_MSG=save_MSG+"Voucher :"+serialnum+"items QTY ="+total_QTY+"founded "+saved_serials.size()+ "\n";}
            newserial.clear();
            for (Map<String,String> srl: saved_serials) {
                newserial.add( srl.get("SerialNum"));
            }
            return true;

        }else return false;
    }
    public   String    sync_data() {

        getData();
        return save_MSG ;
    }
    private boolean  send_serials(Map<String ,String > headerList){
        new_data = new CRUD_Operations(DBNAME,DBserver);

        return(new_data.InsertNewSerials(headerList,Branchcode, MISUser,context));

    }
    private void deleteallvoucher(String VoucherNum){

        for(Map<String,String> dtl : getItemDetails(VoucherNum))
            {
                mid =   dtl.get("ID");
                itemcode =dtl.get("ItemCode");
                ItemDetail_DAO.deleteItemDetails(new ItemsInOutL(mid, "", "", 0, itemcode, VoucherNum));
                saved_serials = ItemSerial_DAO.getItemSerials(mid);
                for (Map<String,String> srl : saved_serials) {
                    if (saved_serials.size()>0)
                    ItemSerial_DAO.deleteItemSerials(new ItemSerials(srl.get("SerialNum"), VoucherNum, mid));
                }

            }
//        headerList2 = getItemHeader(VoucherNum);
        this.ItemHeader_DAO.deleteItemsInOutH(new ItemsInOutH(VoucherNum,"","","","",0));



    }
    private List<Map<String, String>> getItemHeader(String serial) {
        List<Map<String, String>> headerlist;

        headerlist = this.ItemHeader_DAO.getItemHeader(serial);
//        if (headerlist.size() > 0)
            return headerlist;
//        else return null;
    }

    public int sumqty(Map<String,String> dtl){
        for (String key: dtl.keySet())
            total_QTY += Integer.valueOf( dtl.get("QTY"));
        return total_QTY;
    }


    public boolean isAlertDialogShowing(AlertDialog thisAlertDialog) {
        return thisAlertDialog != null && thisAlertDialog.isShowing();
    }
    private boolean mResult;
    public boolean SaveDialog( String message, Context context) {
        String title="";
//         make a handler that throws a runtime exception when a message is received
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                throw new RuntimeException();
            }
        };

        // make a text input dialog and show it
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mResult = true;

                handler.sendMessage(handler.obtainMessage());
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mResult = false;
                handler.sendMessage(handler.obtainMessage());
            }
        });
        alert.show();

        // loop till a runtime exception is triggered.
        try { Looper.loop(); }
        catch(RuntimeException e2) {e2.getMessage();}

        return mResult;
    }
}
