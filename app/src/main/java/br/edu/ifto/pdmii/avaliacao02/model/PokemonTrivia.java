package br.edu.ifto.pdmii.avaliacao02.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PokemonTrivia implements Parcelable {
    private Pokemon pokemon;
    private String guessedName;
    private Integer guessedXp;

    public PokemonTrivia() {
    }

    protected PokemonTrivia(Parcel in) {
        pokemon = in.readParcelable(Pokemon.class.getClassLoader());
        guessedName = in.readString();
        if (in.readByte() == 0) {
            guessedXp = null;
        } else {
            guessedXp = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(pokemon, flags);
        dest.writeString(guessedName);
        if (guessedXp == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(guessedXp);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PokemonTrivia> CREATOR = new Creator<PokemonTrivia>() {
        @Override
        public PokemonTrivia createFromParcel(Parcel in) {
            return new PokemonTrivia(in);
        }

        @Override
        public PokemonTrivia[] newArray(int size) {
            return new PokemonTrivia[size];
        }
    };

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public String getGuessedName() {
        return guessedName;
    }

    public void setGuessedName(String guessedName) {
        this.guessedName = guessedName;
    }

    public Integer getGuessedXp() {
        return guessedXp;
    }

    public void setGuessedXp(Integer guessedXp) {
        this.guessedXp = guessedXp;
    }

    public boolean isTriviaAnswered() {
        return pokemon != null && guessedName != null && guessedXp != null;
    }

    public boolean isGuessRight() {
        return pokemon.getName().equals(guessedName) &&
                pokemon.getBaseExperience().equals(guessedXp);
    }
}
