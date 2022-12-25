package de.preusz.christian.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    SensorManager sm = null;
    TextView xValue = null;
    TextView yValue = null;
    TextView zValue = null;


    List list;
    byte go = Commands.STOP;
    SensorEventListener sel = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy){}
        public void onSensorChanged (SensorEvent event){
             float[] values = event.values;

             xValue.setText("" + values[0]/* + "\ny: " + values[1] + "\nz: " + values[2]*/);
             yValue.setText("" + values[1]);
             zValue.setText("" + values[2]);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* get Sensor Data*/
        sm = (SensorManager)getSystemService(SENSOR_SERVICE);

        xValue = (TextView) findViewById(R.id.xValueText);
        yValue = (TextView) findViewById(R.id.yValueText);
        zValue = (TextView) findViewById(R.id.zValueText);

        list = sm.getSensorList(Sensor.TYPE_GAME_ROTATION_VECTOR);
        if(list.size()>0){
            sm.registerListener(sel,(Sensor)list.get(0),SensorManager.SENSOR_DELAY_NORMAL);
        }
        else{
            Toast.makeText(getBaseContext(),"Error:No Game Rotation Sensor.",Toast.LENGTH_LONG).show();
        }

        /* enable bluetooth */
        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        ActivityResultLauncher activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent i = new Intent(MainActivity.this, BluetoothSelection.class);
                MainActivity.this.startActivity(i);
            }
        });


        Button button_EnableBluetooth = (Button) findViewById(R.id.button_EnableBLuetooth);
        button_EnableBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bluetoothAdapter.isEnabled()){
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    activityResultLauncher.launch(enableBtIntent);
                }
                else{
                    Intent i = new Intent(MainActivity.this, BluetoothSelection.class);
                    MainActivity.this.startActivity(i);
                }
            }
        });

        /* send data */
        Button button_StartStop = (Button) findViewById(R.id.button_StartStop);
        button_StartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (go == Commands.STOP){
                    go = Commands.GO;
                }
                else{
                    go = Commands.STOP;
                }
            }
        });

    }

    @Override
    protected void onStop(){
        if(list.size()>0){
            sm.unregisterListener(sel);
        }
        super.onStop();
    }
}