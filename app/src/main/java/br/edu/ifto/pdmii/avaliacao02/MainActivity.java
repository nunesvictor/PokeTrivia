package br.edu.ifto.pdmii.avaliacao02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import br.edu.ifto.pdmii.avaliacao02.model.Pokemon;
import br.edu.ifto.pdmii.avaliacao02.model.Scene;
import br.edu.ifto.pdmii.avaliacao02.model.Score;
import br.edu.ifto.pdmii.avaliacao02.services.BackgroundMusicService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startBackgroundMusic();

        MainActivityViewModel viewModel = new ViewModelProvider(this).get(
                MainActivityViewModel.class);

        viewModel.getPokemonsLiveData().observe(this, pokemons -> {

            if (pokemons.size() == 0) {
                // Start pokeball spinner view
                setContentView(R.layout.content_main_loader);
                return;
            }

            // Start default view
            setContentView(R.layout.activity_main);

            LottieAnimationView pikachuAnimationView = findViewById(R.id.animation_pikachu);
            pikachuAnimationView.setMinAndMaxFrame(0, 70);
            pikachuAnimationView.playAnimation();

            Button joinBattleButton = findViewById(R.id.button_join_battle);

            joinBattleButton.setOnClickListener(view -> {
                Intent intent = new Intent(this, GameActivity.class);

                Collections.shuffle(pokemons);
                ArrayList<Pokemon> catchedPokemons = new ArrayList<>(pokemons.subList(0, 5));

                intent.putExtra("round", 0);
                intent.putParcelableArrayListExtra("pokemons", new ArrayList<>(pokemons));
                intent.putParcelableArrayListExtra("catchedPokemons", catchedPokemons);

                savePreferences();
                startActivity(intent);
                stopBackgroundMusic();
                finish();
            });
        });
    }

    private void savePreferences() {
        SharedPreferences preferences = getSharedPreferences("APP_PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        TextInputEditText playerNameEditText = findViewById(R.id.edit_player);
        editor.putString("PLAYER_NAME", Objects.requireNonNull(playerNameEditText.getText()).toString());

        SwitchMaterial showHintsSwitch = findViewById(R.id.switch_show_hints);
        editor.putBoolean("SHOW_HINTS", showHintsSwitch.isChecked());
        
        editor.apply();
    }

    private void startBackgroundMusic() {
        Intent intent = new Intent(this, BackgroundMusicService.class);
        intent.putExtra("BACKGROUND_SCENE", new Scene(R.raw.pokemon_theme, 13000L));
        intent.setAction(BackgroundMusicService.START_PLAYBACK_ACTION);

        startService(intent);
    }

    public void stopBackgroundMusic() {
        Intent intent = new Intent(this, BackgroundMusicService.class);
        intent.setAction(BackgroundMusicService.STOP_PLAYBACK_ACTION);

        startService(intent);
    }
}
