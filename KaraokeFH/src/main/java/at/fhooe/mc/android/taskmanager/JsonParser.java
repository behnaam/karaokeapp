/**
 * 
 */
package at.fhooe.mc.android.taskmanager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * JSON class to parse Lyric JSON
 */
public class JsonParser {
	public static TreeMap<Integer, String> getLyrics(Context pContext,String pJson) {
        // dictionary to store lyrics in key : value format.
		TreeMap<Integer, String> _Entries = new TreeMap<Integer, String>();
		
		try {
			JSONArray _EntriesJsonArray = new JSONArray(pJson);
			SongObject _Entry = null;
			
			int _EntriesArrayLentgth = _EntriesJsonArray.length();
			for (int i=0;i<_EntriesArrayLentgth;i++) {
                _Entry = new SongObject(_EntriesJsonArray.getJSONObject(i));
				_Entries.put(_Entry.getTimestamp(), _Entry.getPhrase());
			}
			
		} catch (JSONException e) {
			JSONObject _JsonObject;
			try {
				_JsonObject = new JSONObject(pJson);
				SongObject _Entry = null;
                _Entry = new SongObject(_JsonObject);
				_Entries.put(_Entry.getTimestamp(), _Entry.getPhrase());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		
		return _Entries;
	}

	/**
	 * Grabs a JSON from the server
	 */
	public static String getJsonFile(String p_UrlFormat) 
	{
		StringBuilder _Builder = new StringBuilder();
		
		HttpParams myParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(myParams, 10000);
	    HttpConnectionParams.setSoTimeout(myParams, 10000);
	    myParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpClient _Client = new DefaultHttpClient();

		HttpGet _HttpGet= new HttpGet(p_UrlFormat);
		_HttpGet.setParams(myParams);
		
		try {
			HttpResponse _Response = _Client.execute(_HttpGet);
			StatusLine _StatusLine = _Response.getStatusLine();
			int _StatusCode = _StatusLine.getStatusCode();
			if (_StatusCode == 200) {
				HttpEntity _Entity = _Response.getEntity();
				InputStream content = _Entity.getContent();
				BufferedReader _Reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = _Reader.readLine()) != null) {
					_Builder.append(line);
				}
			} else {
				return null;
			}
		} catch (Exception e) {
            e.printStackTrace();
			return null;
		}

		return _Builder.toString();
	}
}
