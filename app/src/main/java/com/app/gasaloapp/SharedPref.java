package com.app.gasaloapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref
{
    SharedPreferences sharedPreferences;
    public SharedPref(Context context)
    {
        sharedPreferences=context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
    }
    public void saveQrCode(String qr)
    {
        sharedPreferences.edit().putString("QR",qr).apply();
    }
    public String getQrCode()
    {
        return sharedPreferences.getString("QR","");
    }

}
