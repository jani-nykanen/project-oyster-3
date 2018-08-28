package application.gamefield;

import application.Gamepad;
import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.types.Direction;
import core.types.Point;
import core.utility.AssetPack;

/**
 * A movable crate
 * @author Jani Nykänen
 *
 */
public class Crate extends NonPlayerFieldObject {

	/** A bitmap of movable object */
	private static Bitmap bmpMovable;

	
	/**
	 * Initialize global crate content
	 * (read: get assets)
	 * @param assets Assets
	 */
	static public void init(AssetPack assets) {
		
		// Get assets
		bmpMovable = assets.getBitmap("movable");
	}
	
	
	@Override
	protected void eventPlayerInteraction(Player pl, Stage stage, TimeManager tman) {
	
		// TODO: Repeating code from Player. Add
		// universal method?
		// Check direction
		Point t = pos.clone();
		switch(playerDir) {
		
		case Down:		
			++ t.y;
			break;
			
		case Left:
			-- t.x;
			break;
			
		case Right:
			++ t.x;
			break;
			
		case Up:
			-- t.y;
			break;
			
		default:
			break;
		
		}
		
		boolean doMove = true;
		// Check if not out of bounds
		if(t.x < 0 || t.y < 0 
			|| t.x >= stage.getWidth() || t.y >= stage.getHeight()) {
			
			doMove = false;
		}
		
		// Check if a free tile
		if(stage.isSolidExcludeLava(t.x, t.y))
			doMove = false;
		
		// If no obstacles, move
		if(doMove) {
			
			target.x = t.x;
			target.y = t.y;
			moving = true;

			// Update solid data
			stage.updateTileData(pos.x, pos.y, 0);
		}
		
	}
	
	
	/**
	 * Constructor
	 * @param pos Target position
	 */
	public Crate(Point pos) {
		
		super(pos);
	}


	@Override
	public void update(Gamepad vpad, TimeManager tman, Stage stage, float tm) {
		
		if(!exist) {
			
			updateDeath(tm);
		}
	}
	

	@Override
	public void draw(Graphics g) {
		
		if(!exist) {
		
			// If dying, draw death
			drawDeath(g, bmpMovable, 0, 0, 128, 128, 0.0f);

			return;
		}
		
		g.drawScaledBitmapRegion(bmpMovable,0,0,128,128,
				vpos.x, vpos.y, scaleValue.x, scaleValue.y, Flip.NONE);
			
	}


	@Override
	public void playerCollision(Player pl, Gamepad vpad, Stage stage, TimeManager tman) {
		
		if(!exist || tman.isWaiting() || pl.isMoving()) return;
		
		checkPlayerInteraction(pl, vpad, stage, tman);
	}

	
	@Override
	public void onMovingStopped(Stage stage, TimeManager tman) {
		
		// If lava, die
		if(stage.getTile(pos.x, pos.y) == 3) {
			
			stage.updateTileData(pos.x, pos.y, 0);
			die(tman);
		}
		else {
			
			stage.updateTileData(pos.x, pos.y, 2);
		}
	};
}