package bit.stewasc3.webservices2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;

// Fetches top 20 artists name, current listeners, URL for medium sized image.
public class ArtistFetcher
{
	private static final String ROOT = "http://ws.audioscrobbler.com/2.0/?";
	private static final String KEY = "58384a2141a4b9737eacb9d0989b8a8c";
	private static final String METHOD_GET_ARTISTS = "chart.getTopArtists";
	private static final String PARAM_LIMIT = "20";
	private static final String PARAM_FORMAT = "json";
	private static final String TAG = "Json Parser";

	// Parse returned JSON string, init artist objects, add to array.
	private void parseJson(String jsonString, ArrayList<Artist> artists) throws JSONException
	{
        String name;
        String listeners;
        String imageUrl = "";
        JSONArray artistsArray = new JSONObject(jsonString).getJSONObject("artists")
                                                        .getJSONArray("artist");
        for(int i = 0; i < artistsArray.length(); i++)
        {
            JSONObject o = artistsArray.getJSONObject(i);
            name = o.getString("name");
            listeners = o.getString("playcount");
            
            JSONArray a = o.getJSONArray("image");
            for (int j = 0; j < a.length(); j++)
            {
                 JSONObject imageObj = a.getJSONObject(j);
                 if(imageObj.getString("size").compareTo("medium") == 0)
                	 imageUrl = imageObj.getString("#text");
            }
            Artist artist = new Artist();
            artist.setImgUrl(imageUrl);
            artist.setName(name);
            artist.setListeners(listeners);
            artists.add(artist);
        }
	}

	public ArrayList<Artist> fetchArtists()
	{
		ArrayList<Artist> artists = new ArrayList<Artist>();
		try
		{
			// Convenient method for creating paramaterized URL.
			String url = Uri.parse(ROOT).buildUpon()
					.appendQueryParameter("method", METHOD_GET_ARTISTS)
					.appendQueryParameter("api_key", KEY)
					.appendQueryParameter("limit", PARAM_LIMIT)
					.appendQueryParameter("format", PARAM_FORMAT)
					.build().toString();
			// Initialise string from byte array
			String jsonString = new String(getByteArray(url));
			parseJson(jsonString, artists);
			Log.i(TAG, jsonString);
		}
		// Java 7+ can catch multiple exceptions in one block
		catch(IOException|JSONException e)
		{
			Log.e(TAG, "Json retrieval/parse failed");
			e.printStackTrace();
		}
		return artists;
	}
	
	// Returns an array of bytes from an input stream. Public because the ImageDownloader
	// will use this method.
	public byte[] getByteArray(String urlString) throws IOException
	{
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		try
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			InputStream in = conn.getInputStream();
			
			if(conn.getResponseCode() != HttpURLConnection.HTTP_OK)
			{
				return null;
			}
			
			int bytesRead = 0;
			// Buffer length commonly chosen. Buffers recommended due to reading a single byte
			// at a time is inefficient.
			byte[] buffer = new byte[1024];
			
			// read returns number of bytes read as an integer. Use this integer as offset
			// to read byte buffer into output stream. Returns -1 if EOF, breaks loop.
			while ((bytesRead = in.read(buffer)) > 0)
			{
				out.write(buffer, 0, bytesRead);
			}
			out.close();
			return out.toByteArray();
		}
		finally
		{
			// Always disconnect
			conn.disconnect();
		}
	}
}	
