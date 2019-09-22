package com.edu.senac.ferramentashardware;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
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
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
// * {@link AudioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AudioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioFragment extends Fragment {

    private static final  int REQUEST_RECORD_AUDIO_PERMISSION=200;
    private static String fileName=null;
    private MediaRecorder recorder=null;
    private MediaPlayer player=null;
    private boolean permissionRecord=false;
    private String [] permissions={Manifest.permission.RECORD_AUDIO};

    boolean mStartPlayning =true;
    boolean mStartRecording=true;

    Button gravar, escutar;
    ImageView imgStatus;

       @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
           View view = inflater.inflate(R.layout.fragment_audio, container, false);

           //solicita as permissoes para o usuario
           ActivityCompat.requestPermissions(getActivity(),permissions,REQUEST_RECORD_AUDIO_PERMISSION);

           fileName=getActivity().getExternalCacheDir().getAbsolutePath()+"/audioSenac.3gp";

           imgStatus = view.findViewById(R.id.status);
           gravar=view.findViewById(R.id.gravar);
           escutar=view.findViewById(R.id.escutar);

           gravar.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Toast.makeText(getActivity(),"Gravando",Toast.LENGTH_SHORT).show();
                   gravar();
               }
           });

           escutar.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Toast.makeText(getActivity(),"Reproduzindo",Toast.LENGTH_SHORT).show();
                   escutar();
               }
           });

           return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:permissionRecord=grantResults[0]== PackageManager.PERMISSION_GRANTED;
            break;
        }
        if(!permissionRecord){
            Toast.makeText(getActivity(),"Aceite as Permissões",Toast.LENGTH_SHORT).show();
        }
    }

    public void escutar() {
        onPlay(mStartPlayning);
        if(mStartPlayning){
            escutar.setText("parar audio");
            imgStatus.setImageResource(R.drawable.player);
        }else{
            escutar.setText("ouvir");
            imgStatus.setImageResource(R.drawable.play);

        }
        mStartPlayning=!mStartPlayning;
    }
    public void gravar() {
           onRecord(mStartRecording);

           if(mStartRecording){
               gravar.setText("parar gravação");
               imgStatus.setImageResource(R.drawable.microphone);
           }else{
               gravar.setText("gravar");
               imgStatus.setImageResource(R.drawable.play);
           }
           mStartRecording=!mStartRecording;


    }
    private void startPlayning(){
           player= new MediaPlayer();
           try{
               player.setDataSource(fileName);
               player.prepare();
               player.start();
           }catch(Exception e){
               Log.e("audio","erro=>startPlayning");
           }
           
    }
    public void  onPlay(boolean start){
        if(start){
            startPlayning();
        }else{
            stopPlayning();
        }
    }
    public void  onRecord(boolean start){
           if(start){
               startRecording();
           }else{
               stopRecording();
           }
    }
    
    private void stopPlayning(){
           player.release();
           player=null;

    }
    private void startRecording(){
           recorder=new MediaRecorder();
           
           recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
           recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
           recorder.setOutputFile(fileName);
           recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
           try{
            recorder.prepare();

        }catch(Exception e){
            Log.e("audio","erro=>startRecording");
        }
           recorder.start();

    }
    private void stopRecording(){
           recorder.stop();
           recorder.release();
           recorder=null;

    }
}
