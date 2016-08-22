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
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {
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

            Toast.makeText(PhrasesActivity.this,"Sound finished",Toast.LENGTH_SHORT).show();

            audioManager.abandonAudioFocus(afChangeListener);
        }
    }

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

    private MediaPlayer.OnCompletionListener mCompleteListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.phrases);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        final ArrayList<Word> wordsPhrasesArray = new ArrayList<Word>();

        wordsPhrasesArray.add(0, new Word("Where are you going?","minto wuksus",R.raw.phrase_where_are_you_going));
        wordsPhrasesArray.add(1, new Word("What is your name?","tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        wordsPhrasesArray.add(2, new Word("My name is...","oyaaset...", R.raw.phrase_my_name_is));
        wordsPhrasesArray.add(3, new Word("How are you feeling?","michәksәs?", R.raw.phrase_how_are_you_feeling));
        wordsPhrasesArray.add(4, new Word("I’m feeling good.","kuchi achit", R.raw.phrase_im_feeling_good));
        wordsPhrasesArray.add(5, new Word("Are you coming?","әәnәs'aa?", R.raw.phrase_are_you_coming));
        wordsPhrasesArray.add(6, new Word("Yes, I’m coming.","hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        wordsPhrasesArray.add(7, new Word("I’m coming.","әәnәm", R.raw.phrase_im_coming));
        wordsPhrasesArray.add(8, new Word("Let’s go.","wyoowutis", R.raw.phrase_lets_go));
        wordsPhrasesArray.add(9, new Word("Come here.","әnni'nem", R.raw.phrase_come_here));


        ListView phrasesListView = (ListView) findViewById(R.id.list);

        WordAdapter phrasesAdapter = new WordAdapter(this, wordsPhrasesArray, R.color.category_phrases);

        phrasesListView.setAdapter(phrasesAdapter);


        phrasesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                Word temp_word = wordsPhrasesArray.get(position);

//                mediaP = MediaPlayer.create(PhrasesActivity.this, temp_word.getSoundPath());

                audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                int result = audioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    mediaP = MediaPlayer.create(PhrasesActivity.this, temp_word.getSoundPath());
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
