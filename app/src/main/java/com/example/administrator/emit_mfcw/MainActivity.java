package com.example.administrator.emit_mfcw;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    FMCW mfmcw;
    private Button Play_button;
    private Button Stop_button;
    private TextView Show_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Play_button = (Button)findViewById(R.id.play_button);
        Stop_button = (Button)findViewById(R.id.stop_button);
        Show_message = (TextView)findViewById(R.id.textView);
        Play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mfmcw = new FMCW();
                mfmcw.initAudioTrack();
                mfmcw.play();
                Play_button.setEnabled(false);
            }
        });
        Stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mfmcw.stop();
                Play_button.setEnabled(true);
            }
        });
    }
}
