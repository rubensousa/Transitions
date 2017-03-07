package com.github.rubensousa.transitions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.github.rubensousa.transitions.utils.TransitionUtils;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Adapter.OnClickListener {

    private RecyclerView recyclerView;
    private View rippleView;
    private FloatingActionButton fab;
    private boolean launchedActivity;
    private int viewPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupTransitions();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        setupRecyclerView();
        rippleView = findViewById(R.id.rippleView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TransitionUtils.isAtLeastLollipop()) {
                    startRippleTransitionReveal();
                } else {
                    startActivity();
                }
            }
        });

        // Workaround for orientation change issue
        if (savedInstanceState != null) {
            viewPosition = savedInstanceState.getInt("position");
        }

        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                if (sharedElements.isEmpty()) {
                    View view = recyclerView.getLayoutManager().findViewByPosition(viewPosition);
                    if (view != null) {
                        sharedElements.put(names.get(0), view.findViewById(R.id.circleView));
                    }
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", viewPosition);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startRippleTransitionReveal() {
        fab.setVisibility(View.INVISIBLE);
        Animator animator = ViewAnimationUtils.createCircularReveal(rippleView,
                (int) fab.getX() + fab.getWidth() / 2,
                (int) fab.getY(), fab.getWidth() / 2, TransitionUtils.getViewRadius(rippleView) * 2);
        rippleView.setVisibility(View.VISIBLE);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(400);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                recyclerView.animate().alpha(0f);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startActivity();
            }
        });
        animator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startRippleTransitionUnreveal() {
        Animator animator = ViewAnimationUtils.createCircularReveal(rippleView,
                (int) fab.getX() + fab.getWidth() / 2,
                (int) fab.getY(), TransitionUtils.getViewRadius(rippleView) * 2, fab.getWidth() / 2);
        rippleView.setVisibility(View.VISIBLE);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(400);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                recyclerView.animate().alpha(1f);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fab.setVisibility(View.VISIBLE);
                rippleView.setVisibility(View.INVISIBLE);
            }
        });
        animator.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && launchedActivity) {
            startRippleTransitionUnreveal();
            launchedActivity = false;
        }
    }

    @Override
    public void onItemClick(View sharedView, String transitionName, int position) {
        viewPosition = position;
        Intent intent = new Intent(this, TransitionActivity.class);
        intent.putExtra("transition", transitionName);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, sharedView, transitionName);
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

    private void startActivity() {
        ActivityCompat.startActivity(this, new Intent(this, ToolbarActivity.class),
                ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        launchedActivity = true;
    }

    private void setupTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Fade());
        }
    }

    private void setupRecyclerView() {
        recyclerView.setAdapter(new Adapter(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
