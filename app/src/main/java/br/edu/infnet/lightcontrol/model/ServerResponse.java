package br.edu.infnet.lightcontrol.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ServerResponse implements Parcelable {
    public Payload payload;

    protected ServerResponse(Parcel in) {
        payload = in.readParcelable(Payload.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(payload, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServerResponse> CREATOR = new Creator<ServerResponse>() {
        @Override
        public ServerResponse createFromParcel(Parcel in) {
            return new ServerResponse(in);
        }

        @Override
        public ServerResponse[] newArray(int size) {
            return new ServerResponse[size];
        }
    };
}
