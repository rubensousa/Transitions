package com.github.rubensousa.transitions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;


public class ToolbarActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    NestedScrollView nestedScrollView;
    AppBarLayout appbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupTransitions();
        setContentView(R.layout.activity_toolbar);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingLayout);
        if (savedInstanceState == null) {
            nestedScrollView.setAlpha(0f);
            nestedScrollView.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (nestedScrollView.getHeight() != 0) {
                                appbar.setExpanded(true);
                                appbar.addOnOffsetChangedListener(ToolbarActivity.this);
                                nestedScrollView.getViewTreeObserver()
                                        .removeOnGlobalLayoutListener(this);
                                nestedScrollView.animate().setStartDelay(300).alpha(1f);
                                nestedScrollView.setTranslationY(nestedScrollView.getHeight() / 3);
                                nestedScrollView.animate().setStartDelay(300).translationY(0)
                                        .setInterpolator(new AccelerateDecelerateInterpolator());
                            }
                        }
                    });
        }
    }

    private void setupTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade());
        }
    }

    @Override
    public void onBackPressed() {
        AppBarLayout.LayoutParams layoutParams
                = (AppBarLayout.LayoutParams)
                collapsingToolbarLayout.getLayoutParams();
        layoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
        collapsingToolbarLayout.setLayoutParams(layoutParams);

        nestedScrollView.animate()
                .translationY(nestedScrollView.getHeight() / 3)
                .setDuration(200)
                .alpha(0f)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        appbar.setExpanded(false);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        ToolbarActivity.super.onBackPressed();
                    }
                });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            AppBarLayout.LayoutParams layoutParams
                    = (AppBarLayout.LayoutParams)
                    collapsingToolbarLayout.getLayoutParams();
            layoutParams.setScrollFlags(0);
            collapsingToolbarLayout.setLayoutParams(layoutParams);
            appBarLayout.removeOnOffsetChangedListener(this);
        }
    }
}
