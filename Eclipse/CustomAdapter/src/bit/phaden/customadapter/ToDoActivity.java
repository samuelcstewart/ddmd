package bit.phaden.customadapter;



import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ToDoActivity extends Activity 
{
	ToDo[] toDoArray;
	ToDoArrayAdapter adapter;
	ListView lv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do);
		
		// This method loads toDoArray with instances of class ToDo, one for each list item
		initialiseDataArray();

		// Make custom adapter
		adapter = new ToDoArrayAdapter(toDoArray);
		
		// Get a reference to the ListView with id lvToDo
		lv = (ListView) findViewById(R.id.lvToDo);
		
		// Set the ListView's adapter
		lv.setAdapter(adapter);

	} // end onCreate

 private class ToDoArrayAdapter extends ArrayAdapter<ToDo>
 {

	public ToDoArrayAdapter(ToDo[] objects) 
	{ 
		super(ToDoActivity.this, 0, objects); 
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) 
	{
		// YOUR CODE HERE
		
		// Get a LayoutInflater
		LayoutInflater inflater = ToDoActivity.this.getLayoutInflater();
		
		// Inflate custom_list_view and store the returned View in a variable
		View v = inflater.inflate(R.layout.custom_list_view, null);
		
		// Get references to the controls in custom_list_view. Get both the ImageView and the TextView
		ImageView iv = (ImageView) v.findViewById(R.id.ivThumbnail);
		TextView tv = (TextView) v.findViewById(R.id.tvDescription);
		
		// Get the current ToDo instance. Use the Adapter base class's getItem command
		ToDo todo = getItem(position);
		
		// Use the data fields of the current ToDo instance to initialise the View controls correctly
		iv.setImageDrawable(todo.toDoImage);
		tv.setText(todo.toDoName);
		
		// Return your customview
		return v;
		
	}
 }// end class ToDoArrayAdapter

//====================================================================================

private void initialiseDataArray()
{
	Resources resourceMachine = getResources();
	
	String[] activityNames = resourceMachine.getStringArray(R.array.activity_names);
	String[] imageFileNames = resourceMachine.getStringArray(R.array.image_file_names);
	
	toDoArray = new ToDo[activityNames.length];
	
	for (int i=0; i<activityNames.length; i++)
	{
		String activity = activityNames[i];
		
		String imageFile = imageFileNames[i];
		int resourceId = resourceMachine.getIdentifier(imageFile, "drawable", getPackageName());
		Drawable image = resourceMachine.getDrawable(resourceId);
		
		ToDo currentToDo = new ToDo(activity, image);
		
		toDoArray[i] = currentToDo;
	}    	
	
}// end initialiseDataArray
	//====================================================================================

} // end class ToDoActivity