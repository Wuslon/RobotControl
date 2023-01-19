package de.preusz.christian.myapplication;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private static final String TAG = "Bluetooth connection";

    public ConnectThread(BluetoothDevice device) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;
        mmDevice = device;
        //String MY_UUID = //"49e209a9-ec28-4f1c-af70-bae5175ecae4"; //created by an online uuid generator
        mmDevice.fetchUuidsWithSdp();
        ParcelUuid[] myUUIDs = mmDevice.getUuids();

        Log.d(TAG, "ConnectThread: UUIDs: " + myUUIDs);
         UUID myUUID = myUUIDs[0].getUuid();
        Log.d(TAG, "ConnectThread: UUID: " + myUUID);
        //String myUUID = myUUIDs[0].toString();
        //String myUUID = myUUIDs[0].getUuid();
        //Toast.makeText(getApplicationContext(), "ID: " + myUUID, Toast.LENGTH_LONG).show();
        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            tmp = device.createRfcommSocketToServiceRecord(myUUID/*UUID.fromString(myUUID)*/);
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
        mmSocket = tmp;
    }

    public void run() {

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the client socket", closeException);
            }
            return;
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        //manageMyConnectedSocket(mmSocket);
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }
}
