package com.saudisoft.mis_android.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nlscan.android.scan.ScanManager;
import com.nlscan.android.scan.ScanSettings;
import com.saudisoft.mis_android.R;
import com.saudisoft.mis_android.adapter.SwipeDismissListViewTouchListener;

import java.util.ArrayList;

public class AddSerials extends AppCompatActivity {
	private final String DEFAULT = "0";
	private TextView tv_count1,tv_count2;
	private EditText scanner_input;
	ArrayAdapter<String> arrayadapter;
	public ListView Serials_LV;
	int listitemcount;
	ArrayList<String> newserial = new ArrayList<>();
//	private RadioGroup radio_group;
	private FloatingActionButton btn_reset;
	private FloatingActionButton btn_add;
//	private String[] current_serial;
	ArrayList<String>current_serial = new ArrayList<>();
	ArrayList<String> current_serial2 = new ArrayList<>();
	private String mQty;
	private ScanManager mScanMgr;
	private void initView()
	{
		btn_reset=(FloatingActionButton) findViewById(R.id.btn_reset);
		btn_add=(FloatingActionButton) findViewById(R.id.btn_addsrl);
		tv_count1=(TextView)findViewById(R.id.tv_count1);
		tv_count2=(TextView)findViewById(R.id.tv_count6);
		scanner_input =(EditText)findViewById(R.id.et_emulator_result);
		Serials_LV = (ListView) findViewById(R.id.serial_lst);

//		radio_group=(RadioGroup)findViewById(R.id.radio_group);

		//Optional output mode settings
		int outputMode= ScanSettings.Global.VALUE_OUT_PUT_MODE_EMULATE_KEY;
		mScanMgr.setOutpuMode(outputMode);

		SharedPreferences VD = getSharedPreferences( "voucher_data",MODE_PRIVATE );
		mQty=VD.getString("Vqty",DEFAULT);
//		tv_count1.setText(scanner_input.getLineCount()+"");
		tv_count2.setText(mQty);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_serials);
		mScanMgr= ScanManager.getInstance();
		initView();
		//		region disable view keybad on activity
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			scanner_input.setShowSoftInputOnFocus(false);
		else
			scanner_input.setTextIsSelectable(true);
//		endregion
		if(getIntent().getStringArrayListExtra("CurrentSerials") !=null)
		{
			current_serial = getIntent().getStringArrayListExtra("CurrentSerials");
//			StringBuilder builder = new StringBuilder();
			for (String details : current_serial) {
				addToList(details);
			}
//				scanner_input.setText(builder.toString());
//			tv_count1.setText(current_serial.size()+"");
		}
		SwipeDismissListViewTouchListener touchListener =
				new SwipeDismissListViewTouchListener( Serials_LV,new SwipeDismissListViewTouchListener.DismissCallbacks() {
					@Override
					public boolean canDismiss(int position) {
						return true;
					}

					@Override
					public void onDismiss(ListView listView, int[] reverseSortedPositions) {
						for (int position : reverseSortedPositions) {
							newserial.remove(position);
							arrayadapter.notifyDataSetChanged();
							tv_count1.setText(getlistcount(Serials_LV));//show the list count
							Toast.makeText(AddSerials. this,"serial is Deleted successfully" , Toast.LENGTH_LONG).show();
//
						}
					}
				});
		Serials_LV.setOnTouchListener(touchListener);
		btn_add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if (newserial.size()!=0){
					// add all inserted serials to VoucherSerial_list

					//				Collections.addAll(current_serial2, scanner_input.getText().toString().split("\\r?\\n"));
					Intent i=new Intent(AddSerials.this, Voucher_DataEntryActivity.class);
					i.putExtra("Serials", newserial);
					startActivity(i);
					AddSerials.this.finish(); // Destroy activity A and not exist in Back stack

				} else
					Toast.makeText(AddSerials.this, "Please Enter Serial", Toast.LENGTH_SHORT).show();
			}
		});
		btn_reset.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				ClearList();

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
							//barcode = s.subSequence(0, s.length() - 1).toString();

										addToList(barcode);
