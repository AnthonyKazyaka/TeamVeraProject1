package edu.msu.vera.project1;

import java.util.ArrayList;

import edu.msu.vera.project1.Brick;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class Game {
	

	
	/**
	 * Percentage of the display width or height that 
	 * is occupied by the game.
	 */
	final static float SCALE_IN_VIEW = 1.0f;

    /**
     * The size of the game area in pixels
     */
    private int gameArea;
    
    /**
     * How much we scale the bricks
     */
    private float scaleFactor;
    
    /**
     * Left margin in pixels
     */
    private int marginX;
     
    /**
     * Top margin in pixels
     */
    private int marginY;
	
    /**
     * This variable is set to a brick we are dragging. If
     * we are not dragging, the variable is null.
     */
    private Brick dragging = null;

 	
	/**
	 * x location. 
	 * We use relative x locations in the range 0-1 for the center
	 * of the brick.
	 */
    
    private float lastRelX;
    
    /**
     * Most recent relative Y touch when dragging
     */
    private float lastRelY;

    /**
     * The height of the stack
     */
    private float stackHeight = 0;
    
    /**
     * Who's turn it is
     */
    private int turn = 1;
    
	/**
	 * Paint for filling the area the stack is in
	 */
	private Paint fillPaint;
	
	/**
	 * Collection of bricks
	 */
	public ArrayList<Brick> bricks = new ArrayList<Brick>();
	
	public Game(Context context) {
		fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		fillPaint.setColor(Color.rgb(112, 112, 112));

		
	}
	
	public void draw(Canvas canvas) {
		int wid = canvas.getWidth();
		int hit = canvas.getHeight();
		
		// Determine the minimum of the two dimensions
		int minDim = wid < hit ? wid : hit;
		
		gameArea = (int)(minDim * SCALE_IN_VIEW);
		
		// Compute the margins so we center the puzzle
		marginX = (wid - gameArea) / 2;
		marginY = (hit - gameArea) / 2;
		
		//game area is entire canvas 
		canvas.drawRect(0, 0, wid, hit, fillPaint);
		
		scaleFactor = (float)gameArea / (float)canvas.getWidth();
		
		for(Brick brick : bricks) {
			brick.draw(canvas, marginX, marginY, gameArea, scaleFactor);
		}

	}
	
    /**
     * Handle a touch event from the view.
     * @param view The view that is the source of the touch
     * @param event The motion event describing the touch
     * @return true if the touch is handled.
     */
    public boolean onTouchEvent(View view, MotionEvent event) {
    	
        //
        // Convert an x,y location to a relative location in the 
        // puzzle.
        //
        
        float relX = (event.getX() - marginX) / gameArea;
        float relY = (event.getY() - marginY) / gameArea;
    	
        switch (event.getActionMasked()) {

        case MotionEvent.ACTION_DOWN:
            return onTouched(relX, relY);


        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            if(dragging != null) {
                dragging = null;
                return true;
            }
            break;

        case MotionEvent.ACTION_MOVE:
            // If we are dragging, move the brick and force a redraw
            if(dragging != null) {
                dragging.move(relX - lastRelX, relY - lastRelY);
                lastRelX = relX;
                lastRelY = relY;
                view.invalidate();
                return true;
            }
            break;
        }

        
        return false;
    }
    
    /**
     * Handle a touch message. This is when we get an initial touch
     * @param x x location for the touch, relative to the game area - 0 to 1 over the area
     * @param y y location for the touch, relative to the game area - 0 to 1 over the area
     * @return true if the touch is handled
     */
    private boolean onTouched(float x, float y) {
        
        // Check each brick to see if it has been hit
    	if ( bricks.size() > 0){
            int b=bricks.size()-1;
            if(bricks.get(b).hit(x, y, gameArea, scaleFactor)) {
                // We hit a brick!
                dragging = bricks.get(b);
                lastRelX = x;
                lastRelY = y;
                
                return true;
            }
    	}

        
        
        return false;
    }
    
    public void newTurn(Context context, float weight){
    	if (turn == 0){
    		bricks.add(new Brick(context, R.drawable.brick_barney, 0.5f, .9f-stackHeight, weight));
    		turn = 1;
    	}
    	else {
    		bricks.add(new Brick(context, R.drawable.brick_blue, .5f, .9f-stackHeight, weight));
    		turn = 0;
    	}
    	stackHeight += .075f;

    }
    


}
