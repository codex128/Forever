/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import codex.infrunner.components.Decay;
import codex.infrunner.components.Movement;
import codex.jmeutil.Motion;
import codex.jmeutil.character.OrbitalCamera;
import codex.jmeutil.es.ESAppState;
import codex.jmeutil.es.VisualState;
import codex.jmeutil.es.components.Position;
import codex.jmeutil.es.components.Size;
import codex.jmeutil.es.components.Visual;
import codex.jmeutil.listen.Listenable;
import codex.jmeutil.math.FDomain;
import com.jme3.app.Application;
import com.jme3.collision.CollisionResult;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.simsilica.es.EntityId;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.AnalogFunctionListener;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author gary
 */
public class RunnerAppState extends ESAppState implements AnalogFunctionListener,
		Listenable<RunnerStateListener>, SceneGraphVisitor {
	
	public static final Vector3f
			START_POSITION = new Vector3f(0f, 1f, 0f);
	
	EntityId runner;
	VertBumpControl bump;
	OrbitalCamera camera;
	FDomain lookahead = new FDomain(0f, 5f);
	Node collisionscene;
	Vector2f inputdirection = new Vector2f();
	float forwardspeed = SceneGeneratorState.PANEL_SIZE.y*SceneGeneratorState.BPS*2f;
	float acceleration = .01f;
	float damping = .025f;
	float terminal = .16f;
	final int maxjumps = 1;
	boolean jump = false;
	float jumpforce = .11f;
	float gravity = .007f;
	LinkedList<RunnerStateListener> listeners = new LinkedList<>();
	
	public RunnerAppState(EntityId runner) {
		this.runner = runner;
	}
	public RunnerAppState(EntityId runner, Node collisionscene) {
		this(runner);
		this.collisionscene = collisionscene;
	}
	
	@Override
	protected void init(Application app) {
		if (collisionscene == null) {
			collisionscene = visuals.getScene(VisualState.ROOTNODE);
		}
		
		app.getInputManager().setCursorVisible(false);
		camera = new OrbitalCamera(app.getCamera());
		camera.setHorizontalAngle(FastMath.PI);
		camera.setVerticleAngle(FastMath.PI/6f);
		camera.setFocusOffset(new Vector3f(0f, 1f, 5f));
		camera.getDistanceDomain().set(10f, 10f);
		camera.getVerticleAngleDomain().set(0f, FastMath.PI*1.95f);
		camera.getVerticleAngleDomain().set(FastMath.PI/6f, FastMath.PI/2f);
		camera.setMotion(Motion.LERP);
		camera.setCameraSpeed(.07f);
		System.out.println(visuals.getSpatial(runner));
		visuals.getSpatial(runner).addControl(camera);
		camera.setCameraLocation(camera.calculateIdealCameraPosition());
		
		visuals.getSpatial(runner).breadthFirstTraversal(this);
		
		InputMapper im = GuiGlobals.getInstance().getInputMapper();
		im.activateGroup(Functions.RUNNER_GROUP);
		im.addAnalogListener(this, Functions.F_STRAFE, Functions.F_JUMP, Functions.F_DEV_FLOAT);
	}
	@Override
	protected void cleanup(Application app) {
		ed.removeEntity(runner);
		camera.getSpatial().removeControl(camera);
		InputMapper im = GuiGlobals.getInstance().getInputMapper();
		im.deactivateGroup(Functions.RUNNER_GROUP);
		im.removeAnalogListener(this, Functions.F_STRAFE, Functions.F_JUMP, Functions.F_DEV_FLOAT);
	}
	@Override
	protected void onEnable() {}
	@Override
	protected void onDisable() {}
	@Override
	public void update(float tpf) {
		Vector3f velocity = ed.getComponent(runner, Movement.class).getMovement();
		applyXAxis(velocity);
		applyYAxis(velocity);
		camera.setVerticleAngle(-velocity.y*FastMath.PI);
		camera.setFocusOffset(camera.getFocusOffset().add(new Vector3f(0f, 1f, lookahead.applyConstrain(5f+velocity.y*5f)).subtractLocal(camera.getFocusOffset()).multLocal(.5f)));
		ed.setComponent(runner, new Movement(velocity));
		inputdirection.set(0f, 0f);
	}
	
	private void applyXAxis(Vector3f velocity) {
		if (inputdirection.x != 0f) {
			// accellerate
			velocity.x += acceleration*inputdirection.x;
		}
		else if (FastMath.abs(velocity.x) > damping) {
			// decellerate
			velocity.x -= damping*FastMath.sign(velocity.x);
		}
		else {
			// stop
			velocity.x = 0f;
		}
		// limit velocity
		velocity.x = Math.min(Math.max(velocity.x, -terminal), terminal);
	}
	private void applyYAxis(Vector3f velocity) {
		if (inputdirection.y > 0f && (jump || (inputdirection.y >= 2f && Main.DEV_MODE))) {
			// jump
			velocity.y = jumpforce;
			EntityId ring = ed.createEntity();
			ed.setComponents(ring,
					new Position(ed.getComponent(runner, Position.class).getPosition()
							.add(0f, -ed.getComponent(runner, Size.class).getSize().y+.01f, 0f)),
					new Size(1.1f, 1.1f, 0f),
					new Decay(1f),
					new Visual(GameModelFactory.JUMP_RING));
			jump = false;
		}
		else {
			// fall
			velocity.y = avoid(velocity.y-gravity, 0f, -.00001f);
//			velocity.y = 0f;
		}
		// collision
		//if (velocity.y == 0f) return;
		Vector3f position = ed.getComponent(runner, Position.class).getPosition();
		Vector3f size = ed.getComponent(runner, Size.class).getSize();
		Vector3f direction = new Vector3f(0f, -1f, 0f);
		float distance = size.y+Math.abs(velocity.y);
		CollisionResult[] results = {
			VisualState.castRay(position.add(size.x, 0f, size.z), direction, distance, collisionscene),
			VisualState.castRay(position.add(-size.x, 0f, size.z), direction, distance, collisionscene),
			VisualState.castRay(position.add(size.x, 0f, -size.z), direction, distance, collisionscene),
			VisualState.castRay(position.add(-size.x, 0f, -size.z), direction, distance, collisionscene),
		};
		Vector3f vel = new Vector3f(velocity);
		CollisionResult closest = null;
		for (CollisionResult res : results) {
			if (res == null) continue;
			if (closest == null || res.getDistance() < closest.getDistance()) {
				closest = res;
			}
			if (vel.y < -gravity*10) {
				VertBumpControl control = res.getGeometry().getControl(VertBumpControl.class);
				Vector3f offset = new Vector3f(0f, 2f*vel.y, 0f);
				if (control != null) {
					control.setOffset(offset);
				}
				if (bump != null) {
					bump.setOffset(offset);
				}
			}
		}
		if (closest != null) {
			if (closest.getContactNormal().z < 0f) {
				velocity.y = Math.max(Math.min(velocity.y-closest.getContactNormal().z*.1f, -closest.getContactNormal().z*.275f), 0f);
				jump = true;
				position.y = closest.getContactPoint().y+size.y;
				ed.setComponent(runner, new Position(position));
			}
			else if (vel.y <= 0f) {
				velocity.y = 0f;
				jump = true;
				position.y = closest.getContactPoint().y+size.y;
				ed.setComponent(runner, new Position(position));
			}
		}
		ed.setComponent(runner, new Movement(velocity));
	}

	@Override
	public void valueActive(FunctionId func, double value, double tpf) {
		if (func == Functions.F_STRAFE) {
			inputdirection.x = -(float)value;
		}
		else if (func == Functions.F_JUMP) {
			inputdirection.y = 1f;
		}
		else if (func == Functions.F_DEV_FLOAT && Main.DEV_MODE) {
			inputdirection.y = 2f;
		}
	}
	@Override
	public Collection<RunnerStateListener> getListeners() {
		return listeners;
	}
	@Override
	public void visit(Spatial spatial) {
		VertBumpControl c = spatial.getControl(VertBumpControl.class);
		if (c != null) bump = c;
	}
	
	public EntityId getRunnerId() {
		return runner;
	}
	public float getForwardSpeed() {
		return forwardspeed;
	}
	public OrbitalCamera getCamera() {
		return camera;
	}
	
	private static float avoid(float value, float avoid, float padding) {
		if (value == avoid) return avoid+padding;
		else return value;
	}
	
}
