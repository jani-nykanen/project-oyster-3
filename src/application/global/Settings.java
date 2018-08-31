package application.global;

import application.Gamepad;
import application.ui.Button;
import application.ui.MenuContainer;
import core.WeakEventManager;
import core.renderer.Graphics;
import core.renderer.Transformations;
import core.types.Vector2;
import core.utility.VoidCallback;

/**
 * Settings
 * @author Jani Nykänen
 *
 */
public class Settings extends MenuContainer {

	
	/** Amount of buttons */
	static final int BUTTON_COUNT = 5;
	
	/** Button texts */
	static final private String[] BUTTON_TEXT = new String[] {
		
		"Toggle Fullscreen",
		"Music Volume: 100%",
		"Sound volume: 100%",
		"Framerate: 30 FPS",
		"Back"
	};
	
	
	/**
	 * Constructor
	 */
	public Settings(WeakEventManager eventMan) {
		
		super();
		
		// Create callbacks for buttons
		VoidCallback[] cbs = new VoidCallback[BUTTON_COUNT];
		// Fullscreen
		cbs[0] = new VoidCallback() {
			@Override
			public void execute() {
				
				eventMan.toggleFullscreen();
			}
		};
		// Music volume
		cbs[1] = null;
		// Sound volume
		cbs[2] =  null;
		// FPS
		cbs[3]  = null;
		// Back
		cbs[4] = new VoidCallback() {
			@Override
			public void execute() {
				
				timer = TRANSITION_TIME;
				leaving = true;
			}
		};
		
		// Create buttons
		for(int i = 0; i < BUTTON_COUNT; ++ i) {
			
			buttons.addButton(new Button(BUTTON_TEXT[i], cbs[i]));
		}
	}
	
	
	@Override
	protected void activationEvent() { 
		
		buttons.setCursorPos(BUTTON_COUNT-1);
		
		// TODO: Read settings.xml
	}
	
	
	@Override
	public void updateEvent(Gamepad vpad, float tm) {
		
		// Update buttons
		buttons.update(vpad, tm);
	}

	
	@Override
	public void draw(Graphics g) {
		
		final float BOX_WIDTH = 720;
		final float BOX_HEIGHT = 384;
		final float SHADOW_OFFSET = 16;
		final float TEXT_X = 48.0f;
		final float TEXT_Y = 32.0f;
		final float XOFF = -26.0f;
		final float YOFF = 64.0f;
		final float TEXT_SCALE = 0.80f;
		final float SHADOW_OFF = 8.0f;
		
		if(!active) return;
		
		// Center the screen
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		tr.push();
		tr.translate(view.x / 2, view.y / 2);
		tr.use();
		
		// Set global alpha
		float alpha = timer > 0.0f ? 1.0f - timer / TRANSITION_TIME : 1.0f;
		if(leaving)
			alpha = 1.0f - alpha;
		
		g.setGlobalAlpha(alpha);
				
		// Draw a box
		drawBox(g, BOX_WIDTH , BOX_HEIGHT, SHADOW_OFFSET);
		
		// Draw buttons
		buttons.draw(g, bmpFont, -BOX_WIDTH/2 + TEXT_X, -BOX_HEIGHT/2 + TEXT_Y, 
				XOFF, YOFF, TEXT_SCALE, false, SHADOW_OFF);
				
		g.setGlobalAlpha();
		tr.pop();
	}

}
