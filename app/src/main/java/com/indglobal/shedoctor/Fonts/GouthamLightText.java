package com.indglobal.shedoctor.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Android on 8/29/16.
 */
public class GouthamLightText extends TextView {
    public GouthamLightText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/GothamRoundedLight.ttf"));
    }
}
