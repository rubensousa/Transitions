package com.github.rubensousa.transitions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;


public class ToolbarActivity extends AppCompatActivity {

    NestedScrollView nestedScrollView;
    AppBarLayout appbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupTransitions();
        setContentView(R.layout.activity_toolbar);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        if (savedInstanceState == null) {
            nestedScrollView.setAlpha(0f);
            nestedScrollView.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (nestedScrollView.getHeight() != 0) {
                                nestedScrollView.animate().setStartDelay(300).alpha(1f);
                                nestedScrollView.setTranslationY(nestedScrollView.getHeight() / 3);
                                nestedScrollView.animate().setStartDelay(300).translationY(0)
                                        .setInterpolator(new AccelerateDecelerateInterpolator());
                            }
                        }
                    });
        }
        /*appbar.post(new Runnable() {
            @Override
            public void run() {
                appbar.setExpanded(true);
            }
        });*/
    }

    private void setupTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade());
        }
    }

    @Override
    public void onBackPressed() {
        nestedScrollView.animate().translationY(nestedScrollView.getHeight() / 3)
                .setInterpolator(new AccelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ToolbarActivity.super.onBackPressed();
            }
        });
        /*appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -appBarLayout.getHeight() * 0.6f) {
                    ToolbarActivity.super.onBackPressed();
                    appBarLayout.removeOnOffsetChangedListener(this);
                }
            }
        });
        appbar.setExpanded(false);*/
    }
}
