package com.smiledon.library.view.datepicker;

import android.content.Context;

public abstract class Language {
    private static Language sLanguage = null;

    public static Language getLanguage(Context context) {
        if (null == sLanguage) {
            String locale = "CN";
            if (locale.equals("CN")) {
                sLanguage = new CN();
            } else {
                sLanguage = new EN();
            }
        }
        return sLanguage;
    }

    public abstract String[] monthTitles();

    public abstract String ensureTitle();
}
