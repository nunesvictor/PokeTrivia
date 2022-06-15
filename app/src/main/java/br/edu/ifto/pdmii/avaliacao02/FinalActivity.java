package br.edu.ifto.pdmii.avaliacao02;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import br.edu.ifto.pdmii.avaliacao02.adapter.ScoreListAdapter;
import br.edu.ifto.pdmii.avaliacao02.model.Scene;
import br.edu.ifto.pdmii.avaliacao02.model.Score;
import br.edu.ifto.pdmii.avaliacao02.services.BackgroundMusicService;

public class FinalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        startBackgroundMusic();

        FinalActivityViewModel viewModel = new ViewModelProvider(this)
                .get(FinalActivityViewModel.class);

        ListView scoresListView = findViewById(R.id.list_scores);
        ScoreListAdapter adapter = new ScoreListAdapter(this, new ArrayList<>());
        scoresListView.setAdapter(adapter);

        MaterialButton materialButton = findViewById(R.id.button_quit_game);
        materialButton.setOnClickListener(view -> {
            stopBackgroundMusic();
            finish();
        });

        viewModel.getScoresLiveData().observe(this, scores -> {
            adapter.getData().clear();
            adapter.getData().addAll(scores);
            adapter.notifyDataSetChanged();
        });

        Intent intent = getIntent();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference trivia = database.getReference("trivia");
        int namePoints = intent.getIntExtra("namePoints", 0);
        int experiencePoints = intent.getIntExtra("experiencePoints", 0);

        if (intent.getAction().equals("SAVE_SCORE")) {
            Score score = new Score(
                    getSharedPreferences("APP_PREFERENCES", MODE_PRIVATE)
                            .getString("PLAYER_NAME", "Player"),
                    namePoints,
                    experiencePoints,
                    (double) ((namePoints + experiencePoints) / 2)

            );

            trivia.child("scores")
                    .push()
                    .setValue(score).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Pontuação registrada", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(this, "Erro ao registrar pontuação", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void startBackgroundMusic() {
        Intent intent = new Intent(this, BackgroundMusicService.class);
        intent.putExtra("BACKGROUND_SCENE", new Scene(R.raw.pokemon_ending));
        intent.setAction(BackgroundMusicService.START_PLAYBACK_ACTION);

        startService(intent);
    }

    public void stopBackgroundMusic() {
        Intent intent = new Intent(this, BackgroundMusicService.class);
        intent.setAction(BackgroundMusicService.STOP_PLAYBACK_ACTION);

        startService(intent);
    }
}
