package com.github.rubensousa.transitions.utils;


import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Adapted from: https://coderwall.com/p/35xi3w/layout-change-animations-sliding-height
 */
public class ExpandAnimation extends Animation {

    int fromHeight;
    int toHeight;
    View view;

    public ExpandAnimation(View view, int fromHeight, int toHeight) {
        this.view = view;
        this.fromHeight = fromHeight;
        this.toHeight = toHeight;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        int newHeight;
        if (view.getHeight() != toHeight) {
            newHeight = (int) (fromHeight + ((toHeight - fromHeight) * interpolatedTime));
            view.getLayoutParams().height = newHeight;
            view.requestLayout();
        }
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }

    @Override
    public void start() {
        super.start();
        view.startAnimation(this);
    }
}
