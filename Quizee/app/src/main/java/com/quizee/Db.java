package com.quizee;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.quizee.MainActivity;

import java.util.HashMap;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;
import static android.database.sqlite.SQLiteDatabase.CursorFactory;

public class Db{
	public SQLiteDatabase db;

	public Db(Context ctx){
		db = ctx.openOrCreateDatabase("Quizeev2",ctx.MODE_PRIVATE,null);
		config();
		start();
		Log.d("SQlite Initializer", "instance initializer: ");
	}

	public void start()
	{
		
	}

	public void  config()
	{
		//jquery("drop table exams_Results");
		//jquery("alter table Quize add column questions int");
		//jquery("create table if not exists Professors(num integer not null primary key autoincrement,profname text,email text,course text,password text)");
		//jquery("create table if not exists Students(num integer not null primary key autoincrement,student_name text,email text,student_id text,password text)");
		//jquery("delete from exams_Results");
		jquery("create table if not exists user(email_id varchar(50) not null primary key,password varchar(64),name text,role text not null,course text)");

		jquery("create table if not exists Quize(num integer not null primary key autoincrement,title text,duration decimal,questions int,owner varchar(50) references user(email_id),start bigint,end bigint)");

		jquery("create table if not exists Question(num integer not null primary key autoincrement,ask text,marks decimal,answer text,quize int references Quize(num))");
		jquery("create table if not exists Choices(num integer not null primary key autoincrement,choice text,other text,question int references Question(num),quize int references Quize(num)) ");
		jquery("create table if not exists CourseEnrollment(num integer not null primary key autoincrement,student varchar(50) references user(email_id),course varchar(50) references user(email_id) )");
		jquery("create table if not exists exams_Results(num integer not null primary key autoincrement,enroll int references CourseEnrollment(num),quize int references Quize(num) ,marks decimal,start bigint)");

	}

	public void jquery(String query)
	{
		try{
			db.execSQL(query);
		}catch(SQLException e)
		{
			MainActivity.disp(e.toString()+" from "+query);	
			Log.d(TAG,e.toString());
		}
	}

	public Cursor dquery(String query)
	{
		Cursor ans=null;
		try{
			ans=db.rawQuery(query,null);
		}catch(SQLException e)
		{
			MainActivity.disp(e.toString()+" from "+query);
			Log.d(TAG,e.toString());
			Log.d(TAG, "dquery: ");
		}
		return ans;
	}

	public static HashMap<String, String> getData(Cursor s)
	{
		HashMap<String, String> ans=new HashMap<String, String>();
		s.moveToFirst();
		//MainActivity.disp(s.getString(0));
		for(int i=0;i<s.getColumnCount();i++)
			ans.put(s.getColumnName(i),s.getString(i));

		return ans;
	}

	public static HashMap<String, String> getDat(Cursor s)
	{
		HashMap<String, String> ans=new HashMap<String, String>();
		s.moveToFirst();
		for(int i=0;i<s.getColumnCount();i++){
			String str;
			if(s.getString(i)==null)
				str="0";
			else
				str=s.getString(i);
			ans.put(s.getColumnName(i),str);
		}
		return ans;
	}









}