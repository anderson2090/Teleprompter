package com.usamabennadeemspeechix.usama.speechix.addNewScript.database;

// Script POJO

import android.os.Parcel;
import android.os.Parcelable;

public class Script implements Parcelable {


    private String name;
    private String body;
    private int _id;

    public Script() {
    }

    public Script( String name, String body) {
        this.name = name;
        this.body = body;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.body);
        dest.writeInt(this._id);
    }

    protected Script(Parcel in) {
        this.name = in.readString();
        this.body = in.readString();
        this._id = in.readInt();
    }

    public static final Parcelable.Creator<Script> CREATOR = new Parcelable.Creator<Script>() {
        @Override
        public Script createFromParcel(Parcel source) {
            return new Script(source);
        }

        @Override
        public Script[] newArray(int size) {
            return new Script[size];
        }
    };
}
