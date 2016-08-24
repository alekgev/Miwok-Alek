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


public class FragmentPhrases extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.word_list, container, false);



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


        ListView phrasesListView = (ListView) rootView.findViewById(R.id.list);

        WordAdapter phrasesAdapter = new WordAdapter(getActivity(), wordsPhrasesArray, R.color.category_phrases);

        phrasesListView.setAdapter(phrasesAdapter);


        phrasesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                Word temp_word = wordsPhrasesArray.get(position);

//                mediaP = MediaPlayer.create(PhrasesActivity.this, temp_word.getSoundPath());

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
