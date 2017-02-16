package com.github.rubensousa.transitions;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.github.rubensousa.transitions.utils.TransitionUtils;

public class TransitionActivity extends AppCompatActivity {

    FloatingActionButton fab;
    NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = getWindow().getSharedElementEnterTransition();
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                    fab.setScaleX(0);
                    fab.setScaleY(0);
                    fab.setVisibility(View.INVISIBLE);
                    nestedScrollView.setAlpha(0f);
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    fab.show();
                    nestedScrollView.setTranslationY(
                            TransitionUtils.dpToPixels(TransitionActivity.this, 72));
                    nestedScrollView.animate().alpha(1f).translationY(0)
                            .setInterpolator(new DecelerateInterpolator());
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        } else {
            if (savedInstanceState == null) {
                fab.setScaleX(0);
                fab.setScaleY(0);
                fab.setVisibility(View.INVISIBLE);
                fab.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fab.show();
                    }
                }, 300);
            }
        }
    }

    @Override
    public void onBackPressed() {
        fab.setVisibility(View.INVISIBLE);
        nestedScrollView.animate().alpha(0)
                .setInterpolator(new AccelerateInterpolator())
                .translationY(TransitionUtils.dpToPixels(TransitionActivity.this, 72))
                .start();
        supportFinishAfterTransition();
    }
}
