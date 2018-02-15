package hlc.com.monumentosdeespaa.Fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import hlc.com.monumentosdeespaa.R;

/**
 * Created by juanrajc on 15/02/2018.
 */
public class PreferenciasFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }

}