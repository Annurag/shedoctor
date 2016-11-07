package com.indglobal.shedoctor.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.RadioButton;

/**
 * Created by Android on 9/29/16.
 */
public class RadioGoutam extends RadioButton {
    public RadioGoutam(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/GothamRoundedBook.ttf"));
    }
}
