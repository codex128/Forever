/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import codex.infrunner.components.Dialog;
import codex.infrunner.panels.AdvancedPanelBuilder;
import codex.infrunner.panels.ChordPanelBuilder;
import codex.infrunner.panels.FlatPanelBuilder;
import codex.infrunner.panels.MaskPanelBuilder;
import codex.infrunner.panels.NormalizePanelBuilder;
import codex.infrunner.panels.PanelBatchBuilder;
import codex.infrunner.panels.PanelBuildManager;
import codex.infrunner.panels.PatternPanelBuilder;
import codex.infrunner.panels.SimplePanelBuilder;
import codex.j3map.J3map;
import codex.jmeutil.es.ESAppState;
import codex.jmeutil.es.VisualState;
import com.jme3.app.Application;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;

/**
 *
 * @author gary
 */
public class GameState extends ESAppState implements StateFunctionListener,
		SceneGeneratorListener {
	
	private static int
			completions = 0,
			universalStep = 0;
	
	SceneGeneratorState generator;
	SceneGeneratorListener hudUpdater;
	DialogAppState dialog;
	
	@Override
	protected void init(Application app) {
		generator = getState(SceneGeneratorState.class);
		
		configureGenerator();
		initializeMusic();
		createHUD();
		initializeInput();
		
//		if (completions > 0) {
//			J3map source = J3map.openJ3map(assetManager.loadAsset("Interface/Dialog.j3map"));
//			dialog = new DialogAppState(source.getProperty(Dialog.class, "dialog1"));
//			getStateManager().attach(dialog);
//		}
	}
	@Override
	protected void cleanup(Application app) {
		generator.removeListener(this);
		generator.removeListener(hudUpdater);
		generator.setStepTagArray(null);
		visuals.removeScene("game_gui");
		if (dialog != null) {
			getStateManager().detach(dialog);
		}
	}
	@Override
	protected void onEnable() {}
	@Override
	protected void onDisable() {}
	@Override
	public void update(float tpf) {}
	
	private void configureGenerator() {
		// tempo = 144 bpm or 2.4 bps
		PanelBuildManager pbm = generator.getBuildManager();
		pbm.queuePanelBuilder(new FlatPanelBuilder(), 4);
		pbm.queuePanelBuilder(new FlatPanelBuilder(.3f), 35);
		pbm.queuePanelBuilder(new SimplePanelBuilder(), 97);
		pbm.queuePanelBuilder(new AdvancedPanelBuilder(1f, -.05f), 160);
		pbm.queuePanelBuilder(new PatternPanelBuilder(), 222);
		pbm.queuePanelBuilder(new AdvancedPanelBuilder(), 534);
		pbm.queuePanelBuilder(new AdvancedPanelBuilder(.75f, -.15f), 657);
		pbm.queuePanelBuilder(new AdvancedPanelBuilder(.75f, -.075f), 907);
		pbm.queuePanelBuilder(new NormalizePanelBuilder(), 908);
		pbm.queuePanelBuilder(new MaskPanelBuilder(new PatternPanelBuilder(),
				new ChordPanelBuilder(15, -1, 15, -1, 9, 1, 1, -1)), 958);
		pbm.queuePanelBuilder(new FlatPanelBuilder(), 1000);
		generator.resetStep();
		generator.setStepTagArray(new StepTag[] {
			new StepTag("start_music", 5),
			new StepTag("end", 1000),
		});
		generator.addListener(this);
	}
	private void initializeMusic() {		
		getState(MusicAppState.class).loadMusic("Sounds/emotional-battle.wav");		
	}
	private void createHUD() {
		float width = app.getContext().getSettings().getWidth();
		float height = app.getContext().getSettings().getHeight();
		visuals.registerScene("game_gui", new Node(), VisualState.GUINODE);		
        BitmapFont font = assetManager.loadFont("Interface/Fonts/LiberationSansNarrow.fnt");
		BitmapText titletext = new BitmapText(font);
        titletext.setSize(font.getCharSet().getRenderedSize());
        titletext.setText("Forever");
        titletext.setLocalTranslation(10, height-5, 0);
		titletext.setColor(ColorRGBA.White);
        //visuals.getScene("game_gui").attachChild(titletext);
        BitmapText scoretext = new BitmapText(font);
        scoretext.setSize(font.getCharSet().getRenderedSize());
        scoretext.setText("Hello World");
        scoretext.setLocalTranslation(10, height-50, 0);
		scoretext.setColor(ColorRGBA.White);
        //visuals.getScene("game_gui").attachChild(scoretext);
        BitmapText steptext = new BitmapText(font);
        steptext.setSize(15);
        steptext.setText("Step: 0");
        steptext.setLocalTranslation(10, 25, 0);
		steptext.setColor(ColorRGBA.White);
        visuals.getScene("game_gui").attachChild(steptext);
		hudUpdater = new SceneGeneratorListener() {
			@Override
			public void onGenerationStep(int step) {
				steptext.setText("Step: "+universalStep);
			}
		};
		generator.addListener(hudUpdater);
	}
	private void initializeInput() {}
	
	@Override
	public void onGenerationStep(int step) {
		universalStep++;
	}
	@Override
	public void onTaggedStep(StepTag tag) {
		String t = tag.getTag();
		switch (t) {
			case "start_music":
				getState(MusicAppState.class).playMusic(); break;
			case "end":
				completions++;
				getState(MusicAppState.class).setMusicVolumeFade(-.02f);
				generator.getBuildManager().clearBuilderQueue();
				generator.setLevelOffset(generator.getLevelOffset()+new PanelBatchBuilder.ThreadGroupStatistics(generator.getThreads()).average);
				generator.setThreadHeights(0);
				getStateManager().detach(this);
				getState(TitleState.class).setEnabled(true); break;
		}
	}
	@Override
	public void onRunnerDeath(RunnerAppState runner) {
		getState(MusicAppState.class).setMusicVolumeFade(-.02f);
		generator.removeListener(this);
		generator.getBuildManager().clearBuilderQueue();
		getStateManager().detach(this);
		getState(TitleState.class).setEnabled(true);
		universalStep = 0;
		completions = 0;
	}
	@Override
	public void valueChanged(FunctionId func, InputState value, double tpf) {
		
	}
	
}
