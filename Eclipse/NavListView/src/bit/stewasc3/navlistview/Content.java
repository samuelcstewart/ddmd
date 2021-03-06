package bit.stewasc3.navlistview;

import java.util.UUID;

public class Content
{
	private String mTitle;
	private int mImgResId;
	private UUID uuid;
	
	public Content(String title, int imgResId)
	{
		mTitle = title;
		mImgResId = imgResId;
		uuid = UUID.randomUUID();
	}
	
	public String getTitle()
	{
		return mTitle;
	}
	
	public int getImgResId()
	{
		return mImgResId;
	}
	
	public UUID getId()
	{
		return uuid;
	}

}
