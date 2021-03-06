package nz.stewpot.photogallery;

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

public class ThumbnailDownloader<Tokene> extends HandlerThread
{
	private final static String TAG = "ThumbnailDownloader";
	public final static int MESSAGE_DOWNLOAD = 0;
	
	Handler mHandler;
	Map<Tokene, String> requestMap = Collections.synchronizedMap(new HashMap<Tokene, String>());
	Handler mResponseHandler;
	Listener<Tokene> mListener;
	
	public interface Listener<Tokene>
	{
		void onThumbnailDownloaded(Tokene token, Bitmap thumbnail);
	}
	
	public void setListener(Listener<Tokene> listener)
	{
		mListener = listener;
	}
	
	public ThumbnailDownloader(Handler responseHandler)
	{
		super(TAG);
		mResponseHandler = responseHandler;
	}
	
	@SuppressLint("HandlerLeak")
	@Override
	// called before the Looper checks the queue for the first time. This makes it a good place to create your Handler implementation
	protected void onLooperPrepared()
	{
		mHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				if (msg.what == MESSAGE_DOWNLOAD)
				{
					@SuppressWarnings("unchecked")
					// Read up on type erasure
					Tokene token = (Tokene) msg.obj;
					Log.i(TAG, "Got a request for url: " + requestMap.get(token));
					handleRequest(token);
				}
			}
		};
	}
	
	public void queueThumbnail(Tokene token, String url)
	{
		Log.i(TAG, "Got a URL: " + url);
		requestMap.put(token, url);
		mHandler.obtainMessage(MESSAGE_DOWNLOAD, token)
			.sendToTarget();
	}
	
	private void handleRequest(final Tokene token)
	{
		try
		{
			final String url = requestMap.get(token);
			if (url == null)
				return;
			byte[] bmBytes = new FlickrFetchr().getUrlBytes(url);
			final Bitmap bitmap = BitmapFactory.decodeByteArray(bmBytes, 0, bmBytes.length);
			Log.i(TAG, "Bitmap created");
			

	        mResponseHandler.post(new Runnable() 
	        	{
	            	public void run() 
	            	{
	            		if (requestMap.get(token) != url)
	            			return;
	            		requestMap.remove(token);
	            		mListener.onThumbnailDownloaded(token, bitmap);
	            	}
	        	});
		}
		catch(IOException ioe)
		{
			Log.e(TAG, "Error downloading image");
		}
	}
	
	public void clearQueue()
	{
	    mHandler.removeMessages(MESSAGE_DOWNLOAD);
	    requestMap.clear();
	}

}
