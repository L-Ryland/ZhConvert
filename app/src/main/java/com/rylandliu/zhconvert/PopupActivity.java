package com.rylandliu.zhconvert;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.zqc.opencc.android.lib.ChineseConverter;
import com.zqc.opencc.android.lib.ConversionType;

import java.util.stream.Stream;

public class PopupActivity extends Activity {

    private static final String TAG = "PopupActivity";
    private ConversionType s2tConfig;
    private ConversionType t2sConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Intent intent = getIntent();
            CharSequence selectText = null;
            // get system Text
            selectText = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
            SharedPreferences sharedPreferences = getDefaultSharedPreferences(getApplicationContext());
            s2tConfig = convertToConvertType(sharedPreferences.getString("S2T", "s2t.json"));
            t2sConfig = convertToConvertType(sharedPreferences.getString("T2S", "t2s.json"));
            if (selectText != null && s2tConfig != null && t2sConfig != null) {
                boolean isTextAbleToConvert = compareInDepth(selectText);
                String converted = isTextAbleToConvert ? ChineseConverter.convert(selectText.toString(),
                        t2sConfig, getApplicationContext()) : ChineseConverter.convert(selectText.toString(), s2tConfig, getApplicationContext());
                Log.d(TAG, "onCreate: " + "converted= " + converted + "s2tConfig= " + s2tConfig.getValue() + " t2sConfig= " + t2sConfig.getValue());
                intent.putExtra(Intent.EXTRA_PROCESS_TEXT, converted);
                setResult(RESULT_OK, intent);
            } else {
                Log.d(TAG, "onCreate: parse error");
            }
            String actionPaste = Intent.ACTION_PASTE;

        } finally {
            finish();
        }
    }

    /**
     * conversion to convert Type
     *
     * @param position string name
     * @return ConversionType
     */
    private ConversionType convertToConvertType(String position) {
        switch (position) {
            case "tw2sp.json":
                return ConversionType.TW2SP;
            case "s2hk.json":
                return ConversionType.S2HK;
            case "s2t.json":
                return ConversionType.S2T;
            case "s2tw.json":
                return ConversionType.S2TW;
            case "s2twp.json":
                return ConversionType.S2TWP;
            case "t2hk.json":
                return ConversionType.T2HK;
            case "t2s.json":
                return ConversionType.T2S;
            case "t2tw.json":
                return ConversionType.T2TW;
            case "tw2s.json":
                return ConversionType.TW2S;
            case "hk2s.json":
                return ConversionType.HK2S;
        }
        return null;
    }

    /**
     * Test if the given CharSequence tested string is able to convert into another one
     *
     * @param original CharSequence tested string
     * @return true if the string is able to convert, or return false.
     */
    private boolean compareInDepth(CharSequence original) {
        Stream<String> toChars = original.chars().mapToObj(c -> String.valueOf((char) c));
        try {
            toChars.forEach(c -> {
                String converted = ChineseConverter.convert(c, t2sConfig, getApplicationContext());
                if (!converted.equals(c)) throw new RuntimeException();
            });
        } catch (RuntimeException e) {
            return true;
        }
        return false;
    }
}
