/*
 *  Copyright (C) 2014, 2015 - Luis Alejandro González Borrás, Jose Manuel Gómez González>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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


/** Clase para la actividad principal
 * 
 * @author Luis Alejandro Gonzalez Borras
 * @author Jose Manuel Gomez Gonzalez
 * @version 1.0
 * @see Visitar www.github.com/LuisGonzalez2014/Android_Projects/tree/master/appGestosQR
 */
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
        
        // Obtenemos las dimensiones de la pantalla
        Display currentDisplay = getWindowManager().getDefaultDisplay();
        dw = currentDisplay.getWidth();
        dh = currentDisplay.getHeight();

        // Creamos un bitmap con las medidas de la pantalla
        bitmap = Bitmap.createBitmap((int) dw, (int) dh, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        imageView.setImageBitmap(bitmap);
        
        // Inicialiazamos el pincel y le asiganamos un color
        pincel = new Paint();
        pincel.setARGB(255,190,190,190);
        
        // Radio de los circulos a dibujar y coordenadas de la primera circunferencia
        float radio = 30;
        float topLeftX = (float) (dw/4.0);
        float topLeftY = (float) (dh/4.0);
    	
        // Se almacenan todos los puntos y se dibujan en pantalla
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
		// Se cambia el color del pincel
		pincel.setARGB(255,244,81,30);
		
		// Capturamos la acción
		int action = event.getAction();
		actual_point = new PointF(event.getX(event.getPointerId(0)), event.getY(event.getPointerId(0)));
		MyPoint mpoint = new MyPoint();
		
		// Radio de los circulos a dibujar
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
	
	/**
	 * Método que comprueba si el punto tiene las mismas coordenadas que alguno de los dibujados
	 */
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