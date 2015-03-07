package bit.stewasc3.complexcontrols;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{

	Button enrolButton;
	RadioGroup instrumentRadGrp;
	Spinner monthSpinner;
	TextView txtViewEnrollment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		enrolButton = (Button)findViewById(R.id.bttnEnrol);
		instrumentRadGrp = (RadioGroup)findViewById(R.id.radGrpInstrument);
		monthSpinner = (Spinner)findViewById(R.id.spinner1);
		txtViewEnrollment = (TextView)findViewById(R.id.textView3);
		
		enrolButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int id = instrumentRadGrp.getCheckedRadioButtonId();
				
				if (id == -1)
				{
					Toast.makeText(getApplicationContext(), "Select an instrument", Toast.LENGTH_SHORT).show();
				}
				else
				{
					RadioButton selectedBttn = (RadioButton)findViewById(id);
                        
					String month = (String)monthSpinner.getSelectedItem();
					String instrument = (String)selectedBttn.getText();
                
					txtViewEnrollment.setText("You are enrolled for " + instrument + " lessons, starting in " +
                                month);
                }
			}
		});
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
}
