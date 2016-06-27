package com.example.administrator.emit_mfcw;

import android.nfc.Tag;
import android.util.Log;
import FFTlibrary.fft.RealDoubleFFT;
import static java.lang.System.*;
/**
 * Created by Administrator on 2016/6/18 0018.
 */
public class FeatureExtraction {

    private String TAG = "FeatureExtraction";

    public boolean featureExt(final double[] inputData,final double[] lastFrme){
        //Log.e(TAG,"inputData len:"+inputData.length+"");
       // Log.e(TAG,"lastFrame len:"+lastFrme.length+"");
        int winSize = 8192;
        double[] data;
        data = new double[inputData.length + winSize];
        arraycopy(lastFrme, 0, data, 0, lastFrme.length);
        arraycopy(inputData,0, data, lastFrme.length, data.length - lastFrme.length);
        int step;
        //Log.e(TAG,"lastFrame"+lastFrme[3]+"");
        step = 1024;
        int numOfFrames = (data.length- winSize)/ step +1;
        RealDoubleFFT myFFT = new RealDoubleFFT(winSize);
        double[] frameData;
        frameData = new double[8192];
        for (int i = 0;i<numOfFrames;i++){
            arraycopy(data, step * i + 0, frameData, 0, winSize);
            myFFT.ft(frameData);
          double en;
            en = spectralCentroid(frameData);
            Log.e(TAG,en+"");
        }


       // Log.e(TAG,"ffffffffffffffffffffff");
       // Thread ftThread = new Thread(new Runnable() {
         //   @Override
         //   public void run() {

              //  Log.e(TAG,sum+"");
        return true;
              //  Log.e(TAG,entropy+"");
            }
      //  });
     //   ftThread.start();
    //}
    private double spectralCentroid(double[] fftE){
        int len = 8192;
        double[] freValue = new double[len/2];
        //Log.e(TAG,len+"");
        double[] FFT;
        FFT = new double[len / 2];
        double[] pow;
        pow = new double[len / 2];
        double freResulation;
        freResulation = 44100 / len;
        double max = fftE[0];
        for (int j = 0;j<len/2;j++)
        {
            freValue[j] = j* freResulation;                 //every bin corresponding frequecy value
            FFT[j] = fftE[j];                           //using the len/2 points & calculate the max value
            if(fftE[j+1]>fftE[j]){
                max = fftE[j+1];
            }
        }
        //Log.e(TAG,"max:"+max);
        double sum;
        sum = 0;
        for (int i = 0;i< FFT.length;i++ ){
            FFT[i] = FFT[i]/max;
            pow[i] = FFT[i]* FFT[i];                  //normalize the data calculate the power of the signal
            sum = sum + pow[i];
        }
        //Log.e(TAG,"sum:"+sum+"");
       double tempSum = 0;
       //
        for(int i= 0;i< FFT.length;i++){
            tempSum =tempSum+ freValue[i]* pow[i];
        }
        double entropy;
        entropy = tempSum / sum;
        return entropy;
    }
    private void filter(){
                    //design filter to remove the noise

    }
}
