package core;

import static org.lwjgl.glfw.GLFW.*;

import core.renderer.Graphics;


/**
 * Application listener, handles base application
 * behavior
 * @author Jani Nykänen
 *
 */
public class ApplicationListener extends WindowListener {

	/** Frame rate where 'time' is compared to */
	static final int COMPARABLE_FRAME_RATE = 60;
	/** Default frame rate */
	static final int DEFALT_FRAME_RATE = 30;

	/** Input manager, handles input */
	protected InputManager input;
	
	/** Graphics context */
	protected Graphics graph;
	
	/** Frame rate */
	private int frameRate;
	
	/** Time sum */
	private double timeSum;
	
	
	/**
	 * Initialize application
	 */
	private void init() throws Exception {
		
		// Call user-defined starting method before
		// anything is initialized
		onStart();
		
		// Create components
		input = new InputManager();
		
		// Create a window and its context
		initWindowContext();
		
		// Initialize graphics
		graph = new Graphics();
		int[] winSize = getWindowSize();
		graph.setViewport(winSize[0], winSize[1]);

		// Read certain data from configuration
		frameRate = conf.getParameterValueInt("frame_rate", DEFALT_FRAME_RATE);
		
		// Call user-defined initialization method now
		onInit();
		
		// Load data
		// ...
		
		// Loading finished, call post-loading initialization
		// method
		onLoaded();
		
		// Reset timer
		glfwSetTime(0.0);
		timeSum = 0.0;
	}
	
	
	@Override
	protected void eventResize(int w, int h) { 
		
		graph.setViewport(w, h);
	}


	@Override
	protected void eventKeyDown(int key) { 
		
		input.onKeyPressed(key);
	}


	@Override
	protected void eventKeyUp(int key) { 
		
		input.onKeyReleased(key);
	}

	
	@Override
	protected void eventJoyAxis(float x, float y) {
		
		input.onJoyAxis(x, y);
	}
	
	
	@Override
	protected void eventJoyDown(int button) { 
		
		input.onJoyPressed(button);
	}
	
	
	@Override
	protected void eventJoyUp(int button) { 
		
		input.onJoyReleased(button);
	}
	
	
	/**
	 * Update loop
	 * @param tm Time multiplier
	 */
	private void update(float tm) {
		
		// Call user-defined frame update method
		onUpdate(tm);
		
		// Update input
		input.update();
		
	}
	
	
	/**
	 * Draw application
	 */
	private void draw() {
		
		// Call user-defined frame rendering method
		onDraw();
	}
	
	
	/**
	 * Main loop
	 */
	private void loop() {
		
		// We update only certain amount of frames
		// per refresh. This way we can make sure
		// there won't be a hundred frames waiting
		// to be updated.
		final int MAX_FRAME_UPDATE = 5;
		
		boolean redraw = true;
		final double frameWait = 1.0 / frameRate; 
		
		// Wait until enough time has passed, 
		// then update the frame
		timeSum += glfwGetTime();
		glfwSetTime(0.0);
		int updateCount = 0;
		while(timeSum >= frameWait) {

			// Update frame and set frame to be redrawable
			update((float)COMPARABLE_FRAME_RATE / (float)frameRate);
			redraw = true;
			
			timeSum -= frameWait;
			
			if(++ updateCount >= MAX_FRAME_UPDATE)
				break;
		}

		// Draw frame, if necessary
		if(redraw) {
			
			draw();
		}
		
		// If close button pressed
		if(shouldClose()) {
			
			terminate();
		}
		
		// Refresh frame
		refresh();
		
		// Poll events
		glfwPollEvents();
		// Call update event
		updateJoyEvents();
	}
	
	
	/**
	 * Destroy application
	 * @param success If there was an error
	 */
	private void destroy(boolean success) {
		
		// If no errors, we can assume everything was created
		// successfully and thus can be destroyed as well
		if(success) {
		
			// Destroy window
			destroyWindowContext();
			
			// Call user-defined destroy method before
			// destroying "critical" content
			onDestroy();
		}
	}
	
	
	/**
	 * Run application
	 * @param args Arguments
	 */
	public void run(String[] args) {
		
		boolean success = true;
		try {
			
			// Initialize
			init();
			
			// Start the main loop
			while(isRunning()) {
				
				loop();
			}
		}
		catch(Exception e) {
			
			e.printStackTrace();
			success = false;
			
		}
		finally {
			
			// Destroy application content
			destroy(success);
		}
	}
}
