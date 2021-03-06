package edu.msu.vera.project1;


import edu.msu.vera.project1.BrickView;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.view.Menu;
import android.view.View;

@SuppressWarnings("unused")
public class GameActivity extends Activity {

	/**
	 * The brick view in this activity's view
	 */
	private BrickView brickView;
		
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_game);
		
		brickView = (BrickView)this.findViewById(R.id.brickView);
		
		if(bundle != null) {
			// We have saved state
			brickView.loadInstanceState(bundle, this.getBaseContext());
		}
		brickView.startDia();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
	
	public void onCreateBrick1(View view){
        brickView.addBrick(1);
	}
	
	public void onCreateBrick2(View view){
        brickView.addBrick(2);
	}
	
	public void onCreateBrick5(View view){
        brickView.addBrick(5);
	}
	
	public void onCreateBrick10(View view){
        brickView.addBrick(10);
	}
	
	public void onGameOver(View view) {
		Intent intent = new Intent(this, EndGameActivity.class);
		startActivity(intent);
	}
	
	public void onPlaceBrick(View view){
        brickView.placeBrick();
	}

	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(bundle);
		
		brickView.saveInstanceState(bundle);
	}


	
	

}
