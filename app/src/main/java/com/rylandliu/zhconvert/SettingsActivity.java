package com.rylandliu.zhconvert;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.*;
import com.zqc.opencc.android.lib.ConversionType;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

public class SettingsActivity extends AppCompatActivity {


  private static final String TAG = "SettingsActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.settings_activity);
    FragmentManager manager = getSupportFragmentManager();
    SettingsFragment settings = new SettingsFragment();
    if (savedInstanceState == null) {

      manager.beginTransaction()
          .replace(R.id.convert_settings, settings, "universal")
          .commit();
    }
//    ActionBar actionBar = getSupportActionBar();
//    if (actionBar != null) {
//      actionBar.setDisplayHomeAsUpEnabled(true);
//    }
    SharedPreferences sharedPreferences = getDefaultSharedPreferences(getApplicationContext());


  }

  @Override
  public void onConfigurationChanged(@NotNull Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    SharedPreferences sharedPreferences = getDefaultSharedPreferences(getApplicationContext());
    String lang = sharedPreferences.getString("lang", "auto");
    Log.d(TAG, "onConfigurationChanged: " + lang);
    getApplication().createConfigurationContext(newConfig);
  }

  public static class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
      Context context = getPreferenceManager().getContext();
      PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
      // Preference Catogory
      PreferenceCategory convert = new PreferenceCategory(context);
      convert.setTitle(R.string.convert_header);
      PreferenceCategory langHeader = new PreferenceCategory(context);
      langHeader.setTitle(R.string.universal_key);
      // convert lists
      // List Preference
      ListPreference t2sPreference = new ListPreference(context);
      ListPreference s2tPreference = new ListPreference(context);
      ListPreference langSwitch = new ListPreference(context);

      // add preference list
      t2sPreference.setKey("T2S");
      t2sPreference.setTitle(R.string.tr_to_sp);
      t2sPreference.setEntryValues(new String[]{ConversionType.TW2S.getValue(), ConversionType.HK2S.getValue(), ConversionType.TW2SP.getValue(), ConversionType.T2S.getValue()});
      t2sPreference.setEntries(R.array.t2s_options);
      t2sPreference.setSummaryProvider(ListPreference.SimpleSummaryProvider.getInstance());
//      t2sPreference.setDefaultValue(R.string.t2s);
      t2sPreference.setDefaultValue("t2s.json");
      s2tPreference.setKey("S2T");
      s2tPreference.setTitle(R.string.sp_to_tr);
      s2tPreference.setEntryValues(new String[]{ConversionType.TW2T.getValue(), ConversionType.S2TWP.getValue(), ConversionType.HK2T.getValue(), ConversionType.S2TW.getValue(), ConversionType.S2T.getValue(), ConversionType.T2TW.getValue(), ConversionType.S2HK.getValue()});
      s2tPreference.setEntries(R.array.s2t_options);
      s2tPreference.setSummaryProvider(ListPreference.SimpleSummaryProvider.getInstance());
      s2tPreference.setDefaultValue("s2t.json");
      langSwitch.setKey("lang");
      langSwitch.setTitle(R.string.locale);
      String auto = getResources().getString(R.string.auto);
      langSwitch.setEntries(new String[]{"🇬🇧 English", "🇨🇳 简体中文", "🇹🇼 繁體中文", auto});
      langSwitch.setEntryValues(new String[]{"en", "zh-cn", "zh-tw", "auto"});
      langSwitch.setSummaryProvider(ListPreference.SimpleSummaryProvider.getInstance());
      langSwitch.setDefaultValue("auto");
      langSwitch.setOnPreferenceChangeListener(this.switchLangListener);


      // add to view
      screen.addPreference(convert);
      screen.addPreference(t2sPreference);
      screen.addPreference(s2tPreference);
      screen.addPreference(langHeader);
      screen.addPreference(langSwitch);

      setPreferenceScreen(screen);

    }
    private Preference.OnPreferenceChangeListener switchLangListener = (preference, newValue) -> {
      if (preference.getKey().equals("lang")) {
        Context context = getContext();
        Configuration configuration = getContext().getResources().getConfiguration();
        String lang = newValue.toString();
        switch (lang) {
          case "en":
            configuration.setLocale(Locale.ENGLISH);
            break;
          case "zh-cn":
            configuration.setLocale(Locale.CHINA);
            break;
          case "zh-tw":
            configuration.setLocale(Locale.TAIWAN);
            break;
          default:
            break;
        }
        context.createConfigurationContext(configuration);
        return true;
      }
      return false;
    };

  }
}
