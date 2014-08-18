package at.fhooe.mc.android.karaokefh;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import at.fhooe.mc.android.fragments.*;
import at.fhooe.mc.android.taskmanager.AsyncTaskManager;
import at.fhooe.mc.android.taskmanager.GetJsonTask;

public class MainActivity extends Activity {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar _ab = getActionBar();
        _ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        _ab.setDisplayShowHomeEnabled( false );

        //Tab Main Page
        Tab _tab = null;
        _tab = _ab.newTab();
        _tab.setText(getResources().getString(R.string.tab_string_home));
        _tab.setTabListener(new KaraokeTabListener(new FragmentHome()));
        _ab.addTab( _tab );

        // Tab Categories
        _tab = _ab.newTab();
        _tab.setText(getResources().getString(R.string.tab_string_categories));
        _tab.setTabListener(new KaraokeTabListener(new FragmentCategories()));
        _ab.addTab( _tab );

        // Tab Hits
        _tab = _ab.newTab();
        _tab.setText(getResources().getString(R.string.tab_string_hits));
        _tab.setTabListener(new KaraokeTabListener(new FragmentHits()));
        _ab.addTab( _tab );

    }

}
