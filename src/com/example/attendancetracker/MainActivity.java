package com.example.attendancetracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	ListView lv;
	
	JSONArray cList=new JSONArray();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lv=(ListView)findViewById(R.id.lv1);
		
	  
	}
	
	private class StableArrayAdapter extends ArrayAdapter<String> {

	    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

	    public StableArrayAdapter(Context context, int textViewResourceId,
	        List<String> objects) {
	      super(context, textViewResourceId, objects);
	      for (int i = 0; i < objects.size(); ++i) {
	        mIdMap.put(objects.get(i), i);
	      }
	    }

	    @Override
	    public long getItemId(int position) {
	      String item = getItem(position);
	      return mIdMap.get(item);
	    }

	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }

	  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		String courseList=Prefs.getMyStringPref(MainActivity.this, "Courses");
		
		
		
		try {
			cList=new JSONArray(courseList);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int noOfCourses=cList.length();
		
		String [] courses=new String[noOfCourses];
		
		for(int i=0;i<noOfCourses;i++)
			try {
				courses[i]=cList.getJSONObject(i).getString("name");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		final ArrayList<String> list = new ArrayList<String>();
	    for (int i = 0; i < courses.length; ++i) {
	      list.add(courses[i]);
	    }
	    final StableArrayAdapter adapter = new StableArrayAdapter(this,
	        android.R.layout.simple_list_item_1, list);
	    lv.setAdapter(adapter);
		

		lv.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view,
			    int position, long id) {
			    
				  //Intent u = new Intent();
				  Log.v("long clicked","pos: " + position);
	                
	                Intent u = new Intent(MainActivity.this, CourseAttendance.class);
	                try {
						u.putExtra("details", cList.getString(position));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                startActivity(u);
				  
			  }
			  
			}); 
		
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int pos, long id) {
                // TODO Auto-generated method stub

                Log.v("long clicked","pos: " + pos);
                
                Intent u = new Intent(MainActivity.this,EditCourse.class);
                try {
					u.putExtra("details", cList.getString(pos));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                startActivity(u);

                return true;
            }
        }); 
		
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		
	    switch (item.getItemId()) {
	    case R.id.action_addcourse:
	        
	    	//doshit
	    	Intent addCourse = new Intent(MainActivity.this,AddCourse.class);
	    	startActivity(addCourse);
	    	
	        return true;
	    case R.id.action_editcourse:
	        
	    	
	    	//doshit
	    	Intent editCourse = new Intent(MainActivity.this,EditCourse.class);
	    	startActivity(editCourse);
	    	
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

}
