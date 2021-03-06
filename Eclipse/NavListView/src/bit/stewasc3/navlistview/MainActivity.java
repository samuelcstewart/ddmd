package bit.stewasc3.navlistview;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity
{
	
	ArrayList<Content> mContents;
	ListView mListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	
		mContents = ContentManager.get(getApplicationContext()).getContents();
		mListView = (ListView) findViewById(R.id.left_drawer);
		mListView.setOnItemClickListener(new ListClickHandler());
		mListView.setAdapter(new ContentAdapter(mContents));
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
	
	private class ContentAdapter extends ArrayAdapter<Content>
	{
		public ContentAdapter(ArrayList<Content> contents)
		{
			// Not using pre-defined layout, pass 0
			super(MainActivity.this, 0, contents);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if(convertView == null)
			{
				convertView = MainActivity.this.getLayoutInflater()
						.inflate(R.layout.list_item_content, null);
			}
			
			Content c = getItem(position);
			
			//Configure view
			
			ImageView thumbImageView = (ImageView) convertView.findViewById(R.id.imageViewListItem);
			thumbImageView.setImageResource(c.getImgResId());
			
			TextView titleTextView = (TextView) convertView.findViewById(R.id.textViewListItem);
			titleTextView.setText(c.getTitle());
			
			return convertView;
		}
	}
	
	private class ListClickHandler implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			Intent i = new Intent(MainActivity.this, ContentPager.class);
			i.putExtra(ContentPager.EXTRA_POSITION, position);
			startActivity(i);
		}
	}
}
