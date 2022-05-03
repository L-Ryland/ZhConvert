package com.rylandliu.zhconvert.utils;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

/**
 * Settings utils
 */
public class ConfigUtils {

    private static final String TAG = ConfigUtils.class.getSimpleName();

    /**
     * set locale
     *
     * @param res
     * @param langCode
     * @return away true
     */
    public static boolean setLocale(Resources res, String langCode) {
        Configuration configuration = res.getConfiguration();
        LocaleList defaultLocales = res.getConfiguration().getLocales();
        switch (langCode) {
            case "en":
                configuration.setLocale(Locale.ENGLISH);
                break;
            case "zh-cn":
                configuration.setLocale(Locale.CHINA);
                break;
            case "zh-tw":
                configuration.setLocale(Locale.TAIWAN);
                break;
            case "auto":
                Locale mLocal = Locale.getDefault();
                configuration.setLocale(mLocal);
                Log.d(TAG, "setLocale: mLocal=" + mLocal);
            default:
                break;
        }
        DisplayMetrics dm = res.getDisplayMetrics();
        res.updateConfiguration(configuration, dm);

        //restart app
        return true;
    }
}
