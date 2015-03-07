package bit.stewasc3.activitiesintentsrequesting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DataRequestingSettings extends Activity
{
	TextView textView;
	static String KEY_COLOR_VAL = "bit.stewasc3.activitiesintentsrequesting.color_val";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_requesting_settings);
		
		textView = (TextView)findViewById(R.id.textView2);
		
		Intent i = new Intent();
		i.putExtra(KEY_COLOR_VAL, textView.getCurrentTextColor());
		setResult(Activity.RESULT_OK, i);
		
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.data_requesting_settings, menu);
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
}
