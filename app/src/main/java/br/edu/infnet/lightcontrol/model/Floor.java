
package br.edu.infnet.lightcontrol.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Floor implements Parcelable {

    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("controlled_lights")
    @Expose
    private List<ControlledLight> controlled_lights = new ArrayList<ControlledLight>();

    protected Floor(Parcel in) {
        if (in.readByte() == 0) {
            number = null;
        } else {
            number = in.readInt();
        }
        name = in.readString();
    }

    public static final Creator<Floor> CREATOR = new Creator<Floor>() {
        @Override
        public Floor createFromParcel(Parcel in) {
            return new Floor(in);
        }

        @Override
        public Floor[] newArray(int size) {
            return new Floor[size];
        }
    };

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Floor withNumber(Integer number) {
        this.number = number;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Floor withName(String name) {
        this.name = name;
        return this;
    }

    public List<ControlledLight> getControlled_lights() {
        return controlled_lights;
    }

    public void setControlled_lights(List<ControlledLight> controlled_lights) {
        this.controlled_lights = controlled_lights;
    }

    public Floor withControlled_lights(List<ControlledLight> controlled_lights) {
        this.controlled_lights = controlled_lights;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (number == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(number);
        }
        dest.writeString(name);
    }

    @Override
    public String toString() {
        return "Floor{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", controlled_lights=" + controlled_lights +
                '}';
    }
}
