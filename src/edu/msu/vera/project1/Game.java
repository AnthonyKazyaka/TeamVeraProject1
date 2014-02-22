package edu.msu.vera.project1;

import java.util.ArrayList;

import edu.msu.vera.project1.Brick;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Game {
	
	// canvas height
    private float cHeight;
    // canvas width
    private float cWidth;
    // y value of the base of the stack of bricks
    private float brickBase;
    /**
     * Boolean if the top brick is placed or not
     */
    private boolean isPlaced;
    
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
	 * Collection of bricks used the store the stack
	 */
	public ArrayList<Brick> bricks = new ArrayList<Brick>();
	
	/*
	 * Index of the highest brick that is stable
	 */
	private int highestStableBrick = -1;
	private boolean isStable = true;
	
	private int unstableRotationDirection = 1;
	private float unstableRotationAngle = 0.0f;
	
	private long timeOfUnstablePlacement = 0;
	private double timeSinceUnstablePlacement = 0;
	private boolean isDoneAnimatingFall = false;
	
	
	public int turnB = 1;
	//1 = player1
	//2 = player2
	
	public int player1 = 0;
	public int player2 = 0;
	//if true, means 
	
	/**
	 * The name of the bundle keys to save the stack
	 */
	private final static String LOCATIONS = "Game.locations";
	private final static String IDS = "Game.ids";
	private final static String WEIGHTS = "Game.weights";

		
	public Game(Context context) {
		fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		fillPaint.setColor(Color.rgb(112, 112, 112));


	}
	
	public void draw(Canvas canvas, View view) {
		int wid = canvas.getWidth();
		int hit = canvas.getHeight();
		
		cHeight = canvas.getHeight();
		cWidth = canvas.getWidth();
		
		Log.i("Game", "Highest stable brick: " + highestStableBrick);
		if(!isStable && !isDoneAnimatingFall && unstableRotationAngle < 90.0f){
			timeSinceUnstablePlacement = System.currentTimeMillis() - timeOfUnstablePlacement;
			unstableRotationAngle +=  timeSinceUnstablePlacement / 100.0f;
			if(unstableRotationAngle > 90.0f){
				unstableRotationAngle = 90.0f;
				isDoneAnimatingFall = true;
			}
			view.postInvalidate();
		}
		
		//Log.i("Game", "Rotation Angle: " + unstableRotationAngle);
		
		//game area is entire canvas 
		canvas.drawRect(0, 0, wid, hit, fillPaint);
		
		canvas.save();
		
		for(int i = 0; i < bricks.size(); i++)
		{
			bricks.get(i).draw(canvas);
			if(i == highestStableBrick && highestStableBrick > -1){
				float edgeOfBrickX = bricks.get(i).getX() + unstableRotationDirection * bricks.get(i).getWidth() / 2.0f;
				float topEdgeOfBrickY = bricks.get(i).getY()  - bricks.get(i).getHeight() / 2.0f;
				canvas.rotate(unstableRotationDirection * unstableRotationAngle, edgeOfBrickX, topEdgeOfBrickY);
			}	
		}
		
		canvas.restore();

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
        
        // Check top brick to see if it has been hit
    	if ( bricks.size() > 0){
            int b=bricks.size()-1;
            if(bricks.get(b).hit(x, y) && isPlaced) {
                // We hit a brick that has not been placed
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
    	
		Log.i("Game", "Now is player x's turn, x : " + turnB);
    	// when new turn is called is confirms where the base of bricks is located and adds the brick
    	// depending on the turn with the arguments of context, brick_color, x, y, and weight
    	// Also reset isPlaced to true so the brick can be dragged and placed. 
    	
    	if (bricks.size() > 0){
    		brickBase = bricks.get(0).getY();
    	}
    	else {
    		brickBase = cHeight-50;
    	}

    	if (turn == 0){
    		bricks.add(new Brick(context, R.drawable.brick_barney, cWidth/2, brickBase-stackHeight , weight));
    		turn = 1;
    		isPlaced = true;
    	}
    	else {
    		bricks.add(new Brick(context, R.drawable.brick_blue, cWidth/2, brickBase-stackHeight, weight));
    		turn = 0;
    		isPlaced = true;

    	}
    	stackHeight += bricks.get(0).getHeight();
		if (turnB == 1) {
			turnB = 2;
		}
		else {
			turnB = 1;
		}
		//if everything is fine, switch player
    }
    
    public int place(){
    	isPlaced = false;
    	boolean isStable = isStackStable();    	
    	Log.i("Game", "IsStackStable: " + isStable);
    	if (!isStable){
    		return turnB;
    		// if someone failed, return the winner 
    	}
    	else{
    	   	Log.i("Game", "Turn ? : " + turnB);
    		return 3;
    		//no one failed

    	}
    }
    
    
    private boolean isStackStable(){
    	
    	Brick currentBottomBrick;
    	Brick currentBrick;
    	
    	for(int i = bricks.size()-1; i >= 0; i--)
    	{
    		currentBottomBrick = bricks.get(i);
    		
        	float centerOfMass = 0.0f;
        	float totalMass = 0.0f;
        	
    		for(int j = i + 1; j < bricks.size(); j++){
    			currentBrick = bricks.get(j);
    			centerOfMass += currentBrick.getX() * currentBrick.getWeight();
    			totalMass += currentBrick.getWeight();
    		}
    		
    		if(i == bricks.size() - 1){
    			centerOfMass = currentBottomBrick.getX();
    		}
    		else{
    			centerOfMass = (centerOfMass / totalMass);
    		}
    		
    		float brickLeftXPos = currentBottomBrick.getX() - currentBottomBrick.getWidth() / 2.0f;
    		float brickRightXPos = currentBottomBrick.getX() + currentBottomBrick.getWidth() / 2.0f;
    		
    		if(centerOfMass >= brickLeftXPos && centerOfMass <= brickRightXPos);
    		else{
    			if(centerOfMass < brickLeftXPos)
    			{
    				unstableRotationDirection = -1;
    			}
    			//highestStableBrick = GetHighestStableBrick();
    			highestStableBrick = i;
				timeOfUnstablePlacement = System.currentTimeMillis();
    			isStable = false;
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    
	/**
	 * Save the puzzle to a bundle
	 * @param bundle The bundle we save to
	 */
	public void saveInstanceState(Bundle bundle) {
		float [] locations = new float[bricks.size() * 2];
		int [] ids = new int[bricks.size()];
		float [] weights = new float[bricks.size()];
		
		
		for(int i=0;  i<bricks.size(); i++) {
			Brick brick = bricks.get(i);
			locations[i*2] = brick.getX();
			locations[i*2+1] = brick.getY();
			ids[i] = brick.getId();
			weights[i] = brick.getWeight();
		}
		
		bundle.putFloatArray(LOCATIONS, locations);
		bundle.putIntArray(IDS,  ids);
		bundle.putFloatArray(WEIGHTS,  weights);
		bundle.putBoolean("place", isPlaced);
		bundle.putFloat("base", cWidth);

	}
	
	/**
	 * Read the puzzle from a bundle
	 * @param bundle The bundle we save to
	 */
	public void loadInstanceState(Bundle bundle, Context context) {
		float [] locations = bundle.getFloatArray(LOCATIONS);
		int [] ids = bundle.getIntArray(IDS);
		float [] weights = bundle.getFloatArray(WEIGHTS);
		isPlaced = bundle.getBoolean("place");
		
		brickBase = bundle.getFloat("base");
		brickBase = brickBase - (brickBase /5);
		
		for(int i=0; i<ids.length; i++) {

    		bricks.add(new Brick(context, ids[i], locations[i*2], brickBase - stackHeight, weights[i]));
        	stackHeight += bricks.get(0).getHeight();

		}
		if (bricks.size() >= 1){
			if ( bricks.get(bricks.size()-1).getId() == R.drawable.brick_barney){
				turn = 1;
			}
			else turn = 0;
		}
		else {
			turn = 0;
		}

	}
	    	
}
