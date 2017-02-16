package com.github.rubensousa.transitions;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;


public class ToolbarActivity extends AppCompatActivity {

    private AppBarLayout appbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupTransitions();
        setContentView(R.layout.activity_toolbar);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
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
        super.onBackPressed();
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
