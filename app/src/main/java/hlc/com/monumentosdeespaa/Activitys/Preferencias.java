package hlc.com.monumentosdeespaa.Activitys;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import hlc.com.monumentosdeespaa.Fragments.PreferenciasFragment;

/**
 * Created by juanrajc on 15/02/2018.
 */

public class Preferencias extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PreferenciasFragment())
                .commit();
    }
}