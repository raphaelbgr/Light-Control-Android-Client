
package br.edu.infnet.lightcontrol.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ControlledLight implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("connection")
    @Expose
    private String connection;
    @SerializedName("state")
    @Expose
    private Integer state;
    @SerializedName("stragegy")
    @Expose
    private String stragegy;
    @SerializedName("schedule")
    @Expose
    private Schedule schedule;

    public transient int floor;
    public transient int block;
    public transient String blockName;
    public transient String floorName;

    protected ControlledLight(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readString();
        }
        area = in.readString();
        type = in.readString();
        connection = in.readString();
        if (in.readByte() == 0) {
            state = null;
        } else {
            state = in.readInt();
        }
        stragegy = in.readString();
    }

    public static final Creator<ControlledLight> CREATOR = new Creator<ControlledLight>() {
        @Override
        public ControlledLight createFromParcel(Parcel in) {
            return new ControlledLight(in);
        }

        @Override
        public ControlledLight[] newArray(int size) {
            return new ControlledLight[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ControlledLight withId(String id) {
        this.id = id;
        return this;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public ControlledLight withArea(String area) {
        this.area = area;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ControlledLight withType(String type) {
        this.type = type;
        return this;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public ControlledLight withConnection(String connection) {
        this.connection = connection;
        return this;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public ControlledLight withState(Integer state) {
        this.state = state;
        return this;
    }

    public String getStragegy() {
        return stragegy;
    }

    public void setStragegy(String stragegy) {
        this.stragegy = stragegy;
    }

    public ControlledLight withStragegy(String stragegy) {
        this.stragegy = stragegy;
        return this;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public ControlledLight withSchedule(Schedule schedule) {
        this.schedule = schedule;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(id);
        }
        dest.writeString(area);
        dest.writeString(type);
        dest.writeString(connection);
        if (state == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(state);
        }
        dest.writeString(stragegy);
    }

    @Override
    public String toString() {
        return "ControlledLight{" +
                "id=" + id +
                ", area='" + area + '\'' +
                ", type='" + type + '\'' +
                ", connection='" + connection + '\'' +
                ", state=" + state +
                ", stragegy='" + stragegy + '\'' +
                ", schedule=" + schedule +
                '}';
    }
}
