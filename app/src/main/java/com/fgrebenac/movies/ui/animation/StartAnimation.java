package com.fgrebenac.movies.ui.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.fgrebenac.movies.R;
import com.fgrebenac.movies.ui.movies.MoviesActivity;

public class StartAnimation extends AppCompatActivity {

    private ImageView logoImageView;
    private TextView moviesTextView;
    private AnimatorSet animatorSet = new AnimatorSet();
    private boolean animationFlag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_animation);
        initViews();
        initListeners();
        playAnimations();
    }

    private void initViews() {
        logoImageView = findViewById(R.id.logoImageView);
        moviesTextView = findViewById(R.id.moviesTextView);
    }

    private void playAnimations() {
        ObjectAnimator animY = ObjectAnimator.ofFloat(logoImageView, "translationY", -2000, 0f);
        animY.setDuration(1000);
        animY.setInterpolator(new BounceInterpolator());

        ObjectAnimator scaleAlpha = ObjectAnimator.ofFloat(moviesTextView, "alpha", 0f, 1f);
        scaleAlpha.setDuration(1000);

        animatorSet.playTogether(animY, scaleAlpha);
        animatorSet.start();
    }

    private void initListeners() {
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                if(animationFlag) animationFlag = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(StartAnimation.this, MoviesActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        animatorSet.cancel();
        animationFlag = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        playAnimations();
    }
}
