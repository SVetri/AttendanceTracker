package com.example.attendancetracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class CourseAttendance extends FragmentActivity implements DatePickerDialog.OnDateSetListener  {
	
    private int listPosition = -1;
	
	TextView nClassesHeld,nClassesMissed,reqd,attendance,estimated;
	
	Button heldplus,heldminus,missedplus,missedminus;
	
	StableArrayAdapter adapter;
	
	StringBuilder currentdate1 = new StringBuilder();
	
	String courseName,extras;
	ListView classesmissed;
	ArrayList<String> missedlist = new ArrayList<String>();
	
	JSONObject extra;
	int yy, mm, dd;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_attendance);
		
		classesmissed = (ListView) findViewById(R.id.listView1);
		nClassesHeld=(TextView)findViewById(R.id.textView2);
		nClassesMissed=(TextView)findViewById(R.id.textView4);
		
		reqd=(TextView)findViewById(R.id.textView5);
		attendance=(TextView)findViewById(R.id.textView6);
		estimated=(TextView)findViewById(R.id.textView7);
		
		heldplus=(Button)findViewById(R.id.button2);
		heldminus=(Button)findViewById(R.id.button1);
		missedplus=(Button)findViewById(R.id.button4);
		missedminus=(Button)findViewById(R.id.button3);
		
		missedlist = new ArrayList<String>();
		
		/*adapter = new StableArrayAdapter(CourseAttendance.this,
		        android.R.layout.simple_list_item_1, missedlist);
	    
	    classesmissed.setAdapter(adapter);
	    
	    classesmissed.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view,
			     int position, long id) {
				  
				  listPosition = position;				  
				  showDatePickerDialog(view);
					
				//updatedate(currentdate1.toString(), position);
			  }
	    });*/
		
		heldplus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				int hPlus=Integer.parseInt(nClassesHeld.getText().toString());
				hPlus++;
				
				String tot=new String();
				
				try {
					tot = extra.getString("total");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				float maxClasses = Integer.parseInt(tot);
				
				if(hPlus<=maxClasses) nClassesHeld.setText(Integer.toString(hPlus)); 
				
				update();
				
			}
		});
		
		heldminus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				int hMinus=Integer.parseInt(nClassesHeld.getText().toString());
				hMinus--;
				
				int t=Integer.parseInt(nClassesMissed.getText().toString());
				
				if(hMinus>=0&&hMinus>=t) nClassesHeld.setText(Integer.toString(hMinus));
				
				update();
				
			}
		});
		
		missedplus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				int mPlus=Integer.parseInt(nClassesMissed.getText().toString());
				mPlus++;
				
				int q=Integer.parseInt(nClassesHeld.getText().toString());
				
				if(mPlus<=q) {
					
					nClassesMissed.setText(Integer.toString(mPlus));
					
					final Calendar c = Calendar.getInstance();
					yy = c.get(Calendar.YEAR);
				    mm = c.get(Calendar.MONTH);
				    dd = c.get(Calendar.DAY_OF_MONTH);
					
				    StringBuilder currentdate = new StringBuilder().append(dd).append(" ").append("/").append(" ").append(mm + 1).append(" /").append(" ").append(yy);
				    
				    missedlist.add(currentdate.toString());
				    adapter.notifyDataSetChanged();
				    adapter = new StableArrayAdapter(CourseAttendance.this,
					        android.R.layout.simple_list_item_1, missedlist);
				    
				    classesmissed.setAdapter(adapter);
				    
				    classesmissed.setOnItemClickListener(new OnItemClickListener() {
						  @Override
						  public void onItemClick(AdapterView<?> parent, View view,
						     int position, long id) {
							  
							  listPosition = position;
							  showDatePickerDialog(view);
								
								//updatedate(currentdate1.toString(), position);
						  }
				    });
				    
				}
			    
				update();
				
			}
		});
		
		missedminus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				int mMinus=Integer.parseInt(nClassesMissed.getText().toString());
				mMinus--;
				
				if(mMinus>=0) {		
					
					nClassesMissed.setText(Integer.toString(mMinus));
					
					if(missedlist.size()!=0) {
						
						missedlist.remove(missedlist.size()-1);
						adapter.notifyDataSetChanged();
					
					}
									
				    adapter = new StableArrayAdapter(CourseAttendance.this,
					        android.R.layout.simple_list_item_1, missedlist);
				    
				    classesmissed.setAdapter(adapter);
				    
				    classesmissed.setOnItemClickListener(new OnItemClickListener() {
						  @Override
						  public void onItemClick(AdapterView<?> parent, View view,
						     int position, long id) {
							  
							  listPosition = position;				  
							  showDatePickerDialog(view);
								
							//updatedate(currentdate1.toString(), position);
						  }
				    });
					
				}
				
				update();
				
			}
		});
		
		Intent u=getIntent();
		
		extras=u.getStringExtra("details");		
		
		
		try {
			extra = new JSONObject(extras);
			nClassesHeld.setText(extra.getString("classesHeld"));
			nClassesMissed.setText(extra.getString("classesMissed"));
			reqd.setText(extra.getString("required"));
			courseName=extra.getString("name");
			getActionBar().setTitle(courseName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
	}
	
	public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
	
	
	public void updatedate(String date, int position)
	{
		missedlist.set(position, date);
		//adapter = new StableArrayAdapter(CourseAttendance.this,
		//        android.R.layout.simple_list_item_1, missedlist);
		adapter.notifyDataSetChanged();
		classesmissed.setAdapter(adapter);
	}
	
	public void update(){
		
		System.out.println("In update");
		
		float cHeld = Integer.parseInt(nClassesHeld.getText().toString());
		float cMissed = Integer.parseInt(nClassesMissed.getText().toString());
		
		System.out.println(cHeld+" "+cMissed);
		
		float req=Integer.parseInt(reqd.getText().toString());
		
		System.out.println(req);
		
		float attendancepercent = 100-(cMissed/cHeld)*100;
		
		System.out.println(attendancepercent);
		
		if(cHeld!=0.0) attendance.setText(Float.toString(attendancepercent));
		else attendance.setText("0");
		
		String total=new String();

		try {
			total = extra.getString("total");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		float maxClasses = Integer.parseInt(total);
		
		System.out.println("Total ::::::::: "+maxClasses);
		
		float est = ((maxClasses-cMissed)/maxClasses)*100.00f;
		
		estimated.setText("Estimated % : "+Float.toString(est));
		
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		update();	
		
	}
	
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		updateSharedPref();
		
	}
	
	public void updateSharedPref(){
		
		JSONObject course=new JSONObject();
		
		String courses = Prefs.getMyStringPref(CourseAttendance.this, "Courses");
		
		

		JSONArray courses_json=new JSONArray();
		JSONObject jo=new JSONObject();
		
		if(courses != null)
			try {
				courses_json = new JSONArray(courses);
				for(int i=0;i<courses_json.length();i++){
					
					System.out.println(i);
					
					if(courses_json.getJSONObject(i).getString("name").equals(courseName)) {
						
						System.out.println("Satisfied");
						
						courses_json.getJSONObject(i).put("classesHeld",nClassesHeld.getText().toString());
						courses_json.getJSONObject(i).put("classesMissed",nClassesMissed.getText().toString());
					
						//jo=courses_json.getJSONObject(i);
						break;
						
					}	
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		//courses_json.put(course);
		
		Prefs.setMyStringPref(CourseAttendance.this, "Courses", courses_json.toString());
		
		System.out.println("Courses ::: "+courses_json.toString());
		
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		// TODO Auto-generated method stub
		currentdate1 = new StringBuilder().append(day).append(" ").append("/").append(" ").append(month).append("/").append(" ").append(year);
		missedlist.set(listPosition, currentdate1.toString());
		adapter = new StableArrayAdapter(CourseAttendance.this,
		        android.R.layout.simple_list_item_1, missedlist);
		//adapter.notifyDataSetChanged();
		classesmissed.setAdapter(adapter);
	    
	    classesmissed.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view,
			     int position, long id) {
				  
				  listPosition = position;
				  showDatePickerDialog(view);
					
					//updatedate(currentdate1.toString(), position);
			  }
	    });
	}
	
	

}
