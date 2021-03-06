package bit.stewasc3.complexcontrols;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class EnrolDialog extends DialogFragment
{
	public static String EXTRA_MONTH = "bit.stewasc3.complexcontrols.month";
	public static String EXTRA_INSTRUMENT = "bit.stewasc3.complexcontrols.instrument";
	
	String instrument;
	String month;
	
	public interface onEnrol {
		public void onEnrolConfirm(String month, String instrument);
		public void onEnrolCancel();
	}
	
	onEnrol mCallback;
	
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
		try
		{
			mCallback = (onEnrol) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString() + "Interface not implemented");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Bundle args = getArguments();
		
		instrument = args.getString(EXTRA_INSTRUMENT);
		month = args.getString(EXTRA_MONTH);
		
		DialogClickListener listener = new DialogClickListener();

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Enrol for " + instrument + " in " + month)
			.setPositiveButton("Yes", listener)
			.setNegativeButton("No", listener);
		
		return builder.create();
	}

	public static DialogFragment newInstance(String month, String instrument)
	{
		Bundle args = new Bundle();
		args.putString(EXTRA_MONTH, month);
		args.putString(EXTRA_INSTRUMENT, instrument);
		DialogFragment f = new EnrolDialog();
		f.setArguments(args);
		return f;
	}
	
	public class DialogClickListener implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			if (which == DialogInterface.BUTTON_POSITIVE)
				mCallback.onEnrolConfirm(month, instrument);
			else
				mCallback.onEnrolCancel();
		}
	}
}
