package bit.stewasc3.webservices;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity
{
	RelativeLayout mLayout;
	ArrayList<String> artists;
	ArrayAdapter<String> adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLayout = (RelativeLayout)findViewById(R.id.root_layout);
		artists = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, artists);
		
		ListView lv = new ListView(this);
		lv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		lv.setAdapter(adapter);
		mLayout.addView(lv);
		
		// Getting JSON
		FetchJsonTask jsonThread = new FetchJsonTask();
		jsonThread.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class FetchJsonTask extends AsyncTask<Void, Void, String>
	{
		String jsonString = null;
		@Override
		protected String doInBackground(Void... arg0)
		{
			String urlString = "http://ws.audioscrobbler.com/2.0/?method=chart.getTopArtists&format=json&api_key=58384a2141a4b9737eacb9d0989b8a8c";
			try
			{
				URL URLObject = new URL(urlString);
				HttpURLConnection connection = (HttpURLConnection) URLObject.openConnection();
				connection.connect();
				
				if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
					return null;
				
				InputStream inputStream = connection.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				
				String responseString;
				StringBuilder sb = new StringBuilder();
				while((responseString = bufferedReader.readLine()) != null)
				{
					sb.append(responseString);
				}
				jsonString = sb.toString();
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return jsonString;
		}
		
		protected void onPostExecute(String jsonString)
		{
			try
			{
				JSONObject jsonObjArtists = new JSONObject(jsonString).getJSONObject("artists");
				JSONArray jsonArrayArtist = jsonObjArtists.getJSONArray("artist");
				
				for(int i = 0; i < jsonArrayArtist.length(); i++)
				{
					JSONObject o = jsonArrayArtist.getJSONObject(i);
					String title = o.getString("name");
					Log.i("Artist", title);
					adapter.add(title);
				}
			} 
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		
	}
}
