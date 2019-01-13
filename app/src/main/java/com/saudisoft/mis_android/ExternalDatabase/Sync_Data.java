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

import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Taha mosaad on {6/10/2018}.
 */

public class Sync_Data {
    private String save_MSG = "";
    private  String DBNAME;
    private String Branchcode;
    private String DBserver;
    private String MISUser;
    private ItemInOutH_DAO ItemHeader_DAO;
    private ItemsInOutL_DAO ItemDetail_DAO;
    private ItemsSerials_DAO ItemSerial_DAO;
    private final String DEFAULT = "N/A";
    List<Map<String,String>> item_serials;
    private CRUD_Operations new_data;
    private Context context;
    public Sync_Data(String DBNAME, String branchcode, String DBserver,Context context) {
        this.DBNAME = DBNAME;
        this.Branchcode = branchcode;
        this.DBserver = DBserver;
        this.context=context;
        this.ItemHeader_DAO = new ItemInOutH_DAO(context);
        this.ItemDetail_DAO = new ItemsInOutL_DAO(context);
        this.ItemSerial_DAO = new ItemsSerials_DAO(context);
    }
    private void init_Data()
    {
        //load data from saved shared preferences to this activity
        SharedPreferences get = context.getSharedPreferences( "User_data",MODE_PRIVATE );
        MISUser= get.getString("name",DEFAULT) ;


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
    public void sync_data(){
        init_Data();
        List<ItemsInOutH> headerList = this.ItemHeader_DAO.getAllItemheader();
        if(headerList.size()>0) {
            here:   for (ItemsInOutH hdr : headerList)
            {
                List<Map<String, String>> headerList2 = this.ItemHeader_DAO.getItemHeader(hdr.getSerial());

                for (Map<String ,String > hdr2 : headerList2){
                    if (!send_serials(hdr2))
                    { if(SaveDialog((new_data.Sql_MSG != null ? new_data.Sql_MSG : ".") + "\n" +" Next Voucher ?",context))
                    {   SaveLog(new_data.Sql_MSG,false);
                        continue here;
                    }
                       else {  SaveLog(new_data.Sql_MSG,false);
                        break here;
                    }
                    }
                    else {
                        SaveLog("Voucher"+hdr.getSerial()+" Sent",true);
                        deleteallvoucher(hdr.getSerial()); continue here;

                    }
                }
            }
        }
 else SaveLog("No Voucher Founded \n Please Import Voucher first",false);
    }

    private List<Map<String,String>> getItemDetails(String serial){
        return ItemDetail_DAO.getItemDetails(serial);
    }
    private boolean getItemSerial(){
//        ArrayList<String> newserial = new ArrayList<>();
//        saved_serials = ItemSerial_DAO.getItemSerials(mid);
//        if(saved_serials.size()>0){
////            if(saved_serials.size()!=Integer.valueOf(mQTY))
////            {save_MSG=save_MSG+"Voucher :"+serialnum+"items QTY ="+total_QTY+"founded "+saved_serials.size()+ "\n";}
//            newserial.clear();
//            for (Map<String,String> srl: saved_serials) {
//                newserial.add( srl.get("SerialNum"));
//            }
//            return true;
//
//        }else
            return false;
    }

    private boolean  send_serials(Map<String ,String > headerList){
        new_data = new CRUD_Operations(DBNAME,DBserver);

        return(new_data.InsertNewSerials(headerList,Branchcode, MISUser,context));

    }
    private void deleteallvoucher(String VoucherNum){

        for(Map<String,String> dtl : getItemDetails(VoucherNum))
            {
                String mid = dtl.get("ID");
                String itemcode = dtl.get("ItemCode");
                ItemDetail_DAO.deleteItemDetails(new ItemsInOutL(mid, "", "", 0, itemcode, VoucherNum));
                List<Map<String, String>> saved_serials = ItemSerial_DAO.getItemSerials(mid);
                for (Map<String,String> srl : saved_serials) {
                    if (saved_serials.size()>0)
                    ItemSerial_DAO.deleteItemSerials(new ItemSerials(srl.get("SerialNum"), VoucherNum, mid));
                }

            }
//        headerList2 = getItemHeader(VoucherNum);
        this.ItemHeader_DAO.deleteItemsInOutH(new ItemsInOutH(VoucherNum,"","","","",0));



    }
    public int ItemHeaderCount() {
        List<ItemsInOutH> headerlist;
        headerlist = this.ItemHeader_DAO.getAllItemheader();
        if (headerlist.size() > 0 )
            return headerlist.size();
        else return 0;
    }
    public int ItemDetailCount() {
        List<Map<String,String >> Detiallist;
        Detiallist = this.ItemDetail_DAO.GetAllItemDetails();

        if (Detiallist.size() > 0 )
            return Detiallist.size();
        else return 0;
    }
    public int ItemSerialCount() {
        int total_QTY = 0;
        List<Map<String,String >> Detiallist;
        Detiallist = this.ItemDetail_DAO.GetAllItemDetails();
        for (Map<String,String > dtl:Detiallist) {
            total_QTY += Integer.valueOf( dtl.get("QTY"));
        }
        return total_QTY;
    }



    private boolean mResult;
    private boolean SaveDialog(String message, Context context) {
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
    private   void SaveLog(String save_MSG,boolean isSaved)
    {
        SharedPreferences sharedPreferences = context. getSharedPreferences( "User_data", Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String log = ((sharedPreferences.getString("sendmsg","").isEmpty()) ? save_MSG : save_MSG+"\n"+sharedPreferences.getString("sendmsg",""));

        if (isSaved)
        {
            editor.putString("sendmsg", log);
            int sentcount = Integer.valueOf(sharedPreferences.getString("sendcount", "0")) + 1;
            editor.putString("sendcount", sentcount +"");
        }else
        editor.putString("sendmsg",log );

//         editor.commit();
        editor.apply();
    }
    public boolean isAlertDialogShowing(AlertDialog thisAlertDialog) {
        return thisAlertDialog != null && thisAlertDialog.isShowing();
    }

}
