package com.example.oscar.sensores;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener
{
private TextView salida;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        salida=(TextView)findViewById(R.id.salida);
        SensorManager sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        List<Sensor> listaSensores=sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor: listaSensores)
        {
            log(sensor.getName());
        }
        listaSensores=sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        if(!listaSensores.isEmpty())
        {
            Sensor orientationSensor=listaSensores.get(0);
            sensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_UI);
        }
        listaSensores=sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(!listaSensores.isEmpty())
        {
            Sensor acelerometerSensor=listaSensores.get(0);
            sensorManager.registerListener(this, acelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        }
        listaSensores=sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
        if(!listaSensores.isEmpty())
        {
            Sensor sensorMagnetico=listaSensores.get(0);
            sensorManager.registerListener(this, sensorMagnetico, SensorManager.SENSOR_DELAY_UI);
        }
        listaSensores=sensorManager.getSensorList(Sensor.TYPE_PROXIMITY);
        if(!listaSensores.isEmpty())
        {
            Sensor sensorProximidad=listaSensores.get(0);
            sensorManager.registerListener(this, sensorProximidad, SensorManager.SENSOR_DELAY_UI);
        }
    }
    private void log(String string)
    {
        salida.append(string+"\n");
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        switch (event.sensor.getType())
        {
            case Sensor.TYPE_ORIENTATION:
                for(int i=0;i<3; i++)
                {
                    log("Orientacion: "+i+event.values[i]);
                }
                break;
            case Sensor.TYPE_ACCELEROMETER:
                for(int i=0;i<3; i++)
                {
                    log("Acelerometro: "+i+event.values[i]);
                }
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                for(int i=0;i<3; i++)
                {
                    log("Magnetismo: "+i+event.values[i]);
                }
                break;
                default:
                    for (int i=0;i<event.values.length; i++)
                    {
                        log(event.sensor.getType()+":"+i+event.values[i]);
                    }
        }
    }
}
