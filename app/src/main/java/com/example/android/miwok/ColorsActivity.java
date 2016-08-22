/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;


public class ColorsActivity extends AppCompatActivity {
    private MediaPlayer mediaP;

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaP != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaP.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaP = null;

            Toast.makeText(ColorsActivity.this,"Sound finished",Toast.LENGTH_SHORT).show();

            audioManager.abandonAudioFocus(afChangeListener);
        }
    }

    private MediaPlayer.OnCompletionListener mCompleteListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    private AudioManager audioManager;

    private AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT){
                mediaP.pause();
                mediaP.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS){
                releaseMediaPlayer();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                mediaP.pause();
                mediaP.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN){
                mediaP.start();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        final ArrayList<Word> colorsArray = new ArrayList<Word>();

        colorsArray.add(0, new Word("red","weṭeṭṭi", R.drawable.color_red, R.raw.color_red));
        colorsArray.add(1, new Word("green","chokokki", R.drawable.color_green, R.raw.color_green));
        colorsArray.add(2, new Word("brown","ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
        colorsArray.add(3, new Word("gray","ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
        colorsArray.add(4, new Word("black","kululli", R.drawable.color_black, R.raw.color_black));
        colorsArray.add(5, new Word("white","kelelli", R.drawable.color_white, R.raw.color_white));
        colorsArray.add(6, new Word("dusty yellow","ṭopiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        colorsArray.add(7, new Word("mustard yellow","chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        final ListView colorsListView = (ListView) findViewById(R.id.list);

        WordAdapter colorsAdapter = new WordAdapter(this, colorsArray, R.color.category_colors);

        colorsListView.setAdapter(colorsAdapter);


        colorsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                Word temp_word = colorsArray.get(position);

//                mediaP = mediaP.create(ColorsActivity.this, temp_word.getSoundPath());

                audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                int result = audioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    mediaP = MediaPlayer.create(ColorsActivity.this, temp_word.getSoundPath());
                    mediaP.start();
                    mediaP.setOnCompletionListener(mCompleteListener);
                }
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

}
