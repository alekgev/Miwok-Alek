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
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;


public class NumbersActivity extends AppCompatActivity {
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

            Toast.makeText(NumbersActivity.this,"Sound finished",Toast.LENGTH_SHORT).show();

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

//        ArrayList<String> words = new ArrayList<String>();
//
//        words.add(0, "one");
//        words.add(1, "two");
//        words.add(2, "three");
//        words.add(3, "four");
//        words.add(4, "five");
//        words.add(5, "six");
//        words.add(6, "seven");
//        words.add(7, "eight");
//        words.add(8, "nine");
//        words.add(9, "ten");

//        Word wordsW = new Word("one","");

        final ArrayList<Word> wordsArray = new ArrayList<Word>();

        wordsArray.add(0, new Word("one","lutti", R.drawable.number_one, R.raw.number_one));
        wordsArray.add(1, new Word("two","otiiko", R.drawable.number_two, R.raw.number_two));
        wordsArray.add(2, new Word("three","tolookosu", R.drawable.number_three, R.raw.number_three));
        wordsArray.add(3, new Word("four","oyiisa", R.drawable.number_four, R.raw.number_four));
        wordsArray.add(4, new Word("five","massokka", R.drawable.number_five, R.raw.number_five));
        wordsArray.add(5, new Word("six","temmokka", R.drawable.number_six, R.raw.number_six));
        wordsArray.add(6, new Word("seven","kenekaku", R.drawable.number_seven, R.raw.number_seven));
        wordsArray.add(7, new Word("eight","kawinta", R.drawable.number_eight, R.raw.number_eight));
        wordsArray.add(8, new Word("nine","wo`e", R.drawable.number_nine, R.raw.number_nine));
        wordsArray.add(9, new Word("ten","na`aacha", R.drawable.number_ten, R.raw.number_ten));



        final ListView listView = (ListView) findViewById(R.id.list);
//        GridView gridView = (GridView) findViewById(R.id.gridView);

        WordAdapter itemsAdapter = new WordAdapter(this, wordsArray, R.color.category_numbers);

        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                Word word = wordsArray.get(position);
//                mediaP = MediaPlayer.create(NumbersActivity.this, word.getSoundPath());

                audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                int result = audioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    mediaP = MediaPlayer.create(NumbersActivity.this, word.getSoundPath());
                    mediaP.start();
                    mediaP.setOnCompletionListener(mCompleteListener);
                }
            }
        });


//        list.setOnItemClickListener(new WordAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(WordAdapter<?> parent, View view, int position, long id) {
//                Object listItem = list.getItemAtPosition(position);
//            }
//        });


//        gridView.setAdapter(itemsAdapter);

//        LinearLayout rootView = (LinearLayout) findViewById(R.id.rootView);

//        TextView wordView = new TextView(this);
//        wordView.setText(words.get(0));
//        rootView.addView(wordView);

//        while (int i < words.size()) {
//            TextView wordView = new TextView(this);
//            wordView.setText(words.get(i));
//            rootView.addView(wordView);
//            i++;
//        }


//        for (int i=0; i<words.size(); i++){
//            TextView wordView = new TextView(this);
////            Log.v("Vrednost od [" + i +"] ","" + words.get(i));
//            wordView.setText(words.get(i));
//            rootView.addView(wordView);
//        }

//        Log.i("Element 3 = ",words.get(3) + words.size());


//        String[] words = {
//            "one", "two","three","four","five",
//            "six", "seven", "eight","nine","ten" };

//        String [] words = new String [10];
//        words[0]="one";
//        words[1]="two";
//        words[2]="three";
//        words[3]="four";
//        words[4]="five";
//        words[5]="six";
//        words[6]="seven";
//        words[7]="eight";
//        words[8]="nine";
//        words[9]="ten";

//        Log.v("Vrednost od [3]: ",words[3]);

    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
        Log.v("Stopped","OnSTOP");
    }

}

