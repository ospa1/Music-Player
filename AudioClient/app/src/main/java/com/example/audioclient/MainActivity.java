package com.example.audioclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.clipserver.musicServiceInterface;

public class MainActivity extends AppCompatActivity {

    Button startButton;
    Button stopServiceButton;
    Button pauseButton;
    Button playButton;
    Button stopButton;
    EditText numText;
    musicServiceInterface msi;
    boolean isBound;
    Boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numText = findViewById(R.id.editTextNum);
        final Intent musicServiceIntent = getPackageManager().getLaunchIntentForPackage("com.example.clipserver");

        startButton = (Button) findViewById(R.id.serviceButton);
        startButton.setVisibility(View.INVISIBLE);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View src) {

                // Start the MusicService using the Intent
                //startService(musicServiceIntent);
                isBound = true;
                stopServiceButton.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.INVISIBLE);
                stopButton.setVisibility(View.VISIBLE);
                playButton.setVisibility(View.VISIBLE);
            }
        });

        stopServiceButton = (Button) findViewById(R.id.stopServiceButton);
        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View src) {

                // Stop the MusicService using the Intent
                //stopService(musicServiceIntent);
                isBound = false;
                try {
                    msi.stop();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                startButton.setVisibility(View.VISIBLE);
                stopServiceButton.setVisibility(View.INVISIBLE);
                stopButton.setVisibility(View.INVISIBLE);
                playButton.setVisibility(View.INVISIBLE);
                pauseButton.setVisibility(View.INVISIBLE);

            }
        });

        pauseButton = (Button) findViewById(R.id.pauseButton);
        pauseButton.setVisibility(View.INVISIBLE);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //pause the music
                if (pauseButton.getText().toString().equals("Pause")){
                    pauseButton.setText("Resume");
                    try {
                        msi.pause();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                //resume
                else{
                    pauseButton.setText("Pause");
                    try {
                        msi.resume();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //play the music
                String text = numText.getText().toString();
                System.out.println(text);
                int num;

                try{
                    num = Integer.parseInt(text);
                }catch (NumberFormatException e){
                    num = 0;
                }

                if (num > 0 && num <= 5 && isBound){
                    try {
                        msi.play(num);
                        pauseButton.setVisibility(View.VISIBLE);
                        pauseButton.setText("Pause");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    Toast t =Toast.makeText(getApplicationContext(), "invalid number or service not started", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });

        stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View src) {

                // Stop the Music
                if(isBound){
                    try {
                        msi.stop();
                        pauseButton.setVisibility(View.INVISIBLE);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            msi = musicServiceInterface.Stub.asInterface(iBinder);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            msi = null;
            isBound = false;
        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        if(!isBound){
            boolean b =false;
            Intent i = new Intent(musicServiceInterface.class.getName());
            ResolveInfo info = getPackageManager().resolveService(i, 0);
            i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));
            b = bindService(i, this.serviceConnection, Context.BIND_AUTO_CREATE);

            if(b){
                Log.i("client", "its bound");
            }
            else {
                Log.i("client", "bound failed");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isBound){
            unbindService(serviceConnection);
        }
    }
}
