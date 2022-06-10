package br.edu.ifto.pdmii.avaliacao02.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SpriteApiResouce implements Parcelable {
    @SerializedName("front_default")
    private String frontDefault;

    protected SpriteApiResouce(Parcel in) {
        frontDefault = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(frontDefault);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SpriteApiResouce> CREATOR = new Creator<SpriteApiResouce>() {
        @Override
        public SpriteApiResouce createFromParcel(Parcel in) {
            return new SpriteApiResouce(in);
        }

        @Override
        public SpriteApiResouce[] newArray(int size) {
            return new SpriteApiResouce[size];
        }
    };

    public String getFrontDefault() {
        return frontDefault;
    }

    public void setFrontDefault(String frontDefault) {
        this.frontDefault = frontDefault;
    }
}
