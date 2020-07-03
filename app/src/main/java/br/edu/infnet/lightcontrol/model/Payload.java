
package br.edu.infnet.lightcontrol.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Payload implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("master_switch_state")
    @Expose
    private boolean masterSwitchState;
    @SerializedName("blocks")
    @Expose
    private List<Block> blocks = new ArrayList<Block>();

    protected Payload(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        type = in.readString();
    }

    public static final Creator<Payload> CREATOR = new Creator<Payload>() {
        @Override
        public Payload createFromParcel(Parcel in) {
            return new Payload(in);
        }

        @Override
        public Payload[] newArray(int size) {
            return new Payload[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Payload withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Payload withName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Payload withType(String type) {
        this.type = type;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Payload withAddress(Address address) {
        this.address = address;
        return this;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public Payload withBlocks(List<Block> blocks) {
        this.blocks = blocks;
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
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(type);
    }

    @Override
    public String toString() {
        return "Payload{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", address=" + address +
                ", blocks=" + blocks +
                '}';
    }

    public boolean isMasterSwitchState() {
        return masterSwitchState;
    }

    public void setMasterSwitchState(boolean masterSwitchState) {
        this.masterSwitchState = masterSwitchState;
    }
}
