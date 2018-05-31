package com.example.tali.hw1.db;

import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.location.Address;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class AddressTypeConverter {

    @TypeConverter
    public static String fromAddress(Address address){
        Gson gson = new Gson();
        String json = gson.toJson(address);
        return json;
    }

    @TypeConverter
    public static Address toAddress(String value){
        Type addressType = new TypeToken<Address>(){}.getType();
        return new Gson().fromJson(value, addressType);
    }

}
