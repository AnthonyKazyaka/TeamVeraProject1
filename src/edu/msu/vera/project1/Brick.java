package edu.msu.vera.project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Brick {
	
	/**
	 * THe image for the actual brick.
	 */
	private Bitmap brick;
	
	/**
	 * x location. 
	 * We use relative x locations in the range 0-1 for the center
	 * of the brick.
	 */
	private float x = 0;
	
	/**
	 * y location
	 */
	private float y = 0;
	
	/**
	 * weight of brick
	 */
	private float weight = 0;

	public Brick(Context context, int id, float x, float y, float weight) {
		this.x = x;
		this.y = y;
		this.weight = weight;
		
		brick = BitmapFactory.decodeResource(context.getResources(), id);
	}
	
	/**
	 * Draw the brick
	 * @param canvas Canvas we are drawing on
	 * @param marginX Margin x value in pixels
	 * @param marginY Margin y value in pixels
	 * @param game area Size we draw the game area in pixels
	 * @param game area Amount we scale the bricks when we draw them
	 */
	public void draw(Canvas canvas, int marginX, int marginY, int gamearea, float scaleFactor) {
		canvas.save();
		
		// Convert x,y to pixels and add the margin, then draw
		canvas.translate(marginX + x * gamearea, marginY + y * gamearea);
		
		// Scale it to the right size
		canvas.scale(scaleFactor, scaleFactor);
		
		// This magic code makes the center of the piece at 0, 0
		canvas.translate(-brick.getWidth() / 2, -brick.getHeight() / 2);
		
		// Draw the bitmap
		canvas.drawBitmap(brick, 0, 0, null);
		canvas.restore();
	}
	
    /**
     * Test to see if we have touched a brick
     * @param testX X location as a normalized coordinate (0 to 1)
     * @param testY Y location as a normalized coordinate (0 to 1)
     * @param gamearea the size of the game area in pixels
     * @param scaleFactor the amount to scale a brick by
     * @return true if we hit the brick
     */
    public boolean hit(float testX, float testY, int gameArea, float scaleFactor) {
        // Make relative to the location and size to the brick size
        int pX = (int)((testX - x) * gameArea / scaleFactor) + brick.getWidth() / 2;
        int pY = (int)((testY - y) * gameArea / scaleFactor) + brick.getHeight() / 2;
        
        if(pX < 0 || pX >= brick.getWidth() ||
           pY < 0 || pY >= brick.getHeight()) {
            return false;
        }
        
        // We are within the rectangle of the brick.
        // Are we touching actual picture?
        return (brick.getPixel(pX, pY) & 0xff000000) != 0;
    }
    
    /**
     * Move the brick by dx, dy
     * @param dx x amount to move
     * @param dy y amount to move
     */
    public void move(float dx, float dy) {
        x += dx;
        //y += dy;
    }
    
    public float stackHeight ( float h){
    	return h += brick.getHeight();
    }
    
    public float getWeight (){
    	return this.weight;
    }
    

}
