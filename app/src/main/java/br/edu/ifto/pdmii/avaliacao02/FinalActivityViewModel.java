package br.edu.ifto.pdmii.avaliacao02;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.edu.ifto.pdmii.avaliacao02.model.Score;

public class FinalActivityViewModel extends ViewModel {
    private final MutableLiveData<List<Score>> scoresLiveData = new MutableLiveData<>(new ArrayList<>());

    public FinalActivityViewModel() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference trivia = database.getReference("trivia");

        trivia.child("scores").orderByChild("averrage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Score> newData = scoresLiveData.getValue();
                assert newData != null;

                for (DataSnapshot scoreSnapshot : snapshot.getChildren()) {
                    newData.add(scoreSnapshot.getValue(Score.class));
                }

                Collections.reverse(newData);
                scoresLiveData.postValue(newData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public MutableLiveData<List<Score>> getScoresLiveData() {
        return scoresLiveData;
    }
}
