package br.edu.ifto.pdmii.avaliacao02;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import br.edu.ifto.pdmii.avaliacao02.adapter.ScoreListAdapter;
import br.edu.ifto.pdmii.avaliacao02.model.Scene;
import br.edu.ifto.pdmii.avaliacao02.model.Score;
import br.edu.ifto.pdmii.avaliacao02.notification.NotificationHandler;
import br.edu.ifto.pdmii.avaliacao02.services.BackgroundMusicService;

public class FinalActivity extends AppCompatActivity {
    private TextToSpeech textToSpeech;
    private String mostRecentUtteranceID;

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

        viewModel.getScoresLiveData().observe(this, scores -> {
            adapter.getData().clear();
            adapter.getData().addAll(scores);
            adapter.notifyDataSetChanged();
        });

        MaterialButton ttsButton = findViewById(R.id.button_tts);
        ttsButton.setOnClickListener(view -> textToSpeech = new TextToSpeech(this, status -> {
            if (textToSpeech.getEngines().size() == 0) {
                Toast.makeText(FinalActivity.this, "No TTS Engines Installed", Toast.LENGTH_LONG).show();
            } else if (status == TextToSpeech.SUCCESS) {
                StringBuilder builder = new StringBuilder();

                for (Score score : adapter.getData()) {
                    builder.append(String.format(Locale.getDefault(),
                            "jogador: %s, pontuação final: %.2f\n", score.player, score.averrage));
                }

                ttsInitialized(builder.toString());
            }
        }));

        MaterialButton quitButton = findViewById(R.id.button_quit_game);
        quitButton.setOnClickListener(view -> {
            stopBackgroundMusic();
            finish();
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
                            NotificationHandler.throwNotification(this, null, "Sua pontuação foi salva!");
                        } else
                            Toast.makeText(this, "Erro ao registrar pontuação", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void ttsInitialized(String text) {
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                stopBackgroundMusic();
            }

            @Override
            public void onDone(String utteranceId) {
                if (!utteranceId.equals(mostRecentUtteranceID)) {
                    return;
                }

                startBackgroundMusic();
            }

            @Override
            public void onError(String utteranceId) {
            }
        });

        textToSpeech.setLanguage(new Locale("pt", "BR"));
        mostRecentUtteranceID = (new Random().nextInt() % 9999999) + "";

        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, mostRecentUtteranceID);

        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params, mostRecentUtteranceID);
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
