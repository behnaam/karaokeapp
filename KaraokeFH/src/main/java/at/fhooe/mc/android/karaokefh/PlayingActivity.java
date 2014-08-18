package at.fhooe.mc.android.karaokefh;

import java.io.IOException;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnTimedTextListener;
import android.media.TimedText;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import at.fhooe.mc.android.taskmanager.AbstractTask;
import at.fhooe.mc.android.taskmanager.AsyncTaskManager;
import at.fhooe.mc.android.taskmanager.GetJsonTask;
import at.fhooe.mc.android.taskmanager.JsonParser;
import at.fhooe.mc.android.taskmanager.OnTaskCompleteListener;

public class PlayingActivity extends Activity implements OnTaskCompleteListener, View.OnClickListener {

    Button playPauseButton;
    MediaPlayer mediaPlayer;

    // data for song.
    String artist;
    String song;
    String duration;
    String audioUrl;
    // for the lyrics
    TextView previousText;
    TextView currentText;
    TextView nextText;
    // in order not to loose the context for private methods.
    Context mContext;

    TreeMap<Integer,String> Lyrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        mContext = this;
        playPauseButton = (Button) findViewById( R.id.button_play_pause);
        playPauseButton.setOnClickListener( this );

        ActionBar _ab = getActionBar();
        _ab.hide();
        currentText = (TextView) findViewById( R.id.current_phrase);
        previousText = (TextView) findViewById( R.id.last_phrase);
        nextText = (TextView) findViewById( R.id.next_phrase);

        Log.i("KARAOKE", getIntent().getStringExtra("songToPlay"));
        String jsonLink = "http://karaokefh.appspot.com/song/get-song/" + getIntent().getStringExtra("songToPlay");
        AsyncTaskManager taskManager = new AsyncTaskManager(this, this);
        taskManager.setupTask(new GetJsonTask(0, jsonLink, "Downloading lyrics..."));
    }

    @Override
    public void onClick(View view) {
        // check if it's the first time we press the button or not.
        if ( mediaPlayer == null ) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource( audioUrl );
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mediaPlayer.start();
            playPauseButton.setText( R.string.button_pause );

        } else {
            // may we are pausing the song.
            if ( mediaPlayer.isPlaying() ) {
                mediaPlayer.pause();
                playPauseButton.setText( R.string.button_resume );
            } else {
                // continue singing!
                mediaPlayer.start();
                playPauseButton.setText( R.string.button_pause );
            }
        }

        new CheckLyricsTask().execute();
    }


    @Override
    /**
     * This method is called once the task is completed. that means: when we have the JSON with the lyrics.
     */
    public void onTaskComplete(AbstractTask task) {
        String jsonString = task.getResult();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            artist = dataObject.getString("artist");
            song = dataObject.getString("name");
            duration = dataObject.getString("duration");
            audioUrl = dataObject.getString("audio");
            String syncArrayString = dataObject.getString("sync_lyric");
            // now we can set elements in the view.
            TextView _text = null;
            _text = (TextView) findViewById( R.id.band_name_pv );
            _text.setText( artist );
            _text = (TextView) findViewById( R.id.song_name_pv );
            _text.setText( song );
            _text = (TextView) findViewById( R.id.song_duration_pv );
            _text.setText( duration );

            // this is used to store the lyrics in a dict--> ts : lyrics
            Lyrics = new TreeMap<Integer,String>();
            Lyrics = JsonParser.getLyrics(mContext, syncArrayString);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Buffer to stream music.
     */
    public OnBufferingUpdateListener mediaBufferListener = new OnBufferingUpdateListener() {

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            currentText.setText(Lyrics.get(mp.getCurrentPosition()));

            if(Lyrics.get(mp.getCurrentPosition())!=null)
                Log.i("KARAOKE", Lyrics.get(mp.getCurrentPosition()));
        }
    };

    /**
     * Using this we set the lyrics we have to sing at this moment.
     */
    public OnTimedTextListener mediaListener = new MediaPlayer.OnTimedTextListener() {

        @Override
        public void onTimedText(MediaPlayer mp, TimedText text) {
            currentText.setText(Lyrics.get(mp.getCurrentPosition()));
            Log.i("KARAOKE", Lyrics.get(mp.getCurrentPosition()));

        }
    };

    /**
     * This class manages all the things related with the sync between the lyrics and the song.
     */
    private class CheckLyricsTask extends AsyncTask<Void,Void,Void>
    {

        int[] ts;
        int currentPosition = 0;
        Set<Integer> timestamps;

        public CheckLyricsTask() {
            timestamps = Lyrics.keySet();
            ts = new int[timestamps.size()];

            int index = 0;

            // Here we create an array with all the kes of the dict in order to get all TS
            for( Integer i : timestamps ) {
                ts[index++] = i;
            }
        }

        /**
         * We need a background task to execute
         * @param params
         * @return
         */
        @Override
        protected Void doInBackground(Void... params) {
            while(mediaPlayer.isPlaying()) {
                Log.i("KARAOKE", timestamps.size()+" " + ts.length + " " + Lyrics.size());

                // check if we have more lyrics or not.
                if(currentPosition < timestamps.size()-1) {
                    Log.i("KARAOKE", currentPosition + " " + ts[0] + " "+ System.currentTimeMillis());
                    // we have? great, check if the current position of media player is between the current TS and the next TS
                    if (mediaPlayer.getCurrentPosition() >= ts[currentPosition] && mediaPlayer.getCurrentPosition() < ts[currentPosition+1]) {
                        // we are in, so change the texts on the view.
                        runOnUiThread(new Runnable() {
                            public void run() {
                                currentText.setText(Lyrics.get(ts[currentPosition]));
                                if(currentPosition>0) {
                                    previousText.setText(Lyrics.get(ts[currentPosition-1]));
                                }
                                nextText.setText(Lyrics.get(ts[currentPosition+1]));
                            }
                        });
                    } else {
                        // increase to the next lyric, because we already passed the current TS.
                        currentPosition++;
                    }
                } else {
                    // last lyrics: set text and invalidate currentText
                    runOnUiThread(new Runnable() {
                        public void run() {
                            previousText.setText(Lyrics.get(ts[currentPosition-1]));
                            currentText.setText(Lyrics.get(ts[currentPosition]));
                            nextText.setText("");
                            currentText.invalidate();
                        }
                    });
                }
            }
            return null;
        }
    }


    @Override
    protected void onPause() {
        // finish the mediaplayer.
        super.onPause();
        if( mediaPlayer != null ) {
            mediaPlayer.stop();
        }
    }
}
