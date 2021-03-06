package bit.stewasc3.complexcontrols;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements EnrolDialog.onEnrol
{

	Button enrolButton;
	RadioGroup instrumentRadGrp;
	Spinner monthSpinner;
	TextView txtViewEnrollment;
	FragmentManager fm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		fm = getFragmentManager();
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
					int selectedBttnId = instrumentRadGrp.getCheckedRadioButtonId();
					Button selectedBttn = (Button) findViewById(selectedBttnId);
                    String month = (String)monthSpinner.getSelectedItem();
                    String instrument = (String)selectedBttn.getText();
					DialogFragment frag = EnrolDialog.newInstance(month, instrument);
					frag.show(fm, "confirm");
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

	@Override
	public void onEnrolConfirm(String month, String instrument)
	{
		txtViewEnrollment.setText("You have confirmed " + instrument + " lessons for " + month);
	}

	@Override
	public void onEnrolCancel()
	{
		txtViewEnrollment.setText("Cancelled enrollment");
	}
}
