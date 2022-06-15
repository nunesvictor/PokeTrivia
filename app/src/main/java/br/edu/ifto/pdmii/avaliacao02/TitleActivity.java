package br.edu.ifto.pdmii.avaliacao02;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import com.google.android.material.button.MaterialButton;

import br.edu.ifto.pdmii.avaliacao02.model.Scene;
import br.edu.ifto.pdmii.avaliacao02.services.BackgroundMusicService;

public class TitleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        startBackgroundMusic();

        MaterialButton gameStartButton = findViewById(R.id.button_game_start);
        gameStartButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            stopBackgroundMusic();
            finish();
        });

        MaterialButton showHistoryButton = findViewById(R.id.button_show_history);
        showHistoryButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, FinalActivity.class);
            intent.setAction("SHOW_HISTORY");

            startActivity(intent);
            stopBackgroundMusic();
            finish();
        });

        animateView();
    }

    public void animateView() {
        final View imageView = findViewById(R.id.image_pokemon_logo);
        final View gameStartButtonView = findViewById(R.id.button_game_start);
        final View showHistoryButtonView = findViewById(R.id.button_show_history);
        final SpringAnimation springAnimation = new SpringAnimation(imageView, DynamicAnimation.TRANSLATION_Y, 0);

        gameStartButtonView.setVisibility(View.INVISIBLE);
        showHistoryButtonView.setVisibility(View.INVISIBLE);

        springAnimation.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnimation.setStartValue(600f);
        springAnimation.getSpring().setStiffness(SpringForce.STIFFNESS_MEDIUM);

        springAnimation.start();
        springAnimation.addEndListener((animation, canceled, value, velocity) ->
                animateContentView(gameStartButtonView)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                animateContentView(showHistoryButtonView);
                            }
                        }));
    }

    private ViewPropertyAnimator animateContentView(View contentView) {
        int longAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);

        contentView.setAlpha(0f);
        contentView.setVisibility(View.VISIBLE);
        return contentView.animate()
                .alpha(1f)
                .setDuration(longAnimationDuration);
    }

    private void startBackgroundMusic() {
        Intent intent = new Intent(this, BackgroundMusicService.class);
        intent.putExtra("BACKGROUND_SCENE", new Scene(R.raw.pokemon_theme, 0L, 12900L));
        intent.setAction(BackgroundMusicService.START_PLAYBACK_ACTION);

        startService(intent);
    }

    public void stopBackgroundMusic() {
        Intent intent = new Intent(this, BackgroundMusicService.class);
        intent.setAction(BackgroundMusicService.STOP_PLAYBACK_ACTION);

        startService(intent);
    }
}