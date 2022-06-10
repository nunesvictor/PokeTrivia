package br.edu.ifto.pdmii.avaliacao02.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Pokemon implements Parcelable {
    private Integer id;

    @SerializedName("base_experience")
    private Integer baseExperience;

    private String name;

    private SpriteApiResouce sprites;

    protected Pokemon(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            baseExperience = null;
        } else {
            baseExperience = in.readInt();
        }
        name = in.readString();
        sprites = in.readParcelable(SpriteApiResouce.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (baseExperience == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(baseExperience);
        }
        dest.writeString(name);
        dest.writeParcelable(sprites, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Pokemon> CREATOR = new Creator<Pokemon>() {
        @Override
        public Pokemon createFromParcel(Parcel in) {
            return new Pokemon(in);
        }

        @Override
        public Pokemon[] newArray(int size) {
            return new Pokemon[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBaseExperience() {
        return baseExperience;
    }

    public void setBaseExperience(Integer baseExperience) {
        this.baseExperience = baseExperience;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SpriteApiResouce getSprites() {
        return sprites;
    }

    public void setSprites(SpriteApiResouce sprites) {
        this.sprites = sprites;
    }
}
