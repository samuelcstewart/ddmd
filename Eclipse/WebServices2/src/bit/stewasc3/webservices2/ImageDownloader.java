package bit.stewasc3.webservices2;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

/* Token will be ImageView associated with each downloaded image.
 * Ultimately, I think this class would of worked without using a generic type. It is nice to have a portable implementation however.
 * No risk of handler leak since handler is explicit member of ImageDownloader.
 * Code inspired by The Big Nerd Ranch Guide. */
@SuppressLint("HandlerLeak")
public class ImageDownloader<Token> extends HandlerThread
{
	public static final int MSG_DOWNLOAD = 0;
	Handler mHandler;
	Handler mResponseHandler;
	Listener<Token> mListener;
	Map<Token, String> requestMap = Collections.synchronizedMap(new HashMap<Token, String>());
	public interface Listener<Token> 
	{
		void onImageDownloaded(Token token, Bitmap bm);
	}
	
	// Constructor accepts reference to handler from UI thread.
	public ImageDownloader(Handler responseHandler)
	{
		super("ImageDownloader");
		mResponseHandler = responseHandler;
	}
	
	public void setListener(Listener<Token> listener)
	{
		mListener = listener;
	}
	
	@Override
	protected void onLooperPrepared()
	{
		// Initialise message handler
		mHandler = new Handler()
		{
			@Override
			// handler implementation
			public void handleMessage(Message msg)
			{
				// If message is an image download
				if (msg.what == MSG_DOWNLOAD)
				{
					// TODO: read up on type erasure
					// Get message object, call handle
					Token token = (Token)msg.obj;
					handleRequest(token);
				}
			}
		};
	}
	
	private void handleRequest(final Token token)
	{
		try
		{
			// Get URL string associated with token from map.
			final String url = requestMap.get(token);
			if(url == null) return;
			
			byte[] bmBytes = new ArtistFetcher().getByteArray(url);
			final Bitmap bm = BitmapFactory.decodeByteArray(bmBytes, 0, bmBytes.length);
		
			// Post message on UI thread's message queue
			mResponseHandler.post(new Runnable() 
			{
	            public void run() 
	            {
	            	// Ensures ImageView has not been recycled by the ListView..
	                if (requestMap.get(token) != url)
	                    return;
	                // Remove token from request map.
	                requestMap.remove(token);
	                mListener.onImageDownloaded(token, bm);
	            }
			});
		}
		catch(IOException e)
		{
			Log.e("ImageDownloader", "Error retrieving image");
		}
	}

	// Public interface for adding to message queue
	public void queueImage(Token token, String url)
	{
		// Add token mapping to URL
		requestMap.put(token, url);
		
		// Creates message, send to handler. Handler adds message to looper's queue. 
		mHandler.obtainMessage(MSG_DOWNLOAD, token)
			.sendToTarget();
	}
}
