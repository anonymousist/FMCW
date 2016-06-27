package com.example.administrator.emit_mfcw;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

/**
 * Created by martin on 2016/6/16 0016.
 */
public class FMCW {

    AudioTrack mAudioTrack;
    protected int SampleRate = 48000;
    protected double chirp_duration =0.01075;
    protected double max_fre = 20000;
    protected double min_fre = 19000;
    protected int mBufferSize;
    private final String TAG = "AcouText";
    private boolean IsPlaying ;

    private int initAudioTrack(){
         mBufferSize = AudioTrack.getMinBufferSize(SampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
       // Log.e(TAG,"mBufferSize:"+mBufferSize+"  bytes");
        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,SampleRate
                ,AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT,
                mBufferSize,AudioTrack.MODE_STREAM);
                return 1;
    }
    protected void play(){
        initAudioTrack();
        final double[] mSound = new double[mBufferSize];
        final short[] mBuffer = new short[mBufferSize];
        mAudioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
        mAudioTrack.play();
        IsPlaying = true;
        Thread gen = new Thread(new Runnable() {
            @Override
            public void run() {
                    double count = 0;
                   double frequency = min_fre;
                   double point = Math.floor(chirp_duration/(max_fre-min_fre)*SampleRate);

                //frequency = frequency + mSound.length*0.001;
                    for (int i = 0; i < mSound.length; i++) {
                       /*   count ++;
                        if(count == 1) {
                            frequency = frequency + 0.001;
                            count =0;
                        }
                        if(frequency > max_fre)
                        {
                            frequency = min_fre;
                        }
                        */
                        mSound[i] = Math.sin((2.0 * Math.PI * i / (SampleRate / frequency)));
                        mBuffer[i] = (short) (mSound[i] * Short.MAX_VALUE);
                    }
                while (IsPlaying == true) {
                    mAudioTrack.write(mBuffer, 0, mSound.length);
                }
            }
        });
        gen.start();
    }
    protected void stop(){
        if(mAudioTrack != null) {
            IsPlaying = false;
            mAudioTrack.stop();
            mAudioTrack.flush();
            mAudioTrack.release();
            mAudioTrack = null;

        }
    }
}
