package com.rylandliu.zhconvert;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.rylandliu.zhconvert.utils.ConfigUtils;
import com.zqc.opencc.android.lib.ChineseConverter;
import com.zqc.opencc.android.lib.ConversionType;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

  ExecutorService executorService = Executors.newSingleThreadExecutor();
  private ConversionType currentConversionType = ConversionType.TW2SP;
  final Intent intent = new Intent();

  //menu view
  @Override
  public boolean onCreateOptionsMenu(@NonNull Menu menu) {
    getMenuInflater().inflate(R.menu.context_menu, menu);

    return true;
  }

  //menu select
  @Override
  public boolean onOptionsItemSelected(MenuItem menuItem) {
    if (menuItem.getItemId() == R.id.settings) {
      Intent intent = new Intent(this, SettingsActivity.class);
      startActivity(intent);
      return true;
    }
    return false;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SharedPreferences sharedPreferences = getDefaultSharedPreferences(getApplicationContext());
    String lang = sharedPreferences.getString("lang", "auto");
    // set lang
    ConfigUtils.setLocale(getResources(),lang);
    setContentView(R.layout.activity_main);

    Spinner spinner = findViewById(R.id.spinner);
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.conversion_type_array, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);

    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
          case 0:
            currentConversionType = ConversionType.TW2SP;
            break;
          case 1:
            currentConversionType = ConversionType.S2HK;
            break;
          case 2:
            currentConversionType = ConversionType.S2T;
            break;
          case 3:
            currentConversionType = ConversionType.S2TW;
            break;
          case 4:
            currentConversionType = ConversionType.S2TWP;
            break;
          case 5:
            currentConversionType = ConversionType.T2HK;
            break;
          case 6:
            currentConversionType = ConversionType.T2S;
            break;
          case 7:
            currentConversionType = ConversionType.T2TW;
            break;
          case 8:
            currentConversionType = ConversionType.TW2S;
            break;
          case 9:
            currentConversionType = ConversionType.HK2S;
            break;
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    final EditText textView = findViewById(R.id.text);

    findViewById(R.id.btn).setOnClickListener(v -> {
      String originalText = textView.getText().toString();
      Runnable runnable = () -> {
        final String converted = ChineseConverter.convert(originalText,
            currentConversionType, getApplicationContext());
        textView.post(() -> textView.setText(converted));
      };
      executorService.execute(runnable);
    });

  }
}
