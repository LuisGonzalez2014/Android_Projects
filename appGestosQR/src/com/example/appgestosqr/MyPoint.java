/*
 *  Copyright (C) 2014, 2015 - Luis Alejandro González Borrás, Jose Manuel GómezGonzález>
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

import android.graphics.Paint;

/** Clase para los puntos que se dibujan en pantalla
 * 
 * @author Luis Alejandro Gonzalez Borras
 * @author Jose Manuel Gomez Gonzalez
 * @version 1.0
 * @see Visitar www.github.com/LuisGonzalez2014/Android_Projects/tree/master/appGestosQR
 */
public class MyPoint {
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
