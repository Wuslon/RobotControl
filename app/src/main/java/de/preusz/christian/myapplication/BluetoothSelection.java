package de.preusz.christian.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothSelection extends AppCompatActivity {
    public static final int REQUEST_ACCESS_COARSE_LOCATION = 1;
    ArrayList<String> BTDevices = new ArrayList<>();
    //ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_selection);

        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        Spinner BTDeviceNames = findViewById(R.id.spinner_BTDevices);

        if (pairedDevices.size() > 0){
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                BTDevices.add(deviceName);
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, BTDevices);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.notifyDataSetChanged();
        BTDeviceNames.setAdapter(arrayAdapter);
        BTDeviceNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String Name = parent.getItemAtPosition(position).toString();
//                Toast.makeText(parent.getContext(), "Selected: " + Name, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
        Button button_SearchBluetooth = (Button) findViewById(R.id.button_BTSearch);
        button_SearchBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( checkCoarseLocationPermission()){
                    //arrayAdapter.clear();
                    bluetoothAdapter.startDiscovery();
                }
                //Toast.makeText(getApplicationContext(), "Searching ", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean checkCoarseLocationPermission() {
        //checks all needed permissions
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_COARSE_LOCATION);
            Toast.makeText(getApplicationContext(), "Permissions NOT ok ", Toast.LENGTH_LONG).show();
            return false;
        }else{
            Toast.makeText(getApplicationContext(), "Permissions ok ", Toast.LENGTH_LONG).show();
            return true;
        }

    }

    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getApplicationContext(), "Searching ", Toast.LENGTH_LONG).show();
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                if (deviceName/*.isEmpty()*/ == null) {
                    BTDevices.add(deviceHardwareAddress);
                } else {
                    BTDevices.add(deviceName);
                }
                //arrayAdapter.clear();
                //arrayAdapter.addAll(BTDevices);
                //arrayAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Found: " + deviceName + ", " + deviceHardwareAddress, Toast.LENGTH_LONG).show();
            }
            Toast.makeText(getApplicationContext(), "List: " + BTDevices, Toast.LENGTH_LONG).show();
        }
    };

}