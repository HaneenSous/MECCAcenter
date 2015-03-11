package com.mecca_center.app.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import org.androidannotations.annotations.EView;

/**
 * Created by The_Dev on 2/28/2015.
 */
@EView
public class SlideFilter extends LinearLayout {

    private Animation inAnimation;
    private Animation outAnimation;

    public SlideFilter(Context context) {
        super(context);
    }

    public SlideFilter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideFilter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setInAnimation(Animation inAnimation)
    {
        this.inAnimation = inAnimation;
    }

    public void setOutAnimation(Animation outAnimation)
    {
        this.outAnimation = outAnimation;
    }

    @Override
    public void setVisibility(int visibility)
    {
        if (getVisibility() != visibility)
        {
            if (visibility == VISIBLE)
            {
                if (inAnimation != null) startAnimation(inAnimation);
            }
            else if ((visibility == INVISIBLE) || (visibility == GONE))
            {
                if (outAnimation != null) startAnimation(outAnimation);
            }
        }

        super.setVisibility(visibility);
    }


}
