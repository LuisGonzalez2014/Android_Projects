package com.example.appgestosqr;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import com.example.appgestosqr.QRCodeReader;

public class MainActivity extends Activity {
    private Canvas canvas;
    private Paint pincel;
    private Bitmap bitmap;
    private ImageView imageView;
    private PointF actual_point;
    private ArrayList<MyPoint> myPoints;
    private float dw, dh;
    private String secuencia;
    
	
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        myPoints = new ArrayList<MyPoint>();
        imageView = (ImageView) this.findViewById(R.id.imageView1);
        secuencia = new String();
        
        Display currentDisplay = getWindowManager().getDefaultDisplay();
        dw = currentDisplay.getWidth();
        dh = currentDisplay.getHeight();

        bitmap = Bitmap.createBitmap((int) dw, (int) dh, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        imageView.setImageBitmap(bitmap);
        
        pincel = new Paint();
        pincel.setARGB(255,190,190,190);
        
        float radio = 30;
        float topLeftX = (float) (dw/4.0);
        float topLeftY = (float) (dh/4.0);
    	
        Integer k=0;
    	for (int i=1 ; i<=3 ; i++)
        {
        	for (int j=1 ; j<=3 ; j++)
            {
        		MyPoint mpoint = new MyPoint();
	        	mpoint.setId(k);
	        	mpoint.setX(topLeftX*j);
	        	mpoint.setY(topLeftY*i);
	        	mpoint.setRadius(radio);
	        	mpoint.setColor(pincel);
	        	myPoints.add(mpoint);
	        	canvas.drawCircle(myPoints.get(k).getX(), myPoints.get(k).getY(), 
	        			myPoints.get(k).getRadius(), myPoints.get(k).getColor());
	        	k++;
	        }
        }
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


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		pincel.setARGB(255,244,81,30);
		int action = event.getAction();
		actual_point = new PointF(event.getX(event.getPointerId(0)), event.getY(event.getPointerId(0)));
		MyPoint mpoint = new MyPoint();
		int radio = 20;
		
		switch (action) {
		    case MotionEvent.ACTION_DOWN:
		    	if ((mpoint = this.pointTouch(actual_point)) != null)
		    	{
		    		if (secuencia.length() ==0)
		    			secuencia += (mpoint.getId()).toString();
		    		else if (!secuencia.endsWith(mpoint.getId().toString()))
		    		{
		    			secuencia += (mpoint.getId()).toString();
		    		}
		    		canvas.drawCircle(mpoint.getX(), mpoint.getY(), radio, pincel);
			    	imageView.invalidate();
		    	}
		    break;
		    case MotionEvent.ACTION_UP:
		        if (secuencia.equals("0124678"))
		        {
		        	Intent i = new Intent(this, QRCodeReader.class);
		        	startActivity(i);
		        }
		        else
		        {
		        	secuencia = "";
			    	pincel.setARGB(255,190,190,190);
			    	for (MyPoint p : this.myPoints)
					{
			    		canvas.drawCircle(p.getX(), p.getY(), radio, pincel);
					}
			    	imageView.invalidate();
		        }
		    break;
		    case MotionEvent.ACTION_MOVE:
		    	if ((mpoint = this.pointTouch(actual_point)) != null)
		    	{
		    		if (secuencia.length() == 0)
		    			secuencia += (mpoint.getId()).toString();
		    		else if (!secuencia.endsWith(mpoint.getId().toString()))
		    		{
		    			secuencia += (mpoint.getId()).toString();
		    		}
			    	canvas.drawCircle(mpoint.getX(), mpoint.getY(), radio, pincel);
			    	imageView.invalidate();
		    	}
		    break;
		}
	    
	    return true;
	}
	
	// Comprueba si el punto tiene las mismas coordenadas que alguno de los dibujados
	public MyPoint pointTouch(PointF finger){
		int radio = 70;
		for (MyPoint p : this.myPoints)
		{
           if (Math.sqrt(Math.pow(finger.x-p.getX(),2)+Math.pow(finger.y-p.getY(),2)) <= radio)
        	   return p;
		}
		return null;
	}
}

class MyPoint {
	private float x, y, radius;
	private Integer id;
	private Paint color;
	
	public MyPoint(){
		this.id = 0;
		this.x = 0;
		this.y = 0;
		this.radius = 30;
		this.color = new Paint();
		this.color.setARGB(255,190,190,190);
	}
	
	// Modificadores
	public void setId(Integer id){
		this.id = id;
	}
	
	public void setX(float x){
		this.x = x;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public void setRadius(float radius){
		this.radius = radius;
	}
	
	public void setColor(Paint paint){
		this.color.setColor(paint.getColor());
	}
	
	// Consultores
	public Integer getId(){
		return this.id;
	}
	
	public float getX(){
		return this.x;
	}
	
	public float getY(){
		return this.y;
	}
	
	public float getRadius(){
		return this.radius;
	}
	
	public Paint getColor(){
		return this.color;
	}
}