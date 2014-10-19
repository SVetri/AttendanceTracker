package com.example.attendancetracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class AddCourse extends Activity {
	
	Button add;
	
	EditText courseName,courseCode,totalClasses,percentRequired;
	
	RadioGroup radioButtonGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_course);
		
		add=(Button)findViewById(R.id.add);
		courseName=(EditText)findViewById(R.id.courseName);
		courseCode=(EditText)findViewById(R.id.courseCode);
		totalClasses=(EditText)findViewById(R.id.totalClasses);
		percentRequired=(EditText)findViewById(R.id.percentRequired);
		radioButtonGroup=(RadioGroup)findViewById(R.id.radioGroup1);
		
		
		
		add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				String name,id,total,required;
				
				name=courseName.getText().toString();
				id=courseCode.getText().toString();
				total=totalClasses.getText().toString();
				required=percentRequired.getText().toString();
				
				int radioButtonID = radioButtonGroup.getCheckedRadioButtonId();
				
				storeCourse(name,id,total,required,radioButtonID);
				
			}
		});
		
	}
	
	public void storeCourse(String name, String id, String total, String required, int radioButtonID){
		
		JSONObject course=new JSONObject();
		String type=new String();
		System.out.println("In Button Click");
		
		try {
			System.out.println("In Try");
			course.put("name",name);
			course.put("id",id);
			course.put("total",total);
			course.put("required",required);
			course.put("classesHeld", "0");
			course.put("classesMissed", "0");
			
			if(radioButtonID==R.id.theory) type="theory";
			else type="lab";
			
			course.put("type",type);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			System.out.println("Course :::::::: "+course.getString("name"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String key=name+type;
		
		String courses = Prefs.getMyStringPref(AddCourse.this, "Courses");
		
		JSONArray courses_json=new JSONArray();
		
		if(courses != null)
			try {
				courses_json = new JSONArray(courses);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		courses_json.put(course);
		
		Prefs.setMyStringPref(AddCourse.this, "Courses", courses_json.toString());
		
		System.out.println("Courses ::: "+courses_json.toString());
		
		finish();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_course, menu);
		return true;
	}

}
