package com.example.oscar.asteroides;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferencias extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        //addPreferencesFromResource(R.xml.preferencias);
        getFragmentManager().beginTransaction().replace(android.R.id.content,new PreferenciasFragment()).commit();
    }
}
