// Task 3 - Get top 20 artists thumbnail, name and current listeners.
// Populate a list view with custom layout displaying all three pieces
// of information. Code inspired by Big Nerd Ranch Guide.

package bit.stewasc3.webservices2;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity
{
	FetcherTask mThread;
	ArrayAdapter<Artist> mAdapter;
	ArrayList<Artist> mArtists;
	ListView mListView;
	ImageDownloader<ImageView> mImageThread;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mListView = (ListView)findViewById(R.id.listView);
		
		// Call asynchronous task to fetch artist information
		mThread = new FetcherTask();
		mThread.execute();
	
		// Start ImageDownloader thread. Pass Handler to enable ImageDownloader to make changes
		// in UI thread.
		mImageThread = new ImageDownloader<ImageView>(new Handler());
		
		// Implements ImageDownloader's interface. Alternative (haven't tested): implement interface in class definition,
		// send ImageDownloader constructor reference to MainActivity, cast activity reference to Listener,
		// and assign to ImageDownloader's mListener instance variable. Using this method decouples classes.
		mImageThread.setListener(new ImageDownloader.Listener<ImageView>()
		{
			public void onImageDownloaded(ImageView iv, Bitmap bm)
			{
				iv.setImageBitmap(bm);
			}
		});
		mImageThread.start();
		mImageThread.getLooper();
	}
	
	@Override
	// Thread must be explicitly terminated.
	public void onDestroy()
	{
		super.onDestroy();
		mImageThread.quit();
	}
	
	// Adapter for artist ListView.
	private class ArtistAdapter extends ArrayAdapter<Artist>
	{
		public ArtistAdapter(ArrayList<Artist> artists)
		{
			// Context, resource ID, objects.
			// 0 used, custom layout inflated.
			super(MainActivity.this, 0, artists);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if(convertView == null)
			{
				convertView = MainActivity.this.getLayoutInflater()
						.inflate(R.layout.artist_list_item, parent, false);
			}
			
			ImageView iv = (ImageView)convertView.findViewById(R.id.list_imageView);
			Artist artist = getItem(position);
			TextView name = (TextView)convertView.findViewById(R.id.name_textView);
			name.setText(artist.getName());
			TextView listeners = (TextView)convertView.findViewById(R.id.listeners_textView);
			listeners.setText(artist.getListeners());
			
			// Add image to ImageDownloader's message queue for download.
			mImageThread.queueImage(iv, artist.getImgUrl());
			return convertView;
		}
	}
	
	private class FetcherTask extends AsyncTask<Void, Void, ArrayList<Artist>>
	{
		@Override
		protected ArrayList<Artist> doInBackground(Void... arg0)
		{
			return new ArtistFetcher().fetchArtists();
		}

		@Override
		protected void onPostExecute(ArrayList<Artist> result)
		{
			mArtists = result;
			mListView.setAdapter(new ArtistAdapter(mArtists));
		}
	}
}
