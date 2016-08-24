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


public class FragmentFamily extends Fragment {

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

    public FragmentFamily () {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        final ArrayList<Word> familyArray = new ArrayList<Word>();

        familyArray.add(0, new Word("father","әpә", R.drawable.family_father, R.raw.family_father));
        familyArray.add(1, new Word("mother","әṭa", R.drawable.family_mother, R.raw.family_mother));
        familyArray.add(2, new Word("son","angsi", R.drawable.family_son, R.raw.family_son));
        familyArray.add(3, new Word("daughter","tune", R.drawable.family_daughter, R.raw.family_daughter));
        familyArray.add(4, new Word("older brother","taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        familyArray.add(5, new Word("younger brother","chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        familyArray.add(6, new Word("older sister","teṭe", R.drawable.family_older_sister, R.raw.family_older_sister));
        familyArray.add(7, new Word("younger sister","kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        familyArray.add(8, new Word("grandmother","ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        familyArray.add(9, new Word("grandfather","paapa", R.drawable.family_grandfather, R.raw.family_grandfather));

        ListView familyListView = (ListView) rootView.findViewById(R.id.list);

        WordAdapter familyAdapter = new WordAdapter(getActivity(), familyArray, R.color.category_family);

        familyListView.setAdapter(familyAdapter);

        familyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                Word word_temp = familyArray.get(position);
//                mediaP = mediaP.create(FamilyActivity.this, word_temp.getSoundPath());


                audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

                int result = audioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    mediaP = MediaPlayer.create(getActivity(), word_temp.getSoundPath());
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
