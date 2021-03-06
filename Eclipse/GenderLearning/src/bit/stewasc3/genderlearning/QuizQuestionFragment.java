package bit.stewasc3.genderlearning;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class QuizQuestionFragment extends Fragment
{
	ArrayList<Question> mQuestions;
	ImageView mQuestionImageView;
	Button mSubmitButton;
	RadioGroup mAnswerGroup;
	RadioButton mRadioDer;
	RadioButton mRadioDas;
	RadioButton mRadioDie;
	int mQuestionIndex;
	Question mQuestion;
	int mAnsweredCorrectly;
	OnQuizFinished mCallback;
	
	public interface OnQuizFinished
	{
		public void quizFinished(int score, int totalQuestions);
	}
	
	@Override
	// Opposed to an activity's onCreate method, a fragment's onCreate must be public.
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mQuestions = new ArrayList<Question>();
		mQuestions.add(new Question(EGender.DAS, R.drawable.das_auto));
		mQuestions.add(new Question(EGender.DAS, R.drawable.das_haus));
		mQuestions.add(new Question(EGender.DAS, R.drawable.das_schaf));
		mQuestions.add(new Question(EGender.DER, R.drawable.der_apfel));
		mQuestions.add(new Question(EGender.DER, R.drawable.der_baum));
		mQuestions.add(new Question(EGender.DER, R.drawable.der_stuhl));
		mQuestions.add(new Question(EGender.DIE, R.drawable.die_ente));
		mQuestions.add(new Question(EGender.DIE, R.drawable.die_hexe));
		mQuestions.add(new Question(EGender.DIE, R.drawable.die_kuh));
		mQuestions.add(new Question(EGender.DIE, R.drawable.die_milch));
		mQuestions.add(new Question(EGender.DIE, R.drawable.die_strasse));
		Collections.shuffle(mQuestions);
		
		mQuestionIndex = 0;
		mQuestion = mQuestions.get(mQuestionIndex);
		mAnsweredCorrectly = 0;
	}
	
	// A fragment inflates it's view in onCreateView, as opposed to an activity.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_quizquestion, parent, false);
		mQuestionImageView = (ImageView)v.findViewById(R.id.imageView1);
		mQuestionImageView.setImageResource(mQuestion.getImageResource());
		
		mAnswerGroup = (RadioGroup)v.findViewById(R.id.radio_group);
		mRadioDas = (RadioButton)v.findViewById(R.id.radio_das);
		mRadioDer = (RadioButton)v.findViewById(R.id.radio_der);
		mRadioDie = (RadioButton)v.findViewById(R.id.radio_die);
		
		mSubmitButton = (Button)v.findViewById(R.id.bttn_submit);
		mSubmitButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String toastMsg = "Incorrect";
				EGender gender = mQuestion.getGender();
				if(mRadioDas.isChecked() && gender == EGender.DAS ||
						mRadioDie.isChecked() && gender == EGender.DIE ||
						mRadioDer.isChecked() && gender == EGender.DER)
				{
					toastMsg = "Correct";
					++mAnsweredCorrectly;
				}
				Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_SHORT).show();
				
				if(mQuestionIndex < mQuestions.size() -1)
				{
					++mQuestionIndex;
					mQuestion = mQuestions.get(mQuestionIndex);
					mQuestionImageView.setImageResource(mQuestion.getImageResource());
				}
				else
					mCallback.quizFinished(mAnsweredCorrectly, mQuestions.size());		
			}	
		}
		);
		return v;
	}
	
	@Override
	public void onAttach(Activity a)
	{
		super.onAttach(a);
		mCallback = (OnQuizFinished) a;
	}

}
