package com.example.attendancetracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class EditCourse extends Activity {

	JSONObject details;
	
	EditText name,code,total,required;
	RadioGroup rg;
	RadioButton theory,lab;
	
	Button edit;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_course);
		
		Intent u=getIntent();
		
		String extras=u.getStringExtra("details");
		
		try {
			details=new JSONObject(extras);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		name=(EditText)findViewById(R.id.courseName);
		code=(EditText)findViewById(R.id.courseCode);
		total=(EditText)findViewById(R.id.totalClasses);
		required=(EditText)findViewById(R.id.percentRequired);
		
		rg=(RadioGroup)findViewById(R.id.radioGroup1);
		theory=(RadioButton)findViewById(R.id.theory);
		lab=(RadioButton)findViewById(R.id.lab);
		
		edit=(Button)findViewById(R.id.edit);
		edit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				String ename,eid,etotal,erequired;
				
				ename=name.getText().toString();
				eid=code.getText().toString();
				etotal=total.getText().toString();
				erequired=required.getText().toString();
				
				int radioButtonID = rg.getCheckedRadioButtonId();
				
				try {
					editCourse(ename,eid,etotal,erequired,radioButtonID,details.getString("name"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		try {
			name.setText(details.getString("name"));
			code.setText(details.getString("id"));
			total.setText(details.getString("total"));
			required.setText(details.getString("required"));
			if(details.getString("type").equals("theory")) theory.setSelected(true);
			else lab.setSelected(true);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
public void editCourse(String name, String id, String total, String required, int radioButtonID,String courseName){
		
		JSONObject course=new JSONObject();
		String type=new String();
		System.out.println("In Button Click"+courseName);
		
		if(radioButtonID==R.id.theory) type="theory";
		else type="lab";
	
		
		String courses = Prefs.getMyStringPref(EditCourse.this, "Courses");
		
		JSONArray courses_json=new JSONArray();
		JSONObject jo=new JSONObject();
		
		if(courses != null)
			try {
				courses_json = new JSONArray(courses);
				for(int i=0;i<courses_json.length();i++){
					
					System.out.println(i);
					
					if(courses_json.getJSONObject(i).getString("name").equals(courseName)) {
						
						System.out.println("Satisfied");
						
						courses_json.getJSONObject(i).put("name",name);
						courses_json.getJSONObject(i).put("id",id);
						courses_json.getJSONObject(i).put("total",total);
						courses_json.getJSONObject(i).put("required",required);
						
						if(radioButtonID==R.id.theory) type="theory";
						else type="lab";
						
						courses_json.getJSONObject(i).put("type",type);
						
						//jo=courses_json.getJSONObject(i);
						break;
						
					}
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		//courses_json.put(course);
		
		Prefs.setMyStringPref(EditCourse.this, "Courses", courses_json.toString());
		
		System.out.println("Courses ::: "+courses_json.toString());
		
		finish();
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_course, menu);
		return true;
	}

}
