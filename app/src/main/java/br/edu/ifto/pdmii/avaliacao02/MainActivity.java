package br.edu.ifto.pdmii.avaliacao02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import br.edu.ifto.pdmii.avaliacao02.model.Pokemon;
import br.edu.ifto.pdmii.avaliacao02.model.Scene;
import br.edu.ifto.pdmii.avaliacao02.services.BackgroundMusicService;
import br.edu.ifto.pdmii.avaliacao02.text.PlayerNameTextWatcher;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText playerNameEditText;
    private SwitchMaterial showHintsSwitch;
    private MaterialButton joinBattleButton;

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

            joinBattleButton = findViewById(R.id.button_join_battle);
            showHintsSwitch = findViewById(R.id.switch_show_hints);
            playerNameEditText = findViewById(R.id.edit_player);

            playerNameEditText.addTextChangedListener(new PlayerNameTextWatcher(
                    joinBattleButton, showHintsSwitch));

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

        editor.putString("PLAYER_NAME", Objects.requireNonNull(
                playerNameEditText.getText()).toString());
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
