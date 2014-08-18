package at.fhooe.mc.android.fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import at.fhooe.mc.android.karaokefh.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FragmentHits extends Fragment {

    @Override
    public View onCreateView( LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState ) {
        return _inflater.inflate(R.layout.fragment_hits, null);
    }
}