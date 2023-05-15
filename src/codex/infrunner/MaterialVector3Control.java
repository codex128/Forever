/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import codex.jmeutil.control.GeometryControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 *
 * @author gary
 */
public class MaterialVector3Control extends GeometryControl {

	final String param;
	Vector3f vec;
	Vector3f velocity = new Vector3f();
	Vector3f force = new Vector3f();
	Vector3f target;
	InterpolationMode mode = InterpolationMode.LINEAR;
	
	public MaterialVector3Control(String param, Vector3f vec) {
		this.param = param;
		this.vec = vec;
	}
	public MaterialVector3Control(String param, Vector3f vec, Vector3f force) {
		this.param = param;
		this.vec = vec;
		this.force.set(force);
	}
	
	@Override
	protected void controlUpdate(float tpf) {
		if (null != mode) switch (mode) {
			case LINEAR:
				linearMotion(); break;
			case LERP:
				lerpMotion(); break;
			case ACCELERATE:
				accelerate(); break;
		}
		geometry.getMaterial().setVector3(param, vec);
	}
	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {}
	
	protected void linearMotion() {
		if (target != null) {
			if (vec.distanceSquared(target) < force.lengthSquared()) {
				vec.set(target);
			}
			else {
				vec.addLocal(target.subtract(vec).normalizeLocal().multLocal(force.length()));
			}
		}
		else {
			vec.addLocal(force);
		}
	}
	protected void lerpMotion() {
		if (target == null) return;
		vec.addLocal(target.subtract(vec).multLocal(force.length()));
	}
	protected void accelerate() {
		velocity.addLocal(force);
		if (target != null) {
			if (vec.distanceSquared(target) < velocity.lengthSquared()) {
				vec.set(target);
			}
			else {
				vec.addLocal(target.subtract(vec).normalizeLocal().multLocal(velocity.length()));
			}
		}
		else {
			vec.addLocal(velocity);
		}
	}
	
	public void setVector(Vector3f vec) {
		this.vec.set(vec);
	}
	public void setVelocity(Vector3f velocity) {
		this.velocity.set(velocity);
	}
	public void setForce(Vector3f force) {
		this.force.set(force);
	}
	public void setTarget(Vector3f target) {
		this.target = target;
	}
	public void setInterpolationMode(InterpolationMode mode) {
		this.mode = mode;
	}
	
	public String getControlledParam() {
		return param;
	}
	public Vector3f getVector() {
		return vec;
	}
	public Vector3f getVelocity() {
		return velocity;
	}
	public Vector3f getForce() {
		return force;
	}
	public Vector3f getTarget() {
		return target;
	}
	public InterpolationMode getInterpolationMode() {
		return mode;
	}
	
}
