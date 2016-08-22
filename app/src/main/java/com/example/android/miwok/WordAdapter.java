package com.example.android.miwok;

import android.app.Activity;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * Created by Vaska on 08.8.2016.
 */
public class WordAdapter extends ArrayAdapter<Word> {

    private int colorResourceID;

    private static final String LOG_TAG = ArrayAdapter.class.getSimpleName();

    public WordAdapter (Activity context, ArrayList<Word> arrayList, int color){
        super(context, 0, arrayList);
        colorResourceID=color;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        final Word row = getItem(position);

        TextView miwok = (TextView) listItemView.findViewById(R.id.miwok);

        miwok.setText(row.getMiwokTranslation());

        TextView english = (TextView) listItemView.findViewById(R.id.english);

        english.setText(row.getEnglishTranslation());

        ImageView imageIcon = (ImageView) listItemView.findViewById(R.id.imageIcon);


        if (row.hasImage()) {

            imageIcon.setImageResource(row.getIcon());
            imageIcon.setVisibility(View.VISIBLE);
        }
        else
            imageIcon.setVisibility(View.GONE);


        LinearLayout linearL = (LinearLayout) listItemView.findViewById(R.id.wordsLayout);

//        int color = ContextCompat.getColor(getContext(),colorResourceID);
//
//        linearL.setBackgroundColor(color);


        linearL.setBackgroundColor(ContextCompat.getColor(getContext(),colorResourceID));

/*--    Sam koa pocnav so setOnClickListener, ama se pravi na cela niza za site naednas i ne e dobro.
        Poardno e vo activity posebno so onItemClickListener.
--*/
//        linearL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MediaPlayer sound = MediaPlayer.create(getContext(), row.getSoundPath());
//                sound.start();
//            }
//        });

        return listItemView;
    }
}
