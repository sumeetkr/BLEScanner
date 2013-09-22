package in.sumeetkumar.blescanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.R.array;
import android.R.integer;
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

public class LuggageStatusFragment extends Fragment {

	private static final int REQUEST_ENABLE_BT = 1;

	ListView listDevicesFound;
	Button btnScanDevice;
	TextView stateBluetooth;
	BluetoothAdapter bluetoothAdapter;

	// Stops scanning after 1 seconds.
	private boolean enable;


	ArrayAdapter<String> btArrayAdapter;

	@Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
        		R.layout.activity_main, container, false);
        
        return rootView;
    }

	@Override
    public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	
		
		Button button= (Button) this.getActivity().findViewById(R.id.trackLuggageButton);
		button.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	NetworkDataAccess dataAccess = new NetworkDataAccess();
		    	dataAccess.execute("background","Progress","result");
		      
		    }
		});

		
		enable = !enable;
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
	
	 private class NetworkDataAccess extends AsyncTask<String,String,String>{

		    protected String doInBackground(String... background){
		       EditText text = (EditText) getActivity().findViewById(R.id.enterTicketText);
		       String ticketNo = text.getEditableText().toString();
		       //ticketNo = "EK7DFC";
		       return BLEDataSender.getData(ticketNo);
		    }

		    protected void onPostExecute(String result){
		    	final String resultCopy = result;
		    	
		    	getActivity().runOnUiThread(new Runnable() {
		    		public void run() {
		    			TextView textView = (TextView)getActivity().findViewById(R.id.textStatus);
		    			textView.setText(resultCopy);
		    		}
		    		 });
		    }
		  }
}
