package ru.codegeek.food;

import android.os.Parcel;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

/**
 * Created by Роман on 06.12.2015.
 */
public class City {
    int id;
    String name;

    @Override
    public String toString() {
        return name;
    }

}
