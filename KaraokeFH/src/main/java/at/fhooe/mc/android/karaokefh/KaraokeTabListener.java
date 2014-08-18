package at.fhooe.mc.android.karaokefh;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class KaraokeTabListener implements ActionBar.TabListener {

    private Fragment fragment = null;

    public KaraokeTabListener( Fragment _frag) {
        fragment = _frag;
    }

    @Override
    public void onTabReselected(Tab _tab, FragmentTransaction _ft) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onTabSelected(Tab _tab, FragmentTransaction _ft) {
        // TODO Auto-generated method stub
        _ft.replace(R.id.main_fragment_loader, fragment );

    }

    @Override
    public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
        // TODO Auto-generated method stub

    }
}

