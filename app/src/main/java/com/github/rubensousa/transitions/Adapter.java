package com.github.rubensousa.transitions;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.github.rubensousa.transitions.utils.ExpandAnimation;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View expandView;
        boolean expanded;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            expandView = itemView.findViewById(R.id.expandView);
        }

        public void bind() {
            ViewGroup.LayoutParams params = expandView.getLayoutParams();
            params.height = expanded ?
                    itemView.getResources().getDimensionPixelOffset(R.dimen.card_expanded_height)
                    : itemView.getResources().getDimensionPixelOffset(R.dimen.card_default_height);
            expandView.setLayoutParams(params);
        }

        @Override
        public void onClick(View v) {
            int expandedHeight = itemView.getResources()
                    .getDimensionPixelOffset(R.dimen.card_expanded_height);
            int defaultHeight = itemView.getResources()
                    .getDimensionPixelOffset(R.dimen.card_default_height);

            if (expanded) {
                int tmp = defaultHeight;
                defaultHeight = expandedHeight;
                expandedHeight = tmp;
            }

            ExpandAnimation expandAnimation = new ExpandAnimation(expandView, defaultHeight,
                    expandedHeight);
            expandAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            expandAnimation.setDuration(300);
            expandAnimation.start();
            expanded = !expanded;
        }
    }
}
