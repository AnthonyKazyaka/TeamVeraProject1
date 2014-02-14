package edu.msu.vera.project1;

import edu.msu.vera.project1.Game;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class BrickView extends View {
 
	private Context cont;
	/**
	 * The actual game
	 */
	private Game game;
	
	public BrickView(Context context) {
		super(context);
		cont = context;
		init(context);
	}

	public BrickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		cont = context;
		init(context);
	}

	public BrickView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		cont = context; 
		init(context);
	}
	
	private void init(Context context) {
		game = new Game(context);

	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		game.draw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
        return game.onTouchEvent(this, event);

	}
	
    public void addBrick(float weight) {
        game.newTurn(cont, weight);
        this.invalidate();
    }
	
    public void placeBrick(){
    	game.place();
    }
	

}
