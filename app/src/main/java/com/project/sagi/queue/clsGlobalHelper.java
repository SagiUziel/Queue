package com.project.sagi.queue;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class clsGlobalHelper {
    public static void setLocalLanguage(Context context){
        String languageToLoad  = "heb";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
