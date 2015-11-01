package de.bbg.ema_excercises;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private TextView txtSensorX;
    private TextView txtSensorY;
    private TextView txtSensorZ;
    private TextView txtOutputTime;

    private ProgressBar pbSensorXNeg;
    private ProgressBar pbSensorYNeg;
    private ProgressBar pbSensorZNeg;
    private ProgressBar pbSensorXPos;
    private ProgressBar pbSensorYPos;
    private ProgressBar pbSensorZPos;

    private Button btnRecord;
    private Button btnStop;


    private String time = "";
    private long longTime = 10000;

    private Boolean isRecording = false;

    Context context;
    private List<String[]> strExport = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csv);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        System.out.println("onCreate");

        context = this.getApplication();
        txtSensorX = (TextView)findViewById(R.id.txtSensorX);
        txtSensorY = (TextView)findViewById(R.id.txtSensorY);
        txtSensorZ = (TextView)findViewById(R.id.txtSensorZ);
        txtOutputTime = (TextView)findViewById(R.id.txtOutputTime);

        pbSensorXPos = (ProgressBar)findViewById(R.id.pbSensorXPos);
        pbSensorYPos = (ProgressBar)findViewById(R.id.pbSensorYPos);
        pbSensorZPos = (ProgressBar)findViewById(R.id.pbSensorZPos);

        pbSensorXNeg = (ProgressBar)findViewById(R.id.pbSensorXNeg);
        pbSensorYNeg = (ProgressBar)findViewById(R.id.pbSensorYNeg);
        pbSensorZNeg = (ProgressBar)findViewById(R.id.pbSensorZNeg);

        pbSensorXNeg.setRotation(180);
        pbSensorYNeg.setRotation(180);
        pbSensorZNeg.setRotation(180);

        btnRecord = (Button)findViewById(R.id.btnRecord);






        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("onCreate onClick");

                String[] newLine = new String[4];

                newLine[0] = "Time";
                newLine[1] = "X";
                newLine[2] = "Y";
                newLine[3] = "Z";
                strExport.add(newLine);

                isRecording = true;

                btnRecord.setClickable(false);
                btnRecord.setEnabled(false);
                btnRecord.setText(R.string.recording);

                new CountDownTimer(longTime, 1) {

                    public void onTick(long millisUntilFinished) {
                        long newTime = longTime - millisUntilFinished;
                        time = newTime + "";
                        txtOutputTime.setText("Timer: " + newTime + " ms");
                    }

                    public void onFinish() {

                        try {
                            isRecording = false;
                            writeCsv(strExport);
                            btnRecord.setClickable(true);
                            btnRecord.setEnabled(true);
                            btnRecord.setText(R.string.record);

                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context,context.getString(R.string.csvCreated), duration);
                            toast.show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        });



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

                if(isRecording) {
                    String[] newLine = new String[4];


                    if(time.equals("")) {
                        newLine[0] = "0";
                    }else{
                        newLine[0] = time;
                    }

                    newLine[1] = sensorX+"";
                    newLine[2] = sensorY+"";
                    newLine[3] = sensorZ+"";
                    strExport.add(newLine);
                }

                txtSensorX.setText("X: "+sensorX + "");
                txtSensorY.setText("Y: "+sensorY + "");
                txtSensorZ.setText("Z: "+sensorZ + "");

                sensorX = multiTen(sensorX);
                sensorY = multiTen(sensorY);
                sensorZ = multiTen(sensorZ);




                if(sensorX > 0) {
                    pbSensorXPos.setProgress(Math.round(sensorX));
                    pbSensorXNeg.setProgress(0);
                }
                else{
                    pbSensorXNeg.setProgress(Math.round(sensorX) * -1);
                    pbSensorXPos.setProgress(0);
                }

                if(sensorY > 0) {
                    pbSensorYPos.setProgress(Math.round(sensorY));
                    pbSensorYNeg.setProgress(0);
                }
                else{
                    pbSensorYNeg.setProgress(Math.round(sensorY) * -1);
                    pbSensorYPos.setProgress(0);
                }

                if(sensorZ > 0) {
                    pbSensorZPos.setProgress(Math.round(sensorZ));
                    pbSensorZNeg.setProgress(0);
                }
                else{
                    pbSensorZNeg.setProgress(Math.round(sensorZ) * -1);
                    pbSensorZPos.setProgress(0);
                }


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

    public float multiTen(float val){
        return  val * 10;
    }

    public void writeCsv(List<String[]> values) throws IOException {
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "Export.csv";
        String filePath = baseDir + File.separator +"EMA_excercise"+File.separator + fileName;
       // File f = new File(filePath ); 
        CSVWriter writer = null;
        System.out.println("writeCsv " + filePath);
        try
        {
            writer = new CSVWriter(new FileWriter(filePath));
            writer.writeAll(values);
            writer.close();

            System.out.println("writeCsv try");
        }
        catch (IOException e)
        {
            //error
            System.out.println("writeCsv catch " + e.toString());
        }

    }

}
