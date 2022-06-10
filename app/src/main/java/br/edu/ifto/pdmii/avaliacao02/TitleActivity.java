package br.edu.ifto.pdmii.avaliacao02;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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
            Log.i("BackgroundMusicService-Lifecycle", "TitleActivity.onCreate: calling finish");
            stopBackgroundMusic();
            finish();
        });
    }

    private void startBackgroundMusic() {
        Intent intent = new Intent(this, BackgroundMusicService.class);
        intent.putExtra("BACKGROUND_SCENE", new Scene(R.raw.pokemon_theme, 0L, 12900L));
        intent.setAction(BackgroundMusicService.START_PLAYBACK_ACTION);

        Log.i("BackgroundMusicService-Lifecycle", "TitleActivity.startBackgroundMusic: calling startService");
        startService(intent);
    }

    public void stopBackgroundMusic() {
        Intent intent = new Intent(this, BackgroundMusicService.class);
        intent.setAction(BackgroundMusicService.STOP_PLAYBACK_ACTION);

        Log.i("BackgroundMusicService-Lifecycle", "TitleActivity.stopBackgroundMusic: calling startService");
        startService(intent);
    }
}