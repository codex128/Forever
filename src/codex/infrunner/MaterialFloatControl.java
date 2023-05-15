/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import codex.jmeutil.control.GeometryControl;
import com.jme3.math.FastMath;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 *
 * @author gary
 */
public class MaterialFloatControl extends GeometryControl {
	
	String param;
	float value;
	float speed;
	Float target;
	InterpolationMode mode = InterpolationMode.LINEAR;
	
	public MaterialFloatControl(String param) {
		this.param = param;
	}
	public MaterialFloatControl(String param, float value) {
		this.param = param;
		this.value = value;
	}
	public MaterialFloatControl(String param, float value, float speed) {
		this.param = param;
		this.value = value;
		this.speed = speed;
	}
	
	@Override
	protected void controlUpdate(float tpf) {
		if (mode == InterpolationMode.LINEAR) {
			if (target != null) {
				if (FastMath.abs(target-value) < speed) {
					value = target;
				}
				else {
					value += speed*Math.signum(target-value);
				}
			}
			else {
				value += speed;
			}
		}
		else if (mode == InterpolationMode.LERP && target != null) {
			value += (target-value)*speed;
		}
		geometry.getMaterial().setFloat(param, value);
	}
	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {}
	
	public void setValue(float value) {
		this.value = value;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public void setTarget(Float target) {
		this.target = target;
	}
	public void setInterpolationMode(InterpolationMode mode) {
		this.mode = mode;
	}	

	public String getParam() {
		return param;
	}
	public float getValue() {
		return value;
	}
	public float getSpeed() {
		return speed;
	}
	public Float getTarget() {
		return target;
	}
	public InterpolationMode getInterpolationMode() {
		return mode;
	}
	
}
