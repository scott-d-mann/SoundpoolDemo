package com.example.soundpooldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    private SoundPool soundPool;

    private AudioManager audioManager;
    private Button cowButton;
    private Button duckButton;

    // Maximumn sound stream.
    private static final int MAX_STREAMS = 5;

    // Stream type.
    private static final int streamType = AudioManager.STREAM_MUSIC;

    private boolean loaded;
    private int soundIdCow;
    private int soundIdDuck;
    private float volume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.cowButton = (Button) this.findViewById(R.id.cowBtn);
        this.duckButton = (Button) this.findViewById(R.id.duckBtn);

        this.cowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSoundCow();
            }
        });

        this.duckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSoundDuck();
            }
        });

        // AudioManager audio settings for adjusting the volume
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Current volumn Index of particular stream type.
        float currentVolumeIndex = (float) audioManager.getStreamVolume(streamType);

        // Get the maximum volume index for a particular stream type.
        float maxVolumeIndex  = (float) audioManager.getStreamMaxVolume(streamType);

        // Volume is a value 0 --> 1
        // Get the current stream's volume which is used as the volume with it setVolume method.
        this.volume = currentVolumeIndex / maxVolumeIndex;

        // Suggests an audio stream whose volume should be changed by
        // the hardware volume controls.
        this.setVolumeControlStream(streamType);

        AudioAttributes audioAttrib = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        SoundPool.Builder builder= new SoundPool.Builder();
        builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

        this.soundPool = builder.build();

        // When Sound Pool load complete.
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });

        // Load sound file (cow.wav) into SoundPool.
        this.soundIdCow = this.soundPool.load(this, R.raw.cow,1);

        // Load sound file (duck.wav) into SoundPool.
        this.soundIdDuck = this.soundPool.load(this, R.raw.duck,1);

    }

    // When users click on the button "Duck"
    public void playSoundDuck( )  {
        if(loaded)  {
            float leftVolume = volume;
            float rightVolume = volume;
            // Play sound of gunfire. Returns the ID of the new stream.
            int streamId = this.soundPool.play(this.soundIdDuck,leftVolume, rightVolume, 1, 1, 1f);
        }
    }

    // When users click on the button "Cow"
    public void playSoundCow( )  {
        if(loaded)  {
            float leftVolume = volume;
            float rightVolume = volume;

            // Play sound objects destroyed. Returns the ID of the new stream.
            int streamId = this.soundPool.play(this.soundIdCow,leftVolume, rightVolume, 1, 0, 1f);
        }
    }

}