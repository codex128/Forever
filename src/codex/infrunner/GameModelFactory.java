/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import codex.infrunner.components.Active;
import codex.jmeutil.es.factory.ModelFactory;
import codex.jmeutil.es.factory.SimpleModelFactory;
import codex.jmeutil.es.components.Color;
import codex.jmeutil.es.components.Position;
import codex.jmeutil.es.components.Size;
import codex.jmeutil.es.components.Visual;
import codex.jmeutil.es.factory.ModelTools;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.simsilica.es.EntityId;

/**
 *
 * @author gary
 */
public class GameModelFactory extends SimpleModelFactory {
	
	public static final String
			MAIN_CHARACTER = "main_character",
			PANEL = "panel",
			BROKEN_PANEL = "broken_panel",
			RAMP = "ramp",
			HYPERSPACE = "hyperspace",
			JUMP_RING = "jump_ring",
			SUN = "sun",
			HYPERSPACE_ROD = "hyperspace_rod";
	
	public GameModelFactory() {}
	
	@Override
	protected Spatial createOtherModel(ModelTools tools) {
		switch (tools.ed.getComponent(tools.entity, Visual.class).getModel()) {
			case MAIN_CHARACTER:
				return createMainCharacter(tools);
			case PANEL:
				return createPanel(tools);
			case BROKEN_PANEL:
				return createBrokenPanel(tools);
			case RAMP:
				return createRamp(tools);
			case HYPERSPACE:
				return createHyperspace(tools);
			case JUMP_RING:
				return createJumpRing(tools);
			case SUN:
				return createSun(tools);
			case HYPERSPACE_ROD:
				return createHyperspaceRod(tools);
			default:
				return null;
		}
	}
	
