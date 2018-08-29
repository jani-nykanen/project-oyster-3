package application.global;

import application.Gamepad;
import application.Scene;
import core.renderer.Graphics;
import core.utility.AssetPack;
import core.utility.RGBFloat;

/**
 * The global scene
 * @author Jani Nykänen
 *
 */
public class Global extends Scene {

	/** Scene name */
	public String name = "game";
	
	/** Transition object */
	private Transition trans;
	
	
	@Override
	public void init(AssetPack assets) throws Exception {
		
		// Create global components
		trans = new Transition();
		
		// TEMP: Fade!
		trans.activate(Transition.Mode.Out, Transition.Type.Fade, 1.0f, new RGBFloat(), null);
	}

	
	@Override
	public void update(Gamepad vpad, float tm) {
		
		// Update transitions
		trans.update(tm);
	}
	

	@Override
	public void draw(Graphics g) {
		
		// Draw transition
		trans.draw(g);
	}
	

	@Override
	public void destroy() {
		
		// ...
	}

	
	@Override
	public void changeTo() {
		
		// ...
	}

}