//								tv_count1.setText(scanner_input.getLineCount()+"");
						}
//							}
						}else	{
						Toast.makeText(AddSerials.this, "Serial :"+s.toString() +"Repeated", Toast.LENGTH_SHORT).show();
						scanner_input.setText("");
					}

						}else	Toast.makeText(AddSerials.this, "Max Serial Is :"+mQty, Toast.LENGTH_SHORT).show();
			}
			}}
		});
//		endregion
	}
	private boolean checkrepet(String serial){

		for (String i : newserial)
		{
			if (i.equalsIgnoreCase(serial)) return false;
		}
			return true;
	}
	public void addToList(String barcode) {
		if(!barcode.isEmpty()){
			newserial.add(barcode);
			arrayadapter = new ArrayAdapter<>(this,R.layout.list_ltem,R.id.voucher_serials_txt,newserial);
			arrayadapter.notifyDataSetChanged();
			Serials_LV.setAdapter(arrayadapter);
			tv_count1.setText(getlistcount(Serials_LV));//show the list count
			Serials_LV.setSelection(Serials_LV.getCount());// restore the position of listview
			scanner_input.setText("");
		}
//		scanner_input.requestFocus();
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
		tv_count1.setText(getlistcount(Serials_LV));//show the list count
		Serials_LV.setSelection(Serials_LV.getCount());// restore the position of listview
		scanner_input.requestFocus();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.savebtn, menu);
		return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item) {
//		int id = item.getItemId();
//
////		if(!scanner_input.getText().equals(null)&&!scanner_input.getText().toString().isEmpty()) {
//		if (newserial.size()!=0){
//			// add all inserted serials to VoucherSerial_list
//			if (id == R.id.add_serial_btn) {
////				Collections.addAll(current_serial2, scanner_input.getText().toString().split("\\r?\\n"));
//				Intent i=new Intent(AddSerials.this, Voucher_DataEntryActivity.class);
//				i.putExtra("Serials", newserial);
//				startActivity(i);
//				AddSerials.this.finish(); // Destroy activity A and not exist in Back stack
//			}
//		} else
//			Toast.makeText(this, "Please Enter Serial", Toast.LENGTH_SHORT).show();
		return super.onOptionsItemSelected(item);
	}

	private void registerReceiver() {
		IntentFilter intFilter=new IntentFilter(ScanManager.ACTION_SEND_SCAN_RESULT);
		registerReceiver(mResultReceiver, intFilter);
	}
	
	private void unRegisterReceiver() {
		try {unregisterReceiver(mResultReceiver);} catch (Exception e) {e.getMessage();}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unRegisterReceiver();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver();
	}

	public void onBackPressed() {
//        moveTaskToBack(true);
			startActivity(new Intent(AddSerials.this, Voucher_DataEntryActivity.class));
		AddSerials.this.finish(); // Destroy activity A and not exist in Back stack

	}
	/**
	 * Monitor the broadcast of the scan code data,
	 * and use this method to obtain the scan code data when setting
	 * the broadcast output.
	 *
	 */
	private BroadcastReceiver mResultReceiver=new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
//			mResultReceiver.getResultData();
//			String action=intent.getAction();
//			if(ScanManager.ACTION_SEND_SCAN_RESULT.equals(action)){
////				byte[] bvalue1=intent.getByteArrayExtra(ScanManager.EXTRA_SCAN_RESULT_ONE_BYTES);
////				byte[] bvalue2=intent.getByteArrayExtra(ScanManager.EXTRA_SCAN_RESULT_TWO_BYTES);
////				String svalue1=null;
////				String svalue2=null;
//				try {
////					if(bvalue1!=null)
////						svalue1=new String(bvalue1,"GBK");
////					if(bvalue2!=null)
////						svalue2=new String(bvalue1,"GBK");
////					svalue1=svalue1==null?"":svalue1;
////					svalue2=svalue2==null?"":svalue2;
////					tv_broadcast_result.setText(svalue1+"\n");
//				} catch (Exception e) {
//					e.printStackTrace();
////					tv_broadcast_result.setText("data encode failed.");
//				}
//

//			}
		}
	};
	
}






