	// spatials
	private Spatial createMainCharacter(ModelTools tools) {
		Size size = ModelFactory.getArgumentComponent(tools.ed, tools.entity, new Size(1f, 1f, 1f));
		Color color = ModelFactory.getArgumentComponent(tools.ed, tools.entity, new Color(ColorRGBA.randomColor()));
		float min = Math.min(Math.min(size.getSize().x, size.getSize().y), size.getSize().z);
		Mesh mesh = new Sphere(10, 10, min);
		Geometry geometry = new Geometry("character geometry", mesh);
		Material mat = new Material(tools.assetManager, "Shaders/Character.j3md");
		mat.setColor("Color", color.getColor());
		mat.setVector3("VertOffset", new Vector3f());
		geometry.setMaterial(mat);
		geometry.addControl(new VertBumpControl("VertOffset"));
		return geometry;
	}
	private Spatial createPanel(ModelTools tools) {
		Color color = ModelFactory.getArgumentComponent(tools.ed, tools.entity, new Color(ColorRGBA.randomColor()));
		Position position = ModelFactory.getArgumentComponent(tools.ed, tools.entity, new Position());
		Spatial panel = tools.assetManager.loadModel("Models/panel.j3o");
		panel.setMaterial(createPanelMaterial(tools, color.getColor()));
		Spatial geometry = ((Node)panel).getChild(0);
		geometry.addControl(new TextureScrollControl(
				"NoiseOffset", new Vector2f(FastMath.rand.nextFloat(), FastMath.rand.nextFloat()), new Vector2f(0f, .5f)));
		MaterialFloatControl alpha = new MaterialFloatControl("Alpha", 0f);
		alpha.setSpeed(GameUtils.random(.01f, .1f));
		alpha.setTarget(1f);
		alpha.setInterpolationMode(InterpolationMode.LINEAR);
		geometry.addControl(alpha);
		VertBumpControl vert = new VertBumpControl("VertOffset");
		//vert.setOffset(new Vector3f(Math.min(Math.abs(position.getPosition().x), position.getPosition().z)*Math.signum(position.getPosition().x), 0f, 0f));
		vert.setOffset(new Vector3f());
		geometry.addControl(vert);
		return panel;
	}
	private Spatial createBrokenPanel(ModelTools tools) {
		Color color = ModelFactory.getArgumentComponent(tools.ed, tools.entity, new Color(ColorRGBA.randomColor()));
		Position position = ModelFactory.getArgumentComponent(tools.ed, tools.entity, new Position());
		Spatial panel = tools.assetManager.loadModel("Models/panel.j3o");
		panel.setMaterial(createPanelMaterial(tools, color.getColor()));
		Spatial geometry = ((Node)panel).getChild(0);
		MaterialFloatControl alpha = new MaterialFloatControl("Alpha", 1f/position.getPosition().z);
		alpha.setSpeed(GameUtils.random(.01f, .2f));
		alpha.setTarget(1f);
		alpha.setInterpolationMode(InterpolationMode.LINEAR);
		geometry.addControl(alpha);
		MaterialVector3Control fall = new MaterialVector3Control("VertOffset", new Vector3f());
		geometry.addControl(fall);
		return panel;
	}
	private Spatial createRamp(ModelTools tools) {
		Size size = ModelFactory.getArgumentComponent(tools.ed, tools.entity, new Size(new Vector3f(1f, 1f, 1f)));
		Color color = ModelFactory.getArgumentComponent(tools.ed, tools.entity, new Color(ColorRGBA.randomColor()));
		Node n = new Node();
		Spatial panel = createPanel(tools);
		panel.setLocalTranslation(0f, size.getSize().z, 0f);
		panel.setLocalRotation(GameUtils.rotateFromAxisX(FastMath.atan2(-size.getSize().z, size.getSize().y)));
		panel.setLocalScale(1f, 1f, .2f*size.getSize().clone().setX(0f).length());
		panel.setMaterial(createPanelMaterial(tools, color.getColor()));
		n.attachChild(panel);
		return n;
	}
	private Spatial createHyperspace(ModelTools tools) {
		Size size = ModelFactory.getArgumentComponent(tools.ed, tools.entity, new Size(20f, 20f, 20f));
		Spatial hyper = tools.assetManager.loadModel("Models/hyperspace.j3o");
		//hyper.setLocalScale(size.getSize());
		Material m = new Material(tools.assetManager, "Shaders/Hyperspace.j3md");
		Texture tex = tools.assetManager.loadTexture("Textures/hyperspace.png");
		tex.setWrap(Texture.WrapMode.Repeat);
		m.setTexture("Texture", tex);
		m.setVector2("Offset1", new Vector2f());
		m.setVector2("Offset2", new Vector2f());
		//m.setTransparent(true);
		//hyper.setQueueBucket(RenderQueue.Bucket.Transparent);
		//m.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		m.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
		hyper.setMaterial(m);
		((Node)hyper).getChild(0).addControl(new TextureScrollControl(
				"Offset1", new Vector2f(), new Vector2f(0f, -.5f)));
		((Node)hyper).getChild(0).addControl(new TextureScrollControl(
				"Offset2", new Vector2f(.2f, .5f), new Vector2f(.01f, -.6f)));
		return hyper;
	}
	private Spatial createJumpRing(ModelTools tools) {
		Size size = ModelFactory.getArgumentComponent(tools.ed, tools.entity, new Size(1f, 1f, 0f));
		Node n = new Node("jump_ring");
		Quad mesh = new Quad(size.getSize().x, size.getSize().y);
		Geometry g = new Geometry("quad", mesh);
		g.move(-size.getSize().x/2f, 0f, -size.getSize().y/2f);
		g.rotate(FastMath.HALF_PI, 0f, 0f);
		Material m = new Material(tools.assetManager, "Shaders/SimpleFade.j3md");
		m.setFloat("Alpha", 1f);
		m.setTexture("Texture", tools.assetManager.loadTexture("Textures/ripple.png"));
		m.setTransparent(true);
		m.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		m.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
		g.setMaterial(m);
		g.setQueueBucket(RenderQueue.Bucket.Transparent);
		n.attachChild(g);
		n.addControl(new TransformAnimControl().setScale(1.05f));
		g.addControl(new MaterialFloatControl("Alpha", .25f, -.01f));
		return n;
	}
	private Spatial createSun(ModelTools tools) {
		Size size = ModelFactory.getArgumentComponent(tools.ed, tools.entity, new Size(1f, 1f, 1f));
		Node n = new Node("sun");
		Mesh mesh = new Quad(size.getSize().x, size.getSize().y);
		Geometry sun = new Geometry("sun", mesh);
		sun.setLocalTranslation(-size.getSize().x/2, -size.getSize().y/2, 0f);
		Material mat = new Material(tools.assetManager, "Shaders/Sun.j3md");
		mat.setColor("Color", ColorRGBA.Yellow);
		Texture tex = tools.assetManager.loadTexture("Textures/distant_sun.png");
		mat.setTexture("BaseTexture", tex);
		mat.setTexture("OverlayTexture", tex);
		mat.setFloat("Alpha", 1f);
		mat.setFloat("Theta", 0f);
		mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Front);
		mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		mat.setTransparent(true);
		sun.setMaterial(mat);
		sun.setQueueBucket(RenderQueue.Bucket.Transparent);
		sun.addControl(new MaterialFloatControl("Theta", 0f, .01f));
		n.attachChild(sun);
		return n;
	}
	private Spatial createHyperspaceRod(ModelTools tools) {
		Color color = ModelFactory.getArgumentComponent(tools.ed, tools.entity, new Color(ColorRGBA.White));
		Spatial rod = tools.assetManager.loadModel("Models/hyperspace_element.j3o");
		Material mat = new Material(tools.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color.getColor());
		rod.setMaterial(mat);
		return rod;
	}
	
	// materials
	private Material createPanelMaterial(ModelTools tools, ColorRGBA color) {
		Active active = ModelFactory.getArgumentComponent(tools.ed, tools.entity, new Active(true));
		Material material = new Material(tools.assetManager, "Shaders/Panel.j3md");
		material.setColor("BaseColor", color);
		material.setTexture("AlphaMap", tools.assetManager.loadTexture("Textures/PanelTexture.png"));
		Texture noise = tools.assetManager.loadTexture("Textures/noise.png");
		noise.setWrap(Texture.WrapMode.Repeat);
		if (active.isActive()) {
			material.setTexture("NoiseMap", noise);
			material.setVector2("NoiseOffset", new Vector2f(0f, 0f));
			material.setFloat("NoiseFactor", .18f);
		}
		material.setVector3("VertOffset", new Vector3f());
		material.setFloat("Alpha", 1f);
		material.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
		return material;
	}
	
}
