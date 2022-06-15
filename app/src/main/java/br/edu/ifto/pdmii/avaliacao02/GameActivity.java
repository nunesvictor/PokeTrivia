package br.edu.ifto.pdmii.avaliacao02;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import br.edu.ifto.pdmii.avaliacao02.model.Pokemon;
import br.edu.ifto.pdmii.avaliacao02.model.PokemonTrivia;
import br.edu.ifto.pdmii.avaliacao02.model.Scene;
import br.edu.ifto.pdmii.avaliacao02.services.BackgroundMusicService;

public class GameActivity extends AppCompatActivity {
    private static final int[] OST_RESOURCES = {
            R.raw.pokemon_fight_1,
            R.raw.pokemon_fight_2,
            R.raw.pokemon_fight_3,
            R.raw.pokemon_fight_4,
            R.raw.pokemon_fight_5,
    };

    private static final int[] POKEMON_BUTTONS_RESOURCES = {
            R.id.button_pokemon_1,
            R.id.button_pokemon_2,
            R.id.button_pokemon_3,
            R.id.button_pokemon_4,
            R.id.button_pokemon_5,
            R.id.button_pokemon_6,
    };

    private static final int[] XP_BUTTONS_RESOURCES = {
            R.id.button_xp_1,
            R.id.button_xp_2,
            R.id.button_xp_3,
            R.id.button_xp_4,
            R.id.button_xp_5,
            R.id.button_xp_6,
    };

    private final PokemonTrivia trivia = new PokemonTrivia();
    private final ViewHolder viewHolder = new ViewHolder();
    private ArrayList<Pokemon> pokemons;
    private ArrayList<Pokemon> catchedPokemons;
    private boolean showHints;
    private int round;
    private int namePoints;
    private int experiencePoints;

    static class ViewHolder {
        public TextView roundTextView;
        public ImageView pokemonSpriteImageView;
        public MaterialButton[] pokemonNameButtons = new MaterialButton[6];
        public MaterialButton[] pokemonXpButtons = new MaterialButton[6];
        public MaterialButton confirmButton;

        private void uncheckAllExcept(MaterialButton[] haystack, MaterialButton needle) {
            for (MaterialButton button : haystack) {
                if (button.getId() == needle.getId()) {
                    continue;
                }

                button.setChecked(false);
            }
        }

        public void choosePokemonName(MaterialButton needle) {
            uncheckAllExcept(pokemonNameButtons, needle);
        }

        public void choosePokemonXp(MaterialButton needle) {
            uncheckAllExcept(pokemonXpButtons, needle);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initView();
    }

    private void initActivityParameters() {
        Intent intent = getIntent();

        pokemons = intent.getParcelableArrayListExtra("pokemons");
        catchedPokemons = intent.getParcelableArrayListExtra("catchedPokemons");
        round = intent.getIntExtra("round", 0);
        namePoints = intent.getIntExtra("namePoints", 0);
        experiencePoints = intent.getIntExtra("experiencePoints", 0);

        showHints = getSharedPreferences("APP_PREFERENCES", MODE_PRIVATE)
                .getBoolean("SHOW_HINTS", false);
    }

    private void initView() {
        initActivityParameters();
        startBackgroundMusic();

        viewHolder.roundTextView = findViewById(R.id.text_round);
        viewHolder.roundTextView.setText(getString(R.string.text_round_number, round + 1));

        trivia.setPokemon(catchedPokemons.get(round));
        viewHolder.pokemonSpriteImageView = findViewById(R.id.image_pokemon_sprite);

        Picasso.get()
                .load(trivia.getPokemon().getSprites().getFrontDefault())
                .resize(450, 450)
                .into(viewHolder.pokemonSpriteImageView);

        for (int i = 0; i < POKEMON_BUTTONS_RESOURCES.length; i++) {
            viewHolder.pokemonNameButtons[i] = findViewById(POKEMON_BUTTONS_RESOURCES[i]);
            viewHolder.pokemonXpButtons[i] = findViewById(XP_BUTTONS_RESOURCES[i]);
        }

        initConfirmButton();
        initPokemonXpButtons();
        initPokemonNameButtons();
    }

