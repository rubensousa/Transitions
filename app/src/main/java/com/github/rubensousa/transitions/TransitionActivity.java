package com.github.rubensousa.transitions;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.view.View;

public class TransitionActivity extends AppCompatActivity {

    FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = getWindow().getSharedElementEnterTransition();
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                    fab.setScaleX(0);
                    fab.setScaleY(0);
                    fab.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    fab.show();
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
        supportFinishAfterTransition();
    }
}
