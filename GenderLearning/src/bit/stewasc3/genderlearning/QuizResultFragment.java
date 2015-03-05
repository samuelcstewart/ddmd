package bit.stewasc3.genderlearning;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class QuizResultFragment extends Fragment
{
	
	static String ARG_TOTAL = "bit.stewasc3.genderlearning.total_questions";
	static String ARG_CORRECT = "bit.stewas3.genderlearning.total_correct";
	
	private int totalQuestions;
	private int totalCorrect;
	private TextView resultText;
	private Button restartButton;
	
	OnQuizRestart mCallback;

	public interface OnQuizRestart
	{
		public void startQuiz();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		totalQuestions = getArguments().getInt(ARG_TOTAL);
		totalCorrect = getArguments().getInt(ARG_CORRECT);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, parent, savedInstanceState);
		View v = inflater.inflate(R.layout.fragment_quizresult, parent, false);
		
		resultText = (TextView)v.findViewById(R.id.txtViewResult);
		resultText.setText("You got " + Integer.toString(totalCorrect) +
				" out of " + Integer.toString(totalQuestions) + " correct");
		
		restartButton = (Button)v.findViewById(R.id.bttnRestart);
		restartButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				mCallback.startQuiz();
			}	
		});
		return v;
	}
	
	@Override
	public void onAttach(Activity a)
	{
		super.onAttach(a);
		mCallback = (OnQuizRestart) a;
	}
}
