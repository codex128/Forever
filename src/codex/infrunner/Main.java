package codex.infrunner;

import codex.j3map.J3mapFactory;
import codex.jmeutil.es.EntityState;
import codex.jmeutil.es.PositionUpdateState;
import codex.jmeutil.es.ScaleUpdateState;
import codex.jmeutil.es.VisualState;
import codex.jmeutil.es.components.Visual;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
	
	public static final boolean
			DEV_MODE = false; // release: change to false
	
	
    public static void main(String[] args) {
        Main app = new Main();
		app.setDisplayFps(false);
		app.setDisplayStatView(false);
		AppSettings as = new AppSettings(true);
		as.setGammaCorrection(true);
		as.setUseJoysticks(true);
		app.setSettings(as);
        app.start();
    }

    @Override
    public void simpleInitApp() {
		
		GuiGlobals.initialize(this);
		Functions.initialize(GuiGlobals.getInstance().getInputMapper());
		
		VisualState visuals = new VisualState();
		GameModelFactory mainfactory = new GameModelFactory();
		visuals.registerModelFactory("main_factory", mainfactory);
		Visual.setDefaultFactory("main_factory");
		
		stateManager.attachAll(
				new EntityState(),
				visuals,
				new PositionUpdateState(),
				new ScaleUpdateState(),
				new MovementState(),
				new DecayState(),
				new CopyPositionState(),
				new ThresholdKillState(),
				new MusicAppState(),
				new SceneGeneratorState(),
				new TitleState());
		
    }
    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
