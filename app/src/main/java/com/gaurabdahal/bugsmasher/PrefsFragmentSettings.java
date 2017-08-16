//Gaurab Dahal

package com.gaurabdahal.bugsmasher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by gaurabdahal on 7/11/17.
 */
public class PrefsFragmentSettings extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    Context context;

    public PrefsFragmentSettings(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs_fragment_settings);

        Preference prefShare = getPreferenceScreen().findPreference("key_share");
        prefShare.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Log.i("afdsasf", "enter1");
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Download BugSmasher");
                i.putExtra(Intent.EXTRA_TEXT, "It's a super awesome game. Download it from this link https://play.google.com/store/apps/details?id=com.gaurabdahal.bugsmasher");
                startActivity(Intent.createChooser(i, "Share via"));
                return true;
            }
        });
    }

    public void onResume(){
            super.onResume();
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(getActivity());
            int highscore = sp.getInt("highscore", 0);
            Preference pref;
            pref = getPreferenceScreen().findPreference("key_highscore");
            pref.setSummary("" + highscore);

    }

    public void onPause(){
        super.onPause();

    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
