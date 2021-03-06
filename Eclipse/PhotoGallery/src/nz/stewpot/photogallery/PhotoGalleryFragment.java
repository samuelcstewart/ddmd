package nz.stewpot.photogallery;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class PhotoGalleryFragment extends Fragment
{
	private final static String TAG = "PhotoGalleryFragment";
	GridView mGrid;
	ArrayList<GalleryItem> mItems;
	ThumbnailDownloader<ImageView> mThumbnailThread;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		new FetchItemsTask().execute();
	
		// Start thread for thumbnail downloading
		mThumbnailThread = new ThumbnailDownloader<ImageView>(new Handler());
		mThumbnailThread.setListener(new ThumbnailDownloader.Listener<ImageView>()
				{
					@Override
					public void onThumbnailDownloaded(ImageView token,
							Bitmap thumbnail)
					{
						if (isVisible())
						{
							token.setImageBitmap(thumbnail);
						}
					}
				});
        mThumbnailThread.start();
        mThumbnailThread.getLooper();
        Log.i(TAG, "Background thread started");
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
		mGrid = (GridView) v.findViewById(R.id.gridView);
		return v;
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		// You must quit handler threads
		mThumbnailThread.quit();
		Log.i(TAG, "Background thread destroyed");
	}
	
	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		mThumbnailThread.clearQueue();
	}
	
	// Third parameter is type of result produced by AsyncTask, also type of onPostExecute param
	// First parameter is input parameters, passed into execute(), doInBackGround ("First", "Second") 
	// Second type for sending progress updates
	private class FetchItemsTask extends AsyncTask<Void, Void, ArrayList<GalleryItem>>
	{
		@Override
		protected ArrayList<GalleryItem> doInBackground(Void... arg0)
		{
			return new FlickrFetchr().fetchItems();
		}

		@Override
		protected void onPostExecute(ArrayList<GalleryItem> result)
		{
			mItems = result;
			setupAdapter();
		}
	}
	
	void setupAdapter()
	{
		if (getActivity() == null || mGrid == null) return;
		
		if (mItems != null)
		{
		//	mGrid.setAdapter(new ArrayAdapter<GalleryItem>(getActivity(), 
		//			android.R.layout.simple_gallery_item, mItems));
			mGrid.setAdapter(new GalleryItemAdapter(mItems));
		}
		else
		{
			mGrid.setAdapter(null);
		}
	}
	
	private class GalleryItemAdapter extends ArrayAdapter<GalleryItem>
	{
		public GalleryItemAdapter(ArrayList<GalleryItem> items)
		{
			super(getActivity(), 0, items);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null) 
			{
                convertView = getActivity().getLayoutInflater()
                	.inflate(R.layout.gallery_item, parent, false);
			}
            ImageView imageView = (ImageView)convertView
            	.findViewById(R.id.gallery_item_imageView);
            imageView.setImageResource(R.drawable.brian_up_close);
            GalleryItem item = getItem(position);
            mThumbnailThread.queueThumbnail(imageView, item.getUrl());
            return convertView;	
		}
	}
	
}
