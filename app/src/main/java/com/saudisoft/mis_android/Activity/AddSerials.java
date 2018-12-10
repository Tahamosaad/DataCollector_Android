package com.saudisoft.mis_android.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nlscan.android.scan.ScanManager;
import com.nlscan.android.scan.ScanSettings;
import com.saudisoft.mis_android.R;

import java.util.ArrayList;
import java.util.Collections;

public class AddSerials extends AppCompatActivity {

//	private TextView tv_broadcast_result;
	private EditText scanner_input;
//	private RadioGroup radio_group;
	private FloatingActionButton btn_reset;
//	private String[] current_serial;
	ArrayList current_serial = new ArrayList<>();
	ArrayList current_serial2 = new ArrayList<>();

	private ScanManager mScanMgr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_serials);
		mScanMgr= ScanManager.getInstance();
		initView();
		if(getIntent().getStringArrayListExtra("CurrentSerials") !=null)
		{
			current_serial = getIntent().getStringArrayListExtra("CurrentSerials");
			StringBuilder builder = new StringBuilder();
			for (Object details : current_serial) {
				builder.append(details).append("\n");
			}
				scanner_input.setText(builder.toString());

		}
	}
	
	private void initView()
	{
//		tv_broadcast_result=(TextView)findViewById(R.id.tv_broadcast_result);
		scanner_input =(EditText)findViewById(R.id.et_emulator_result);
//		radio_group=(RadioGroup)findViewById(R.id.radio_group);
		//region disable view keybad on activity
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			scanner_input.setShowSoftInputOnFocus(false);
		else
			scanner_input.setTextIsSelectable(true);
		//endregion
		btn_reset=(FloatingActionButton) findViewById(R.id.btn_reset);

		btn_reset.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				scanner_input.setText("");
			}
		});
		
		//Optional output mode settings
		int outputMode= ScanSettings.Global.VALUE_OUT_PUT_MODE_EMULATE_KEY;
		mScanMgr.setOutpuMode(outputMode);

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.savebtn, menu);
		return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if(!scanner_input.getText().equals(null)&&!scanner_input.getText().toString().isEmpty()) {

			// add all inserted serials to VoucherSerial_list
			if (id == R.id.add_serial_btn) {
				Collections.addAll(current_serial2, scanner_input.getText().toString().split("\\r?\\n"));
				Intent i=new Intent(AddSerials.this, Voucher_DataEntryActivity.class);
				i.putExtra("Serials", current_serial2);
				startActivity(i);
				AddSerials.this.finish(); // Destroy activity A and not exist in Back stack
			}
		} else
			Toast.makeText(this, "Please Enter Serial", Toast.LENGTH_SHORT).show();
		return super.onOptionsItemSelected(item);
	}

	private void registerReceiver()
	{
		IntentFilter intFilter=new IntentFilter(ScanManager.ACTION_SEND_SCAN_RESULT);
		registerReceiver(mResultReceiver, intFilter);
	}
	
	private void unRegisterReceiver()
	{
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






















