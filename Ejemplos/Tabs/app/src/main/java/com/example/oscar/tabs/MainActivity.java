package com.example.oscar.tabs;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

public class MainActivity extends FragmentActivity {
private FragmentTabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabHost = findViewById(android.R.id.tabhost);
        if (tabHost != null) {
            tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
            tabHost.addTab(tabHost.newTabSpec("Tab1").setIndicator("legua1"), Tab1.class, null);
            tabHost.addTab(tabHost.newTabSpec("Tab2").setIndicator("legua2"), Tab2.class, null);
            tabHost.addTab(tabHost.newTabSpec("Tab3").setIndicator("legua3"), Tab3.class, null);
        }
    }
}
