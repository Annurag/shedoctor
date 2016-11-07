package com.indglobal.shedoctor.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by Android on 9/13/16.
 */
public class GouthamCheck extends CheckBox {

    public GouthamCheck(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/GothamRoundedBook.ttf"));
    }
}
