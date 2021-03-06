package bit.stewasc3.genderlearning;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

public class QuizActivity extends Activity
	implements QuizQuestionFragment.OnQuizFinished, QuizResultFragment.OnQuizRestart
{	
	FragmentManager fm;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);
		fm = getFragmentManager();
		startQuiz();
	}

	@Override
	public void quizFinished(int score, int totalQuestions)
	{
		QuizResultFragment resultFragment = new QuizResultFragment();
		
		Bundle args = new Bundle();
		args.putInt(QuizResultFragment.ARG_TOTAL, totalQuestions);
		args.putInt(QuizResultFragment.ARG_CORRECT, score);
		resultFragment.setArguments(args);
		
		fm.beginTransaction()
			.replace(R.id.fragmentContainer, resultFragment)
			.commit();
	}
	
	@Override
	public void startQuiz()
	{
		QuizQuestionFragment questionFragment = new QuizQuestionFragment();
		fm.beginTransaction()
			.replace(R.id.fragmentContainer, questionFragment)
			.commit();
	}
}
