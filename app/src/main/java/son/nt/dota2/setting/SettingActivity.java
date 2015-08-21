package son.nt.dota2.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.parse.ParsePush;

import son.nt.dota2.MsConst;
import son.nt.dota2.R;

/**
 * Created by Sonnt on 8/21/15.
 */
public class SettingActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    SharedPreferences sharedPreferences;
    SeekBar volumeBar;
    TextView txtVolume;
    AlertDialog alertDialog;
    PreferenceScreen preferenceScreen;
    int max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.user_settings);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        preferenceScreen = (PreferenceScreen) findPreference("pref_volume");
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        preferenceScreen.setSummary("" + currentVolume + "/" + max);
        preferenceScreen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {


                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = layoutInflater.inflate(R.layout.dialog_settings, null);
                txtVolume = (TextView) view.findViewById(R.id.setting_txt_volume);
                volumeBar = (SeekBar) view.findViewById(R.id.setting_volume);
                view.findViewById(R.id.setting_btn_done).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                volumeBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
                final int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//              audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
                volumeBar.setProgress(currentVolume);
                txtVolume.setText(String.valueOf(currentVolume));
                volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                            txtVolume.setText(String.valueOf(progress));
                            preferenceScreen.setSummary("" + progress + "/" + max);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });


                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this)
                        .setView(view);
                alertDialog = builder.create();
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });
                alertDialog.show();

                return false;
            }
        });
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();

        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if ("pref_get_push".equals(key)) {
            boolean isSub = sharedPreferences.getBoolean("pref_get_push", true);
            try {
                if (isSub) {
                    ParsePush.subscribeInBackground(MsConst.CHANNEL_COMMON);
                } else {
                    ParsePush.unsubscribeInBackground(MsConst.CHANNEL_COMMON);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
