
package br.edu.infnet.lightcontrol.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fri implements Parcelable {

    @SerializedName("set")
    @Expose
    private Integer set;
    @SerializedName("time")
    @Expose
    private String time;

    protected Fri(Parcel in) {
        if (in.readByte() == 0) {
            set = null;
        } else {
            set = in.readInt();
        }
        time = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Fri> CREATOR = new Creator<Fri>() {
        @Override
        public Fri createFromParcel(Parcel in) {
            return new Fri(in);
        }

        @Override
        public Fri[] newArray(int size) {
            return new Fri[size];
        }
    };

    public Integer getSet() {
        return set;
    }

    public void setSet(Integer set) {
        this.set = set;
    }

    public Fri withSet(Integer set) {
        this.set = set;
        return this;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Fri withTime(String time) {
        this.time = time;
        return this;
    }

}
