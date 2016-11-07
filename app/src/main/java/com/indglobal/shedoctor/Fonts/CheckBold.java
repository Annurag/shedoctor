package com.indglobal.shedoctor.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by Android on 9/24/16.
 */
public class CheckBold extends CheckBox {
    public CheckBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/GothamRoundedBook.ttf"));
    }
}
