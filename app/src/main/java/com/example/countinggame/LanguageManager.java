package com.example.countinggame;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManager {
    private Context ctx;
    private SharedPreferences sharedPreferences;
    public LanguageManager(Context ctx){
        this.ctx = ctx;
        sharedPreferences = ctx.getSharedPreferences("LANG",Context.MODE_PRIVATE);
    }

    public void updateResource(String code){
        Locale locale = new Locale(code);
        locale.setDefault(locale);
        Resources resources = ctx.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config,resources.getDisplayMetrics());
    }

    public String getLang(){
       return sharedPreferences.getString("lang","en");
    }

    public void setLang(String code){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lang",code);
        editor.commit();
    }
}
