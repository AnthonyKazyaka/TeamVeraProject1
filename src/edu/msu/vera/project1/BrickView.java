package edu.msu.vera.project1;

import edu.msu.vera.project1.Game;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
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

		game.draw(canvas, this);
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
    	int result = game.place();
    	if (result != 3){
    		if (result == 1) {
                // Instantiate a dialog box builder
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                
                // Parameterize the builder
                builder.setTitle("Winner !");
                builder.setMessage("Winner is player1 !!!");
                //builder.setPositiveButton(android.R.string.ok, null);
                
                // Create the dialog box and show it
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
    		}
    		else {
                // Instantiate a dialog box builder
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                
                // Parameterize the builder
                builder.setTitle("Winner !");
                builder.setMessage("Winner is player2 !!!");
                //builder.setPositiveButton(android.R.string.ok, null);
                
                // Create the dialog box and show it
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
    		}
    	}
    	this.invalidate();
    }
    
	/**
	 * Save the stack to a bundle
	 * @param bundle The bundle we save to
	 */
	public void saveInstanceState(Bundle bundle) {
		game.saveInstanceState(bundle);
	}
	
	/**
	 * Load the stack from a bundle
	 * @param bundle The bundle we save to
	 */
	public void loadInstanceState(Bundle bundle, Context context) {
		game.loadInstanceState(bundle, context);
	}
	
	public void startDia(){
        // Instantiate a dialog box builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        
        // Parameterize the builder
        builder.setTitle(R.string.hurrah);
        builder.setMessage(R.string.completed_puzzle);
        builder.setPositiveButton(android.R.string.ok, null);
        
        // Create the dialog box and show it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
	}
	

}
