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


public class FragmentNumbers extends Fragment {

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

            Toast.makeText(getActivity(), "Sound finished", Toast.LENGTH_SHORT).show();

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
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                mediaP.pause();
                mediaP.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mediaP.pause();
                mediaP.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mediaP.start();
            }
        }
    };

    public FragmentNumbers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.word_list,container,false);



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

        wordsArray.add(0, new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        wordsArray.add(1, new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
        wordsArray.add(2, new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        wordsArray.add(3, new Word("four", "oyiisa", R.drawable.number_four, R.raw.number_four));
        wordsArray.add(4, new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        wordsArray.add(5, new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        wordsArray.add(6, new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        wordsArray.add(7, new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        wordsArray.add(8, new Word("nine", "wo`e", R.drawable.number_nine, R.raw.number_nine));
        wordsArray.add(9, new Word("ten", "na`aacha", R.drawable.number_ten, R.raw.number_ten));


        final ListView listView = (ListView) rootView.findViewById(R.id.list);
//        GridView gridView = (GridView) findViewById(R.id.gridView);

        WordAdapter itemsAdapter = new WordAdapter(getActivity(), wordsArray, R.color.category_numbers);

        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                Word word = wordsArray.get(position);
//                mediaP = MediaPlayer.create(NumbersActivity.this, word.getSoundPath());

                audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

                int result = audioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaP = MediaPlayer.create(getActivity(), word.getSoundPath());
                    mediaP.start();
                    mediaP.setOnCompletionListener(mCompleteListener);
                }
            }
        });

        return  rootView;
    }


    @Override
    public void onStop() {
        super.onStop();

        releaseMediaPlayer();
    }
}
