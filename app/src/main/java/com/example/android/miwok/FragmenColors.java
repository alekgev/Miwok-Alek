package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class FragmenColors extends Fragment {

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

            Toast.makeText(getActivity(),"Sound finished",Toast.LENGTH_SHORT).show();

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

    public FragmenColors (){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        final ArrayList<Word> colorsArray = new ArrayList<Word>();

        colorsArray.add(0, new Word("red","weṭeṭṭi", R.drawable.color_red, R.raw.color_red));
        colorsArray.add(1, new Word("green","chokokki", R.drawable.color_green, R.raw.color_green));
        colorsArray.add(2, new Word("brown","ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
        colorsArray.add(3, new Word("gray","ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
        colorsArray.add(4, new Word("black","kululli", R.drawable.color_black, R.raw.color_black));
        colorsArray.add(5, new Word("white","kelelli", R.drawable.color_white, R.raw.color_white));
        colorsArray.add(6, new Word("dusty yellow","ṭopiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        colorsArray.add(7, new Word("mustard yellow","chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        final ListView colorsListView = (ListView) rootView.findViewById(R.id.list);

        WordAdapter colorsAdapter = new WordAdapter(getActivity(), colorsArray, R.color.category_colors);

        colorsListView.setAdapter(colorsAdapter);


        colorsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                Word temp_word = colorsArray.get(position);

//                mediaP = mediaP.create(ColorsActivity.this, temp_word.getSoundPath());

                audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

                int result = audioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    mediaP = MediaPlayer.create(getActivity(), temp_word.getSoundPath());
                    mediaP.start();
                    mediaP.setOnCompletionListener(mCompleteListener);
                }
            }
        });

        return rootView;

    }

    @Override
    public void onStop() {
        super.onStop();

        releaseMediaPlayer();
    }
}
