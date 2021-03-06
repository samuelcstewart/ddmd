package bit.stewasc3.navlistview;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ContentFragment extends Fragment
{
	private Content mContent;
	private ImageView mImageView;
	private TextView mTextView;
	
	public static final String EXTRA_CONTENT_ID = "bit.stewasc3.contentid";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		UUID contentId = (UUID)getArguments().getSerializable(EXTRA_CONTENT_ID);
		mContent = ContentManager.get(getActivity()).getContent(contentId);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState)
	{
		View v = inflater.inflate(R.layout.fragment_content, container, false);
		mTextView = (TextView) v.findViewById(R.id.textViewContentTitle);
		mImageView = (ImageView) v.findViewById(R.id.imageViewContent);
		
		mTextView.setText(mContent.getTitle());
		mImageView.setImageResource(mContent.getImgResId());
		
		return v;
	}

}
