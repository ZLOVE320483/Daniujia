package com.xiaojia.daniujia.domain;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

/**
 * Created by Administrator on 2016/4/21
 */
public class SerializableMap implements Parcelable {
    private SparseArray<Object> map;

    public SerializableMap(Parcel in) {
        map = in.readSparseArray(in.getClass().getClassLoader());
    }

    public SerializableMap(){

    }
    public static final Creator<SerializableMap> CREATOR = new Creator<SerializableMap>() {
        @Override
        public SerializableMap createFromParcel(Parcel in) {
            return new SerializableMap(in);
        }

        @Override
        public SerializableMap[] newArray(int size) {
            return new SerializableMap[size];
        }
    };


    public SparseArray<Object> getMap(){
        return  map;
    }
    public void setMap (SparseArray<Object> map){
        this.map = map;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSparseArray(map);
    }
}
