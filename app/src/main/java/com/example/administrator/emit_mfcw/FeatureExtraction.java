package com.example.administrator.emit_mfcw;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.nfc.Tag;
import android.util.Log;
import FFTlibrary.fft.RealDoubleFFT;
import static java.lang.System.*;
/**
 * Created by Administrator on 2016/6/18 0018.
 */
public class FeatureExtraction {

    private String TAG = "FeatureExtraction";
    double[] pow;
    private MainActivity manactivity = new MainActivity();
    private myView myViwer ;
    public boolean featureExt(final double[] inputData,final double[] lastFrme){
        int winSize = 8192;
        double[] data;

        data = new double[inputData.length + winSize];
        arraycopy(lastFrme, 0, data, 0, lastFrme.length);
        arraycopy(inputData,0, data, lastFrme.length, data.length - lastFrme.length);
        int step;
        step = 1024;
        int numOfFrames = (data.length- winSize)/ step +1;
        RealDoubleFFT myFFT = new RealDoubleFFT(winSize);
        double[] frameData;
        frameData = new double[8192];
        for (int i = 0;i<numOfFrames;i++){
            arraycopy(data, step * i, frameData, 0, winSize);
            //applyHanningWindow(frameData, 0, frameData.length);
            myFFT.ft(frameData);
            double[] FFT_Data = new double[8192];
            FFT_Da(frameData,FFT_Data);          //计算fft的功率
            filter(FFT_Data);
            String result;
            result = freShift(FFT_Data);
            Log.e(TAG,"freshift:"+result);
        }
        return true;
    }

    private String freShift(double[] FFT_Data){
        //int low = (int) (18800/22050.0*4096);//3492
        int temMaxInd = 0;
        double maxData = FFT_Data[0];
        for (int i = 0; i <FFT_Data.length ;i++){
            if(maxData<FFT_Data[i]){
                maxData =FFT_Data[i];
                temMaxInd = i;
            }
        }
        //Log.e(TAG,"maxFFTdata"+maxData);
        Log.e(TAG,"最大索引："+temMaxInd);
        //Log.e(TAG,"最大值："+maxData);
        if (temMaxInd - 7059 > 5){

            return  "1";
        }
        else if (temMaxInd - 7059 < -5){

            return "-1";
        }
        else {
              return "0";
        }
    }



    private double[] applyHanningWindow(double[] signal_in, int pos, int size)
    {
        for (int i = pos; i < pos + size; i++)
        {
            int j = i - pos; // j = index into Hann window function
            signal_in[i] = (double)(signal_in[i] * 0.5 * (1.0 - Math.cos(2.0 * Math.PI * j / size)));
        }
        return signal_in;
    }

private void FFT_Da(double[] Frame_fft,double[] FFT_Data){

    int len = 8192;
    double max = Frame_fft[0];
    for (int j = 0;j<len-1;j++)
    {
        FFT_Data[j] = Math.abs(Frame_fft[j]);                 //every bin corresponding frequecy value
        if(Frame_fft[j+1]>Frame_fft[j]){
            max = Frame_fft[j+1];//using the len/2 points & calculate the max value
        }
    }
    for(int i = 0;i< len;i++){
        FFT_Data[i] = FFT_Data[i]/max;
        FFT_Data[i] = 10*Math.log10(FFT_Data[i]*FFT_Data[i]);
    }
    //Log.e("FFT_DATA:",""+FFT_Data[7069]);
}
private void filter(double[] FFT_Data){
                    //design filter to remove the noise
        for(int i =0;i<FFT_Data.length;i++){
            if(i<7000||i>7100||(7056<i&&i<7061))
            {
                    FFT_Data[i] = -120;
            }
            if(FFT_Data[i]<-75)
            {
                FFT_Data[i] = -120;
            }
        }
    }
}
//||(3526 < i&& i< 3533)