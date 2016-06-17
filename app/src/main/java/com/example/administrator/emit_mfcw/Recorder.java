package com.example.administrator.emit_mfcw;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by martin on 2016/6/17 0017.
 */
public class Recorder {
private String outputFile = null;
    private AudioRecord myAudioRecorder = null;
    protected int sampleRate = 44100;
    private  String fileName;
    private byte[] buffer;
    private int bufferSize;
    private FileOutputStream fos;

    protected void initRecorder(){
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Recording";
        bufferSize = AudioRecord.getMinBufferSize(sampleRate,    //sampling rate
                AudioFormat.CHANNEL_IN_MONO,   //channel configuration
                AudioFormat.ENCODING_PCM_16BIT);         //audio encoding

         buffer = new byte[bufferSize];

        myAudioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC,  //audio source
                sampleRate,              //sample rate
                AudioFormat.CHANNEL_IN_MONO,   //channel
                AudioFormat.ENCODING_PCM_16BIT,          //audio encoding
                bufferSize);            //buffer size
            }

    protected void initFileSystem(){
        File sDir = new File(outputFile);
        if (!sDir.exists()) {
            sDir.mkdirs();
       }
        File file = new File(outputFile,currentTime()+".pcm");
        //fileName = file.getName();
        if(file.exists()){
            file.delete();
        }
        try {
           // file.mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
             fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    protected void startRecord(){
        Thread record = new Thread(new Runnable() {
            @Override
            public void run() {
                initRecorder();
                initFileSystem();
                myAudioRecorder.startRecording();   //start recording
                while (!(myAudioRecorder.getRecordingState() != 1)){
                    int bufferReadResult = myAudioRecorder.read(buffer, 0, bufferSize);
                    if (AudioRecord.ERROR_INVALID_OPERATION != bufferReadResult) {
                        try {
                            fos.write(buffer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    if(fos != null){
                    fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        record.start();

    }
    private String currentTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return df.format(new Date());
    }
    protected void stopRecord(){
        if(myAudioRecorder != null) {
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;
        }
    }

}
