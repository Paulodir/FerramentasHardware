package com.edu.senac.ferramentashardware;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import static android.content.Context.SENSOR_SERVICE;


/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * // * {@link AudioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AudioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AcelerometroFragment extends Fragment implements SensorEventListener {
    DecimalFormat df = new DecimalFormat("#.0");
    Sensor acelerometro;
    SensorManager sm;
    TextView direcao;
    String resposta;
    Float axisX = 0.0f, axisY = 0.0f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_acelerometro, container, false);

        sm =(SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        acelerometro = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sm.registerListener( this,acelerometro,SensorManager.SENSOR_STATUS_ACCURACY_LOW);
        direcao=view.findViewById(R.id.direcao);

        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        axisX =(float) Math.floor(sensorEvent.values[0]*10)/10;
        axisY =(float) Math.floor(sensorEvent.values[1]);
        direcao.setRotation(10*axisX);


        Log.e("aaa",""+axisX);
        if (axisY < 0){
            direcao.setRotationX(180);
        }else{
            direcao.setRotationX(0);
        }

        if((sensorEvent.values[0])<(-2.5)){
            resposta="Você Inclinou Para:" +
                    "\nDireita";
        }else if((sensorEvent.values[0])>(2.5)){
            resposta="Você Inclinou Para:" +
                    "\nEsquerda";
        }else{
            resposta="Incline Para " +
                    "\nEsquerda ou Direita";
        }
        //+"\n X: "+sensorEvent.values[0]+"\n Y: "+sensorEvent.values[1]+"\n Z: "+sensorEvent.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}