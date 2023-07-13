package com.quizee;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;

import android.text.Html;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class myutils{





static class pshape extends GradientDrawable {
	public int strokeColor= Color.rgb(52,58,99);
	public int bg=Color.parseColor("#00ffffff");


	pshape()
	{
		//bg=Color.WHITE;
		go();
	}

	public void go()
	{
		
	        setColor(bg);
	        setStroke(2,strokeColor);
	        setCornerRadius(10);
	}
}


public static class rshape extends GradientDrawable{


	public rshape(int bg)
	{
		//GradientDrawable shape=new GradientDrawable();
        setCornerRadius(30);
        setColor(bg);
	}

	rshape(int bg,int radius)
	{
		setCornerRadius(30);
        	setColor(bg);
	}

	public rshape(int bg,boolean stroke)
	{
		//GradientDrawable shape=new GradientDrawable();
        setCornerRadius(30);
        setColor(bg);
	        if(stroke)
	        {
	        	setStroke(2,Color.rgb(52,58,64));
	        }
	}


}

public static TableRow prow(Context ctx,String i1,String i2)
{
	TableRow tl=new TableRow(ctx);
	TextView t1=new TextView(ctx);
	t1.setText(Html.fromHtml(i1));
	tl.addView(t1);
	t1.setGravity(Gravity.CENTER);
	t1.setPadding(0,0,40,0);

	TextView t2=new TextView(ctx);
	t2.setText(Html.fromHtml(i2));
	t2.setGravity(Gravity.CENTER);
	tl.addView(t2);
	tl.setGravity(Gravity.CENTER);
	t2.setPadding(40,0,0,0);

	return tl;
}


}