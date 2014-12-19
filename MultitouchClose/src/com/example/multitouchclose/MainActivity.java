package com.example.multitouchclose;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.PointF;

public class MainActivity extends Activity {
	private enum GestureDetectionState {
		CAPTURING, DETECTED, IDLE
	}
	private GestureDetectionState gds;
	private double PERCENT = 0.6;
	
	TextView texto, d1, d2, d3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		texto = (TextView) findViewById(R.id.text);
		d1 = (TextView) findViewById(R.id.p1);
		d2 = (TextView) findViewById(R.id.p2);
		d3 = (TextView) findViewById(R.id.p3);
		this.gds = GestureDetectionState.IDLE;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private PointF initial_point_1 = new PointF(), initial_point_2 = new PointF(), initial_point_3 = new PointF();
	private PointF actual_point_1 = new PointF(), actual_point_2 = new PointF(), actual_point_3 = new PointF();
	private double initial_dist_1=Integer.MAX_VALUE, initial_dist_2=Integer.MAX_VALUE, initial_dist_3=Integer.MAX_VALUE;
	private double actual_dist_1=Integer.MIN_VALUE, actual_dist_2=Integer.MIN_VALUE, actual_dist_3=Integer.MIN_VALUE;
	
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
    	if (ev.getPointerCount() == 3 && this.gds == GestureDetectionState.DETECTED)
    	{
			texto.setText("Esperando gesto...");
			actual_point_1 = new PointF(ev.getX(ev.getPointerId(0)), ev.getY(ev.getPointerId(0)));
			actual_point_2 = new PointF(ev.getX(ev.getPointerId(1)), ev.getY(ev.getPointerId(1)));
			actual_point_3 = new PointF(ev.getX(ev.getPointerId(2)), ev.getY(ev.getPointerId(2)));
        	
    		actual_dist_1 = this.distance(actual_point_1, actual_point_2);
    		actual_dist_2 = this.distance(actual_point_1, actual_point_3);
    		actual_dist_3 = this.distance(actual_point_2, actual_point_3);
    		
        	d1.setText(Double.toString(this.distance(actual_point_1, actual_point_2)));
        	d2.setText(Double.toString(this.distance(actual_point_1, actual_point_3)));
        	d3.setText(Double.toString(this.distance(actual_point_2, actual_point_3)));
    		
    		if (initial_dist_1 * PERCENT >= actual_dist_1 &&
    		    initial_dist_2 * PERCENT >= actual_dist_2 &&
    		    initial_dist_3 * PERCENT >= actual_dist_3)
    		{
    			android.os.Process.killProcess(android.os.Process.myPid());
    		}
    		else if (initial_point_1.y <= actual_point_1.y * PERCENT &&
    				 initial_point_2.y <= actual_point_2.y * PERCENT &&
    				 initial_point_3.y <= actual_point_3.y * PERCENT)
    		{
    			super.finish();
    		}
    	}
    	else if (ev.getPointerCount() == 3 && this.gds == GestureDetectionState.IDLE)
    	{
    		this.gds = GestureDetectionState.DETECTED;
    		initial_point_1 = new PointF(ev.getX(ev.getPointerId(0)), ev.getY(ev.getPointerId(0)));
    		initial_point_2 = new PointF(ev.getX(ev.getPointerId(1)), ev.getY(ev.getPointerId(1)));
    		initial_point_3 = new PointF(ev.getX(ev.getPointerId(2)), ev.getY(ev.getPointerId(2)));
        	
        	initial_dist_1 = this.distance(initial_point_1, initial_point_2);
        	initial_dist_2 = this.distance(initial_point_1, initial_point_3);
        	initial_dist_3 = this.distance(initial_point_2, initial_point_3);
    	}
    	else if (ev.getPointerCount() == 0)
    	{
			texto.setText("Esperando gesto...");
    		this.gds = GestureDetectionState.IDLE;
    	}
        
        return true;
    }
    
    private double distance(PointF p1, PointF p2) {
    	return Math.sqrt(Math.pow(p2.x-p1.x, 2) + Math.pow(p2.y-p1.y, 2));
    }
}
