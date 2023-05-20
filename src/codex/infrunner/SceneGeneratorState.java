/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import codex.infrunner.components.CopyPosition;
import codex.infrunner.components.KillThreshold;
import codex.infrunner.panels.PanelThread;
import codex.infrunner.panels.PanelBuildManager;
import codex.infrunner.panels.FlatPanelBuilder;
import codex.infrunner.components.Movement;
import codex.infrunner.panels.PanelBatchBuilder;
import codex.jmeutil.es.ESAppState;
import codex.jmeutil.es.factory.EntityFactory;
import codex.jmeutil.es.factory.EntitySpawner;
import codex.jmeutil.es.VisualState;
import codex.jmeutil.es.components.Color;
import codex.jmeutil.es.components.GameObject;
import codex.jmeutil.es.components.Position;
import codex.jmeutil.es.components.Scale;
import codex.jmeutil.es.components.Size;
import codex.jmeutil.es.components.Visual;
import codex.jmeutil.listen.Listenable;
import codex.jmeutil.math.AxisConstraint;
import codex.jmeutil.math.Threshold3f;
import com.jme3.app.Application;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.Node;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.simsilica.es.Filters;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author gary
 */
public class SceneGeneratorState extends ESAppState implements Listenable<SceneGeneratorListener>,
		EntityFactory {
	
	// size of panel, y is forward, z is up
	public static final Vector3f
			PANEL_SIZE = new Vector3f(1f, 5f, 3f);
	public static final float
			BPS = 2.4f,
			SPEED = PANEL_SIZE.y*BPS*2f;
	
	RunnerAppState runner;
	EntitySet panels;
	EntityId hyperspace;
	EntityId sun;
	PanelBuildManager builder;
	FilterPostProcessor fpp;
	Vector3f spawnMain = new Vector3f(0f, 0f, 200f);
	PanelThread[] threads;
	EntityId lastpanel;
	Threshold3f panelkillpoint = new Threshold3f(null, 0, null, 0, -20f, -1);
	LinkedList<SceneGeneratorListener> listeners = new LinkedList<>();
	StepTag[] tags;
	int tagindex = 0;
	int leveloffset = 0;
	
	@Override
	protected void init(Application app) {
		panels = ed.getEntities(
				Filters.fieldEquals(GameObject.class, "type", "panel"),
				GameObject.class,
				Position.class);
		visuals.registerScene("game_collision", new Node("game collision scene"), VisualState.ROOTNODE);
		
		initializeRunner();
		createSkybox();
		initializeRodSpawner();
		initializePanels();
		initializeFilters();
	}
	@Override
	protected void cleanup(Application app) {
		lastpanel = null;
		ed.removeEntity(hyperspace);
		ed.removeEntity(sun);
		hyperspace = null;
		clearAllPanels();
		panels.release();
		panels = null;
		//rodspawn.removeFromParent();
		visuals.removeScene("game_collision");
		getStateManager().detach(runner);
		app.getViewPort().removeProcessor(fpp);
	}
	@Override
	protected void onEnable() {}
	@Override
	protected void onDisable() {}
	@Override
	public void update(float tpf) {
		fillPanelVoid();
		panels.applyChanges();
		PanelBatchBuilder.ThreadGroupStatistics stats = new PanelBatchBuilder.ThreadGroupStatistics(threads);
		Vector3f runnerPos = ed.getComponent(runner.getRunnerId(), Position.class).getPosition();
		if (runnerPos.y < spawnMain.y+leveloffset*PANEL_SIZE.z+stats.lowest.get3DLocation().y-PANEL_SIZE.z*5) {
			// a concurrent modification exception occured here
			notifyListeners(l -> l.onRunnerDeath(runner));
			setLevelOffset((int)(runnerPos.y/SceneGeneratorState.PANEL_SIZE.z)-5);
			spawnMain.setX(runnerPos.x-PANEL_SIZE.x*threads.length/2);
			createStarterPanels();
		}
	}
	@Override
	public EntityId createEntity(EntitySpawner spawner, Vector3f baselocation, Vector3f offset) {
		Vector3f runnerPos = ed.getComponent(runner.getRunnerId(), Position.class).getPosition();
		boolean fast = FastMath.rand.nextBoolean();
		EntityId id = ed.createEntity();
		ed.setComponents(id,
				new Position(runnerPos.x+offset.x, runnerPos.y+offset.y, spawnMain.z),
				new Scale(new Vector3f(.05f, .05f, (fast ? .2f : .1f))),
				new Movement(new Vector3f(0f, 0f, -SPEED*(fast ? 2f : 1f)), true),
				new KillThreshold(panelkillpoint),
				new Visual(GameModelFactory.HYPERSPACE_ROD));
		return id;
	}
	
	private void initializeRunner() {
		EntityId r = ed.createEntity();
		ed.setComponents(r,
				new Position(RunnerAppState.START_POSITION.add(0f, leveloffset*PANEL_SIZE.z, 0f)),
				new Movement(new Vector3f(0f, -.0001f, 0f)),
				new Visual(GameModelFactory.MAIN_CHARACTER),
				new Size(.3f, .3f, .5f),
				new Color(ColorRGBA.Blue));
		runner = new RunnerAppState(r, visuals.getScene("game_collision"));
		getStateManager().attach(runner);
	}
	private void createSkybox() {
		sun = ed.createEntity();
//		ed.setComponents(sun,
//				new GameObject("sun"),
//				new Position(0f, 0f, 250f),
//				new CopyPosition(runner.getRunnerId(), new AxisConstraint(true, true, false)),
//				new Scale(100f),
//				new Visual(GameModelFactory.SUN));
		hyperspace = ed.createEntity();
		ed.setComponents(hyperspace,
				new GameObject("hyperspace"),
				new Position(new Vector3f(0f, 0f, 20f)),
				new CopyPosition(runner.getRunnerId(), new AxisConstraint(true, true, false)),
				new Scale(100f),
				new Visual(GameModelFactory.HYPERSPACE));
	}
	private void initializeRodSpawner() {
		//rodspawn = new EntitySpawner(this, .01f, 100f);
		//visuals.getScene(VisualState.ROOTNODE).attachChild(rodspawn);
	}
	private void initializePanels() {
		// tempo = 144 bpm, or 2.4 bps
		builder = new PanelBuildManager(new FlatPanelBuilder());
//		builder.setDelay(startDelay);
		threads = new PanelThread[25];
		Noise tendency = new Noise(0f);
		tendency.getStepDomain().set(0f, 2f);
		tendency.getGlobalDomain().set(-1f, 4f);
		for (int i = 0; i < threads.length; i++) {
			PanelThread thread = new PanelThread(new Vector2f(PANEL_SIZE.x*i, 0f));
			thread.setUpTendency((int)tendency.getNext());
			threads[i] = thread;
		}
		spawnMain.setX(-(PANEL_SIZE.x*threads.length)/2);
		createStarterPanels();
	}
	private void initializeFilters() {
		fpp = new FilterPostProcessor(assetManager);
		//MotionBlurFilter blur = new MotionBlurFilter();
		//blur.setSampleDistance(1f);
		//blur.setSampleStrength(.5f);
		//fpp.addFilter(blur);
		BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Scene);
		bloom.setBloomIntensity(2f);
		bloom.setBlurScale(1.5f);
		//bloom.setExposurePower(10f);
		//fpp.addFilter(bloom);
		//getApplication().getViewPort().addProcessor(fpp);
	}
	private void createPanelBatch(Vector3f location) {
		EntityId[] batch = builder.buildPanelBatch(ed, location, threads, PANEL_SIZE, builder.getGlobalStep(), leveloffset);
		processNewPanels(batch);
	}
	private void processNewPanels(EntityId[] panels) {
		for (EntityId panel : panels) {
			if (panel == null) continue;
			// speed is multiplied each frame by tpf automatically in MovementState
			ed.setComponents(panel, 
					new Movement(new Vector3f(0f, 0f, -SPEED), true),
					new KillThreshold(panelkillpoint));
			lastpanel = panel;
		}
	}
	private void fillPanelVoid() {
		while (true) {
			Vector3f paneloffset = ed.getComponent(lastpanel, Position.class).getPosition();
			if (paneloffset.z <= spawnMain.z-PANEL_SIZE.y) {
				createPanelBatch(spawnMain.subtract(0f, 0f, spawnMain.z-paneloffset.z-PANEL_SIZE.y));
				for (PanelThread thread : threads) {
					thread.getColor().h += 0.005f;
					if (thread.getColor().h > 1f) {
						thread.getColor().h--;
					}
				}
				//step++;
				notifyListeners(l -> {
					l.onGenerationStep(builder.getGlobalStep());
					if (tags != null && tagindex < tags.length
							&& tags[tagindex].getStepNum() == builder.getGlobalStep()) {
						l.onTaggedStep(tags[tagindex++]);
					}
				});
			}
			else {
				break;
			}
		}
	}
	
	public void clearAllPanels() {
		panels.forEach(p -> ed.removeEntity(p.getId()));
	}
	public void createStarterPanels() {
		clearAllPanels();
		setThreadHeights(0);
		int start = (int)((spawnMain.z-panelkillpoint.z)/(PANEL_SIZE.y));
		createPanelBatch(spawnMain.subtract(0f, 0f, start*PANEL_SIZE.y));
		fillPanelVoid();
	}
	
	public void resetStep() {
		builder.resetGlobalStep();
	}
	public void setStepTagArray(StepTag[] tags) {
		tagindex = 0;
		this.tags = tags;
	}
	public void setSpawnDistance(float distance) {
		spawnMain.setZ(distance);
	}
	public void setThreadHeights(int height) {
		for (PanelThread thread : threads) {
			thread.setLevel(height);
		}
	}
	public void setLevelOffset(int leveloffset) {
		this.leveloffset = leveloffset;
	}
	
	@Override
	public Collection<SceneGeneratorListener> getListeners() {
		return listeners;
	}
	public PanelBuildManager getBuildManager() {
		return builder;
	}
	public PanelThread[] getThreads() {
		return threads;
	}
	public Vector3f getPanelSpawnMain() {
		return spawnMain;
	}
	public float getPanelKillPoint() {
		return panelkillpoint.z;
	}
	public int getStep() {
		return builder.getGlobalStep();
	}
	public StepTag[] getTags() {
		return tags;
	}
	public float getSpawnPoint() {
		return spawnMain.z;
	}
	public RunnerAppState getRunner() {
		return runner;
	}
	public int getLevelOffset() {
		return leveloffset;
	}
	
}
