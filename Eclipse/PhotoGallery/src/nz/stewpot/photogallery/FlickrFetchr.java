package nz.stewpot.photogallery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.net.Uri;
import android.util.Log;

public class FlickrFetchr
{
	private static final String TAG = "FlickrFetchr";
	private static final String ENDPOINT = "https://api.flickr.com/services/rest/"; 
	private static final String API_KEY = "779977fa4e3ba66422116192cdd6dc02";
	private static final String METHOD_GET_RECENT = "flickr.photos.getRecent"; 
	private static final String PARAM_EXTRAS = "extras";
	// Include URL for small image if available
	private static final String EXTRA_SMALL_URL = "url_s";
	private static final String XML_PHOTO = "photo";
	    
	byte[] getUrlBytes(String urlSpec) throws IOException 
	{
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) 
            {
                return null;
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            
            //Input stream will yield bytes as they become available
            while ((bytesRead = in.read(buffer)) > 0) 
            {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } 
        finally 
        {
            connection.disconnect();
        }
	}
	
    public String getUrl(String urlSpec) throws IOException 
    {
        return new String(getUrlBytes(urlSpec));
    }

    public ArrayList<GalleryItem> fetchItems()
    {
    	ArrayList<GalleryItem> items = new ArrayList<GalleryItem>();
    	try
    	{
    		String url = Uri.parse(ENDPOINT).buildUpon()
    				.appendQueryParameter("method", METHOD_GET_RECENT)
    				.appendQueryParameter("api_key", API_KEY)
    				.appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL)
    				.build().toString();
    		String xmlString = getUrl(url);
    		Log.i(TAG, xmlString);
    		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
    		XmlPullParser parser = factory.newPullParser();
    		parser.setInput(new StringReader(xmlString));
    		parseItems(items, parser);
    	}
    	catch(IOException e)
    	{
    		Log.e(TAG, "Failed to retrieve XML");
    	}
    	catch(XmlPullParserException xppe)
    	{
    		Log.e(TAG, "Failed to parse items");
    	}
    	return items;
    }
    
    public void parseItems(ArrayList<GalleryItem> items, XmlPullParser parser)
    	throws XmlPullParserException, IOException
    {
    	int eventType = parser.next();
    	
    	while(eventType != XmlPullParser.END_DOCUMENT)
    	{
    		if (eventType == XmlPullParser.START_TAG &&
    				XML_PHOTO.equals(parser.getName()))
    		{
    			String id = parser.getAttributeValue(null, "id");
    			String caption = parser.getAttributeValue(null, "title");
    			String smallUrl = parser.getAttributeValue(null, EXTRA_SMALL_URL);
    			GalleryItem item = new GalleryItem(); 
    			item.setId(id);
    			item.setCaption(caption);
    			item.setUrl(smallUrl);
    			items.add(item);
    		}
    		eventType = parser.next();
    	}
    	
    }
}

