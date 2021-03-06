package bit.stewasc3.navlistview;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class ContentPager extends FragmentActivity
{

	public static final String EXTRA_POSITION = "bit.stewasc3.navlistview.position";
	private ViewPager mPager;
	private ArrayList<Content> mContents;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pager);
		
		mContents = ContentManager.get(this).getContents();
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(new ContentPagerAdapter(getSupportFragmentManager()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pager, menu);
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
	
	public class ContentPagerAdapter extends FragmentStatePagerAdapter
	{
		public ContentPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}
		
		@Override
		public Fragment getItem(int position)
		{
			ContentFragment frag = new ContentFragment();
			
			Bundle args = new Bundle();
			args.putSerializable(ContentFragment.EXTRA_CONTENT_ID, mContents.get(position).getId());
			frag.setArguments(args);
			return frag;
		}
		
		@Override
		public int getCount()
		{
			return mContents.size();
		}
	}
}
