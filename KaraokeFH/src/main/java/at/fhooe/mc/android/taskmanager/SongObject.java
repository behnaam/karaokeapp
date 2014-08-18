package at.fhooe.mc.android.taskmanager;

import org.json.JSONException;
import org.json.JSONObject;

public class SongObject {
	int timestamp;
	String phrase;
	
	public SongObject(JSONObject jsonObject) {
        try {
			timestamp = jsonObject.getInt("ts");
			phrase = jsonObject.getString("phrase");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public int getTimestamp() {
		return timestamp;
	}

	public String getPhrase() {
		return phrase;
	}
}
