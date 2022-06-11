package br.edu.ifto.pdmii.avaliacao02;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import br.edu.ifto.pdmii.avaliacao02.model.Scene;
import br.edu.ifto.pdmii.avaliacao02.services.BackgroundMusicService;

public class TitleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        startBackgroundMusic();

        Button gameStartButton = findViewById(R.id.button_game_start);
        gameStartButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            stopBackgroundMusic();
            finish();
        });

        animateView();
    }

    public void animateView() {
        final View img = findViewById(R.id.image_pokemon_logo);
        final SpringAnimation anim = new SpringAnimation(img, DynamicAnimation.TRANSLATION_Y, 0);

        anim.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        anim.setStartValue(600f);
        anim.getSpring().setStiffness(SpringForce.STIFFNESS_MEDIUM);

        anim.start();
        anim.addEndListener((animation, canceled, value, velocity) -> {
            final View contentView = findViewById(R.id.button_game_start);
            int longAnimationDuration = getResources().getInteger(
                    android.R.integer.config_longAnimTime);

            contentView.setAlpha(0f);
            contentView.setVisibility(View.VISIBLE);
            contentView.animate()
                    .alpha(1f)
                    .setDuration(longAnimationDuration)
                    .setListener(null);
        });
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