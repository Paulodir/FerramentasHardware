package com.edu.senac.ferramentashardware;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * // * {@link AudioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AudioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioFragment extends Fragment {


    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;
    private MediaRecorder recorder = null;
    MediaPlayer mediaPlayer = null;
    private boolean permissionRecord = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    boolean mStartPlayning = true;
    boolean mStartRecording = true;
    boolean gravando = false;
    boolean ouvindo = false;

    SeekBar posicaoBar;
    SeekBar volumeBar;
    TextView tempoTranscorido;
    TextView tempoRestante;
    Button gravar, escutar;
    ImageView imgStatus;
    int tempoTotal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio, container, false);

        //solicita as permissoes para o usuario
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        fileName = getActivity().getExternalCacheDir().getAbsolutePath() + "/audioSenac.3gp";

        imgStatus = view.findViewById(R.id.status);
        gravar = view.findViewById(R.id.gravar);
        escutar = view.findViewById(R.id.escutar);
        tempoTranscorido = view.findViewById(R.id.tempoPassado);
        tempoRestante = view.findViewById(R.id.tempoFaltando);

        gravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Gravando", Toast.LENGTH_SHORT).show();
                gravar();
            }
        });

        escutar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Reproduzindo", Toast.LENGTH_SHORT).show();
                escutar();
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionRecord) {
            Toast.makeText(getActivity(), "Aceite as Permissões", Toast.LENGTH_SHORT).show();
        }
    }

    public void escutar() {
        if (!mStartRecording){
            gravar();
        }
        onPlay(mStartPlayning);
        if (mStartPlayning) {

            escutar.setText("parar audio");
            imgStatus.setImageResource(R.drawable.player);
        } else {
            escutar.setText("ouvir");
            imgStatus.setImageResource(R.drawable.play);

        }
        mStartPlayning = !mStartPlayning;
    }

    public void gravar() {
        if (!mStartPlayning){
            escutar();
        }
        onRecord(mStartRecording);

        if (mStartRecording) {

            gravar.setText("parar gravação");
            imgStatus.setImageResource(R.drawable.microphone);
        } else {

            gravar.setText("gravar");
            imgStatus.setImageResource(R.drawable.play);
        }
        mStartRecording = !mStartRecording;


    }
    private void startPlayning() {
        ouvindo = true;
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(fileName);
            mediaPlayer.prepare();
            mediaPlayer.start();
            //mediaPlayer = MediaPlayer.create(this,R.raw.fireball);//(this, R.media.music);
            mediaPlayer.setLooping(false);
            mediaPlayer.seekTo(0);
            mediaPlayer.setVolume(0.5f, 0.5f);
            tempoTotal = mediaPlayer.getDuration();

            volumeBar = getActivity().findViewById(R.id.volume);
            volumeBar.setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            float volumeNum = progress / 100f;
                            mediaPlayer.setVolume(volumeNum, volumeNum);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    }
            );

            posicaoBar = getActivity().findViewById(R.id.progresso);
            posicaoBar.setMax(tempoTotal);
            posicaoBar.setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (fromUser) {
                                mediaPlayer.seekTo(progress);
                                posicaoBar.setProgress(progress);
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    }
            );

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mediaPlayer != null) {
                        try {
                            Message msg = new Message();
                            msg.what = mediaPlayer.getCurrentPosition();
                            handler.sendMessage(msg);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {}
                    }
                }
            }).start();
        } catch (Exception e) {
            Log.e("audio", "erro=>startPlayning");
        }

    }

    //(gravando)?"fdfdf":"fbdfgf";
    public void onPlay(boolean start) {
        if (start) {
            //if(gravando==true){
            //   ouvindo==false
            //  }
            startPlayning();
        } else {
            stopPlayning();
        }
    }

    public void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void stopPlayning() {
        ouvindo = false;
        mediaPlayer.release();
        mediaPlayer = null;

    }

    private void startRecording() {
        gravando = true;
        recorder = new MediaRecorder();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();

        } catch (Exception e) {
            Log.e("audio", "erro=>startRecording");
        }
        recorder.start();

    }

    private void stopRecording() {
        gravando = false;
        recorder.stop();
        recorder.release();
        recorder = null;

    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            // Atualiza progressoBar
            posicaoBar.setProgress(currentPosition);

            // Atualiza tempo
            String elapsedTime = criaTemporizador(currentPosition);
            tempoTranscorido.setText(elapsedTime);

            String remainingTime = criaTemporizador(tempoTotal-currentPosition);
            tempoRestante.setText("- " + remainingTime);
        }
    };

    public String criaTemporizador(int time) {
        String tempo = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        tempo = min + ":";
        if (sec < 10) tempo += "0";
        tempo += sec;

        return tempo;
    }

}