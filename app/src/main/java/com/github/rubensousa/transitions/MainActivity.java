package com.github.rubensousa.transitions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;

public class MainActivity extends AppCompatActivity {

    private View rippleView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rippleView = findViewById(R.id.rippleView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TransitionUtils.isAtLeastLollipop()) {
                    startRippleTransition();
                } else {
                    startActivity();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startRippleTransition() {
        Animator animator = ViewAnimationUtils.createCircularReveal(rippleView, (int) fab.getX(),
                (int) fab.getY(), 0, TransitionUtils.getViewRadius(rippleView));
        rippleView.setVisibility(View.VISIBLE);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startActivity();
            }
        });
        animator.start();
    }

    private void startActivity() {

    }
}
