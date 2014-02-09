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
	
    private float cHeight;
    private float cWidth;
    private float brickBase;
    
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
     * How far the bricks are scrolled up
     */
    private float scrollDistance = 0;
    
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
		
		cHeight = canvas.getHeight();
		cWidth = canvas.getWidth();
		
		//game area is entire canvas 
		canvas.drawRect(0, 0, wid, hit, fillPaint);
				
		for(Brick brick : bricks) {
			brick.draw(canvas);
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
        
        float relX = event.getX();
        float relY = event.getY();
    	
        switch (event.getActionMasked()) {

        case MotionEvent.ACTION_DOWN:
            return onTouched(relX, relY);


        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            if(dragging != null) {
                dragging = null;
                return true;
            }
            else if ( scrollDistance > 0){
            	scrollDistance = 0;
            }
            break;

        case MotionEvent.ACTION_MOVE:
            // If we are dragging, move the brick and force a redraw
            if(dragging != null) {
                dragging.move(relX - lastRelX, 0);
                lastRelX = relX;
                lastRelY = relY;
                view.invalidate();
                return true;
            }
            else if (scrollDistance <= 0){
            
        		for(Brick brick : bricks) {
                    brick.move(0, relY - lastRelY);              
        		}
                scrollDistance += (lastRelY -relY);
                lastRelY = relY;
                view.invalidate();
        		return true;
            }
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
            if(bricks.get(b).hit(x, y)) {
                // We hit a brick!
                dragging = bricks.get(b);
                lastRelX = x;
                lastRelY = y;
                
                return true;
            }
            else{                
            	lastRelX = x;
            	lastRelY = y;
            	return true;
            }

    	}

        
        
        return false;
    }
    
    public void newTurn(Context context, float weight){
    	
    	if (bricks.size() > 0){
    		brickBase = bricks.get(0).getY();
    	}
    	else {
    		brickBase = cHeight-50;
    	}

    	if (turn == 0){
    		bricks.add(new Brick(context, R.drawable.brick_barney, cWidth/2, brickBase-stackHeight , weight));
    		turn = 1;
    	}
    	else {
    		bricks.add(new Brick(context, R.drawable.brick_blue, cWidth/2, brickBase-stackHeight, weight));
    		turn = 0;
    	}
    	stackHeight += bricks.get(0).getHeight();

    }
    

    	
}
