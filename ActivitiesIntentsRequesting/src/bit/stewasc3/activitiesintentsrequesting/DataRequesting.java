package bit.stewasc3.activitiesintentsrequesting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DataRequesting extends Activity
{
	Button bttnColor;
	TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_requesting);
		bttnColor = (Button)findViewById(R.id.button1);
		textView = (TextView)findViewById(R.id.textView2);

		bttnColor.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(DataRequesting.this, DataRequestingSettings.class);
				startActivityForResult(i, 0);
			}
			
		});
	}
	
	@Override
	protected void onActivityResult(int requestId, int resultCode, Intent i)
	{
		if (requestId == 0 &&
				resultCode == Activity.RESULT_OK)
		{
			int color = i.getIntExtra(DataRequestingSettings.KEY_COLOR_VAL, 0);
			textView.setTextColor(color);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.data_requesting, menu);
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
