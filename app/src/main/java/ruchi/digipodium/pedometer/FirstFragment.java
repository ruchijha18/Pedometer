package ruchi.digipodium.pedometer;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class FirstFragment extends Fragment {

    private TextView tv1;
    private Button reset;
    private TextView tv;
    private SensorManager sensorManager;
    private double magni;
    private Integer stepcount = 0;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv1 =(TextView)getActivity().findViewById(R.id.text);
        reset =(Button)getActivity().findViewById(R.id.reset) ;
        tv =(TextView)getActivity().findViewById(R.id.tv);
        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorEventListener obj = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent != null) {
                    float x_accelerometer = sensorEvent.values[0];
                    float y_accelerometer = sensorEvent.values[1];
                    float z_accelerometer = sensorEvent.values[2];

                    double magnitute = Math.sqrt(x_accelerometer * x_accelerometer + y_accelerometer * y_accelerometer + z_accelerometer * z_accelerometer);
                    double magniDelta = magnitute - magni;
                    magni = magnitute;
                    if (magniDelta >2) {
                        stepcount++;
                    }
                    Calendar A= Calendar.getInstance();
                    int a=A.get(Calendar.HOUR_OF_DAY);
                    int b=A.get(Calendar.MINUTE);


                    if(a==0&&b==00)
                        stepcount=0;
                    tv.setText(stepcount.toString());

                    reset.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            stepcount=0;

                        }
                    });

                }
            }


            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {


            }
        };
        sensorManager.registerListener(obj, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences =this.getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.clear();
        ed.putInt("X", stepcount);
        ed.apply();
    }
    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences =this.getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.clear();
        ed.putInt("X", stepcount);
        ed.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences =this.getActivity().getPreferences(Context.MODE_PRIVATE);
        stepcount = sharedPreferences.getInt("X", 0);
    }
}