    private void initPokemonNameButtons() {
        MaterialButton[] pokemonNameButtons = viewHolder.pokemonNameButtons;
        MaterialButton confirmButton = viewHolder.confirmButton;
        Set<String> pokemonNameOptions = getPokemonNameOptions();
        int i = 0;

        for (Iterator<String> iterator = pokemonNameOptions.iterator(); iterator.hasNext(); i++) {
            MaterialButton button = pokemonNameButtons[i];
            String pokemonName = iterator.next();

            if (showHints && trivia.getPokemon().getName().equals(pokemonName)) {
                button.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
                button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            }

            button.setText(pokemonName);
            button.setOnClickListener(view -> {
                MaterialButton self = (MaterialButton) view;

                if (self.isChecked()) {
                    viewHolder.choosePokemonName(self);
                } else self.setChecked(true);

                trivia.setGuessedName(self.getText().toString());
                namePoints += trivia.isNameGuessRight() ? 1 : 0;
                confirmButton.setEnabled(trivia.isTriviaAnswered());
            });
        }
    }

    private void initPokemonXpButtons() {
        MaterialButton[] pokemonXpButtons = viewHolder.pokemonXpButtons;
        MaterialButton confirmButton = viewHolder.confirmButton;
        Set<Integer> pokemonXpOptions = getPokemonXpOptions();
        int i = 0;

        for (Iterator<Integer> iterator = pokemonXpOptions.iterator(); iterator.hasNext(); i++) {
            MaterialButton button = pokemonXpButtons[i];
            Integer baseExperience = iterator.next();

            if (showHints && trivia.getPokemon().getBaseExperience().equals(baseExperience)) {
                button.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
                button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            }

            button.setText(String.format(Locale.getDefault(), "%d", baseExperience));
            button.setOnClickListener(view -> {
                MaterialButton self = (MaterialButton) view;

                if (self.isChecked()) {
                    viewHolder.choosePokemonXp(self);
                } else self.setChecked(true);

                trivia.setGuessedXp(Integer.valueOf(self.getText().toString()));
                experiencePoints += trivia.isXpGuessRight() ? 1 : 0;
                confirmButton.setEnabled(trivia.isTriviaAnswered());
            });
        }
    }

    private void initConfirmButton() {
        viewHolder.confirmButton = findViewById(R.id.button_confirm);
        viewHolder.confirmButton.setOnClickListener(view -> {
            Intent intent;

            if (round < 4) {
                intent = new Intent(this, GameActivity.class);

                intent.putExtra("round", round + 1);
                intent.putParcelableArrayListExtra("pokemons", new ArrayList<>(pokemons));
                intent.putParcelableArrayListExtra("catchedPokemons", catchedPokemons);
            } else {
                intent = new Intent(this, FinalActivity.class);
                intent.setAction("SAVE_SCORE");
            }

            intent.putExtra("namePoints", namePoints);
            intent.putExtra("experiencePoints", experiencePoints);

            startActivity(intent);
            stopBackgroundMusic();
            finish();
        });
    }

    private Set<String> getPokemonNameOptions() {
        Random random = new Random();
        Set<String> pokemonNameOptions = new HashSet<>();

        pokemonNameOptions.add(trivia.getPokemon().getName());

        while (pokemonNameOptions.size() < 6) {
            Collections.shuffle(pokemons);
            Pokemon pokemon = pokemons.get(random.nextInt(pokemons.size()));

            pokemonNameOptions.add(pokemon.getName());
        }

        return pokemonNameOptions;
    }

    private Set<Integer> getPokemonXpOptions() {
        Random random = new Random();
        Set<Integer> pokemonXpOptions = new HashSet<>();

        pokemonXpOptions.add(trivia.getPokemon().getBaseExperience());

        while (pokemonXpOptions.size() < 6) {
            Collections.shuffle(pokemons);
            Pokemon pokemon = pokemons.get(random.nextInt(pokemons.size()));

            pokemonXpOptions.add(pokemon.getBaseExperience());
        }

        return pokemonXpOptions;
    }

    private void startBackgroundMusic() {
        Intent intent = new Intent(this, BackgroundMusicService.class);
        intent.putExtra("BACKGROUND_SCENE", new Scene(OST_RESOURCES[round]));
        intent.setAction(BackgroundMusicService.START_PLAYBACK_ACTION);

        startService(intent);
    }

    public void stopBackgroundMusic() {
        Intent intent = new Intent(this, BackgroundMusicService.class);
        intent.setAction(BackgroundMusicService.STOP_PLAYBACK_ACTION);

        startService(intent);
    }
}
