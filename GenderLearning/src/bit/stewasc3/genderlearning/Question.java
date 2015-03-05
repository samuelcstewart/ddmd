package bit.stewasc3.genderlearning;

public class Question
{
	private EGender mGender;
	private int mImageResource;
	
	public Question(EGender gender, int imageResource)
	{
		mGender = gender;
		mImageResource = imageResource;
	}

	public EGender getGender()
	{
		return mGender;
	}

	public int getImageResource()
	{
		return mImageResource;
	}
}