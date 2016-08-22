package com.example.android.miwok;

/**
 * Created by Vaska on 08.8.2016.
 */
public class Word {


    private String miwokTranslation;
    private String englishTranslation;
    private int iconResourceID = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = -1;
    private int audioPath;


    public String getMiwokTranslation () {
        return(miwokTranslation);
    }

    public String getEnglishTranslation() {
        return(englishTranslation);
    }

    public int getSoundPath() {
        return(audioPath);
    }

    public int getIcon() {
        return(iconResourceID);
    }

    public Word (String miwok, String english, int icon, int audio){
        miwokTranslation=miwok;
        englishTranslation=english;
        iconResourceID=icon;
        audioPath=audio;
    }

    public Word (String miwok, String english, int audio){
        miwokTranslation=miwok;
        englishTranslation=english;
        audioPath=audio;
    }

    public boolean hasImage (){
        return iconResourceID != NO_IMAGE_PROVIDED;
    }
}