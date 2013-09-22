package in.sumeetkumar.blescanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.R.array;
import android.R.integer;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final int REQUEST_ENABLE_BT = 1;

	ListView listDevicesFound;
	Button btnScanDevice;
	TextView stateBluetooth;
	BluetoothAdapter bluetoothAdapter;

	// Stops scanning after 1 seconds.
	private static final long SCAN_PERIOD = 10000;
	private boolean mScanning = false;
	private Handler mHandler;
	private boolean enable;
	private HashMap<String, BLETagData> bleTagsData;

	ArrayAdapter<String> btArrayAdapter;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnScanDevice = (Button)findViewById(R.id.buttonScan);

		stateBluetooth = (TextView)findViewById(R.id.bluetoothstate);
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		listDevicesFound = (ListView)findViewById(R.id.listDevicesFound);
		bleTagsData = new HashMap<String, BLETagData>();

		btArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);
		listDevicesFound.setAdapter(btArrayAdapter);

		mHandler = new Handler();
		CheckBlueToothState();

		btnScanDevice.setOnClickListener(btnScanDeviceOnClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void CheckBlueToothState(){
		if (bluetoothAdapter == null){
			stateBluetooth.setText("Bluetooth NOT support");
		}else{
			if (bluetoothAdapter.isEnabled()){
				if(bluetoothAdapter.isDiscovering()){
					stateBluetooth.setText("Bluetooth is currently in device discovery process.");
				}else{
					stateBluetooth.setText("Bluetooth is Enabled.");
					btnScanDevice.setEnabled(true);
				}
			}else{
				stateBluetooth.setText("Bluetooth is NOT Enabled!");
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
	}

	private Button.OnClickListener btnScanDeviceOnClickListener
	= new Button.OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			btArrayAdapter.clear();
			enable = !enable;
			scanLeDevice(true);
		}};

		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			// TODO Auto-generated method stub
			if(requestCode == REQUEST_ENABLE_BT){
				CheckBlueToothState();
			}
		}

		// Device scan callback.
		private BluetoothAdapter.LeScanCallback mLeScanCallback =
				new BluetoothAdapter.LeScanCallback() {
			@Override
			public void onLeScan(final BluetoothDevice device, int rssi,
					byte[] scanRecord) {

				final int rssiCopy = rssi;
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(device.getName().contains("Kensington")){
							BLETagData data = new BLETagData(device.getAddress(), device.getName(), rssiCopy);
							bleTagsData.put(data.getDeviceName(), data);
						}
					}
				});
			}
		};

		private void scanLeDevice(final boolean enable) {

			int delay = 10000;
			int period = 2000;
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					if (enable) {
						// Stops scanning after a pre-defined scan period.
						mHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								mScanning = false;

								bluetoothAdapter.stopLeScan(mLeScanCallback);
								btArrayAdapter.clear();

								for (BLETagData data : bleTagsData.values()) {
									String toPrint= data.getDeviceName() + 
											" : " + data.getMacAddress() +
											" : " + data.getSignalStrength() + " db";

									//System.out.println(toPrint);
									btArrayAdapter.add(toPrint);	
								}
								btArrayAdapter.notifyDataSetChanged();

							}
						}, SCAN_PERIOD);

						mScanning = true;
						//if(!btArrayAdapter.isEmpty()) btArrayAdapter.clear();
						bluetoothAdapter.startLeScan(mLeScanCallback);
					} else {
						mScanning = false;
						bluetoothAdapter.stopLeScan(mLeScanCallback);
					}

				}
			}, delay, period);

		}

}
