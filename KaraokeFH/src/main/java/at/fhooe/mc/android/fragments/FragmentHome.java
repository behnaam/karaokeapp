package at.fhooe.mc.android.fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import at.fhooe.mc.android.karaokefh.PlayingActivity;
import at.fhooe.mc.android.karaokefh.R;
import at.fhooe.mc.android.karaokefh.SelectSongSpinnerAdapter;
import at.fhooe.mc.android.karaokefh.Song;
import at.fhooe.mc.android.taskmanager.AbstractTask;
import at.fhooe.mc.android.taskmanager.AsyncTaskManager;
import at.fhooe.mc.android.taskmanager.GetJsonTask;
import at.fhooe.mc.android.taskmanager.JsonParser;
import at.fhooe.mc.android.taskmanager.OnTaskCompleteListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FragmentHome extends Fragment implements View.OnClickListener {

    private SelectSongSpinnerAdapter adapter;
    private Spinner songListSpinner;
    private String songSelected;

    @Override
    public View onCreateView( LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState ) {
        View _v = _inflater.inflate(R.layout.fragment_home, null);

        Button _b = (Button) _v.findViewById(R.id.button_play_song);
        _b.setOnClickListener( this );

        Song[] songList = new Song[3];
        songList[0] = new Song("That's not my name - The Ting Tings", 6001);
        songList[1] = new Song("Don't stop me now - Queen", 8001);
        songList[2] = new Song("Yellow - Coldplay", 9001);

        adapter = new SelectSongSpinnerAdapter( _v.getContext(), android.R.layout.simple_spinner_item, songList);
        songListSpinner = (Spinner) _v.findViewById(R.id.spinner_select_song);
        songListSpinner.setAdapter(adapter);
        songListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Song song = adapter.getItem(position);
                Intent intent= new Intent( getActivity(), PlayingActivity.class);
                // get data from spinner.
                songSelected = String.valueOf( song.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        return _v;
    }


    @Override
    public void onClick(View view) {
        // Launch new intent.
        Intent intent= new Intent( this.getActivity(), PlayingActivity.class);
        // get data from spinner.
        intent.putExtra("songToPlay",songSelected );
        startActivity(intent);
    }
}