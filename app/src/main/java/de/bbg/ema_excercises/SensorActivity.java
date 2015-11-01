package de.bbg.ema_excercises;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SensorActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private TextView txtSensorX;
    private TextView txtSensorY;
    private TextView txtSensorZ;

    private ProgressBar pbSensorX;
    private ProgressBar pbSensorY;
    private ProgressBar pbSensorZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        txtSensorX = (TextView)findViewById(R.id.txtSensorX);
        txtSensorY = (TextView)findViewById(R.id.txtSensorY);
        txtSensorZ = (TextView)findViewById(R.id.txtSensorZ);

        pbSensorX = (ProgressBar)findViewById(R.id.pbSensorX);
        pbSensorY = (ProgressBar)findViewById(R.id.pbSensorY);
        pbSensorZ = (ProgressBar)findViewById(R.id.pbSensorZ);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        SensorEventListener listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                //System.out.println("onSensorChanged " + event.sensor.toString());
                if(event.sensor.getType() != Sensor.TYPE_ACCELEROMETER){
                    return;
                }

                float sensorX = event.values[0];
                float sensorY = event.values[1];
                float sensorZ = event.values[2];

                txtSensorX.setText(sensorX+"");
                txtSensorY.setText(sensorY + "");
                txtSensorZ.setText(sensorZ + "");

                pbSensorX.setProgress(Math.round(sensorX));
                pbSensorY.setProgress(Math.round(sensorY));
                pbSensorZ.setProgress(Math.round(sensorZ));

                //System.out.println("onSensorChanged TYPE_ACCELEROMETER 0:" +event.values[0] +" 1:"+ event.values[1]+" 2:"+event.values[2]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                System.out.println("onAccuracyChanged");
            }
        };


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(listener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        System.out.println("mSensorManager " + mSensorManager.toString());
        System.out.println("mSensor " + mSensor.toString());
        System.out.println("listener " + listener.toString());






    }

}
