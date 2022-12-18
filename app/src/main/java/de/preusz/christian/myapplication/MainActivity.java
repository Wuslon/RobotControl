package de.preusz.christian.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    SensorManager sm = null;
    TextView xValue = null;
    TextView yValue = null;
    TextView zValue = null;

    List list;
    SensorEventListener sel = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy){}
        public void onSensorChanged (SensorEvent event){
             float[] values = event.values;

             xValue.setText(""+values[0]);
             yValue.setText(""+values[1]);
             zValue.setText(""+values[2]);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }

    @Override
    protected void onStop(){
        if(list.size()>0){
            sm.unregisterListener(sel);
        }
        super.onStop();
    }
}