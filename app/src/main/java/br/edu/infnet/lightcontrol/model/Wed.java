
package br.edu.infnet.lightcontrol.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wed implements Parcelable {

    @SerializedName("set")
    @Expose
    private Integer set;
    @SerializedName("time")
    @Expose
    private String time;

    protected Wed(Parcel in) {
        if (in.readByte() == 0) {
            set = null;
        } else {
            set = in.readInt();
        }
        time = in.readString();
    }

    public static final Creator<Wed> CREATOR = new Creator<Wed>() {
        @Override
        public Wed createFromParcel(Parcel in) {
            return new Wed(in);
        }

        @Override
        public Wed[] newArray(int size) {
            return new Wed[size];
        }
    };

    public Integer getSet() {
        return set;
    }

    public void setSet(Integer set) {
        this.set = set;
    }

    public Wed withSet(Integer set) {
        this.set = set;
        return this;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Wed withTime(String time) {
        this.time = time;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (set == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(set);
        }
        dest.writeString(time);
    }
}
