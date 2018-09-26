
package br.edu.infnet.lightcontrol.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mon implements Parcelable {

    @SerializedName("set")
    @Expose
    private Integer set;
    @SerializedName("time")
    @Expose
    private String time;

    protected Mon(Parcel in) {
        if (in.readByte() == 0) {
            set = null;
        } else {
            set = in.readInt();
        }
        time = in.readString();
    }

    public static final Creator<Mon> CREATOR = new Creator<Mon>() {
        @Override
        public Mon createFromParcel(Parcel in) {
            return new Mon(in);
        }

        @Override
        public Mon[] newArray(int size) {
            return new Mon[size];
        }
    };

    public Integer getSet() {
        return set;
    }

    public void setSet(Integer set) {
        this.set = set;
    }

    public Mon withSet(Integer set) {
        this.set = set;
        return this;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Mon withTime(String time) {
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

    @Override
    public String toString() {
        return "Mon{" +
                "set=" + set +
                ", time='" + time + '\'' +
                '}';
    }
}
