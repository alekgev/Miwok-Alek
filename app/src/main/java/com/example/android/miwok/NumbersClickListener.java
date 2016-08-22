package com.example.android.miwok;

import android.view.View;
import android.widget.Toast;

/**
 * Created by Vaska on 07.8.2016.
 */
public class NumbersClickListener implements View.OnClickListener {

    @Override
    public void onClick (View view){
        Toast.makeText(view.getContext(),"You just entered Numbers category!", Toast.LENGTH_SHORT).show();
    }
}
