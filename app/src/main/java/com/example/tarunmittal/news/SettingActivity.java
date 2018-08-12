package com.example.tarunmittal.news;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.news_setting);

            Preference minNews = findPreference(getString(R.string.minimum_news_key));
            bindPreference(minNews);
            Preference orderBy = findPreference(getString(R.string.order_by_key));
            bindPreference(orderBy);
            Preference section = findPreference(getString(R.string.section_key));
            bindPreference(section);
        }

        private void bindPreference(Preference preference) {

            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String prefernceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, prefernceString);

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {

            String stringValue = o.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }

    }

}
