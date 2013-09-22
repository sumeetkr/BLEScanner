package in.sumeetkumar.blescanner;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class LuggageDistanceFragment extends Fragment {
	private static final int REQUEST_ENABLE_BT = 1;

	ListView listDevicesFound;
	Button btnScanDevice;
	TextView stateBluetooth;
	BluetoothAdapter bluetoothAdapter;

	// Stops scanning after 1 seconds.
	private static final long SCAN_PERIOD = 2000;
	private boolean mScanning = false;
	private Handler mHandler;
	private boolean enable;
	private HashMap<String, BLETagData> bleTagsData;
	private String macAddres;
	private String phoneNumber;

	ArrayAdapter<String> btArrayAdapter;

	@Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
        		R.layout.activity_luggage_distance_fragment, container, false);
        
        return rootView;
    }

	@Override
    public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	
		macAddres= getMacAddress();
		phoneNumber = getPhoneNumber();
		
		stateBluetooth = (TextView) this.getActivity().findViewById(R.id.bluetoothstate);
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		listDevicesFound = (ListView) this.getActivity().findViewById(R.id.listDevicesFound);
		bleTagsData = new HashMap<String, BLETagData>();
		
		
		btArrayAdapter = new ArrayAdapter<String>(LuggageDistanceFragment.this.getActivity(),
				android.R.layout.simple_list_item_1);
		listDevicesFound.setAdapter(btArrayAdapter);

		mHandler = new Handler();
		
		
		enable = !enable;
		scanLeDevice(true);
	}
	
	private void CheckBlueToothState() {
		if (bluetoothAdapter == null) {
			stateBluetooth.setText("Bluetooth NOT support");
		} else {
			if (bluetoothAdapter.isEnabled()) {
				if (bluetoothAdapter.isDiscovering()) {
					stateBluetooth
							.setText("Bluetooth is currently in device discovery process.");
				} else {
					stateBluetooth.setText("Bluetooth is Enabled.");
					btnScanDevice.setEnabled(true);
				}
			} else {
				stateBluetooth.setText("Bluetooth is NOT Enabled!");
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == REQUEST_ENABLE_BT) {
			CheckBlueToothState();
		}
	}

	// Device scan callback.
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, int rssi,
				byte[] scanRecord) {

			final int rssiCopy = rssi;
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (device.getName().contains("Kensington")) {
						BLETagData data = new BLETagData(macAddres,
								device.getName(), rssiCopy, phoneNumber);
						
						System.out.println(data.getMacAddress() +" : " + data.gettagName());
						bleTagsData.put(data.gettagName(), data);
					}
				}
			});
		}
	};

	private void scanLeDevice(final boolean enable) {

		int delay = 0;
		int period = 3000;
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
							sendDataToServer();
							btArrayAdapter.clear();

							for (BLETagData data : bleTagsData.values()) {
								String toPrint = data.gettagName() + " : "
										+ "Phone no " + data.getPhoneNumber() + " : "
										+ getDistanceFromSignalStrength(data.getSignalStrength());

								// System.out.println(toPrint);
								btArrayAdapter.add(toPrint);
							}
							btArrayAdapter.notifyDataSetChanged();

						}
					}, SCAN_PERIOD);

					mScanning = true;
					// if(!btArrayAdapter.isEmpty()) btArrayAdapter.clear();
					bluetoothAdapter.startLeScan(mLeScanCallback);
				} else {
					mScanning = false;
					bluetoothAdapter.stopLeScan(mLeScanCallback);
				}

			}
		}, delay, period);

	}

	private String getDistanceFromSignalStrength(int signalStrength){
		double distance = (1000/(110 + signalStrength) - 13);
		return Double.toString( distance) + " ft";
	}
	private void sendDataToServer() {
		for (BLETagData data : bleTagsData.values()) {
			WebRequestData webRequestData = new WebRequestData(macAddres, data.gettagName(), data.getSignalStrength(), data.getMacAddress());
			BLEDataSender.sendData(webRequestData);
		}
		
	}
	
	private String getMacAddress(){
		TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
		
	}
	
	private String getPhoneNumber(){
		TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		 return telephonyManager.getLine1Number();
	}
	
}
