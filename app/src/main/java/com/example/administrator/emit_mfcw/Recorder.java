package com.example.administrator.emit_mfcw;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
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
    private short[] buffer;
    private double[] toTransfrom;
    private int blockSize;           //每次读取块的大小
    private FileOutputStream fos;
    private String TAG = "Recorder";
    private boolean IsRecording;
    FeatureExtraction mFeatureExt;
    private double[] curlastFrame;  //用于临时存储当前读取buffer数据的最后一帧数据
    private double[] lastFrame;       //上次读取buffer数据的最后一帧
    protected void initRecorder(){
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Recording";
        int bufferSize = AudioRecord.getMinBufferSize(sampleRate,    //sampling rate
                AudioFormat.CHANNEL_IN_MONO,   //channel configuration
                AudioFormat.ENCODING_PCM_16BIT);         //audio encoding
                Log.e(TAG,"buffersize:"+bufferSize);
        myAudioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC,  //audio source
                sampleRate,              //sample rate
                AudioFormat.CHANNEL_IN_MONO,   //channel
                AudioFormat.ENCODING_PCM_16BIT,          //audio encoding
                bufferSize*6);            //buffer size
         blockSize = bufferSize*3;
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
    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);            //讲short类型转换为byte类型， short低位存储到bytes在前，高位存储在后
            sData[i] = 0;
        }
        return bytes;
    }

    protected void startRecord(){
        IsRecording = true;

        mFeatureExt = new FeatureExtraction();
        Thread record = new Thread(new Runnable() {
            @Override
            public void run() {
                initRecorder();                             //初始化recorder
                initFileSystem();                           //初始化 文件 存储
                buffer = new short[blockSize];
                lastFrame = new double[8192];
                curlastFrame = new double[8192];
                Log.e(TAG, "blockSize:" + blockSize + "");
                myAudioRecorder.startRecording();   //start recording
                toTransfrom = new double[blockSize];
                for (int i =0;i<8192;i++){
                    lastFrame[i] = 0;
                }

                while (IsRecording == true){
                    int bufferReadResult = myAudioRecorder.read(buffer,0,blockSize);
                    Log.e(TAG, "bufferReadResult:"+bufferReadResult+"");
                    byte bData[] = short2byte(buffer);
                   // Log.e(TAG,"bData:"+(double)bData[300]);
                    for (int i = 0;i<blockSize && i < bufferReadResult;i++){
                        toTransfrom[i] = (double)(bData[i*2+1]<<8+bData[i*2])/32768.0;                    //获取双精度类型数据
                        //toTransfrom[i] = (double)buffer[i]/32768.0;
                        //Log.e(TAG,"******"+toTransfrom[i]+"***");
                    }
                 //   Log.e(TAG,"buffer:"+buffer[300]);
                  //  Log.e(TAG,"toTransfrom:"+toTransfrom[300]);
                    if(bufferReadResult > 8192){
                             for (int j=0;j< 8192;j++){
                                   curlastFrame[j]=toTransfrom[bufferReadResult-8192+j];              //读取buffer出来的最后一帧数据，用于两次buffer的平滑
                             }
                    }
                    //myFFT.ft(toTransfrom);
                    mFeatureExt.featureExt(toTransfrom,lastFrame);
                    for (int i = 0;i<lastFrame.length;i++){                                  //讲当前读取buffer的最后一帧保存到数据，下次读取buffer后，一并送入featureextraction模块。
                        lastFrame[i] = curlastFrame[i];
                    }
                    if (AudioRecord.ERROR_INVALID_OPERATION != bufferReadResult) {
                        try {
                            fos.write(bData,0,blockSize*2);                                 //写入文件
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
            IsRecording = false;
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;
        }
    }

}
