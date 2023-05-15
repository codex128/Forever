/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import codex.jmeutil.Timer;
import codex.jmeutil.es.ESAppState;
import codex.jmeutil.es.VisualState;
import com.jme3.app.Application;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

/**
 *
 * @author gary
 */
public class TitleState extends ESAppState implements RawInputListener {
	
	Node scene;
	MaterialFloatControl fade = new MaterialFloatControl("AlphaFactor", 0f);
	Timer timer = new Timer(2f);
	
	@Override
	protected void init(Application app) {
		scene = new Node("title_scene");
		visuals.registerScene("title_scene", scene, VisualState.GUINODE);
		
		Vector2f tsize = new Vector2f();
		tsize.x = windowSize.x/2f;
		tsize.y = tsize.x/7f;
		Mesh tmesh = new Quad(tsize.x, tsize.y);
		Geometry title = new Geometry("title", tmesh);
		title.setLocalTranslation(windowSize.x/2f-tsize.x/2f, windowSize.y-tsize.y-10f, 0f);
		Material tmat = new Material(assetManager, "Shaders/Title.j3md");
		tmat.setColor("Color", ColorRGBA.White);
		tmat.setTexture("Texture", assetManager.loadTexture("Textures/forever_title_4.png"));
		tmat.setVector3("VertOffset", new Vector3f());
		tmat.setFloat("AlphaFactor", 0f);
		tmat.setTransparent(true);
		tmat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		title.setMaterial(tmat);
		title.addControl(fade);
		//title.addControl(new MaterialHueControl("Color", 0f, .001f, .3f, 1f, 255));
		scene.attachChild(title);
		
		/** Write text on the screen (HUD) */
        BitmapFont font = assetManager.loadFont("Interface/Fonts/LiberationSansNarrow.fnt");
        BitmapText text = new BitmapText(font);
		text.setSize(15f);
		text.setBox(new Rectangle(-100f, -25f, 200f, 50f));
		text.setAlignment(BitmapFont.Align.Center);
		text.setVerticalAlignment(BitmapFont.VAlign.Center);
        text.setText("press any key");
        text.setLocalTranslation(windowSize.x/2f, 70f, 0);
		//scene.attachChild(text);
		
		timer.setCycleMode(Timer.CycleMode.ONCE);
	}
	@Override
	protected void cleanup(Application app) {}
	@Override
	protected void onEnable() {
		app.getInputManager().addRawInputListener(this);
		fade.setSpeed(.01f);
		fade.setTarget(1f);
		timer.reset();
		timer.start();
	}
	@Override
	protected void onDisable() {
		//visuals.removeScene("title_scene");
		app.getInputManager().removeRawInputListener(this);
		fade.setSpeed(.01f);
		fade.setTarget(0f);
	}
	@Override
	public void update(float tpf) {
		timer.update(tpf);
	}
	
	private void beginGame(boolean force) {
		if (timer.isRunning() && !force) return;
		setEnabled(false);
		getStateManager().attach(new GameState());
	}
	
	@Override
	public void beginInput() {}
	@Override
	public void endInput() {}
	@Override
	public void onJoyAxisEvent(JoyAxisEvent evt) {
		beginGame(false);
	}
	@Override
	public void onJoyButtonEvent(JoyButtonEvent evt) {
		beginGame(false);
	}
	@Override
	public void onMouseMotionEvent(MouseMotionEvent evt) {}
	@Override
	public void onMouseButtonEvent(MouseButtonEvent evt) {
		beginGame(false);
	}
	@Override
	public void onKeyEvent(KeyInputEvent evt) {
		beginGame(false);
	}
	@Override
	public void onTouchEvent(TouchEvent evt) {}
	
}
