package br.edu.ifto.pdmii.avaliacao02.model;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

public class Score {
    public String player;
    public Integer namePoints;
    public Integer experiencePoints;
    public Double averrage;

    public Score() {
    }

    public Score(String player, Integer namePoints, Integer experiencePoints, Double averrage) {
        this.player = player;
        this.namePoints = namePoints;
        this.experiencePoints = experiencePoints;
        this.averrage = averrage;
    }

    @NonNull
    @Override
    public String toString() {
        return "Score{" +
                "player='" + player + '\'' +
                ", namePoints=" + namePoints +
                ", experiencePoints=" + experiencePoints +
                '}';
    }
}
